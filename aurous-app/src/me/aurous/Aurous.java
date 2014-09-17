package me.aurous;

import java.awt.Color;
import java.awt.EventQueue;

import me.aurous.ui.UISession;
import me.aurous.ui.frames.AurousFrame;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.progressbar.WebProgressBarStyle;
import com.alee.laf.scroll.WebScrollBarStyle;

/**
 * @author Andrew
 *
 */
public class Aurous {

	public static void main(final String[] args) {
		WebLookAndFeel.install();
		configureWebLAF();
		EventQueue.invokeLater(() -> {
			try {
				final AurousFrame window = new AurousFrame();
				UISession.setPresenter(window);
				window.aurousFrame.setVisible(true);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}

	private static void configureWebLAF() {

		WebProgressBarStyle.bgBottom = (new Color(34, 35, 39));
		WebProgressBarStyle.progressTopColor = Color.pink;
		WebProgressBarStyle.progressBottomColor = Color.YELLOW;
		WebScrollBarStyle.paintTrack = true;

		// WebLookAndFeel.setDecorateFrames(true);
		// WebLookAndFeel.setDecorateDialogs(true); //not working on linux

	}
}
