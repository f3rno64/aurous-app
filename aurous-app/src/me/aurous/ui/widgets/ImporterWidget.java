package me.aurous.ui.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window.Type;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import me.aurous.swinghacks.GhostText;
import me.aurous.ui.UISession;
import me.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class ImporterWidget {

	public static void openImporter() {
		if ((UISession.getImporterWidget() != null)
				&& UISession.getImporterWidget().isOpen()) {
			UISession.getImporterWidget().getWidget().toFront();
			UISession.getImporterWidget().getWidget().repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final ImporterWidget window = new ImporterWidget();
				UISession.setImporterWidget(window);
				UISession.getImporterWidget().getWidget().setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JFrame importerWidget;

	private JTextField playListNameField;
	private String playListURL = "";

	private JButton redditServiceButton;

	public JButton importPlayListButton;

	public JProgressBar importProgressBar;

	public JLabel importInstrucLabel;

	public JLabel lblEnterAPlaylist;
	private JButton hateChanServiceButton;

	public ImporterWidget() {
		initialize();
	}

	public JLabel getEnterPlaylistLabel() {
		return lblEnterAPlaylist;
	}

	public JLabel getImportInstrucLabel() {
		return importInstrucLabel;
	}

	public JButton getImportPlayListButton() {
		return importPlayListButton;
	}

	public JProgressBar getImportProgressBar() {
		return importProgressBar;
	}

	public JTextField getPlayListNameField() {
		return playListNameField;
	}

	public JFrame getWidget() {
		return importerWidget;
	}

	private void initialize() {
		importerWidget = new JFrame();
		importerWidget.setResizable(false);
		importerWidget.setType(Type.UTILITY);
		importerWidget.setIconImage(Toolkit.getDefaultToolkit().getImage(
				ImporterWidget.class.getResource("/resources/aurouslogo.png")));
		importerWidget.setTitle("Playlist Importer");
		importerWidget.getContentPane().setBackground(new Color(32, 33, 35));
		importerWidget.setBounds(100, 100, 379, 372);
		importerWidget
		.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		importerWidget.getContentPane().setLayout(null);
		importerWidget.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {
				final int confirm = JOptionPane.showOptionDialog(
						importerWidget,
						"Are You Sure You Want to Close this Importer?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					UISession.setImporterWidget(null);
					importerWidget.dispose();
				}

			}
		});

		final JPanel servicePanel = new JPanel();
		servicePanel.setBackground(Color.DARK_GRAY);
		servicePanel.setBounds(0, 67, 373, 65);
		importerWidget.getContentPane().add(servicePanel);
		servicePanel.setLayout(null);

		importProgressBar = new JProgressBar();
		importProgressBar.setVisible(false);
		importProgressBar.setStringPainted(true);
		importProgressBar.setBounds(0, 180, 373, 37);
		importerWidget.getContentPane().add(importProgressBar);
		final JLabel playListURLLabel = new JLabel("");
		playListURLLabel.setForeground(Color.WHITE);
		playListURLLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		playListURLLabel.setBounds(223, 311, 150, 14);
		importerWidget.getContentPane().add(playListURLLabel);

		final JButton youTubeServiceButton = new JButton("");
		youTubeServiceButton.addActionListener(e -> {

			ImporterWidget.this.playListURL = PlayListUtils
					.importPlayListPrompt();
			playListURLLabel.setText(ImporterWidget.this.playListURL);

		});

		youTubeServiceButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		youTubeServiceButton.setBorderPainted(false);
		youTubeServiceButton.setBorder(null);
		youTubeServiceButton.setMargin(new Insets(0, 0, 0, 0));
		youTubeServiceButton.setContentAreaFilled(false);
		youTubeServiceButton.setIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/w_youtube.png")));
		youTubeServiceButton.setRolloverIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/w_youtube_h.png")));

		youTubeServiceButton.setBounds(122, 11, 35, 35);

		servicePanel.add(youTubeServiceButton);

		redditServiceButton = new JButton("");
		redditServiceButton.addActionListener(e -> {
			ImporterWidget.this.playListURL = PlayListUtils
					.importPlayListPrompt();
			playListURLLabel.setText(ImporterWidget.this.playListURL);

		});
		redditServiceButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		redditServiceButton.setIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/w_reddit.png")));
		redditServiceButton.setRolloverIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/w_reddit_h.png")));
		redditServiceButton.setMargin(new Insets(0, 0, 0, 0));
		redditServiceButton.setContentAreaFilled(false);
		redditServiceButton.setBorderPainted(false);
		redditServiceButton.setBorder(null);
		redditServiceButton.setBounds(167, 11, 35, 35);
		servicePanel.add(redditServiceButton);

		hateChanServiceButton = new JButton("");
		hateChanServiceButton.setIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/8chan.png")));
		hateChanServiceButton.setRolloverIcon(new ImageIcon(
				ImporterWidget.class.getResource("/resources/8chan_h.png")));
		hateChanServiceButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		hateChanServiceButton.setMargin(new Insets(0, 0, 0, 0));
		hateChanServiceButton.setContentAreaFilled(false);
		hateChanServiceButton.setBorderPainted(false);
		hateChanServiceButton.setBorder(null);
		hateChanServiceButton.setBounds(212, 11, 35, 35);
		hateChanServiceButton.addActionListener(e -> {
			ImporterWidget.this.playListURL = PlayListUtils
					.importPlayListPrompt();
			playListURLLabel.setText(ImporterWidget.this.playListURL);

		});
		servicePanel.add(hateChanServiceButton);

		final JLabel lblSelectAService = new JLabel("Select a Service");
		lblSelectAService.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblSelectAService.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectAService.setForeground(Color.WHITE);
		lblSelectAService.setBounds(54, 11, 251, 50);
		importerWidget.getContentPane().add(lblSelectAService);

		this.playListNameField = new JTextField();
		this.playListNameField.setBorder(UIManager
				.getBorder("TextField.border"));
		this.playListNameField.setHorizontalAlignment(SwingConstants.CENTER);
		this.playListNameField.setFont(new Font("Segoe UI", Font.PLAIN, 23));
		this.playListNameField.setBounds(0, 180, 373, 37);
		importerWidget.getContentPane().add(this.playListNameField);
		final GhostText ghostText = new GhostText("FMA OST",
				this.playListNameField);
		ghostText.setHorizontalAlignment(SwingConstants.CENTER);
		ghostText.setHorizontalTextPosition(SwingConstants.CENTER);
		this.playListNameField.setColumns(10);

		lblEnterAPlaylist = new JLabel("Enter a Playlist Name");
		lblEnterAPlaylist.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterAPlaylist.setForeground(Color.WHITE);
		lblEnterAPlaylist.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblEnterAPlaylist.setBounds(54, 133, 251, 50);
		importerWidget.getContentPane().add(lblEnterAPlaylist);

		importPlayListButton = new JButton("");
		importPlayListButton.addActionListener(e -> {
			if (!ImporterWidget.this.playListURL.trim().isEmpty()
					&& !ImporterWidget.this.playListNameField.getText().trim()
					.isEmpty()) {

				importProgressBar.setVisible(true);

				PlayListUtils.getImportRules(this.playListURL,
						this.playListNameField.getText());

			} else {
				JOptionPane.showMessageDialog(importerWidget,
						"No playlist url or name detected", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		});
		importPlayListButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		importPlayListButton.setBorderPainted(false);
		importPlayListButton.setBorder(null);
		importPlayListButton.setMargin(new Insets(0, 0, 0, 0));
		importPlayListButton.setContentAreaFilled(false);
		importPlayListButton.setIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/ic_export_import.png")));
		importPlayListButton.setBounds(149, 261, 64, 64);
		importerWidget.getContentPane().add(importPlayListButton);

		importInstrucLabel = new JLabel("Import Playlist");
		importInstrucLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		importInstrucLabel.setForeground(Color.WHITE);
		importInstrucLabel.setHorizontalAlignment(SwingConstants.CENTER);
		importInstrucLabel.setBounds(94, 228, 181, 22);
		importerWidget.getContentPane().add(importInstrucLabel);
		importerWidget.setLocationRelativeTo(UISession.getPresenter()
				.getAurousFrame());

	}

	public boolean isOpen() {
		return importerWidget == null ? false : importerWidget.isVisible();
	}
}
