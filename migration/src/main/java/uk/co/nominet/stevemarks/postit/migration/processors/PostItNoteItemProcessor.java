package uk.co.nominet.stevemarks.postit.migration.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import uk.co.nominet.stevemarks.postit.domain.PostIt;

public class PostItNoteItemProcessor implements ItemProcessor<PostIt, PostIt> {
    private static final Logger log = LoggerFactory.getLogger(PostItNoteItemProcessor.class);

    @Override
    public PostIt process(final PostIt postIt) throws Exception {
        final String email = postIt.getEmail();
        final String text = postIt.getText();

        if (!StringUtils.hasText(email) || !StringUtils.hasText(text)) {
            throw new IllegalArgumentException("Email and text must not be empty.");
        }

        final PostIt transformedPostItNote = new PostIt(null, email, text);

        log.info("Converting (" + postIt + ") into (" + transformedPostItNote + ")");

        return transformedPostItNote;
    }
}
