public abstract class Game {
    private Problem[] problems;
    private int cnt, time;
    private GameWindow window;

    public Game (GameWindow gw) {
        cnt = time = -1;
        window = gw;
    }

    public Game (Problem[] p, int n, int t, GameWindow gw) {
        problems = p;
        cnt = n;
        time = t;
        window = gw;
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

    protected void setName (String name) {
        window.displayName(name);
    }

    protected double askQuestion (Problem p) {
        //System.out.print("\033[H\033[2J");
        //System.out.println(p.getQuestion());
        window.displayProblem(p);
        /*timedResponse r = new TimedResponse(time);
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
        return r.getTime();*/
        return 0;
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
