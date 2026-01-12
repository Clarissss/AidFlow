package com.aidflow.config;

import com.aidflow.entity.InventoryItem;
import com.aidflow.entity.User;
import com.aidflow.repository.InventoryItemRepository;
import com.aidflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize default users if not exists
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("Admin User");
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            
            User fieldWorker = new User();
            fieldWorker.setUsername("fieldworker");
            fieldWorker.setPassword(passwordEncoder.encode("field123"));
            fieldWorker.setName("Field Worker");
            fieldWorker.setRole(User.Role.FIELD_WORKER);
            fieldWorker.setActive(true);
            userRepository.save(fieldWorker);
        }
        
        // Initialize sample inventory if not exists
        if (inventoryItemRepository.count() == 0) {
            InventoryItem item1 = new InventoryItem();
            item1.setName("Selimut");
            item1.setCategory("Pakaian");
            item1.setQuantity(150);
            item1.setUnit("buah");
            item1.setDescription("Selimut untuk korban bencana");
            item1.setCreatedAt(LocalDateTime.now());
            inventoryItemRepository.save(item1);
            
            InventoryItem item2 = new InventoryItem();
            item2.setName("Tenda");
            item2.setCategory("Shelter");
            item2.setQuantity(30);
            item2.setUnit("buah");
            item2.setDescription("Tenda untuk pengungsian");
            item2.setCreatedAt(LocalDateTime.now());
            inventoryItemRepository.save(item2);
            
            InventoryItem item3 = new InventoryItem();
            item3.setName("Makanan Instan");
            item3.setCategory("Makanan");
            item3.setQuantity(500);
            item3.setUnit("paket");
            item3.setDescription("Makanan siap saji");
            item3.setCreatedAt(LocalDateTime.now());
            inventoryItemRepository.save(item3);
            
            InventoryItem item4 = new InventoryItem();
            item4.setName("Air Mineral");
            item4.setCategory("Minuman");
            item4.setQuantity(1000);
            item4.setUnit("botol");
            item4.setDescription("Air minum kemasan");
            item4.setCreatedAt(LocalDateTime.now());
            inventoryItemRepository.save(item4);
        }
    }
}
