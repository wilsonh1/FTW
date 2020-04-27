// store HashSet of Players, created by Server
// Thread invokeAny
import java.util.*;
import java.net.*;
import java.io.*;

public class MultiPlayerServer extends Game {
    private HashMap<String, Player> players;

    private ServerSocket server;
    private ArrayList<Socket> clients;

    public MultiPlayerServer (ProblemSet ps, int n, int t) throws IOException {
        super(ps, n, t);
        players = new HashMap<String, Player>();
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("IP: " + local.getHostAddress());
        String hostName = local.getHostName().toLowerCase();
        System.out.println("Name: " + hostName);
        players.put(hostName, new Player(n, true));
        getPlayers();
    }

    private void getPlayers () throws IOException {
        server = new ServerSocket(5000);
        server.setSoTimeout(500);
        clients = new ArrayList<Socket>();
        System.out.println("Waiting for players...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(">>> ");
        while (true) {
            Socket connection = null;
            try {
                connection = server.accept();
            } catch (SocketTimeoutException e) {
                if (br.ready()) {
                    if (br.readLine().equals("begin"))
                        break;
                    else
                        System.out.print(">>> ");
                }
                continue;
            } catch (SocketException e) {
                e.printStackTrace();
            }
            clients.add(connection);
            String playerName = connection.getInetAddress().getHostName().toLowerCase();
            System.out.println(playerName + " joined");
            players.put(playerName + "a", new Player(getCount(), true));
            System.out.print(">>> ");
        }
        System.out.println(clients.size());
        for (String name : players.keySet())
            System.out.println(name);
    }

    public String run () {
        return "test";
    }

}
