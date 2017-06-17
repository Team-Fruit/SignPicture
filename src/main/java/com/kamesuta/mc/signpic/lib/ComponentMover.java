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

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *  This class allows you to move a Component by using a mouse. The Component
 *  moved can be a high level Window (ie. Window, Frame, Dialog) in which case
 *  the Window is moved within the desktop. Or the Component can belong to a
 *  Container in which case the Component is moved within the Container.
 *
 *  When moving a Window, the listener can be added to a child Component of
 *  the Window. In this case attempting to move the child will result in the
 *  Window moving. For example, you might create a custom "Title Bar" for an
 *  undecorated Window and moving of the Window is accomplished by moving the
 *  title bar only. Multiple components can be registered as "window movers".
 *
 *  Components can be registered when the class is created. Additional
 *  components can be added at any time using the registerComponent() method.
 */
public class ComponentMover extends MouseAdapter {
	private Insets dragInsets = new Insets(0, 0, 0, 0);
	private Dimension snapSize = new Dimension(1, 1);
	private Insets edgeInsets = new Insets(0, 0, 0, 0);
	private boolean changeCursor = true;
	private boolean autoLayout = false;

	private Class<?> destinationClass;
	private Component destinationComponent;
	private Component destination;
	private Component source;

	private Point pressed;
	private Point location;

	private Cursor originalCursor;
	private boolean autoscrolls;
	private boolean potentialDrag;

	/**
	 *  Constructor for moving individual components. The components must be
	 *  regisetered using the registerComponent() method.
	 */
	public ComponentMover() {
	}

	/**
	 *  Constructor to specify a Class of Component that will be moved when
	 *  drag events are generated on a registered child component. The events
	 *  will be passed to the first ancestor of this specified class.
	 *
	 *  @param destinationClass  the Class of the ancestor component
	 *  @param component         the Components to be registered for forwarding
	 *                           drag events to the ancestor Component.
	 */
	public ComponentMover(final Class<?> destinationClass, final Component... components) {
		this.destinationClass = destinationClass;
		registerComponent(components);
	}

	/**
	 *  Constructor to specify a parent component that will be moved when drag
	 *  events are generated on a registered child component.
	 *
	 *  @param destinationComponent  the component drage events should be forwareded to
	 *  @param components    the Components to be registered for forwarding drag
	 *                       events to the parent component to be moved
	 */
	public ComponentMover(final Component destinationComponent, final Component... components) {
		this.destinationComponent = destinationComponent;
		registerComponent(components);
	}

	/**
	 *  Get the auto layout property
	 *
	 *  @return  the auto layout property
	 */
	public boolean isAutoLayout() {
		return this.autoLayout;
	}

	/**
	 *  Set the auto layout property
	 *
	 *  @param  autoLayout when true layout will be invoked on the parent container
	 */
	public void setAutoLayout(final boolean autoLayout) {
		this.autoLayout = autoLayout;
	}

	/**
	 *  Get the change cursor property
	 *
	 *  @return  the change cursor property
	 */
	public boolean isChangeCursor() {
		return this.changeCursor;
	}

	/**
	 *  Set the change cursor property
	 *
	 *  @param  changeCursor when true the cursor will be changed to the
	 *                       Cursor.MOVE_CURSOR while the mouse is pressed
	 */
	public void setChangeCursor(final boolean changeCursor) {
		this.changeCursor = changeCursor;
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
	 *  Set the drag insets. The insets specify an area where mouseDragged
	 *  events should be ignored and therefore the component will not be moved.
	 *  This will prevent these events from being confused with a
	 *  MouseMotionListener that supports component resizing.
	 *
	 *  @param  dragInsets
	 */
	public void setDragInsets(final Insets dragInsets) {
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
	 *  Remove listeners from the specified component
	 *
	 *  @param component  the component the listeners are removed from
	 */
	public void deregisterComponent(final Component... components) {
		for (final Component component : components)
			component.removeMouseListener(this);
	}

	/**
	 *  Add the required listeners to the specified component
	 *
	 *  @param component  the component the listeners are added to
	 */
	public void registerComponent(final Component... components) {
		for (final Component component : components)
			component.addMouseListener(this);
	}

	/**
	 *	Get the snap size
	 *
	 *  @return the snap size
	 */
	public Dimension getSnapSize() {
		return this.snapSize;
	}

	/**
	 *  Set the snap size. Forces the component to be snapped to
	 *  the closest grid position. Snapping will occur when the mouse is
	 *  dragged half way.
	 */
	public void setSnapSize(final Dimension snapSize) {
		if (
			snapSize.width<1
					||snapSize.height<1
		)
			throw new IllegalArgumentException("Snap sizes must be greater than 0");

		this.snapSize = snapSize;
	}

	/**
	 *  Setup the variables used to control the moving of the component:
	 *
	 *  source - the source component of the mouse event
	 *  destination - the component that will ultimately be moved
	 *  pressed - the Point where the mouse was pressed in the destination
	 *      component coordinates.
	 */
	@Override
	public void mousePressed(final MouseEvent e) {
		this.source = e.getComponent();
		final int width = this.source.getSize().width-this.dragInsets.left-this.dragInsets.right;
		final int height = this.source.getSize().height-this.dragInsets.top-this.dragInsets.bottom;
		final Rectangle r = new Rectangle(this.dragInsets.left, this.dragInsets.top, width, height);

		if (r.contains(e.getPoint()))
			setupForDragging(e);
	}

	private void setupForDragging(final MouseEvent e) {
		this.source.addMouseMotionListener(this);
		this.potentialDrag = true;

		//  Determine the component that will ultimately be moved

		if (this.destinationComponent!=null)
			this.destination = this.destinationComponent;
		else if (this.destinationClass==null)
			this.destination = this.source;
		else
			this.destination = SwingUtilities.getAncestorOfClass(this.destinationClass, this.source);

		this.pressed = e.getLocationOnScreen();
		this.location = this.destination.getLocation();

		if (this.changeCursor) {
			this.originalCursor = this.source.getCursor();
			this.source.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		//  Making sure autoscrolls is false will allow for smoother dragging of
		//  individual components

		if (this.destination instanceof JComponent) {
			final JComponent jc = (JComponent) this.destination;
			this.autoscrolls = jc.getAutoscrolls();
			jc.setAutoscrolls(false);
		}
	}

	/**
	 *  Move the component to its new location. The dragged Point must be in
	 *  the destination coordinates.
	 */
	@Override
	public void mouseDragged(final MouseEvent e) {
		final Point dragged = e.getLocationOnScreen();
		final int dragX = getDragDistance(dragged.x, this.pressed.x, this.snapSize.width);
		final int dragY = getDragDistance(dragged.y, this.pressed.y, this.snapSize.height);

		int locationX = this.location.x+dragX;
		int locationY = this.location.y+dragY;

		//  Mouse dragged events are not generated for every pixel the mouse
		//  is moved. Adjust the location to make sure we are still on a
		//  snap value.

		if (this.edgeInsets!=null) {
			while (locationX<this.edgeInsets.left)
				locationX += this.snapSize.width;

			while (locationY<this.edgeInsets.top)
				locationY += this.snapSize.height;

			final Dimension d = getBoundingSize(this.destination);

			while (locationX+this.destination.getSize().width+this.edgeInsets.right>d.width)
				locationX -= this.snapSize.width;

			while (locationY+this.destination.getSize().height+this.edgeInsets.bottom>d.height)
				locationY -= this.snapSize.height;
		}

		//  Adjustments are finished, move the component

		this.destination.setLocation(locationX, locationY);
	}

	/*
	 *  Determine how far the mouse has moved from where dragging started
	 *  (Assume drag direction is down and right for positive drag distance)
	 */
	private int getDragDistance(final int larger, final int smaller, final int snapSize) {
		final int halfway = snapSize/2;
		int drag = larger-smaller;
		drag += drag<0 ? -halfway : halfway;
		drag = drag/snapSize*snapSize;

		return drag;
	}

	/*
	 *  Get the bounds of the parent of the dragged component.
	 */
	private Dimension getBoundingSize(final Component source) {
		if (source instanceof Window) {
			final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			final Rectangle bounds = env.getMaximumWindowBounds();
			return new Dimension(bounds.width, bounds.height);
		} else
			return source.getParent().getSize();
	}

	/**
	 *  Restore the original state of the Component
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
		if (!this.potentialDrag)
			return;

		this.source.removeMouseMotionListener(this);
		this.potentialDrag = false;

		if (this.changeCursor)
			this.source.setCursor(this.originalCursor);

		if (this.destination instanceof JComponent)
			((JComponent) this.destination).setAutoscrolls(this.autoscrolls);

		//  Layout the components on the parent container

		if (this.autoLayout)
			if (this.destination instanceof JComponent)
				((JComponent) this.destination).revalidate();
			else
				this.destination.validate();
	}
}