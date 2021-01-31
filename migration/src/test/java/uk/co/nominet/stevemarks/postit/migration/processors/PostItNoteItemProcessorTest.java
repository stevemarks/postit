package uk.co.nominet.stevemarks.postit.migration.processors;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.nominet.stevemarks.postit.domain.PostIt;

public class PostItNoteItemProcessorTest {

    private PostItNoteItemProcessor postItNoteItemProcessor;

    @Before
    public void setup() {
        postItNoteItemProcessor = new PostItNoteItemProcessor();
    }

    @Test
    public void givenAValidPostIt_withARequirementToNotModifiyThePostIt_thenEnsureThatThePostItContentsHasNotChanged()
            throws Exception {
        PostIt input = new PostIt(1l, "admin", "Do Laundry");
        PostIt result = postItNoteItemProcessor.process(input);

        assertNull(result.getId());
        assertEquals("admin", result.getEmail());
        assertEquals("Do Laundry", result.getText());

        // enusre that the processor creates a new Object.
        assertNotEquals(input, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenAPostIt_withoutAEmail_thenThrowAnError() throws Exception {
        PostIt input = new PostIt(1l, null, "Do Laundry");
        postItNoteItemProcessor.process(input);

        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenAPostIt_withoutText_thenThrowAnError() throws Exception {
        PostIt input = new PostIt(1l, "admin", null);
        postItNoteItemProcessor.process(input);

        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenAPostIt_withoutAnEmailOrText_thenThrowAnError() throws Exception {
        PostIt input = new PostIt(1l, null, null);
        postItNoteItemProcessor.process(input);

        Assert.fail();
    }
}
