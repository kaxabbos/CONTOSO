package com.contoso.repo;

import com.contoso.models.Users;
import com.contoso.models.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoUsers extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    List<Users> findByRole(Roles roles);

    Users findByUsernameAndPassword(String username, String password);

}
