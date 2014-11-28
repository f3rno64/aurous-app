package me.aurous.grabbers;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import me.aurous.ui.UISession;
import me.aurous.utils.Constants;
import me.aurous.utils.media.MediaUtils;
import me.aurous.utils.playlist.PlayListUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Andrew
 *
 */
public class HateChanGrabber {

	public static void buildHatePlaylist(final String url,
			final String playListName) {

		scrapeThread(url, playListName);

	}

	private static void scrapeThread(final String url, final String playListName) {
		final Thread thread = new Thread() {
			@Override
			public void run() {

				try {
					if (Pattern
							.compile("8chan.co\\/(.*?)\\/(.*?)\\/(.*?).html")
							.matcher(url).find()) {
						// print("Fetching %s...", url);
						String last = "";
						final String out = Constants.DATA_PATH + "playlist/"
								+ playListName + ".plist";
						final Document doc = Jsoup.connect(url).get();
						final Elements links = doc.select("iframe");

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
									PlayListUtils.disableImporterInterface();
								}

								if (!link.getElementsByTag("iframe")
										.attr("src").equals(last)) {

									final String mediaLine = MediaUtils
											.getBuiltString(link
													.getElementsByTag("iframe")
													.attr("src"));
									if (!mediaLine.isEmpty()) {
										bw.write(mediaLine);
										bw.newLine();
										last = link.getElementsByTag("iframe")
												.attr("src");
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
						JOptionPane
								.showMessageDialog(
										null,
										"Invalid URL Detected, make sure it is an 8chan thread.",
										"Error", JOptionPane.ERROR_MESSAGE);
						if (UISession.getImporterWidget()
								.getImportProgressBar() != null) {
							PlayListUtils.resetImporterInterface();
						}
					}
				} catch (HeadlessException | IOException e) {
					JOptionPane.showMessageDialog(null,
							"HTTP client error, please try again.", "Error",
							JOptionPane.ERROR_MESSAGE);
					if (UISession.getImporterWidget().getImportProgressBar() != null) {
						PlayListUtils.resetImporterInterface();
					}
					// e.printStackTrace();
				}
			}
		};
		thread.start();

	}

}
