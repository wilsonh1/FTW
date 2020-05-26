import java.io.*;

public class FTW {
    private static final String questions = "data/questions.txt";
    private static final String answers = "data/answers.txt";
    private static final String images = "data/images.txt";

    private static ProblemSet ps;
    private static FTWWindow window;

    public static void main (String[] args) throws Exception {
        ps = new ProblemSet(questions, answers, images);
        GameSettings gs = new GameSettings();
        window = new FTWWindow(gs);
        while (true) {
            gs.setDone(false);
            while (!gs.isDone());
            gs.setFinished(false);
            if (gs.isJoin())
                joinGame(gs.getIP(), gs.getName());
            else
                startGame(gs.isMulti(), gs.getCount(), gs.getTime(), gs.getName());
            gs.setFinished(true);
        }
    }

    public static int getSize () {
        return ps.getSize();
    }

    public static void show () {
        window.show();
    }

    public static void startGame (boolean isMulti, int n, int t, String name) throws Exception {
        Game g;
        Problem[] p = ps.getProblems(n);
        if (isMulti)
            g = new MultiPlayerServer(p, n, t, name);
        else
            g = new SinglePlayer(p, n, t);
        g.run();
    }

    public static void joinGame (String ip, String name) throws IOException {
        Game g = new MultiPlayerClient(ip, name);
        try {
            g.run();
        } catch (Exception e) {
            g.displayMessage("Disconnected from host", true);
            g.showClose();
        }
    }
}
