public class SinglePlayer extends Game {
    Player player;

    public SinglePlayer (ProblemSet ps, int n, int t) {
        super(ps, n, t);
        player = new Player(n);
    }

    public String run () {
        for (int i = 0; i < getCount(); i++) {
            double t = askQuestion(i);
            if (t > 0) {
                player.addPoints();
                player.addTime(t);
            }
            else
                player.addTime(-t);
            //if (i < getCount() - 1) {
                System.out.println((i < getCount() - 1) ? "Next question..." : "Results...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            //}
        }
        System.out.print("\033[H\033[2J");
        return player.toString();
    }
}
