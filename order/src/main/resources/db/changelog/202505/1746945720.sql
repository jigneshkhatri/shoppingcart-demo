-- Insert dummy products
INSERT INTO products (name) VALUES ('Laptop');
INSERT INTO products (name) VALUES ('Smartphone');
INSERT INTO products (name) VALUES ('Headphones');
INSERT INTO products (name) VALUES ('Smartwatch');
INSERT INTO products (name) VALUES ('Bluetooth Speaker');

-- Insert inventory using subqueries to fetch product IDs
INSERT INTO inventory (product_id, quantity, version)
VALUES ((SELECT id FROM products WHERE name = 'Laptop'), 50, 1);

INSERT INTO inventory (product_id, quantity, version)
VALUES ((SELECT id FROM products WHERE name = 'Smartphone'), 120, 1);

INSERT INTO inventory (product_id, quantity, version)
VALUES ((SELECT id FROM products WHERE name = 'Headphones'), 75, 1);

INSERT INTO inventory (product_id, quantity, version)
VALUES ((SELECT id FROM products WHERE name = 'Smartwatch'), 30, 1);

INSERT INTO inventory (product_id, quantity, version)
VALUES ((SELECT id FROM products WHERE name = 'Bluetooth Speaker'), 60, 1);
