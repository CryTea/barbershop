package com.barbershop.controllers.database;

import com.barbershop.controllers.alerts.AlertController;
import com.barbershop.models.Event;
import com.barbershop.models.Invoice;

public class DeleteData {
    public static void DeleteEvent(int id){
        GetData.GetAll();
        boolean invoice_found = false;
        for (Invoice i : GetData.AllInvoices) {
            if (i.getEventId() == id){
                invoice_found = true;
                break;
            }
        }
        if (invoice_found) {
            AlertController.showError("Delete Event "+id, "This event have an invoice attched!");
        } else {
            DB.deleteRow("DB", "Event", "event_id = '"+id+"'");
        }
        GetData.GetAll();
    }
    public static void DeleteInvoice(int id){
        DB.deleteRow("DB", "BillProduct", "invoice_id = '"+id+"'");
        DB.deleteRow("DB", "BillService", "invoice_id = '"+id+"'");
        DB.deleteRow("DB", "Invoice", "invoice_id = '"+id+"'");
        GetData.GetAll();
    }
    public static void DeleteProduct(int id){
        GetData.GetAll();
        boolean found = false;
        for (Invoice i : GetData.AllInvoices) {
            if (i.getEventId() == id){
                found = true;
                break;
            }
        }
        if (found) {
            AlertController.showError("Delete Product "+id, "This product is in an invoice!");
        } else {
            DB.deleteRow("DB", "Product", "product_id = '"+id+"'");
        }
        GetData.GetAll();
    }
    public static void DeleteService(int id){
        GetData.GetAll();
        boolean found = false;
        for (Invoice i : GetData.AllInvoices) {
            if (i.getEventId() == id){
                found = true;
                break;
            }
        }
        for (Event e : GetData.AllEvents) {
            if (e.getServiceId() == id){
                found = true;
                break;
            }
        }
        if (found) {
            AlertController.showError("Delete Service "+id, "This service is in an invoice!");
        } else {
            DB.deleteRow("DB", "Service", "service_id = '"+id+"'");
        }
        GetData.GetAll();
    }
    public static void DeleteClient(int id){
        boolean found = false;
        for (Event e : GetData.AllEvents) {
            if (e.getClientId() == id){
                found = true;
                break;
            }
        }
        if (found) {
            AlertController.showError("Delete Client "+id, "This client have an event(s) attached!");
        } else {
            DB.deleteRow("DB", "Client", "client_id = '"+id+"'");
        }
        GetData.GetAll();
    }
}
