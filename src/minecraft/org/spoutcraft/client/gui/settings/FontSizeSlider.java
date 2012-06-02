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

import com.pclewis.mcpatcher.mod.TextureUtils;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericSlider;

public class FontSizeSlider extends GenericSlider{
	public FontSizeSlider() {
		/*super("Music");*/
		setSliderPosition((float)(ConfigReader.fontSize-8)/64F);
		setTooltip("You can use the size property to affect how large or small the text will appear.\nThe default size used is 18.");
	}

	@Override
	public void onSliderDrag(SliderDragEvent event) {
		ConfigReader.fontSize = 8+(int)(this.getSliderPosition()*64F);
		ConfigReader.write();
		TextureUtils.setFontRenderer();
	}

	public String getText() {
		return ""+(8+(int)(this.getSliderPosition()*64F));
	}
}
