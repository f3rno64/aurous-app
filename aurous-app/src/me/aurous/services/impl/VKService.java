package me.aurous.services.impl;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

import me.aurous.services.PlaylistService;
import me.aurous.ui.UISession;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Constants;
import me.aurous.utils.Utils;
import me.aurous.vkapi.VKAuth;
import me.aurous.vkapi.audio.AudioApi;

import org.json.JSONArray;
import org.json.JSONObject;

public class VKService extends PlaylistService {

	private final String contentURL;
	private final String playListName;
	private String streamURL;

	public VKService(final String contentURL, final String playListName) {
		this.contentURL = contentURL;
		this.playListName = playListName;

	}

	private String getVKStream(final String json) {
		final JSONObject jsonObj = new JSONObject(json);
		final JSONArray response = jsonObj.getJSONArray("response");
		for (final int i = 0; i < response.length();) {
			final Object jsonObject = response.get(i);
			final JSONObject jsonResults = new JSONObject(jsonObject.toString());
			final String stream = jsonResults.getString("url");
			return stream;

		}
		return "";

	}

	public String getStreamURL() {
		return this.streamURL;
	}

	public String getPlayListName() {
		return this.playListName;
	}

	@Override
	public void grab() {
		String json = null;
		final int pos = contentURL.lastIndexOf("/");
		final String ids = contentURL.substring(pos + "/".length());
		final String formData = Utils.readFile(Constants.DATA_PATH
				+ "settings/vkauth.dat", Charset.defaultCharset());

		final AudioApi api = new AudioApi(VKAuth.VK_APP_ID, formData.trim());
		final String parameters = String.format("audios=%s", ids);

		try {
			json = api.searchAudioByIdJson(parameters);
		} catch (final IOException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		}
		if (json.contains("\"response\":[0]")) {
			JOptionPane.showMessageDialog(UISession.getSearchWidget()
					.getWidget(), "No Stream Found!", "Error",
					JOptionPane.ERROR_MESSAGE);

			this.streamURL = "nil";
		}
		final String streamURL = getVKStream(json);

		this.streamURL = streamURL.trim();
	}
}
