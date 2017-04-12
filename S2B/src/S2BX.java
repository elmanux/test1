import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class S2BX extends JPanel {

	private static final long serialVersionUID = 1L;

	JLabel lNorth, lCenter;

	public S2BX(String text, String help) {

		lNorth = getLabel(text, SwingConstants.TOP, SwingConstants.RIGHT);
		lCenter = getLabel("?", SwingConstants.CENTER, SwingConstants.CENTER);
		lCenter.setToolTipText(help);
		lCenter.setVisible(false);

		addMouseListener(getMlEnterExit());

		setOpaque(true);
		setBackground(S2B.colorMain);

		JPanel pCenter = new JPanel();
		pCenter.setBackground(S2B.colorMain);
		pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.LINE_AXIS));

		pCenter.add(Box.createHorizontalGlue());
		pCenter.add(lCenter);
		pCenter.add(Box.createHorizontalGlue());

		setLayout(new BorderLayout());
		add(lNorth, BorderLayout.NORTH);
		add(pCenter, BorderLayout.CENTER);

	}

	@Override
	public synchronized void addMouseListener(MouseListener mli) {
		lNorth.addMouseListener(mli);
		lCenter.addMouseListener(mli);
		super.addMouseListener(mli);
	}

	public void setText(String t) {
		lNorth.setText(t);
	}

	private JLabel getLabel(String text, int va, int ha) {
		JLabel l = new JLabel(text);
		l.setVerticalAlignment(va);
		l.setHorizontalAlignment(ha);
		// l.setBorder(BorderFactory.createLineBorder(Color.RED));
		l.setForeground(S2B.colorText);
		l.setBackground(S2B.colorMain);
		l.setOpaque(true);

		return l;
	}

	private MouseListener getMlEnterExit() {
		return new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				for (Component c : getAllComps()) {
					c.setBackground(S2B.colorText);
					c.setForeground(S2B.colorMain);
				}
				lCenter.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				for (Component c : getAllComps()) {
					c.setBackground(S2B.colorMain);
					c.setForeground(S2B.colorText);
				}
				lCenter.setVisible(false);

			}

			private Set<Component> getAllComps() {
				Set<Component> ac = new HashSet<>();
				for (Component c : getComponents()) {
					ac.add(c);
					if (c instanceof Container) {
						Container cc = (Container) c;
						ac.addAll(Arrays.asList(cc.getComponents()));
					}
				}
				return ac;
			}
		};
	}
}
