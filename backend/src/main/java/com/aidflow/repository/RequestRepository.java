package com.aidflow.repository;

import com.aidflow.entity.Request;
import com.aidflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByFieldWorker(User fieldWorker);
    List<Request> findByStatus(Request.RequestStatus status);
}
