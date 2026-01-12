package com.aidflow.dto;

import java.time.LocalDateTime;

public class NewItemRequestDTO {
    private Long id;
    private Long fieldWorkerId;
    private String fieldWorkerName;
    private String itemName;
    private String category;
    private Integer requestedQuantity;
    private String unit;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    
    public NewItemRequestDTO() {
    }
    
    public NewItemRequestDTO(Long id, Long fieldWorkerId, String fieldWorkerName, String itemName, String category, Integer requestedQuantity, String unit, String description, String status, LocalDateTime createdAt, LocalDateTime updatedAt, String notes) {
        this.id = id;
        this.fieldWorkerId = fieldWorkerId;
        this.fieldWorkerName = fieldWorkerName;
        this.itemName = itemName;
        this.category = category;
        this.requestedQuantity = requestedQuantity;
        this.unit = unit;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFieldWorkerId() {
        return fieldWorkerId;
    }
    
    public void setFieldWorkerId(Long fieldWorkerId) {
        this.fieldWorkerId = fieldWorkerId;
    }
    
    public String getFieldWorkerName() {
        return fieldWorkerName;
    }
    
    public void setFieldWorkerName(String fieldWorkerName) {
        this.fieldWorkerName = fieldWorkerName;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public void setRequestedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
