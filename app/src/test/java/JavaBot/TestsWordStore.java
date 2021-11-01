package JavaBot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

public class TestsWordStore {

    @Test
    public void checkWordStoreGetDict() throws IOException {
        var wordStore = new WordStore();
        var topics = wordStore.getTopics();

        Assertions.assertEquals(3, topics.size());
        Assertions.assertEquals(72, wordStore.get(topics.get(0)).size());
        Assertions.assertEquals(54, wordStore.get(topics.get(1)).size());
        Assertions.assertEquals(75, wordStore.get(topics.get(2)).size());
    }
}
