package com.contoso.repo;

import com.contoso.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoUsers extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    List<Users> findAllByOrderByRole();
}
