package com.aidflow.dto;

import java.time.LocalDateTime;

public class DonationDTO {
    private Long id;
    private String donorName;
    private String email;
    private String phone;
    private Double amount;
    private String item;
    private Integer itemQuantity;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    
    public DonationDTO() {
    }
    
    public DonationDTO(Long id, String donorName, String email, String phone, Double amount, String item, Integer itemQuantity, String message, String status, LocalDateTime createdAt) {
        this.id = id;
        this.donorName = donorName;
        this.email = email;
        this.phone = phone;
        this.amount = amount;
        this.item = item;
        this.itemQuantity = itemQuantity;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDonorName() {
        return donorName;
    }
    
    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getItem() {
        return item;
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public Integer getItemQuantity() {
        return itemQuantity;
    }
    
    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
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
}
