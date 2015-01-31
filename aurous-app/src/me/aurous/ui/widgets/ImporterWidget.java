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
import me.aurous.utils.Utils;
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
		return this.lblEnterAPlaylist;
	}

	public JLabel getImportInstrucLabel() {
		return this.importInstrucLabel;
	}

	public JButton getImportPlayListButton() {
		return this.importPlayListButton;
	}

	public JProgressBar getImportProgressBar() {
		return this.importProgressBar;
	}

	public JTextField getPlayListNameField() {
		return this.playListNameField;
	}

	public JFrame getWidget() {
		return this.importerWidget;
	}

	private void initialize() {
		this.importerWidget = new JFrame();
		this.importerWidget.setResizable(false);
		this.importerWidget.setType(Type.UTILITY);
		this.importerWidget.setIconImage(Toolkit.getDefaultToolkit().getImage(
				ImporterWidget.class.getResource("/resources/aurouslogo.png")));
		this.importerWidget.setTitle("Playlist Importer");
		this.importerWidget.getContentPane().setBackground(
				new Color(32, 33, 35));
		this.importerWidget.setBounds(100, 100, 379, 372);
		this.importerWidget
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.importerWidget.getContentPane().setLayout(null);
		this.importerWidget
				.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(
							final java.awt.event.WindowEvent windowEvent) {
						final int confirm = JOptionPane
								.showOptionDialog(
										ImporterWidget.this.importerWidget,
										"Are You Sure You Want to Close this Importer?",
										"Exit Confirmation",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE, null,
										null, null);
						if (confirm == 0) {
							UISession.setImporterWidget(null);
							ImporterWidget.this.importerWidget.dispose();
						}

					}
				});

		final JPanel servicePanel = new JPanel();
		servicePanel.setBackground(Color.DARK_GRAY);
		servicePanel.setBounds(0, 67, 373, 65);
		this.importerWidget.getContentPane().add(servicePanel);
		servicePanel.setLayout(null);

		this.importProgressBar = new JProgressBar();
		this.importProgressBar.setVisible(false);
		this.importProgressBar.setStringPainted(true);
		this.importProgressBar.setBounds(0, 180, 373, 37);
		this.importerWidget.getContentPane().add(this.importProgressBar);
		final JLabel playListURLLabel = new JLabel("");
		playListURLLabel.setForeground(Color.WHITE);
		playListURLLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		playListURLLabel.setBounds(223, 311, 150, 14);
		this.importerWidget.getContentPane().add(playListURLLabel);

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

		youTubeServiceButton.setBounds(145, 11, 35, 35);

		servicePanel.add(youTubeServiceButton);

		this.redditServiceButton = new JButton("");
		this.redditServiceButton.addActionListener(e -> {
			ImporterWidget.this.playListURL = PlayListUtils
					.importPlayListPrompt();
			playListURLLabel.setText(ImporterWidget.this.playListURL);

		});
		this.redditServiceButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.redditServiceButton.setIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/w_reddit.png")));
		this.redditServiceButton.setRolloverIcon(new ImageIcon(
				ImporterWidget.class.getResource("/resources/w_reddit_h.png")));
		this.redditServiceButton.setMargin(new Insets(0, 0, 0, 0));
		this.redditServiceButton.setContentAreaFilled(false);
		this.redditServiceButton.setBorderPainted(false);
		this.redditServiceButton.setBorder(null);
		this.redditServiceButton.setBounds(190, 11, 35, 35);
		servicePanel.add(this.redditServiceButton);

		this.hateChanServiceButton = new JButton("");
		this.hateChanServiceButton.setIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/8chan.png")));
		this.hateChanServiceButton.setRolloverIcon(new ImageIcon(
				ImporterWidget.class.getResource("/resources/8chan_h.png")));
		this.hateChanServiceButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.hateChanServiceButton.setMargin(new Insets(0, 0, 0, 0));
		this.hateChanServiceButton.setContentAreaFilled(false);
		this.hateChanServiceButton.setBorderPainted(false);
		this.hateChanServiceButton.setBorder(null);
		this.hateChanServiceButton.setBounds(235, 11, 35, 35);
		this.hateChanServiceButton.addActionListener(e -> {
			ImporterWidget.this.playListURL = PlayListUtils
					.importPlayListPrompt();
			playListURLLabel.setText(ImporterWidget.this.playListURL);

		});
		servicePanel.add(this.hateChanServiceButton);

		final JButton aurousServiceButton = new JButton("");
		aurousServiceButton.addActionListener(arg0 -> {
			ImporterWidget.this.playListURL = PlayListUtils
					.importPlayListPrompt();
			playListURLLabel.setText(ImporterWidget.this.playListURL);
		});
		aurousServiceButton.setIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/aurousimporticon.png")));
		aurousServiceButton.setRolloverIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/aurousimporticon_h.png")));
		aurousServiceButton.setMargin(new Insets(0, 0, 0, 0));
		aurousServiceButton.setContentAreaFilled(false);
		aurousServiceButton.setBorderPainted(false);
		aurousServiceButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		aurousServiceButton.setOpaque(false);
		aurousServiceButton.setBorder(null);
		aurousServiceButton.setBounds(100, 11, 35, 35);
		servicePanel.add(aurousServiceButton);

		final JLabel lblSelectAService = new JLabel("Select a Service");
		lblSelectAService.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblSelectAService.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectAService.setForeground(Color.WHITE);
		lblSelectAService.setBounds(54, 11, 251, 50);
		this.importerWidget.getContentPane().add(lblSelectAService);

		this.playListNameField = new JTextField();
		this.playListNameField.setBorder(UIManager
				.getBorder("TextField.border"));
		this.playListNameField.setHorizontalAlignment(SwingConstants.CENTER);
		this.playListNameField.setFont(new Font("Segoe UI", Font.PLAIN, 23));
		this.playListNameField.setBounds(0, 180, 373, 37);
		this.importerWidget.getContentPane().add(this.playListNameField);
		final GhostText ghostText = new GhostText("FMA OST",
				this.playListNameField);
		ghostText.setHorizontalAlignment(SwingConstants.CENTER);
		ghostText.setHorizontalTextPosition(SwingConstants.CENTER);
		this.playListNameField.setColumns(10);

		this.lblEnterAPlaylist = new JLabel("Enter a Playlist Name");
		this.lblEnterAPlaylist.setHorizontalAlignment(SwingConstants.CENTER);
		this.lblEnterAPlaylist.setForeground(Color.WHITE);
		this.lblEnterAPlaylist.setFont(new Font("Calibri", Font.PLAIN, 20));
		this.lblEnterAPlaylist.setBounds(54, 133, 251, 50);
		this.importerWidget.getContentPane().add(this.lblEnterAPlaylist);

		this.importPlayListButton = new JButton("");
		this.importPlayListButton.addActionListener(e -> {
			if (!ImporterWidget.this.playListURL.trim().isEmpty()
					&& !ImporterWidget.this.playListNameField.getText().trim()
							.isEmpty()) {

				this.importProgressBar.setVisible(true);

				PlayListUtils.getImportRules(this.playListURL,
						this.playListNameField.getText());

			} else {
				JOptionPane.showMessageDialog(this.importerWidget,
						"No playlist url or name detected", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		});
		this.importPlayListButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.importPlayListButton.setBorderPainted(false);
		this.importPlayListButton.setBorder(null);
		this.importPlayListButton.setMargin(new Insets(0, 0, 0, 0));
		this.importPlayListButton.setContentAreaFilled(false);
		this.importPlayListButton.setIcon(new ImageIcon(ImporterWidget.class
				.getResource("/resources/ic_export_import.png")));
		this.importPlayListButton.setBounds(149, 261, 64, 64);
		this.importerWidget.getContentPane().add(this.importPlayListButton);

		this.importInstrucLabel = new JLabel("Import Playlist");
		this.importInstrucLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		this.importInstrucLabel.setForeground(Color.WHITE);
		this.importInstrucLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.importInstrucLabel.setBounds(94, 228, 181, 22);
		this.importerWidget.getContentPane().add(this.importInstrucLabel);
		this.importerWidget.setLocationRelativeTo(UISession.getPresenter()
				.getAurousFrame());

	}

	public boolean isOpen() {
		return Utils.isNull(this.importerWidget) ? false : this.importerWidget
				.isVisible();
	}
}
