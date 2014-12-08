package me.aurous.services.impl;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import me.aurous.services.PlaylistService;
import me.aurous.ui.UISession;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Constants;
import me.aurous.utils.Utils;
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
public class HateChanService extends PlaylistService {

	private final String contentURL;
	private final String playListName;
	private final String TAG_TYPE = "a[href]";
	private final String ATTRIBUTE_TYPE = "abs:href";

	public HateChanService(final String contentURL, final String playListName) {
		this.contentURL = contentURL;
		this.playListName = playListName;

	}

	@Override
	public void buildPlayList() {
		grab();

	}

	@Override
	public void grab() {
		final Thread thread = new Thread(
				() -> {

					try {
						if (Pattern
								.compile(
										"8chan.co\\/(.*?)\\/(.*?)\\/(.*?).html")
										.matcher(contentURL).find()) {
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
							final String header = "Title,Artist,Time,Date Added,User,Album,Art,Link";
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
						final ExceptionWidget eWidget = new ExceptionWidget(
								Utils.getStackTraceString(e, ""));
						eWidget.setVisible(true);
						if (UISession.getImporterWidget()
								.getImportProgressBar() != null) {
							PlayListUtils.resetImporterInterface();
						}
					}
				});
		thread.start();

	}

}
