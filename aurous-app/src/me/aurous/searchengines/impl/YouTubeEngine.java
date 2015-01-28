package me.aurous.searchengines.impl;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import me.aurous.apis.impl.webapi.search.impl.YouTubeSearch;
import me.aurous.searchengines.SearchEngine;
import me.aurous.ui.UISession;
import me.aurous.utils.Constants;
import me.aurous.utils.ModelUtils;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class YouTubeEngine extends SearchEngine {
	
	public String buildSearchCSV(final String json) {

		final StringBuilder csv = new StringBuilder()
		.append("Title, Artist, Duration, URL, id");
		csv.append(System.getProperty("line.separator"));
		// ...
		JSONArray jsonArray = new JSONArray(json);
		// ...
	
		for(int i=0;i< jsonArray.length();i++){                        
			//System.out.println(jsonArray.length());
		    JSONObject jsonResults = jsonArray.getJSONObject(i);
		  
		    String channel = jsonResults.getString("artist");
		  
			String title = jsonResults.getString("title");
			String artist = getArtist(title, channel);
			final String url = jsonResults.getString("link");
			final String id = jsonResults.getString("id");
			String duration = jsonResults.getString("duration");
			title = MediaUtils.cleanString(title);

			artist = MediaUtils.cleanString(artist);

			final String line = String.format("%s,%s,%s,%s,%s", title, artist,
					MediaUtils.calculateTime((int) Duration.parse(duration).getSeconds()), url, id);
			csv.append(line.trim());
			csv.append(System.getProperty("line.separator"));
		}

		return csv.toString();
	}
	public String getArtist(String title, String channel) {
		final Pattern pattern = Pattern.compile("^.*?(?=-)");
		final Matcher matcher = pattern.matcher(title);
		while (matcher.find()) {
			return matcher.group(0).trim();
		}
		final String artist = channel;

		return artist.trim();
	}
	@Override
	public void search() {
		if (Constants.IS_SEARCHING) {
			return;
		}
		Constants.IS_SEARCHING = true;
		String query = UISession.getSearchWidget().getSearchBar().getText();
		query = query.replace(" ", "%20");

			

		final YouTubeSearch api = new YouTubeSearch();

		final String searchJSON = api.getYoutubeJson(query);
		if (searchJSON.equals("[]")) {
			JOptionPane.showMessageDialog(UISession.getSearchWidget()
					.getWidget(), "No results found!", "Error",
					JOptionPane.ERROR_MESSAGE);
			Constants.IS_SEARCHING = false;
			return;
		}
		final String searchCSV = buildSearchCSV(searchJSON);
		Utils.writeFile(searchCSV, Constants.DATA_PATH
				+ "search/youtubecache.dat");
		ModelUtils.loadSearchResults(Constants.DATA_PATH
				+ "search/youtubecache.dat");
		Constants.IS_SEARCHING = false;

	}
}
