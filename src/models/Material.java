package models;

public class Material {
    private int materialId;
    private int supplierId;
    private String name;
    private String category;
    private double unitPrice;
    private int stockQuantity;
    private String status; // Available, Discontinued

    // Constructor for creating a new material (before it goes into the database)
    public Material(int supplierId, String name, String category, double unitPrice, int stockQuantity, String status) {
        this.supplierId = supplierId;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.status = status;
    }

    // Constructor for retrieving a material from the database (includes materialId)
    public Material(int materialId, int supplierId, String name, String category, double unitPrice, int stockQuantity, String status) {
        this.materialId = materialId;
        this.supplierId = supplierId;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.status = status;
    }

    // Getters
    public int getMaterialId() { return materialId; }
    public int getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getUnitPrice() { return unitPrice; }
    public int getStockQuantity() { return stockQuantity; }
    public String getStatus() { return status; }

    // Setters
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setStatus(String status) { this.status = status; }
}