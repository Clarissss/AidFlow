package com.aidflow.controller;

import com.aidflow.dto.DonationDTO;
import com.aidflow.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "http://localhost:4200")
public class DonationController {
    
    @Autowired
    private DonationService donationService;
    
    @GetMapping
    public ResponseEntity<List<DonationDTO>> getAllDonations() {
        List<DonationDTO> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<DonationDTO>> getPendingDonations() {
        List<DonationDTO> donations = donationService.getPendingDonations();
        return ResponseEntity.ok(donations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DonationDTO> getDonationById(@PathVariable Long id) {
        try {
            DonationDTO donation = donationService.getDonationById(id);
            return ResponseEntity.ok(donation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<DonationDTO> createDonation(@RequestBody DonationDTO dto) {
        try {
            DonationDTO created = donationService.createDonation(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/verify")
    public ResponseEntity<DonationDTO> verifyDonation(@PathVariable Long id) {
        try {
            DonationDTO updated = donationService.verifyDonation(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
