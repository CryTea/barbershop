-- Создание таблицы Client
CREATE TABLE client (
                        client_id SERIAL PRIMARY KEY,
                        first_name VARCHAR(255) NOT NULL,
                        last_name VARCHAR(255) NOT NULL,
                        phone VARCHAR(20) NOT NULL
);

-- Создание таблицы Service
CREATE TABLE service (
                         service_id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price NUMERIC(10, 2) NOT NULL
);

-- Создание таблицы Product
CREATE TABLE product (
                         product_id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         quantity INTEGER NOT NULL,
                         price NUMERIC(10, 2) NOT NULL
);

-- 2. Создаем invoice без ссылки на event (пока event не существует)
CREATE TABLE invoice (
                         invoice_id SERIAL PRIMARY KEY,
                         client_id INTEGER NOT NULL,
                         appointment_id INTEGER,
                         sub_total NUMERIC(10, 2) NOT NULL,
                         reductions NUMERIC(10, 2) DEFAULT 0,
                         tax NUMERIC(10, 2) NOT NULL,
                         total NUMERIC(10, 2) NOT NULL,
                         FOREIGN KEY (client_id) REFERENCES client(client_id)
);

-- Создание таблицы Event
CREATE TABLE event (
                       event_id SERIAL PRIMARY KEY,
                       date_time TIMESTAMP NOT NULL,
                       client_id INTEGER NOT NULL,
                       invoice_id INTEGER,
                       service_id INTEGER,
                       description TEXT,
                       type INTEGER NOT NULL CHECK (type IN (0, 1, 2)),
                       FOREIGN KEY (client_id) REFERENCES client(client_id),
                       FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id),
                       FOREIGN KEY (service_id) REFERENCES service(service_id)
);

ALTER TABLE invoice ADD CONSTRAINT fk_invoice_event
    FOREIGN KEY (appointment_id) REFERENCES event(event_id);


-- Создание таблицы BillProduct
CREATE TABLE billproduct (
                             invoice_id INTEGER NOT NULL,
                             product_id INTEGER NOT NULL,
                             FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id) ON DELETE CASCADE,
                             FOREIGN KEY (product_id) REFERENCES product(product_id),
                             PRIMARY KEY (invoice_id, product_id)
);

-- Создание таблицы BillService
CREATE TABLE billservice (
                             invoice_id INTEGER NOT NULL,
                             service_id INTEGER NOT NULL,
                             FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id) ON DELETE CASCADE,
                             FOREIGN KEY (service_id) REFERENCES service(service_id),
                             PRIMARY KEY (invoice_id, service_id)
);

-- Вставка тестовых данных
INSERT INTO client (first_name, last_name, phone) VALUES
('John', 'Doe', '123456789'),
('Jane', 'Smith', '987654321'),
('Michael', 'Johnson', '555555555');

INSERT INTO service (name, description, price) VALUES
('Haircut', 'Standard haircut service', 20.00),
('Manicure', 'Basic manicure service', 15.00),
('Massage', '60-minute full body massage', 50.00);

INSERT INTO product (name, description, quantity, price) VALUES
('Shampoo', '250ml bottle of shampoo', 10, 8.00),
('Conditioner', '200ml bottle of conditioner', 8, 6.00),
('Body Lotion', '150ml bottle of body lotion', 15, 10.00);

-- Важно: сначала создаем invoice, так как event ссылается на invoice
INSERT INTO invoice (client_id, appointment_id, sub_total, reductions, tax, total) VALUES
(1, NULL, 20.00, 0.00, 3.00, 23.00),
(2, NULL, 15.00, 0.00, 2.25, 17.25),
(3, NULL, 50.00, 0.00, 7.50, 57.50);

INSERT INTO event (date_time, client_id, invoice_id, service_id, description, type) VALUES
('2024-04-25 10:00:00', 1, 1, 1, 'Haircut appointment', 2),
('2024-04-26 14:00:00', 2, 2, 2, 'Manicure appointment', 2),
('2024-04-27 16:00:00', 3, 3, 3, 'Massage appointment', 0);

-- Обновляем invoice с корректными appointment_id
UPDATE invoice SET appointment_id = 1 WHERE invoice_id = 1;
UPDATE invoice SET appointment_id = 2 WHERE invoice_id = 2;
UPDATE invoice SET appointment_id = 3 WHERE invoice_id = 3;

INSERT INTO billproduct (invoice_id, product_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 1);

INSERT INTO billservice (invoice_id, service_id) VALUES
(1, 2),
(2, 3),
(3, 1);
