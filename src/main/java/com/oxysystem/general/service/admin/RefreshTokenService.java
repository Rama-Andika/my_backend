package com.oxysystem.general.service.admin;

import com.oxysystem.general.model.tenant.admin.User;

public interface RefreshTokenService {
    String create(User user);
    User validateAndRotate(String rawToken);
    void delete(Long userId);
}
