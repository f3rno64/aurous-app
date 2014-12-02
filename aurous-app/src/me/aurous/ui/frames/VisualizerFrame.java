package me.aurous.ui.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import me.aurous.player.functions.PlayerFunctions;
import me.aurous.player.scenes.VisualizerScene;
import me.aurous.ui.UISession;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Utils;

public class VisualizerFrame extends JFrame {

	public static void visualize() {
		if (Utils.isNull(UISession.getMediaPlayer())) {
			return;
		}
		if ((UISession.getVisualFrame() != null)
				&& UISession.getVisualFrame().isOpen()) {
			UISession.getVisualFrame().toFront();
			UISession.getVisualFrame().repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final VisualizerFrame frame = new VisualizerFrame();
				UISession.setVisualFrame(frame);
				frame.setVisible(true);
			} catch (final Exception e) {
				final ExceptionWidget eWidget = new ExceptionWidget(Utils
						.getStackTraceString(e, ""));
				eWidget.setVisible(true);
			}
		});
	}

	public Scene scene;

	/**
	 *
	 */
	private static final long serialVersionUID = -6758290362272465557L;

	public JFXPanel panel;

	/**
	 * Create the frame.
	 */
	public VisualizerFrame() {
		if (com.sun.javafx.Utils.isMac()) {
			// take the menu bar off the jframe
			System.setProperty("apple.laf.useScreenMenuBar", "false");

			// set the name of the application menu item
			System.setProperty(
					"com.apple.mrj.application.apple.menu.about.name", "Aurous");
			final com.apple.eawt.Application macApp = com.apple.eawt.Application
					.getApplication();
			macApp.setDockIconImage(new ImageIcon(this.getClass().getResource(
					"/resources/aurouslogo.png")).getImage());
		}
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AurousFrame.class.getResource("/resources/aurouslogo.png")));
		setTitle("Aurous Visual");
		setAlwaysOnTop(true);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout(0, 0));

		panel = new JFXPanel();
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final boolean isPaused = PlayerFunctions.isPaused;
				final int c = e.getKeyCode();

				if (c == KeyEvent.VK_LEFT) {
					PlayerFunctions.seekPrevious();
				} else if (c == KeyEvent.VK_RIGHT) {
					PlayerFunctions.seekNext();
				} else if (c == KeyEvent.VK_P) {
					if (!isPaused) {
						PlayerFunctions.pause(UISession.getControlPanel()
								.play());
					} else {
						PlayerFunctions
								.play(UISession.getControlPanel().play());
					}
				} else if (c == KeyEvent.VK_SPACE) {
					if (!isPaused) {
						PlayerFunctions.pause(UISession.getControlPanel()
								.play());
					} else {
						PlayerFunctions
								.play(UISession.getControlPanel().play());
					}
				} else if (c == KeyEvent.VK_ESCAPE) {
					panel.setScene(null);
					scene = null;
					UISession.setVisualFrame(null);
					setVisible(false);
					dispose();
					return;
				}
			}
		});
		getContentPane().add(panel, BorderLayout.CENTER);
		Platform.runLater(() -> initFX(panel));
	}

	private void initFX(final JFXPanel fxPanel) {
		final VisualizerScene visualScene = new VisualizerScene();
		// This method is invoked on the JavaFX thread
		scene = visualScene.createVisualScene();
		fxPanel.setScene(scene);
	}

	public boolean isOpen() {
		return Utils.isNull(UISession.getVisualFrame()) ? false : UISession
				.getVisualFrame().isVisible();
	}
}
