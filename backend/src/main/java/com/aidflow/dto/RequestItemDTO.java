package com.aidflow.dto;

public class RequestItemDTO {
    private Long inventoryItemId;
    private String inventoryItemName;
    private Integer quantity;
    
    public RequestItemDTO() {
    }
    
    public RequestItemDTO(Long inventoryItemId, String inventoryItemName, Integer quantity) {
        this.inventoryItemId = inventoryItemId;
        this.inventoryItemName = inventoryItemName;
        this.quantity = quantity;
    }
    
    public Long getInventoryItemId() {
        return inventoryItemId;
    }
    
    public void setInventoryItemId(Long inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }
    
    public String getInventoryItemName() {
        return inventoryItemName;
    }
    
    public void setInventoryItemName(String inventoryItemName) {
        this.inventoryItemName = inventoryItemName;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
