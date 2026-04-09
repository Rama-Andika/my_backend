package com.oxysystem.general.repository.db1.admin;

import com.oxysystem.general.model.db1.admin.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String hash);

    @Modifying
    @Query("DELETE FROM RefreshToken r where r.user.userId = :userId")
    void deleteByUser(@Param("userId") Long userId);
}
