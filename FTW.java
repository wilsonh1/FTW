import java.io.*;
import java.net.*;

public class FTW {
    private static final String questions = "data/questions.txt";
    private static final String answers = "data/answers.txt";
    private static final String images = "data/images.txt";

    private static PrintWriter logger;

    public static void main (String[] args) throws IOException, UnknownHostException {
        ProblemSet ps = new ProblemSet(questions, answers, images);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command = "";
        while (!command.equals("exit")) {
            prompt("");
            command = br.readLine();
            Game g;
            if (command.equals("start")) {
                logger = new PrintWriter(new BufferedWriter(new FileWriter("log.txt")));
                prompt("single / multi - player");
                String type = br.readLine();
                prompt("# of problems");
                int n = Integer.parseInt(br.readLine());
                prompt("Time per problem");
                int t = Integer.parseInt(br.readLine());
                if (type.equals("single"))
                    g = new SinglePlayer(ps, n, t);
                else
                    g = new MultiPlayerServer(ps, n, t);
                System.out.println(g.run());
                logger.close();
            } else if (command.equals("join")) {
                prompt("Host IP");
                String ip = br.readLine();
                g = new MultiPlayerClient(ip);
            }
        }
    }

    public static void prompt (String s) {
        if (!s.equals(""))
            System.out.println(s);
        System.out.print(">>> ");
    }

    public static void log (String s) {
        logger.println(s);
    }
}
