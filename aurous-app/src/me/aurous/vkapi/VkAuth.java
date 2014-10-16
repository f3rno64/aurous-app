package me.aurous.vkapi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import me.aurous.utils.Constants;

public class VkAuth extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	public static final int VK_APP_ID = 4554985;
	public static String VK_APP_MASK = "audio,offline";
	public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
	public static final String VK_AUTH_URL = "https://oauth.vk.com/authorize?client_id="
			+ VK_APP_ID + "&scope=" + VK_APP_MASK + "&response_type=token";
	public static final String APPLICATION_TITLE = "Aurous VK Auth";
	public static final String LOGIN_SUCCESS_PAGE = "blank.html#",
			LOGIN_FAILURE_PAGE = "blank.html#error";
	private volatile boolean loginSuccess = false, loginFailure = false;

	private String formData = null;

	private void changeState(final String Url) {
		if (Url.contains(LOGIN_FAILURE_PAGE)) {
			loginFailure = true;
		} else if (Url.contains(LOGIN_SUCCESS_PAGE)) {
			loginSuccess = true;
			try {
				formData = URLDecoder.decode(
						Url.substring(Url.indexOf(LOGIN_SUCCESS_PAGE)
								+ LOGIN_SUCCESS_PAGE.length()), "UTF-8");
			} catch (final UnsupportedEncodingException ex) {
				Logger.getLogger(VkAuth.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
	}

	@Override
	public void start(final Stage primaryStage) {
		final File f = new File(Constants.DATA_PATH + "settings/vkauth.dat");
		if (f.exists() && !f.isDirectory()) { /* do something */
			final int dialogButton = JOptionPane.YES_NO_OPTION;
			final int dialogResult = JOptionPane
					.showConfirmDialog(
							null,
							"You already have an OAuth key, would you like to generate a new one?",
							"Warning", dialogButton);

			if (dialogResult == JOptionPane.YES_OPTION) {
			} else {
				Platform.exit();
			}
		}

		primaryStage.setTitle(APPLICATION_TITLE);
		final WebView view = new WebView();
		final WebEngine engine = view.getEngine();
		engine.load(VK_AUTH_URL);
		engine.getLoadWorker()
		.stateProperty()
		.addListener(
				(ChangeListener<State>) (ov, oldState, newState) -> {
					if (newState == State.SUCCEEDED) {
						changeState(engine.getLocation());
					}
				});
		primaryStage.setScene(new Scene(view));
		primaryStage.show();

		new Thread(() -> {
			while (!loginSuccess && !loginFailure && primaryStage.isShowing()) {

			}
			if (loginFailure || (!primaryStage.isShowing())) {
				Platform.exit();
			} else {
				try {

					final PrintWriter out = new PrintWriter(Constants.DATA_PATH
							+ "settings/vkauth.dat");
					out.println(formData);
					out.close();
					Platform.exit();

				} catch (final IOException ex) {
					Logger.getLogger(VkAuth.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		}).start();
	}

}
