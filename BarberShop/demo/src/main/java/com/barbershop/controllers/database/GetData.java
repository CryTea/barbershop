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

    public static void GetAll(){
        GetEvents();
        GetProducts();
        GetServices();
        GetInvoices();
        GetClients();
    }
    public static void GetEvents(){
        AllEvents.clear();
        List<List<String>> DBevents = DB.selectRow("DB", "Event", "event_id,date_time,client_id,invoice_id,service_id,description,type", "");
        for (List<String> e : DBevents) {
            Event event = new Event(Integer.parseInt(e.get(0)), LocalDateTime.parse(e.get(1),DateTimeFormatter.ISO_LOCAL_DATE_TIME), Integer.parseInt(e.get(2)), Integer.parseInt(e.get(3)), Integer.parseInt(e.get(4)), e.get(5), Integer.parseInt(e.get(6)));
            AllEvents.add(event);
        }
    }
    public static List<Product> getProductByInvoice(int id){
        List<Product> products = new ArrayList<>();
        List<List<String>> DBproducts = DB.selectRow("DB", "BillProduct", "invoice_id,product_id", "WHERE invoice_id = '"+id+"'");
        for (List<String> dbp : DBproducts) {
            for (Product p : AllProducts) {
                if (p.getProductId() == Integer.parseInt(dbp.get(1))){
                    products.add(p);
                }
            }
        }
        return products;
    }
    public static List<Integer> getProductIdByInvoice(int id){
        List<Integer> products = new ArrayList<>();
        List<List<String>> DBproducts = DB.selectRow("DB", "BillProduct", "invoice_id,product_id", "WHERE invoice_id = '"+id+"'");
        for (List<String> dbp : DBproducts) {
            for (Product p : AllProducts) {
                if (p.getProductId() == Integer.parseInt(dbp.get(1))){
                    products.add(p.getProductId());
                }
            }
        }
        return products;
    }
    public static List<Service> getServiceByInvoice(int id){
        List<Service> services = new ArrayList<>();
        List<List<String>> DBservices = DB.selectRow("DB", "BillService", "invoice_id,service_id", "WHERE invoice_id = '"+id+"'");
        for (List<String> dbp : DBservices) {
            for (Service s : AllServices) {
                if (s.getServiceId() == Integer.parseInt(dbp.get(1))){
                    services.add(s);
                }
            }
        }
        return services;
    }
    public static List<Integer> getServiceIdByInvoice(int id){
        List<Integer> services = new ArrayList<>();
        List<List<String>> DBservices = DB.selectRow("DB", "BillService", "invoice_id,service_id", "WHERE invoice_id = '"+id+"'");
        for (List<String> dbp : DBservices) {
            for (Service s : AllServices) {
                if (s.getServiceId() == Integer.parseInt(dbp.get(1))){
                    services.add(s.getServiceId());
                }
            }
        }
        return services;
    }
    public static void GetInvoices(){
        AllInvoices.clear();
        List<List<String>> DBinvoices = DB.selectRow("DB", "Invoice", "invoice_id,client_id,appointment_id,sub_total,reductions,tax,total", "");
        for (List<String> i : DBinvoices) {
            List<Integer> products = getProductIdByInvoice(Integer.parseInt(i.get(0)));
            List<Integer> services = getServiceIdByInvoice(Integer.parseInt(i.get(0)));
            Invoice invoice = new Invoice(Integer.parseInt(i.get(0)), Integer.parseInt(i.get(1)), Integer.parseInt(i.get(2)), products, services, Double.parseDouble(i.get(3)), Double.parseDouble(i.get(4)), Double.parseDouble(i.get(5)), Double.parseDouble(i.get(6)));
            AllInvoices.add(invoice);
        }
    }
    public static void GetProducts(){
        AllProducts.clear();
        List<List<String>> DBproducts = DB.selectRow("DB", "Product", "product_id,name,description,quantity,price", "");
        for (List<String> p : DBproducts) {
            Product product = new Product(Integer.parseInt(p.get(0)), p.get(1), p.get(2), Integer.parseInt(p.get(3)), Double.parseDouble(p.get(4)));
            AllProducts.add(product);
        }
    }
    public static void GetServices(){
        AllServices.clear();
        List<List<String>> DBservices = DB.selectRow("DB", "Service", "service_id,name,description,price", "");
        for (List<String> p : DBservices) {
            Service service = new Service(Integer.parseInt(p.get(0)), p.get(1), p.get(2), Double.parseDouble(p.get(3)));
            AllServices.add(service);
        }
    }
    public static List<Event> getClientEvents(int id){
        List<Event> events = new ArrayList<>();
        for (Event event : AllEvents) {
            if (event.getClientId() == id) {
                events.add(event);
            }
        }
        return events;
    }
    public static List<Invoice> getClientInvoices(int id){
        List<Invoice> invoices = new ArrayList<>();
        for (Invoice invoice : AllInvoices) {
            if (invoice.getClientId() == id) {
                invoices.add(invoice);
            }
        }
        return invoices;
    }
    public static void GetClients(){
        AllClients.clear();
        List<List<String>> DBclients = DB.selectRow("DB", "Client", "client_id,first_name,last_name,phone", "");
        for (List<String> c : DBclients) {
            List<Event> events = getClientEvents(Integer.parseInt(c.get(0)));
            List<Invoice> invoices = getClientInvoices(Integer.parseInt(c.get(0)));
            Client client = new Client(Integer.parseInt(c.get(0)), c.get(1), c.get(2), Integer.parseInt(c.get(3)), events, invoices);
            AllClients.add(client);
        }
    }
    
}
