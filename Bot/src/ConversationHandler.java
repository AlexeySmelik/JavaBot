import java.util.Scanner;

public class ConversationHandler {
    private static QuestionHelper dictionary;
    public static boolean stop;

    public static void workItteration(){
        dictionary = new QuestionHelper();
        var question = dictionary.getRandomQuestion();
        System.out.println(question.question);

        Scanner sc = new Scanner(System.in);
        var personAnswer = sc.nextLine();

        getAnswer(personAnswer, question.answer);
    }

    private static void getAnswer(String personAnswer, String questionAnswer){
        if (questionAnswer.equals(personAnswer))
        {
            System.out.println("Правильный ответ");
        }
        else if ("Конец".equals(personAnswer)){
            stop = true;
            System.out.println("Пока-пока!");
        }
        else{
            System.out.println("Ответ неверный");
        }
    }
}


