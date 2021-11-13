package JavaBot.db;

import JavaBot.data_classes.Word;

import java.util.List;
import java.util.stream.Stream;

public class User {
    public final String id;
    public List<Word> learnedWords;

    public User(String id, List<Word> learnedWords) {
        this.id = id;
        this.learnedWords = learnedWords;
    }

    public void updateLearnedWords(List<Word> newWords) {
        learnedWords = Stream.concat(learnedWords.stream(), newWords.stream()).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
