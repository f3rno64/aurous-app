package me.aurous.notifiers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import org.nikkii.alertify4j.Alertify;
import org.nikkii.alertify4j.AlertifyBuilder;
import org.nikkii.alertify4j.AlertifyType;

/**
 * @author Andrew
 *
 */
public class NotificationHandler {
	/**
	 *
	 */
	private static final long serialVersionUID = 491074731263378512L;



	public static void displayAlert(Icon icon, String message, AlertifyType type, int time)  {
		Alertify.show(new AlertifyBuilder().icon(icon).type(type)
				.text(message).autoClose(time).build());

	}
	public static void displayAlert(String message, AlertifyType type, int time) {
		Alertify.show(new AlertifyBuilder().type(type)
				.text(message).autoClose(time).build());

	}

}