import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class MultiPlayerServer extends Game {
    private HashMap<String, Player> players;
    private TreeSet<Player> leaderboard;

    private ServerSocket server;
    private String hostIP;
    private ArrayList<Client> clients;

    public MultiPlayerServer (Problem[] p, int n, int t, String name) throws Exception {
        super(p, n, t);
        players = new HashMap<String, Player>();
        leaderboard = new TreeSet<Player>();
        hostIP = InetAddress.getLocalHost().getHostAddress();
        displayMessage("IP: " + hostIP, true);
        String hostName = name + hostIP.replaceAll("^\\d+\\.\\d+\\.\\d+\\.", "");
        displayName(hostName);
        addPlayer(hostIP, hostName);
        getPlayers();
    }

    private void getPlayers () throws Exception {
        server = new ServerSocket(5000);
        server.setSoTimeout(500);
        clients = new ArrayList<Client>();
        displayMessage("Waiting for players...", false);
        AtomicBoolean b = new AtomicBoolean();
        displayBeginBtn(b);
        while (true) {
            Socket connection = null;
            try {
                connection = server.accept();
            } catch (SocketTimeoutException e) {
                if (b.get())
                    break;
                continue;
            }
            Client c = new Client(connection);
            clients.add(c);
            String playerName = (String)c.getMessage();
            displayMessage(playerName + " joined", false);
            addPlayer(c.getAddress(), playerName);
        }
    }

    private void addPlayer (String ip, String name) {
        if (players.containsKey(ip))
            return;
        Player p = new Player(ip, name);
        players.put(ip, p);
        leaderboard.add(p);
    }

    public void run ()  throws Exception {
        displayMessage("Starting game...", false);
        broadcast(getCount());
        broadcast(getTime());
        startGame();
        updateSide("Leaderboard\n-----------", true);
        for (int i = 0; i < getCount(); i++) {
            if (!isActive()) {
                server.close();
                return;
            }
            Problem p = getProblemByIndex(i);
            ArrayList<Callable<Double>> tasks = createTasks(p);
            ExecutorService executor = Executors.newFixedThreadPool(clients.size() + 1);
            List<Future<Double>> res = executor.invokeAll(tasks);
            executor.shutdown();
            TreeMap<Double, Player> responses = new TreeMap<Double, Player>();
            for (int j = 0; j < res.size(); j++) {
                double t = res.get(j).get();
                if (t < 0)
                    continue;
                else {
                    Player pl = players.get((j == 0) ? hostIP : clients.get(j - 1).getAddress());
                    if (!leaderboard.contains(pl))
                        continue;
                    leaderboard.remove(pl);
                    while (responses.containsKey(t))
                        t += 0.00001;
                    responses.put(t, pl);
                }
            }
            String m = getPoints(responses);
            displayMessage(m, false);
            broadcast(m);
            String lb = "Leaderboard\n-----------\n";
            for (Player rnk : leaderboard)
                lb += rnk.getName() + " - " + rnk.getPoints() + " point(s)\n";
            updateSide(lb, true);
            broadcast(lb);
            displayMessage((i < getCount() - 1) ? "Next question..." : "Results...", false);
        }
        Thread.sleep(5000);
        HashMap<String, String> results = getResults();
        for (Client c : clients)
            c.sendMessage(results.get(c.getAddress()));
        displayMessage(results.get(hostIP), true);
        server.close();
        showClose();
    }

    private void broadcast (Object o) {
        for (Client c : clients)
            c.sendMessage(o);
    }

    private ArrayList<Callable<Double>> createTasks (Problem p) {
        ArrayList<Callable<Double>> tasks = new ArrayList<Callable<Double>>();
        Callable<Double> hostInput = () -> {
            return askQuestion(p);
        };
        tasks.add(hostInput);
        for (Client c : clients) {
            Callable<Double> clientInput = () -> {
                c.sendMessage(p);
                Object r = c.getMessage();
                if (r == null)
                    return -1.0;
                return (Double)r;
            };
            tasks.add(clientInput);
        }
        return tasks;
    }

    private String getPoints (TreeMap<Double, Player> responses) {
        int points = players.size();
        String m = "";
        for (Map.Entry<Double, Player> entry : responses.entrySet()) {
            Player p = entry.getValue();
            p.addPoints(points);
            leaderboard.add(p);
            m += "Answered by " + p.getName() + " - " + String.format("%.3f", entry.getKey()) + "s + " + points + " point(s)\n";
            points--;
        }
        if (m.equals(""))
            m = "Answered by no one";
        return m.trim();
    }

    private HashMap<String, String> getResults () {
        HashMap<String, String> res = new HashMap<String, String>();
        int rank = 0, pre = -1;
        for (Player p : leaderboard) {
            if (p.getPoints() != pre) {
                rank++;
                pre = p.getPoints();
            }
            res.put(p.getIP(), p.toString() + "\nRank: " + rank);
        }
        return res;
    }

    private class Client {
        private String address;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        private Client (Socket c) throws IOException {
            address = c.getInetAddress().getHostAddress();
            oos = new ObjectOutputStream(c.getOutputStream());
            ois = new ObjectInputStream(c.getInputStream());
        }

        private String getAddress () {
            return address;
        }

        private void sendMessage (Object o) {
            try {
                oos.writeObject(o);
                oos.flush();
            } catch (Exception e) {
                return;
            }
        }

        private Object getMessage () {
            try {
                return ois.readObject();
            } catch (Exception e) {
                return null;
            }
        }
    }
}
