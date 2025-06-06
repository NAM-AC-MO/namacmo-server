CREATE TABLE payment_events (
    id VARCHAR(255) PRIMARY KEY,
    buyer_id VARCHAR(255) NOT NULL,
    is_payment_done BOOLEAN NOT NULL DEFAULT FALSE,
    payment_key VARCHAR(255) UNIQUE,
    order_id VARCHAR(255) UNIQUE,
    type ENUM('TOSS', 'KAKAO_PAY', 'KB_CARD', 'SINHAN_CARD', 'PAYCO', 'KB_PAY', 'NAVER_PAY') NOT NULL,
    order_name VARCHAR(255) NOT NULL,
    method ENUM('EASY_PAY', 'CARD', 'OTHER'),
    psp_raw_data JSON,
    approved_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment_orders (
    id VARCHAR(255) PRIMARY KEY,
    payment_event_id VARCHAR(255) NOT NULL,
    seller_id VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    order_id VARCHAR(255) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    payment_order_status ENUM('NOT_STARTED', 'EXECUTING', 'SUCCESS', 'FAILURE', 'UNKNOWN') NOT NULL DEFAULT 'NOT_STARTED',
    ledger_updated BOOLEAN NOT NULL DEFAULT FALSE,
    wallet_updated BOOLEAN NOT NULL DEFAULT FALSE,
    failed_count TINYINT NOT NULL DEFAULT 0,
    threshold TINYINT NOT NULL DEFAULT 5,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (payment_event_id) REFERENCES payment_events(id)
);

CREATE TABLE payment_order_histories (
    id VARCHAR(255) PRIMARY KEY,
    payment_order_id VARCHAR(255) NOT NULL,
    previous_status ENUM('NOT_STARTED', 'EXECUTING', 'SUCCESS', 'FAILURE', 'UNKNOWN'),
    new_status ENUM('NOT_STARTED', 'EXECUTING', 'SUCCESS', 'FAILURE', 'UNKNOWN'),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(255),
    reason VARCHAR(255),

    FOREIGN KEY (payment_order_id) REFERENCES payment_orders(id)
);
