package me.aurous.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import me.aurous.player.Settings;
import me.aurous.ui.models.ForcedListSelectionModel;
import me.aurous.utils.Constants;
import me.aurous.utils.ModelUtils;
import me.aurous.utils.media.MediaUtils;
import me.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class TabelPanel extends JPanel implements ActionListener {
	public static JTable table;
	public static DefaultTableModel tableModel;
	private final Color background = new Color(35, 35, 35);
	JPopupMenu popup;

	/**
	 *
	 */
	private static final long serialVersionUID = -5598764407384505341L;
	protected JScrollPane scroller;

	public TabelPanel() {

		initPanel();

	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final Component c = (Component) e.getSource();
		final JPopupMenu popup = (JPopupMenu) c.getParent();
		final JTable table = (JTable) popup.getInvoker();

		switch (e.getActionCommand()) {
		case "Delete":
			PlayListUtils.removeSelectedRows(table);
			break;
		case "Play":
			MediaUtils.switchMedia(table);
			break;
		case "Share":
			// System.out.println("Sharing");
			break;
		case "Copy URL":
			MediaUtils.copyMediaURL(table);
			break;
		}
		// System.out.println(searchTable.getSelectedRow() + " : " +
		// searchTable.getSelectedColumn());
	}

	public void initPanel() {
		setBackground(background);
		setLayout(new BorderLayout());

		table = new JTable();

		table.setBackground(background);
		table.setForeground(Color.GRAY);
		table.setOpaque(true);

		final JTableHeader header = table.getTableHeader();
		header.setOpaque(false);
		header.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		header.setForeground(Color.GRAY);
		header.setAutoscrolls(true);

		header.setFont(new Font("Calibri", Font.PLAIN, 14));

		header.setBorder(BorderFactory.createEmptyBorder());

		if (Settings.getLastPlayList().isEmpty()) {

			ModelUtils
			.loadPlayList(Constants.DATA_PATH + "scripts/blank.plist");
		} else {
			ModelUtils.loadPlayList(Settings.getLastPlayList());
		}

		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final int c = e.getKeyCode();
				final JTable target = (JTable) e.getSource();
				if (c == KeyEvent.VK_DELETE) {
					PlayListUtils.removeSelectedRows(target);
					// PlayListUtils.removeLineFromPlayList(file, lineToRemove)

				} else if (c == KeyEvent.VK_ADD) {

				} else if (c == KeyEvent.VK_LEFT) {

				} else if (c == KeyEvent.VK_RIGHT) {

				} else if (c == KeyEvent.VK_ENTER) {

				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					final JTable target = (JTable) e.getSource();

					target.getSelectedRow();
					MediaUtils.switchMedia(target);
				}

			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					final JTable source = (JTable) e.getSource();
					final int row = source.rowAtPoint(e.getPoint());
					final int column = source.columnAtPoint(e.getPoint());

					if (!source.isRowSelected(row)) {
						source.changeSelection(row, column, false, false);
					}

					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});
		table.setSelectionModel(new ForcedListSelectionModel());

		this.scroller = new javax.swing.JScrollPane(table);

		final TableColumn hiddenLink = table.getColumnModel().getColumn(
				ModelUtils.LINK_INDEX);
		hiddenLink.setMinWidth(0);
		hiddenLink.setPreferredWidth(0);
		hiddenLink.setMaxWidth(0);
		hiddenLink.setCellRenderer(new ModelUtils.InteractiveRenderer(
				ModelUtils.LINK_INDEX));
		final TableColumn hiddenAlbumArt = table.getColumnModel().getColumn(
				ModelUtils.ALBUMART_INDEX);
		hiddenAlbumArt.setMinWidth(0);
		hiddenAlbumArt.setPreferredWidth(0);
		hiddenAlbumArt.setMaxWidth(0);
		hiddenAlbumArt.setCellRenderer(new ModelUtils.InteractiveRenderer(
				ModelUtils.ALBUMART_INDEX));

		// setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// setBorder(new EtchedBorder());

		// this.scroller.setBorder(BorderFactory.createEmptyBorder());

		popup = new JPopupMenu();

		final JMenuItem shareItem = new JMenuItem("Share");
		shareItem.addActionListener(this);
		popup.add(shareItem);
		final JMenuItem copyItem = new JMenuItem("Copy URL");
		copyItem.addActionListener(this);
		popup.add(copyItem);
		final JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(this);
		popup.add(deleteItem);
		final JMenuItem playItem = new JMenuItem("Play");
		playItem.addActionListener(this);
		popup.add(playItem);
		table.setAutoCreateRowSorter(true);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		add(this.scroller, BorderLayout.CENTER);
		table.setFillsViewportHeight(true);
		table.setSelectionBackground(table.getBackground());
		table.setSelectionForeground(new Color(213, 163, 0));
		table.setGridColor(new Color(44, 44, 44));
		table.setShowVerticalLines(false);
		table.setBorder(new EtchedBorder());
		table.setFont(new Font("Calibri", Font.PLAIN, 14));
		table.setName("playlist");

	}

}
