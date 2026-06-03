CREATE TABLE redemption (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    reward_id BIGINT NOT NULL,
    points_spent INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_redemption_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_redemption_reward FOREIGN KEY (reward_id) REFERENCES reward_catalog(id)
);

CREATE INDEX idx_redemption_customer ON redemption(customer_id);
