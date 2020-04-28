public class Player implements Comparable<Player> {
    private boolean isMulti;
    private int cnt, correct;
    private double time;

    public Player (int n, boolean is) {
        cnt = n;
        isMulti = is;
    }

    public void addPoints () {
        correct++;
    }

    public void addTime (double t) {
        time += t;
    }

    public int compareTo (Player p) {
        return correct - p.correct;
    }

    public String toString () {
        String s = "Results\n-------\n";
        if (isMulti)
            return s + "Points: " + correct;
        return s + "Accuracy: " + String.format("%.2f%%\n", correct * 100.0 / cnt) + "Average time: " + String.format("%.2fs", time / cnt);
    }
}
