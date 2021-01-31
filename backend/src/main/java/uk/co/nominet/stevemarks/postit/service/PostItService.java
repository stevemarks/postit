package uk.co.nominet.stevemarks.postit.service;

import uk.co.nominet.stevemarks.postit.domain.Credentials;
import uk.co.nominet.stevemarks.postit.domain.PostIt;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PostItService {

    CompletableFuture<List<PostIt>> findAllByCredentials(Credentials credentials, String username) throws Exception;

    CompletableFuture<PostIt> deleteById(Credentials credentials, long id) throws Exception;

    CompletableFuture<PostIt> create(Credentials credentials, PostIt postIt) throws Exception;

    CompletableFuture<PostIt> update(Credentials credentials, Long id, PostIt postIt) throws Exception;

    CompletableFuture<PostIt> findById(Credentials credentials, long id) throws Exception;
}
