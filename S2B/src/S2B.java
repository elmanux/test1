import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class S2B extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new S2B();
	}

	private int op = 80;

	private int miop = 10;

	private int maop = 100;

	private int opfac = 3;

	private Dimension siz = new Dimension(400, 200);

	private Point loc = new Point(100, 100);

	public static Color colorMain = Color.BLACK;

	public static Color colorText = Color.DARK_GRAY;

	private S2BX lCenter, lFac, lExit;

	public S2B() {
		lExit = new S2BX("       x  ", "Click to exit");
		lExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});

		lFac = new S2BX(String.valueOf(opfac), "<html>MouseWheel<br>\u2191: FAC=+1<br>\u2193: FAC=-1");
		lFac.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				changeFac(e.getWheelRotation());
			}
		});

		lCenter = new S2BX("<>", "<html>MouseWheel<br>\u2191: OP+=FAC <br>\u2193 OP-=FAC");
		lCenter.lNorth.setHorizontalAlignment(SwingConstants.LEFT);
		lCenter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switchSize();
			}
		});
		lCenter.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				moveFrame(e);
			}
		});
		lCenter.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				changeOp(e.getWheelRotation());
			}
		});

		JPanel pConf = new JPanel(new GridLayout(2, 1));
		pConf.add(lExit);
		pConf.add(lFac);
		pConf.setOpaque(false);
		pConf.setBorder(null);

		setLayout(new BorderLayout());
		add(lCenter, BorderLayout.CENTER);
		add(pConf, BorderLayout.EAST);

		setUndecorated(true);
		getContentPane().setBackground(colorMain);
		setOp();

		setSize(siz);
		setLocation(loc);

		setVisible(true);
	}

	protected void moveFrame(MouseEvent e) {
		if (getExtendedState() != MAXIMIZED_BOTH) {
			setLocation(e.getXOnScreen(), e.getYOnScreen());
		}
	}

	protected void changeOp(int wheelRotation) {
		int nop = op - (wheelRotation * opfac);
		if (nop > maop) {
			nop = maop;
		} else if (nop < miop) {
			nop = miop;
		}
		if (nop != op) {
			op = nop;
			setOp();
		}
	}

	protected void switchSize() {
		int extendedState = getExtendedState();
		if (MAXIMIZED_BOTH == extendedState) {
			setSize(siz);
			setLocation(loc);
		} else {
			loc = getLocation();
			setExtendedState(MAXIMIZED_BOTH);
		}
	}

	protected void changeFac(int wheelRotation) {
		int nfac = opfac - wheelRotation;
		if (nfac > 0 && nfac <= 20) {
			opfac = nfac;
			lFac.setText(String.valueOf(opfac));
		}
	}

	private void setOp() {
		setOpacity((float) op / 100);
		lCenter.setText(String.valueOf(op));
	}

}