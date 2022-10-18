package com.badlogic.gdx.tools.hiero.unicodefont.effects;

import com.badlogic.gdx.tools.hiero.unicodefont.Glyph;
import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public interface Effect {
  void draw(BufferedImage paramBufferedImage, Graphics2D paramGraphics2D, UnicodeFont paramUnicodeFont, Glyph paramGlyph);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\Effect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */