package JavaBot.ux;

public class QuestionForm {
    public String question;
    public String answer;
    public String hint;

    public QuestionForm(String question, String answer) {
        this.question = question;
        this.answer = answer;
        hint = "*".repeat(answer.length());
    }

    public void UpdateHint(){
        if(hint.chars().filter(ch -> ch == '*').count() == 0) {
            return;
        }
        var index = hint.lastIndexOf('*');
        hint =  hint.substring(0, index) + answer.charAt(index) + hint.substring(index+1);
    }
}
