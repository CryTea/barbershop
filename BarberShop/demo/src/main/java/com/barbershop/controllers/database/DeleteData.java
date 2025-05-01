package com.barbershop.controllers.database;

import com.barbershop.controllers.alerts.AlertController;
import com.barbershop.models.Event;
import com.barbershop.models.Invoice;

public class DeleteData {

    public static void DeleteInvoiceByEventId(int eventId) {
        Invoice invoiceToDelete = null;
        for (Invoice i : GetData.AllInvoices) {
            if (i.getEventId() == eventId) {
                invoiceToDelete = i;
                break;
            }
        }
        if (invoiceToDelete != null) {
            DeleteInvoice(invoiceToDelete.getInvoice_id());
        }
    }

    public static void DeleteEvent(int id) {
        GetData.GetAll();
        boolean invoice_found = false;
        for (Invoice i : GetData.AllInvoices) {
            if (i.getEventId() == id) {
                invoice_found = true;
                break;
            }
        }

        if (invoice_found) {
            DeleteInvoiceByEventId(id); // 🆕 Удаляем счёт
        }

        DB.deleteRow("DB", "event", "event_id = '" + id + "'");
        GetData.GetAll();
    }


    public static void DeleteInvoice(int id) {
        DB.deleteRow("DB", "billproduct", "invoice_id = '" + id + "'");
        DB.deleteRow("DB", "billservice", "invoice_id = '" + id + "'");
        DB.deleteRow("DB", "invoice", "invoice_id = '" + id + "'");
        GetData.GetAll();
    }

    public static void DeleteProduct(int id) {
        GetData.GetAll();
        boolean found = false;
        for (Invoice i : GetData.AllInvoices) {
            if (i.getEventId() == id) {
                found = true;
                break;
            }
        }
        if (found) {
            AlertController.showError("Delete Product " + id, "This product is in an invoice!");
        } else {
            DB.deleteRow("DB", "product", "product_id = '" + id + "'");
        }
        GetData.GetAll();
    }

    public static void DeleteService(int id) {
        GetData.GetAll();
        boolean found = false;
        for (Invoice i : GetData.AllInvoices) {
            if (i.getEventId() == id) {
                found = true;
                break;
            }
        }
        for (Event e : GetData.AllEvents) {
            if (e.getServiceId() == id) {
                found = true;
                break;
            }
        }
        if (found) {
            AlertController.showError("Delete Service " + id, "This service is in an invoice!");
        } else {
            DB.deleteRow("DB", "service", "service_id = '" + id + "'");
        }
        GetData.GetAll();
    }

    public static void DeleteClient(int id) {
        boolean found = false;
        for (Event e : GetData.AllEvents) {
            if (e.getClientId() == id) {
                found = true;
                break;
            }
        }
        if (found) {
            AlertController.showError("Delete Client " + id, "This client have an event(s) attached!");
        } else {
            DB.deleteRow("DB", "client", "client_id = '" + id + "'");
        }
        GetData.GetAll();
    }
}