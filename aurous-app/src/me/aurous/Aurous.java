package me.aurous;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import me.aurous.ui.UISession;
import me.aurous.ui.frames.AurousFrame;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;

/**
 * @author Andrew
 *
 */
public class Aurous {

	public static void main(final String[] args) {
		configureSynthetica();
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

	private static void configureSynthetica() {

		try {
			System.setProperty("swing.aatext", "true");

			UIManager.put("Synthetica.popupMenu.blur.enabled", false);
			UIManager.put("Synthetica.dialog.icon.enabled", true);
			SyntheticaLookAndFeel.setWindowsDecorated(true); // setting for
																// false allow
																// for areo
																// stuff

			UIManager
			.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel");
			SyntheticaLookAndFeel.setFont("Dialog", 12);

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
