import java.io.*;
import java.util.*;

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

    public ArrayList<String> parse () {
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 0; i < question.length(); i++) {
            int j = i, k;
            if (question.charAt(i) == '\\' && (i < question.length() - 1 && question.charAt(i + 1) == '(')) {
                k = question.indexOf("\\)", i);
                i = k + 1;
            } else {
                k = question.indexOf("\\(", i);
                if (k == -1)
                    k = question.length();
                i = k - 1;
            }
            res.add(question.substring(j, k));
        }
        return res;
    }
}
