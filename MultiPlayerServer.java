// store HashSet of Players, created by Server
// Thread invokeAny
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class MultiPlayerServer extends Game {
    private HashMap<String, Player> players;
    //private TreeSet<Player> leaderboard;

    private String hostName;
    private ServerSocket server;
    private ArrayList<Client> clients;

    public MultiPlayerServer (ProblemSet ps, int n, int t) throws IOException {
        super(ps, n, t);
        players = new HashMap<String, Player>();
        //leaderboard = new TreeSet<Player>();
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("IP: " + local.getHostAddress());
        hostName = local.getHostName().toLowerCase();
        System.out.println("Name: " + hostName);
        Player hp = new Player(n, true);
        players.put(hostName, hp);
        //leaderboard.add(hp);
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
                    if (br.readLine().equals("begin"))
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
            Player cp = new Player(getCount(), true);
            players.put(c.getName(), cp);
            //leaderboard.add(cp);
            FTW.prompt();
        }
    }

    public String run () throws Exception {
        System.out.println("Starting game...");
        for (Client c : clients)
            c.sendMessage(getCount());
        for (Client c : clients)
            c.sendMessage(getTime());
        wait(2500);
        for (int i = 0; i < getCount(); i++) {
            Problem p = getProblemByIndex(i);
            ArrayList<Callable<Double>> tasks = createTasks(p);
            ExecutorService executor = Executors.newFixedThreadPool(players.size());
            List<Future<Double>> results = null;
            try {
                results = executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.shutdown();
            for (int j = 0; j < results.size(); j++) {
                Future<Double> f = results.get(j);
                double t = 0;
                try {
                    t = f.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (t <= 0)
                    continue;
                else {
                    Player pl = players.get((j == 0) ? hostName : clients.get(j).getName());
                    //leaderboard.remove(pl);
                    pl.addPoints();
                    //leaderboard.add(pl);
                }
            }
            System.out.println((i < getCount() - 1) ? "Next question..." : "Results...");
            wait(5000);
        }
        System.out.print("\033[H\033[2J");
        for (Client c : clients)
            c.sendMessage(players.get(c.getName()).toString());
        return players.get(hostName).toString();
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

    private class Client {
        private Socket connection;
        private String name;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        private Client (Socket c) throws IOException {
            connection = c;
            name = c.getInetAddress().getHostName().toLowerCase() + "a";
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
