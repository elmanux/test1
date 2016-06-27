package gui;

import java.io.File;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class BackupTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	public static final int COL_NAME = 0;
	public static final int COL_CHECK = 1;
	public static final int COL_SEL = 2;

	public BackupTableModel(final GuiTest1 gui) {
		super(new String[] { "Name", "Check", " " }, 1);

		addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int changedRow = e.getFirstRow();
				if (e.getFirstRow() >= 0 && e.getColumn() == COL_SEL) {
					if (Boolean.TRUE.equals(getValueAt(changedRow, COL_SEL))) {
						for (int row = 0; row < getRowCount(); row++) {
							if (row != changedRow) {
								if (((Boolean) getValueAt(row, COL_SEL)).equals(Boolean.TRUE)) {
									setValueAt(Boolean.FALSE, row, COL_SEL);
								}
							}
						}
					}

					gui.refreshGui();
				}
			}
		});
	}

	void addBackup(File dirBackup, long check) {
		addRow(new Object[] { dirBackup, check, Boolean.FALSE });
	}

	File getSelectedBackup() {
		for (int row = 0; row < getRowCount(); row++) {
			if (((Boolean) getValueAt(row, COL_SEL)).equals(Boolean.TRUE)) {
				return (File) getValueAt(row, 0);
			}
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == COL_SEL;
	}

	@Override
	public Class<?> getColumnClass(int c) {
		Object o = getValueAt(0, c);
		if (o == null) {
			return String.class;
		}
		return o.getClass();
	}
}