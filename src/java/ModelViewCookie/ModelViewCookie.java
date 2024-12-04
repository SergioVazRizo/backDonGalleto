/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dongalleto.modelView;

/**
 *
 * @author Usuario
 */
public class ModelViewCookie {
    private int id;
    private String name;
    private int recipeId;
    private String description;
    private String status;
    private double unitPrice;
    private double package500gPrice;
    private double package1000gPrice;
    private double pricePerGram;
    private int stock;
    private double weightPerUnit;

    public ModelViewCookie(int id, String name, int recipeId, String description, String status, double unitPrice, double package500gPrice, double package1000gPrice, double pricePerGram, int stock, double weightPerUnit) {
        this.id = id;
        this.name = name;
        this.recipeId = recipeId;
        this.description = description;
        this.status = status;
        this.unitPrice = unitPrice;
        this.package500gPrice = package500gPrice;
        this.package1000gPrice = package1000gPrice;
        this.pricePerGram = pricePerGram;
        this.stock = stock;
        this.weightPerUnit = weightPerUnit;
    }

    public ModelViewCookie() {
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

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getPackage500gPrice() {
        return package500gPrice;
    }

    public void setPackage500gPrice(double package500gPrice) {
        this.package500gPrice = package500gPrice;
    }

    public double getPackage1000gPrice() {
        return package1000gPrice;
    }

    public void setPackage1000gPrice(double package1000gPrice) {
        this.package1000gPrice = package1000gPrice;
    }

    public double getPricePerGram() {
        return pricePerGram;
    }

    public void setPricePerGram(double pricePerGram) {
        this.pricePerGram = pricePerGram;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getWeightPerUnit() {
        return weightPerUnit;
    }

    public void setWeightPerUnit(double weightPerUnit) {
        this.weightPerUnit = weightPerUnit;
    }
        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CookieModelView{");
        sb.append("id:").append(id);
        sb.append(", name:").append(name);
        sb.append(", recipeId:").append(recipeId);
        sb.append(", description:").append(description);
        sb.append(", status:").append(status);
        sb.append(", unitPrice:").append(unitPrice);
        sb.append(", package500gPrice:").append(package500gPrice);
        sb.append(", package1000gPrice:").append(package1000gPrice);
        sb.append(", pricePerGram:").append(pricePerGram);
        sb.append(", stock:").append(stock);
        sb.append(", weightPerUnit:").append(weightPerUnit);
        sb.append('}');
        return sb.toString();
    }

    
}