public class SinglePlayer extends Game {
    private Player player;

    public SinglePlayer (Problem[] p, int n, int t) {
        super(p, n, t);
        player = new Player(n, null);
    }

    public String run () throws Exception {
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
            System.out.println((i < getCount() - 1) ? "Next question..." : "Results...");
            wait(5000);
        }
        System.out.print("\033[H\033[2J");
        return player.toString();
    }
}
