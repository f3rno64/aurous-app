package me.aurous.ui;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import me.aurous.player.scenes.MediaPlayerScene;
import me.aurous.ui.frames.AurousFrame;
import me.aurous.ui.widgets.AboutWidget;
import me.aurous.ui.widgets.BuilderWidget;
import me.aurous.ui.widgets.DiscoWidget;
import me.aurous.ui.widgets.ImporterWidget;
import me.aurous.ui.widgets.SettingsWidget;

/**
 * Created by Andrew on 9/4/2014.
 */
public class UISession {

	private static AurousFrame presenter;

	public static AurousFrame getPresenter() {
		return presenter;
	}

	public static void setPresenter(final AurousFrame presenter) {
		UISession.presenter = presenter;
	}
	
	private static AboutWidget aboutWidget;

	public static AboutWidget getAboutWidget() {
		return aboutWidget;
	}

	public static void setAboutWidget(final AboutWidget aboutWidget) {
		UISession.aboutWidget = aboutWidget;
	}
	
	private static SettingsWidget settingsWidget;

	public static SettingsWidget getSettingsWidget() {
		return settingsWidget;
	}

	public static void setSettingsWidget(final SettingsWidget settingsWidget) {
		UISession.settingsWidget = settingsWidget;
	}
	
	
	private static BuilderWidget builderWidget;

	public static BuilderWidget getBuilderWidget() {
		return builderWidget;
	}

	public static void setBuilderWidget(final BuilderWidget builderWidget) {
		UISession.builderWidget = builderWidget;
	}
	
	private static ImporterWidget importerWidget;

	public static ImporterWidget getImporterWidget() {
		return importerWidget;
	}

	public static void setImporterWidget(final ImporterWidget importerWidget) {
		UISession.importerWidget = importerWidget;
	}
	
	
	
	private static DiscoWidget discoWidget;

	public static DiscoWidget getDiscoWidget() {
		return discoWidget;
	}

	public static void setDiscoWidget(final DiscoWidget discoWidget) {
		UISession.discoWidget = discoWidget;
	}
	
	
	

	private static MediaPlayer mediaPlayer;

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(final MediaPlayer mediaPlayer) {
		UISession.mediaPlayer = mediaPlayer;
	}

	private static MediaView mediaView;

	public static MediaView getMediaView() {
		return mediaView;
	}

	public static void setMediaView(final MediaView mediaView) {
		UISession.mediaView = mediaView;
	}

	private static Media media;

	public static Media getMedia() {
		return media;
	}

	public static void setMedia(final Media media) {
		UISession.media = media;

	}

	private static MediaPlayerScene mediaScene;

	public static MediaPlayerScene getMediaPlayerScene() {
		return mediaScene;
	}

	public static void setMediaPlayerScene(final MediaPlayerScene scene) {
		UISession.mediaScene = scene;

	}

	private static JFXPanel jfxPanel;

	public static JFXPanel getJFXPanel() {
		return jfxPanel;
	}

	public static void setJFXPanel(final JFXPanel fxPanel) {
		UISession.jfxPanel = fxPanel;

	}
}
