import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Ff extends JFrame {
	public static void main(String[] args) throws Exception {
		new Ff();
	}

	private Robot r = new Robot();
	private JButton b = new JButton("x");
	private boolean f;

	public Ff() throws Exception {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("closing");

				dispose();
			}
		});

		addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				System.out.println("focus gained");
				f = true;
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				System.out.println("focus lost");
				f = false;
			}
		});

		b.setFocusable(true);
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						f = false;
						System.out.println("start....");
						r.delay(3000);

						while (!f) {
							System.out.println("down");
							r.mousePress(InputEvent.BUTTON3_DOWN_MASK);
							r.delay(1000);
							System.out.println("up");
							r.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
							System.out.println(f);
						}
					}
				});

			}
		});

		add(b);
		setSize(200, 200);
		setAlwaysOnTop(true);
		setVisible(true);
	}

}
