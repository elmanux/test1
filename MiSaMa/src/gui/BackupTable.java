package gui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class BackupTable extends JTable {

	private static final long serialVersionUID = 1L;

	public void sortByName() {
		// TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>)
		// getRowSorter();
		// sorter.getSortKeys().add(new
		// RowSorter.SortKey(BackupTableModel.COL_NAME, SortOrder.ASCENDING));
		// sorter.sort();
	}

	public BackupTable(BackupTableModel tm) {

		super(tm);

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// setAutoCreateRowSorter(true);

		TableRowSorter<TableModel> sorter = new TableRowSorter<>(getModel());
		setRowSorter(sorter);
		sorter.setSortKeys(new ArrayList<>());
		// sorter.sort();

		getColumnModel().getColumn(BackupTableModel.COL_CHECK).setMinWidth(100);
		getColumnModel().getColumn(BackupTableModel.COL_CHECK).setMaxWidth(100);
		getColumnModel().getColumn(BackupTableModel.COL_SEL).setMinWidth(20);
		getColumnModel().getColumn(BackupTableModel.COL_SEL).setMaxWidth(20);

		// setTableHeader(null);
		setShowVerticalLines(false);
		setDefaultRenderer(Boolean.class, new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JRadioButton rb = new JRadioButton();
				rb.setBackground(Color.WHITE);
				rb.setSelected((boolean) value);
				return rb;
			}
		});
		setDefaultRenderer(Long.class, new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JTextField t = new JTextField("" + value);
				t.setBorder(null);
				return t;
			}
		});
		setDefaultRenderer(File.class, new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				File f = (File) value;
				JTextField t = new JTextField(f.getName());
				t.setBorder(null);
				return t;
			}
		});
	}
}
