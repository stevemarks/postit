package uk.co.nominet.stevemarks.postit.auth.tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {
    private static final Logger LOG = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        LOG.debug("Setting tenantId to " + tenantId);
        CONTEXT.set(tenantId);
    }

    public static String getCurrentTenant() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}