CREATE TABLE point_ledger (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    purchase_id BIGINT,
    points INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    points_remaining INT NOT NULL DEFAULT 0,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ledger_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_ledger_purchase FOREIGN KEY (purchase_id) REFERENCES purchase(id)
);

CREATE INDEX idx_ledger_customer_type_expiry ON point_ledger(customer_id, type, expires_at);
CREATE INDEX idx_ledger_customer_remaining ON point_ledger(customer_id, points_remaining);
CREATE INDEX idx_ledger_purchase ON point_ledger(purchase_id);
