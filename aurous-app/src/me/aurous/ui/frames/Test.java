package me.aurous.ui.frames;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

final public class Test {

    private int timeStep = 0;
    private final int ballYTravel = 100;
    private final int BALL_NUM = 64;

    public static void main(String... args) {
        new Test();
    }

    public Test() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Waves");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new DrawPanel());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    class DrawPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public DrawPanel() {
        	setBackground(new Color(3, 2, 12));
            new Timer(25, new ActionListener() {//25 seconds should be smooth enough
                @Override
                public void actionPerformed(ActionEvent e) {
                    timeStep++;
                    repaint();
                }
            }).start();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1354, 270);
        }

        public double getY(int i, int t) {
            return 100 + ballYTravel / 2 * (Math.sin(t * (i / 200d + 0.08)));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int k = 0; k < BALL_NUM; k++) {
            	g.drawString("Le Gem", 10 + 20 * k, (int) getY(k, timeStep));
            //   g.fillOval(10 + 20 * k, (int) getY(k, timeStep), 12, 12);
                g.setColor(new Color(29, 82, 112));
            }

        }
    }
}
