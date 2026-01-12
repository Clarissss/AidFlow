package com.aidflow.controller;

import com.aidflow.dto.RequestDTO;
import com.aidflow.dto.RequestItemDTO;
import com.aidflow.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:4200")
public class RequestController {
    
    @Autowired
    private RequestService requestService;
    
    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllRequests(
            @RequestParam(required = false) Long fieldWorkerId) {
        List<RequestDTO> requests;
        if (fieldWorkerId != null) {
            requests = requestService.getRequestsByFieldWorker(fieldWorkerId);
        } else {
            requests = requestService.getAllRequests();
        }
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<RequestDTO>> getPendingRequests() {
        List<RequestDTO> requests = requestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getRequestById(@PathVariable Long id) {
        try {
            RequestDTO request = requestService.getRequestById(id);
            return ResponseEntity.ok(request);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(
            @RequestParam Long fieldWorkerId,
            @RequestBody List<RequestItemDTO> items) {
        try {
            RequestDTO created = requestService.createRequest(fieldWorkerId, items);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<RequestDTO> approveRequest(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String notes = body != null ? body.get("notes") : null;
            RequestDTO updated = requestService.approveRequest(id, notes);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<RequestDTO> rejectRequest(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String notes = body != null ? body.get("notes") : null;
            RequestDTO updated = requestService.rejectRequest(id, notes);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
