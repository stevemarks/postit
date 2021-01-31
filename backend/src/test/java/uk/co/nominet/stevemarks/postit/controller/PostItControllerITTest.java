package uk.co.nominet.stevemarks.postit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.co.nominet.stevemarks.postit.domain.PostIt;
import uk.co.nominet.stevemarks.postit.respository.PostItRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.co.nominet.stevemarks.postit.utility.BasicAuth.createBasicAuthHeader;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostItControllerITTest {

    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost:";

    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private PostItRepository postItRepository;

    @Test
    public void contextLoads() {

    }

    @Before
    public void setup() {
        this.postItRepository.deleteAll();
    }

    @Test
    public void givenUserIsLoggedIn_WhenTheyCreateANote_thenCreateTheNote() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", createBasicAuthHeader("tester", "password"));
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        PostIt postIt = new PostIt(null, "tester", "Do Laundry");
        HttpEntity<String> request = new HttpEntity<String>(om.writeValueAsString(postIt), headers);

        ResponseEntity response = restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.POST, request, PostIt.class);
        assertEquals(200, response.getStatusCodeValue());

        List<PostIt> notes = postItRepository.findAll();
        assertEquals(1, notes.size());

        PostIt savedPostIt = notes.get(0);
        assertNotNull(savedPostIt.getId());
        assertEquals(postIt.getEmail(), savedPostIt.getEmail());
        assertEquals(postIt.getText(), savedPostIt.getText());
    }

    @Test
    public void givenUserIsLoggedIn_WhenTheyUpdateANote_thenSaveTheNote() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", createBasicAuthHeader("tester", "password"));
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        PostIt postIt = new PostIt(null, "tester", "Do Laundry");

        HttpEntity<String> createRequest = new HttpEntity<String>(om.writeValueAsString(postIt), headers);
        ResponseEntity<PostIt> createResponse = restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.POST, createRequest, PostIt.class);
        assertEquals(200, createResponse.getStatusCodeValue());
        PostIt createdPostIt = createResponse.getBody();

        createdPostIt.setText("Do Laundry again");
        HttpEntity<String> updateRequest = new HttpEntity<String>(om.writeValueAsString(createdPostIt), headers);
        ResponseEntity updateResponse = restTemplate.exchange(baseUrl + port + "/user/tester/postit/" + createdPostIt.getId(), HttpMethod.PUT, updateRequest, PostIt.class);
        assertEquals(200, updateResponse.getStatusCodeValue());

        List<PostIt> notes = postItRepository.findAll();
        assertEquals(1, notes.size());

        PostIt savedPostIt = notes.get(0);
        assertNotNull(savedPostIt.getId());
        assertEquals(postIt.getEmail(), savedPostIt.getEmail());
        assertEquals("Do Laundry again", savedPostIt.getText());
    }

    @Test
    public void givenUserIsLoggedIn_WhenTheyDeleteANote_thenDeleteTheNote() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", createBasicAuthHeader("tester", "password"));
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        PostIt postIt = new PostIt(null, "tester", "Do Laundry");

        HttpEntity<String> createRequest = new HttpEntity<String>(om.writeValueAsString(postIt), headers);
        ResponseEntity<PostIt> createResponse = restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.POST, createRequest, PostIt.class);
        assertEquals(200, createResponse.getStatusCodeValue());
        PostIt createdPostIt = createResponse.getBody();
        assertEquals(1, this.postItRepository.findAll().size());

        HttpEntity<String> updateRequest = new HttpEntity<String>(headers);
        ResponseEntity updateResponse = restTemplate.exchange(baseUrl + port + "/user/tester/postit/" + createdPostIt.getId(), HttpMethod.DELETE, updateRequest, PostIt.class);
        assertEquals(204, updateResponse.getStatusCodeValue());

        assertEquals(0, this.postItRepository.findAll().size());
    }

    @Test
    public void givenUserIsLoggedIn_WhenTheyRetrieveAllNotes_thenReturnTheNotes() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", createBasicAuthHeader("tester", "password"));
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        final ParameterizedTypeReference<List<PostIt>> responseType = new ParameterizedTypeReference<List<PostIt>>() {
        };

        HttpEntity<String> readRequest = new HttpEntity<String>(headers);
        ResponseEntity<List<PostIt>> readResponse1 = restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.GET, readRequest, responseType);
        assertEquals(200, readResponse1.getStatusCodeValue());
        List<PostIt> readPostIt1 = readResponse1.getBody();
        assertEquals(0, this.postItRepository.findAll().size());
        assertEquals(0, readPostIt1.size());

        PostIt postIt = new PostIt(null, "tester", "Do Laundry");
        HttpEntity<String> request = new HttpEntity<String>(om.writeValueAsString(postIt), headers);
        restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.POST, request, PostIt.class);
        restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.POST, request, PostIt.class);
        restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.POST, request, PostIt.class);
        restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.POST, request, PostIt.class);
        restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.POST, request, PostIt.class);

        ResponseEntity<List<PostIt>> readResponse2 = restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.GET, readRequest, responseType);
        assertEquals(200, readResponse2.getStatusCodeValue());
        List<PostIt> readPostIt2 = readResponse2.getBody();
        assertEquals(5, this.postItRepository.findAll().size());
        assertEquals(5, readPostIt2.size());
    }

    @Test(expected = HttpClientErrorException.class)
    public void givenUserIsNotLoggedIn_WhenTheyRetrieveAllNotes_thenReturnAnError() {
        RestTemplate restTemplate = new RestTemplate();
        final ParameterizedTypeReference<List<PostIt>> responseType = new ParameterizedTypeReference<List<PostIt>>() {
        };

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> readRequest = new HttpEntity<String>(headers);
        ResponseEntity<List<PostIt>> readResponse1 = restTemplate.exchange(baseUrl + port + "/user/tester/postit", HttpMethod.GET, readRequest, responseType);
        assertEquals(401, readResponse1.getStatusCodeValue());
    }
}
