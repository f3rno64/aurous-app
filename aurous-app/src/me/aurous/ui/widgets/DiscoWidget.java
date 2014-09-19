package me.aurous.ui.widgets;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window.Type;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import me.aurous.swinghacks.GhostText;
import me.aurous.ui.UISession;
import me.aurous.utils.playlist.YouTubeDiscoUtils;

/**
 * @author Andrew
 *
 */
public class DiscoWidget {

	/**
	 * Launch the application.
	 */
	public static void openDisco() {
		if ((UISession.getDiscoWidget() != null)
				&& UISession.getDiscoWidget().isOpen()) {
			UISession.getDiscoWidget().getWidget().toFront();
			UISession.getDiscoWidget().getWidget().repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final DiscoWidget window = new DiscoWidget();
				UISession.setDiscoWidget(window);
				UISession.getDiscoWidget().getWidget().setVisible(true);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	public JFrame discoWidget;

	public JTextField queryField;
	public JProgressBar discoProgressBar;
	public JButton discoBuildButton;
	public JButton top100Button;

	/**
	 * Create the application.
	 */
	public DiscoWidget() {
		initialize();
	}

	public JButton getDiscoBuildButton() {
		return discoBuildButton;
	}

	public JProgressBar getDiscoProgressBar() {
		return discoProgressBar;
	}

	public JTextField getQueryField() {
		return queryField;
	}

	public JButton getTop100Button() {
		return top100Button;
	}

	public JFrame getWidget() {
		return discoWidget;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		discoWidget = new JFrame();
		discoWidget.setTitle("Disco Mixer");
		discoWidget.setIconImage(Toolkit.getDefaultToolkit().getImage(
				DiscoWidget.class.getResource("/resources/aurouslogo.png")));
		discoWidget.setType(Type.UTILITY);
		discoWidget.setResizable(false);
		discoWidget.setBounds(100, 100, 606, 239);
		discoWidget
		.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		discoWidget.getContentPane().setLayout(null);
		discoWidget.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {
				final int confirm = JOptionPane.showOptionDialog(discoWidget,
						"Are You Sure You Want to Close Disco Mixer?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					UISession.setDiscoWidget(null);
					discoWidget.dispose();
				}

			}
		});

		final JLabel logoLabel = new JLabel("");
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logoLabel.setIcon(new ImageIcon(DiscoWidget.class
				.getResource("/resources/fmw.png")));
		logoLabel.setBounds(10, 0, 580, 70);
		discoWidget.getContentPane().add(logoLabel);

		discoProgressBar = new JProgressBar();
		discoProgressBar.setStringPainted(true);
		discoProgressBar.setBounds(113, 119, 380, 49);
		discoProgressBar.setVisible(false);
		discoWidget.getContentPane().add(discoProgressBar);

		queryField = new JTextField();
		queryField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		queryField.setHorizontalAlignment(SwingConstants.CENTER);
		queryField.setBounds(113, 119, 380, 44);
		discoWidget.getContentPane().add(queryField);
		queryField.setColumns(10);

		final JLabel instructionsLabel = new JLabel(
				"Enter an Artist, Song or Choose from the Top 100!");
		instructionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionsLabel.setBounds(23, 81, 541, 27);
		discoWidget.getContentPane().add(instructionsLabel);

		discoBuildButton = new JButton("Disco!");
		discoBuildButton.addActionListener(e -> {
			if (!queryField.getText().trim().isEmpty()) {
				discoProgressBar.setVisible(true);
				YouTubeDiscoUtils.buildDiscoPlayList(queryField.getText());
			} else {
				JOptionPane.showMessageDialog(discoWidget,
						"Please enter search query", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		});
		discoBuildButton.setForeground(Color.BLACK);
		discoBuildButton.setBounds(197, 174, 100, 26);
		discoWidget.getContentPane().add(discoBuildButton);

		top100Button = new JButton("Top Hits!");
		top100Button.addActionListener(e -> {
			discoProgressBar.setVisible(true);
			YouTubeDiscoUtils.buildTopPlayList();
		});
		top100Button.setForeground(Color.BLACK);
		top100Button.setBounds(307, 174, 100, 26);
		discoWidget.getContentPane().add(top100Button);
		final GhostText ghostText = new GhostText("Ghost B.C.", queryField);
		ghostText.setHorizontalAlignment(SwingConstants.CENTER);
		discoWidget.setLocationRelativeTo(UISession.getPresenter()
				.getAurousFrame());
	}

	public boolean isOpen() {
		return discoWidget == null ? false : discoWidget.isVisible();
	}
}
