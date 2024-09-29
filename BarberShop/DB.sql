CREATE TABLE Client (
    client_id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone INT
);

CREATE TABLE Event (
    event_id INTEGER PRIMARY KEY AUTOINCREMENT,
    date_time DATETIME,
    client_id INT,
    invoice_id INT,
    service_id INT,
    description TEXT,
    type INTEGER CHECK (type IN (0, 1, 2)),
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id),
    FOREIGN KEY (service_id) REFERENCES Service(service_id)
);

CREATE TABLE Invoice (
    invoice_id INTEGER PRIMARY KEY AUTOINCREMENT,
    client_id INT,
    appointment_id INT,
    sub_total DOUBLE,
    reductions DOUBLE,
    tax DOUBLE,
    total DOUBLE,
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (appointment_id) REFERENCES Event(event_id)
);

CREATE TABLE BillProduct (
    invoice_id INTEGER,
    product_id INTEGER,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE BillService (
    invoice_id INTEGER,
    service_id INTEGER,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id),
    FOREIGN KEY (service_id) REFERENCES Service(service_id),
    PRIMARY KEY (invoice_id, service_id)
);

CREATE TABLE Product (
    product_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255),
    description TEXT,
    quantity INT,
    price DOUBLE
);

CREATE TABLE Service (
    service_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255),
    description TEXT,
    price DOUBLE
);

-- Insert dummy data into Client table
INSERT INTO Client (first_name, last_name, phone) VALUES
('John', 'Doe', 123456789),
('Jane', 'Smith', 987654321),
('Michael', 'Johnson', 555555555);

-- Insert dummy data into Service table
INSERT INTO Service (name, description, price) VALUES
('Haircut', 'Standard haircut service', 20.00),
('Manicure', 'Basic manicure service', 15.00),
('Massage', '60-minute full body massage', 50.00);

-- Insert dummy data into Product table
INSERT INTO Product (name, description, quantity, price) VALUES
('Shampoo', '250ml bottle of shampoo', 10, 8.00),
('Conditioner', '200ml bottle of conditioner', 8, 6.00),
('Body Lotion', '150ml bottle of body lotion', 15, 10.00);

-- Insert dummy data into Invoice table
INSERT INTO Invoice (client_id, appointment_id, sub_total, reductions, tax, total) VALUES
(1, 1, 20.00, 0.00, 3.00, 23.00),
(2, 2, 15.00, 0.00, 2.25, 17.25),
(3, 3, 50.00, 0.00, 7.50, 57.50);

-- Insert dummy data into BillProduct table
INSERT INTO BillProduct (invoice_id, product_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 1);

-- Insert dummy data into BillService table
INSERT INTO BillService (invoice_id, service_id) VALUES
(1, 2),
(2, 3),
(3, 1);

-- Insert dummy data into Event table
INSERT INTO Event (date_time, client_id, invoice_id, service_id, description, type) VALUES
('2024-04-25T10:00:00', 1, 1, 1, 'Haircut appointment', 2),
('2024-04-26T14:00:00', 2, 2, 2, 'Manicure appointment', 2),
('2024-04-27T16:00:00', 3, 3, 3, 'Massage appointment', 0);
