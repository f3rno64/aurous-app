package me.aurous.services.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.swing.JOptionPane;

import me.aurous.services.PlaylistService;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Constants;
import me.aurous.utils.Utils;
import me.aurous.utils.playlist.PlayListUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;

public class AurousService extends PlaylistService {
	private final String playlistName;
	private final String SHARE_URL;
	private CloseableHttpClient httpClient;
	private final int NOT_FOUND = 404;

	public AurousService(final String playlistName, final String playlistID) {
		this.playlistName = playlistName;
		this.SHARE_URL = "https://aurous.me/api/playlist/share/" + playlistID;
	}

	@Override
	public void buildPlayList() {
		PlayListUtils.disableImporterInterface();
		grab();

	}

	@Override
	public void grab() {
		try {
			final BasicCookieStore cookieStore = new BasicCookieStore();

			final SSLContext sslContext = buildSSLContext();

			final SSLConnectionSocketFactory sslsf = buildSSLConnectionSocketFactory(sslContext);

			this.httpClient = buildHttpClient(cookieStore, sslsf);

			final String playList = fetchPlaylist(cookieStore, this.httpClient)
					.replace("<br />", "").trim();
			if (playList.equals("404")) {
				JOptionPane.showMessageDialog(null,
						"No playlist could be found.", "Error",
						JOptionPane.ERROR_MESSAGE);
				PlayListUtils.resetImporterInterface();
				return;
			}
			final String path = Constants.DATA_PATH + "playlist/"
					+ this.playlistName + ".plist";
			Utils.writeFile(playList, path);
			PlayListUtils.resetImporterInterface();
		} catch (KeyManagementException | NoSuchAlgorithmException
				| KeyStoreException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		} catch (final ClientProtocolException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		} catch (final URISyntaxException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		} catch (final IOException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		} finally {
			try {
				this.httpClient.close();
			} catch (final IOException e) {
				final ExceptionWidget eWidget = new ExceptionWidget(
						Utils.getStackTraceString(e, ""));
				eWidget.setVisible(true);
			}

		}
	}

	private static SSLConnectionSocketFactory buildSSLConnectionSocketFactory(
			final SSLContext sslcontext) {
		final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return sslsf;
	}

	private CloseableHttpClient buildHttpClient(
			final BasicCookieStore cookieStore,
			final SSLConnectionSocketFactory sslsf) {
		final CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore)
				.setRedirectStrategy(new LaxRedirectStrategy()).build();
		return httpclient;
	}

	private SSLContext buildSSLContext() throws NoSuchAlgorithmException,
	KeyManagementException, KeyStoreException {
		final SSLContext sslcontext = SSLContexts.custom().useTLS()
				.loadTrustMaterial(null, (chain, authType) -> true).build();
		return sslcontext;
	}

	private String fetchPlaylist(final BasicCookieStore cookieStore,
			final CloseableHttpClient httpclient) throws URISyntaxException,
			IOException, ClientProtocolException {

		final HttpUriRequest playlistGET = RequestBuilder.get()
				.setUri(new URI(this.SHARE_URL)).build();

		final CloseableHttpResponse fetchResponse = httpclient
				.execute(playlistGET);

		if (fetchResponse.getStatusLine().getStatusCode() == this.NOT_FOUND) {
			System.out.println(fetchResponse.getStatusLine());
			return "404";
		}

		try {
			final HttpEntity submitResponseEntity = fetchResponse.getEntity();

			final String playlistData = EntityUtils
					.toString(submitResponseEntity);
			EntityUtils.consume(submitResponseEntity);
			return playlistData;
		} finally {
			fetchResponse.close();
		}
	}
}
