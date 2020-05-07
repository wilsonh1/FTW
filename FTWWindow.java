import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class FTWWindow {
    private JFrame frame;

    public FTWWindow () {
        frame = new JFrame("FTW");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("FTW !");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startBtn = new JButton("New");
        startBtn.addActionListener(e -> newGame());
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton joinBtn = new JButton("Join");
        joinBtn.addActionListener(e -> joinGame());
        joinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(startBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(joinBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(exitBtn);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private void newGame () {
        System.out.println("start");
    }

    private void joinGame () {
        System.out.println("join");
    }
}
