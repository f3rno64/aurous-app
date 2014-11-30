package me.aurous.player.scenes;

import java.util.Random;

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
import me.aurous.ui.UISession;
import me.aurous.utils.media.MediaUtils;

public class VisualizerScene {
	public static int GetScreenWorkingHeight() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().height;
	}

	public static int GetScreenWorkingWidth() {
		return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().width;
	}

	/**
	 * Creates a Javafx based visualizer.
	 */
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
		final Scene visaulizer = new Scene(root, screenWidth, screenHeight,
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

		return (visaulizer);
	}

}
