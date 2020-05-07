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
        System.out.println("new");

        int psSize = FTWTest.getSize();
        JLabel modeLabel = new JLabel("Mode", JLabel.RIGHT);
        JLabel cntLabel = new JLabel("# of problems (1-" + psSize + ")", JLabel.RIGHT);
        JTextField cntField = new JTextField("5");
        JLabel timeLabel = new JLabel("Time per problem", JLabel.RIGHT);
        JTextField timeField = new JTextField("45");

        JRadioButton single = new JRadioButton("single", true);
        single.setActionCommand("single");
        JRadioButton multi = new JRadioButton("multi");
        multi.setActionCommand("multi");
        ButtonGroup modeBtns = new ButtonGroup();
        modeBtns.add(single);
        modeBtns.add(multi);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(4, 2, 10, 10));
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        fieldsPanel.add(modeLabel);
        fieldsPanel.add(single);
        fieldsPanel.add(new JPanel());
        fieldsPanel.add(multi);
        fieldsPanel.add(cntLabel);
        fieldsPanel.add(cntField);
        fieldsPanel.add(timeLabel);
        fieldsPanel.add(timeField);

        final String[] opts = {"Start", "Cancel"};

        if (JOptionPane.showOptionDialog(frame, fieldsPanel, "New game",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
            opts, opts[0]) != 0) {
            System.out.println("cancel");
            return;
        }

        String errorMsg = "";

        String mode = modeBtns.getSelection().getActionCommand();
        int cnt = 0, time = 0;
        try {
            cnt = Integer.parseInt(cntField.getText());
            time = Integer.parseInt(timeField.getText());
        } catch (NumberFormatException e) {
            errorMsg = "Invalid # or time - Must be a positive integer";
        }

        if (errorMsg.equals("")) {
            if (cnt <= 0 || cnt > psSize)
                errorMsg = "Invalid # - Must be between 1 and " + psSize;
            else if (time <= 0)
                errorMsg += "Invalid time - Must be a positive integer";
        }

        if (!errorMsg.equals("")) {
            JOptionPane.showMessageDialog(frame, errorMsg, "Game creation failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        frame.setVisible(false);
        try {
            FTWTest.startGame(mode.equals("multi"), cnt, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void joinGame () {
        System.out.println("join");

        JLabel ipLabel = new JLabel("Host IP", JLabel.RIGHT);
        JTextField ipField = new JTextField(15);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.LINE_AXIS));
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        fieldsPanel.add(ipLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        fieldsPanel.add(ipField);

        final String[] opts = {"Join", "Cancel"};

        if (JOptionPane.showOptionDialog(frame, fieldsPanel, "New game",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
            opts, opts[0]) != 0) {
            System.out.println("cancel");
            return;
        }

        String ip = ipField.getText().trim();
        frame.setVisible(false);
        try {
            FTWTest.joinGame(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
