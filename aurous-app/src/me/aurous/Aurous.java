package me.aurous;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import me.aurous.ui.UISession;
import me.aurous.ui.frames.AurousFrame;
import me.aurous.utils.Constants;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.WebMenuBarStyle;
import com.alee.laf.menu.WebMenuItemStyle;
import com.alee.laf.progressbar.WebProgressBarStyle;
import com.alee.laf.scroll.WebScrollBarStyle;
import com.alee.laf.separator.WebSeparatorStyle;
import com.alee.laf.slider.WebSliderStyle;
import com.alee.laf.table.WebTableStyle;
import com.alee.utils.FileUtils;

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

	private static boolean containsLine(final String fileName,
			final String lineStop) {
		try {
			final Scanner s = new Scanner(System.in);

			final File f = new File(fileName);
			final Scanner numScan = new Scanner(f);

			String line;

			while (numScan.hasNext()) {
				line = numScan.nextLine();
				if (line.contains(lineStop)) {

					s.close();
					numScan.close();
					return true;
				}
			}
			s.close();
			numScan.close();
			return false;
		} catch (final FileNotFoundException e) {
			return true;
		}
	}

	public static void main(final String[] args) {

		configureWebLAF();
		setup();
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

	private static void setup() {
		if (!containsLine("Aurous.ini", "Virtual Machine Parameters")) {
			try {

				final List<String> lines = Files.readAllLines(
						Paths.get("Aurous.ini"), StandardCharsets.UTF_8);
				lines.add(
						11,
						"Virtual Machine Parameters=-XX:MinHeapFreeRatio=40 -XX:MaxHeapFreeRatio=70 -XX:MaxMetaspaceSize=128m -Xms3670k -Xmx256m -Dsun.java2d.noddraw=true -XX:+UseParallelGC");
				Files.write(Paths.get("Aurous.ini"), lines,
						StandardCharsets.UTF_8);
				JOptionPane
				.showMessageDialog(
						null,
						"Aurous has modified some core settings, application will exit. Please restart.",
						"Don't worry!", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final File data_path = new File(Constants.DATA_PATH);
		if (data_path.exists()) {
			// empty
		} else {
			final boolean success = data_path.mkdirs();
			if (!success) {
				JOptionPane
						.showMessageDialog(
								null,
								"Unable to create data folder, try running as admin. Program will exit",
								"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			} else {
				// move files to new data folder
				final File source = new File("data");
				FileUtils.copyDirectory(source, data_path);
			}
		}
	}
}
