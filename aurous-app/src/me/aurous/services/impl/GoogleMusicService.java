package me.aurous.services.impl;

import java.util.Iterator;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.SwingUtilities;

import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GoogleMusicService {
	private static String html;

	public static void main(final String[] args) {
		importPlayList("");
	}

	public static void importPlayList(final String playListName) {

		SwingUtilities
				.invokeLater(() -> {
					new JFXPanel(); // this will prepare JavaFX toolkit and
									// environment
					Platform.runLater(() -> {
						final Group root = new Group();
						final Scene scene = new Scene(root, 820, 480);

						final Stage stage = new Stage();
						final WebView webView = new WebView();
						final WebEngine webEngine = webView.getEngine();
						webEngine
								.getLoadWorker()
								.stateProperty()
								.addListener(
										(ChangeListener<State>) (ov, oldState,
												newState) -> {
											if (newState
													.equals(Worker.State.SUCCEEDED)) {
												html = (String) webEngine
														.executeScript("document.documentElement.outerHTML");
												webEngine.getLoadWorker()
														.cancel();
												Platform.exit();

											}
										});

						webEngine
								.load("https://play.google.com/music/preview/pl/AMaBXykQXEAwq4hSwCaTRBIctCrMlhQRW3fO58T6mjrIB-DGZLxCSQUFQ7e9jddIOjCkDRptNZqufvKZI6qHFU2LqpY7WrIfmw==");
						root.getChildren().add(webView);
						stage.centerOnScreen();

						stage.setScene(scene);
						stage.show();
						stage.toBack();

					});
				});

		while (Utils.isNull(html)) {

		}
		Document doc = null;
		doc = Jsoup.parse(html);
		final Element table = doc.select("table[class=tracks]").first();

		final Iterator<Element> ite = table.select(
				"td[class=title-col fade-out]").iterator();

		while (ite.hasNext()) {
			final String songTags = ite.next().html()
					.replaceAll("[\r\n]+", " ").trim();
			final String artist = MediaUtils.getBetween(songTags,
					"<div class=\"artist\">", "</div>").trim();
			final String song = MediaUtils.getBetween(songTags,
					"<div class=\"track-title\">", "</div>").trim();
			System.out.println(song);

		}

		final Iterator<Element> ite2 = table.select("td[class=album-art]")
				.iterator();

		while (ite2.hasNext()) {
			final String imgTag = ite2.next().html().replaceAll("[\r\n]+", " ")
					.trim();
			final String albumArt = MediaUtils.getBetween(imgTag,
					"<img src=\"", "=s40");
			System.out.println(albumArt);

		}

	}
}
