package me.aurous.player.functions;

import java.awt.Color;
import java.util.Random;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;

import me.aurous.ui.UISession;
import me.aurous.ui.panels.TabelPanel;
import me.aurous.utils.media.MediaUtils;

/**
 * @author Andrew A class that contains player functions
 */
public class PlayerFunctions {

	/**
	 * Handle the shuffle/repeat buttons
	 */
	public static void handleSpecialLabels(final boolean isRepeat) {
		final JLabel repeatStatusLabel = UISession.getControlPanel().repeat();
		final JLabel shuffleStatusLabel = UISession.getControlPanel().shuffle();
		if (isRepeat) {
			if (repeatStatusLabel.isEnabled()) {
				if (repeat == true) {
					repeat = false;
					repeatStatusLabel.setForeground(Color.GRAY);
					shuffleStatusLabel.setEnabled(true);

				} else {
					repeat = true;
					shuffle = false;
					shuffleStatusLabel.setEnabled(false);
					repeatStatusLabel.setForeground(Color.WHITE);
					shuffleStatusLabel.setForeground(Color.GRAY);
				}
			}
		} else {
			if (shuffleStatusLabel.isEnabled()) {
				if (shuffle == true) {
					shuffle = false;
					shuffleStatusLabel.setForeground(Color.GRAY);
					repeatStatusLabel.setEnabled(true);

				} else {
					shuffle = true;
					repeat = false;
					repeatStatusLabel.setEnabled(false);
					shuffleStatusLabel.setForeground(Color.WHITE);
					repeatStatusLabel.setForeground(Color.GRAY);
				}
			}
		}
	}

	/**
	 * Pause the current song and set the state.
	 */
	public static void pause(final JButton mediaStateButton) {

		if (UISession.getMediaPlayer() != null) {
			mediaStateButton.setText("\uf04b");

			UISession.getMediaPlayer().pause();
			isPaused = true;
			mediaStateButton.setToolTipText("Play");
		}

	}

	/**
	 * Unpause the current song and set the state.
	 */
	public static void play(final JButton mediaStateButton) {

		if (UISession.getMediaPlayer() != null) {

			mediaStateButton.setText("\uF04C");
			UISession.getMediaPlayer().play();
			isPaused = false;
			mediaStateButton.setToolTipText("Pause");

		} else {
			mediaStateButton.setText("\uF04C");
			mediaStateButton.setToolTipText("Pause");
		}

	}

	/**
	 * Repeat a song
	 */
	public static void repeat() {
		final JTable table = TabelPanel.table;
		if (table != null) {

			final int index = table.getSelectedRow();
			table.setRowSelectionInterval(0, index);
			MediaUtils.switchMedia(table);

		} else {

		}

	}

	/**
	 * Seek a time on the player.
	 */
	public static void seek(final int value) {
		final MediaPlayer player = UISession.getMediaPlayer();
		if (player != null) {
			final double d = value / 100D;
			player.seek(Duration.millis(player.getMedia().getDuration()
					.toMillis()
					* d));
		}
	}

	/**
	 * Skip forward one song.
	 */
	public static void seekNext() {
		final JTable table = TabelPanel.table;
		if (table != null) {
			final int total = table.getRowCount();
			final int idx = table.getSelectedRow();
			if (total == 0) {
				return;
			} else if ((idx == -1) && (total == 0)) {
				return;
			} else if ((idx == -1) && (total > 0)) {
				table.setRowSelectionInterval(0, 0);
				MediaUtils.switchMedia(table);

			} else if ((idx + 1) == total) {
				table.setRowSelectionInterval(0, 0);
				MediaUtils.switchMedia(table);

			} else {

				try {
					table.setRowSelectionInterval(0, idx + 1);
					MediaUtils.switchMedia(table);
				} catch (final Exception e) {

					table.setRowSelectionInterval(0, 0);
					MediaUtils.switchMedia(table);
				}

			}
		}

	}

	/**
	 * Skip back one song.
	 */
	public static void seekPrevious() {
		final JTable table = TabelPanel.table;
		if (table != null) {
			final int total = table.getRowCount();
			final int idx = table.getSelectedRow();
			if (total == 0) {
				return;
			} else if ((idx == -1) && (total == 0)) {
				return;
			} else if ((idx == -1) && (total > 0)) {
				table.setRowSelectionInterval(0, total - 1);
				MediaUtils.switchMedia(table);

			} else if (idx <= 0) {
				table.setRowSelectionInterval(0, total - 1);
				MediaUtils.switchMedia(table);

			} else {
				try {
					table.setRowSelectionInterval(0, idx - 1);
					MediaUtils.switchMedia(table);
				} catch (final Exception e) {
					table.setRowSelectionInterval(0, 0);
					MediaUtils.switchMedia(table);
				}

			}
		}
	}

	/**
	 * Plays a random song.
	 */
	public static void shuffle() {

		final JTable table = TabelPanel.table;
		final int totalIndexs = table.getRowCount();
		final int randomIndex = new Random().nextInt(totalIndexs);
		table.setRowSelectionInterval(0, randomIndex);
		MediaUtils.switchMedia(table);

	}

	public boolean switching = false;

	public static boolean isPaused;

	public static boolean repeat = false;

	public static boolean shuffle = false;

}
