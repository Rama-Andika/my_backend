package com.oxysystem.general.filter;

import com.oxysystem.general.config.tenant.TenantContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Tangkap X-Tenant-ID dari header
        String tenantId = request.getHeader("X-Tenant-ID");

        // 2. Set Context
        if (tenantId != null && !tenantId.trim().isEmpty()) {
            TenantContext.setCurrentTenant(tenantId);
        } else {
            // Jika kosong (misal request ke /auth/login pertama kali),
            // set ke database master
            TenantContext.setCurrentTenant("master");
        }

        try {
            // 3. Lanjutkan ke filter berikutnya (JwtAuthorizationFilter)
            filterChain.doFilter(request, response);
        } finally {
            // 4. WAJIB: Bersihkan context agar tidak memory leak
            TenantContext.clear();
        }
    }
}