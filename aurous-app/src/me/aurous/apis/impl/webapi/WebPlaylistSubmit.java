package me.aurous.apis.impl.webapi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

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
import org.json.JSONException;
import org.json.JSONObject;

public class WebPlaylistSubmit {

	private static SSLConnectionSocketFactory buildSSLConnectionSocketFactory(
			final SSLContext sslcontext) {
		final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return sslsf;
	}

	private final String decodedContent;
	private final String playlistName;
	private final String SUBMIT_URL;
	private final String playlistCover;
	private String SUBMIT_MESSAGE;
	private boolean VALID_SUBMIT = false;
	private final String AUTH_TYPE = "submit_list";
	private final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

	private CloseableHttpClient httpClient;

	public WebPlaylistSubmit(final String decodedContent,
			final String playlistName) {
		this.decodedContent = decodedContent;
		this.playlistName = playlistName;
		this.SUBMIT_URL = "https://aurous.me/api/playlist/";
		this.playlistCover = getPlaylistCover();
		this.SUBMIT_MESSAGE = "";

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

	private String getPlaylistCover() {
		final String[] lines = this.decodedContent.split(System
				.getProperty("line.separator"));
		return lines[1].split(",")[6].trim();
	}

	private String executeShare(final BasicCookieStore cookieStore,
			final CloseableHttpClient httpclient) throws URISyntaxException,
			IOException, ClientProtocolException {

		final HttpUriRequest submitPost = RequestBuilder.post()
				.setUri(new URI(this.SUBMIT_URL))
				.addParameter("auth_type", this.AUTH_TYPE)
				.addParameter("playlist_content", this.decodedContent)
				.addParameter("playlist_name", this.playlistName)
				.addParameter("playlist_cover", this.playlistCover)
				.addHeader("User-Agent", this.USER_AGENT).build();

		final CloseableHttpResponse submitResponse = httpclient
				.execute(submitPost);
		try {
			final HttpEntity submitResponseEntity = submitResponse.getEntity();
			final String jsonData = EntityUtils.toString(submitResponseEntity);
			final JSONObject json = new JSONObject(jsonData);
			this.SUBMIT_MESSAGE = json.getString("message");
			int successcode = -1;

			try {
				successcode = json.getInt("successcode");
			} catch (final JSONException e) {
				this.VALID_SUBMIT = false;
				return this.SUBMIT_MESSAGE;
			}

			if (successcode == 200) {
				this.VALID_SUBMIT = true;
			}
			EntityUtils.consume(submitResponseEntity);
		} finally {
			submitResponse.close();
		}

		return this.SUBMIT_MESSAGE;
	}

	public boolean isValidSubmit() {
		return this.VALID_SUBMIT;
	}

	public String getSubmitMessage() {
		return this.SUBMIT_MESSAGE;
	}

	public void submitPlaylist() throws Exception {
		final BasicCookieStore cookieStore = new BasicCookieStore();

		final SSLContext sslContext = buildSSLContext();

		final SSLConnectionSocketFactory sslsf = buildSSLConnectionSocketFactory(sslContext);

		this.httpClient = buildHttpClient(cookieStore, sslsf);

		try {

			executeShare(cookieStore, this.httpClient);

		} finally {
			this.httpClient.close();

		}

	}
}
