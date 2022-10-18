/*     */ package com.badlogic.gdx.tools.hiero;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.Glyph;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.GlyphPage;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*     */ import com.badlogic.gdx.utils.IntIntMap;
/*     */ import java.awt.Font;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.lwjgl.BufferUtils;
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
/*     */ public class BMFontUtil
/*     */ {
/*     */   private final UnicodeFont unicodeFont;
/*     */   
/*     */   public BMFontUtil(UnicodeFont unicodeFont) {
/*  53 */     this.unicodeFont = unicodeFont;
/*     */   }
/*     */   
/*     */   public void save(File outputBMFontFile) throws IOException {
/*  57 */     File outputDir = outputBMFontFile.getParentFile();
/*  58 */     String outputName = outputBMFontFile.getName();
/*  59 */     if (outputName.endsWith(".fnt")) outputName = outputName.substring(0, outputName.length() - 4);
/*     */ 
/*     */     
/*  62 */     getGlyph(' ');
/*  63 */     getGlyph(false);
/*  64 */     this.unicodeFont.loadGlyphs();
/*     */     
/*  66 */     PrintStream out = new PrintStream(new FileOutputStream(new File(outputDir, outputName + ".fnt")));
/*  67 */     Font font = this.unicodeFont.getFont();
/*  68 */     int pageWidth = this.unicodeFont.getGlyphPageWidth();
/*  69 */     int pageHeight = this.unicodeFont.getGlyphPageHeight();
/*  70 */     out.println("info face=\"" + font.getFontName() + "\" size=" + font.getSize() + " bold=" + (font.isBold() ? 1 : 0) + " italic=" + (
/*  71 */         font.isItalic() ? 1 : 0) + " charset=\"\" unicode=0 stretchH=100 smooth=1 aa=1 padding=" + this.unicodeFont
/*  72 */         .getPaddingTop() + "," + this.unicodeFont.getPaddingRight() + "," + this.unicodeFont.getPaddingBottom() + "," + this.unicodeFont
/*  73 */         .getPaddingLeft() + " spacing=" + this.unicodeFont.getPaddingAdvanceX() + "," + this.unicodeFont
/*  74 */         .getPaddingAdvanceY());
/*  75 */     out.println("common lineHeight=" + this.unicodeFont.getLineHeight() + " base=" + this.unicodeFont.getAscent() + " scaleW=" + pageWidth + " scaleH=" + pageHeight + " pages=" + this.unicodeFont
/*  76 */         .getGlyphPages().size() + " packed=0");
/*     */     
/*  78 */     int pageIndex = 0, glyphCount = 0;
/*  79 */     for (Iterator<GlyphPage> pageIter = this.unicodeFont.getGlyphPages().iterator(); pageIter.hasNext(); ) {
/*  80 */       String fileName; GlyphPage page = pageIter.next();
/*     */       
/*  82 */       if (pageIndex == 0 && !pageIter.hasNext()) {
/*  83 */         fileName = outputName + ".png";
/*     */       } else {
/*  85 */         fileName = outputName + (pageIndex + 1) + ".png";
/*  86 */       }  out.println("page id=" + pageIndex + " file=\"" + fileName + "\"");
/*  87 */       glyphCount += page.getGlyphs().size();
/*  88 */       pageIndex++;
/*     */     } 
/*     */     
/*  91 */     out.println("chars count=" + glyphCount);
/*     */     
/*  93 */     pageIndex = 0;
/*  94 */     List allGlyphs = new ArrayList(512);
/*  95 */     for (Iterator<GlyphPage> iterator1 = this.unicodeFont.getGlyphPages().iterator(); iterator1.hasNext(); ) {
/*  96 */       GlyphPage page = iterator1.next();
/*  97 */       List<Glyph> glyphs = page.getGlyphs();
/*  98 */       Collections.sort(glyphs, new Comparator<Glyph>() {
/*     */             public int compare(Glyph o1, Glyph o2) {
/* 100 */               return o1.getCodePoint() - o2.getCodePoint();
/*     */             }
/*     */           });
/* 103 */       for (Iterator<Glyph> glyphIter = page.getGlyphs().iterator(); glyphIter.hasNext(); ) {
/* 104 */         Glyph glyph = glyphIter.next();
/* 105 */         writeGlyph(out, pageWidth, pageHeight, pageIndex, glyph);
/*     */       } 
/* 107 */       allGlyphs.addAll(page.getGlyphs());
/* 108 */       pageIndex++;
/*     */     } 
/*     */     
/* 111 */     String ttfFileRef = this.unicodeFont.getFontFile();
/* 112 */     if (ttfFileRef == null) {
/* 113 */       System.out.println("Kerning information could not be output because a TTF font file was not specified.");
/*     */     } else {
/* 115 */       Kerning kerning = new Kerning();
/*     */       try {
/* 117 */         kerning.load(Gdx.files.internal(ttfFileRef).read(), font.getSize());
/* 118 */       } catch (IOException ex) {
/* 119 */         System.out.println("Unable to read kerning information from font: " + ttfFileRef);
/* 120 */         ex.printStackTrace();
/*     */       } 
/*     */       
/* 123 */       IntIntMap glyphCodeToCodePoint = new IntIntMap();
/* 124 */       for (Iterator<Glyph> iter = allGlyphs.iterator(); iter.hasNext(); ) {
/* 125 */         Glyph glyph = iter.next();
/* 126 */         glyphCodeToCodePoint.put((new Integer(getGlyphCode(font, glyph.getCodePoint()))).intValue(), (new Integer(glyph.getCodePoint())).intValue());
/*     */       } 
/*     */       
/* 129 */       List<KerningPair> kernings = new ArrayList(256);
/*     */ 
/*     */ 
/*     */       
/* 133 */       for (IntIntMap.Entry entry : kerning.getKernings()) {
/* 134 */         int firstGlyphCode = entry.key >> 16;
/* 135 */         int secondGlyphCode = entry.key & 0xFFFF;
/* 136 */         int offset = entry.value;
/* 137 */         int firstCodePoint = glyphCodeToCodePoint.get(firstGlyphCode, -1);
/* 138 */         int secondCodePoint = glyphCodeToCodePoint.get(secondGlyphCode, -1);
/*     */         
/* 140 */         if (firstCodePoint == -1 || secondCodePoint == -1 || offset == 0) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 145 */         KerningPair pair = new KerningPair();
/* 146 */         pair.firstCodePoint = firstCodePoint;
/* 147 */         pair.secondCodePoint = secondCodePoint;
/* 148 */         pair.offset = offset;
/* 149 */         kernings.add(pair);
/*     */       } 
/* 151 */       out.println("kernings count=" + kernings.size());
/* 152 */       for (Iterator<KerningPair> iterator = kernings.iterator(); iterator.hasNext(); ) {
/* 153 */         KerningPair pair = iterator.next();
/* 154 */         out.println("kerning first=" + pair.firstCodePoint + " second=" + pair.secondCodePoint + " amount=" + pair.offset);
/*     */       } 
/*     */     }  class KerningPair {
/* 157 */       public int firstCodePoint; public int secondCodePoint; public int offset; }; out.close();
/*     */     
/* 159 */     int width = this.unicodeFont.getGlyphPageWidth();
/* 160 */     int height = this.unicodeFont.getGlyphPageHeight();
/* 161 */     IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
/* 162 */     BufferedImage pageImage = new BufferedImage(width, height, 2);
/* 163 */     int[] row = new int[width];
/*     */     
/* 165 */     pageIndex = 0;
/* 166 */     for (Iterator<GlyphPage> iterator2 = this.unicodeFont.getGlyphPages().iterator(); iterator2.hasNext(); ) {
/* 167 */       String fileName; GlyphPage page = iterator2.next();
/*     */       
/* 169 */       if (pageIndex == 0 && !iterator2.hasNext()) {
/* 170 */         fileName = outputName + ".png";
/*     */       } else {
/* 172 */         fileName = outputName + (pageIndex + 1) + ".png";
/*     */       } 
/* 174 */       page.getTexture().bind();
/* 175 */       buffer.clear();
/* 176 */       GL11.glGetTexImage(3553, 0, 32993, 5121, buffer);
/* 177 */       WritableRaster raster = pageImage.getRaster();
/* 178 */       for (int y = 0; y < height; y++) {
/* 179 */         buffer.get(row);
/* 180 */         raster.setDataElements(0, y, width, 1, row);
/*     */       } 
/* 182 */       File imageOutputFile = new File(outputDir, fileName);
/* 183 */       ImageIO.write(pageImage, "png", imageOutputFile);
/*     */       
/* 185 */       pageIndex++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Glyph getGlyph(char c) {
/* 191 */     char[] chars = { c };
/* 192 */     GlyphVector vector = this.unicodeFont.getFont().layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/*     */     
/* 194 */     Rectangle bounds = vector.getGlyphPixelBounds(0, GlyphPage.renderContext, 0.0F, 0.0F);
/* 195 */     return this.unicodeFont.getGlyph(vector.getGlyphCode(0), c, bounds, vector, 0);
/*     */   }
/*     */   
/*     */   void writeGlyph(PrintStream out, int pageWidth, int pageHeight, int pageIndex, Glyph glyph) {
/* 199 */     out.println("char id=" + String.format("%-7s ", new Object[] { Integer.valueOf(glyph.getCodePoint()) }) + "x=" + 
/* 200 */         String.format("%-5s", new Object[] { Integer.valueOf((int)(glyph.getU() * pageWidth)) }) + "y=" + 
/* 201 */         String.format("%-5s", new Object[] { Integer.valueOf((int)(glyph.getV() * pageHeight)) }) + "width=" + 
/* 202 */         String.format("%-5s", new Object[] { Integer.valueOf(glyph.getWidth()) }) + "height=" + 
/* 203 */         String.format("%-5s", new Object[] { Integer.valueOf(glyph.getHeight()) }) + "xoffset=" + 
/* 204 */         String.format("%-5s", new Object[] { Integer.valueOf(glyph.getXOffset()) }) + "yoffset=" + 
/* 205 */         String.format("%-5s", new Object[] { Integer.valueOf(glyph.getYOffset()) }) + "xadvance=" + 
/* 206 */         String.format("%-5s", new Object[] { Integer.valueOf(glyph.getXAdvance()) }) + "page=" + 
/* 207 */         String.format("%-5s", new Object[] { Integer.valueOf(pageIndex) }) + "chnl=0 ");
/*     */   }
/*     */ 
/*     */   
/*     */   private int getGlyphCode(Font font, int codePoint) {
/* 212 */     char[] chars = Character.toChars(codePoint);
/* 213 */     GlyphVector vector = font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/* 214 */     return vector.getGlyphCode(0);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hiero\BMFontUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */