/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.gui.minimap;

import org.apache.commons.lang3.ArrayUtils;
import org.getspout.commons.math.Vector3;
import org.lwjgl.input.Mouse;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.chunkcache.HeightMap;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.animation.PropertyAnimation;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.Control;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.Orientation;
import org.spoutcraft.spoutcraftapi.gui.Point;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.Widget;

public class GuiOverviewMap extends GuiSpoutScreen {
	
	private MapWidget map;
	private Label title, menuTitle;
	private Button buttonDone, buttonWaypoint, buttonFocus, buttonCloseMenu, buttonZoomIn, buttonZoomOut, buttonShowPlayer, buttonReset;
	private GenericScrollArea hoverMenu;
	
	private boolean dragging = false;
	private int dragStartX = -1, dragStartY = -1;
	Point coords = null;
	int y = -1;
	Waypoint clickedWaypoint = null;
	private int focus_mode = -1;
	private static final int FOCUS_SET = 1;
	private static final int FOCUS_REMOVE = 3;
	private int waypoint_mode = -1;
	private static final int WAYPOINT_ADD = 1;
	private static final int WAYPOINT_EDIT = 2;
	
	@Override
	protected void createInstances() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");

		title = new GenericLabel("Overview Map");
		buttonDone = new GenericButton("Done");
		buttonZoomIn = new GenericButton("+");
		buttonZoomOut = new GenericButton("-");
		buttonShowPlayer = new GenericButton("Player");
		buttonReset = new GenericButton("Reset View");
		map = new MapWidget(this);
		map.setGeometry(0, 0, width, height);
		map.showPlayer();
		getScreen().attachWidgets(spoutcraft, map, title, buttonDone, buttonZoomIn, buttonZoomOut, buttonShowPlayer, buttonReset);
		
		hoverMenu = new GenericScrollArea();
		hoverMenu.setBackgroundColor(new Color(0x55ffffff));
		hoverMenu.setPriority(RenderPriority.Lowest);
		menuTitle = new GenericLabel("What do you want to do?");
		buttonWaypoint = new GenericButton("Add Waypoint");
		buttonFocus = new GenericButton("Set Focus");
		buttonFocus.setTooltip("If a waypoint is in focus, the direction\nto it will be drawn on the minimap.");
		buttonCloseMenu = new GenericButton("Close");
		hoverMenu.attachWidgets(spoutcraft, buttonFocus, buttonWaypoint, buttonCloseMenu, menuTitle);	
		
		setMenuVisible(false);
		getScreen().attachWidget(spoutcraft, hoverMenu);
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(5);
		
		map.setGeometry(0, 0, width, height);
		map.setPriority(RenderPriority.Highest);

		buttonZoomIn.setGeometry(5, height - 25, 20, 20);
		buttonZoomOut.setGeometry(25, height - 25, 20, 20);
		buttonDone.setGeometry(width - 55, height - 25, 50, 20);
		buttonShowPlayer.setGeometry(50, height - 25, 50, 20);
		buttonReset.setGeometry(105, height - 25, 75, 20);
		
		hoverMenu.setGeometry(width / 2 - 320 / 2, height / 2 - 46 / 2, 320, 46);
		int w = SpoutClient.getHandle().fontRenderer.getStringWidth(menuTitle.getText());
		menuTitle.setGeometry(320 / 2 - w / 2, 5, w, 11);
		buttonWaypoint.setGeometry(5, 21, 100, 20);
		buttonFocus.setGeometry(110, 21, 100, 20);
		buttonCloseMenu.setGeometry(215, 21, 100, 20);
	}
	
	@Override
	public void buttonClicked(Button btn) {
		if(btn == buttonDone) {
			mc.displayGuiScreen(null);
		}
		if(btn == buttonZoomIn) {
			map.zoomBy(0.1f);
		}
		if(btn == buttonZoomOut) {
			map.zoomBy(-0.1f);
		}
		if(btn == buttonShowPlayer) {
			map.showPlayer();
		}
		if(btn == buttonWaypoint) {
			switch(waypoint_mode) {
			case WAYPOINT_ADD:
				int x = coords.getX();
				int z = coords.getY();
				SpoutClient.getHandle().displayGuiScreen(new GuiAddWaypoint(this, x, y, z));
				break;
			case WAYPOINT_EDIT:
				SpoutClient.getHandle().displayGuiScreen(new GuiAddWaypoint(this, clickedWaypoint));
				break;
			}
			setMenuVisible(false);
		}
		if(btn == buttonCloseMenu) {
			setMenuVisible(false);
		}
		if(btn == buttonFocus) {
			switch(focus_mode) {
			case FOCUS_SET:
				if(clickedWaypoint != null) {
					MinimapConfig.getInstance().setFocussedWaypoint(clickedWaypoint);
				}
				break;
			case FOCUS_REMOVE:
				MinimapConfig.getInstance().setFocussedWaypoint(null);
				break;
			}
			setMenuVisible(false);
		}
		if(btn == buttonReset) {
			map.reset();
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		if(button == 0 && isInBoundingRect(map, x, y) && !hitsWidget(x, y, map, title)) {
			setMenuVisible(false);
			dragStartX = x;
			dragStartY = y;
		}
		super.mouseClicked(x, y, button);
	}

	private boolean hitsWidget(int x, int y, Widget ...exclude) {
		for(Widget widget:getScreen().getAttachedWidgets(true)) {
			if(isInBoundingRect(widget, x, y)) {
				if(ArrayUtils.contains(exclude, widget) || !widget.isVisible()) {
					continue;
				}
				return true;
			}
		}
		return false;
	}
	
	private void setMenuVisible(boolean visible) {
		hoverMenu.setVisible(visible);
		for(Widget w:hoverMenu.getAttachedWidgets(true)) {
			w.setVisible(visible);
			if(w instanceof Control) {
				((Control) w).setEnabled(visible);
			}
		}
	}
	
	private boolean withinManhattanLength(Point center, Point clicked, int length) {
		int cx = center.getX();
		int cy = center.getY();
		int x = clicked.getX();
		int y = clicked.getY();
		return x >= cx - length && x <= cx + length && y >= cy - length && y <= cy + length;
	}
	
	private Waypoint getClickedWaypoint(int x, int z) {
		Point clicked = new Point(x,z);
		for(Waypoint waypoint:MinimapConfig.getInstance().getWaypoints(MinimapUtils.getWorldName())) {
			if(withinManhattanLength(new Point(waypoint.x, waypoint.z), clicked, 2)) {
				return waypoint;
			}
		}
		return null;
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int button) {
		if(button == 0 && !Mouse.isButtonDown(button)) {
			if(dragStartX == x && dragStartY == y && !dragging) {
				setMenuVisible(true);
				coords = map.mapOutsideToCoords(new Point(x, y));
				clickedWaypoint = getClickedWaypoint(coords.getX(), coords.getY());
				focus_mode = -1;
				if(withinManhattanLength(map.getPlayerPosition(), coords, 2)) {
					coords = map.getPlayerPosition();
					this.y = (int) SpoutClient.getHandle().thePlayer.posY;
				} else {
					this.y = HeightMap.getHeightMap(MinimapUtils.getWorldName()).getHeight(coords.getX(), coords.getY());
				}
				if(MinimapConfig.getInstance().getFocussedWaypoint() != null) {
					Waypoint waypoint = MinimapConfig.getInstance().getFocussedWaypoint();
					if(withinManhattanLength(new Point(waypoint.x, waypoint.z), coords, 2)) {
						buttonFocus.setText("Remove Focus");
						focus_mode = FOCUS_REMOVE;
						clickedWaypoint = waypoint;
					}
				} else if (clickedWaypoint == null) {
					buttonFocus.setEnabled(false);
					buttonFocus.setText("Set Focus");
				} else {
					buttonFocus.setText("Set Focus");
					focus_mode = FOCUS_SET;
				}
				if(clickedWaypoint == null) {
					waypoint_mode = WAYPOINT_ADD;
					buttonWaypoint.setText("Add Waypoint");
				} else {
					waypoint_mode = WAYPOINT_EDIT;
					buttonWaypoint.setText("Edit Waypoint");
				}
				menuTitle.setText("Position (" + coords.getX() + ":" + this.y + ":" + coords.getY() + ")");
			}
			dragging = false;
			dragStartX = -1;
			dragStartY = -1;
		}
		if(dragging || dragStartX != -1) {
			dragging = true;
			int mX = (int) map.getScrollPosition(Orientation.HORIZONTAL);
			int mY = (int) map.getScrollPosition(Orientation.VERTICAL);
			mX -= x - dragStartX;
			mY -= y - dragStartY;
			map.setScrollPosition(Orientation.HORIZONTAL, mX);
			map.setScrollPosition(Orientation.VERTICAL, mY);
			
			dragStartX = x;
			dragStartY = y;
		}
		super.mouseMovedOrUp(x, y, button);
	}
}