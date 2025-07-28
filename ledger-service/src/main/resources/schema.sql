CREATE TABLE ledger_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    reference_id BIGINT NOT NULL,
    reference_type VARCHAR(50),
    order_id VARCHAR(255),
    idempotency_key VARCHAR(255) UNIQUE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE ledger_entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(15, 2) NOT NULL,
    account_id BIGINT NOT NULL,
    transaction_id BIGINT NOT NULL,
    type ENUM('CREDIT', 'DEBIT') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES ledger_transaction(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

INSERT INTO accounts (name) VALUES ('REVENUE');
INSERT INTO accounts (name) VALUES ('ITEM_BUYER');

DELIMITER $$

CREATE TRIGGER check_balance_after_insert
AFTER INSERT ON ledger_entries
FOR EACH ROW
BEGIN
    DECLARE credit_sum DECIMAL(15,2);
    DECLARE debit_sum DECIMAL(15,2);

    SELECT SUM(amount) INTO credit_sum
    FROM ledger_entries
    WHERE transaction_id = NEW.transaction_id AND type = 'CREDIT';

    SELECT SUM(amount) INTO debit_sum
    FROM ledger_entries
    WHERE transaction_id = NEW.transaction_id AND type = 'DEBIT';

    IF NOT (credit_sum = debit_sum) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'The sum of CREDIT and DEBIT amounts does not balance to zero.';
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER prevent_update_ledger_entries
BEFORE UPDATE ON ledger_entries
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Updates are not allowed on this table.';
END $$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER prevent_delete_ledger_entries
BEFORE DELETE ON ledger_entries
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Delete are not allowed on this table.';
END $$

DELIMITER ;