import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class MultiPlayerServer extends Game {
    private HashMap<String, Player> players;
    private TreeSet<Player> leaderboard;

    private ServerSocket server;
    private String hostName;
    private ArrayList<Client> clients;

    public MultiPlayerServer (Problem[] p, int n, int t) throws IOException {
        super(p, n, t);
        players = new HashMap<String, Player>();
        leaderboard = new TreeSet<Player>();
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("IP: " + local.getHostAddress());
        hostName = local.getHostName().toLowerCase();
        displayName(hostName);
        //System.out.println("Name: " + hostName);
        Player hp = new Player(n, hostName);
        players.put(hostName, hp);
        leaderboard.add(hp);
        getPlayers();
    }

    private void getPlayers () throws IOException {
        server = new ServerSocket(5000);
        server.setSoTimeout(500);
        clients = new ArrayList<Client>();
        System.out.println("Waiting for players...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        FTW.prompt();
        while (true) {
            Socket connection = null;
            try {
                connection = server.accept();
            } catch (SocketTimeoutException e) {
                if (br.ready()) {
                    if (br.readLine().equals("start"))
                        break;
                    else
                        FTW.prompt();
                }
                continue;
            } catch (SocketException e) {
                e.printStackTrace();
            }
            Client c = new Client(connection);
            clients.add(c);
            System.out.println(c.getName() + " joined");
            Player cp = new Player(getCount(), c.getName());
            players.put(c.getName(), cp);
            leaderboard.add(cp);
            FTW.prompt();
        }
    }

    public void run () throws Exception {
        System.out.println("Starting game...");
        for (Client c : clients)
            c.sendMessage(getCount());
        for (Client c : clients)
            c.sendMessage(getTime());
        wait(2500);
        for (int i = 0; i < getCount(); i++) {
            Problem p = getProblemByIndex(i);
            ArrayList<Callable<Double>> tasks = createTasks(p);
            ExecutorService executor = Executors.newFixedThreadPool(clients.size() + 1);
            List<Future<Double>> res = null;
            try {
                res = executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.shutdown();
            double mn = getTime() + 1;
            Player first = null;
            for (int j = 0; j < res.size(); j++) {
                double t = res.get(j).get();
                if (t <= 0)
                    continue;
                else if (t < mn) {
                    mn = t;
                    first = players.get((j == 0) ? hostName : clients.get(j - 1).getName());
                }
            }
            String m;
            if (first != null) {
                leaderboard.remove(first);
                first.addPoints();
                leaderboard.add(first);
                m = "Answered by " + first.getName() + " " + mn + "s";
            }
            else
                m = "Answered by no one";
            String lb = "\nLeaderboard\n-----------\n";
            for (Player rnk : leaderboard)
                lb += rnk.getName() + " - " + rnk.getPoints() + " point(s)\n";
            lb += "-----------\n";
            System.out.println(m + "\n" + lb);
            for (Client c : clients)
                c.sendMessage(m + "\n" + lb);
            System.out.println((i < getCount() - 1) ? "Next question..." : "Results...");
            wait(5000);
        }
        System.out.print("\033[H\033[2J");
        HashMap<String, String> results = getResults();
        for (Client c : clients)
            c.sendMessage(results.get(c.getName()));
        server.close();
    }

    private ArrayList<Callable<Double>> createTasks (Problem p) throws Exception {
        ArrayList<Callable<Double>> tasks = new ArrayList<Callable<Double>>();
        Callable<Double> hostInput = () -> {
            return askQuestion(p);
        };
        tasks.add(hostInput);
        for (Client c : clients) {
            Callable<Double> clientInput = () -> {
                c.sendMessage(p);
                return (Double)c.getMessage();
            };
            tasks.add(clientInput);
        }
        return tasks;
    }

    private HashMap<String, String> getResults () {
        HashMap<String, String> res = new HashMap<String, String>();
        int rank = 0, pre = -1;
        for (Player p : leaderboard) {
            if (p.getPoints() != pre) {
                rank++;
                pre = p.getPoints();
            }
            res.put(p.getName(), p.toString() + "\nRank: " + rank);
        }
        return res;
    }

    private class Client {
        private Socket connection;
        private String name;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        private Client (Socket c) throws IOException {
            connection = c;
            name = c.getInetAddress().getHostName().toLowerCase();
            oos = new ObjectOutputStream(c.getOutputStream());
            ois = new ObjectInputStream(c.getInputStream());
        }

        private String getName () {
            return name;
        }

        private void sendMessage (Object o) throws IOException {
            oos.writeObject(o);
            oos.flush();
        }

        private Object getMessage () throws Exception {
            return ois.readObject();
        }
    }
}
