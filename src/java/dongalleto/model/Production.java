package dongalleto.model;

public class Production {

    private int id;
    private int cookieId;
    private String productionStatus; 
    private int unitsProduced;

    public Production(int id, int cookieId, String productionStatus, int unitsProduced) {
        this.id = id;
        this.cookieId = cookieId;
        this.productionStatus = productionStatus;
        this.unitsProduced = unitsProduced;
    }

    public Production() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCookieId() {
        return cookieId;
    }

    public void setCookieId(int cookieId) {
        this.cookieId = cookieId;
    }

    public String getProductionStatus() {
        return productionStatus;
    }

    public void setProductionStatus(String productionStatus) {
        this.productionStatus = productionStatus;
    }

    public int getUnitsProduced() {
        return unitsProduced;
    }

    public void setUnitsProduced(int unitsProduced) {
        this.unitsProduced = unitsProduced;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Production{");
        sb.append("id:").append(id);
        sb.append(", cookieId:").append(cookieId);
        sb.append(", productionStatus:").append(productionStatus);
        sb.append(", unitsProduced:").append(unitsProduced);
        sb.append('}');
        return sb.toString();
    }
}