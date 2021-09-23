import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionHelper implements IQuestionGetter {
    private final ArrayList<QuestionForm> questions;

    public QuestionHelper() {
        questions = new ArrayList<>();
        loadQuestions();
    }

    public ArrayList<QuestionForm> get() {
        return questions;
    }

    private void loadQuestions() {
        var file = new File("Bot/src/res", "questions.txt");
        try(var bf = new BufferedReader(new FileReader(file))){
            for (var line : bf.lines().toList())
                parseLine(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLine(String line) {
        var arr = line.split("-");
        questions.add(new QuestionForm(arr[0].trim(), arr[1].trim()));
    }
}
