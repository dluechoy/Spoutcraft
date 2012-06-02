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
package org.spoutcraft.client.gui.settings;

import com.pclewis.mcpatcher.mod.TextureUtils;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.gui.SafeButton;

public class FontResetButton extends SafeButton {
	GameSettingsScreen parent;

	public FontResetButton(GameSettingsScreen parent) {
		setText("Reset to Default Fonts");
		setTooltip("Resets font options to default settings.");
		this.parent = parent;
	}

	@Override
	protected void executeAction() {
		ConfigReader.betterFontsEnabled = false;
		ConfigReader.fontName = "SansSerif";
		ConfigReader.fontSize = 18;
		ConfigReader.fontAntiAlias = false;
		ConfigReader.fontDropShadowEnabled = true;
		ConfigReader.write();
		TextureUtils.setFontRenderer();

		SpoutClient.getHandle().displayGuiScreen(new GameSettingsScreen(parent.parent));
	}
}
