package me.aurous.services.fetchers.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.aurous.player.Settings;
import me.aurous.services.fetchers.ServiceFetcher;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YouTubeFetcher extends ServiceFetcher {

	private String contentURL;
	private String contentData;
	private String videoID;
	private String title;
	private static String YOUTUBE_REGEX = "^[^v]+v=(.{11}).*";

	private static String API_URL = "http://gdata.youtube.com/feeds/api/videos/";

	public YouTubeFetcher(final String contentURL, final String contentData) {
		this.contentURL = contentURL;
		this.contentData = contentData;
	}

	private String formatYouTubeURL(final String id) {

		final String url = "https://www.youtube.com/watch?v=%s";
		final String rebuiltURL = String.format(url, id);

		return rebuiltURL;
	}

	/**
	 * @author Andrew
	 *
	 *         gets a YouTube video ID from a url
	 *
	 */
	public static String getYouTubeID(final String url) {
		if (url.contains("embed")) {
			return url.substring(url.lastIndexOf("/") + 1);
		}
		final String id = "";

		final Pattern pattern = Pattern.compile(YOUTUBE_REGEX);
		final Matcher matcher = pattern.matcher(url);

		if (matcher.matches()) {

			return matcher.group(1);
		}

		return id;
	}

	/**
	 * @author Andrew
	 *
	 *         Gets remote JSON via the GDATA Api which should never time out
	 *         since no key is needed
	 *
	 */
	@Override
	public String getVideoData() {
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		final String gData = String.format("%s%s%s", API_URL, this.videoID,
				"?alt=json");
		if (gData.equals("http://gdata.youtube.com/feeds/api/videos/?alt=json")) {
			return "";
		}
		// System.out.println(gData);
		try {
			url = new URL(gData);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (final Exception e) {
			return "";
		}
		return result;
	}

	/**
	 * @author Andrew
	 *
	 *         Pulls the title of the video down from JSON
	 *
	 */
	@Override
	public String getSongTitle() {
		String title = "";
		try {
			final JSONObject json = new JSONObject(this.contentData);
			final JSONObject dataObject = json.getJSONObject("entry");
			final JSONObject mediaGroup = dataObject
					.getJSONObject("media$group");
			final JSONObject media_title = mediaGroup
					.getJSONObject("media$title");
			title = media_title.getString("$t");

			title = MediaUtils.cleanString(title);
			return title;
		} catch (final JSONException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		}

		return title;
	}

	/**
	 * @author Andrew
	 *
	 *         Extracts the artist from the video title
	 *
	 */
	private String extractTitle() {
		final Pattern pattern = Pattern.compile("\\-(.*)$");
		final Matcher matcher = pattern.matcher(this.title);
		while (matcher.find()) {
			return matcher.group(1).trim();
		}
		return this.title.trim();
	}

	/**
	 * @author Andrew
	 *
	 *         Gets the total seconds a youtube video last
	 *
	 */
	@Override
	public String getDuration() {
		final String duration = "";
		try {
			final JSONObject json = new JSONObject(this.contentData);
			final JSONObject dataObject = json.getJSONObject("entry");
			final JSONObject mediaGroup = dataObject
					.getJSONObject("media$group");
			final JSONObject yt_duration = mediaGroup
					.getJSONObject("yt$duration");
			final int total_duration_seconds = Integer.parseInt(yt_duration
					.getString("seconds"));
			return MediaUtils.calculateTime(total_duration_seconds);
		} catch (final JSONException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
			return duration;
		}

	}

	/**
	 * @author Andrew
	 *
	 *         Formats the id into the YouTube cover image format as highest
	 *         resolution possible. If format changes will get from the JSON
	 */
	@Override
	public String getAlbumArt() {
		final String coverArt;
		if (getStatusCode("http://i.ytimg.com/vi/" + this.videoID
				+ "/maxresdefault.jpg") != 404) {
			coverArt = String
					.format("https://i.ytimg.com/vi/%s/maxresdefault.jpg",
							this.videoID);
		} else {
			coverArt = String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg",
					this.videoID);
		}

		return coverArt;
	}

	@Override
	public String buildLine() {
		if (!this.contentURL.startsWith("http")) {
			return "";
		}
		final String id = getYouTubeID(this.contentURL);
		this.videoID = id;
		this.contentURL = formatYouTubeURL(id);

		this.contentData = getVideoData();

		if (this.contentData.isEmpty()) {
			return "";
		}
		final String thumbNail = getAlbumArt();
		this.title = getSongTitle();
		final String artist = (getArtist());
		final String songTitle = (extractTitle());
		final String duration = getDuration();
		final String date = Utils.getDate();
		final String user = Settings.getUserName();
		final String line = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
				songTitle, artist, duration, date, user, "", thumbNail,
				this.contentURL);
		return line;
	}

	/**
	 * @author Andrew
	 *
	 *         Extracts the artist from the video title
	 *
	 */
	@Override
	public String getArtist() {
		final Pattern pattern = Pattern.compile("^.*?(?=-)");
		final Matcher matcher = pattern.matcher(this.title);
		while (matcher.find()) {
			return matcher.group(0).trim();
		}
		final String artist = getUploader();

		return artist.trim();
	}

	/**
	 * @author Andrew
	 *
	 *         Gets the total seconds a youtube video last
	 *
	 *
	 */
	@Override
	public String getUploader() {
		final String uploader = "";
		try {
			final JSONObject json = new JSONObject(this.contentData);
			final JSONObject dataObject = json.getJSONObject("entry");
			final JSONArray author = dataObject.getJSONArray("author");
			final JSONObject userInfo = author.getJSONObject(0);
			final JSONObject userNameObject = userInfo.getJSONObject("name");
			final String username = userNameObject.getString("$t");

			return username;
		} catch (final JSONException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
			return uploader;
		}

	}

	@Override
	public int getStatusCode(final String url) {
		try {
			final URL u = new URL(url);
			final HttpURLConnection huc = (HttpURLConnection) u
					.openConnection();
			huc.setRequestMethod("GET"); // OR huc.setRequestMethod ("HEAD");
			huc.connect();
			final int code = huc.getResponseCode();
			return code;
		} catch (final IOException e) {
			return 400;
		}

	}

}
