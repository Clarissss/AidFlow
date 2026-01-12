package com.aidflow.repository;

import com.aidflow.entity.NewItemRequest;
import com.aidflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewItemRequestRepository extends JpaRepository<NewItemRequest, Long> {
    List<NewItemRequest> findByFieldWorker(User fieldWorker);
    List<NewItemRequest> findByStatus(NewItemRequest.RequestStatus status);
}
