DELIMITER //
CREATE TRIGGER books_price_with_discount_preinserting
    BEFORE INSERT ON books
    FOR EACH ROW
BEGIN
    SET NEW.price_with_discount = NEW.price;
END //