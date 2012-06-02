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

import java.util.ArrayList;
import java.util.List;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import com.pclewis.mcpatcher.mod.TextureUtils;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.GenericComboBox;

import java.util.regex.Pattern;

public class FontNameCombo extends GenericComboBox {
	private GameSettingsScreen gui;
	private Font allFonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

	public FontNameCombo(GameSettingsScreen gui) {
		List<String> items = new ArrayList<String>();
		for (Font aFont : allFonts) {
			items.add(aFont.getName());
//			System.out.println("BetterFonts " + aFont.getName());
		}
		setItems(items);
		setSelection(items.indexOf(ConfigReader.fontName));  //since there is no arbitary user input getFontFromName is unnecessary.
		System.out.println("BetterFonts FontNameCombo : " + ConfigReader.fontName);
		this.gui = gui;
	}

	//Store the string as fontname and trigger custom texture change widget.
	@Override
	public void onSelectionChanged(int i, String text) {
		ConfigReader.fontName=text;
		ConfigReader.write();
		TextureUtils.setFontRenderer();
	}
}
