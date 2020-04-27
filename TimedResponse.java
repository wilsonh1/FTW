import java.util.concurrent.*;
import java.io.*;

public class TimedResponse {
    private Response res;
    private int timeout;
    // Input Stream ?

    public TimedResponse (int to) {
        res = new Response();
        timeout = to;
    }

    public Response getResponse () {
        PlayerInput task = new PlayerInput();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(task);
        try {
            res.setInput(result.get(timeout, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            //System.out.println("Time's up !");
            result.cancel(true);
            res.setInput(null);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        res.setRecieved(System.currentTimeMillis());
        executor.shutdown();
        //System.out.println(res);
        return res;
    }

    private class PlayerInput implements Callable<String> {
        public String call() throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = "";
            res.setSent(System.currentTimeMillis());
            while (input.equals("")) {
                System.out.print(">>> ");
                try {
                    while (!br.ready()) {
                        Thread.sleep(10);
                    }
                    input = br.readLine();
                } catch (InterruptedException e) {
                    return null;
                }
            }
            return input;
        }
    }
}
