package dongalleto.model;

import java.util.Date;
import java.util.List;

public class Sale {
    private int id;  // ID de la venta
    private String date;  // Fecha de la venta
    private List<SaleItem> items;  
    private double total;  

    public Sale() {
    }

    public Sale(int id, String date, List<SaleItem> items, double total) {
        this.id = id;
        this.date = date;
        this.items = items;
        this.total = total;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", date=" + date +
                ", items=" + items +
                ", total=" + total +
                '}';
    }
}