package me.aurous.utils.playlist;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlaylistManager {

	public static void writePlaylist() { // send csv string, write from there
		final JSONObject object = new JSONObject();
		object.put("totalSongs", "2");
		object.put("totalTime", "420");
		object.put("playListName", "The Playlist");
		object.put("uploader", "Andrew");
		object.put("playListCover", "http://aurous.me/api/covers/x365j");

		final JSONArray songs = new JSONArray();
		for (int j = 0; j < 15; j++) {
			final JSONArray songData = new JSONArray();
			// songData.put("Backstreet");
			// songData.put("stuff");
			// songData.put("fuc241q52tk");
			// songData.put("fuc525626k");
			songs.put(songData);
		}

		object.put("songs", songs);

		System.out.println(object.toString());

	}

	public static void addToPlaylist() {

		/*
		 * JSONArray addToArray = object.getJSONArray("songs"); JSONArray
		 * songData2 = new JSONArray(); songData2.put("Ba252521562ckstreet");
		 * songData2.put("stu62626ff"); songData2.put("fuc262241q52tk");
		 * songData2.put("fuc525626k"); addToArray.put(songData2);
		 * 
		 * object.put("totalSongs", "3");
		 */
		// System.out.println(object.toString());
	}

	public static void readPlaylist() {

	}
}
