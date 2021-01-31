package uk.co.nominet.stevemarks.postit.auth.tenant;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import uk.co.nominet.stevemarks.postit.auth.tenant.TenantDataSource;

@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final String DEFAULT_TENANT_STRING = "default";

    @Autowired
    private DataSource defaultDS;

    @Autowired
    private ApplicationContext context;

    private Map<String, DataSource> map = new HashMap<>();

    boolean init = false;

    @PostConstruct
    public void load() {
        map.put(DEFAULT_TENANT_STRING, defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(DEFAULT_TENANT_STRING);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        if (!init) {
            init = true;
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            map.putAll(tenantDataSource.getAll());
        }
        return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(DEFAULT_TENANT_STRING);
    }
}
