package com.aidflow.service;

import com.aidflow.dto.DonationDTO;
import com.aidflow.entity.Donation;
import com.aidflow.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationService {
    
    @Autowired
    private DonationRepository donationRepository;
    
    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<DonationDTO> getPendingDonations() {
        return donationRepository.findByStatus(Donation.DonationStatus.PENDING).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public DonationDTO getDonationById(Long id) {
        Donation donation = donationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Donation not found"));
        return convertToDTO(donation);
    }
    
    @Transactional
    public DonationDTO createDonation(DonationDTO dto) {
        Donation donation = new Donation();
        donation.setDonorName(dto.getDonorName());
        donation.setEmail(dto.getEmail());
        donation.setPhone(dto.getPhone());
        donation.setAmount(dto.getAmount());
        donation.setItem(dto.getItem());
        donation.setItemQuantity(dto.getItemQuantity());
        donation.setMessage(dto.getMessage());
        donation.setStatus(Donation.DonationStatus.PENDING);
        
        Donation saved = donationRepository.save(donation);
        return convertToDTO(saved);
    }
    
    @Transactional
    public DonationDTO verifyDonation(Long id) {
        Donation donation = donationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        donation.setStatus(Donation.DonationStatus.VERIFIED);
        Donation updated = donationRepository.save(donation);
        return convertToDTO(updated);
    }
    
    private DonationDTO convertToDTO(Donation donation) {
        return new DonationDTO(
            donation.getId(),
            donation.getDonorName(),
            donation.getEmail(),
            donation.getPhone(),
            donation.getAmount(),
            donation.getItem(),
            donation.getItemQuantity(),
            donation.getMessage(),
            donation.getStatus().name(),
            donation.getCreatedAt()
        );
    }
}
