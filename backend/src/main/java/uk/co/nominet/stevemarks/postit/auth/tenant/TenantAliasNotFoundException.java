package uk.co.nominet.stevemarks.postit.auth.tenant;

public class TenantAliasNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 2041210814327906719L;

    public TenantAliasNotFoundException(final String message) {
        super(message);
    }
}
