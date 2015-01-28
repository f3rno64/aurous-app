package me.aurous.apis.impl.webapi.search.impl;

import java.io.IOException;

import me.aurous.apis.impl.webapi.search.SearchApi;

public class YouTubeSearch extends SearchApi {

	
	public String getYoutubeJson(final String paramaters) {
		try {
			return submitQuery("youtube", paramaters);
		} catch (IOException e) {
		}
		return paramaters;
	}
}
