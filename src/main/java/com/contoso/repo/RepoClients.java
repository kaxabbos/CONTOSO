package com.contoso.repo;

import com.contoso.models.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoClients extends JpaRepository<Clients, Long> {
    List<Clients> findAllByOrderByIdDesc();
}
