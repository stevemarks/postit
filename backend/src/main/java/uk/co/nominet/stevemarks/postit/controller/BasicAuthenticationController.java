package uk.co.nominet.stevemarks.postit.controller;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.nominet.stevemarks.postit.auth.basic.AuthenticationBean;
import uk.co.nominet.stevemarks.postit.domain.Credentials;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class BasicAuthenticationController {

    @GetMapping(path = "/basicauth")
    public AuthenticationBean testAuthentication() {
        return new AuthenticationBean("You are authenticated");
    }

    public static final Credentials getCredentialsFromRequest(final HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.toLowerCase().startsWith("basic")) {
            String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);

            final String[] values = credentials.split(":", 2);
            return new Credentials(values[0], values[1]);
        }
        throw new BadCredentialsException("The credentials provided are invalid.");
    }
}
