public class Response {
    private String input;
    private long sent, received;

    public void setInput (String in) {
        input = in;
    }

    public void setSent (long s) {
        sent = s;
    }

    public void setRecieved (long r) {
        received = r;
    }

    public String getInput () {
        return input;
    }

    public double getTime () {
        return (received - sent)/1000.0;
    }

    public String toString () {
        return input + " " + sent + " " + received;
    }
}
