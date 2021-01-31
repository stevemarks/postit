package uk.co.nominet.stevemarks.postit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.nominet.stevemarks.postit.domain.Credentials;
import uk.co.nominet.stevemarks.postit.domain.PostIt;
import uk.co.nominet.stevemarks.postit.service.PostItService;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8081" })
public class PostItController {

    private static final Logger LOG = LoggerFactory.getLogger(PostItController.class);
    private final PostItService postItService;

    @Autowired
    public PostItController(final PostItService postItService) {
        this.postItService = postItService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{username}/postit")
    public ResponseEntity<List<PostIt>> getAllPostIts(@PathVariable final String username,
            final HttpServletRequest request) {
        final Credentials credentials = BasicAuthenticationController.getCredentialsFromRequest(request);
        List<PostIt> notes = new ArrayList<>();
        try {
            notes = postItService.findAllByCredentials(credentials, username).get();
            return new ResponseEntity<>(notes, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Exception reading notes", e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/user/{username}/postit/{id}")
    public ResponseEntity<Void> deletePostIt(@PathVariable String username, @PathVariable long id,
            HttpServletRequest request) {

        Credentials credentials = BasicAuthenticationController.getCredentialsFromRequest(request);
        PostIt postIt = null;

        try {
            postIt = postItService.deleteById(credentials, id).get();
            if (postIt != null) {
                return ResponseEntity.noContent().build();
            }

        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.GONE);
        } catch (Exception e) {
            LOG.error("Exception deleting note:", e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/user/{username}/postit")
    public ResponseEntity<PostIt> create(@PathVariable String username, @RequestBody PostIt postIt,
            HttpServletRequest request) {

        final Credentials credentials = BasicAuthenticationController.getCredentialsFromRequest(request);
        if (!credentials.getUsername().equalsIgnoreCase(username)) {
            LOG.error("Credentials on the path do not match the credentials provided");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        PostIt createdPostIt = null;
        try {
            createdPostIt = postItService.create(credentials, postIt).get();
            return new ResponseEntity<>(createdPostIt, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Exception creating note:", e);
        }
        return new ResponseEntity<>(createdPostIt, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/user/{username}/postit/{id}")
    public ResponseEntity<PostIt> update(@PathVariable String username, @PathVariable long id,
            @RequestBody PostIt postIt, HttpServletRequest request) {

        final Credentials credentials = BasicAuthenticationController.getCredentialsFromRequest(request);
        if (!credentials.getUsername().equalsIgnoreCase(username)) {
            LOG.error("Credentials on the path do not match the credentials provided");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        PostIt postItUpdated = null;
        try {
            postItUpdated = postItService.update(credentials, id, postIt).get();
            return new ResponseEntity<>(postItUpdated, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Exception updating note:", e);
        }
        return new ResponseEntity<>(postItUpdated, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
