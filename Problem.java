import java.io.*;

public class Problem implements Serializable {
    private String question, answer, imgURL;

    public Problem (String q, String a, String i) {
        question = q;
        answer = a;
        imgURL = i;
    }

    public String getQuestion () {
        return question;
    }

    public String getImg () {
        return imgURL;
    }

    public boolean checkAnswer (String input) {
        return answer.equals(input.toLowerCase());
    }
}
