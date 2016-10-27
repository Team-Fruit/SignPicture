package com.kamesuta.mc.signpic.gui.file;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.kamesuta.mc.signpic.lib.ComponentResizer;

public class GuiUpload {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final GuiUpload window = new GuiUpload();
					window.frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiUpload() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setLocationRelativeTo(null);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setUndecorated(true);

		final ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(this.frame);
		cr.setSnapSize(new Dimension(10, 10));

		final JPanel base = new JPanel();
		base.setBorder(new LineBorder(new Color(128, 128, 128)));
		this.frame.getContentPane().add(base, BorderLayout.CENTER);
		base.setLayout(new BorderLayout(0, 0));

		final JPanel title = new JPanel();
		title.setBackground(new Color(45, 45, 45));
		base.add(title, BorderLayout.NORTH);

		final UiImage settings = new UiImage();
		try {
			settings.setImage(ImageIO.read(GuiUpload.class.getResource("/assets/signpic/textures/ui/setting.png")));
		} catch (final IOException e) {
		}

		final UiImage close = new UiImage();
		try {
			close.setImage(ImageIO.read(GuiUpload.class.getResource("/assets/signpic/textures/ui/close.png")));
		} catch (final IOException e) {
		}

		final UiImage icon = new UiImage();
		try {
			icon.setImage(ImageIO.read(GuiUpload.class.getResource("/assets/signpic/textures/logo.png")));
		} catch (final IOException e) {
		}

		final JLabel lbltitle = new JLabel("SignPicture!File");
		lbltitle.setForeground(new Color(154, 202, 71));
		lbltitle.setFont(new Font(Font.DIALOG, Font.BOLD, 30));

		final JLabel lbldiscription = new JLabel("A File Uploader!");
		lbldiscription.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		lbldiscription.setForeground(new Color(0, 255, 255));
		final GroupLayout gl_title = new GroupLayout(title);
		gl_title.setHorizontalGroup(
				gl_title.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_title.createSequentialGroup()
								.addGap(12)
								.addComponent(icon, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_title.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_title.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_title.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
														.addComponent(settings, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
														.addGap(4)
														.addComponent(close, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
														.addGap(4))
												.addGroup(gl_title.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(lbltitle)
														.addGap(16)))
										.addGroup(gl_title.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(lbldiscription)))));
		gl_title.setVerticalGroup(
				gl_title.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_title.createSequentialGroup()
								.addGap(4)
								.addGroup(gl_title.createParallelGroup(Alignment.LEADING)
										.addComponent(settings, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
										.addComponent(close, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_title.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_title.createSequentialGroup()
												.addComponent(lbltitle)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(lbldiscription))
										.addComponent(icon, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(20, Short.MAX_VALUE)));
		title.setLayout(gl_title);

		final JPanel drop = new JPanel();
		drop.setBackground(new Color(255, 255, 255));
		base.add(drop, BorderLayout.CENTER);

		final JPanel droparea = new JPanel();
		droparea.setBackground(new Color(255, 255, 255));
		droparea.setBorder(new DashedBorder());
		final GroupLayout gl_drop = new GroupLayout(drop);
		gl_drop.setHorizontalGroup(
				gl_drop.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_drop.createSequentialGroup()
								.addContainerGap()
								.addComponent(droparea, GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
								.addContainerGap()));
		gl_drop.setVerticalGroup(
				gl_drop.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_drop.createSequentialGroup()
								.addContainerGap()
								.addComponent(droparea, GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
								.addContainerGap()));
		droparea.setLayout(new BoxLayout(droparea, BoxLayout.Y_AXIS));

		final Component glue1 = Box.createVerticalGlue();
		droparea.add(glue1);

		final JLabel lblimagehere = new JLabel("Drop images here");
		droparea.add(lblimagehere);
		lblimagehere.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblimagehere.setFont(new Font("Tahoma", Font.PLAIN, 25));

		final JLabel lblOr = new JLabel("or");
		lblOr.setAlignmentX(Component.CENTER_ALIGNMENT);
		droparea.add(lblOr);

		final Component verticalStrut = Box.createVerticalStrut(5);
		droparea.add(verticalStrut);

		final JButton btnselect = new JButton("Select Images for Upload");
		btnselect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
			}
		});
		btnselect.setForeground(Color.BLACK);
		btnselect.setBackground(Color.WHITE);
		final Border line = new LineBorder(Color.BLACK);
		final Border margin = new EmptyBorder(5, 15, 5, 15);
		final Border compound = new CompoundBorder(line, margin);
		btnselect.setBorder(compound);
		btnselect.setFocusPainted(false);
		btnselect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(final MouseEvent evt) {
				btnselect.setBackground(Color.CYAN);
			}

			@Override
			public void mouseExited(final MouseEvent evt) {
				btnselect.setBackground(Color.WHITE);
			}
		});
		btnselect.setAlignmentX(Component.CENTER_ALIGNMENT);
		droparea.add(btnselect);

		final Component glue2 = Box.createVerticalGlue();
		droparea.add(glue2);
		drop.setLayout(gl_drop);

		this.frame.pack();
		final Dimension minsize = new Dimension(this.frame.getSize());
		this.frame.setMinimumSize(minsize);
		cr.setMinimumSize(minsize);
		this.frame.setSize(400, 400);
	}

	static class DashedBorder extends AbstractBorder {
		@Override
		public void paintBorder(final Component comp, final Graphics g, final int x, final int y, final int w, final int h) {
			final Graphics2D gg = (Graphics2D) g.create();
			gg.setColor(Color.GRAY);
			gg.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 6 }, 0));
			gg.drawRect(x, y, w-1, h-1);
			gg.dispose();
		}
	}
}
