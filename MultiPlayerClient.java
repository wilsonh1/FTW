import java.net.*;
import java.io.*;

public class MultiPlayerClient extends Game {
    private Socket server;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public MultiPlayerClient (String ip, String name) throws IOException {
        server = new Socket(InetAddress.getByName(ip), 5000);
        InetAddress local = InetAddress.getLocalHost();
        String address = local.getHostAddress();
        String playerName = name + address.replaceAll("^\\d+\\.\\d+\\.\\d+\\.", "");
        displayName(playerName);
        ois = new ObjectInputStream(server.getInputStream());
        oos = new ObjectOutputStream(server.getOutputStream());
        oos.writeObject(playerName);
        oos.flush();
    }

    public void run () throws Exception {
        displayMessage("Waiting for players...", true);
        int n = (Integer)ois.readObject();
        int t = (Integer)ois.readObject();
        setTime(t);
        displayMessage("Starting game...", false);
        startGame();
        updateSide("Leaderboard\n-----------", true);
        for (int i = 0; i < n; i++) {
            if (!isActive()) {
                server.close();
                return;
            }
            Problem p = (Problem)ois.readObject();
            oos.writeObject(askQuestion(p));
            oos.flush();
            String m = (String)ois.readObject();
            displayMessage(m, false);
            String lb = (String)ois.readObject();
            updateSide(lb, true);
            displayMessage((i < n - 1) ? "Next question..." : "Results...", false);
        }
        String pr = (String)ois.readObject();
        displayMessage(pr, true);
        showClose();
    }
}
