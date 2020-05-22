import javax.swing.*;//SwingUtilities.*;
import java.util.concurrent.atomic.*;

public abstract class Game {
    private Problem[] problems;
    private int cnt, time;
    private int index;
    private GameWindow window;
    private AtomicBoolean active;

    public Game () {
        cnt = time = -1;
        active = new AtomicBoolean(true);
        window = new GameWindow(active);
    }

    public Game (Problem[] p, int n, int t) {
        this();
        problems = p;
        cnt = n;
        time = t;
    }

    protected int getCount () {
        return cnt;
    }

    protected int getTime () {
        return time;
    }

    protected void setTime (int t) {
        time = t;
    }

    protected Problem getProblemByIndex (int index) {
        return problems[index];
    }

    protected boolean isActive () {
        return active.get();
    }

    protected void displayName (String name) {
        window.displayName(name);
    }

    protected void displayMessage (String m, boolean flag) {
        window.displayMessage(m, flag);
    }

    protected void updateSide (String m, boolean flag) {
        window.updateSide(m, flag);
    }

    protected void displayBeginBtn (AtomicBoolean b) {
        window.displayBeginBtn(b);
    }

    protected void startGame () {
        window.startGame();
    }

    protected void showClose () {
        window.showClose();
    }

    protected double askQuestion (Problem p) {
        AtomicBoolean done = new AtomicBoolean();
        window.displayProblem(p, done);
        while (!done.get());
        Response r = new Response();
        window.getResponse(time, r);
        while (!r.isDone());
        String input = r.getInput(), res;
        double t;
        if (input == null) {
            res = "Time's up !";
            t = -time;
        } else if (!p.checkAnswer(input)) {
            res = "Incorrect ! " + r.getTime() + "s";
            t = -r.getTime();
        } else {
            res = "Correct ! " + r.getTime() + "s";
            t = r.getTime();
        }
        displayMessage(res, true);
        return t;
    }

    abstract void run () throws Exception;
}
