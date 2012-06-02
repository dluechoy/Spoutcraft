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

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import org.spoutcraft.client.SpoutClient;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class BetterFontsCheckBox extends GenericCheckBox{
	public BetterFontsCheckBox () {
		super("Enable BetterFonts");
		setChecked(ConfigReader.betterFontsEnabled);
		setTooltip("The BetterFonts adds OpenType font support. This will use the fonts installed on your system for drawing text instead of the builtin bitmap fonts that come with Minecraft. All in-game text will change to use the new fonts including GUIs, the F3 debug screen, chat, and even signs. Languages such as Arabic and Hindi look much better with this mod since both require complex layout that the bitmap fonts simply can't provide.\nThis mod should have little or no impact on performance.");
	}

	public BetterFontsCheckBox(String title) {
		//super(title);
	}

	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "This option is not allowed.";
		}
		return super.getTooltip();
	}
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.betterFontsEnabled = this.isChecked();
		ConfigReader.write();

		Minecraft game = SpoutClient.getHandle();
		//game.fontRenderer.initialize(game.gameSettings, "/font/default.png", game.renderEngine);
		game.fontRenderer.enableBetterFonts = ConfigReader.betterFontsEnabled; // this way the glyphcache is not flushed.


	}
}
