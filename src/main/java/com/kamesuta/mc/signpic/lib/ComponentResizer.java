package com.kamesuta.mc.signpic.lib;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *  The ComponentResizer allows you to resize a component by dragging a border
 *  of the component.
 */
public class ComponentResizer extends MouseAdapter {
	private final static Dimension MINIMUM_SIZE = new Dimension(10, 10);
	private final static Dimension MAXIMUM_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

	private static Map<Integer, Integer> cursors = new HashMap<Integer, Integer>();
	{
		cursors.put(1, Cursor.N_RESIZE_CURSOR);
		cursors.put(2, Cursor.W_RESIZE_CURSOR);
		cursors.put(4, Cursor.S_RESIZE_CURSOR);
		cursors.put(8, Cursor.E_RESIZE_CURSOR);
		cursors.put(3, Cursor.NW_RESIZE_CURSOR);
		cursors.put(9, Cursor.NE_RESIZE_CURSOR);
		cursors.put(6, Cursor.SW_RESIZE_CURSOR);
		cursors.put(12, Cursor.SE_RESIZE_CURSOR);
	}

	private Insets dragInsets;
	private Dimension snapSize;
	private Insets edgeInsets = new Insets(0, 0, 0, 0);

	private int direction;
	protected static final int NORTH = 1;
	protected static final int WEST = 2;
	protected static final int SOUTH = 4;
	protected static final int EAST = 8;

	private Cursor sourceCursor;
	private boolean resizing;
	private Rectangle bounds;
	private Point pressed;
	private boolean autoscrolls;

	private Dimension minimumSize = MINIMUM_SIZE;
	private Dimension maximumSize = MAXIMUM_SIZE;

	/**
	 *  Convenience contructor. All borders are resizable in increments of
	 *  a single pixel. Components must be registered separately.
	 */
	public ComponentResizer() {
		this(new Insets(5, 5, 5, 5), new Dimension(1, 1));
	}

	/**
	 *  Convenience contructor. All borders are resizable in increments of
	 *  a single pixel. Components can be registered when the class is created
	 *  or they can be registered separately afterwards.
	 *
	 *  @param components components to be automatically registered
	 */
	public ComponentResizer(final Component... components) {
		this(new Insets(5, 5, 5, 5), new Dimension(1, 1), components);
	}

	/**
	 *  Convenience contructor. Eligible borders are resisable in increments of
	 *  a single pixel. Components can be registered when the class is created
	 *  or they can be registered separately afterwards.
	 *
	 *  @param dragInsets Insets specifying which borders are eligible to be
	 *                    resized.
	 *  @param components components to be automatically registered
	 */
	public ComponentResizer(final Insets dragInsets, final Component... components) {
		this(dragInsets, new Dimension(1, 1), components);
	}

	/**
	 *  Create a ComponentResizer.
	 *
	 *  @param dragInsets Insets specifying which borders are eligible to be
	 *                    resized.
	 *  @param snapSize Specify the dimension to which the border will snap to
	 *                  when being dragged. Snapping occurs at the halfway mark.
	 *  @param components components to be automatically registered
	 */
	public ComponentResizer(final Insets dragInsets, final Dimension snapSize, final Component... components) {
		setDragInsets(dragInsets);
		setSnapSize(snapSize);
		registerComponent(components);
	}

	/**
	 *  Get the drag insets
	 *
	 *  @return  the drag insets
	 */
	public Insets getDragInsets() {
		return this.dragInsets;
	}

	/**
	 *  Set the drag dragInsets. The insets specify an area where mouseDragged
	 *  events are recognized from the edge of the border inwards. A value of
	 *  0 for any size will imply that the border is not resizable. Otherwise
	 *  the appropriate drag cursor will appear when the mouse is inside the
	 *  resizable border area.
	 *
	 *  @param  dragInsets Insets to control which borders are resizeable.
	 */
	public void setDragInsets(final Insets dragInsets) {
		validateMinimumAndInsets(this.minimumSize, dragInsets);

		this.dragInsets = dragInsets;
	}

	/**
	 *  Get the bounds insets
	 *
	 *  @return  the bounds insets
	 */
	public Insets getEdgeInsets() {
		return this.edgeInsets;
	}

	/**
	 *  Set the edge insets. The insets specify how close to each edge of the parent
	 *  component that the child component can be moved. Positive values means the
	 *  component must be contained within the parent. Negative values means the
	 *  component can be moved outside the parent.
	 *
	 *  @param  edgeInsets
	 */
	public void setEdgeInsets(final Insets edgeInsets) {
		this.edgeInsets = edgeInsets;
	}

	/**
	 *  Get the components maximum size.
	 *
	 *  @return the maximum size
	 */
	public Dimension getMaximumSize() {
		return this.maximumSize;
	}

	/**
	 *  Specify the maximum size for the component. The component will still
	 *  be constrained by the size of its parent.
	 *
	 *  @param maximumSize the maximum size for a component.
	 */
	public void setMaximumSize(final Dimension maximumSize) {
		this.maximumSize = maximumSize;
	}

	/**
	 *  Get the components minimum size.
	 *
	 *  @return the minimum size
	 */
	public Dimension getMinimumSize() {
		return this.minimumSize;
	}

	/**
	 *  Specify the minimum size for the component. The minimum size is
	 *  constrained by the drag insets.
	 *
	 *  @param minimumSize the minimum size for a component.
	 */
	public void setMinimumSize(final Dimension minimumSize) {
		validateMinimumAndInsets(minimumSize, this.dragInsets);

		this.minimumSize = minimumSize;
	}

	/**
	 *  Remove listeners from the specified component
	 *
	 *  @param component  the component the listeners are removed from
	 */
	public void deregisterComponent(final Component... components) {
		for (final Component component : components) {
			component.removeMouseListener(this);
			component.removeMouseMotionListener(this);
		}
	}

	/**
	 *  Add the required listeners to the specified component
	 *
	 *  @param component  the component the listeners are added to
	 */
	public void registerComponent(final Component... components) {
		for (final Component component : components) {
			component.addMouseListener(this);
			component.addMouseMotionListener(this);
		}
	}

	/**
	 *	Get the snap size.
	 *
	 *  @return the snap size.
	 */
	public Dimension getSnapSize() {
		return this.snapSize;
	}

	/**
	 *  Control how many pixels a border must be dragged before the size of
	 *  the component is changed. The border will snap to the size once
	 *  dragging has passed the halfway mark.
	 *
	 *  @param snapSize Dimension object allows you to separately spcify a
	 *                  horizontal and vertical snap size.
	 */
	public void setSnapSize(final Dimension snapSize) {
		this.snapSize = snapSize;
	}

	/**
	 *  When the components minimum size is less than the drag insets then
	 *	we can't determine which border should be resized so we need to
	 *  prevent this from happening.
	 */
	private void validateMinimumAndInsets(final Dimension minimum, final Insets drag) {
		final int minimumWidth = drag.left+drag.right;
		final int minimumHeight = drag.top+drag.bottom;

		if (
			minimum.width<minimumWidth
					||minimum.height<minimumHeight
		) {
			final String message = "Minimum size cannot be less than drag insets";
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 */
	@Override
	public void mouseMoved(final MouseEvent e) {
		final Component source = e.getComponent();
		final Point location = e.getPoint();
		this.direction = 0;

		if (location.x<this.dragInsets.left)
			this.direction += WEST;

		if (location.x>source.getWidth()-this.dragInsets.right-1)
			this.direction += EAST;

		if (location.y<this.dragInsets.top)
			this.direction += NORTH;

		if (location.y>source.getHeight()-this.dragInsets.bottom-1)
			this.direction += SOUTH;

		//  Mouse is no longer over a resizable border

		if (this.direction==0)
			source.setCursor(this.sourceCursor);
		else // use the appropriate resizable cursor
		{
			final int cursorType = cursors.get(this.direction);
			final Cursor cursor = Cursor.getPredefinedCursor(cursorType);
			source.setCursor(cursor);
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		if (!this.resizing) {
			final Component source = e.getComponent();
			this.sourceCursor = source.getCursor();
		}
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		if (!this.resizing) {
			final Component source = e.getComponent();
			source.setCursor(this.sourceCursor);
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		//	The mouseMoved event continually updates this variable

		if (this.direction==0)
			return;

		//  Setup for resizing. All future dragging calculations are done based
		//  on the original bounds of the component and mouse pressed location.

		this.resizing = true;

		final Component source = e.getComponent();
		this.pressed = e.getPoint();
		SwingUtilities.convertPointToScreen(this.pressed, source);
		this.bounds = source.getBounds();

		//  Making sure autoscrolls is false will allow for smoother resizing
		//  of components

		if (source instanceof JComponent) {
			final JComponent jc = (JComponent) source;
			this.autoscrolls = jc.getAutoscrolls();
			jc.setAutoscrolls(false);
		}
	}

	/**
	 *  Restore the original state of the Component
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
		this.resizing = false;

		final Component source = e.getComponent();
		source.setCursor(this.sourceCursor);

		if (source instanceof JComponent)
			((JComponent) source).setAutoscrolls(this.autoscrolls);
	}

	/**
	 *  Resize the component ensuring location and size is within the bounds
	 *  of the parent container and that the size is within the minimum and
	 *  maximum constraints.
	 *
	 *  All calculations are done using the bounds of the component when the
	 *  resizing started.
	 */
	@Override
	public void mouseDragged(final MouseEvent e) {
		if (this.resizing==false)
			return;

		final Component source = e.getComponent();
		final Point dragged = e.getPoint();
		SwingUtilities.convertPointToScreen(dragged, source);

		changeBounds(source, this.direction, this.bounds, this.pressed, dragged);
	}

	protected void changeBounds(final Component source, final int direction, final Rectangle bounds, final Point pressed, final Point current) {
		//  Start with original locaton and size

		int x = bounds.x;
		int y = bounds.y;
		int width = bounds.width;
		int height = bounds.height;

		//  Resizing the West or North border affects the size and location

		if (WEST==(direction&WEST)) {
			int drag = getDragDistance(pressed.x, current.x, this.snapSize.width);
			if (this.edgeInsets!=null) {
				final int maximum = Math.min(width+x, this.maximumSize.width);
				drag = getDragBounded(drag, this.snapSize.width, width+this.edgeInsets.left, this.minimumSize.width, maximum);
			} else
				drag = getDragBounded(drag, this.snapSize.width, width, this.minimumSize.width, this.maximumSize.width);

			x -= drag;
			width += drag;
		}

		if (NORTH==(direction&NORTH)) {
			int drag = getDragDistance(pressed.y, current.y, this.snapSize.height);
			if (this.edgeInsets!=null) {
				final int maximum = Math.min(height+y, this.maximumSize.height);
				drag = getDragBounded(drag, this.snapSize.height, height+this.edgeInsets.top, this.minimumSize.height, maximum);
			} else
				drag = getDragBounded(drag, this.snapSize.height, height, this.minimumSize.height, this.maximumSize.height);

			y -= drag;
			height += drag;
		}

		//  Resizing the East or South border only affects the size

		if (EAST==(direction&EAST)) {
			int drag = getDragDistance(current.x, pressed.x, this.snapSize.width);
			final Dimension boundingSize = getBoundingSize(source);
			if (this.edgeInsets!=null) {
				final int maximum = Math.min(boundingSize.width-x, this.maximumSize.width);
				drag = getDragBounded(drag, this.snapSize.width, width+this.edgeInsets.right, this.minimumSize.width, maximum);
			} else
				drag = getDragBounded(drag, this.snapSize.width, width, this.minimumSize.width, this.maximumSize.width);

			width += drag;
		}

		if (SOUTH==(direction&SOUTH)) {
			int drag = getDragDistance(current.y, pressed.y, this.snapSize.height);
			final Dimension boundingSize = getBoundingSize(source);
			if (this.edgeInsets!=null) {
				final int maximum = Math.min(boundingSize.height-y, this.maximumSize.height);
				drag = getDragBounded(drag, this.snapSize.height, height+this.edgeInsets.bottom, this.minimumSize.height, maximum);
			} else
				drag = getDragBounded(drag, this.snapSize.height, height, this.minimumSize.height, this.maximumSize.height);

			height += drag;
		}

		source.setBounds(x, y, width, height);
		source.validate();
	}

	/*
	 *  Determine how far the mouse has moved from where dragging started
	 */
	private int getDragDistance(final int larger, final int smaller, final int snapSize) {
		final int halfway = snapSize/2;
		int drag = larger-smaller;
		drag += drag<0 ? -halfway : halfway;
		drag = drag/snapSize*snapSize;

		return drag;
	}

	/*
	 *  Adjust the drag value to be within the minimum and maximum range.
	 */
	private int getDragBounded(int drag, final int snapSize, final int dimension, final int minimum, final int maximum) {
		while (dimension+drag<minimum)
			drag += snapSize;

		while (dimension+drag>maximum)
			drag -= snapSize;

		return drag;
	}

	/*
	 *  Keep the size of the component within the bounds of its parent.
	 */
	private Dimension getBoundingSize(final Component source) {
		if (source instanceof Window) {
			final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			final Rectangle bounds = env.getMaximumWindowBounds();
			return new Dimension(bounds.width, bounds.height);
		} else
			return source.getParent().getSize();
	}
}