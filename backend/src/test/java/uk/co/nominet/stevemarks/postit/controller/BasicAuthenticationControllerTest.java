package uk.co.nominet.stevemarks.postit.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;

public class BasicAuthenticationControllerTest {

    @Test(expected = BadCredentialsException.class)
    public void givenRequest_WhenTheCredentialsAreMalformed_ThenReturnBadCredentialsException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "OAUTH2");
        BasicAuthenticationController.getCredentialsFromRequest(request);
        Assert.fail();
    }
}
