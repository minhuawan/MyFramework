/*     */ package com.badlogic.gdx.tools.hiero.unicodefont;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.ColorEffect;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.Effect;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.IntBuffer;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GlyphPage
/*     */ {
/*     */   private final UnicodeFont unicodeFont;
/*     */   private final int pageWidth;
/*     */   private final int pageHeight;
/*     */   private final Texture texture;
/*  56 */   private final List<Glyph> pageGlyphs = new ArrayList<Glyph>(32);
/*  57 */   private final List<String> hashes = new ArrayList<String>(32);
/*  58 */   Array<Row> rows = new Array();
/*     */   
/*     */   public static final int MAX_GLYPH_SIZE = 256;
/*     */   
/*     */   GlyphPage(UnicodeFont unicodeFont, int pageWidth, int pageHeight) {
/*  63 */     this.unicodeFont = unicodeFont;
/*  64 */     this.pageWidth = pageWidth;
/*  65 */     this.pageHeight = pageHeight;
/*     */     
/*  67 */     this.texture = new Texture(pageWidth, pageHeight, Pixmap.Format.RGBA8888);
/*  68 */     this.rows.add(new Row());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int loadGlyphs(List glyphs, int maxGlyphsToLoad) {
/*  80 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  81 */     this.texture.bind();
/*     */     
/*  83 */     int loadedCount = 0;
/*  84 */     for (Iterator<Glyph> iter = glyphs.iterator(); iter.hasNext(); ) {
/*  85 */       Glyph glyph = iter.next();
/*  86 */       int width = Math.min(256, glyph.getWidth());
/*  87 */       int height = Math.min(256, glyph.getHeight());
/*  88 */       if (width == 0 || height == 0) {
/*  89 */         this.pageGlyphs.add(glyph);
/*     */       } else {
/*  91 */         Row bestRow = null;
/*     */         
/*  93 */         for (int ii = 0, nn = this.rows.size - 1; ii < nn; ii++) {
/*  94 */           Row row = (Row)this.rows.get(ii);
/*  95 */           if (row.x + width < this.pageWidth && 
/*  96 */             row.y + height < this.pageHeight && 
/*  97 */             height <= row.height && (
/*  98 */             bestRow == null || row.height < bestRow.height)) bestRow = row; 
/*     */         } 
/* 100 */         if (bestRow == null) {
/*     */           
/* 102 */           Row row = (Row)this.rows.peek();
/* 103 */           if (row.y + height >= this.pageHeight)
/* 104 */             continue;  if (row.x + width < this.pageWidth) {
/* 105 */             row.height = Math.max(row.height, height);
/* 106 */             bestRow = row;
/*     */           } else {
/*     */             
/* 109 */             bestRow = new Row();
/* 110 */             row.y += row.height;
/* 111 */             bestRow.height = height;
/* 112 */             this.rows.add(bestRow);
/*     */           } 
/*     */         } 
/* 115 */         if (bestRow == null)
/*     */           continue; 
/* 117 */         if (renderGlyph(glyph, bestRow.x, bestRow.y, width, height)) bestRow.x += width;
/*     */       
/*     */       } 
/* 120 */       iter.remove();
/* 121 */       loadedCount++;
/* 122 */       if (loadedCount == maxGlyphsToLoad) {
/*     */         break;
/*     */       }
/*     */     } 
/* 126 */     return loadedCount;
/*     */   }
/*     */   static class Row {
/*     */     int x;
/*     */     int y;
/*     */     int height; }
/*     */   
/*     */   private boolean renderGlyph(Glyph glyph, int pageX, int pageY, int width, int height) {
/*     */     int format;
/* 135 */     scratchGraphics.setComposite(AlphaComposite.Clear);
/* 136 */     scratchGraphics.fillRect(0, 0, 256, 256);
/* 137 */     scratchGraphics.setComposite(AlphaComposite.SrcOver);
/*     */     
/* 139 */     ByteBuffer glyphPixels = scratchByteBuffer;
/*     */     
/* 141 */     if (this.unicodeFont.getRenderType() == UnicodeFont.RenderType.FreeType && this.unicodeFont.bitmapFont != null) {
/* 142 */       BitmapFont.BitmapFontData data = this.unicodeFont.bitmapFont.getData();
/* 143 */       BitmapFont.Glyph g = data.getGlyph((char)glyph.getCodePoint());
/* 144 */       Pixmap fontPixmap = ((TextureRegion)this.unicodeFont.bitmapFont.getRegions().get(g.page)).getTexture().getTextureData().consumePixmap();
/*     */       
/* 146 */       int fontWidth = fontPixmap.getWidth();
/* 147 */       int padTop = this.unicodeFont.getPaddingTop(), padBottom = this.unicodeFont.getPaddingBottom();
/* 148 */       int padLeftBytes = this.unicodeFont.getPaddingLeft() * 4;
/* 149 */       int padXBytes = padLeftBytes + this.unicodeFont.getPaddingRight() * 4;
/* 150 */       int glyphRowBytes = width * 4, fontRowBytes = g.width * 4;
/*     */       
/* 152 */       ByteBuffer fontPixels = fontPixmap.getPixels();
/* 153 */       byte[] row = new byte[glyphRowBytes];
/* 154 */       glyphPixels.position(0); int i;
/* 155 */       for (i = 0; i < padTop; i++)
/* 156 */         glyphPixels.put(row); 
/* 157 */       glyphPixels.position((height - padBottom) * glyphRowBytes);
/* 158 */       for (i = 0; i < padBottom; i++)
/* 159 */         glyphPixels.put(row); 
/* 160 */       glyphPixels.position(padTop * glyphRowBytes);
/* 161 */       for (int y = 0, n = g.height; y < n; y++) {
/* 162 */         fontPixels.position(((g.srcY + y) * fontWidth + g.srcX) * 4);
/* 163 */         fontPixels.get(row, padLeftBytes, fontRowBytes);
/* 164 */         glyphPixels.put(row);
/*     */       } 
/* 166 */       fontPixels.position(0);
/* 167 */       glyphPixels.position(height * glyphRowBytes);
/* 168 */       glyphPixels.flip();
/* 169 */       format = 6408;
/*     */     } else {
/*     */       
/* 172 */       if (this.unicodeFont.getRenderType() == UnicodeFont.RenderType.Native) {
/* 173 */         for (Iterator<Effect> iter = this.unicodeFont.getEffects().iterator(); iter.hasNext(); ) {
/* 174 */           Effect effect = iter.next();
/* 175 */           if (effect instanceof ColorEffect) scratchGraphics.setColor(((ColorEffect)effect).getColor()); 
/*     */         } 
/* 177 */         scratchGraphics.setColor(Color.white);
/* 178 */         scratchGraphics.setFont(this.unicodeFont.getFont());
/* 179 */         scratchGraphics.drawString("" + (char)glyph.getCodePoint(), 0, this.unicodeFont.getAscent());
/* 180 */       } else if (this.unicodeFont.getRenderType() == UnicodeFont.RenderType.Java) {
/* 181 */         scratchGraphics.setColor(Color.white);
/* 182 */         for (Iterator<Effect> iter = this.unicodeFont.getEffects().iterator(); iter.hasNext();)
/* 183 */           ((Effect)iter.next()).draw(scratchImage, scratchGraphics, this.unicodeFont, glyph); 
/* 184 */         glyph.setShape(null);
/*     */       } 
/*     */       
/* 187 */       width = Math.min(width, this.texture.getWidth());
/* 188 */       height = Math.min(height, this.texture.getHeight());
/*     */       
/* 190 */       WritableRaster raster = scratchImage.getRaster();
/* 191 */       int[] row = new int[width];
/* 192 */       for (int y = 0; y < height; y++) {
/* 193 */         raster.getDataElements(0, y, width, 1, row);
/* 194 */         scratchIntBuffer.put(row);
/*     */       } 
/* 196 */       format = 32993;
/*     */     } 
/*     */ 
/*     */     
/* 200 */     String hash = "";
/*     */     try {
/* 202 */       MessageDigest md = MessageDigest.getInstance("SHA-256");
/* 203 */       md.update(glyphPixels);
/* 204 */       BigInteger bigInt = new BigInteger(1, md.digest());
/* 205 */       hash = bigInt.toString(16);
/* 206 */     } catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
/*     */     
/* 208 */     scratchByteBuffer.clear();
/* 209 */     scratchIntBuffer.clear();
/*     */     
/*     */     try {
/* 212 */       for (int i = 0, n = this.hashes.size(); i < n; i++) {
/* 213 */         String other = this.hashes.get(i);
/* 214 */         if (other.equals(hash)) {
/* 215 */           Glyph dupe = this.pageGlyphs.get(i);
/* 216 */           glyph.setTexture(dupe.texture, dupe.u, dupe.v, dupe.u2, dupe.v2);
/* 217 */           return false;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 221 */       this.hashes.add(hash);
/* 222 */       this.pageGlyphs.add(glyph);
/*     */     } 
/*     */     
/* 225 */     Gdx.gl.glTexSubImage2D(this.texture.glTarget, 0, pageX, pageY, width, height, format, 5121, glyphPixels);
/*     */     
/* 227 */     float u = pageX / this.texture.getWidth();
/* 228 */     float v = pageY / this.texture.getHeight();
/* 229 */     float u2 = (pageX + width) / this.texture.getWidth();
/* 230 */     float v2 = (pageY + height) / this.texture.getHeight();
/* 231 */     glyph.setTexture(this.texture, u, v, u2, v2);
/*     */     
/* 233 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Glyph> getGlyphs() {
/* 238 */     return this.pageGlyphs;
/*     */   }
/*     */ 
/*     */   
/*     */   public Texture getTexture() {
/* 243 */     return this.texture;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 248 */   private static ByteBuffer scratchByteBuffer = ByteBuffer.allocateDirect(262144);
/*     */   
/*     */   static {
/* 251 */     scratchByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
/*     */   }
/*     */   
/* 254 */   private static IntBuffer scratchIntBuffer = scratchByteBuffer.asIntBuffer();
/*     */   
/* 256 */   private static BufferedImage scratchImage = new BufferedImage(256, 256, 2);
/* 257 */   static Graphics2D scratchGraphics = (Graphics2D)scratchImage.getGraphics();
/*     */   
/*     */   static {
/* 260 */     scratchGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 261 */     scratchGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*     */   }
/*     */   
/* 264 */   public static FontRenderContext renderContext = scratchGraphics.getFontRenderContext();
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\GlyphPage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */