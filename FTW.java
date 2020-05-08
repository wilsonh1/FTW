import java.io.*;

public class FTW {
    private static final String questions = "data/questions.txt";
    private static final String answers = "data/answers.txt";
    private static final String images = "data/images.txt";

    private static ProblemSet ps;

    public static void main (String[] args) throws Exception {
        ps = new ProblemSet(questions, answers, images);
        GameSettings gs = new GameSettings();
        FTWWindow window = new FTWWindow(gs);
        while (true) {
            gs.setDone(false);
            //window.show();
            while (!gs.isDone());
            if (gs.isJoin())
                joinGame(gs.getIP());
            else
                startGame(gs.isMulti(), gs.getCount(), gs.getTime());
        }
    }

    public static int getSize () {
        return ps.getSize();
    }

    public static void startGame (boolean isMulti, int n, int t) throws Exception {
        Game g;
        GameWindow gw = new GameWindow();
        Problem[] p = ps.getProblems(n);
        if (isMulti)
            g = new MultiPlayerServer(p, n, t, gw);
        else
            g = new SinglePlayer(p, n, t, gw);
        g.run();
    }

    public static void joinGame (String ip) throws Exception {
        GameWindow gw = new GameWindow();
        Game g = new MultiPlayerClient(ip, gw);
        g.run();
    }

    public static void prompt () {
        System.out.print(">>> ");
    }

    public static void prompt (String s) {
        System.out.println(s);
        prompt();
    }

}
