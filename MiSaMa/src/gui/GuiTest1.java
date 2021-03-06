package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import copy.CopyUtil;

public class GuiTest1 extends JFrame implements ActionListener, WindowFocusListener {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new GuiTest1();
	}

	private String defaultSaveDir = "C:\\Users\\elman\\AppData\\Roaming\\.minecraft\\saves";

	private File dirWorld;

	private JLabel lSavedir, lWorld, lCheck, lBackupSize;

	private JButton bSelectWorld, bBackup, bRestore, bRefresh;

	private JFileChooser fileChooser;

	private BackupTable tBackups;

	private JScrollPane spBackups;

	private BackupTableModel tmBackups;

	private CopyUtil copyUtil = new CopyUtil();

	public GuiTest1() {
		super("SGB");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Path path = FileSystems.getDefault().getPath(defaultSaveDir);
		if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			fileChooser = new JFileChooser(path.toFile());
		} else {
			fileChooser = new JFileChooser();
		}

		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		lSavedir = new JLabel(defaultSaveDir);
		lWorld = new JLabel("?");
		lCheck = new JLabel("?");
		lBackupSize = new JLabel("?");

		bSelectWorld = getButton("select");
		bBackup = getButton("backup");
		bRestore = getButton("restore");
		bRestore.setEnabled(false);
		bRefresh = getButton("refresh");

		tmBackups = new BackupTableModel(this);
		tBackups = new BackupTable(tmBackups);
		spBackups = new JScrollPane(tBackups);

		buildLayout();

		addWindowFocusListener(this);
		setSize(500, 400);
		setVisible(true);

		bSelectWorld.doClick();

//		dirWorld = new File(defaultSaveDir + "/W17");
//		setNewSelectedWorld(dirWorld);
	}

	private JButton getButton(String text) {
		JButton b = new JButton(text);
		b.setActionCommand(text);
		b.addActionListener(this);
		return b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String eventActionCommand = e.getActionCommand();
		if (eventActionCommand.equals(bSelectWorld.getActionCommand())) {
			int showOpenDialog = fileChooser.showOpenDialog(GuiTest1.this);
			if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
				setNewSelectedWorld(fileChooser.getSelectedFile());
			}
		} else {
			if (eventActionCommand.equals(bBackup.getActionCommand())) {
				copyUtil.backup(dirWorld);
			} else if (eventActionCommand.equals(bRestore.getActionCommand())) {
				copyUtil.restore(tmBackups.getSelectedBackup(), dirWorld);
			}
			refreshBackupList();
		}
	}

	private void buildLayout() {

		JPanel pDirLabels = new JPanel(new GridLayout(4, 1));
		pDirLabels.add(lSavedir);
		pDirLabels.add(lWorld);
		pDirLabels.add(lCheck);
		pDirLabels.add(lBackupSize);

		JPanel pDirButtons = new JPanel(new GridLayout(4, 1));
		pDirButtons.add(bSelectWorld);
		pDirButtons.add(bRefresh);
		pDirButtons.add(bBackup);

		JPanel pNorth = new JPanel(new BorderLayout());
		pNorth.add(pDirLabels, BorderLayout.CENTER);
		pNorth.add(pDirButtons, BorderLayout.EAST);

		setLayout(new BorderLayout());

		pNorth.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		add(pNorth, BorderLayout.NORTH);
		add(spBackups, BorderLayout.CENTER);
		add(bRestore, BorderLayout.SOUTH);
	}

	public void refreshGui() {
		bRestore.setEnabled(tmBackups.getSelectedBackup() != null);
		lCheck.setText("" + copyUtil.getChecksum(dirWorld));
		lBackupSize.setText("" + copyUtil.getBackupSizeInGb(dirWorld));
	}

	protected void refreshBackupList() {

		tmBackups.setRowCount(0);

		Map<File, Long> backups = copyUtil.getBackups(dirWorld);
		for (Entry<File, Long> e : backups.entrySet()) {
			tmBackups.addBackup(e.getKey(), e.getValue());
		}

		refreshGui();
		repaint();
	}

	protected void setNewSelectedWorld(File selectedWorld) {
		dirWorld = selectedWorld;
		lSavedir.setText(dirWorld.getParent());
		lWorld.setText(selectedWorld.getName());
		lCheck.setText("" + copyUtil.getChecksum(selectedWorld));
		refreshBackupList();
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		refreshGui();
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		//
	}

}
