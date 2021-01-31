package uk.co.nominet.stevemarks.postit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.nominet.stevemarks.postit.domain.Credentials;
import uk.co.nominet.stevemarks.postit.domain.PostIt;
import uk.co.nominet.stevemarks.postit.respository.PostItRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PostItServiceImpl implements PostItService {

    private static final Logger LOG = LoggerFactory.getLogger(PostItServiceImpl.class);
    private final PostItRepository postItRepository;

    @Autowired
    public PostItServiceImpl(final PostItRepository postItRepository) {
        this.postItRepository = postItRepository;
    }

    public CompletableFuture<List<PostIt>> findAllByCredentials(Credentials credentials, String username)
            throws Exception {
        CompletableFuture<List<PostIt>> future = CompletableFuture.supplyAsync(() -> {
            LOG.debug("User: {} requested notes", credentials.getUsername());
            if (!credentials.getUsername().equalsIgnoreCase(username)) {
                throw new IllegalArgumentException("Request is invalid");
            }
            List<PostIt> notes = this.postItRepository.findAllByEmail(credentials.getUsername());
            LOG.debug("User: {} retrieved notes", notes);
            return notes;
        });
        return future;
    }

    public CompletableFuture<PostIt> create(Credentials credentials, PostIt postIt) throws Exception {
        CompletableFuture<PostIt> create = CompletableFuture.supplyAsync(() -> {
            LOG.debug("User: {} requested to create a note", credentials.getUsername());
            if (postIt == null || !credentials.getUsername().equalsIgnoreCase(postIt.getEmail())) {
                throw new IllegalArgumentException("PostIt note is invalid");
            }
            postIt.setId(null);

            PostIt savedPostIt = this.postItRepository.save(postIt);
            LOG.debug("User: {} created a note", savedPostIt);
            return savedPostIt;
        });
        return create;
    }

    public CompletableFuture<PostIt> update(Credentials credentials, Long id, PostIt postIt) throws Exception {
        CompletableFuture<PostIt> update = CompletableFuture.supplyAsync(() -> {
            LOG.debug("User: {} requested to update a note", credentials.getUsername());
            if (postIt == null || id == null || id != postIt.getId()
                    || !credentials.getUsername().equalsIgnoreCase(postIt.getEmail())) {
                throw new IllegalArgumentException("PostIt note is invalid");
            }
            PostIt savedPosit = this.postItRepository.save(postIt);
            LOG.debug("User: {} updated a note", credentials.getUsername());
            return savedPosit;
        });
        return update;
    }

    public CompletableFuture<PostIt> deleteById(Credentials credentials, long id) throws Exception {
        CompletableFuture<PostIt> deleteById = CompletableFuture.supplyAsync(() -> {
            LOG.debug("User: {} requested to delete a note", credentials.getUsername());
            PostIt postIt;
            try {
                CompletableFuture<PostIt> postItCf = findById(credentials, id);
                postIt = postItCf.get();
                if (postIt == null) {
                    return null;
                }

                this.postItRepository.deleteById(id);
                LOG.debug("User: {} deleted a note", postIt);
                return postIt;
            } catch (Exception e) {
                LOG.error("Exception deleting PostitNot", e);
            }
            return null;
        });
        return deleteById;
    }

    public CompletableFuture<PostIt> findById(Credentials credentials, long id) throws Exception {
        final CompletableFuture<PostIt> findById = CompletableFuture.supplyAsync(() -> {
            LOG.debug("User: {} requested to view a note", credentials.getUsername());
            Optional<PostIt> postItOptional = this.postItRepository.findById(id);
            if (postItOptional.isPresent()) {
                LOG.debug("User: {} viewed a note", credentials.getUsername());
                return postItOptional.get();
            }

            return null;
        });

        return findById;
    }
}
