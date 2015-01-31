package me.aurous.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import me.aurous.player.functions.PlayerFunctions;
import me.aurous.ui.UISession;

public class DurationPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 8242889578389979949L;
	public String current, maximum;
	public JSlider seek;

	private JLabel currentTime, maximumTime;

	public DurationPanel() {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.current = "0:00";
		this.maximum = "0:00";
		this.currentTime = new JLabel(this.current);
		this.currentTime.setFont(new Font("Calibri", Font.PLAIN, 13));
		this.currentTime.setForeground(Color.GRAY);
		this.maximumTime = new JLabel(this.maximum);
		this.maximumTime.setForeground(Color.GRAY);
		this.maximumTime.setFont(new Font("Calibri", Font.PLAIN, 13));

		this.seek = new JSlider() {
			private static final long serialVersionUID = -4931644654633925931L;

			{
				final MouseListener[] listeners = getMouseListeners();
				for (final MouseListener l : listeners) {
					removeMouseListener(l); // remove UI-installed TrackListener
				}
				final BasicSliderUI ui = (BasicSliderUI) getUI();
				final BasicSliderUI.TrackListener tl = ui.new TrackListener() {
					// this is where we jump to absolute value of click
					@Override
					public void mouseClicked(final MouseEvent e) {
						final Point p = e.getPoint();
						final int value = ui.valueForXPosition(p.x);

						setValue(value);

						PlayerFunctions.seek(value);
					}

					// disable check that will invoke scrollDueToClickInTrack
					@Override
					public boolean shouldScroll(final int dir) {
						return false;
					}
				};
				addMouseListener(tl);
			}
		};

		this.seek.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(final MouseEvent e) {
				if (UISession.getMediaPlayer() != null) {
					PlayerFunctions.seek(DurationPanel.this.seek.getValue());
				}
			}
		});
		this.seek.setPreferredSize(new Dimension(250, 25));
		setOpaque(false);
		add(this.currentTime);
		add(Box.createRigidArea(new Dimension(3, 0)));
		add(this.seek);
		add(Box.createRigidArea(new Dimension(3, 0)));
		add(this.maximumTime);
		add(Box.createRigidArea(new Dimension(5, 0)));
	}

	public JLabel current() {
		return this.currentTime;
	}

	public JSlider seek() {
		return this.seek;
	}

	public JLabel total() {
		return this.maximumTime;
	}

}
