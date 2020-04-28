public class Player implements Comparable<Player> {
    private int cnt, correct;
    private double time;
    private String name; // use name instead

    public Player (int n, String mn) {
        cnt = n;
        name = mn;
    }

    public void addPoints () {
        correct++;
    }

    public void addTime (double t) {
        time += t;
    }

    public String getName () {
        return name;
    }

    public int getPoints () {
        return correct;
    }

    public int compareTo (Player p) {
        if (correct == p.correct)
            return name.compareTo(p.name);
        return correct - p.correct;
    }

    public String toString () {
        String s = "Results\n-------\n";
        if (name != null)
            return s + "Points: " + correct;
        return s + "Accuracy: " + String.format("%.2f%%\n", correct * 100.0 / cnt) + "Average time: " + String.format("%.2fs", time / cnt);
    }
}
