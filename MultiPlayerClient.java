// server -> problem statement -> client -> ask question
import java.net.*;
import java.io.*;

public class MultiPlayerClient extends Game {
    private Socket server;
    private BufferedReader sbr;

    public MultiPlayerClient (String ip) throws IOException, UnknownHostException {
        super(null, 0, 0);
        server = new Socket(InetAddress.getByName(ip), 5000);
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("Name: " + local.getHostName().toLowerCase());
        sbr = new BufferedReader(new InputStreamReader(server.getInputStream()));
        sbr.readLine();
    }

    public String run () {
        return "";
    }

}
