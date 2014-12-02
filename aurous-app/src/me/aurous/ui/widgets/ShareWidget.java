package me.aurous.ui.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import me.aurous.utils.Utils;

public class ShareWidget extends JFrame {

	private final JPanel contentPane;
	private final JTextField shareLinkArea;
	private final String message;
	private final String TWITTER_INTENT = "https://twitter.com/intent/tweet?";
	private final String TWITTER_MESSAGE = "I just shared a playlist on Aurous.me";
	private final String HASH_TAGS = "aurous, music";
	private final String ENCODING = "UTF-8";

	/**
	 * Create the frame.
	 */
	public ShareWidget(final String message) {
		this.message = message;
		setResizable(false);
		setTitle("Share Playlist");
		setAlwaysOnTop(true);
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 151);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(35, 35, 35));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		shareLinkArea = new JTextField();
		shareLinkArea.setForeground(Color.WHITE);
		shareLinkArea.setBackground(Color.DARK_GRAY);
		shareLinkArea.setHorizontalAlignment(SwingConstants.CENTER);
		shareLinkArea.setFont(new Font("Open Sans", Font.PLAIN, 18));
		shareLinkArea.setText(this.message);
		shareLinkArea.setBounds(22, 32, 404, 44);
		contentPane.add(shareLinkArea);
		shareLinkArea.setColumns(10);

		final JButton copyButton = new JButton("Copy");
		copyButton.addActionListener(arg0 -> {
			final StringSelection stringSelection = new StringSelection(
					shareLinkArea.getText());
			final Clipboard clpbrd = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
		});
		copyButton.setBounds(22, 87, 203, 23);
		contentPane.add(copyButton);

		final JButton tweetButton = new JButton("Tweet It");
		tweetButton.addActionListener(arg0 -> {
			final String playlistURL = shareLinkArea.getText();
			String tweet = null;
			try {

				tweet = String.format("%stext=%s&url=%s&hashtags=%s",
						TWITTER_INTENT,
						URLEncoder.encode(TWITTER_MESSAGE, ENCODING),
						URLEncoder.encode(playlistURL, ENCODING),
						URLEncoder.encode(HASH_TAGS, ENCODING));
				Utils.openURL(new URL(tweet));
			} catch (final Exception e) {
				final ExceptionWidget eWidget = new ExceptionWidget(Utils
						.getStackTraceString(e, ""));
				eWidget.setVisible(true);
			}
		});
		tweetButton.setBounds(233, 87, 193, 23);
		contentPane.add(tweetButton);

		final JLabel shareLabel = new JLabel("Share Link:");
		shareLabel.setForeground(Color.WHITE);
		shareLabel.setFont(new Font("Open Sans", Font.BOLD, 16));
		shareLabel.setHorizontalAlignment(SwingConstants.CENTER);
		shareLabel.setBounds(20, 7, 404, 14);
		contentPane.add(shareLabel);

	}
}
