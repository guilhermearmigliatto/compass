-- Inserting sellers
INSERT INTO seller (id, name) VALUES (1, 'Guilherme Moraes Armigliatto');
INSERT INTO seller (id, name) VALUES (2, 'Pedro Silva');
INSERT INTO seller (id, name) VALUES (3, 'Maria Barros');

-- Inserting charges
INSERT INTO charge (id, seller_Id, amount) VALUES (1, 1, 500.00);
INSERT INTO charge (id, seller_Id, amount) VALUES (2, 1, 150.00);
INSERT INTO charge (id, seller_Id, amount) VALUES (3, 1, 400.00);
INSERT INTO charge (id, seller_Id, amount) VALUES (4, 2, 250.00);
INSERT INTO charge (id, seller_Id, amount) VALUES (5, 3, 300.00);