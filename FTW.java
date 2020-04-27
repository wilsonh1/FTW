import java.io.*;

public class FTW {
    private static final String questions = "data/questions.txt";
    private static final String answers = "data/answers.txt";
    private static final String images = "data/images.txt";

    private static PrintWriter logger;

    public static void main (String[] args) throws IOException {
        ProblemSet ps = new ProblemSet(questions, answers, images);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command = "";
        while (!command.equals("exit")) {
            prompt("");
            command = br.readLine();
            if (command.equals("start")) {
                logger = new PrintWriter(new BufferedWriter(new FileWriter("log.txt")));
                Game g;
                prompt("single / multi - player");
                String type = br.readLine();
                prompt("# of problems");
                int n = Integer.parseInt(br.readLine());
                prompt("time per problem");
                int t = Integer.parseInt(br.readLine());
                if (type.equals("single"))
                    g = new SinglePlayer(ps, n, t);
                else
                    g = new MultiPlayer(ps, n, t);
                System.out.println(g.run());
                logger.close();
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
