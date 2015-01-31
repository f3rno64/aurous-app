package me.aurous.player.scenes;

import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.aurous.ui.UISession;
import me.aurous.ui.panels.ControlPanel;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;

public class RemoteMediaScene {
	public ChangeListener<Duration> progressChangeListener;

	private Media media;

	public MediaPlayer player;

	public MediaView view;

	/**
	 * Create a JFX media player scene.
	 */
	public Scene createMediaPlayer(final String sourceURL) throws Throwable {

		final ControlPanel panel = UISession.getControlPanel();
		final Group root = new Group();
		root.autosize();
		MediaUtils.activeMedia = sourceURL;
		final String trailer = MediaUtils.getMediaURL(sourceURL);
		try {
			this.media = new Media(trailer.trim());

		} catch (final Exception e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
			return null;
		}

		this.player = new MediaPlayer(this.media);

		this.view = new MediaView(this.player);
		this.view.setFitWidth(1);
		this.view.setFitHeight(1);
		this.view.setPreserveRatio(false);

		// System.out.println("media.width: "+media.getWidth());

		final Scene mediaPlayer = new Scene(root, 1, 1, Color.BLACK);

		// player.play();

		this.player.setOnReady(() -> {
			this.player.setAutoPlay(false);
			panel.seek().setValue(0);
			if (sourceURL
					.contains("https://www.youtube.com/watch?v=kGubD7KG9FQ")) {
				this.player.pause();
			} else {
				this.player.play();
			}

		});

		this.progressChangeListener = (observableValue, oldValue, newValue) -> {

			final long currentTime = (long) newValue.toMillis();

			final long totalDuration = (long) this.player.getMedia()
					.getDuration().toMillis();
			updateTime(currentTime, totalDuration);
		};

		this.player.currentTimeProperty().addListener(
				this.progressChangeListener);

		this.player.setOnEndOfMedia(() -> {
			this.player.currentTimeProperty().removeListener(
					this.progressChangeListener);
			MediaUtils.handleEndOfStream();

		});

		UISession.setMediaPlayer(this.player);
		UISession.setMediaView(this.view);
		UISession.setMedia(this.media);

		return (mediaPlayer);
	}

	private void updateTime(final long currentTime, final long totalDuration) {
		final ControlPanel panel = UISession.getControlPanel();
		final int percentage = (int) (((currentTime * 100.0) / totalDuration) + 0.5); // jesus
		final double seconds = currentTime / 1000.0;
		UISession.getControlPanel().seek().setValue(percentage);
		panel.seek().setValue(percentage);
		panel.current().setText(MediaUtils.calculateTime((int) seconds));

	}

}
