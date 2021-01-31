package uk.co.nominet.stevemarks.postit.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.co.nominet.stevemarks.postit.domain.PostIt;
import uk.co.nominet.stevemarks.postit.service.PostItService;
import uk.co.nominet.stevemarks.postit.service.PostItServiceImpl;
import uk.co.nominet.stevemarks.postit.utility.BasicAuth;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;

public class PostItControllerTest {

    private PostItController postItController;
    private PostItService postItService = mock(PostItServiceImpl.class);

    @Before
    public void setup() {
        postItController = new PostItController(postItService);
    }

    @Test
    public void givenANoteToDelete_WhenTheNoteDoesNotExistInTheDatabase_ThenReturnHttpGone() throws Exception {
        long id = 1l;
        when(postItService.deleteById(Mockito.any(), eq(id))).thenReturn(null);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("tester", "password"));

        ResponseEntity<Void> result = postItController.deletePostIt("tester", id, request);

        assertEquals(HttpStatus.GONE.value(), result.getStatusCodeValue());
    }

    @Test
    public void givenANoteToCreate_WhereTheUsernameOnThePathDoesNotMatchTheUsernameInTheNote_ThenReturnBadRequest()
            throws Exception {
        PostIt postit = new PostIt();
        when(postItService.create(Mockito.any(), eq(postit))).thenReturn(null);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("tester", "password"));

        ResponseEntity<PostIt> result = postItController.create("differentUsername", postit, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCodeValue());
        verify(postItService, times(0)).create(Mockito.any(), Mockito.any());
    }

    @Test
    public void givenAExecutionException_WhenGettingANote_thenReturnInternalServerError() throws Exception {
        long id = 1l;

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("admin", "password"));
        when(postItService.deleteById(Mockito.any(), eq(id)))
                .thenThrow(new ExecutionException("Testing a ExecutionException", new IllegalArgumentException()));
        ResponseEntity<Void> result = postItController.deletePostIt("admin", id, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void givenAExecutionException_WhenDeletingANote_thenReturnInternalServerError() throws Exception {
        long id = 1l;

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("admin", "password"));
        when(postItService.deleteById(Mockito.any(), eq(id)))
                .thenThrow(new ExecutionException("Testing a ExecutionException", new IllegalArgumentException()));
        ResponseEntity<Void> result = postItController.deletePostIt("admin", id, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void givenAExecutionException_WhenCreatingANote_thenReturnInternalServerError() throws Exception {
        PostIt postIt = new PostIt();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("admin", "password"));
        when(postItService.create(Mockito.any(), eq(postIt)))
                .thenThrow(new ExecutionException("Testing a ExecutionException", new IllegalArgumentException()));
        ResponseEntity<PostIt> result = postItController.create("admin", postIt, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void givenAExecutionException_WhenUpdatingANote_thenReturnInternalServerError() throws Exception {
        long id = 1l;
        PostIt postIt = new PostIt();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("admin", "password"));
        when(postItService.update(Mockito.any(), eq(id), eq(postIt)))
                .thenThrow(new ExecutionException("Testing a ExecutionException", new IllegalArgumentException()));
        ResponseEntity<PostIt> result = postItController.update("admin", id, postIt, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void givenARequest_WhenAuthenticatedUserDoesNotMatchTheResourcePath_thenReturnAnAuthenticationError() throws Exception {
        long id = 1l;
        PostIt postIt = new PostIt();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("colin", "password"));
        when(postItService.update(Mockito.any(), eq(id), eq(postIt)))
                .thenThrow(new ExecutionException("Testing a ExecutionException", new IllegalArgumentException()));
        ResponseEntity<PostIt> result = postItController.update("steve", id, postIt, request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }
}
