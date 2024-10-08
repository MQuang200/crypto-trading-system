CREATE TABLE USERS (
    ID INT PRIMARY KEY,
    USERNAME VARCHAR(50) NOT NULL,
    PASSWORD VARCHAR(50) NOT NULL
);

CREATE TABLE CURRENCIES (
    ID INT PRIMARY KEY,
    NAME VARCHAR(50) NOT NULL,
    SYMBOL VARCHAR(50) NOT NULL
);

CREATE TABLE ASSETS (
    ID INT GENERATED ALWAYS AS IDENTITY,
    CURRENCY_ID INT NOT NULL,
    USER_ID INT NOT NULL,
    BALANCE FLOAT(24) DEFAULT 0,
    PRIMARY KEY (USER_ID, CURRENCY_ID),
    FOREIGN KEY (CURRENCY_ID) REFERENCES CURRENCIES(ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);

CREATE TABLE TRANSACTIONS (
    ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    USER_ID INT NOT NULL,
    CURRENCY_PAIR VARCHAR(50) NOT NULL,
    AMOUNT FLOAT(24) NOT NULL,
    TYPE VARCHAR(50) NOT NULL,
    PRICE FLOAT(24) NOT NULL,
    TOTAL FLOAT(24) NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);

CREATE TABLE PRICES (
    ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    CURRENCY_PAIR VARCHAR(50) NOT NULL,
    BID_PRICE FLOAT(24) NOT NULL,
    ASK_PRICE FLOAT(24) NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL
);