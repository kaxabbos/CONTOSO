package com.contoso.repo;

import com.contoso.models.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RepoClients extends JpaRepository<Clients, Long> {
}
