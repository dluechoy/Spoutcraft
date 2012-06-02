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

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class FontAntiAliasCheckBox extends GenericCheckBox{
	public FontAntiAliasCheckBox () {
		super("Anti-Alias");
		setChecked(ConfigReader.fontAntiAlias);
		setTooltip("This enables anti-aliasing which blurs the edges of the font to make it seem less jagged, but this can also make the font seem less sharp. You'll have to experiment with both options to determine which one looks better.\nThe default is to disable anti-aliasing.");
	}

	public FontAntiAliasCheckBox(String title) {
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
		ConfigReader.fontAntiAlias = this.isChecked();
		ConfigReader.write();
		TextureUtils.setFontRenderer();
	}
}
