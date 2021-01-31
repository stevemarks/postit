package uk.co.nominet.stevemarks.postit.auth.tenant;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(TenantInterceptor.class);
    private static final String TENANT_HEADER = "Tenant";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                String tenantId = request.getServerName();
                LOG.info("tenantId: {}", tenantId);
                if (tenantId != null && !tenantId.isEmpty()) {
                    TenantContext.setTenantId(tenantId);
                    LOG.info("Tenant header get: {}", tenantId);
                } else {
                    LOG.error("Tenant header not found.");
                    throw new TenantAliasNotFoundException("Tenant header not found.");
                }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        TenantContext.clear();
    }
}