import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GameWindow {
    private JFrame frame;
    private JPanel top, left, right, bottom;

    public GameWindow () {
        frame = new JFrame("FTW");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
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
        left.setPreferredSize(new Dimension(520, 380));
        left.setBorder(border);

        right = new JPanel();
        right.setPreferredSize(new Dimension(170, 380));
        right.setBorder(border);

        bottom = new JPanel();
        bottom.setPreferredSize(new Dimension(700, 80));
        bottom.setBorder(border);

        JPanel back = new JPanel();
        back.setLayout(new BorderLayout(10, 10));
        back.setBorder(new EmptyBorder(10, 10, 10, 10));
        back.add(top, BorderLayout.PAGE_START);
        back.add(left, BorderLayout.CENTER);
        back.add(right, BorderLayout.LINE_END);
        back.add(bottom, BorderLayout.PAGE_END);

        frame.add(back);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void displayName (String name) {
        top.add(new JLabel(name), BorderLayout.LINE_END);
    }

    public void displayProblem (Problem p) {
        left.removeAll();
        left.add(new JLabel(p.getQuestion()));
    }
}
