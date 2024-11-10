# BarberShop JavaFX App
### Requirements
The system aims to facilitate the management of appointments, transactions, products, services, invoices, and requisitions for a barber shop. 
It allows appointments scheduling, billing of services and products, management of inventory, and tracking of financial transactions.
### Entities
- **Client:** Represents customers who schedule appointments and receive invoices for services and products.
- **Service:** Describes the services offered by the business, including their name, code, description, price, and duration.
- **Product:** Represents the products available for sale, including their name, code, description, and price.
- **Invoice:** Represents the billing document issued to clients, including details such as the total cost, subtotal, tips, discount, tax rate, and balance due.
- **Appointment:** Represents scheduled appointments, including the date, time, requested service, and any additional notes.
- **Requisition:** Represents requests for products, including details such as the requisition number, requested date, received date, quantity received, and any notes.
### Data
The system stores data related to clients, services, products, invoices and appointments. 
This data includes client information, service and product details, invoice amounts and appointment schedules.

## Run the project
You can run the project using the .jar file: [BarberShop.jar](https://github.com/NiemaAM/BarberShop-JavaFX-App/blob/main/BarberShop/BarberShop.jar.lnk)

## Database (sql lite)
[![SQLite](https://img.shields.io/badge/SQLite-3.39.2-lightgrey.svg)](https://www.sqlite.org)
```sql
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
```

## Controller (Java)
[![Java](https://img.shields.io/badge/Java-17-red.svg)](https://www.java.com)
```java

```

## View (JavaFx)
[![JavaFX](https://img.shields.io/badge/JavaFX-20-blue.svg)](https://openjfx.io)
```java

```
```xml

```
