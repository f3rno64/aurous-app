package me.aurous.player.scenes;

import me.aurous.ui.UISession;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class VisualizerScene {

	public static Media media = UISession.getMedia();

	public static MediaPlayer player = UISession.getMediaPlayer();



	public static MediaView view = UISession.getMediaView();

	public static Scene createScene() throws Throwable {
		final Group root = new Group();
		root.autosize();
	
		

		

		// System.out.println("media.width: "+media.getWidth());

		final Timeline slideIn = new Timeline();
		final Timeline slideOut = new Timeline();
		root.setOnMouseExited(mouseEvent -> slideOut.play());
		root.setOnMouseEntered(mouseEvent -> slideIn.play());
		final VBox vbox = new VBox();
		final Slider slider = new Slider();
		vbox.getChildren().add(slider);

		final Text durationLabel = new Text(25, 25, "0:0:0:0");
		durationLabel.setFill(Color.WHITESMOKE);
		durationLabel.setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
		final DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.BLACK);

		durationLabel.setEffect(ds);
		// text1.setCache(true);
		// t.setX(10.0f);
		// t.setY(270.0f);
		// t.setFont(Font.font(null, FontWeight.BOLD, 32));

		final HBox hbox = new HBox(2);
		final int bands = VisualizerScene.player.getAudioSpectrumNumBands();
		final Rectangle[] rects = new Rectangle[bands];
		for (int i = 0; i < rects.length; i++) {
			rects[i] = new Rectangle();
			rects[i].setFill(Color.ALICEBLUE);
			hbox.getChildren().add(rects[i]);
		}

		vbox.getChildren().add(hbox);

		root.getChildren().add(VisualizerScene.view);
		root.getChildren().add(vbox);
		root.getChildren().add(durationLabel);
		durationLabel.setTranslateX(-25);

		vbox.setMaxWidth(522);

		hbox.setTranslateY(60);
		slider.setBackground(Background.EMPTY);

		final Scene scene = new Scene(root, 530, 405, Color.BLACK);

		VisualizerScene.player.play();

	
			final int w = 530;
			final int h = 405;

			hbox.setMinWidth(w);
			final int bandWidth = w / rects.length;
			for (final Rectangle r : rects) {
				r.setWidth(bandWidth);

				r.setHeight(2);
			}

			vbox.setMinSize(w, 100);

			vbox.setTranslateY(h - 100);

			slider.setMin(0.0);
			slider.setValue(0.0);
			slider.setTranslateY(60);
			slider.setMax(VisualizerScene.player.getTotalDuration()
					.toSeconds());

			slideOut.getKeyFrames().addAll(
					new KeyFrame(new Duration(0), new KeyValue(vbox
							.translateYProperty(), h - 100), new KeyValue(vbox
									.opacityProperty(), 0.9)),
									new KeyFrame(new Duration(300), new KeyValue(vbox
											.translateYProperty(), h), new KeyValue(vbox
													.opacityProperty(), 0.0)));
			slideIn.getKeyFrames().addAll(
					new KeyFrame(new Duration(0), new KeyValue(vbox
							.translateYProperty(), h), new KeyValue(vbox
									.opacityProperty(), 0.0)),
									new KeyFrame(new Duration(300), new KeyValue(vbox
											.translateYProperty(), h - 100), new KeyValue(vbox
													.opacityProperty(), 0.9)));
		
		VisualizerScene.player.currentTimeProperty().addListener(
				(observableValue, duration, current) -> {
					slider.setValue(current.toSeconds());
					final long currentTime = (long) current.toMillis();
					final long totalDuration = (long) VisualizerScene.player
							.getMedia().getDuration().toMillis();
					VisualizerScene.updateTime(currentTime, totalDuration,
							durationLabel);

				});
		slider.setOnMouseClicked(mouseEvent -> VisualizerScene.player
				.seek(Duration.seconds(slider.getValue())));
		VisualizerScene.player
		.setAudioSpectrumListener((v, v1, mags, floats1) -> {

			for (int i = 0; i < rects.length; i++) {
				final double height = mags[i] + 60;
				if (height > 2) {
					rects[i].setHeight(height);
				}
			}
		});
	System.out.println("Dacac");
		return (scene);
	}

	public static String formatDuration(final long millis) {
		final long seconds = (millis / 1000) % 60;
		final long minutes = (millis / (1000 * 60)) % 60;
		final long hours = millis / (1000 * 60 * 60);

		final StringBuilder b = new StringBuilder();
		b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours)
				: String.valueOf(hours));
		b.append(":");
		b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0"
				+ minutes) : String.valueOf(minutes));
		b.append(":");
		b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0"
				+ seconds) : String.valueOf(seconds));
		return b.toString();
	}

	private static void updateTime(final long currentTime,
			final long totalDurationlong, final Text durationLabel) {
		final String currentDuration = VisualizerScene
				.formatDuration(currentTime);

		final String time = VisualizerScene.formatDuration(totalDurationlong);

		durationLabel.setText(currentDuration + "/" + time);

		final int percentage = (int) (((currentTime * 100.0) / totalDurationlong) + 0.5); // jesus
		// god
		if (percentage == 100) {

		
		}

	}

}
