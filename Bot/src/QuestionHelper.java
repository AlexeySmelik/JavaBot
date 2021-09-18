import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class QuestionHelper {
    private final ArrayList<QuestionForm> questions;
    private final Random random;
    private ArrayList<Integer> notUsedIndexes;

    public QuestionHelper() {
        questions = new ArrayList<>();
        random = new Random();
        setQuestions();
        updateNotUsedIndexes();
    }

    public QuestionForm getRandomQuestion() {
        if (notUsedIndexes.size() == 0)
            updateNotUsedIndexes();
        var randInt = random.nextInt(notUsedIndexes.size());
        var index = notUsedIndexes.get(randInt);
        notUsedIndexes.remove(randInt);
        return questions.get(index);
    }

    private void updateNotUsedIndexes(){
        notUsedIndexes = new ArrayList<>();
        for (var i = 0; i < questions.size(); i++)
            notUsedIndexes.add(i);
    }

    private void setQuestions() {
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
