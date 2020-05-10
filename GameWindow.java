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
    private JPanel top, bottom;
    private JTextArea right;
    private JScrollPane left;
    //private JTextField bottom;

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

        left = new JScrollPane(left);
        left.setPreferredSize(new Dimension(520, 380));
        left.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        left.setBorder(new LineBorder(Color.BLACK, 2, true));

        right = textArea("");
        right.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane rightSP = new JScrollPane(right);
        rightSP.setPreferredSize(new Dimension(170, 380));
        rightSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightSP.setBorder(new LineBorder(Color.BLACK, 2, true));

        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setPreferredSize(new Dimension(700, 60));
        bottom.setBorder(border);

        JPanel back = new JPanel();
        back.setLayout(new BorderLayout(10, 10));
        back.setBorder(new EmptyBorder(10, 10, 10, 10));
        back.add(top, BorderLayout.PAGE_START);
        back.add(left, BorderLayout.CENTER);
        back.add(rightSP, BorderLayout.LINE_END);
        back.add(bottom, BorderLayout.PAGE_END);

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

    public void displayBeginBtn (AtomicBoolean b) {
        JButton begin = new JButton("Begin");
        begin.addActionListener(e -> b.set(true));
        bottom.add(begin);
        bottom.revalidate();
        bottom.repaint();
    }

    public void startGame () {
        JLabel answerLabel = new JLabel("Answer", JLabel.RIGHT);
        JTextField text = new JTextField(25);
        text.setEditable(false);
        text.setMaximumSize(text.getPreferredSize());
        JButton close = new JButton("Close");
        close.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        close.setVisible(false);

        bottom.removeAll();
        bottom.add(answerLabel);
        bottom.add(Box.createRigidArea(new Dimension(10, 0)));
        bottom.add(text);
        bottom.add(new Box.Filler(new Dimension(10, 0), new Dimension(10, 0), new Dimension(700, 0)));
        bottom.add(close);
        bottom.revalidate();
        bottom.repaint();
    }

    public void displayMessage (String m, boolean flag) {
        JTextArea text;
        if (flag) {
            text = textArea(m);
            left.setViewportView(text);
        } else {
            text = (JTextArea)left.getViewport().getView();
            text.append("\n\n" + m);
        }
    }

    private String parseProblem (Problem p) {
        String q = p.getQuestion();
        System.out.println(UIManager.getFont("Label.font"));
        String font = UIManager.getFont("Label.font").getFamily();
        String html = "<html><body style=\"font-family: " + font + ";font-size: 13\"><p>";
        String t = "", latex = "https://latex.codecogs.com/png.latex?";
        boolean flag = false;
        for (int i = 0; i < q.length(); i++) {
            char c = q.charAt(i);
            if (c == '\\' && i < q.length() - 1) {
                char nc = q.charAt(i + 1);
                if (nc == '(' ) {
                    flag = true;
                    t = "";
                    i++;
                    continue;
                } else if (nc == ')') {
                    flag = false;
                    html += "<img src=\"" + latex + t + "\" height=\"12\" width=\"auto\">";
                    i++;
                    continue;
                }
            }
            if (!flag)
                html += c;
            else
                t += c;
        }
        if (p.getImg() != null)
            html += "</p><p><img src=\"" + p.getImg() + "\" height=\"200\" width=\"auto\">";
        html += "</p></body></html>";
        System.out.println(html);
        return html;
    }

    public void displayProblem (Problem p, AtomicBoolean b) {
        String html = parseProblem(p);
        JTextPane text = new JTextPane();
        text.setContentType("text/html");
        text.setOpaque(true);
        text.setEditable(false);
        text.setFocusable(false);
        text.setBackground(UIManager.getColor("Label.background"));
        text.setBorder(new EmptyBorder(0, 10, 10, 10));
        text.setText(html);

        Timer timer = new Timer(5 * 1000, e -> {
            left.setViewportView(text);
            b.set(true);
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void updateSide (String m, boolean flag) {
        if (flag)
            right.setText(m);
        else
            right.append("\n" + m);
    }

    public void getResponse (int time, Response r) {
        JTextField text = (JTextField)bottom.getComponent(2);
        text.setEditable(true);
        r.setSent(System.currentTimeMillis());
        Timer timer = new Timer(time * 1000, e -> {
            text.setText("");
            text.setEditable(false);
            r.setDone(true);
        });
        text.addActionListener(e -> {
            String s = text.getText().trim().toLowerCase();
            if (s.equals(""))
                return;
            else {
                System.out.println(s);
                r.setReceived(System.currentTimeMillis());
                r.setInput(s);
                text.setText("");
                text.setEditable(false);
                timer.stop();
                r.setDone(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void showClose () {
        JButton close = (JButton)bottom.getComponent(4);
        close.setVisible(true);
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
        text.setBorder(new EmptyBorder(10, 10, 10, 10));
        return text;
    }
}
