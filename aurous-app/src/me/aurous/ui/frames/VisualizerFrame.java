package me.aurous.ui.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import me.aurous.player.functions.PlayerFunctions;
import me.aurous.ui.UISession;
import me.aurous.utils.media.MediaUtils;

public class VisualizerFrame extends JFrame {
	public static int GetScreenWorkingHeight() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().height;
	}

	public static int GetScreenWorkingWidth() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().width;
	}


	public static void visualize() {
		if (UISession.getMediaPlayer() == null) {
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
				e.printStackTrace();
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

	public Scene createVisualScene() {
		final int screenHeight = GetScreenWorkingHeight();
		final int screenWidth = GetScreenWorkingWidth();
		final double delimter = (screenWidth / 1.673);
		final int translatLocation = (int) Math.round(screenWidth - delimter);
		final Group root = new Group();
		final Random rand = new Random();

		UISession.getMediaView().setFitWidth(1);
		UISession.getMediaView().setFitHeight(1);
		UISession.getMediaView().setPreserveRatio(false);

		final VBox vbox = new VBox();
		final HBox hbox = new HBox(2);
		final int bands = UISession.getMediaPlayer().getAudioSpectrumNumBands() - 20;
		final Rectangle[] rects = new Rectangle[bands];
		for (int i = 0; i < rects.length; i++) {
			rects[i] = new Rectangle();

			rects[i].getTransforms().add(new Rotate(180, 0, 0)); //
			rects[i].setFill(Color.GRAY);
			rects[i].setArcHeight(screenHeight);
			hbox.getChildren().add(rects[i]);

		}

		final Text durationLabel = new Text(25, 25, MediaUtils.activeInfo);
		durationLabel.setFill(Color.WHITESMOKE);
		durationLabel.setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
		final DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.BLACK);

		durationLabel.setEffect(ds);

		vbox.getChildren().add(hbox);
		root.getChildren().add(UISession.getMediaView());
		root.getChildren().add(vbox);

		root.getChildren().add(durationLabel);
		durationLabel.setTranslateX(-25);
		vbox.setMaxWidth(screenWidth);

		hbox.setTranslateY(translatLocation);
		final Scene scene = new Scene(root, screenWidth, screenHeight,
				Color.rgb(35, 35, 35));
		hbox.setMinWidth(screenWidth);
		final int bandWidth = screenWidth / rects.length;
		int recheight = 2;
		for (final Rectangle r : rects) {
			recheight++;
			r.setWidth(bandWidth);

			r.setHeight(recheight);
		}

		vbox.setMinSize(screenWidth, 100);

		vbox.setTranslateY(405 - 100);
		UISession.getMediaPlayer()
				.setAudioSpectrumListener(
						(v, v1, mags, floats1) -> {

							for (int i = 0; i < rects.length; i++) {
								final double h = mags[i] + 60;
								if (h > 2) {
									// nextInt is normally exclusive of the top
									// value,
									// so add 1 to make it inclusive
									final int randomNum = rand
											.nextInt((10 - 1) + 1) + 1;
									rects[i].setHeight(h * 30);
									rects[i].setFill(randomNum > 5 ? Color.GREY
											: Color.DARKGRAY);

								}
							}
						});

		return (scene);
	}

	private void initFX(final JFXPanel fxPanel) {
		// This method is invoked on the JavaFX thread
		scene = createVisualScene();
		fxPanel.setScene(scene);
	}

	public boolean isOpen() {
		return UISession.getVisualFrame() == null ? false : UISession
				.getVisualFrame().isVisible();
	}
}
