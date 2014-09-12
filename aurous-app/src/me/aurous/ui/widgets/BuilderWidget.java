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
				e.printStackTrace();
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

		builderWidget = new JFrame();
		builderWidget.getContentPane().setBackground(new Color(32, 33, 35));
		builderWidget.setIconImage(Toolkit.getDefaultToolkit().getImage(
				BuilderWidget.class.getResource("/resources/aurouslogo.png")));
		builderWidget.setType(Type.UTILITY);

		builderWidget.setResizable(false);
		builderWidget.setTitle("Playlist Builder");
		builderWidget.getContentPane().setLayout(null);
		builderWidget.setPreferredSize(new Dimension(400, 545));
		builderWidget
		.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		loadingIcon = new JLabel("");

		loadingIcon.setBackground(Color.WHITE);
		loadingIcon.setHorizontalAlignment(SwingConstants.CENTER);
		loadingIcon.setIcon(new ImageIcon(BuilderWidget.class
				.getResource("/resources/loading1.gif")));
		loadingIcon.setBounds(0, 0, 394, 516);
		loadingIcon.setVisible(false);

		builderWidget.getContentPane().add(loadingIcon);
		final JScrollPane builderScrollPane = new JScrollPane();
		builderScrollPane.setPreferredSize(new Dimension(400, 530));
		builderScrollPane.setBounds(0, 84, 394, 339);
		builderWidget.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {
				final int confirm = JOptionPane.showOptionDialog(builderWidget,
						"Are You Sure You Want to Close this Builder?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					UISession.setBuilderWidget(null);
					builderWidget.dispose();
				}

			}
		});
		playListTextArea = new JTextArea();
		playListTextArea.setBackground(Color.DARK_GRAY);
		playListTextArea.setForeground(Color.WHITE);
		playListTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		playListTextArea.addMouseListener(new ContextMenuMouseListener());
		playListTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {

			}
		});
		playListTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final int c = e.getKeyCode();
				if (c == KeyEvent.VK_PASTE) {

				}
			}
		});
		lines = new JTextArea("1");
		lines.setForeground(Color.WHITE);

		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);

		playListTextArea.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void changedUpdate(final DocumentEvent de) {
						lines.setText(getText());
					}

					public String getText() {
						final int caretPosition = playListTextArea
								.getDocument().getLength();
						final Element root = playListTextArea.getDocument()
								.getDefaultRootElement();
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
						SwingUtilities.invokeLater(() -> {
							if (UISession.getBuilderWidget().newLining == false) {
								playListTextArea.append(System
										.getProperty("line.separator"));
								lines.setText(getText());
								UISession.getBuilderWidget().newLining = true;
							}
						});

					}

					@Override
					public void removeUpdate(final DocumentEvent de) {
						lines.setText(getText());

					}

				});
		builderWidget.getContentPane().setLayout(null);

		builderScrollPane.setViewportView(playListTextArea);
		builderScrollPane.setRowHeaderView(lines);
		builderScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		builderWidget.getContentPane().add(builderScrollPane);

		final JLabel instructionsLabel = new JLabel(
				"<html><body>Paste the links of the songs you want to import<br>Make sure each one is on a new line like so: <br>https://www.youtube.com/watch?v=lqY4jkWCmKY<br>https://www.youtube.com/watch?v=hH9Y9SPZYTI</br></body></html>");
		instructionsLabel.setForeground(Color.WHITE);
		instructionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
		instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionsLabel.setBounds(57, 0, 285, 82);
		builderWidget.getContentPane().add(instructionsLabel);

		final JLabel enterPlayListNameLabel = new JLabel("Enter Playlist Name:");
		enterPlayListNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		enterPlayListNameLabel.setForeground(Color.WHITE);
		enterPlayListNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		enterPlayListNameLabel.setBounds(24, 423, 343, 23);
		builderWidget.getContentPane().add(enterPlayListNameLabel);

		playListNameTextField = new JTextField();
		playListNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		playListNameTextField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		playListNameTextField.setColumns(10);
		playListNameTextField.setBounds(0, 449, 394, 33);
		builderWidget.getContentPane().add(playListNameTextField);

		buildListButton = new JButton("build");
		buildListButton.setForeground(Color.WHITE);
		buildListButton.setBackground(Color.DARK_GRAY);
		buildListButton
				.addActionListener(e -> {
					if (playListTextArea.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(builderWidget,
								"You must add links to build a playlist",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					} else if (playListNameTextField.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(builderWidget,
								"Please enter a name for your playlist",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					loadingIcon.setVisible(true);
					playListTextArea.setEditable(false);
					buildListButton.setEnabled(false);
					playListNameTextField.setEditable(false);
					final String items = playListTextArea.getText();

					PlayListUtils.buildPlayList(items,
							playListNameTextField.getText());

				});
		buildListButton.setBounds(147, 493, 89, 23);

		builderWidget.getContentPane().add(buildListButton);

		builderWidget.pack();

		builderWidget.setVisible(true);
		final GhostText ghostText = new GhostText("FMA OST",
				playListNameTextField);
		ghostText.setHorizontalAlignment(SwingConstants.CENTER);
		ghostText.setHorizontalTextPosition(SwingConstants.CENTER);
		builderWidget.setLocationRelativeTo(UISession.getPresenter()
				.getAurousFrame());
	}

	public JButton getBuildListButton() {
		return buildListButton;
	}

	public JLabel getLoadingIcon() {
		return loadingIcon;
	}

	public JTextField getPlayListNameTextField() {
		return playListNameTextField;
	}

	public JTextArea getPlayListTextArea() {
		return playListTextArea;
	}

	public JFrame getWidget() {
		return builderWidget;
	}

	public boolean isOpen() {
		return builderWidget == null ? false : builderWidget.isVisible();
	}
}