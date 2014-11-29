package me.aurous.utils.playlist;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import me.aurous.grabbers.HateChanGrabber;
import me.aurous.grabbers.RedditGrabber;
import me.aurous.player.Settings;
import me.aurous.swinghacks.GhostText;
import me.aurous.ui.UISession;
import me.aurous.ui.listeners.ContextMenuMouseListener;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.ui.widgets.ImporterWidget;
import me.aurous.utils.Constants;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;

/**
 * @author Andrew
 *
 */
public class PlayListUtils {

	/**
	 * popup panel to add url to playlist
	 */
	public static void additionToPlayListPrompt() {

		if ((Utils.isNull(Settings.getLastPlayList()))
				|| Settings.getLastPlayList().isEmpty()) {
			JOptionPane.showMessageDialog(new JFrame(),
					"You do not have any playlist loaded!", "Uh oh",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		final JTextField urlField = new JTextField();
		urlField.addMouseListener(new ContextMenuMouseListener());
		urlField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {

			}
		});
		new GhostText("https://www.youtube.com/watch?v=TU3b1qyEGsE", urlField);
		urlField.setHorizontalAlignment(SwingConstants.CENTER);
		final JPanel panel = new JPanel(new GridLayout(0, 1));

		panel.add(new JLabel("Paste media url"));
		panel.add(urlField);

		final int result = JOptionPane.showConfirmDialog(null, panel,
				"Add to Playlist", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			addUrlToPlayList(urlField.getText());
		} else {

		}

	}

	public static void addUrlToPlayList(final String url) {
		if (url.isEmpty()) {
			return;
		}
		if (url.contains("&list=")) {
			return;
		} else {
			try {
				final String filename = Settings.getLastPlayList();

				final FileWriter fw = new FileWriter(filename, true); // the
				String data = "";
				// true
				if (url.contains("vk.me/")) {
					data = url;
				} else {
					data = getaddRules(url);
				}

				fw.write("\n" + data);// appends

				fw.close();

			} catch (final IOException ioe) {
				final ExceptionWidget eWidget = new ExceptionWidget(
						Utils.getStackTraceString(ioe, ""));
				eWidget.setVisible(true);
			}
		}

	}

	public static void buildPlayList(final String playListItems,
			final String playListName) {
		final Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					final String name = Constants.DATA_PATH + "playlist/"
							+ playListName + ".plist";
					final String header = "Title, Artist, Time, Date Added, User, Album, ALBUMART_INDEX, link";
					final File file = new File(name);

					final PrintWriter printWriter = new PrintWriter(file);
					final String[] lines = playListItems.split("\n");
					printWriter.println(header);
					for (final String line : lines) {
						if (UISession.getBuilderWidget().isOpen()) {

							final String playListItem = MediaUtils
									.getBuiltString(line.trim());
							if (!playListItem.isEmpty()) {

								printWriter.println(playListItem);
							} else {
								continue;
							}

						} else {

							printWriter.close();
							deletePlayList(name);
							return;
						}

					}
					printWriter.close();
					UISession.getBuilderWidget().getLoadingIcon()
					.setVisible(false);
					UISession.getBuilderWidget().getPlayListTextArea()
					.setEditable(true);
					UISession.getBuilderWidget().getBuildListButton()
					.setEnabled(true);
					UISession.getBuilderWidget().getPlayListNameTextField()
					.setEditable(false);

				} catch (final FileNotFoundException e) {
					final ExceptionWidget eWidget = new ExceptionWidget(
							Utils.getStackTraceString(e, ""));
					eWidget.setVisible(true);
				}
			}
		};
		thread.start();

	}

	public static void deletePlayList(final JList<?> list)

	{
		final String path = list.getSelectedValue().toString();
		try {

			final File file = new File(path);

			if (file.delete()) {
				// System.out.println(file.getName() + " is deleted!");
			} else {
				// System.out.println("Delete operation is failed.");
			}

		} catch (final Exception e) {

			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);

		}
	}

	public static void deletePlayList(final String path)

	{

		try {

			final File file = new File(path);

			if (file.delete()) {
				// System.out.println(file.getName() + " is deleted!");
			} else {
				// System.out.println("Delete operation is failed.");
			}

		} catch (final Exception e) {

			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		}
	}

	public static void disableImporterInterface() {

		final ImporterWidget widget = UISession.getImporterWidget();
		widget.getImportProgressBar().setVisible(true);

		widget.getImportPlayListButton().setEnabled(false);
		widget.getImportInstrucLabel().setText("Importing Playlist");
		widget.getEnterPlaylistLabel().setText("");

	}

	public static String getaddRules(final String sourceURL) {
		if (sourceURL.contains("youtube")) {

			final String tubeLine = YouTubeDataFetcher
					.buildPlayListLine(sourceURL);
			return tubeLine;

		} else if (sourceURL.contains("soundcloud")) {

			// return SoundCloudGrabber.buildPlayListLine(sourceURL);
		} else {
			JOptionPane.showMessageDialog(null, "No importer found!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return "";
	}

	public static void getImportRules(final String sourceURL,
			final String playListName) {
		if (sourceURL.contains("youtube")) {

			YouTubePlayListImporter.importYoutubePlayList(sourceURL,
					playListName);

		} else if (sourceURL.contains("soundcloud")) {

		} else if (sourceURL.contains("reddit")) {
			final RedditGrabber redditGrabber = new RedditGrabber(sourceURL,
					playListName);
			redditGrabber.buildPlayList();
		} else if (sourceURL.contains("8chan")) {
			final HateChanGrabber hateChanGrabber = new HateChanGrabber(
					sourceURL, playListName);
			hateChanGrabber.buildPlayList();

		} else {
			JOptionPane.showMessageDialog(null, "No importer found!", "Error",
					JOptionPane.ERROR_MESSAGE);
			UISession.getImporterWidget().getImportProgressBar()
			.setVisible(false);
		}
	}

	/**
	 * popup panel to create a playlist
	 */
	public static String importPlayListPrompt() {
		final JTextField urlField = new JTextField();
		final GhostText gText = new GhostText("Enter service url", urlField);

		urlField.addMouseListener(new ContextMenuMouseListener());
		urlField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {

			}
		});
		urlField.setHorizontalAlignment(SwingConstants.CENTER);
		gText.setHorizontalAlignment(SwingConstants.CENTER);

		final JPanel panel = new JPanel(new GridLayout(0, 1));

		panel.add(new JLabel("Enter a PlayList URL"));
		panel.add(urlField);
		final int result = JOptionPane.showConfirmDialog(null, panel,
				"Add to Service", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			if (!urlField.getText().isEmpty()) {
				return urlField.getText();
			}
		} else {

		}

		return "";

	}

	/**
	 * @author Andrew
	 *
	 *         Deletes line from PlayList
	 */
	public static void removeLineFromPlayList(final String file,
			final String lineToRemove) {

		try {

			final File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			final File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			final BufferedReader br = new BufferedReader(new FileReader(file));
			final PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {

				if (!line.contains(lineToRemove)) {

					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile)) {
				System.out.println("Could not rename file");
			}
			// loadPlayList(PlayerUtils.currentPlayList);
		} catch (final FileNotFoundException ex) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(ex, ""));
			eWidget.setVisible(true);
		} catch (final IOException ex) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(ex, ""));
			eWidget.setVisible(true);
		}
	}

	/**
	 * @author Andrew
	 *
	 *         Removes a row from the JTable while deleting the line from the
	 *         playlist
	 */
	public static void removeSelectedRows(final JTable table) {
		final DefaultTableModel model = (DefaultTableModel) table.getModel();
		final int[] rows = table.getSelectedRows();
		removeLineFromPlayList(Settings.getLastPlayList(),
				(String) table.getValueAt(rows[0], 7));

		for (int i = 0; i < rows.length; i++) {
			model.removeRow(rows[i] - i);
		}

	}

	public static void resetImporterInterface() {
		final ImporterWidget widget = UISession.getImporterWidget();
		if (widget != null) {
			widget.getImportProgressBar().setValue(0);
			widget.getImportProgressBar().setVisible(false);
			widget.getImportInstrucLabel().setText("Import Playlist");
			widget.getEnterPlaylistLabel().setText("Enter a Playlist Name");
			widget.getImportPlayListButton().setEnabled(true);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void watchPlayListDirectory(final JList<?> displayList) {

		try {
			final WatchService watcher = FileSystems.getDefault()
					.newWatchService();
			final Path dir = Paths.get(Constants.DATA_PATH + "playlist/");
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			System.out.println("Watch Service registered for dir: "
					+ dir.getFileName());

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (final InterruptedException ex) {
					return;
				}

				for (final WatchEvent<?> event : key.pollEvents()) {
					final WatchEvent.Kind<?> kind = event.kind();

					java.awt.EventQueue
							// i dont question the Java API, it works now.
					.invokeLater(() -> {

								final DefaultListModel playListModel = new DefaultListModel();

						final File[] playListFolder = new File(
								Constants.DATA_PATH + "playlist/")
						.listFiles();
						if ((kind == ENTRY_CREATE)
								|| ((kind == ENTRY_DELETE)
										&& (playListModel != null) && (playListFolder != null))) {

							for (final File file : playListFolder) {
								playListModel.addElement(file);
							}
							displayList.setModel(playListModel);
									// / displayList.updateUI();
						}
					});

				}

				final boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (final IOException ex) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(ex, ""));
			eWidget.setVisible(true);
		}

	}
}
