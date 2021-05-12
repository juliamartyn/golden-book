CREATE TABLE IF NOT EXISTS favorites (
    id INT NOT NULL AUTO_INCREMENT,
    entity_id INT NULL DEFAULT NULL,
    type VARCHAR(255) NULL DEFAULT NULL,
    customer_id BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX FK_favorites_users (customer_id ASC) VISIBLE,
    CONSTRAINT FK_favorites_users FOREIGN KEY (customer_id) REFERENCES users (id)
);