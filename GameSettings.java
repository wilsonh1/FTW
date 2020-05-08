import java.util.concurrent.atomic.*;

public class GameSettings {
    private AtomicBoolean done, join, multi;
    private AtomicInteger cnt, time;
    private AtomicReference<String> ip;

    public GameSettings () {
        done = new AtomicBoolean();
        join = new AtomicBoolean();
        multi = new AtomicBoolean();
        cnt = new AtomicInteger();
        time = new AtomicInteger();
        ip = new AtomicReference<String>();
    }

    public boolean isDone () {
        return done.get();
    }

    public boolean isJoin () {
        return join.get();
    }

    public boolean isMulti () {
        return multi.get();
    }

    public int getCount () {
        return cnt.get();
    }

    public int getTime () {
        return time.get();
    }

    public String getIP () {
        return ip.get();
    }

    public void setDone (boolean d) {
        done.set(d);
    }

    public void setJoin (boolean j) {
        join.set(j);
    }

    public void setMulti (boolean m) {
        multi.set(m);
    }

    public void setCount (int n) {
        cnt.set(n);
    }

    public void setTime (int t) {
        time.set(t);
    }

    public void setIP (String s) {
        ip.set(s);
    }
}
