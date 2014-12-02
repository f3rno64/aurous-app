package me.aurous.ui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import me.aurous.apis.impl.webapi.WebPlaylistSubmit;
import me.aurous.player.Settings;
import me.aurous.player.functions.PlayerFunctions;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.ui.widgets.SettingsWidget;
import me.aurous.ui.widgets.ShareWidget;
import me.aurous.utils.Constants;
import me.aurous.utils.ModelUtils;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;
import me.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class PlayListPanel extends JPanel implements ActionListener {
	public static class MyCellRenderer extends DefaultListCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1976041664275487088L;

		@Override
		public Component getListCellRendererComponent(final JList<?> list,
				final Object value, final int index, final boolean isSelected,
				final boolean cellHasFocus) {
			if (value instanceof File) {

				final File file = (File) value;

				setText(file.getName().replace(".plist", ""));
				setIcon(new ImageIcon(
						PlayListPanel.class.getResource("/resources/music.png")));

				if (isSelected) {
					setBackground(new Color(36, 36, 36));
					setForeground(Color.LIGHT_GRAY);
				} else {

					setBackground(new Color(36, 36, 36));
					setForeground(Color.GRAY);
				}

				setEnabled(list.isEnabled());
				setFont(list.getFont());
				setOpaque(true);
				if (file.getName().contains("blank.plist")) {

				}
			}
			return this;
		}
	}

	private static Image scale(final Image image, final int width,
			final int height) {
		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	public static void setAlbumArt(final Image image) {
		albumArtLabel.setIcon(new ImageIcon(scale(image, 200, 200)));
	}

	public static void setSongInformation(final String title,
			final String artist) {
		final String information = String.format(
				"<html><strong>%s</strong><br>%s</html>", title, artist);
		MediaUtils.activeInfo = title + "\n" + artist;
		songInformation.setText(information);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 7941760374900934885L;
	public static boolean canSetLast = false;

	private final Color background = new Color(35, 35, 35);

	// public static void run() {

	// EventQueue.invokeLater(() -> new PlayListPanel());

	// }

	// private final PlayListFunctions plFunctions = new PlayListFunctions();

	JPopupMenu popup;

	private static JLabel albumArtLabel;

	private static JLabel songInformation;

	public PlayListPanel() {
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(200, getHeight()));

		setBackground(background);

		final JList<?> displayList = new JList<Object>(new File(
				Constants.DATA_PATH + "playlist/").listFiles());

		displayList.setBackground(background);
		displayList.setForeground(Color.WHITE);

		displayList.setFont(new Font("Calibri", Font.PLAIN, 14));

		displayList
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		displayList.setCellRenderer(new MyCellRenderer());
		displayList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
		displayList.setName("displayList");
		displayList.setVisibleRowCount(-1);
		displayList.setForeground(new Color(180, 182, 184));
		displayList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final int c = e.getKeyCode();
				if (c == KeyEvent.VK_DELETE) {

					PlayListUtils.deletePlayList(displayList);

				} else if (c == KeyEvent.VK_ADD) {

				} else if (c == KeyEvent.VK_LEFT) {

				} else if (c == KeyEvent.VK_RIGHT) {

				} else if (c == KeyEvent.VK_ENTER) {

				}
			}
		});
		final MouseListener mouseListener = new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent mouseEvent) {

				final int index = displayList.locationToIndex(mouseEvent
						.getPoint());
				if (index >= 0) {
					final Object o = displayList.getModel().getElementAt(index);
					// PlayListFrame.this.plFunctions.loadPlayList(o
					// .toString());
					final String playlist = o.toString();
					ModelUtils.loadPlayList(playlist);
					if (canSetLast == true) {
						canSetLast = false;
						Settings.setLastPlayList(playlist);

					}

				}

			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (e.isPopupTrigger()) {

					final JList<?> list = (JList<?>) e.getSource();
					list.setSelectedIndex(list.locationToIndex(e.getPoint()));

					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		final JScrollPane scrollPane = new JScrollPane(displayList);
		scrollPane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(200, getHeight()));

		scrollPane.setBorder(new EtchedBorder());

		albumArtLabel = new JLabel();
		albumArtLabel.setPreferredSize(new Dimension(200, 200));

		albumArtLabel.setBorder(BorderFactory.createEmptyBorder());

		setAlbumArt(new ImageIcon(
				SettingsWidget.class
						.getResource("/resources/album-placeholder.png"))
				.getImage());

		songInformation = new JLabel();
		songInformation.setHorizontalAlignment(SwingConstants.LEFT);
		songInformation.setForeground(Color.LIGHT_GRAY);

		add(scrollPane);

		add(albumArtLabel);

		add(songInformation);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		albumArtLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		songInformation.setAlignmentX(Component.LEFT_ALIGNMENT);

		popup = new JPopupMenu();
		final JMenuItem playItem = new JMenuItem("Play");
		playItem.addActionListener(this);
		popup.add(playItem);
		final JMenuItem loadItem = new JMenuItem("Load");
		loadItem.addActionListener(this);
		popup.add(loadItem);
		final JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(this);
		popup.add(deleteItem);
		final JMenuItem shareItem = new JMenuItem("Share");
		shareItem.addActionListener(this);
		popup.add(shareItem);

		displayList.addMouseListener(mouseListener);
		final Thread thread = new Thread(
				() -> PlayListUtils.watchPlayListDirectory(displayList));
		// start the threadff
		thread.start();
		repaint();
		songInformation.setFont(new Font("Calibri", Font.PLAIN, 14));

	}

	public static String stripExtension(final String s) {
		return (s != null) && (s.lastIndexOf(".") > 0) ? s.substring(0,
				s.lastIndexOf(".")) : s;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final Component c = (Component) e.getSource();
		final JPopupMenu popup = (JPopupMenu) c.getParent();
		final JList<?> list = (JList<?>) popup.getInvoker();
		final Object o = list.getSelectedValue();
		final String playlist = o.toString();
		switch (e.getActionCommand()) {
		case "Delete":
			PlayListUtils.deletePlayList(list);
			break;
		case "Play":
			ModelUtils.loadPlayList(playlist);
			PlayerFunctions.seekNext();
			break;
		case "Load":
			ModelUtils.loadPlayList(playlist);

			break;
		case "Share":
			try {
				final String playListName = Utils.stripExtension(new File(
						playlist).getName());
				String content = new String(Files.readAllBytes(Paths
						.get(playlist)));
				
				if (Utils.isJSONValid(content)) {
					StringBuilder convertedPlayList = new StringBuilder(
							String.format(
									"Title, Artist, Time, Date Added, User, Album, ALBUMART_INDEX, link %s",
									System.lineSeparator()));
					JSONArray json = new JSONArray(content);
					for (int i = 0; i < json.length(); i++) {
						JSONObject obj = json.getJSONObject(i);

						String title = obj.getString("Title").trim();
						String artist = obj.getString("Artist").trim();
						String time = obj.getString("Time").trim();
						String date = obj.getString("Date Added").trim();
						String user = obj.getString("User").trim();
						String album = obj.getString("Album").trim();
						String album_art = obj.getString("ALBUMART_INDEX").trim();
						String link = obj.getString("link").trim();
						convertedPlayList.append(String.format(
								"%s, %s, %s, %s, %s, %s, %s, %s %s", title, artist,
								time, date, user, album, album_art, link,
								System.lineSeparator()));
					}
					content = convertedPlayList.toString();
				}
				
				final WebPlaylistSubmit sharePlaylist = new WebPlaylistSubmit(
						content, playListName);
				sharePlaylist.submitPlaylist();
				final ShareWidget shareWidget = new ShareWidget(
						sharePlaylist.getSubmitMessage());
				shareWidget.setVisible(true);

			} catch (final Exception e1) {

				final ExceptionWidget eWidget = new ExceptionWidget(
						Utils.getStackTraceString(e1, ""));
				eWidget.setVisible(true);
			}
			break;
		}
		// System.out.println(searchTable.getSelectedRow() + " : " +
		// searchTable.getSelectedColumn());
	}

}