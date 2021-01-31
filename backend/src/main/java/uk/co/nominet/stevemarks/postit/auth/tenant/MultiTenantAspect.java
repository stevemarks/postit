package uk.co.nominet.stevemarks.postit.auth.tenant;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Aspect
//@Component
public class MultiTenantAspect {

    private static final Logger LOG = LoggerFactory.getLogger(MultiTenantAspect.class);
    private static final String TENANT_HEADER = "Tenant";

    private final String defaultTenant;

    @Autowired
    public MultiTenantAspect(@Value("${tenants.default.reference}") final String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMappingAdvice() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final String tenant = ((ServletRequestAttributes) requestAttributes).getRequest().getHeader(TENANT_HEADER);
            if (tenant != null && isTenantValid(tenant)) {
                TenantContext.setTenantId(tenant);
            }
        }
    }

    private boolean isTenantValid(final String tenant) {
        return "postit".equalsIgnoreCase(tenant) || "movies".equalsIgnoreCase(tenant);
    }
}
