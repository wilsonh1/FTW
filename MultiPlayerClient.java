// server -> problem statement -> client -> ask question
import java.net.*;
import java.io.*;

public class MultiPlayerClient extends Game {
    Socket server;

    public MultiPlayerClient (String ip) throws IOException, UnknownHostException {
        super(null, 0, 0);
        server = new Socket(InetAddress.getByName(ip), 5000);
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("Name: " + local.getHostName());
    }

    public String run () {
        return "";
    }

}
