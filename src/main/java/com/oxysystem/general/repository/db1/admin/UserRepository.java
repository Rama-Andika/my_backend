package com.oxysystem.general.repository.db1.admin;


import com.oxysystem.general.model.db1.admin.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u.* FROM sysuser u WHERE LOWER(u.login_id) = :username LIMIT 1", nativeQuery = true)
    Optional<User> findUserByUsername(@Param("username") String username);

    @Query(value = "SELECT u.* FROM sysuser u WHERE LOWER(u.login_id) = 'superuser' LIMIT 1",nativeQuery = true)
    Optional<User> findUserAdmin();

    List<User> findAll(Specification<User> spec);
}
