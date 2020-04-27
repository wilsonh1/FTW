public abstract class Game {
    private Problem[] problems;
    private int cnt, time;

    public Game (ProblemSet ps, int n, int t) {
        if (ps != null)
            problems = ps.getProblems(n);
        cnt = n;
        time = t;
    }

    protected int getCount () {
        return cnt;
    }

    protected Problem getProblemByIndex (int index) {
        return problems[index];
    }

    protected double askQuestion (Problem p) {
        System.out.print("\033[H\033[2J");
        System.out.println(p.getQuestion());
        TimedResponse tr = new TimedResponse(time);
        Response r = tr.getResponse();
        String input = r.getInput();
        if (input == null) {
            System.out.println("Time's up !");
            FTW.log("t " + r);
            return -time;
        }
        if (!p.checkAnswer(input)) {
            System.out.println("Incorrect !");
            FTW.log("x " + r);
            return -r.getTime();
        }
        System.out.println("Correct !");
        FTW.log("* " + r);
        return r.getTime();
    }

    abstract String run ();

    protected void wait (int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
