package com.oxysystem.general.service.admin.impl;

import com.oxysystem.general.model.tenant.admin.RefreshToken;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.repository.tenant.admin.RefreshTokenRepository;
import com.oxysystem.general.service.admin.RefreshTokenService;
import com.oxysystem.general.util.RefreshTokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public String create(User user) {
        String rawToken = RefreshTokenGenerator.generate();

        RefreshToken token = new RefreshToken();
        token.setTokenHash(RefreshTokenGenerator.hash(rawToken));
        token.setUser(user);

        refreshTokenRepository.save(token);
        return rawToken; // return KE CLIENT
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public User validateAndRotate(String rawToken) {
        String hash = RefreshTokenGenerator.hash(rawToken);

        RefreshToken token = refreshTokenRepository
                .findByTokenHashAndRevokedFalse(hash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        token.setRevoked(true);
        refreshTokenRepository.save(token);

        return token.getUser();
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        refreshTokenRepository.deleteByUser(userId);
    }
}
