package com.ab.amigo.service;

import com.ab.amigo.controller.clients.OfferFeignClient;
import com.ab.amigo.dto.*;
import com.ab.amigo.entity.Merchant;
import com.ab.amigo.entity.Transaction;
import com.ab.amigo.entity.Wallet;
import com.ab.amigo.exception.Error;
import com.ab.amigo.exception.TransactionFailedException;
import com.ab.amigo.repository.MerchantRepository;
import com.ab.amigo.repository.TransactionRepository;
import com.ab.amigo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    BankService bankService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    OfferFeignClient offerFeignClient;

    private static final String TRANSACTION_FAILED = "Transaction Failed";


    public ResponseDTO<WalletDTO> creatNewWallet(Integer userId, String email) {
        var newWallet = new Wallet(userId, email, 0.0);

        return new ResponseDTO<>(walletRepository.save(newWallet).value());
    }

    /**
     * Top-Up Wallet <br>
     * Top-up wallet with some amount (minimum 100 USD) <br>
     * from a bank account (Hint: Keep Banking details in a table)
     */
    public ResponseDTO<WalletDTO> rechargeWallet(LoadWalletDTO loadWalletDTO) {

        if (loadWalletDTO.rechargeAmount() < 100) {
            throw new TransactionFailedException("Minimum recharge amount is $100");
        }

        var wallet = getWallet(loadWalletDTO.walletId());
        var bankResponse = bankService.deductMoneyFromBank(loadWalletDTO.bankAccount(), loadWalletDTO.rechargeAmount());

        if (bankResponse.hasError())
            throw new TransactionFailedException(bankResponse.errors().stream()
                    .map(Error::message)
                    .collect(Collectors.joining(",")));

        startCreditTransaction(wallet, loadWalletDTO.rechargeAmount());

        return new ResponseDTO<>(new WalletDTO(wallet.getWalletId(), wallet.getEmail(), wallet.getBalance()));
    }

    /**
     * View the wallet transactions done so that customers can keep track of all the transactions.
     * <ul>
     *     <li>Customer should be able to see the transactions made using the wallet.</li>
     *     <li>By default, all the transactions done by the customer should be displayed.</li>
     *     <li>The transaction should be displayed in tabular form. It should contain Transaction ID, Transaction Date & time, Info, Amount credit/debit, and Status</li>
     *     <li>Pagination should be used and only 5 records should be displayed per page.</li>
     * </ul>
     */
    public ResponseDTO<Page<Transaction>> getAllTransaction(Integer walletId, int offSet) {
        var pageable = PageRequest.of(offSet, 5);
        return new ResponseDTO<>(transactionRepository.findAllByWalletId(walletId, pageable));
    }

    public ResponseDTO<List<MerchantDTO>> getALlMerchant() {
        return new ResponseDTO<>(
                merchantRepository.findAll().stream()
                        .map(Merchant::value)
                        .toList());
    }

    /**
     * Transfer money from their wallets to their bank accounts
     * <ol>
     *      <li>Validate that the entered details (Bank code, account number, account holder name, amount) are based on the following conditions:
     *      <ul>
     *          <li>Bank code should start with 4 alphabets followed by 6 to 8 digits</li>
     *          <li>Account number should only contain digits</li>
     *          <li>Account holder’s name should contain only alphabets and spaces</li>
     *          <li>Amount should be greater than 0 and can contain 2 digits after the decimal</li>
     *      </ul>
     *      </li>
     *      <li>On successful validation, verify that the customer has sufficient balance in the wallet.</li>
     *      <li>On successful verification, account details should be verified (Hint: Use banking table to verify account details)</li>
     *      <li>On successful verification, amount gets debited from the wallet and credited to the bank account.</li>
     * </ol>
     *
     * @return the success message.
     * In case of any error or exception, an appropriate error message.
     */
    @Transactional
    public ResponseDTO<String> sendMoneyToBank(WalletToBankTransferDTO transferDTO) {

        // verify the bank details
        var response = bankService.verifyBankDetails(transferDTO.bankDTO());
        if (response.hasError()) {
            throw new TransactionFailedException(TRANSACTION_FAILED + "::" + ResponseDTO.getErrorMsg(response.errors()));
        }

        // verify that user has sufficient balance in wallet
        var wallet = getWallet(transferDTO.walletId());
        verifySufficientBal(wallet, transferDTO.amount().doubleValue());

        //deduct money form wallet
        var debit = startDeductionTransaction(wallet, transferDTO.amount().doubleValue());
        if (debit.hasFailed())
            throw new TransactionFailedException(TRANSACTION_FAILED);

        // send money to bank
        bankService.addMoneyToBank(transferDTO.bankDTO().accNum(), transferDTO.amount().doubleValue());

        return new ResponseDTO<>("Money Transferred Successfully");
    }

    /**
     * Transfer money from their wallet to the other customers wallet
     * <ol>
     *      <li>Validate the entered details (e-mail ID and amount) based on the following conditions:
     *      <ul>
     *          <li>e-mail ID should follow a valid format</li>
     *          <li>amount should be greater than 0 and can contain 2 digits after the decimal point</li>
     *      </ul>
     *      </li>
     *      <li>On successful validation, verify that the customer has sufficient balance in the wallet.</li>
     *      <li>On successful verification, the amount should be debited from the wallet of the transferor and credited to the wallet of the transferee</li>
     * </ol>
     *
     * @return the success message.
     * In case of any error or exception, an appropriate error message.
     */
    public ResponseDTO<String> sendMoneyToWallet(WalletToWalletTransferDTO transferDTO) {
        var senderWallet = getWallet(transferDTO.walletId());
        var reciverWallet = getWallet((transferDTO.receiverEmailId()));

        // verify that user has sufficient balance in wallet
        verifySufficientBal(senderWallet, transferDTO.amount().doubleValue());

        //deduct money form wallet
        var deductionTransaction = startDeductionTransaction(senderWallet, transferDTO.amount().doubleValue());
        if (deductionTransaction.hasFailed())
            throw new TransactionFailedException(TRANSACTION_FAILED + "::" + deductionTransaction.getInfo());

        // add money to receivers wallet
        var creditTransaction = startCreditTransaction(reciverWallet, transferDTO.amount().doubleValue());
        if (creditTransaction.hasFailed())
            throw new TransactionFailedException(TRANSACTION_FAILED + "::" + creditTransaction.getInfo());

        return new ResponseDTO<>("Money Transferred Successfully");
    }

    public ResponseDTO<BillDTO> generateBill(String merchant, String utility) {
        return new ResponseDTO<>(
                new BillDTO(
                        merchant,
                        utility,
                        BigDecimal.valueOf(50 + Double.parseDouble(new DecimalFormat("#.##").format(Math.random() * 200)))
                )
        );
    }

    /**
     * Customers should be able to pay various bills (electricity, mobile, water, DTH, broadband, gas, and insurance) <br>
     * <ul>
     *     <li>Customer should be able to select the merchant and the utility type from the table (Hint: Keep Merchant details in a table)</li>
     *     <li>Generate the amount to be paid for the bill after the customer makes his/her selection by making a service call (stubbed code). This amount should be randomly generated using values in the range (50-200 USD).</li>
     *     <li>Verify that the customer has sufficient balance.</li>
     *     <li>On successful verification, the amount should be debited from the customer’s wallet and credited to the merchant’s wallet and a success message should be displayed. The message should also display the reward points earned (if any). Reward points should be calculated by calling the appropriate method.</li>
     * </ul>
     *
     * @return Success msg. In case of any error or exception, an appropriate error message should be displayed
     */
    public ResponseDTO<String> payBill(Integer userId, BillDTO billDTO, String offerCode) {
        // get customer wallet and verify balance
        var wallet = getWallet(userId);
        verifySufficientBal(wallet, billDTO.amount().doubleValue());

        // get merchant wallet
        var merchant = getMerchant(billDTO.merchant());

        // transfer money to merchant wallet
        var paymentResponse = sendMoneyToWallet(new WalletToWalletTransferDTO(wallet.getWalletId(), merchant.getEmailId(), billDTO.amount()));

        // get cashback if valid offer is provided
        String cashBackResponse = "NO CASHBACK RECEIVED";
        if (StringUtils.hasText(offerCode)) {
            var cashBack = calculateCashBack(billDTO.amount().doubleValue(), offerCode);
            if (cashBack > 0) {
                startCreditTransaction(wallet, cashBack);
                cashBackResponse = cashBack + " RECEIVED AS CASHBACK";
            } else {
                cashBackResponse = "Invalid Offer Code";
            }
        }

        return new ResponseDTO<>(paymentResponse.response() + "::" + cashBackResponse, paymentResponse.errors());
    }

    private double calculateCashBack(double amount, String offerCode) {
        var offerResponse = offerFeignClient.getOffer(offerCode);
        if (offerResponse.hasError())
            return 0;

        double rawCashback = (amount * offerResponse.response().percentOff().doubleValue()) / 100;
        return Double.parseDouble(new DecimalFormat("#.##").format(rawCashback));
    }

    private Wallet getWallet(Integer walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new NoSuchElementException("Wallet does not exist"));
    }

    private Wallet getWallet(String email) {
        return walletRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Wallet does not exist"));
    }

    private void verifySufficientBal(Wallet wallet, Double amount) {
        if (wallet.getBalance() < amount)
            throw new IllegalArgumentException("Wallet balance insufficient");
    }

    private Transaction startCreditTransaction(Wallet wallet, double amount) {
        var transaction = new Transaction();
        transaction.setTimestamp(Timestamp.from(Instant.now()));
        transaction.setStatus("INI");
        transaction.setWalletId(wallet.getWalletId());

        try {
            addMoney(wallet, amount, transaction);
        } catch (Exception ex) {
            transaction.setInfo(ex.getMessage());
            transaction.setStatus("FLD");
        }

        return transactionRepository.save(transaction);
    }

    private Transaction startDeductionTransaction(Wallet wallet, double amount) {
        var transaction = new Transaction();
        transaction.setTimestamp(Timestamp.from(Instant.now()));
        transaction.setStatus("INI");
        transaction.setWalletId(wallet.getWalletId());

        try {
            deductMoney(wallet, amount, transaction);
        } catch (Exception ex) {
            transaction.setInfo(ex.getMessage());
            transaction.setStatus("FLD");
        }

        return transactionRepository.save(transaction);
    }

    private void addMoney(Wallet wallet, double amount, Transaction transaction) {
        transaction.setTransactionType("Credit");
        transaction.setAmount(amount);
        transaction.setInfo("Money credited to wallet");

        var curBal = wallet.getBalance();
        wallet.setBalance(curBal + amount);

        //manually falling transaction
        if (amount == 404) {
            throw new TransactionFailedException("credit transaction failed manually");
        }

        walletRepository.save(wallet);

        transaction.setStatus("SCS");
    }

    private void deductMoney(Wallet wallet, double amount, Transaction transaction) {
        transaction.setTransactionType("Debit");
        transaction.setAmount(amount);
        transaction.setInfo("Money debited from wallet");

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);

        transaction.setStatus("SCS");
    }

    private Merchant getMerchant(String name) {
        return merchantRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Merchant does not exist"));
    }
}
