package me.aurous.searchengines.impl;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

import me.aurous.apis.impl.vkapi.VKAuth;
import me.aurous.apis.impl.vkapi.audio.AudioApi;
import me.aurous.searchengines.SearchEngine;
import me.aurous.ui.UISession;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Constants;
import me.aurous.utils.ModelUtils;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class VKEngine extends SearchEngine {
	private final int RESULT_LIMIT; // to do settings, max is 300
	private int PERFORMER_ONLY;

	public VKEngine(final int resultLimit) {
		this.RESULT_LIMIT = resultLimit;
	}

	public String buildSearchCSV(final String json) {

		final StringBuilder csv = new StringBuilder()
				.append("Title, Artist, Duration, URL, id");
		csv.append(System.getProperty("line.separator"));
		final JSONObject mainObject = new JSONObject(json);
		final JSONArray response = mainObject.getJSONArray("response");
		for (int i = 0; i < response.length(); i++) {
			if (i == 0) {
				continue;
			}
			final Object jsonObject = response.get(i);
			final JSONObject jsonResults = new JSONObject(jsonObject.toString());
			final int duration = jsonResults.getInt("duration");

			String artist = jsonResults.getString("artist");
			String title = jsonResults.getString("title");
			final String url = jsonResults.getString("url");
			final int id = jsonResults.getInt("aid");
			final int owner_id = jsonResults.getInt("owner_id");
			final String id_mix = "http://vk.me/" + owner_id + "_" + id;
			title = MediaUtils.cleanString(title);

			artist = MediaUtils.cleanString(artist);

			final String line = String.format("%s,%s,%s,%s,%s", title, artist,
					MediaUtils.calculateTime(duration), url, id_mix);
			csv.append(line.trim());
			csv.append(System.getProperty("line.separator"));

		}

		return csv.toString();
	}

	@Override
	public void search() {
		if (Constants.IS_SEARCHING) {
			return;
		}
		Constants.IS_SEARCHING = true;
		this.PERFORMER_ONLY = UISession.getSearchWidget().getComboBox()
				.getSelectedItem().toString().equals("by title") ? 0 : 1;

		String query = UISession.getSearchWidget().getSearchBar().getText();
		query = query.replace(" ", "%20");

		final String authData = Utils.readFile(Constants.DATA_PATH
				+ "settings/vkauth.dat", Charset.defaultCharset());

		final AudioApi api = new AudioApi(VKAuth.VK_APP_ID, authData.trim());
		final String parameters = String
				.format("q=%s&auto_complete=1&sort=2&lyrics=0&count=%s&performer_only=%s",
						query, this.RESULT_LIMIT, this.PERFORMER_ONLY);

		try {
			final String searchJSON = api.searchAudioJson(parameters);
			if (searchJSON.contains("\"response\":[0]")) {
				JOptionPane.showMessageDialog(UISession.getSearchWidget()
						.getWidget(), "No results found!", "Error",
						JOptionPane.ERROR_MESSAGE);
				Constants.IS_SEARCHING = false;
				return;
			}
			final String searchCSV = buildSearchCSV(searchJSON);
			Utils.writeFile(searchCSV, Constants.DATA_PATH
					+ "search/vkcache.dat");
			ModelUtils.loadSearchResults(Constants.DATA_PATH
					+ "search/vkcache.dat");
			Constants.IS_SEARCHING = false;
		} catch (final IOException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		}

	}

}
