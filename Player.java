public class Player {
    private int cnt, correct;
    private double time;
    private String name;

    public Player (int n) {
        cnt = n;
    }

    public Player (String s) {
        name = s;
    }

    public void addPoints () {
        correct++;
    }

    public void addTime (double t) {
        time += t;
    }

    public String toString () {
        String s = "Results\n-------\n";
        if (name != null)
            return s + "Points: " + correct;
        return s + "Accuracy: " + String.format("%.2f%%\n", correct * 100.0 / cnt) + "Average time: " + String.format("%.2fs", time / cnt);
    }
}
