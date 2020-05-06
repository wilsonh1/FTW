import java.util.concurrent.*;
import java.io.*;

public class TimedResponse {
    private String input;
    private long sent, received;

    public TimedResponse (int timeout) {
        sent = System.currentTimeMillis();
        PlayerInput task = new PlayerInput();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(task);
        try {
            input = result.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            result.cancel(true);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        received = System.currentTimeMillis();
        executor.shutdown();
    }

    public String getInput () {
        return input;
    }

    public double getTime () {
        return (received - sent)/1000.0;
    }

    private class PlayerInput implements Callable<String> {
        public String call () throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = "";
            while (input.equals("")) {
                FTW.prompt();
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
