drop schema amigo_wallet;
create schema amigo_wallet;
use amigo_wallet;

create table wallet(
    wallet_id integer primary key,
    email varchar(50) unique,
    balance double,
    user_id integer unique
);
insert into wallet values(101, "bpcl@merch.in", 0.0, null);
insert into wallet values(102, "jio@merch.in", 0.0, null);
insert into wallet values(103, "nmdc@merch.in", 0.0, null);
insert into wallet values(104, "lic@merch.in", 0.0, null);
insert into wallet values(111, "abhi@cust.in", 900, 221);
insert into wallet values(112, "spid@cust.in", 1200, 222);
insert into wallet values(113, "dead@cust.in", 100, 223);
select * from wallet;


create table bank(
    acc_num integer primary key,
    ifsc varchar(50),
    acc_holder_name varchar(50),
    balance double,
    user_id integer unique
);
insert into bank values(111111, "AXIS1234567", "Abhishek Malviya", 1000, 221);
insert into bank values(111112, "AXIS1234567", "Spider Man", 1000, 222);
insert into bank values(111113, "AXIS1234567", "Dead Pool", 1000, 223);


create table merchant(
	id integer primary key,
    name varchar(50),
    utilities varchar(100),
    email_id varchar(50)
);
insert into merchant values(1, "BPCL", "Gas", "bpcl@merch.in");
insert into merchant values(2, "Jio", "Mobile::TV::Broadband", "jio@merch.in");
insert into merchant values(3, "NMDC", "Water::Electricity", "nmdc@merch.in");
insert into merchant values(4, "LIC", "LifeInsurance::HealthInsurance", "lic@merch.in");



create table walgen(
    next_val BIGINT primary key
);
insert into walgen values(1000);


create table accgen(
    next_val BIGINT primary key
);
insert into accgen values(900001);



drop schema amigo_customer;
create schema amigo_customer;
use amigo_customer;

create table customer(
    id integer primary key,
    first_name varchar(50),
    last_name varchar(50),
    is_admin boolean,
    region varchar(20),
    email varchar(50) unique,
    bank_account BIGINT unique,
    wallet_id integer unique,
    password varchar(50)
);
insert into customer values(221, "Abhishek", "Malviya", true, "IND", "abhi@cust.in", 111111, 111, "12345");
insert into customer values(222, "Spider", "Man", false, "US", "spid@cust.in", 111112, 112, "abcde");
insert into customer values(223, "Dead", "Pool", false, "US", "dead@cust.in", 111113, 113, "abcde");

create table custgen(
    next_val BIGINT primary key
);
insert into custgen values(500);


-- offerMS schema and data
drop schema amigo_offer;
create schema amigo_offer;
use amigo_offer;

create table offer(
	id integer primary key,
    offer_code varchar(50) unique,
    info varchar(100),
    percentage_off double
);
insert into offer values(1, "FLAT35", "Get 35% off on bill payments",  35.0);
insert into offer values(2, "FLAT11", "Get 11% off on bill payments",  11.0);

create table offgen(
    next_val BIGINT primary key
);
insert into offgen values(100);
