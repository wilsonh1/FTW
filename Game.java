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

    protected int getTime () {
        return time;
    }

    protected void setTime (int t) {
        time = t;
    }

    protected Problem getProblemByIndex (int index) {
        return problems[index];
    }

    protected double askQuestion (Problem p) {
        System.out.print("\033[H\033[2J");
        System.out.println(p.getQuestion());
        System.out.println(System.currentTimeMillis());
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
