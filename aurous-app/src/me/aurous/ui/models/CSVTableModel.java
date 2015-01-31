package me.aurous.ui.models;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import me.aurous.utils.Utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Andrew
 *
 */
public class CSVTableModel {

	/**
	 *
	 *
	 * @param in
	 *            A CSV input stream to parse
	 * @param headers
	 *            A Vector containing the column headers. If this is null, it's
	 *            assumed that the first row contains column headers
	 *
	 * @return A DefaultTableModel containing the CSV values as type String
	 */
	public static DefaultTableModel createTableModel(final File playList,
			final Reader in, Vector<Object> headers) {
		Scanner s = null;
		StringBuilder convertedPlayList = null;
		final String jsonList = Utils.readFile(playList.getAbsolutePath(),
				Charset.defaultCharset());
		if (Utils.isJSONValid(jsonList)) { // if in new format, build to CSV for
			// now. Need to create a new table
			// model structure
			convertedPlayList = new StringBuilder(String.format(
					"Title,Artist,Time,Date Added,User,Album,Art,Link%s",
					System.lineSeparator()));

			final JSONArray json = new JSONArray(jsonList);
			for (int i = 0; i < json.length(); i++) {
				final JSONObject obj = json.getJSONObject(i);

				final String title = StringEscapeUtils.unescapeJava(obj
						.getString("Title").trim());
				final String artist = StringEscapeUtils.unescapeJava(obj
						.getString("Artist").trim());
				final String time = obj.getString("Time").trim();
				final String date = obj.getString("Date Added").trim();
				final String user = obj.getString("User").trim();
				final String album = obj.getString("Album").trim();
				final String album_art = obj.getString("Art").trim();
				final String link = obj.getString("Link").trim();
				convertedPlayList.append(String.format(
						"%s, %s, %s, %s, %s, %s, %s, %s %s", title, artist,
						time, date, user, album, album_art, link,
						System.lineSeparator()));

			}
			Utils.writeFile(convertedPlayList.toString(),
					playList.getAbsolutePath());

			s = new Scanner(new StringReader(convertedPlayList.toString()
					.trim()));
			try {
				in.close();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		DefaultTableModel model = null;

		try {
			try {
				final Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
				if (Utils.isNull(s)) {
					s = new Scanner(in);
				}
				if (!s.hasNext()) {
					return null;
				}

				while (s.hasNextLine()) {

					rows.add(new Vector<Object>(Arrays.asList(s.nextLine()
							.split("(?<!\\\\),".replace("\\,", ","), -1))));

				}

				if (Utils.isNull(headers)) {
					headers = rows.remove(0);
					model = new DefaultTableModel(rows, headers) {

						/**
						 *
						 */
						private static final long serialVersionUID = -611084662140321390L;

						@Override
						public boolean isCellEditable(final int row,
								final int column) {

							return false;
						}
					};
				} else {

					model = new DefaultTableModel(rows, headers) {

						/**
						 *
						 */
						private static final long serialVersionUID = -1929719310445045512L;

						@Override
						public boolean isCellEditable(final int row,
								final int column) {

							return false;
						}
					};
				}
				in.close();
				return model;
			} catch (final Exception e) {
				return null;
			}

		} finally {

			s.close();
		}
	}

	/**
	 *
	 * @param dtm
	 *            The DefaultTableModel to save to stream
	 * @param out
	 *            The stream to which to save
	 *
	 */
	public static void defaultTableModelToStream(final DefaultTableModel dtm,
			final Writer out) throws IOException {
		final String LINE_SEP = System.getProperty("line.separator");
		final int numCols = dtm.getColumnCount();
		final int numRows = dtm.getRowCount();

		// Write headers
		String sep = "";

		for (int i = 0; i < numCols; i++) {
			out.write(sep);
			out.write(dtm.getColumnName(i));
			sep = ",";
		}

		out.write(LINE_SEP);

		for (int r = 0; r < numRows; r++) {
			sep = "";

			for (int c = 0; c < numCols; c++) {
				out.write(sep);
				out.write(dtm.getValueAt(r, c).toString());
				sep = ",";
			}

			out.write(LINE_SEP);
		}
	}
}