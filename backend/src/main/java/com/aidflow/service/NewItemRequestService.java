package com.aidflow.service;

import com.aidflow.dto.NewItemRequestDTO;
import com.aidflow.entity.InventoryItem;
import com.aidflow.entity.NewItemRequest;
import com.aidflow.entity.User;
import com.aidflow.repository.InventoryItemRepository;
import com.aidflow.repository.NewItemRequestRepository;
import com.aidflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewItemRequestService {
    
    @Autowired
    private NewItemRequestRepository newItemRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    public List<NewItemRequestDTO> getAllRequests() {
        return newItemRequestRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<NewItemRequestDTO> getRequestsByFieldWorker(Long fieldWorkerId) {
        User fieldWorker = userRepository.findById(fieldWorkerId)
            .orElseThrow(() -> new RuntimeException("Field worker not found"));
        
        return newItemRequestRepository.findByFieldWorker(fieldWorker).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<NewItemRequestDTO> getPendingRequests() {
        return newItemRequestRepository.findByStatus(NewItemRequest.RequestStatus.PENDING).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public NewItemRequestDTO getRequestById(Long id) {
        NewItemRequest request = newItemRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("New item request not found"));
        return convertToDTO(request);
    }
    
    @Transactional
    public NewItemRequestDTO createRequest(Long fieldWorkerId, NewItemRequestDTO dto) {
        User fieldWorker = userRepository.findById(fieldWorkerId)
            .orElseThrow(() -> new RuntimeException("Field worker not found"));
        
        NewItemRequest request = new NewItemRequest();
        request.setFieldWorker(fieldWorker);
        request.setItemName(dto.getItemName());
        request.setCategory(dto.getCategory());
        request.setRequestedQuantity(dto.getRequestedQuantity());
        request.setUnit(dto.getUnit());
        request.setDescription(dto.getDescription());
        request.setStatus(NewItemRequest.RequestStatus.PENDING);
        
        NewItemRequest saved = newItemRequestRepository.save(request);
        return convertToDTO(saved);
    }
    
    @Transactional
    public NewItemRequestDTO approveRequest(Long id, String notes) {
        NewItemRequest request = newItemRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("New item request not found"));
        
        // Create new inventory item from request
        InventoryItem newItem = new InventoryItem();
        newItem.setName(request.getItemName());
        newItem.setCategory(request.getCategory());
        newItem.setQuantity(request.getRequestedQuantity());
        newItem.setUnit(request.getUnit());
        newItem.setDescription(request.getDescription());
        
        inventoryItemRepository.save(newItem);
        
        request.setStatus(NewItemRequest.RequestStatus.APPROVED);
        request.setNotes(notes);
        
        NewItemRequest updated = newItemRequestRepository.save(request);
        return convertToDTO(updated);
    }
    
    @Transactional
    public NewItemRequestDTO rejectRequest(Long id, String notes) {
        NewItemRequest request = newItemRequestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("New item request not found"));
        
        request.setStatus(NewItemRequest.RequestStatus.REJECTED);
        request.setNotes(notes);
        
        NewItemRequest updated = newItemRequestRepository.save(request);
        return convertToDTO(updated);
    }
    
    private NewItemRequestDTO convertToDTO(NewItemRequest request) {
        return new NewItemRequestDTO(
            request.getId(),
            request.getFieldWorker().getId(),
            request.getFieldWorker().getName(),
            request.getItemName(),
            request.getCategory(),
            request.getRequestedQuantity(),
            request.getUnit(),
            request.getDescription(),
            request.getStatus().name(),
            request.getCreatedAt(),
            request.getUpdatedAt(),
            request.getNotes()
        );
    }
}
