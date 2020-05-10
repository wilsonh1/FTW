import java.util.*;

public class Test {
    public static void main (String[] args) {
        Player p1 = new Player("1", "asdf");
        Player p2 = new Player("1", "aa");
        Player p3 = new Player("2", "asdf");
        TreeSet<Player> s = new TreeSet<Player>();
        s.add(p1);
        s.add(p2);
        s.add(p3);
        System.out.println(s.size());
        System.out.println(s.contains(p2));
        System.out.println(s.contains(p1));
    }
}
