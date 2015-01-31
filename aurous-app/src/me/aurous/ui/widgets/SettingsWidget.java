package me.aurous.ui.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window.Type;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import me.aurous.player.Settings;
import me.aurous.ui.UISession;
import me.aurous.utils.Utils;

/**
 * @author Andrew
 *
 */
public class SettingsWidget {

	/**
	 * Launch the application.
	 */
	public static void openSettings() {
		if ((UISession.getSettingsWidget() != null)
				&& UISession.getSettingsWidget().isOpen()) {
			UISession.getSettingsWidget().getSettingsWidget().toFront();
			UISession.getSettingsWidget().getSettingsWidget().repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final SettingsWidget window = new SettingsWidget();
				UISession.setSettingsWidget(window);
				UISession.getSettingsWidget().getSettingsWidget()
				.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JFrame settingsWidget;

	private JLabel avatarTitleLabel;

	private JLabel profileNameTitleLabel;
	private JLabel avatarLabel;
	private JLabel changeAvatarHypeLink;
	public JTextField userNameField;
	private JLabel viewProfileHyperLink;
	private JSeparator separator;
	private JLabel onlineServicesLabel;
	private JSeparator separator_1;
	private JLabel notficationsLabel;
	public JCheckBox showPlayBackAlertCheckbox;
	private JLabel lblWhenANew;
	private JSeparator separator_2;
	private JLabel playBackSettingsLabel;
	public JCheckBox lowQualityCheckBox;
	private JLabel lblHelpsIfYoure;
	private JButton saveSettingsButton;
	private JButton exitFrameButton;
	private JLabel lblIfYouWant;
	public JCheckBox savePlayBackCheckBox;
	private JLabel skypeLabel;
	private JCheckBox updateSkypeCheckbox;
	private final String[] engines = { "VK", "YouTube" };

	/**
	 * Create the application.
	 */
	public SettingsWidget() {
		initialize();
	}

	public JFrame getSettingsWidget() {
		return this.settingsWidget;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.settingsWidget = new JFrame();
		this.settingsWidget.setTitle("Settings");
		this.settingsWidget.setIconImage(Toolkit.getDefaultToolkit().getImage(
				SettingsWidget.class.getResource("/resources/aurouslogo.png")));
		this.settingsWidget.setResizable(false);
		this.settingsWidget.getContentPane().setBackground(
				new Color(49, 49, 49));
		this.settingsWidget.getContentPane().setLayout(null);
		this.settingsWidget
				.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(
							final java.awt.event.WindowEvent windowEvent) {
						UISession.setSettingsWidget(null);
						SettingsWidget.this.settingsWidget.dispose();

					}
				});
		this.avatarTitleLabel = new JLabel("Avatar");
		this.avatarTitleLabel.setForeground(Color.WHITE);
		this.avatarTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		this.avatarTitleLabel.setBounds(10, 11, 50, 14);
		this.settingsWidget.getContentPane().add(this.avatarTitleLabel);

		this.profileNameTitleLabel = new JLabel("Profile name");
		this.profileNameTitleLabel.setForeground(Color.WHITE);
		this.profileNameTitleLabel
				.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		this.profileNameTitleLabel.setBounds(70, 12, 83, 14);
		this.settingsWidget.getContentPane().add(this.profileNameTitleLabel);

		this.avatarLabel = new JLabel("");
		this.avatarLabel.setIcon(new ImageIcon(SettingsWidget.class
				.getResource("/resources/avatar.jpg")));
		this.avatarLabel.setBounds(10, 36, 37, 37);
		this.settingsWidget.getContentPane().add(this.avatarLabel);

		this.changeAvatarHypeLink = new JLabel(
				"<html><a href=\\\"\\\" style=\"color: #ffffff\">Change</a></html>");
		this.changeAvatarHypeLink.setForeground(Color.WHITE);
		this.changeAvatarHypeLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.changeAvatarHypeLink.setBounds(10, 82, 50, 24);
		this.changeAvatarHypeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.settingsWidget.getContentPane().add(this.changeAvatarHypeLink);

		this.userNameField = new JTextField();
		this.userNameField
				.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
		this.userNameField.setBounds(70, 37, 183, 24);
		this.settingsWidget.getContentPane().add(this.userNameField);
		this.userNameField.setColumns(10);

		this.viewProfileHyperLink = new JLabel(
				"<html><a href=\\\"\\\" style=\"color: #ffffff\">View my Aurous Community Profile</a></html>");
		this.viewProfileHyperLink.setForeground(Color.WHITE);
		this.viewProfileHyperLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.viewProfileHyperLink.setBounds(70, 82, 217, 24);
		this.viewProfileHyperLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.settingsWidget.getContentPane().add(this.viewProfileHyperLink);

		this.separator = new JSeparator();
		this.separator.setForeground(Color.LIGHT_GRAY);
		this.separator.setBounds(10, 117, 497, 2);
		this.settingsWidget.getContentPane().add(this.separator);

		this.onlineServicesLabel = new JLabel("Online Service Settings");
		this.onlineServicesLabel.setForeground(Color.WHITE);
		this.onlineServicesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		this.onlineServicesLabel.setBounds(10, 122, 155, 24);
		this.settingsWidget.getContentPane().add(this.onlineServicesLabel);

		this.separator_1 = new JSeparator();
		this.separator_1.setForeground(Color.LIGHT_GRAY);
		this.separator_1.setBounds(10, 184, 497, 2);
		this.settingsWidget.getContentPane().add(this.separator_1);

		this.notficationsLabel = new JLabel("Notfications");
		this.notficationsLabel.setForeground(Color.WHITE);
		this.notficationsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		this.notficationsLabel.setBounds(10, 194, 155, 24);
		this.settingsWidget.getContentPane().add(this.notficationsLabel);

		this.showPlayBackAlertCheckbox = new JCheckBox("Display Alert");
		this.showPlayBackAlertCheckbox.setFont(new Font("Segoe UI", Font.PLAIN,
				11));
		this.showPlayBackAlertCheckbox.setForeground(Color.WHITE);
		this.showPlayBackAlertCheckbox.setBackground(new Color(49, 49, 49));
		this.showPlayBackAlertCheckbox.setBounds(10, 241, 98, 23);
		this.settingsWidget.getContentPane()
				.add(this.showPlayBackAlertCheckbox);

		this.lblWhenANew = new JLabel("When a new song plays while minimized");
		this.lblWhenANew.setForeground(Color.WHITE);
		this.lblWhenANew.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.lblWhenANew.setBounds(10, 218, 222, 24);
		this.settingsWidget.getContentPane().add(this.lblWhenANew);

		this.playBackSettingsLabel = new JLabel("Playback Settings");
		this.playBackSettingsLabel.setForeground(Color.WHITE);
		this.playBackSettingsLabel
				.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		this.playBackSettingsLabel.setBounds(10, 303, 155, 24);
		this.settingsWidget.getContentPane().add(this.playBackSettingsLabel);

		this.lowQualityCheckBox = new JCheckBox("Stream Lower Bitrate");
		this.lowQualityCheckBox.setBackground(new Color(49, 49, 49));
		this.lowQualityCheckBox.setForeground(Color.WHITE);
		this.lowQualityCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		this.lowQualityCheckBox.setBounds(10, 346, 134, 23);
		this.settingsWidget.getContentPane().add(this.lowQualityCheckBox);

		this.lblHelpsIfYoure = new JLabel("If you're experiencing buffering.");
		this.lblHelpsIfYoure.setForeground(Color.WHITE);
		this.lblHelpsIfYoure.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.lblHelpsIfYoure.setBounds(10, 322, 209, 28);
		this.settingsWidget.getContentPane().add(this.lblHelpsIfYoure);

		final JComboBox<String> searchEngineCombo = new JComboBox<String>();
		searchEngineCombo.setBackground(Color.YELLOW);
		searchEngineCombo.setBounds(377, 148, 130, 25);
		for (final String option : this.engines) {
			searchEngineCombo.addItem(option);
		}
		searchEngineCombo.setSelectedItem(Settings.getSearchEngine());
		this.settingsWidget.getContentPane().add(searchEngineCombo);

		final JLabel engineLabel = new JLabel("Search Engine");
		engineLabel.setForeground(Color.WHITE);
		engineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		engineLabel.setBounds(377, 122, 140, 24);
		this.settingsWidget.getContentPane().add(engineLabel);

		this.saveSettingsButton = new JButton("APPLY");
		this.saveSettingsButton.addActionListener(e -> {
			Settings.setUserName(this.userNameField.getText().trim());
			Settings.setDisplayAlert(this.showPlayBackAlertCheckbox
					.isSelected());
			Settings.setStreamLowQuality(this.lowQualityCheckBox.isSelected());
			Settings.setSavePlayBack(this.savePlayBackCheckBox.isSelected());
			Settings.setSkypeUpdate(this.updateSkypeCheckbox.isSelected());
			Settings.setSearchEngine(searchEngineCombo.getSelectedItem()
					.toString());
			Settings.saveSettings(false);
		});
		this.saveSettingsButton.setForeground(Color.BLACK);
		this.saveSettingsButton.setBounds(323, 425, 89, 23);
		this.settingsWidget.getContentPane().add(this.saveSettingsButton);

		this.exitFrameButton = new JButton("CANCEL");
		this.exitFrameButton.addActionListener(e -> this.settingsWidget
				.dispose());
		this.exitFrameButton.setForeground(Color.BLACK);
		this.exitFrameButton.setBounds(422, 425, 89, 23);
		this.settingsWidget.getContentPane().add(this.exitFrameButton);

		this.separator_2 = new JSeparator();
		this.separator_2.setForeground(Color.LIGHT_GRAY);
		this.separator_2.setBounds(10, 292, 497, 8);
		this.settingsWidget.getContentPane().add(this.separator_2);
		this.settingsWidget.setType(Type.UTILITY);
		this.settingsWidget.setBounds(100, 100, 533, 484);
		this.settingsWidget
		.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.lblIfYouWant = new JLabel("If you want to show a song on twitch");
		this.lblIfYouWant.setForeground(Color.WHITE);
		this.lblIfYouWant.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.lblIfYouWant.setBounds(10, 376, 209, 28);
		this.settingsWidget.getContentPane().add(this.lblIfYouWant);

		this.savePlayBackCheckBox = new JCheckBox("Enable Streaming Features");
		this.savePlayBackCheckBox.setSelected(false);
		this.savePlayBackCheckBox.setForeground(Color.WHITE);
		this.savePlayBackCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		this.savePlayBackCheckBox.setBackground(new Color(49, 49, 49));
		this.savePlayBackCheckBox.setBounds(10, 400, 216, 23);
		this.settingsWidget.getContentPane().add(this.savePlayBackCheckBox);
		this.settingsWidget.setLocationRelativeTo(UISession.getPresenter()
				.getAurousFrame());

		this.showPlayBackAlertCheckbox.setSelected(Settings.isDisplayAlert());
		this.lowQualityCheckBox.setSelected(Settings.isStreamLowQuality());
		this.savePlayBackCheckBox.setSelected(Settings.isSavePlayBack());
		this.userNameField.setText(Settings.getUserName());

		this.skypeLabel = new JLabel("Update Skype status with current song");
		this.skypeLabel.setForeground(Color.WHITE);
		this.skypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.skypeLabel.setBounds(242, 218, 222, 24);
		this.settingsWidget.getContentPane().add(this.skypeLabel);

		this.updateSkypeCheckbox = new JCheckBox("Update Skype");
		this.updateSkypeCheckbox.setSelected(Settings.isUpdateSkype());
		this.updateSkypeCheckbox.setForeground(Color.WHITE);
		this.updateSkypeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		this.updateSkypeCheckbox.setBackground(new Color(49, 49, 49));
		this.updateSkypeCheckbox.setBounds(242, 241, 98, 23);
		this.settingsWidget.getContentPane().add(this.updateSkypeCheckbox);

	}

	public boolean isOpen() {
		return Utils.isNull(this.settingsWidget) ? false : this.settingsWidget
				.isVisible();
	}

	public void setSettingsWidget(final JFrame settingsWidget) {
		this.settingsWidget = settingsWidget;
	}
}
