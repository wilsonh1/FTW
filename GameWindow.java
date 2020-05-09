import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.concurrent.atomic.*;

import java.net.URL;
import javax.imageio.*;
import java.awt.image.*;

public class GameWindow {
    private JFrame frame;
    private JPanel top, left;
    private JTextArea right;
    private JTextField bottom;

    public GameWindow (AtomicBoolean active) {
        System.out.println("started");
        frame = new JFrame("FTW");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                active.set(false);
                FTWWindow.show();
            }
        });

        CompoundBorder border = new CompoundBorder(
            new LineBorder(Color.BLACK, 2, true),
            new EmptyBorder(10, 10, 10, 10));

        top = new JPanel();
        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(700, 40));
        top.setBorder(border);

        left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
        left.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane leftSP = new JScrollPane(left);
        leftSP.setPreferredSize(new Dimension(520, 380));
        leftSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftSP.setBorder(new LineBorder(Color.BLACK, 2, true));
        leftSP.getVerticalScrollBar().setUnitIncrement(16);

        right = textArea("Leaderboard\n-----------");
        right.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane rightSP = new JScrollPane(right);
        rightSP.setPreferredSize(new Dimension(170, 380));
        rightSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightSP.setBorder(new LineBorder(Color.BLACK, 2, true));

        bottom = new JTextField(20);
        bottom.setEditable(false);
        JPanel temp = new JPanel();
        JLabel answerLabel = new JLabel("Answer", JLabel.RIGHT);
        JPanel bottomP = new JPanel();
        bottomP.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomP.setPreferredSize(new Dimension(700, 60));
        bottomP.setBorder(border);
        bottomP.add(answerLabel);
        bottomP.add(bottom);

        JPanel back = new JPanel();
        back.setLayout(new BorderLayout(10, 10));
        back.setBorder(new EmptyBorder(10, 10, 10, 10));
        back.add(top, BorderLayout.PAGE_START);
        back.add(leftSP, BorderLayout.CENTER);
        back.add(rightSP, BorderLayout.LINE_END);
        back.add(bottomP, BorderLayout.PAGE_END);

        frame.add(back);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void dispose () {
        frame.dispose();
    }

    public void displayName (String name) {
        top.add(new JLabel(name), BorderLayout.LINE_END);
        top.revalidate();
        top.repaint();
    }

    public void displayMessage (String m, boolean flag) {
        JTextArea text;
        if (flag) {
            text = textArea(m);
            left.removeAll();
            left.add(text);
            left.revalidate();
            left.repaint();
        } else {
            text = (JTextArea)left.getComponent(0);
            text.append("\n\n" + m);
        }
    }

    public void displayProblem (Problem p) {
        displayMessage(p.getQuestion(), true);
        if (p.getImg() != null) {
            try {
                URL url = new URL(p.getImg());
                BufferedImage image = ImageIO.read(url);
                JLabel img = new JLabel(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
                left.add(img);
                left.revalidate();
                left.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateSide (String m, boolean flag) {
        if (flag) {

        } else {
            right.append("\n" + m);
        }
    }

    public void getResponse (int time, Response r) {
        bottom.setEditable(true);
        r.setSent(System.currentTimeMillis());
        Timer timer = new Timer(time * 1000, e -> {
            bottom.setText("");
            bottom.setEditable(false);
            r.setDone(true);
        });
        bottom.addActionListener(e -> {
            String s = bottom.getText();
            if (s.equals(""))
                return;
            else {
                System.out.println(s);
                r.setReceived(System.currentTimeMillis());
                r.setInput(s);
                bottom.setText("");
                bottom.setEditable(false);
                timer.stop();
                r.setDone(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private JTextArea textArea (String s) {
        JTextArea text = new JTextArea(s);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setOpaque(true);
        text.setEditable(false);
        text.setFocusable(false);
        text.setBackground(UIManager.getColor("Label.background"));
        text.setFont(UIManager.getFont("Label.font"));
        text.setPreferredSize(new Dimension(0, 10));
        return text;
    }
}
