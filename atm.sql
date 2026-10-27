CREATE DATABASE atm_db;

USE atm_db;

CREATE TABLE accounts (
    account_number INT PRIMARY KEY,
    account_holder VARCHAR(50),
    pin INT,
    balance DOUBLE
);

INSERT INTO accounts VALUES
(1001, 'Ayush', 1234, 5000.00),
(1002, 'Rahul', 5678, 10000.00);

SELECT * FROM accounts;