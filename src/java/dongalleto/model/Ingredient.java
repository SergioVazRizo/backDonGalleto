package dongalleto.model;

/**
 *
 * @author checo
 */
public class Ingredient {

    private int id;
    private String name;
    private double stock;
    private String unit;
    private double minimumStock;
    private double cost;
    private String expirationDate;

    public Ingredient(int id, String name, double stock, String unit, double minimumStock, double cost, String expirationDate) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.unit = unit;
        this.minimumStock = minimumStock;
        this.cost = cost;
        this.expirationDate = expirationDate;
    }

    public Ingredient() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(double minimumStock) {
        this.minimumStock = minimumStock;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ingredient{");
        sb.append("id:").append(id);
        sb.append(", name:").append(name);
        sb.append(", stock:").append(stock);
        sb.append(", unit:").append(unit);
        sb.append(", minimumStock:").append(minimumStock);
        sb.append(", cost:").append(cost);
        sb.append(", expirationDate:").append(expirationDate);
        sb.append('}');
        return sb.toString();
    }
}
