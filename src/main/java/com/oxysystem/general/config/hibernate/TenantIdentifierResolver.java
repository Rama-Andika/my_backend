package com.oxysystem.general.config.hibernate;

import com.oxysystem.general.config.tenant.TenantContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            return tenantId;
        }
        // Jika karena suatu alasan tidak ada ID, arahkan ke master agar tidak crash
        return "master";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}