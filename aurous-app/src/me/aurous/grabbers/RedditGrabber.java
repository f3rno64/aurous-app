package me.aurous.grabbers;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

import me.aurous.ui.UISession;
import me.aurous.utils.Constants;
import me.aurous.utils.media.MediaUtils;
import me.aurous.utils.playlist.PlayListUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RedditGrabber extends AurousGrabber {
	private final String COMMENT_QUERY = "?limit=500"; // is it 500 or 1000?
	private String contentURL;
	private final String playListName;
	private final String TAG_TYPE = "a[href]";
	private final String ATTRIBUTE_TYPE = "abs:href";

	public RedditGrabber(final String contentURL, final String playListName) {
		this.contentURL = contentURL;
		this.playListName = playListName;

	}

	@Override
	public void buildPlayList() {
		if (contentURL.contains("comments")) {
			contentURL = addQueryToURL(contentURL);
		}

		grab();

	}

	@Override
	public void grab() {
		final Thread thread = new Thread(
				() -> {

					try {
						if (contentURL.contains("reddit")) {
							// print("Fetching %s...", url);
							String last = "";
							final String out = Constants.DATA_PATH
									+ "playlist/" + playListName + ".plist";
							final Document doc = Jsoup
									.connect(contentURL)
									.ignoreContentType(true)
									.userAgent(
											"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
									.referrer("http://www.google.com")
									.timeout(12000).followRedirects(true).get();
							final Elements links = doc.select(TAG_TYPE);
							final File playListOut = new File(out);
							final FileOutputStream fos = new FileOutputStream(
									playListOut);
							final BufferedWriter bw = new BufferedWriter(
									new OutputStreamWriter(fos));
							final String header = "Title, Artist, Time, Date Added, User, Album, ALBUMART_INDEX, link";
							bw.write(header);
							bw.newLine();
							int iterations = 0;
							for (final Element link : links) {
								if (UISession.getImporterWidget().isOpen()) {
									if (UISession.getImporterWidget()
											.getImportProgressBar() != null) {

										iterations += 1;

										final int percent = (int) ((iterations * 100.0f) / links
												.size());
										UISession.getImporterWidget()
										.getImportProgressBar()
										.setValue(percent);
										PlayListUtils
										.disableImporterInterface();
									}
									if (!link.attr(ATTRIBUTE_TYPE).equals(last)) {

										final String mediaLine = MediaUtils
												.getBuiltString(link
														.attr(ATTRIBUTE_TYPE));
										if (!mediaLine.isEmpty()) {
											bw.write(mediaLine);
											bw.newLine();
											last = link.attr(ATTRIBUTE_TYPE);
										}

									}
								} else {

									bw.close();
									PlayListUtils.deletePlayList(out);
									if (UISession.getImporterWidget()
											.getImportProgressBar() != null) {
										PlayListUtils.resetImporterInterface();
									}
									return;
								}
							}
							bw.close();
							if (UISession.getImporterWidget()
									.getImportProgressBar() != null) {
								PlayListUtils.resetImporterInterface();
							}

						} else {
							JOptionPane.showMessageDialog(null,
									"Invalid URL Detected, is this reddit?",
									"Error", JOptionPane.ERROR_MESSAGE);
							if (UISession.getImporterWidget()
									.getImportProgressBar() != null) {
								PlayListUtils.resetImporterInterface();
							}
						}
					} catch (HeadlessException | IOException e) {
						JOptionPane.showMessageDialog(null,
								"HTTP client error, please try again.",
								"Error", JOptionPane.ERROR_MESSAGE);
						if (UISession.getImporterWidget()
								.getImportProgressBar() != null) {
							PlayListUtils.resetImporterInterface();
						}
						// e.printStackTrace();
					}
				});
		thread.start();
	}

	private String addQueryToURL(final String url) {
		final String commentQueryURL = String.format(url + "%s", COMMENT_QUERY);
		return commentQueryURL;
	}
}
