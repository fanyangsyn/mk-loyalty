CREATE TABLE purchase (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    reference_number VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    purchased_at TIMESTAMP NOT NULL,
    refunded_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_purchase_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE INDEX idx_purchase_customer_id ON purchase(customer_id);
CREATE INDEX idx_purchase_reference ON purchase(reference_number);
CREATE INDEX idx_purchase_customer_status_date ON purchase(customer_id, status, purchased_at);
