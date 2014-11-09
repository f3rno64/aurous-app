package me.aurous.ui.menus;

import java.awt.Font;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import me.aurous.player.Settings;
import me.aurous.ui.frames.VisualizerFrame;
import me.aurous.ui.widgets.AboutWidget;
import me.aurous.ui.widgets.BuilderWidget;
import me.aurous.ui.widgets.DiscoWidget;
import me.aurous.ui.widgets.ImporterWidget;
import me.aurous.ui.widgets.SearchWidget;
import me.aurous.ui.widgets.SettingsWidget;
import me.aurous.utils.playlist.PlayListUtils;

public class AurousBar extends JMenuBar {

	/**
	 *
	 */
	private static final long serialVersionUID = -3829530752614750645L;
	private final JMenu fileMenu;
	private final JMenuItem exitMenuItem;

	public AurousBar() {
		setFont(new Font("Calibri", Font.PLAIN, 12));
		this.fileMenu = new JMenu("File");

		this.exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(e -> {
			Settings.saveSettings(false);
			System.exit(0);
		});

		final JMenuItem settingsItem = new JMenuItem("Settings");
		fileMenu.add(settingsItem);
		settingsItem.addActionListener(arg0 -> SettingsWidget.openSettings());

		final JMenuItem visualItem = new JMenuItem("Visual");
		fileMenu.add(visualItem);
		visualItem.addActionListener(arg0 -> VisualizerFrame.visualize());
		this.fileMenu.add(this.exitMenuItem);
		// add menus to menubar
		add(this.fileMenu);

		final JMenu playListMenu = new JMenu("Playlist");
		add(playListMenu);

		final JMenuItem buildPlayListOption = new JMenuItem("Build Playlist");
		buildPlayListOption.addActionListener(arg0 -> BuilderWidget
				.openBuilder());
		playListMenu.add(buildPlayListOption);

		final JMenuItem importPlayListOption = new JMenuItem("Import Playlist");
		importPlayListOption.addActionListener(arg0 -> ImporterWidget
				.openImporter());
		playListMenu.add(importPlayListOption);

		final JMenuItem importSingleItem = new JMenuItem(
				"Add to Current Playlist");
		importSingleItem.addActionListener(arg0 -> PlayListUtils
				.additionToPlayListPrompt());
		playListMenu.add(importSingleItem);

		final JMenu toolsMenu = new JMenu("Tools");
		add(toolsMenu);

		final JMenuItem searchItem = new JMenuItem("Search");
		searchItem.addActionListener(arg0 -> SearchWidget.openSearch());
		toolsMenu.add(searchItem);

		final JMenuItem discoItem = new JMenuItem("Disco Mode");
		discoItem.addActionListener(arg0 -> DiscoWidget.openDisco());
		//toolsMenu.add(discoItem);

		final JMenu helpMenu = new JMenu("Help");
		add(helpMenu);

		final JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(arg0 -> AboutWidget.showAbout());
		helpMenu.add(aboutItem);

	}

}
