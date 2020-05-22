import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class FTWWindow {
    private GameSettings gs;
    private static JFrame frame;

    public FTWWindow (GameSettings g) {
        gs = g;

        frame = new JFrame("FTW");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void show () {
        frame.setVisible(true);
    }

    private void newGame () {
        if (!gs.isFinished()) {
            JOptionPane.showMessageDialog(frame, "Wait for current game to finish", "Game creation failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int psSize = FTW.getSize();
        JLabel modeLabel = new JLabel("Mode", JLabel.RIGHT);
        JLabel cntLabel = new JLabel("# of problems (1-" + psSize + ")", JLabel.RIGHT);
        JTextField cntField = new JTextField("5");
        JLabel timeLabel = new JLabel("Time per problem", JLabel.RIGHT);
        JTextField timeField = new JTextField("45");
        JLabel nameLabel = new JLabel("Name (multiplayer only)", JLabel.RIGHT);
        JTextField nameField = new JTextField();
        nameField.setEditable(false);

        JRadioButton single = new JRadioButton("single", true);
        single.setActionCommand("single");
        single.addActionListener(e -> {
            nameField.setText("");
            nameField.setEditable(false);
        });
        JRadioButton multi = new JRadioButton("multi");
        multi.setActionCommand("multi");
        multi.addActionListener(e -> {
            nameField.setEditable(true);
        });
        ButtonGroup modeBtns = new ButtonGroup();
        modeBtns.add(single);
        modeBtns.add(multi);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(5, 2, 10, 10));
        fieldsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        fieldsPanel.add(modeLabel);
        fieldsPanel.add(single);
        fieldsPanel.add(new JPanel());
        fieldsPanel.add(multi);
        fieldsPanel.add(cntLabel);
        fieldsPanel.add(cntField);
        fieldsPanel.add(timeLabel);
        fieldsPanel.add(timeField);
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(nameField);

        final String[] opts = {"Create", "Cancel"};

        if (JOptionPane.showOptionDialog(frame, fieldsPanel, "New game",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
            opts, opts[0]) != 0) {
            return;
        }

        String errorMsg = "";

        String mode = modeBtns.getSelection().getActionCommand();
        String name = nameField.getText().trim();
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

        gs.setJoin(false);
        gs.setMulti(mode.equals("multi"));
        gs.setCount(cnt);
        gs.setTime(time);
        gs.setName(name);
        gs.setDone(true);
    }

    private void joinGame () {
        if (!gs.isFinished()) {
            JOptionPane.showMessageDialog(frame, "Wait for current game to finish", "Game creation failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JLabel ipLabel = new JLabel("Host IP", JLabel.RIGHT);
        JTextField ipField = new JTextField(15);
        JLabel nameLabel = new JLabel("Name", JLabel.RIGHT);
        JTextField nameField = new JTextField(15);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        fieldsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 5, 0, 5);
        fieldsPanel.add(ipLabel, c);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.insets = new Insets(0, 5, 5, 0);
        fieldsPanel.add(ipField, c);
        c.gridy = 1;
        c.insets = new Insets(5, 5, 0, 0);
        fieldsPanel.add(nameField, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.insets = new Insets(5, 0, 0, 5);
        fieldsPanel.add(nameLabel, c);

        final String[] opts = {"Join", "Cancel"};

        if (JOptionPane.showOptionDialog(frame, fieldsPanel, "New game",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
            opts, opts[0]) != 0) {
            return;
        }

        String ip = ipField.getText().trim();
        String name = nameField.getText().trim();
        frame.setVisible(false);

        gs.setJoin(true);
        gs.setIP(ip);
        gs.setName(name);
        gs.setDone(true);
    }
}
