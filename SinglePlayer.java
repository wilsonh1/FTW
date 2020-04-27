public class SinglePlayer extends Game {
    private Player player;

    public SinglePlayer (ProblemSet ps, int n, int t) {
        super(ps, n, t);
        player = new Player(n, false);
    }

    public String run () {
        System.out.println("Starting game...");
        wait(2500);
        for (int i = 0; i < getCount(); i++) {
            double t = askQuestion(getProblemByIndex(i));
            if (t > 0) {
                player.addPoints();
                player.addTime(t);
            }
            else
                player.addTime(-t);
            //if (i < getCount() - 1) {
                System.out.println((i < getCount() - 1) ? "Next question..." : "Results...");
                wait(5000);
            //}
        }
        System.out.print("\033[H\033[2J");
        return player.toString();
    }
}
