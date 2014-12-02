package me.aurous;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import me.aurous.apis.impl.webapi.WebPlaylistSubmit;

public class test {

	public static void main(final String[] args) throws IOException {
		final String playListPath = "C:/Users/Andrew/.aurous_data/playlist/Awesome Mix Vol. 1.plist";
		final String playListName = stripExtension(new File(playListPath)
				.getName());
		final String content = new String(Files.readAllBytes(Paths
				.get(playListPath)));

		final WebPlaylistSubmit submit = new WebPlaylistSubmit(content,
				playListName);
		try {
			submit.submitPlaylist();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String stripExtension(final String s) {
		return (s != null) && (s.lastIndexOf(".") > 0) ? s.substring(0,
				s.lastIndexOf(".")) : s;
	}

}
