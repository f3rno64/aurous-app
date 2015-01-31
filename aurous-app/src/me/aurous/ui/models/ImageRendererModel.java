package me.aurous.ui.models;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import me.aurous.utils.ModelUtils;

public class ImageRendererModel extends DefaultTableCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 3408189592723108040L;

	JLabel lbl = new JLabel();

	@Override
	public Component getTableCellRendererComponent(final JTable table,
			final Object value, final boolean isSelected,
			final boolean hasFocus, final int row, final int column) {
		final ImageIcon icon = me.aurous.utils.Utils.getSongIcon((String) table
				.getModel().getValueAt(row, ModelUtils.ART_INDEX));
		if (row == table.getSelectedRow()) {
			this.lbl.setForeground(new Color(213, 163, 0));

		} else {
			this.lbl.setForeground(Color.GRAY);
		}
		this.lbl.setFont(new Font("Calibri", Font.PLAIN, 14));
		this.lbl.setText((String) table.getModel().getValueAt(row, 0));
		this.lbl.setIcon(icon);

		return this.lbl;
	}
}
