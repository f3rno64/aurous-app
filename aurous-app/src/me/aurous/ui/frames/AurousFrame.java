package me.aurous.ui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import me.aurous.player.Settings;
import me.aurous.player.scenes.MediaPlayerScene;
import me.aurous.ui.UISession;
import me.aurous.ui.menus.AurousBar;
import me.aurous.ui.panels.ControlPanel;
import me.aurous.ui.panels.PlayListPanel;
import me.aurous.ui.panels.TabelPanel;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;

import com.boxysystems.jgoogleanalytics.FocusPoint;
import com.boxysystems.jgoogleanalytics.JGoogleAnalyticsTracker;


/**
 * @author Andrew
 *
 */
public class AurousFrame implements WindowListener {

	public JFrame aurousFrame;

	public JFXPanel jfxPanel;

	public Scene scene;

	/**
	 * Create the application.
	 */
	public AurousFrame() {
		initialize();
		this.aurousFrame.addWindowListener(this);

	}

	public JFrame getAurousFrame() {
		return aurousFrame;
	}

	public Scene getScene() {
		return scene;
	}

	private void initFX(final JFXPanel fxPanel) {
		// This method is invoked on the JavaFX thread
		try {
			final MediaPlayerScene mediaPlayerScene = new MediaPlayerScene();
			UISession.setMediaPlayerScene(mediaPlayerScene);
			scene = UISession.getMediaPlayerScene().createScene(
					"https://www.youtube.com/watch?v=kGubD7KG9FQ");
			setScene(scene);
			fxPanel.setScene(scene);
			UISession.setJFXPanel(fxPanel);
		} catch (final Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Initialize the contents of the frame.
	 *
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		if (com.sun.javafx.Utils.isMac()) {
			// take the menu bar off the jframe
			System.setProperty("apple.laf.useScreenMenuBar", "true");

			// set the name of the application menu item
			System.setProperty(
					"com.apple.mrj.application.apple.menu.about.name", "Aurous");
			final com.apple.eawt.Application macApp = com.apple.eawt.Application
					.getApplication();
			macApp.setDockIconImage(new ImageIcon(this.getClass().getResource(
					"/resources/aurouslogo.png")).getImage());
		}

		this.aurousFrame = new JFrame();
		this.aurousFrame.setResizable(true);

		this.aurousFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				AurousFrame.class.getResource("/resources/aurouslogo.png")));
		this.aurousFrame.setTitle("Aurous");
		this.aurousFrame.getContentPane().setBackground(new Color(30, 30, 30));
		this.aurousFrame.setSize(new Dimension(800, 600));
		this.aurousFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.aurousFrame.getContentPane().setLayout(new BorderLayout());
		this.aurousFrame.setFont(new Font("Calibri", Font.PLAIN, 12));
		// we don't actually have to add the panel
		jfxPanel = new JFXPanel();

		try {
			final ControlPanel mediaControlsPanel = new ControlPanel();
			UISession.setControlPanel(mediaControlsPanel);
			this.aurousFrame.getContentPane().add(mediaControlsPanel,
					BorderLayout.SOUTH);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final JPanel playListPanel = new PlayListPanel();
		this.aurousFrame.getContentPane().add(playListPanel, BorderLayout.WEST);

		final JMenuBar menuBar = new AurousBar();

		this.aurousFrame.setJMenuBar(menuBar);

		final JPanel tabelPanel = new TabelPanel();
		this.aurousFrame.getContentPane().add(tabelPanel, BorderLayout.CENTER);

		// lets make the panel support media
		Platform.runLater(() -> {
			initFX(jfxPanel);
		});

		Utils.centerFrameOnMainDisplay(aurousFrame);

		this.aurousFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {
				Settings.saveSettings(false);

			}
		});

		// tracking code
		final JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(
				"Aurous App", "2.2.5", "UA-53956512-1");

		final FocusPoint focusPoint = new FocusPoint("AppStart");

		tracker.trackAsynchronously(focusPoint);
		Settings.loadSettings();

	}

	public void setScene(final Scene scene) {
		this.scene = scene;
	}

	@Override
	public void windowActivated(final WindowEvent e) {
		MediaUtils.isOutOfFocus = false;

	}

	@Override
	public void windowClosed(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
		MediaUtils.isOutOfFocus = true;

	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
		MediaUtils.isOutOfFocus = false;

	}

	@Override
	public void windowIconified(final WindowEvent e) {
		MediaUtils.isOutOfFocus = true;

	}

	@Override
	public void windowOpened(final WindowEvent e) {
		MediaUtils.isOutOfFocus = false;

	}
}
