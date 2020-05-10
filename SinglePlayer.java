import java.util.*;

public class SinglePlayer extends Game {
    private Player player;

    public SinglePlayer (Problem[] p, int n, int t) {
        super(p, n, t);
        displayName("SinglePlayer");
        player = new Player(n);
    }

    public void run () throws Exception {
        //System.out.println("EDT " + javax.swing.SwingUtilities.isEventDispatchThread());
        startGame();
        displayMessage("Starting game...", true);
        updateSide("Problem results\n--------------", true);
        wait(2500);
        for (int i = 0; i < getCount(); i++) {
            if (!isActive()) {
                System.out.println("finished");
                return;
            }
            double t = askQuestion(getProblemByIndex(i));
            if (t > 0) {
                player.addPoints();
                player.addTime(t);
                System.out.println(t);
                updateSide((i+1) + ". * - " + t + "s", false);
            }
            else {
                player.addTime(-t);
                updateSide((i+1) + ". x - " + -t + "s", false);
            }
            displayMessage((i < getCount() - 1) ? "Next question..." : "Results...", false);
            wait(5000);
        }
        displayMessage(player.toString(), true);
        showClose();
    }
}
