import java.io.*;

public class FTWTest {
    private static final String questions = "data/questions.txt";
    private static final String answers = "data/answers.txt";
    private static final String images = "data/images.txt";

    private static ProblemSet ps;

    public static void main (String[] args) throws Exception {
        ps = new ProblemSet(questions, answers, images);
        FTWWindow window = new FTWWindow();
    }

    public static void startGame (boolean isMulti, int n, int t) throws Exception {
        Game g;
        Problem[] p = ps.getProblems(n);
        if (isMulti)
            g = new MultiPlayerServer(p, n, t);
        else
            g = new SinglePlayer(p, n, t);
        g.run();
    }

    public static void joinGame (String ip) throws Exception {
        Game g = new MultiPlayerClient(ip);
        g.run();
    }
}
