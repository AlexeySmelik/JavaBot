import java.util.Scanner;

public class Main {
    public static void main(String [] args){
        var q = new QuestionHelper();
        for (var i = 0; i < 3; i++) {
            var form = q.getRandomQuestion();
            System.out.println(String.format("Вопрос: %s \nCorrect answer: %s\n", form.question, form.answer));
        }

        var c = new ConversationHandler();
        while(!c.stop){
            c.workItteration();
        }
    }
}
