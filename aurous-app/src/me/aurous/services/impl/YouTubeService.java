package me.aurous.services.impl;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.aurous.player.Settings;
import me.aurous.services.PlaylistService;
import me.aurous.services.fetchers.impl.YouTubeFetcher;
import me.aurous.ui.UISession;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.ui.widgets.ImporterWidget;
import me.aurous.utils.Constants;
import me.aurous.utils.Internet;
import me.aurous.utils.Utils;
import me.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class YouTubeService extends PlaylistService {
	private String streamURL;
	private final String SITE_HTML;
	private final String contentURL;
	private final String PLAYER_VERSION_REGEX = "\\\\/\\\\/s.ytimg.com\\\\/yts\\\\/jsbin\\\\/html5player-(.+?)\\.js";
	private final String URL_ENCODE_REGEX = "\"url_encoded_fmt_stream_map\":\\s+\"(.+?)\"";
	private final String URL_STREAMS_REGEX = "(^url=|(\\\\u0026url=|,url=))(.+?)(\\\\u0026|,|$)";
	private final String STREAM_SIGNATURES_REGEX = "(^s=|(\\\\u0026s=|,s=))(.+?)(\\\\u0026|,|$)";

	public YouTubeService(final String contentURL) {
		this.contentURL = contentURL;
		this.SITE_HTML = Internet.text(this.contentURL);
	}

	@Override
	public void buildMediaLink() {
		grab();
	}
@Override
public void importPlayList(String playListName) {
	
		final Thread thread = new Thread() {
			@Override
			public void run() {

				try {
					if (contentURL.contains("playlist?")) {
						// fsyprint("Fetching %s...", url);
						String last = "";
						final String out = Constants.DATA_PATH + "playlist/"
								+ playListName + ".plist";
						final Document doc = Jsoup
								.connect(contentURL)
								.ignoreContentType(true)
								.userAgent(
										"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
										.referrer("http://www.google.com")
										.timeout(12000).followRedirects(true).get();
						final Elements links = doc.select("a[href]");
						final File playListOut = new File(out);
						final FileOutputStream fos = new FileOutputStream(
								playListOut);
						final BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(fos));
						final String header = "Title,Artist,Time,Date Added,User,Album,Art,Link";
						int iterations = 0;
						final ImporterWidget importWidget = UISession
								.getImporterWidget();
						bw.write(header);
						bw.newLine();
						for (final Element link : links) {
							if (((importWidget != null) && importWidget
									.isOpen())) {
								if ((importWidget != null)
										&& (importWidget.getImportProgressBar() != null)) {
									iterations += 1;

									final int percent = (int) ((iterations * 100.0f) / links
											.size());

									importWidget.getImportProgressBar()
									.setValue(percent);
									PlayListUtils.disableImporterInterface();
								}
								if (link.attr("abs:href").contains("watch?v=")
										&& (link.text().length() > 0)
										&& !link.text().contains("Play all")
										&& !link.text().contains("views")
										&& !link.attr("abs:href").equals(last)) {
									YouTubeFetcher youTubeFetcher = new YouTubeFetcher(link
													.attr("abs:href"), Internet.text(link
													.attr("abs:href")));
									final String mediaLine = youTubeFetcher.buildLine();
									if (!mediaLine.isEmpty()) {
										bw.write(mediaLine);
										bw.newLine();
										last = link.attr("abs:href");

									}

								}
							} else {

								bw.close();
								PlayListUtils.deletePlayList(out);
								if (importWidget != null) {
									PlayListUtils.resetImporterInterface();
								}
								return;
							}
						}
						bw.close();
						if (importWidget != null) {
							PlayListUtils.resetImporterInterface();
						}

					} else {
						JOptionPane.showMessageDialog(null,
								"Invalid URL Detected must contain playlist?",
								"Error", JOptionPane.ERROR_MESSAGE);
						if (UISession.getImporterWidget() != null) {
							PlayListUtils.resetImporterInterface();
						}
					}
				} catch (HeadlessException | IOException e) {

					if (UISession.getImporterWidget() != null) {
						PlayListUtils.resetImporterInterface();
					}
					final ExceptionWidget eWidget = new ExceptionWidget(
							Utils.getStackTraceString(e, ""));
					eWidget.setVisible(true);
				}
			}
		};
		thread.start();
}
	@Override
	public void grab() {
		String lowQualityMP4 = null;
		String highQualityMP4 = null;
		try {

			final List<String> list = extractURLS(this.SITE_HTML);

			for (final String url : list) {
				if (url.contains("itag=5")) {
					lowQualityMP4 = url;
				} else if (url.contains("itag=18")) {
					highQualityMP4 = url;
				}
			}
			if (Settings.isStreamLowQuality() == true) {
				this.streamURL = lowQualityMP4;
			}
			if (Utils.isNull(highQualityMP4)) {
				this.streamURL = lowQualityMP4;
			} else {
				this.streamURL = highQualityMP4;
			}

		} catch (final UnsupportedEncodingException e) {

			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		}
		this.streamURL = highQualityMP4;
	}

	public String getStreamURL() {
		return this.streamURL;
	}

	private List<String> extractURLS(final String html)
			throws UnsupportedEncodingException {

		final List<String> streams = new ArrayList<String>();
		final List<String> signatures = new ArrayList<String>();
		String playerVersion = "";
		Pattern pattern = Pattern.compile(PLAYER_VERSION_REGEX);
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			playerVersion = matcher.group(1).toString();
		}
		if (Utils.isNull(Constants.HTML5_PLAYER_CODE)) { // grab once so we
			// don't have to
			// pull it down each
			// time
			Constants.HTML5_PLAYER_CODE = Internet
					.text("http://s.ytimg.com/yts/jsbin/" + "html5player-"
							+ playerVersion.replace("\\", "") + ".js");
		}

		pattern = Pattern.compile(URL_ENCODE_REGEX);

		matcher = pattern.matcher(html);
		String unescapedHtml = "";
		while (matcher.find()) {

			unescapedHtml = matcher.group(1);

		}

		pattern = Pattern.compile(URL_STREAMS_REGEX);

		matcher = pattern.matcher(unescapedHtml);

		while (matcher.find()) {

			streams.add(URLDecoder.decode(matcher.group(3), "UTF-8"));

		}

		pattern = Pattern.compile(STREAM_SIGNATURES_REGEX);

		matcher = pattern.matcher(unescapedHtml);

		while (matcher.find()) {

			signatures.add(URLDecoder.decode(matcher.group(3), "UTF-8"));

		}
		final List<String> urls = new ArrayList<String>();

		for (int i = 0; i < (streams.size() - 1); i++) {
			String URL = streams.get(i).toString();

			if (signatures.size() > 0) {
				final String Sign = signDecipher(signatures.get(i).toString(),
						Constants.HTML5_PLAYER_CODE);
				URL += "&signature=" + Sign;

			}

			urls.add(URL.trim());

		}

		return urls;

	}

	private String signDecipher(final String signature, final String playercode) {
		try {

			final ScriptEngine engine = new ScriptEngineManager()
			.getEngineByName("nashorn");
			engine.eval(new FileReader(Constants.LEGACY_DATA_PATH
					+ "scripts/decrypt.js"));
			final Invocable invocable = (Invocable) engine;

			final Object result = invocable.invokeFunction("getWorkingVideo",
					signature, playercode);
			return (String) result;
		} catch (ScriptException | FileNotFoundException
				| NoSuchMethodException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		}
		return "error";
	}

}
