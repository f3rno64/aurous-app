package me.aurous.ui.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import me.aurous.player.Settings;
import me.aurous.player.functions.PlayerFunctions;
import me.aurous.ui.UISession;
import me.aurous.ui.widgets.ExceptionWidget;
import me.aurous.utils.Utils;
import me.aurous.utils.media.MediaUtils;

import com.alee.laf.button.WebButtonUI;

/**
 * @author Andrew
 *
 */
public class ControlPanel extends JPanel {
	private final DurationPanel durationPanel;

	private final Color background = new Color(35, 35, 35);

	private final JSlider volume;

	private final JLabel repeatStatusLabel;

	private final JLabel shuffleStatusLabel;

	private final JButton play;

	Font fontAwesome;

	/**
	 *
	 */
	private static final long serialVersionUID = 2823722567425016521L;

	public ControlPanel() throws IOException {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(background);
		setBorder(new EtchedBorder());
		final InputStream in = this.getClass().getResourceAsStream(
				"/resources/fontawesome-webfont.ttf");
		Font ttfBase = null;
		try {
			ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
		} catch (final FontFormatException e1) {
			final ExceptionWidget eWidget = new ExceptionWidget(
					Utils.getStackTraceString(e1, ""));
			eWidget.setVisible(true);
		}

		fontAwesome = ttfBase.deriveFont(Font.PLAIN, 24);
		setFont(fontAwesome);

		final JButton previous = new JButton("");
		((WebButtonUI) previous.getUI()).setUndecorated(true);
		previous.setFont(fontAwesome);
		previous.addActionListener(e -> PlayerFunctions.seekPrevious());
		previous.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		previous.setBorderPainted(false);
		previous.setBorder(null);
		previous.setText("\uF04A");

		previous.setMargin(new Insets(0, 0, 0, 0));
		previous.setContentAreaFilled(false);
		previous.addChangeListener(new ChangeListener() {
			private boolean rollover = false;

			@Override
			public void stateChanged(final ChangeEvent e) {
				final JButton btn = (JButton) e.getSource();
				final ButtonModel model = btn.getModel();
				if (model.isRollover() && !rollover) {
					btn.setForeground(Color.LIGHT_GRAY);
					rollover = true;
				} else if (rollover && !model.isRollover()) {
					btn.setForeground(Color.GRAY);
					rollover = false;
				}
			}
		});
		// previous.setIcon(new ImageIcon(previousButtonIcon));
		// previous.setRolloverIcon(new ImageIcon(previousRollOver));

		play = new JButton("");
		play.addActionListener(e -> {
			final boolean isPaused = PlayerFunctions.isPaused;
			if (!isPaused) {
				PlayerFunctions.pause(play);
			} else {
				PlayerFunctions.play(play);
			}
		});
		((WebButtonUI) play.getUI()).setUndecorated(true);
		play.setFont(fontAwesome);
		play.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		play.setBorderPainted(false);
		play.setBorder(null);
		play.setText("\uf04b");

		play.setMargin(new Insets(0, 0, 0, 0));
		play.setContentAreaFilled(false);
		play.addChangeListener(new ChangeListener() {
			private boolean rollover = false;

			@Override
			public void stateChanged(final ChangeEvent e) {
				final JButton btn = (JButton) e.getSource();
				final ButtonModel model = btn.getModel();
				if (model.isRollover() && !rollover) {
					btn.setForeground(Color.LIGHT_GRAY);
					rollover = true;
				} else if (rollover && !model.isRollover()) {
					btn.setForeground(Color.GRAY);
					rollover = false;
				}
			}
		});

		// play.setIcon(new ImageIcon(playBackPaused));
		// play.setRolloverIcon(new ImageIcon(playBackPausedHover));

		final JButton next = new JButton("");
		((WebButtonUI) next.getUI()).setUndecorated(true);
		next.addActionListener(e -> PlayerFunctions.seekNext());
		next.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		next.setBorderPainted(false);

		next.setBorder(null);
		next.setFont(fontAwesome);
		next.setMargin(new Insets(0, 0, 0, 0));
		next.setContentAreaFilled(false);
		next.setText("\uF04E");
		// next.setIcon(new ImageIcon(forwardButtonIcon));
		// next.setRolloverIcon(new ImageIcon(forwardButtonHover));
		next.setToolTipText("Next");
		next.addChangeListener(new ChangeListener() {
			private boolean rollover = false;

			@Override
			public void stateChanged(final ChangeEvent e) {
				final JButton btn = (JButton) e.getSource();
				final ButtonModel model = btn.getModel();
				if (model.isRollover() && !rollover) {
					btn.setForeground(Color.LIGHT_GRAY);
					rollover = true;
				} else if (rollover && !model.isRollover()) {
					btn.setForeground(Color.GRAY);
					rollover = false;
				}
			}
		});

		final JButton mute = new JButton("");
		((WebButtonUI) mute.getUI()).setUndecorated(true);
		mute.setFont(fontAwesome);
		mute.setText("\uF028");
		mute.addActionListener(e -> MediaUtils.muteToggle());
		mute.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mute.setBorderPainted(false);

		mute.setBorder(null);
		mute.setMargin(new Insets(0, 0, 0, 0));
		mute.setContentAreaFilled(false);
		mute.addChangeListener(new ChangeListener() {
			private boolean rollover = false;

			@Override
			public void stateChanged(final ChangeEvent e) {
				final JButton btn = (JButton) e.getSource();
				final ButtonModel model = btn.getModel();
				if (model.isRollover() && !rollover) {
					btn.setForeground(Color.LIGHT_GRAY);
					rollover = true;
				} else if (rollover && !model.isRollover()) {
					btn.setForeground(Color.GRAY);
					rollover = false;
				}
			}
		});

		volume = new JSlider() {
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

		volume.addChangeListener(listener -> {
			final double volumeLevel = volume().getValue() / 100D;
			if (UISession.getMediaPlayer() != null) {

				UISession.getMediaPlayer().setVolume(
						((double) volume.getValue() / 100));
				if (!MediaUtils.muted) {
					Settings.setVolume(volume.getValue());
				}

			}

			if (volumeLevel >= .66) {
				mute.setText("\uF028");
			} else if (volumeLevel >= .10) {
				mute.setText("\uF027");
			} else {
				mute.setText("\uF026");
			}
			System.out.println(volume.getValue());
		});

		volume.setValue(Settings.getVolume());
		volume.setPreferredSize(new Dimension(100, 25));
		volume.setMaximumSize(new Dimension(100, 25));
		mute.setPreferredSize(new Dimension(22, 25));
		mute.setMaximumSize(new Dimension(22, 25));

		add(Box.createRigidArea(new Dimension(10, 37)));
		add(previous);
		add(Box.createRigidArea(new Dimension(25, 0)));
		add(play);
		add(Box.createRigidArea(new Dimension(25, 0)));
		add(next);
		add(Box.createRigidArea(new Dimension(15, 0)));
		add(mute);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(volume);
		add(Box.createRigidArea(new Dimension(5, 0)));

		final JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setBackground(new Color(160, 160, 160));

		final Dimension size = new Dimension(1,
				separator.getMaximumSize().height);

		separator.setMaximumSize(size);

		add(separator);
		add(Box.createRigidArea(new Dimension(10, 0)));

		durationPanel = new DurationPanel();
		add(durationPanel);

		final JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
		final Dimension size1 = new Dimension(1,
				separator1.getMaximumSize().height);
		separator1.setMaximumSize(size1);
		add(separator1);
		add(Box.createRigidArea(new Dimension(10, 0)));

		shuffleStatusLabel = new JLabel("\uF074");
		shuffleStatusLabel.setFont(fontAwesome);
		shuffleStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		shuffleStatusLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent arg0) {
				if (shuffleStatusLabel.isEnabled()) {
					PlayerFunctions.handleSpecialLabels(false);
				}
			}
		});
		shuffleStatusLabel.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		shuffleStatusLabel.setForeground(Color.GRAY);

		repeatStatusLabel = new JLabel("\uF01E");
		repeatStatusLabel.setFont(fontAwesome);
		repeatStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

		repeatStatusLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent arg0) {
				if (repeatStatusLabel.isEnabled()) {
					PlayerFunctions.handleSpecialLabels(true);
				}

			}
		});
		repeatStatusLabel.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		repeatStatusLabel.setForeground(Color.GRAY);

		add(shuffleStatusLabel);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(repeatStatusLabel);
		add(Box.createRigidArea(new Dimension(5, 0)));

	}

	public JLabel current() {
		return durationPanel.current();
	}

	public JButton play() {
		return play;
	}

	public JLabel repeat() {
		return repeatStatusLabel;
	}

	public JSlider seek() {
		return durationPanel.seek();
	}

	public JLabel shuffle() {
		return shuffleStatusLabel;
	}

	public JLabel total() {
		return durationPanel.total();
	}

	public JSlider volume() {
		return volume;
	}

	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		super.paintComponent(g2d);
	}
}
