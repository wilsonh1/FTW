import java.util.*;

public class SinglePlayer extends Game {
    private Player player;

    public SinglePlayer (Problem[] p, int n, int t) {
        super(p, n, t);
        displayName("SinglePlayer");
        player = new Player(n, null);
    }

    public String run () throws Exception {
        System.out.println("EDT " + javax.swing.SwingUtilities.isEventDispatchThread());
        displayMessage("Starting game...");
        wait(2500);
        for (int i = 0; i < getCount(); i++) {
            if (!isActive()) {
                System.out.println("finished");
                return "";
            }
            double t = askQuestion(getProblemByIndex(i));
            if (t > 0) {
                player.addPoints();
                player.addTime(t);
            }
            else
                player.addTime(-t);
            displayMessage((i < getCount() - 1) ? "Next question..." : "Results...");
            wait(5000);
        }
        displayMessage(player.toString());
        return player.toString();
    }
}
