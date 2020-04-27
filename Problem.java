public class Problem {
    private String question, answer, imgURL;

    public Problem (String q, String a, String i) {
        question = q;
        answer = a;
        imgURL = i;
    }

    public String getQuestion () {
        return question;
    }

    public boolean checkAnswer (String input) {
        return answer.equals(input.toLowerCase());
    }

    public String toString () {
        return question + " " + answer + ((imgURL != null) ? " " + imgURL : "");
    }
}
