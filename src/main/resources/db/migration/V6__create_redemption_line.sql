CREATE TABLE redemption_line (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    redemption_id BIGINT NOT NULL,
    point_ledger_id BIGINT NOT NULL,
    points_used INT NOT NULL,
    CONSTRAINT fk_rline_redemption FOREIGN KEY (redemption_id) REFERENCES redemption(id),
    CONSTRAINT fk_rline_ledger FOREIGN KEY (point_ledger_id) REFERENCES point_ledger(id)
);

CREATE INDEX idx_rline_redemption ON redemption_line(redemption_id);
CREATE INDEX idx_rline_ledger ON redemption_line(point_ledger_id);
