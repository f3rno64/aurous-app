package me.aurous.searchers;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

import me.aurous.ui.UISession;
import me.aurous.utils.ModelUtils;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;
import me.aurous.vkapi.VkAuth;
import me.aurous.vkapi.audio.AudioApi;

import org.json.JSONArray;
import org.json.JSONObject;

public class VKEngine {
	public static String buildSearchCSV(final String json) {
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

	public static void search() {
		if (isSearching) {
			return;
		}

		isSearching = true;
		String query = UISession.getSearchWidget().getSearchBar().getText();
		query = query.replace(" ", "%20");
		final int RESULT_LIMIT = 100; // to do settings, max is 300
		final int PERFORMER_ONLY = UISession.getSearchWidget().getComboBox()
				.getSelectedItem().toString().equals("by title") ? 0 : 1;

		final String formData = Utils.readFile("data/settings/vkauth.dat",
				Charset.defaultCharset());

		final AudioApi api = new AudioApi(VkAuth.VK_APP_ID, formData.trim());
		final String parameters = String
				.format("q=%s&auto_complete=1&sort=2&lyrics=0&count=%s&performer_only=%s",
						query, RESULT_LIMIT, PERFORMER_ONLY);

		try {
			final String json = api.searchAudioJson(parameters);
			if (json.contains("\"response\":[0]")) {
				JOptionPane.showMessageDialog(UISession.getSearchWidget()
						.getWidget(), "No results found!", "Error",
						JOptionPane.ERROR_MESSAGE);
				isSearching = false;
				return;
			}
			final String csv = buildSearchCSV(json);
			Utils.writeFile(csv, "data/search/vkcache.dat");
			ModelUtils.loadSearchResults("data/search/vkcache.dat");
			isSearching = false;
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	private static boolean isSearching = false;

}
