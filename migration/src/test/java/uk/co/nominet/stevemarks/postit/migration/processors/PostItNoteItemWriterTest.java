package uk.co.nominet.stevemarks.postit.migration.processors;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.nominet.stevemarks.postit.domain.PostIt;

public class PostItNoteItemWriterTest {

    private PostItNoteItemWriter postItNoteItemWriter;

    @Before
    public void setup() {
        postItNoteItemWriter = new PostItNoteItemWriter();
    }

    @Test
    public void that() throws Exception {
        List<PostIt> items = new ArrayList<>();
        items.add(new PostIt(1l, "admin", "Do Laundry"));
        postItNoteItemWriter.write(items);
    }
}
