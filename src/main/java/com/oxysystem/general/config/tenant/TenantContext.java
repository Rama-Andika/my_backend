package com.oxysystem.general.config.tenant;

public class TenantContext {

    // Menyimpan ID Klien secara spesifik untuk setiap Thread yang sedang berjalan
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        // SANGAT PENTING: Mencegah kebocoran memori (Memory Leak)
        CURRENT_TENANT.remove();
    }
}