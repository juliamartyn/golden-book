DELIMITER //
CREATE TRIGGER email_history_on_updating_order
    AFTER UPDATE ON orders
    FOR EACH ROW
BEGIN
    IF (NEW.status_id = 2) THEN
		INSERT INTO email_history(order_id, email_type) VALUES(OLD.id, 'ORDER_CONFIRMED');
    END IF;
    IF (NEW.status_id = 3 OR NEW.status_id = 4) THEN
		INSERT INTO email_history(order_id, email_type) VALUES(OLD.id, 'ORDER_STATUS_UPDATE');
    END IF;
END //

DELIMITER //
CREATE TRIGGER email_history_on_inserting_order
    AFTER INSERT ON orders
    FOR EACH ROW
BEGIN
    IF (NEW.status_id = 5) THEN
		INSERT INTO email_history(order_id, email_type) VALUES(NEW.id, 'BOOK_AVAILABLE');
    END IF;
END //