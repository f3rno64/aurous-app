package me.aurous.apis.impl.webapi.search;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import me.aurous.utils.Internet;

public class SearchApi {

	public String submitQuery(final String searchEngine, final String parameters)
			throws IOException {
		return Internet.text(generateQuery(searchEngine, parameters));
	}

	public String generateQuery(final String searchEngine,
			final String parameter) {
		try {
			return String.format("https://aurous.me/api/search/%s/%s/",
					searchEngine, URLEncoder.encode(parameter, "UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return parameter;
	}
}
