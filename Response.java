import java.util.concurrent.atomic.*;

public class Response {
    AtomicBoolean done;
    AtomicReference<String> input;
    AtomicLong sent, received;

    public Response () {
        done = new AtomicBoolean();
        input = new AtomicReference<String>();
        sent = new AtomicLong();
        received = new AtomicLong();
    }

    public boolean isDone () {
        return done.get();
    }

    public String getInput () {
        return input.get();
    }

    public double getTime () {
        return (received.get() - sent.get())/1000.0;
    }

    public void setDone (boolean d) {
        done.set(d);
    }

    public void setInput (String i) {
        input.set(i);
    }

    public void setSent (long s) {
        sent.set(s);
    }

    public void setReceived (long r) {
        received.set(r);
    }
}
