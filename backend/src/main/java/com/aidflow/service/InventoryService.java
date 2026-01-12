package com.aidflow.service;

import com.aidflow.dto.InventoryItemDTO;
import com.aidflow.entity.InventoryItem;
import com.aidflow.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    public List<InventoryItemDTO> getAllItems() {
        return inventoryItemRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public InventoryItemDTO getItemById(Long id) {
        InventoryItem item = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory item not found"));
        return convertToDTO(item);
    }
    
    @Transactional
    public InventoryItemDTO createItem(InventoryItemDTO dto) {
        InventoryItem item = new InventoryItem();
        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setQuantity(dto.getQuantity());
        item.setUnit(dto.getUnit());
        item.setDescription(dto.getDescription());
        
        InventoryItem saved = inventoryItemRepository.save(item);
        return convertToDTO(saved);
    }
    
    @Transactional
    public InventoryItemDTO updateItem(Long id, InventoryItemDTO dto) {
        InventoryItem item = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory item not found"));
        
        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setQuantity(dto.getQuantity());
        item.setUnit(dto.getUnit());
        item.setDescription(dto.getDescription());
        
        InventoryItem updated = inventoryItemRepository.save(item);
        return convertToDTO(updated);
    }
    
    @Transactional
    public void deleteItem(Long id) {
        if (!inventoryItemRepository.existsById(id)) {
            throw new RuntimeException("Inventory item not found");
        }
        inventoryItemRepository.deleteById(id);
    }
    
    @Transactional
    public InventoryItemDTO updateQuantity(Long id, Integer quantity) {
        InventoryItem item = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory item not found"));
        
        item.setQuantity(quantity);
        InventoryItem updated = inventoryItemRepository.save(item);
        return convertToDTO(updated);
    }
    
    private InventoryItemDTO convertToDTO(InventoryItem item) {
        return new InventoryItemDTO(
            item.getId(),
            item.getName(),
            item.getCategory(),
            item.getQuantity(),
            item.getUnit(),
            item.getDescription()
        );
    }
}
