INSERT INTO users(username, password, email, role_id, disabled)
VALUES ('admin', 'admin', '', 1, 0),
       ('seller', 'seller', '', 2, 0),
       ('customer', 'customer', 'customer@gmail.com', 3, 0),
       ('customerT', 'customerT', 'customer@gmail.com', 3, 0);

INSERT INTO authors(name, surname) VALUES ('Name', 'Surname');

INSERT INTO ebooks(file_reference, price) VALUES ('2cd331b3-834b-4fd5-9af1-40a22c500d3b.txt', 5);

INSERT INTO books(title, price, quantity, category_id, author_id, ebook_id)
VALUES ('Title', 105, 10, 1, 1, 1),
       ('Title2', 50, 10, 2, 1, 1),
       ('Title3', 50, 10, 2, 1, 1),
       ('Title4', 50, 10, 2, 1, 1);

INSERT INTO orders(buyer_id, status_id, total_price, created_at) VALUES (3, 1, 100, '2021-05-17 11:27:20.218206');

INSERT INTO orders_books(order_id, book_id, book_type) VALUES (1, 1, 'PAPER');

INSERT INTO coupons(book_quantity, discount, customer_id, used) VALUES (2, 10, 4, 0);

INSERT INTO read_return_tariff(price_per_day, book_id) VALUES (10, 1);

INSERT INTO read_return_history(customer_id, tariff_id, created_at, email_reminding) VALUES (3, 1, '2021-05-17 11:27:20.218206', 0);