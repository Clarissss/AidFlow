package com.aidflow.controller;

import com.aidflow.dto.NewItemRequestDTO;
import com.aidflow.service.NewItemRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/new-item-requests")
@CrossOrigin(origins = "http://localhost:4200")
public class NewItemRequestController {
    
    @Autowired
    private NewItemRequestService newItemRequestService;
    
    @GetMapping
    public ResponseEntity<List<NewItemRequestDTO>> getAllRequests(
            @RequestParam(required = false) Long fieldWorkerId) {
        List<NewItemRequestDTO> requests;
        if (fieldWorkerId != null) {
            requests = newItemRequestService.getRequestsByFieldWorker(fieldWorkerId);
        } else {
            requests = newItemRequestService.getAllRequests();
        }
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<NewItemRequestDTO>> getPendingRequests() {
        List<NewItemRequestDTO> requests = newItemRequestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NewItemRequestDTO> getRequestById(@PathVariable Long id) {
        try {
            NewItemRequestDTO request = newItemRequestService.getRequestById(id);
            return ResponseEntity.ok(request);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<NewItemRequestDTO> createRequest(
            @RequestParam Long fieldWorkerId,
            @RequestBody NewItemRequestDTO dto) {
        try {
            NewItemRequestDTO created = newItemRequestService.createRequest(fieldWorkerId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<NewItemRequestDTO> approveRequest(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String notes = body != null ? body.get("notes") : null;
            NewItemRequestDTO updated = newItemRequestService.approveRequest(id, notes);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<NewItemRequestDTO> rejectRequest(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String notes = body != null ? body.get("notes") : null;
            NewItemRequestDTO updated = newItemRequestService.rejectRequest(id, notes);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
