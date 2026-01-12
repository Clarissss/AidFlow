package com.aidflow.service;

import com.aidflow.dto.RequestDTO;
import com.aidflow.dto.RequestItemDTO;
import com.aidflow.entity.*;
import com.aidflow.repository.InventoryItemRepository;
import com.aidflow.repository.RequestRepository;
import com.aidflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {
    
    @Autowired
    private RequestRepository requestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    public List<RequestDTO> getAllRequests() {
        return requestRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<RequestDTO> getRequestsByFieldWorker(Long fieldWorkerId) {
        User fieldWorker = userRepository.findById(fieldWorkerId)
            .orElseThrow(() -> new RuntimeException("Field worker not found"));
        
        return requestRepository.findByFieldWorker(fieldWorker).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<RequestDTO> getPendingRequests() {
        return requestRepository.findByStatus(Request.RequestStatus.PENDING).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public RequestDTO getRequestById(Long id) {
        Request request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        return convertToDTO(request);
    }
    
    @Transactional
    public RequestDTO createRequest(Long fieldWorkerId, List<RequestItemDTO> items) {
        User fieldWorker = userRepository.findById(fieldWorkerId)
            .orElseThrow(() -> new RuntimeException("Field worker not found"));
        
        Request request = new Request();
        request.setFieldWorker(fieldWorker);
        request.setStatus(Request.RequestStatus.PENDING);
        
        // Add request items
        for (RequestItemDTO itemDTO : items) {
            InventoryItem inventoryItem = inventoryItemRepository.findById(itemDTO.getInventoryItemId())
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));
            
            RequestItem requestItem = new RequestItem();
            requestItem.setRequest(request);
            requestItem.setInventoryItem(inventoryItem);
            requestItem.setQuantity(itemDTO.getQuantity());
            
            request.getItems().add(requestItem);
        }
        
        Request saved = requestRepository.save(request);
        return convertToDTO(saved);
    }
    
    @Transactional
    public RequestDTO approveRequest(Long id, String notes) {
        Request request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        // Check inventory availability
        for (RequestItem item : request.getItems()) {
            InventoryItem inventoryItem = item.getInventoryItem();
            if (inventoryItem.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient inventory for: " + inventoryItem.getName());
            }
        }
        
        // Deduct inventory
        for (RequestItem item : request.getItems()) {
            InventoryItem inventoryItem = item.getInventoryItem();
            inventoryItem.setQuantity(inventoryItem.getQuantity() - item.getQuantity());
            inventoryItemRepository.save(inventoryItem);
        }
        
        request.setStatus(Request.RequestStatus.APPROVED);
        request.setNotes(notes);
        
        Request updated = requestRepository.save(request);
        return convertToDTO(updated);
    }
    
    @Transactional
    public RequestDTO rejectRequest(Long id, String notes) {
        Request request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus(Request.RequestStatus.REJECTED);
        request.setNotes(notes);
        
        Request updated = requestRepository.save(request);
        return convertToDTO(updated);
    }
    
    private RequestDTO convertToDTO(Request request) {
        List<RequestItemDTO> items = request.getItems().stream()
            .map(item -> new RequestItemDTO(
                item.getInventoryItem().getId(),
                item.getInventoryItem().getName(),
                item.getQuantity()
            ))
            .collect(Collectors.toList());
        
        return new RequestDTO(
            request.getId(),
            request.getFieldWorker().getId(),
            request.getFieldWorker().getName(),
            items,
            request.getStatus().name(),
            request.getCreatedAt(),
            request.getUpdatedAt(),
            request.getNotes()
        );
    }
}
