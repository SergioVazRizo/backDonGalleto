package dongalleto.model;

public class SaleItem {
    private int cookieId;  // ID de la galleta
    private String cookieName;  // Nombre de la galleta
    private int quantity;  // Cantidad comprada
    private double pricePerUnit;  // Precio por unidad
    private double subtotal;  // Subtotal (quantity * pricePerUnit)
    private String saleType;  // Tipo de venta (UNIT, WEIGHT, PACKAGE_500, PACKAGE_1000, AMOUNT)

    public SaleItem() {
    }

    public SaleItem(int cookieId, String cookieName, int quantity, double pricePerUnit, double subtotal, String saleType) {
        this.cookieId = cookieId;
        this.cookieName = cookieName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.subtotal = subtotal;
        this.saleType = saleType;
    }

    public int getCookieId() {
        return cookieId;
    }

    public void setCookieId(int cookieId) {
        this.cookieId = cookieId;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    @Override
    public String toString() {
        return "SaleItem{" +
                "cookieId=" + cookieId +
                ", cookieName='" + cookieName + '\'' +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                ", subtotal=" + subtotal +
                ", saleType='" + saleType + '\'' +
                '}';
    }
}
