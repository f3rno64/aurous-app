package me.aurous;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;

import me.aurous.ui.UISession;
import me.aurous.ui.frames.AurousFrame;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.WebMenuBarStyle;
import com.alee.laf.menu.WebMenuItemStyle;
import com.alee.laf.progressbar.WebProgressBarStyle;
import com.alee.laf.scroll.WebScrollBarStyle;
import com.alee.laf.separator.WebSeparatorStyle;
import com.alee.laf.slider.WebSliderStyle;
import com.alee.laf.table.WebTableStyle;

/**
 * @author Andrew
 *
 */
public class Aurous {

	private static void configureWebLAF() {
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true"); // enable System aa

		/* Editing constants */
		StyleConstants.backgroundColor = (new Color(35, 35, 35));
		StyleConstants.darkBackgroundColor = (new Color(35, 35, 35));
		StyleConstants.shadeColor = new Color(35, 35, 35);
		StyleConstants.fieldFocusColor = new Color(35, 35, 35);
		StyleConstants.darkBackgroundColor = new Color(35, 35, 35);
		StyleConstants.textColor = Color.GRAY;
		StyleConstants.borderColor = Color.DARK_GRAY;

		WebScrollBarStyle.buttonsSize = new Dimension(10, 10);

		WebProgressBarStyle.bgBottom = (new Color(34, 35, 39));
		WebProgressBarStyle.progressTopColor = Color.pink;
		WebProgressBarStyle.progressBottomColor = Color.YELLOW;

		// WebLookAndFeel.setDecorateFrames(true);
		// WebLookAndFeel.setDecorateDialogs(true); //not working on linux

		WebSliderStyle.drawThumb = false;
		WebSliderStyle.trackBgBottom = new Color(45, 45, 45);
		WebSliderStyle.trackBgTop = new Color(45, 45, 45);
		WebSliderStyle.thumbBgTop = new Color(110, 112, 114); // also sets
		// progress
		// color
		WebSliderStyle.trackRound = 0;

		WebTableStyle.background = (new Color(35, 35, 35));
		WebTableStyle.foreground = Color.GRAY;
		WebTableStyle.cellsSpacing = new Dimension(5, 5);
		WebTableStyle.gridColor = (new Color(35, 35, 35));

		WebMenuBarStyle.undecorated = true;
		WebMenuBarStyle.borderColor = (new Color(35, 35, 35));

		WebMenuItemStyle.selectedBottomBg = (new Color(35, 35, 35));
		WebMenuItemStyle.selectedTopBg = (new Color(35, 35, 35));

		StyleConstants.separatorLightUpperColor = Color.DARK_GRAY;
		StyleConstants.separatorLightColor = Color.DARK_GRAY;
		StyleConstants.separatorUpperColor = Color.DARK_GRAY;
		StyleConstants.separatorColor = Color.DARK_GRAY;
		WebSeparatorStyle.margin = new Insets(0, -1, 0, 0);

	}

	public static void main(final String[] args) {
		configureWebLAF();
		WebLookAndFeel.install();

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
}
