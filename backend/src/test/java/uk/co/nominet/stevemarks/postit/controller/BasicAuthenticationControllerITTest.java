package uk.co.nominet.stevemarks.postit.controller;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.co.nominet.stevemarks.postit.auth.basic.AuthenticationBean;

import static org.junit.Assert.assertEquals;
import static uk.co.nominet.stevemarks.postit.utility.BasicAuth.createBasicAuthHeader;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationControllerITTest {

    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost:";

    @Test
    public void givenUserIsLoggedIn_WhenTheyCallTheAuthenticateEndpoint_thenReturnSuccess() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", createBasicAuthHeader("tester", "password"));

        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<AuthenticationBean> response = restTemplate.exchange(baseUrl + port + "/basicauth", HttpMethod.GET, request, AuthenticationBean.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test(expected = Exception.class)
    public void givenUserIsNotLoggedIn_WhenTheyCallTheAuthenticateEndpoint_thenReturnSuccess() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<AuthenticationBean> response = restTemplate.exchange(baseUrl + port + "/basicauth", HttpMethod.GET, request, AuthenticationBean.class);
        Assert.fail();
    }
}
