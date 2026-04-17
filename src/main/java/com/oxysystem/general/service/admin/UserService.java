package com.oxysystem.general.service.admin;

import com.oxysystem.general.model.tenant.admin.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> search(String username, String password, String token);
    Optional<User> findUserAdmin();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User save(User user);
}
