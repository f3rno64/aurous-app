package me.aurous.notifiers;

import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Utils;

import com.skype.Skype;
import com.skype.SkypeException;

/**
 * @author Andrew This class handles updating the Skype status with the
 *         currently playing song.
 */

public class SkypeHandler {
	private final String artist;
	private final String title;

	public SkypeHandler(final String artist, final String title) {
		this.artist = artist;
		this.title = title;

	}

	/**
	 * Removes the skype status
	 */
	public void disposeSkypeSong() {
		try {

			Skype.getProfile().setRichMoodMessage("");
		} catch (final Exception e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		}
	}

	/**
	 * Sets the rich status of a skype user with the current song.
	 */
	public void setSkypeSong() {
		try {

			Skype.getProfile()
			.setRichMoodMessage(
					String.format(
							"<u><b><blink><font size=\"12px\"><font color=\"#232323\">♫ <strong>%s - %s</strong> ♫</font></font></blink></b></u> <strong> https://aurous.me/ - Listen to music instantly, for free! </strong>",
							this.title, this.artist));
		} catch (final Exception e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
		}
	}

	/**
	 * Checks if skype is running.
	 */
	public boolean isRunning() {
		try {
			return Skype.isRunning();
		} catch (final SkypeException e) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e, ""));
			eWidget.setVisible(true);
			return false;
		}

	}

	/**
	 * Checks if skype is installed.
	 */
	public boolean isInstalled() {
		return Skype.isInstalled();
	}
}
