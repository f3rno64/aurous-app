package me.aurous.ui.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import me.aurous.ui.UISession;
import me.aurous.utils.Utils;

/**
 * @author Andrew
 *
 */
public class AboutWidget {

	/**
	 * Launch the application.
	 */
	public static void showAbout() {
		if ((UISession.getAboutWidget() != null)
				&& UISession.getAboutWidget().isOpen()) {
			UISession.getAboutWidget().getWidget().toFront();
			UISession.getAboutWidget().getWidget().repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final AboutWidget window = new AboutWidget();
				UISession.setAboutWidget(window);
				UISession.getAboutWidget().getWidget().setVisible(true);
			} catch (final Exception e) {
				final ExceptionWidget eWidget = new ExceptionWidget(Utils
						.getStackTraceString(e, ""));
				eWidget.setVisible(true);
			}
		});
	}

	private JFrame aboutWidget;

	private JLabel titleLabel;

	private JLabel copyRightLabel;
	private JLabel aboutLabel;
	private JSeparator separator;
	private JLabel socialLabel;
	private JSeparator separator_1;
	private JButton blogButton;
	private JButton twitterButton;
	private JButton youTubeButton;
	private JButton faceBookButton;
	private JButton donateButton;
	private JButton fokMeButton;
	private JLabel andrewsAvatar;

	/**
	 * Create the application.
	 */
	public AboutWidget() {
		initialize();
	}

	public JFrame getWidget() {
		return this.aboutWidget;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.aboutWidget = new JFrame();
		this.aboutWidget.setIconImage(Toolkit.getDefaultToolkit().getImage(
				AboutWidget.class.getResource("/resources/aurouslogo.png")));
		this.aboutWidget.setType(Type.UTILITY);
		this.aboutWidget.setTitle("About");
		this.aboutWidget.setResizable(false);
		this.aboutWidget.setBounds(100, 100, 370, 400);
		this.aboutWidget
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.aboutWidget.getContentPane().setLayout(null);
		this.aboutWidget.getContentPane().setBackground(new Color(32, 33, 35));
		this.aboutWidget.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {

				UISession.setAboutWidget(null);
				AboutWidget.this.aboutWidget.dispose();

			}
		});

		this.titleLabel = new JLabel("Aurous 2.8");
		this.titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.titleLabel.setForeground(Color.WHITE);
		this.titleLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		this.titleLabel.setBounds(90, 11, 202, 27);
		this.aboutWidget.getContentPane().add(this.titleLabel);

		this.copyRightLabel = new JLabel(
				"Copyright \u00A9 2014, Codeusa Software");
		this.copyRightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.copyRightLabel.setForeground(Color.WHITE);
		this.copyRightLabel.setFont(new Font("Calibri", Font.PLAIN, 11));
		this.copyRightLabel.setBounds(58, 49, 247, 14);
		this.aboutWidget.getContentPane().add(this.copyRightLabel);

		this.aboutLabel = new JLabel(
				"<html><center>Aurous is maintained by one mentally unstable guy with way too much ambition and a habit of premature optimization.<br>If you're interested in helping out check out the links below. </center></html>");
		this.aboutLabel.setForeground(Color.WHITE);
		this.aboutLabel.setFont(new Font("Calibri", Font.PLAIN, 13));
		this.aboutLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.aboutLabel.setBounds(10, 74, 344, 100);
		this.aboutWidget.getContentPane().add(this.aboutLabel);

		this.separator = new JSeparator();
		this.separator.setBounds(10, 186, 344, 2);
		this.aboutWidget.getContentPane().add(this.separator);

		this.socialLabel = new JLabel("Social");
		this.socialLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.socialLabel.setForeground(Color.WHITE);
		this.socialLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
		this.socialLabel.setBounds(90, 245, 177, 27);
		this.aboutWidget.getContentPane().add(this.socialLabel);

		this.separator_1 = new JSeparator();
		this.separator_1.setBounds(10, 283, 344, 2);
		this.aboutWidget.getContentPane().add(this.separator_1);

		this.blogButton = new JButton();
		this.blogButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://blog.aurous.me/"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		this.blogButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.blogButton.setBorderPainted(false);
		this.blogButton.setBorder(null);
		this.blogButton.setMargin(new Insets(0, 0, 0, 0));
		this.blogButton.setContentAreaFilled(false);
		this.blogButton.setIcon(new ImageIcon(AboutWidget.class
				.getResource("/resources/tumblr.png")));
		this.blogButton.setBounds(20, 296, 64, 64);
		this.aboutWidget.getContentPane().add(this.blogButton);

		this.twitterButton = new JButton();
		this.twitterButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://twitter.com/codeusasoftware"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		this.twitterButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.twitterButton.setIcon(new ImageIcon(AboutWidget.class
				.getResource("/resources/twitter.png")));
		this.twitterButton.setMargin(new Insets(0, 0, 0, 0));
		this.twitterButton.setContentAreaFilled(false);
		this.twitterButton.setBorderPainted(false);
		this.twitterButton.setBorder(null);
		this.twitterButton.setBounds(108, 296, 64, 64);
		this.aboutWidget.getContentPane().add(this.twitterButton);

		this.youTubeButton = new JButton();
		this.youTubeButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://youtube.com/codeusasoftware"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		this.youTubeButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.youTubeButton.setIcon(new ImageIcon(AboutWidget.class
				.getResource("/resources/youtube.png")));
		this.youTubeButton.setMargin(new Insets(0, 0, 0, 0));
		this.youTubeButton.setContentAreaFilled(false);
		this.youTubeButton.setBorderPainted(false);
		this.youTubeButton.setBorder(null);
		this.youTubeButton.setBounds(195, 296, 64, 64);
		this.aboutWidget.getContentPane().add(this.youTubeButton);

		this.faceBookButton = new JButton();
		this.faceBookButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://facebook.com/codeusasoftware"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		this.faceBookButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.faceBookButton.setIcon(new ImageIcon(AboutWidget.class
				.getResource("/resources/facebook.png")));
		this.faceBookButton.setMargin(new Insets(0, 0, 0, 0));
		this.faceBookButton.setContentAreaFilled(false);
		this.faceBookButton.setBorderPainted(false);
		this.faceBookButton.setBorder(null);
		this.faceBookButton.setBounds(279, 296, 64, 64);
		this.aboutWidget.getContentPane().add(this.faceBookButton);

		this.donateButton = new JButton();
		this.donateButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://blog.aurous.me/donate"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		this.donateButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.donateButton.setIcon(new ImageIcon(AboutWidget.class
				.getResource("/resources/paypal.png")));
		this.donateButton.setMargin(new Insets(0, 0, 0, 0));
		this.donateButton.setContentAreaFilled(false);
		this.donateButton.setBorderPainted(false);
		this.donateButton.setFocusPainted(false);
		this.donateButton.setBorder(BorderFactory.createEmptyBorder());
		this.donateButton.setBounds(10, 199, 166, 33);

		this.aboutWidget.getContentPane().add(this.donateButton);

		this.fokMeButton = new JButton();
		this.fokMeButton
		.addActionListener(e -> {
			try {
				Utils.openURL(new URL(
						"https://github.com/Codeusa/aurous-app"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		this.fokMeButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.fokMeButton.setIcon(new ImageIcon(AboutWidget.class
				.getResource("/resources/btn_github_lg.png")));
		this.fokMeButton.setMargin(new Insets(0, 0, 0, 0));
		this.fokMeButton.setContentAreaFilled(false);
		this.fokMeButton.setBorderPainted(false);
		this.fokMeButton.setBorder(null);
		this.fokMeButton.setBounds(195, 199, 159, 33);
		this.aboutWidget.getContentPane().add(this.fokMeButton);

		this.andrewsAvatar = new JLabel("");
		this.andrewsAvatar.setIcon(new ImageIcon(AboutWidget.class
				.getResource("/resources/andrew.jpg")));
		this.andrewsAvatar.setBounds(0, 0, 80, 63);
		this.aboutWidget.getContentPane().add(this.andrewsAvatar);
		this.aboutWidget.setLocationRelativeTo(UISession.getPresenter()
				.getAurousFrame());
	}

	public boolean isOpen() {
		return Utils.isNull(this.aboutWidget) ? false : this.aboutWidget
				.isVisible();
	}

	public void setWidget(final JFrame aboutWidget) {
		this.aboutWidget = aboutWidget;
	}
}
