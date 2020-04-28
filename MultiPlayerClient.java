import java.net.*;
import java.io.*;

public class MultiPlayerClient extends Game {
    private Socket server;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public MultiPlayerClient (String ip) throws IOException {
        super(null, 0, 0);
        server = new Socket(InetAddress.getByName(ip), 5000);
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("Name: " + local.getHostName().toLowerCase());
        ois = new ObjectInputStream(server.getInputStream());
        oos = new ObjectOutputStream(server.getOutputStream());
    }

    public String run () throws Exception {
        System.out.println("Waiting for players...");
        int n = (Integer)ois.readObject();
        int t = (Integer)ois.readObject();
        setTime(t);
        System.out.println("Starting game...");
        for (int i = 0; i < n; i++) {
            Problem p = (Problem)ois.readObject();
            oos.writeObject(askQuestion(p));
            oos.flush();
            String m = (String)ois.readObject();
            System.out.println(m);
            System.out.println((i < n - 1) ? "Next question..." : "Results...");
        }
        String pr = (String)ois.readObject();
        System.out.print("\033[H\033[2J");
        return pr;
    }

}
