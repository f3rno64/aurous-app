package me.aurous.notifiers;

import java.awt.Font;

import javax.swing.Icon;

import org.nikkii.alertify4j.Alertify;
import org.nikkii.alertify4j.AlertifyBuilder;
import org.nikkii.alertify4j.AlertifyType;

/**
 * @author Andrew This class handles building alerts for the Alertify4J library
 */
public class NotificationHandler {
	public static void displayAlert(final Font font, final Icon icon,
			final String message, final AlertifyType type, final int time) {
		Alertify.show(new AlertifyBuilder().font(font).icon(icon).type(type)
				.text(message).autoClose(time).build());

	}

	public static void displayAlert(final Font font, final String message,
			final AlertifyType type) {
		Alertify.show(new AlertifyBuilder().font(font).type(type).text(message)
				.build());

	}

	public static void displayAlert(final Font font, final String message,
			final AlertifyType type, final int time) {
		Alertify.show(new AlertifyBuilder().font(font).type(type).text(message)
				.autoClose(time).build());

	}

	public static void displayAlert(final Icon icon, final String message,
			final AlertifyType type) {
		Alertify.show(new AlertifyBuilder().icon(icon).type(type).text(message)
				.build());

	}

	public static void displayAlert(final Icon icon, final String message,
			final AlertifyType type, final int time) {
		Alertify.show(new AlertifyBuilder().icon(icon).type(type).text(message)
				.autoClose(time).build());
	}

	public static void displayAlert(final String message,
			final AlertifyType type) {
		Alertify.show(new AlertifyBuilder().type(type).text(message).build());

	}

	public static void displayAlert(final String message,
			final AlertifyType type, final int time) {
		Alertify.show(new AlertifyBuilder().type(type).text(message)
				.autoClose(time).build());

	}

}