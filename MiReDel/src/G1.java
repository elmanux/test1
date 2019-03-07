import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class G1 extends JFrame implements MouseWheelListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new G1();

	}

	C1 c;
	int f = 5;
	int maxDiff = 1000;
	int ox0, oy0, ox, oy;
	private Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
	private JTextField tfMaxDiff;
	private JTextArea taDel;
	private JButton bDel;

	public G1() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
		ox = (int) (s.getWidth() / 2);
		oy = (int) (s.getHeight() / 2);
		ox0 = ox;
		oy0 = oy;

		c = new C1();
		calcDelBasedWp(c);

		tfMaxDiff = new JTextField("" + maxDiff);
		taDel = new JTextArea();
		taDel.setFocusable(false);
		tfMaxDiff.setFocusable(false);
		JScrollPane spDel = new JScrollPane(taDel);
		bDel = new JButton("DEL");
		bDel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.isShiftDown() && e.isAltDown()) {
					Set<RegInf> regMaxDiff = getRegMaxDiff();
					System.out.println(
							regMaxDiff.stream().map(r -> r.f.getAbsolutePath()).collect(Collectors.joining("\r")));
					for (RegInf r : regMaxDiff) {
						boolean b = r.del();
						System.out.println(b + "\t" + r.name);
					}
				}
			}
		});

		tfMaxDiff.setBounds(20, 100, 50, 40);
		spDel.setBounds(20, 120, 500, 500);
		bDel.setBounds(s.width - 200, s.height - 150, 50, 50);

		setLayout(null);
		add(tfMaxDiff);
		add(spDel);
		add(bDel);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
	}

	private void calcDelBasedWp(C1 c) {

		for (RegInf r : c.regs) {
			Point p1 = r.p;
			int mind = Integer.MAX_VALUE;
			WpInf minw = null;
			for (WpInf w : c.wps) {
				Point p2 = w.p;

				double dd = Math.sqrt(Math.pow(p2.y - p1.y, 2) + Math.pow(p2.x - p1.x, 2));
				int d = (int) dd;
				if (d < mind) {
					mind = d;
					minw = w;
				}
			}
			r.minw = minw;
			r.mind = mind;
		}

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (RegInf i : c.regs) {
			drawReg(g, i);
		}
		for (WpInf i : c.wps) {
			drawWp(g, i);
		}
	}

	private void drawWp(Graphics g, WpInf i) {

		g.setColor(Color.DARK_GRAY);
		g.setFont(font);
		int x = (i.bX / f) + ox;
		int y = (i.bY / f) + oy;
		g.drawString(i.n, x, y);
		g.fillOval(x, y, 10, 10);
	}

	private void drawReg(Graphics g, RegInf i) {
		g.setColor(Color.LIGHT_GRAY);
		int x = (i.bX / f) + ox;
		int y = (i.bY / f) + oy;
		int wh = 511 / f;
		int xh = x + (wh / 2);
		int yh = y + (wh / 2);
		String s = i.rX + "/" + i.rY;

		g.setColor(Color.BLACK);
		g.drawString(s, xh, yh);
		g.setFont(font);
		if (i.mind > maxDiff) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.GREEN);
		}
		g.fillRect(x, y, wh, wh);

		int xw = (i.minw.bX / f) + ox;
		int yw = (i.minw.bY / f) + oy;

		g.drawLine(xh, yh, xw, yw);
		g.drawString("" + i.mind, xh, yh + font.getSize());

		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font(Font.DIALOG, Font.PLAIN, 8));
		g.drawString(i.bX + "/" + i.bY, x, y + 15);
		g.fillOval(x - 5, y - 5, 10, 10);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int wr = e.getWheelRotation();
		if (e.isShiftDown()) {
			maxDiff += wr * 50;
			taDel.setText(null);
			tfMaxDiff.setText("" + maxDiff);
			for (RegInf r : getRegMaxDiff()) {
				taDel.append(r.f.getName() + "\r\n");
			}

		} else {
			if (f < 99 && f > 1) {
				f += wr;
				font = new Font(Font.DIALOG, Font.PLAIN, 80 / f);
			}
		}
		repaint();
	}

	private Set<RegInf> getRegMaxDiff() {
		return c.regs.stream().filter(r -> r.mind > maxDiff).collect(Collectors.toSet());
	}

	Point drawStart = null;

	@Override
	public void mouseDragged(MouseEvent e) {
		if (drawStart == null) {
			drawStart = e.getPoint();
		}
		ox = ox0 + e.getX();
		oy = oy0 + e.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println(ox + "/" + oy);
		if (drawStart != null) {
			drawStart = null;
//			ox0 = ox;
//			oy0 = oy;
		}
	}

}
