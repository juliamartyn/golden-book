CREATE TABLE IF NOT EXISTS e_orders (
    id INT NOT NULL AUTO_INCREMENT,
    code VARCHAR(255) NOT NULL,
    expiration_date DATE NOT NULL,
    order_id INT NOT NULL,
    PRIMARY KEY (id),
    INDEX FK__e_orders__orders (order_id ASC) VISIBLE,
    CONSTRAINT FK__e_orders__orders FOREIGN KEY (order_id) REFERENCES orders (id)
);

