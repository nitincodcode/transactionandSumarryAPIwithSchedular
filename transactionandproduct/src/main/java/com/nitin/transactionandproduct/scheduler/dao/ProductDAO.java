package com.nitin.transactionandproduct.scheduler.dao;

public class ProductDAO {

    private String productId;
    private String productName;
    private String productManufacturingCity;

    public ProductDAO(String productId, String productName, String productManufacturingCity) {
        this.productId = productId.trim();
        this.productName = productName.trim();
        this.productManufacturingCity = productManufacturingCity.trim();
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductManufacturingCity() {
        return productManufacturingCity;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProductDAO)) {
            return false;
        }
        ProductDAO prd = (ProductDAO) obj;
        return productId.equals(prd.getProductId());
    }

    @Override
    public String toString() {
        return "Product ID: " + this.productId + " Product Name: " + this.productName + " Product Manfacturing City" + this.productManufacturingCity;
    }
}
