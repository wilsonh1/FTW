import java.util.*;
import java.io.*;

public class ProblemSet {
    private ArrayList<Problem> list;

    public ProblemSet (String q, String a, String i) throws IOException {
        list = new ArrayList<Problem>();
        BufferedReader brq = new BufferedReader(new FileReader(q));
        BufferedReader bra = new BufferedReader(new FileReader(a));
        BufferedReader bri = new BufferedReader(new FileReader(i));
        String question;
        while ((question = brq.readLine()) != null) {
            String imgURL = bri.readLine();
            if (imgURL == null || imgURL.equals(""))
                list.add(new Problem(question, bra.readLine(), null));
            else
                list.add(new Problem(question, bra.readLine(), imgURL));
        }
    }

    public Problem[] getProblems (int n) {
        ArrayList<Problem> copy = new ArrayList<Problem>(list);
        Problem[] p = new Problem[n];
        for (int i = 0; i < n; i++) {
            int index = (int)(Math.random() * copy.size());
            p[i] = copy.get(index);
            copy.remove(index);
        }
        return p;
    }

    public int getSize () {
        return list.size();
    }
}
