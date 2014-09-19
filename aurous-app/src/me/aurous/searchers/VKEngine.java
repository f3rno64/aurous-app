package me.aurous.searchers;

import static com.sun.javafx.Utils.convertUnicode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.Normalizer;

import javax.swing.JOptionPane;

import me.aurous.ui.UISession;
import me.aurous.utils.ModelUtils;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;
import me.aurous.vkapi.VkAuth;
import me.aurous.vkapi.audio.AudioApi;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;



public class VKEngine {
	private static boolean isSearching = false;
	public static String buildSearchCSV(final String json) {
		final StringBuilder csv = new StringBuilder()
				.append("Title, Artist, Duration, URL");
		csv.append(System.getProperty("line.separator"));
		final JSONObject jsonObj = new JSONObject(json);
		final JSONArray response = jsonObj.getJSONArray("response");
		for (int i = 0; i < response.length(); i++) {
			if (i == 0) {
				continue;
			}
			final Object cities = response.get(i);
			final JSONObject jsonResults = new JSONObject(cities.toString());
			final int duration = jsonResults.getInt("duration");

			String artist = jsonResults.getString("artist");
			String title = jsonResults.getString("title");
			final String url = jsonResults.getString("url");
			title = cleanString(title);

			artist = cleanString(artist);

			final String line = String.format("%s,%s,%s,%s", title, artist,
					MediaUtils.calculateTime(duration), url);
			csv.append(line.trim());
			csv.append(System.getProperty("line.separator"));

		}

		return csv.toString();
	}

	private static String cleanString(String dirtyString) {
		dirtyString = StringEscapeUtils.escapeHtml4(dirtyString.replaceAll(
				"[^\\x20-\\x7e]", ""));
		dirtyString = flattenToAscii(dirtyString);
		dirtyString = convertUnicode(dirtyString);

		if (dirtyString.contains(",")) {
			dirtyString = escapeComma(dirtyString);
		}
		dirtyString = StringEscapeUtils.unescapeHtml4(dirtyString);
		final String cleanedString = dirtyString;
		return cleanedString;
	}

	private static String escapeComma(final String str) {
		return str.replace(",", "\\,");
	}

	public static String flattenToAscii(String string) {
		final char[] out = new char[string.length()];
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		int j = 0;
		for (int i = 0, n = string.length(); i < n; ++i) {
			final char c = string.charAt(i);
			if (c <= '\u007F') {
				out[j++] = c;
			}
		}
		return new String(out);
	}

	public static void search() {
		if (isSearching) {
			return;
		}
		
		isSearching = true;
		String query = UISession.getSearchWidget().getSearchBar().getText();
		query = query.replace(" ", "%20");
		int RESULT_LIMIT = 100; //to do settings, max is 300
		int PERFORMER_ONLY = UISession.getSearchWidget().getComboBox().getSelectedItem().toString().equals("by title") ? 0 : 1 ;
		
		final String formData = Utils.readFile("data/settings/vkauth.dat",
				Charset.defaultCharset());

		final AudioApi api = new AudioApi(VkAuth.VK_APP_ID, formData.trim());
		final String parameters = String.format(
				"q=%s&lyrics=0&count=%s&performer_only=%s", query,
				RESULT_LIMIT, PERFORMER_ONLY);

		try {
			final String json = api.searchAudioJson(parameters);
			System.out.println(json);
			if (json.contains("\"response\":[0]")) {
				JOptionPane.showMessageDialog(UISession.getSearchWidget().getWidget(), "No results found!", "Error",
						JOptionPane.ERROR_MESSAGE);
				isSearching = false;
				return;
			}
			final String csv = buildSearchCSV(json);
			Utils.writeFile(csv, "data/settings/vkcache.dat");
			ModelUtils.loadSearchResults("data/settings/vkcache.dat");
			isSearching = false;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		
	}

	

}
