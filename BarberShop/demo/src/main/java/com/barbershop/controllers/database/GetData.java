package com.barbershop.controllers.database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.barbershop.models.*;

public class GetData {
    public static List<Event> AllEvents = new ArrayList<>();
    public static List<Product> AllProducts = new ArrayList<>();
    public static List<Service> AllServices = new ArrayList<>();
    public static List<Client> AllClients = new ArrayList<>();
    public static List<Invoice> AllInvoices = new ArrayList<>();

    public static void GetAll() {
        GetEvents();
        GetProducts();
        GetServices();
        GetInvoices();
        GetClients();
    }

    public static void GetEvents() {
        AllEvents.clear();
        List<List<String>> DBevents = DB.selectRow("DB", "event",
                "event_id, date_time, client_id, invoice_id, service_id, description, type", "");

        for (List<String> e : DBevents) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                int eventId = Integer.parseInt(e.get(0));
                LocalDateTime dateTime = LocalDateTime.parse(e.get(1), formatter);
                int clientId = Integer.parseInt(e.get(2));

                // Обработка NULL для invoice_id
                Integer invoiceId = null;
                if (e.get(3) != null && !e.get(3).isEmpty()) {
                    invoiceId = Integer.parseInt(e.get(3));
                }

                int serviceId = Integer.parseInt(e.get(4));
                String description = e.get(5);
                int type = Integer.parseInt(e.get(6));

                Event event = new Event(eventId, dateTime, clientId, invoiceId,
                        serviceId, description, type);
                AllEvents.add(event);
            } catch (Exception ex) {
                System.err.println("Error parsing event data: " + e);
                ex.printStackTrace();
            }
        }
    }

    public static List<Product> getProductByInvoice(int id) {
        List<Product> products = new ArrayList<>();
        List<List<String>> DBproducts = DB.selectRow("DB", "billproduct",
                "invoice_id, product_id", "WHERE invoice_id = '" + id + "'");
        for (List<String> dbp : DBproducts) {
            for (Product p : AllProducts) {
                if (p.getProductId() == Integer.parseInt(dbp.get(1))) {
                    products.add(p);
                }
            }
        }
        return products;
    }

    public static List<Integer> getProductIdByInvoice(int id) {
        List<Integer> products = new ArrayList<>();
        List<List<String>> DBproducts = DB.selectRow("DB", "billproduct",
                "invoice_id, product_id", "WHERE invoice_id = '" + id + "'");
        for (List<String> dbp : DBproducts) {
            Integer productId = safeParseInt(dbp.get(1));
            if (productId != null) {
                products.add(productId);
            }
        }
        return products;
    }

    private static Integer safeParseInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static List<Service> getServiceByInvoice(int id) {
        List<Service> services = new ArrayList<>();
        List<List<String>> DBservices = DB.selectRow("DB", "billservice",
                "invoice_id, service_id", "WHERE invoice_id = '" + id + "'");
        for (List<String> dbp : DBservices) {
            for (Service s : AllServices) {
                if (s.getServiceId() == Integer.parseInt(dbp.get(1))) {
                    services.add(s);
                }
            }
        }
        return services;
    }

    public static List<Integer> getServiceIdByInvoice(int id) {
        List<Integer> services = new ArrayList<>();
        List<List<String>> DBservices = DB.selectRow("DB", "billservice",
                "invoice_id, service_id", "WHERE invoice_id = '" + id + "'");
        for (List<String> dbp : DBservices) {
            services.add(Integer.parseInt(dbp.get(1)));
        }
        return services;
    }

    public static void GetInvoices() {
        AllInvoices.clear();
        List<List<String>> DBinvoices = DB.selectRow("DB", "invoice",
                "invoice_id, client_id, appointment_id, sub_total, reductions, tax, total", "");
        for (List<String> i : DBinvoices) {
            List<Integer> products = getProductIdByInvoice(Integer.parseInt(i.get(0)));
            List<Integer> services = getServiceIdByInvoice(Integer.parseInt(i.get(0)));
            Invoice invoice = new Invoice(Integer.parseInt(i.get(0)), Integer.parseInt(i.get(1)),
                    Integer.parseInt(i.get(2)), products, services, Double.parseDouble(i.get(3)),
                    Double.parseDouble(i.get(4)), Double.parseDouble(i.get(5)), Double.parseDouble(i.get(6)));
            AllInvoices.add(invoice);
        }
    }

    public static void GetProducts() {
        AllProducts.clear();
        List<List<String>> DBproducts = DB.selectRow("DB", "product",
                "product_id, name, description, quantity, price", "");
        for (List<String> p : DBproducts) {
            Product product = new Product(Integer.parseInt(p.get(0)), p.get(1), p.get(2),
                    Integer.parseInt(p.get(3)), Double.parseDouble(p.get(4)));
            AllProducts.add(product);
        }
    }

    public static void GetServices() {
        AllServices.clear();
        List<List<String>> DBservices = DB.selectRow("DB", "service",
                "service_id, name, description, price", "");
        for (List<String> p : DBservices) {
            Service service = new Service(Integer.parseInt(p.get(0)), p.get(1), p.get(2),
                    Double.parseDouble(p.get(3)));
            AllServices.add(service);
        }
    }

    public static List<Event> getClientEvents(int id) {
        List<Event> events = new ArrayList<>();
        for (Event event : AllEvents) {
            if (event.getClientId() == id) {
                events.add(event);
            }
        }
        return events;
    }

    public static List<Invoice> getClientInvoices(int id) {
        List<Invoice> invoices = new ArrayList<>();
        for (Invoice invoice : AllInvoices) {
            if (invoice.getClientId() == id) {
                invoices.add(invoice);
            }
        }
        return invoices;
    }

    public static void GetClients() {
        AllClients.clear();
        List<List<String>> DBclients = DB.selectRow("DB", "client",
                "client_id, first_name, last_name, phone", "");
        for (List<String> c : DBclients) {
            List<Event> events = getClientEvents(Integer.parseInt(c.get(0)));
            List<Invoice> invoices = getClientInvoices(Integer.parseInt(c.get(0)));
            Client client = new Client(Integer.parseInt(c.get(0)), c.get(1), c.get(2),
                    Integer.parseInt(c.get(3)), events, invoices);
            AllClients.add(client);
        }
    }
}