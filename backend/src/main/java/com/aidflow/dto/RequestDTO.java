package com.aidflow.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RequestDTO {
    private Long id;
    private Long fieldWorkerId;
    private String fieldWorkerName;
    private List<RequestItemDTO> items;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    
    public RequestDTO() {
    }
    
    public RequestDTO(Long id, Long fieldWorkerId, String fieldWorkerName, List<RequestItemDTO> items, String status, LocalDateTime createdAt, LocalDateTime updatedAt, String notes) {
        this.id = id;
        this.fieldWorkerId = fieldWorkerId;
        this.fieldWorkerName = fieldWorkerName;
        this.items = items;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.notes = notes;
    }
    
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
    
    public List<RequestItemDTO> getItems() {
        return items;
    }
    
    public void setItems(List<RequestItemDTO> items) {
        this.items = items;
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
