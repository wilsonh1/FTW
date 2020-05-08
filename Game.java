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

    protected void displayMessage (String m) {
        window.displayMessage(m);
    }

    protected double askQuestion (Problem p) {
        //System.out.print("\033[H\033[2J");
        //System.out.println(p.getQuestion());
        window.displayProblem(p);
        TimedResponse r = new TimedResponse(time);
        String input = r.getInput();
        if (input == null) {
            System.out.println("Time's up !");
            return -time;
        }
        if (!p.checkAnswer(input)) {
            System.out.println("Incorrect ! " + r.getTime() + "s");
            return -r.getTime();
        }
        System.out.println("Correct ! " + r.getTime() + "s");
        return r.getTime();
    }

    abstract String run () throws Exception;

    protected void wait (int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
