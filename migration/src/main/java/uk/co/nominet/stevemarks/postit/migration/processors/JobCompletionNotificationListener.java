package uk.co.nominet.stevemarks.postit.migration.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.co.nominet.stevemarks.postit.domain.PostIt;
import uk.co.nominet.stevemarks.postit.utility.BasicAuth;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOG.info("!!! JOB FINISHED! Time to verify the results");

            final Map<String, String> parameters = new HashMap<>();
            parameters.put("username", "admin");
            final Flux<PostIt> postItFlux = WebClient.create().get()
                    .uri("http://localhost:8080/user/{username}/postit", parameters)
                    .header(HttpHeaders.AUTHORIZATION, BasicAuth.createBasicAuthHeader("admin", "password")).retrieve()
                    .bodyToFlux(PostIt.class);
            List<PostIt> notes = postItFlux.collectList().block();
            notes.forEach(p -> {
                LOG.info("Migrated PostIt: {}", p);
            });
            /*
             * jdbcTemplate .query("SELECT email, text FROM postit", (rs, row) -> new
             * PostIt(null, rs.getString(1), rs.getString(2))) .forEach(postit ->
             * log.info("Found <" + postit + "> in the database."));
             */
        }
    }
}
