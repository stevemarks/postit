package uk.co.nominet.stevemarks.postit.utility;

import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuth {

    public static String createBasicAuthHeader(final String username, final String password) {
        String plainCredentials = username + ":" + password;
        byte[] plainCredentialsBytes = plainCredentials.getBytes();
        byte[] base64CredentialsBytes = Base64.encodeBase64(plainCredentialsBytes);
        String base64Credentials = new String(base64CredentialsBytes);
        return "Basic " + base64Credentials;
    }
}
