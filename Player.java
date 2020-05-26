public class Player implements Comparable<Player> {
    private int cnt, correct;
    private double time;

    private String ip;
    private String name;

    public Player (int n) {
        cnt = n;
    }

    public Player (String i, String n) {
        ip = i;
        name = n;
    }

    public void addPoints () {
        correct++;
    }

    public void addPoints (int x) {
        correct += x;
    }

    public void addTime (double t) {
        time += t;
    }

    public String getName () {
        return name;
    }

    public String getIP () {
        return ip;
    }

    public int getPoints () {
        return correct;
    }

    public int compareTo (Player p) {
        if (correct == p.correct)
            return ip.compareTo(p.ip);
        return p.correct - correct;
    }

    public String toString () {
        String s = "Results\n-------\n";
        if (name != null)
            return s + "Points: " + correct;
        return s + "Accuracy: " + String.format("%.2f%%\n", correct * 100.0 / cnt) + "Average time: " + String.format("%.3fs", time / cnt);
    }
}
