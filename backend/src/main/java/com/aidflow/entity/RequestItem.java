package com.aidflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "request_items")
public class RequestItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;
    
    @Column(nullable = false)
    private Integer quantity;
    
    // Constructors
    public RequestItem() {
    }
    
    public RequestItem(Long id, Request request, InventoryItem inventoryItem, Integer quantity) {
        this.id = id;
        this.request = request;
        this.inventoryItem = inventoryItem;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Request getRequest() {
        return request;
    }
    
    public void setRequest(Request request) {
        this.request = request;
    }
    
    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }
    
    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
