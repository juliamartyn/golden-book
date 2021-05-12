CREATE TABLE IF NOT EXISTS roles (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    phone VARCHAR(255) DEFAULT NULL,
    role_id INT DEFAULT NULL,
    disabled BOOLEAN DEFAULT NULL,
    delivery_address VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX FK_users_roles (role_id ASC) VISIBLE,
    CONSTRAINT FK_users_roles FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS book_category (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS discounts (
    id INT NOT NULL AUTO_INCREMENT,
    discount_percent INT DEFAULT NULL,
    due_date DATE NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS books (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) DEFAULT NULL,
    author VARCHAR(255) DEFAULT NULL,
    description VARCHAR(255) DEFAULT NULL,
    image BLOB DEFAULT NULL,
    price DECIMAL(10,2) DEFAULT NULL,
    quantity INT DEFAULT NULL,
    start_sale_date DATE DEFAULT NULL,
    category_id INT DEFAULT NULL,
    discount_id INT DEFAULT NULL,
    price_with_discount DECIMAL(10,2) DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX FK_books_discount (discount_id ASC) VISIBLE,
    INDEX FK_books_category (category_id ASC) VISIBLE,
    CONSTRAINT FK_books_discount FOREIGN KEY (discount_id) REFERENCES discounts (id) ON DELETE SET NULL,
    CONSTRAINT FK_books_category FOREIGN KEY (category_id) REFERENCES book_category (id)
);

CREATE TABLE IF NOT EXISTS order_status (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS coupons (
    id INT NOT NULL AUTO_INCREMENT,
    book_quantity INT DEFAULT NULL,
    discount INT DEFAULT NULL,
    due_date DATE DEFAULT NULL,
    customer_id BIGINT DEFAULT NULL,
    used BOOLEAN DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX FK_coupons_users (customer_id ASC) VISIBLE,
    CONSTRAINT FK_coupons_users FOREIGN KEY (customer_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS orders (
    id INT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) DEFAULT NULL,
    total_price DECIMAL(10,2) DEFAULT NULL,
    buyer_id BIGINT DEFAULT NULL,
    status_id INT DEFAULT NULL,
    coupon_id INT DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX FK_orders__order_status (status_id ASC) VISIBLE,
    INDEX FK_orders_users (buyer_id ASC) VISIBLE,
    INDEX FK_orders_coupons (coupon_id ASC) VISIBLE,
    CONSTRAINT FK_orders__order_status FOREIGN KEY (status_id) REFERENCES order_status (id),
    CONSTRAINT FK_orders_users FOREIGN KEY (buyer_id) REFERENCES users (id),
    CONSTRAINT FK_orders_coupons FOREIGN KEY (coupon_id) REFERENCES coupons (id)
);


CREATE TABLE IF NOT EXISTS orders_books (
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    INDEX FK_orders_books__books (book_id ASC) VISIBLE,
    INDEX FK_orders_books__orders (order_id ASC) VISIBLE,
    CONSTRAINT FK_orders_books__books FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT FK_orders_books__orders FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS email_history (
    id INT NOT NULL AUTO_INCREMENT,
    order_id INT  DEFAULT NULL,
    email_type VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX FK_email_history__orders (order_id ASC) VISIBLE,
    CONSTRAINT FK_email_history__orders FOREIGN KEY (order_id) REFERENCES orders (id)
);


CREATE TABLE IF NOT EXISTS read_return_tariff (
    id INT NOT NULL AUTO_INCREMENT,
    price_per_day DECIMAL(10,2) DEFAULT NULL,
    book_id INT DEFAULT NULL,
    PRIMARY KEY (id),
    INDEX FK_read_return_tariff__books (book_id ASC) VISIBLE,
    CONSTRAINT FK_read_return_tariff__books FOREIGN KEY (book_id) REFERENCES books (id)
);

CREATE TABLE IF NOT EXISTS read_return_history (
    id INT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT DEFAULT NULL,
    tariff_id INT DEFAULT NULL,
    created_at DATE NOT NULL,
    expected_return_date DATE DEFAULT NULL,
    rent_days_number INT DEFAULT NULL,
    email_reminding BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    INDEX FK_read_return_history__users (customer_id ASC) VISIBLE,
    INDEX FK_read_return_history__read_return_tariff (tariff_id ASC) VISIBLE,
    CONSTRAINT FK_read_return_history__read_return_tariff FOREIGN KEY (tariff_id) REFERENCES read_return_tariff (id),
    CONSTRAINT FK_read_return_history__users FOREIGN KEY (customer_id) REFERENCES users (id)
);