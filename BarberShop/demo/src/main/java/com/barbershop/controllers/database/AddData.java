package com.barbershop.controllers.database;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.barbershop.controllers.alerts.StockAlert;
import com.barbershop.controllers.patterns.PaternController;
import com.barbershop.models.Client;
import com.barbershop.models.Event;
import com.barbershop.models.Invoice;
import com.barbershop.models.Product;
import com.barbershop.models.Service;

public class AddData {
    public static int AddEvent(Event event) {
        try {
            return DB.executeInsertWithReturnId(
                    "INSERT INTO event (date_time, client_id, invoice_id, service_id, description, type) " +
                            "VALUES (?, ?, ?, ?, ?, ?) RETURNING event_id",
                    event.getDateTime(),
                    event.getClientId(),
                    event.getInvoiceId(),
                    event.getServiceId(),
                    event.getDecription(),
                    event.getType()
            );
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
            return -1;
        } finally {
            GetData.GetAll();
        }
    }

    public static int AddInvoice(Invoice invoice) throws StockAlert {
        try {
            return DB.executeInsertWithReturnId(
                    "INSERT INTO invoice (client_id, appointment_id, sub_total, reductions, tax, total) " +
                            "VALUES (?, ?, ?, ?, ?, ?) RETURNING invoice_id",
                    invoice.getClientId(),
                    invoice.getEventId(),
                    invoice.getSub_total(),
                    invoice.getReductions(),
                    invoice.getTax(),
                    invoice.getTotal()
            );
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
            return -1;
        } finally {
            GetData.GetAll();
        }
    }

    public static int AddProduct(Product product) {
        try {
            return DB.executeInsertWithReturnId(
                    "INSERT INTO product (name, description, quantity, price) VALUES (?, ?, ?, ?) RETURNING product_id",
                    product.getName(),
                    product.getDescription(),
                    product.getQuantity(),
                    product.getPrice()
            );
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
            return -1;
        } finally {
            GetData.GetAll();
        }
    }

    public static int AddService(Service service) {
        try {
            return DB.executeInsertWithReturnId(
                    "INSERT INTO service (name, description, price) VALUES (?, ?, ?) RETURNING service_id",
                    service.getName(),
                    service.getDescription(),
                    service.getPrice()
            );
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
            return -1;
        } finally {
            GetData.GetAll();
        }
    }

    public static int AddClient(Client client) {
        String capitalizedFirstName = PaternController.capitalize(client.getFirst_name());
        String lastName = client.getLast_name().toUpperCase();

        // Используем параметризованный запрос через executeInsertWithReturnId
        try {
            return DB.executeInsertWithReturnId(
                    "INSERT INTO client (first_name, last_name, phone) VALUES (?, ?, ?) RETURNING client_id",
                    capitalizedFirstName,
                    lastName,
                    client.getPhone()
            );
        } catch (SQLException e) {
            System.out.println("PostgreSQL ERROR: " + e.getMessage());
            return -1;
        } finally {
            GetData.GetAll();
        }
    }
}