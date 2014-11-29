package me.aurous.ui.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import me.aurous.utils.Utils;

public class ExceptionWidget extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 3205213418287389373L;
	private final JTextArea exceptionText;

	/**
	 * Create the frame.
	 */
	public ExceptionWidget(final String exception) {
		setAlwaysOnTop(true);
		setResizable(false);
		getContentPane().setBackground(new Color(32, 33, 35));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 433, 434);
		getContentPane().setLayout(null);

		final JLabel warningIcon = new JLabel("");
		warningIcon.setIcon(new ImageIcon(ExceptionWidget.class
				.getResource("/resources/warning-icon.png")));
		warningIcon.setBounds(10, 11, 64, 78);
		getContentPane().add(warningIcon);

		exceptionText = new JTextArea();
		exceptionText.setForeground(Color.WHITE);
		exceptionText.setBackground(Color.DARK_GRAY);
		exceptionText.setText(exception);
		exceptionText.setEditable(false);
		exceptionText.setFont(new Font("Source Code Pro", Font.PLAIN, 11));
		exceptionText.setBounds(10, 90, 407, 276);
		getContentPane().add(exceptionText);
		exceptionText.setColumns(50);

		final JButton githubButton = new JButton("Report");
		githubButton.addActionListener(arg0 -> {
			try {
				Utils.openURL(new URL(
						"https://github.com/Codeusa/aurous-app/issues/new"));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
		githubButton.setBounds(126, 377, 89, 23);
		getContentPane().add(githubButton);

		final JButton copyButton = new JButton("Copy");
		copyButton.addActionListener(e -> {

			final StringSelection stringSelection = new StringSelection(
					exceptionText.getText());
			final Clipboard clpbrd = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
		});
		copyButton.setBounds(225, 377, 89, 23);
		getContentPane().add(copyButton);

		final JLabel warningMessage = new JLabel(
				"<html><strong>Uh oh!</strong> Looks like Aurous ran into a slight issue. Please copy the below text and report it on the Github! This can help us prevent it ever again.</html>");
		warningMessage.setForeground(Color.WHITE);
		warningMessage.setFont(new Font("Consolas", Font.PLAIN, 13));
		warningMessage.setBounds(84, 11, 323, 78);
		getContentPane().add(warningMessage);
	}
}
