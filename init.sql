CREATE DATABASE library_management;
USE library_management;


CREATE TABLE users (
    Name varchar(20) NOT NULL,
    Password varchar(20) NOT NULL,
    Access_token varchar(10),
    TransactionID varchar(20),
    Is_logon bit NOT NULL DEFAULT b'0'
);

CREATE TABLE books (
    ID int PRIMARY KEY AUTO_INCREMENT,
    Title varchar(100) NOT NULL UNIQUE,
    Author varchar(100) NOT NULL,
    Publisher varchar(100) NOT NULL,
    Year int NOT NULL,
    Available bit NOT NULL DEFAULT b'1'
);

CREATE TABLE transactions (
    Access_token varchar(10) NOT NULL,
    TransactionID varchar(20) NOT NULL,
    Action varchar(255)
);

