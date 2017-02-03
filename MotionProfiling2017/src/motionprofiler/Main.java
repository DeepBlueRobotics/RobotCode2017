package motionprofiler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Main {

	static Path p;
	static Trajectory trajectory;

	public static void main(String[] args) {
		startPathDemo();
	}

	public static void startPathDemo() {
		p = new Path(25, 25, 50, 50, 0, 0, 2);
		JFrame frame = new JFrame("Pathfinder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		JTextField f1 = new JTextField("0");
		JTextField f2 = new JTextField("0");
		JTextField f3 = new JTextField("2");
		@SuppressWarnings("serial")
		class MyPanel extends JPanel {
			ArrayList<Double> xarray = new ArrayList<>();
			ArrayList<Double> yarray = new ArrayList<>();

			@Override
			public void paint(Graphics g) {
				for (double s = 0; s <= 1; s += .002) {
					double x = p.getX(s) * 10;
					double y = p.getY(s) * 10;
					y = getHeight() - y;
					g.setColor(Color.CYAN);
					g.fillRect((int) x - 1, (int) y - 1, 3, 3);
				}
				for (int i = 0; i < xarray.size(); i++) {
					double x = xarray.get(i);
					double y = yarray.get(i);
					double y2 = getHeight() - y * 10;
					double x2 = x * 10;
					g.setColor(Color.MAGENTA);
					g.fillRect((int) x2 - 1, (int) y2 - 1, 3, 3);
				}
			}

			Timer tim;

			public void draw() {
				xarray.clear();
				yarray.clear();
				tim = new Timer(1, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (p.getS(l) < 1)
							doStuff();
						else
							tim.stop();
					}
				});
				l = 0;
				theta = p.getTheta(0);
				x = p.getX(0);
				y = p.getY(0);
				dt = .1;
				tim.start();
			}

			double l = 0, theta = p.getTheta(0), x = p.getX(0), y = p.getY(0), dt = .1;

			public void doStuff() {
				// The motion profiling part
				int i = trajectory.getCurrentIndex(l);
				double v = trajectory.getV(i);
				l += v * dt;
				double w = trajectory.getW(i);
				theta += w * dt;
				x += (v * Math.sin((Math.toRadians(theta)))) * dt;
				y += (v * Math.cos((Math.toRadians(theta)))) * dt;
				xarray.add(x);
				yarray.add(y);
				repaint();
			}
		}
		;
		MyPanel panel = new MyPanel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				double x = me.getX() / 10;
				double y = (panel.getHeight() - me.getY()) / 10;
				double angle0 = Double.parseDouble(f1.getText());
				double angle1 = Double.parseDouble(f2.getText());
				double k = Double.parseDouble(f3.getText());
				p = new Path(25, 25, x, y, angle0, angle1, k);
				trajectory = new Trajectory(p, .05, 2.5, 25, 0.5, 25, 10, 1007);// path,v0,v1,vmax,amax,wmax,alphamax,points
				panel.draw();
			}
		});
		panel.setPreferredSize(new Dimension(500, 500));
		f1.setPreferredSize(new Dimension(100, 25));
		f2.setPreferredSize(new Dimension(100, 25));
		f3.setPreferredSize(new Dimension(100, 25));
		frame.setLayout(new FlowLayout());
		frame.add(panel);
		frame.add(f1);
		frame.add(f2);
		frame.add(f3);
		frame.setVisible(true);
	}
}