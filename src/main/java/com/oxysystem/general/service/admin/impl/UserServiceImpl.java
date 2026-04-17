package com.oxysystem.general.service.admin.impl;

import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.repository.tenant.admin.UserRepository;
import com.oxysystem.general.service.admin.UserService;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> search(String username, String password, String token){
        return this.userRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(username != null && !username.isEmpty()){
                predicates.add(cb.equal(root.get("username"), username));
            }
            if(password != null && !password.isEmpty()){
                predicates.add(cb.equal(root.get("password"), password));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public Optional<User> findUserAdmin() {
        return userRepository.findUserAdmin();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
