package uk.co.nominet.stevemarks.postit.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.nominet.stevemarks.postit.domain.Credentials;
import uk.co.nominet.stevemarks.postit.domain.PostIt;
import uk.co.nominet.stevemarks.postit.respository.PostItRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PostItServiceTest {

    private PostItService postItService;

    private PostItRepository postItRepository = mock(PostItRepository.class);

    @Before
    public void setup() {
        this.postItService = new PostItServiceImpl(postItRepository);
    }

    @Test
    public void givenARequestToReadAllNotes_WhenThereAreNotesInTheDatabase_ThenReturnTheNotes() throws Exception {
        String username = "tester";
        Credentials credentials = new Credentials(username, "password");
        PostIt postIt1 = new PostIt(1l, username, "Do Laundry");
        PostIt postIt2 = new PostIt(2l, username, "Do Laundry");

        List<PostIt> positNotes = new ArrayList<>();
        positNotes.add(postIt1);
        positNotes.add(postIt2);

        when(postItRepository.findAllByEmail(username)).thenReturn(positNotes);
        CompletableFuture<List<PostIt>> future = postItService.findAllByCredentials(credentials, username);
        List<PostIt> result = future.get();

        assertEquals(2, result.size());
        verify(postItRepository, times(1)).findAllByEmail(username);
    }

    @Test
    public void givenARequestToReadAllNotes_WhenThereAreNoNotesInTheDatabase_ThenReturnAnEmptyList() throws Exception {
        String username = "tester";
        Credentials credentials = new Credentials(username, "password");

        List<PostIt> positNotes = new ArrayList<>();
        when(postItRepository.findAll()).thenReturn(positNotes);
        List<PostIt> result = postItService.findAllByCredentials(credentials, username).get();

        assertEquals(0, result.size());
        verify(postItRepository, times(1)).findAllByEmail(username);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenARequestToReadAllNotesForAUsernameThatDiffersToTheAuthenticatedUser_ThenReturnAnException()
            throws Throwable {
        String authenticatedUsername = "tester";
        String usernamePassedInRequest = "anotherUsername";
        Credentials credentials = new Credentials(authenticatedUsername, "password");
        PostIt postIt1 = new PostIt(1l, usernamePassedInRequest, "Do Laundry");
        PostIt postIt2 = new PostIt(2l, usernamePassedInRequest, "Do Laundry");

        List<PostIt> positNotes = new ArrayList<>();
        positNotes.add(postIt1);
        positNotes.add(postIt2);

        when(postItRepository.findAll()).thenReturn(positNotes);
        CompletableFuture<List<PostIt>> future = postItService.findAllByCredentials(credentials,
                usernamePassedInRequest);
        try {
            future.get();
        } catch (Exception e) {
            Throwable ae = e.getCause();
            throw ae;
        }
        Assert.fail();
    }

    @Test
    public void givenACreatePostItRequest_WhenTheAuthenticationCredentialsMatchThePath_ThenCreateTheNote()
            throws Exception {
        String username = "tester";
        Credentials credentials = new Credentials(username, "password");
        PostIt postIt = new PostIt(null, username, "Do Laundry");

        when(postItRepository.save(postIt)).thenReturn(postIt);
        PostIt result = postItService.create(credentials, postIt).get();

        assertEquals(postIt, result);
        verify(postItRepository, times(1)).save(postIt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenACreatePostItRequest_WhenTheAuthenticationCredentialsDoesNotMatchThePath_ThenThrowException()
            throws Throwable {
        String username = "tester";
        Credentials credentials = new Credentials(username + "1", "password");
        PostIt postIt = new PostIt(null, username + "2", "Do Laundry");

        when(postItRepository.save(postIt)).thenReturn(postIt);

        CompletableFuture<PostIt> future = postItService.create(credentials, postIt);
        try {
            future.get();
        } catch (ExecutionException e) {
            Throwable ae = e.getCause();
            throw ae;
        }

        Assert.fail();
    }

    @Test
    public void givenAUpdatePostItRequest_WhenTheAuthenticationCredentialsMatchThePath_ThenCreateTheNote()
            throws Exception {
        String username = "tester";
        Credentials credentials = new Credentials(username, "password");
        long id = 0l;
        PostIt postIt = new PostIt(id, username, "Do Laundry");

        when(postItRepository.save(postIt)).thenReturn(postIt);
        PostIt result = postItService.update(credentials, id, postIt).get();

        assertEquals(postIt, result);
        verify(postItRepository, times(1)).save(postIt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenAUpdatePostItRequest_WhenTheAuthenticationCredentialsDoesNotMatchThePath_ThenThrowException()
            throws Throwable {
        String username = "tester";
        Credentials credentials = new Credentials(username + "1", "password");
        long id = 0l;
        PostIt postIt = new PostIt(id, username + "2", "Do Laundry");

        when(postItRepository.save(postIt)).thenReturn(postIt);
        CompletableFuture<PostIt> future = postItService.update(credentials, id, postIt);

        try {
            future.get();
        } catch (ExecutionException e) {
            Throwable ae = e.getCause();
            throw ae;
        }
        Assert.fail();
    }

    @Test
    public void givenANoteId_WhenTheNoteDoesNotExistInTheDatabase_ThenReturnNull() throws Exception {
        long id = 0l;
        String username = "tester";
        Credentials credentials = new Credentials(username, "password");
        when(postItRepository.findById(id)).thenReturn(Optional.empty());
        PostIt postIt = postItService.deleteById(credentials, id).get();

        assertNull(postIt);
    }

    @Test
    public void givenANoteId_WhenTheNoteExistsInTheDatabase_ThenDelete() throws Exception {
        long id = 0l;
        PostIt postItFromDatabase = new PostIt();
        postItFromDatabase.setId(id);
        when(postItRepository.findById(id)).thenReturn(Optional.of(postItFromDatabase));
        Credentials credentials = new Credentials("tester", "password");
        PostIt result = postItService.deleteById(credentials, id).get();
        assertEquals(postItFromDatabase, result);

        verify(postItRepository, times(1)).deleteById(id);
    }
}
