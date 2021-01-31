package uk.co.nominet.stevemarks.postit.migration.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import uk.co.nominet.stevemarks.postit.domain.PostIt;
import uk.co.nominet.stevemarks.postit.utility.BasicAuth;

public class PostItNoteItemWriter implements ItemWriter<PostIt> {

    private static final Logger LOG = LoggerFactory.getLogger(PostItNoteItemWriter.class);

    @Override
    public void write(final List<? extends PostIt> items) throws Exception {
        for (final PostIt postIt : items) {
            final Map<String, String> parameters = new HashMap<>();
            parameters.put("username", postIt.getEmail());
            final Flux<PostIt> postItFlux = WebClient.create().post()
                    .uri("http://localhost:8080/user/{username}/postit", parameters).bodyValue(postIt)
                    .header(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("admin", "password")).retrieve()
                    .bodyToFlux(PostIt.class);

            Disposable d = postItFlux.subscribe();
            d.dispose();
            PostIt p = postItFlux.blockFirst();
            LOG.info("Saved PostIt note:{}", p);
        }
    }
}
