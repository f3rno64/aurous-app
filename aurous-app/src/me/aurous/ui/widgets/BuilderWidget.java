package me.aurous.ui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import me.aurous.swinghacks.GhostText;
import me.aurous.ui.UISession;
import me.aurous.ui.listeners.ContextMenuMouseListener;
import me.aurous.utils.Utils;
import me.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class BuilderWidget {
	public static void openBuilder() {
		if ((UISession.getBuilderWidget() != null)
				&& UISession.getBuilderWidget().isOpen()) {
			UISession.getBuilderWidget().getWidget().toFront();
			UISession.getBuilderWidget().getWidget().repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final BuilderWidget window = new BuilderWidget();
				UISession.setBuilderWidget(window);
				UISession.getBuilderWidget().getWidget().setVisible(true);

			} catch (final Exception e) {
				final ExceptionWidget eWidget = new ExceptionWidget(Utils
						.getStackTraceString(e, ""));
				eWidget.setVisible(true);
			}
		});
	}

	private JTextArea playListTextArea;
	private JTextArea lines;

	private JFrame builderWidget;

	private boolean newLining = false;

	private JButton buildListButton;

	private JTextField playListNameTextField;

	private JLabel loadingIcon;

	/**
	 * @wbp.parser.entryPoint
	 */
	public BuilderWidget() {

		this.builderWidget = new JFrame();
		this.builderWidget.getContentPane()
				.setBackground(new Color(32, 33, 35));
		this.builderWidget.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BuilderWidget.class.getResource("/resources/aurouslogo.png")));
		this.builderWidget.setType(Type.UTILITY);

		this.builderWidget.setResizable(false);
		this.builderWidget.setTitle("Playlist Builder");
		this.builderWidget.getContentPane().setLayout(null);
		this.builderWidget.setPreferredSize(new Dimension(400, 545));
		this.builderWidget
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		this.loadingIcon = new JLabel("");
		this.loadingIcon.setForeground(Color.CYAN);

		this.loadingIcon.setBackground(Color.DARK_GRAY);
		this.loadingIcon.setHorizontalAlignment(SwingConstants.CENTER);
		this.loadingIcon.setIcon(new ImageIcon(BuilderWidget.class
				.getResource("/resources/loading1.gif")));
		this.loadingIcon.setBounds(0, 0, 394, 516);
		this.loadingIcon.setVisible(false);

		this.builderWidget.getContentPane().add(this.loadingIcon);
		final JScrollPane builderScrollPane = new JScrollPane();
		builderScrollPane.setPreferredSize(new Dimension(400, 530));
		builderScrollPane.setBounds(0, 84, 394, 339);
		this.builderWidget
				.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(
							final java.awt.event.WindowEvent windowEvent) {
						final int confirm = JOptionPane.showOptionDialog(
								BuilderWidget.this.builderWidget,
								"Are You Sure You Want to Close this Builder?",
								"Exit Confirmation", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, null, null);
						if (confirm == 0) {
							UISession.setBuilderWidget(null);
							BuilderWidget.this.builderWidget.dispose();
						}

					}
				});
		this.playListTextArea = new JTextArea();
		this.playListTextArea.setBackground(Color.DARK_GRAY);
		this.playListTextArea.setForeground(Color.WHITE);
		this.playListTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		this.playListTextArea.addMouseListener(new ContextMenuMouseListener());
		this.playListTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {

			}
		});
		this.playListTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final int c = e.getKeyCode();
				if (c == KeyEvent.VK_PASTE) {

				}
			}
		});
		this.lines = new JTextArea("1");
		this.lines.setForeground(Color.WHITE);

		this.lines.setBackground(Color.LIGHT_GRAY);
		this.lines.setEditable(false);

		this.playListTextArea.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void changedUpdate(final DocumentEvent de) {
						BuilderWidget.this.lines.setText(getText());
					}

					public String getText() {
						final int caretPosition = BuilderWidget.this.playListTextArea
								.getDocument().getLength();
						final Element root = BuilderWidget.this.playListTextArea
								.getDocument().getDefaultRootElement();
						String text = "1"
								+ System.getProperty("line.separator");
						for (int i = 2; i < (root
								.getElementIndex(caretPosition) + 2); i++) {
							text += i + System.getProperty("line.separator");
						}
						return text;
					}

					@Override
					public void insertUpdate(final DocumentEvent de) {
						UISession.getBuilderWidget().newLining = false;
						SwingUtilities
								.invokeLater(() -> {
									if (UISession.getBuilderWidget().newLining == false) {
										BuilderWidget.this.playListTextArea.append(System
												.getProperty("line.separator"));
										BuilderWidget.this.lines
												.setText(getText());
										UISession.getBuilderWidget().newLining = true;
									}
								});

					}

					@Override
					public void removeUpdate(final DocumentEvent de) {
						BuilderWidget.this.lines.setText(getText());

					}

				});
		this.builderWidget.getContentPane().setLayout(null);

		builderScrollPane.setViewportView(this.playListTextArea);
		builderScrollPane.setRowHeaderView(this.lines);
		builderScrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		this.builderWidget.getContentPane().add(builderScrollPane);

		final JLabel instructionsLabel = new JLabel(
				"<html><body>Paste the links of the songs you want to import<br>Make sure each one is on a new line like so: <br>https://www.youtube.com/watch?v=lqY4jkWCmKY<br>https://www.youtube.com/watch?v=hH9Y9SPZYTI</br></body></html>");
		instructionsLabel.setForeground(Color.WHITE);
		instructionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
		instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionsLabel.setBounds(57, 0, 285, 82);
		this.builderWidget.getContentPane().add(instructionsLabel);

		final JLabel enterPlayListNameLabel = new JLabel("Enter Playlist Name:");
		enterPlayListNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		enterPlayListNameLabel.setForeground(Color.WHITE);
		enterPlayListNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		enterPlayListNameLabel.setBounds(24, 423, 343, 23);
		this.builderWidget.getContentPane().add(enterPlayListNameLabel);

		this.playListNameTextField = new JTextField();
		this.playListNameTextField
				.setHorizontalAlignment(SwingConstants.CENTER);
		this.playListNameTextField
				.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		this.playListNameTextField.setColumns(10);
		this.playListNameTextField.setBounds(0, 449, 394, 33);
		this.builderWidget.getContentPane().add(this.playListNameTextField);

		this.buildListButton = new JButton("build");
		this.buildListButton.setForeground(Color.WHITE);
		this.buildListButton.setBackground(Color.DARK_GRAY);
		this.buildListButton.addActionListener(e -> {
			if (this.playListTextArea.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this.builderWidget,
						"You must add links to build a playlist", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else if (this.playListNameTextField.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this.builderWidget,
						"Please enter a name for your playlist", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.loadingIcon.setVisible(true);
			this.playListTextArea.setEditable(false);
			this.buildListButton.setEnabled(false);
			this.playListNameTextField.setEditable(false);
			final String items = this.playListTextArea.getText();

			PlayListUtils.buildPlayList(items,
					this.playListNameTextField.getText());

		});
		this.buildListButton.setBounds(147, 493, 89, 23);

		this.builderWidget.getContentPane().add(this.buildListButton);

		this.builderWidget.pack();

		this.builderWidget.setVisible(true);
		final GhostText ghostText = new GhostText("FMA OST",
				this.playListNameTextField);
		ghostText.setHorizontalAlignment(SwingConstants.CENTER);
		ghostText.setHorizontalTextPosition(SwingConstants.CENTER);
		this.builderWidget.setLocationRelativeTo(UISession.getPresenter()
				.getAurousFrame());
	}

	public JButton getBuildListButton() {
		return this.buildListButton;
	}

	public JLabel getLoadingIcon() {
		return this.loadingIcon;
	}

	public JTextField getPlayListNameTextField() {
		return this.playListNameTextField;
	}

	public JTextArea getPlayListTextArea() {
		return this.playListTextArea;
	}

	public JFrame getWidget() {
		return this.builderWidget;
	}

	public boolean isOpen() {
		return Utils.isNull(this.builderWidget) ? false : this.builderWidget
				.isVisible();
	}
}