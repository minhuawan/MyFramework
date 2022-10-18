/*     */ package com.badlogic.gdx.tools.bmfont;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.PixmapIO;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.PixmapPacker;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BitmapFontWriter
/*     */ {
/*     */   public enum OutputFormat
/*     */   {
/*  43 */     Text,
/*     */     
/*  45 */     XML;
/*     */   }
/*     */ 
/*     */   
/*  49 */   private static OutputFormat format = OutputFormat.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setOutputFormat(OutputFormat fmt) {
/*  56 */     if (fmt == null) throw new NullPointerException("format cannot be null"); 
/*  57 */     format = fmt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static OutputFormat getOutputFormat() {
/*  63 */     return format;
/*     */   }
/*     */   
/*     */   public static class Padding { public int up;
/*     */     public int down;
/*     */     public int left;
/*     */     public int right;
/*     */     
/*     */     public Padding() {}
/*     */     
/*     */     public Padding(int up, int down, int left, int right) {
/*  74 */       this.up = up;
/*  75 */       this.down = down;
/*  76 */       this.left = left;
/*  77 */       this.right = right;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Spacing
/*     */   {
/*     */     public int horizontal;
/*     */     
/*     */     public int vertical;
/*     */   }
/*     */   
/*     */   public static class FontInfo
/*     */   {
/*     */     public String face;
/*  92 */     public int size = 12;
/*     */     
/*     */     public boolean bold;
/*     */     
/*     */     public boolean italic;
/*     */     
/*     */     public String charset;
/*     */     
/*     */     public boolean unicode = true;
/*     */     
/* 102 */     public int stretchH = 100;
/*     */     
/*     */     public boolean smooth = true;
/*     */     
/* 106 */     public int aa = 2;
/*     */     
/* 108 */     public BitmapFontWriter.Padding padding = new BitmapFontWriter.Padding();
/*     */     
/* 110 */     public BitmapFontWriter.Spacing spacing = new BitmapFontWriter.Spacing();
/* 111 */     public int outline = 0;
/*     */ 
/*     */     
/*     */     public FontInfo() {}
/*     */     
/*     */     public FontInfo(String face, int size) {
/* 117 */       this.face = face;
/* 118 */       this.size = size;
/*     */     }
/*     */   }
/*     */   
/*     */   private static String quote(Object params) {
/* 123 */     return quote(params, false);
/*     */   }
/*     */   
/*     */   private static String quote(Object params, boolean spaceAfter) {
/* 127 */     if (getOutputFormat() == OutputFormat.XML) {
/* 128 */       return "\"" + params.toString().trim() + "\"" + (spaceAfter ? " " : "");
/*     */     }
/* 130 */     return params.toString();
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
/*     */   public static void writeFont(BitmapFont.BitmapFontData fontData, String[] pageRefs, FileHandle outFntFile, FontInfo info, int scaleW, int scaleH) {
/* 152 */     if (info == null) {
/* 153 */       info = new FontInfo();
/* 154 */       info.face = outFntFile.nameWithoutExtension();
/*     */     } 
/*     */     
/* 157 */     int lineHeight = (int)fontData.lineHeight;
/* 158 */     int pages = pageRefs.length;
/* 159 */     int packed = 0;
/* 160 */     int base = (int)(fontData.capHeight + (fontData.flipped ? -fontData.ascent : fontData.ascent));
/* 161 */     OutputFormat fmt = getOutputFormat();
/* 162 */     boolean xml = (fmt == OutputFormat.XML);
/*     */     
/* 164 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 166 */     if (xml) {
/* 167 */       buf.append("<font>\n");
/*     */     }
/* 169 */     String xmlOpen = xml ? "\t<" : "";
/* 170 */     String xmlCloseSelf = xml ? "/>" : "";
/* 171 */     String xmlTab = xml ? "\t" : "";
/* 172 */     String xmlClose = xml ? ">" : "";
/*     */     
/* 174 */     String xmlQuote = xml ? "\"" : "";
/* 175 */     String alphaChnlParams = xml ? " alphaChnl=\"0\" redChnl=\"0\" greenChnl=\"0\" blueChnl=\"0\"" : " alphaChnl=0 redChnl=0 greenChnl=0 blueChnl=0";
/*     */ 
/*     */ 
/*     */     
/* 179 */     buf.append(xmlOpen).append("info face=\"").append((info.face == null) ? "" : info.face.replaceAll("\"", "'"))
/* 180 */       .append("\" size=").append(quote(Integer.valueOf(info.size))).append(" bold=").append(quote(Integer.valueOf(info.bold ? 1 : 0))).append(" italic=")
/* 181 */       .append(quote(Integer.valueOf(info.italic ? 1 : 0))).append(" charset=\"").append((info.charset == null) ? "" : info.charset)
/* 182 */       .append("\" unicode=").append(quote(Integer.valueOf(info.unicode ? 1 : 0))).append(" stretchH=").append(quote(Integer.valueOf(info.stretchH)))
/* 183 */       .append(" smooth=").append(quote(Integer.valueOf(info.smooth ? 1 : 0))).append(" aa=").append(quote(Integer.valueOf(info.aa))).append(" padding=")
/* 184 */       .append(xmlQuote).append(info.padding.up).append(",").append(info.padding.right).append(",").append(info.padding.down)
/* 185 */       .append(",").append(info.padding.left).append(xmlQuote).append(" spacing=").append(xmlQuote)
/* 186 */       .append(info.spacing.horizontal).append(",").append(info.spacing.vertical).append(xmlQuote).append(xmlCloseSelf)
/* 187 */       .append("\n");
/*     */ 
/*     */     
/* 190 */     buf.append(xmlOpen).append("common lineHeight=").append(quote(Integer.valueOf(lineHeight))).append(" base=").append(quote(Integer.valueOf(base)))
/* 191 */       .append(" scaleW=").append(quote(Integer.valueOf(scaleW))).append(" scaleH=").append(quote(Integer.valueOf(scaleH))).append(" pages=").append(quote(Integer.valueOf(pages)))
/* 192 */       .append(" packed=").append(quote(Integer.valueOf(packed))).append(alphaChnlParams).append(xmlCloseSelf).append("\n");
/*     */     
/* 194 */     if (xml) buf.append("\t<pages>\n");
/*     */ 
/*     */     
/* 197 */     for (int i = 0; i < pageRefs.length; i++) {
/* 198 */       buf.append(xmlTab).append(xmlOpen).append("page id=").append(quote(Integer.valueOf(i))).append(" file=\"").append(pageRefs[i])
/* 199 */         .append("\"").append(xmlCloseSelf).append("\n");
/*     */     }
/*     */     
/* 202 */     if (xml) buf.append("\t</pages>\n");
/*     */ 
/*     */     
/* 205 */     Array<BitmapFont.Glyph> glyphs = new Array(256);
/* 206 */     for (int j = 0; j < fontData.glyphs.length; j++) {
/* 207 */       if (fontData.glyphs[j] != null)
/*     */       {
/* 209 */         for (int n = 0; n < (fontData.glyphs[j]).length; n++) {
/* 210 */           if (fontData.glyphs[j][n] != null) {
/* 211 */             glyphs.add(fontData.glyphs[j][n]);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 216 */     buf.append(xmlOpen).append("chars count=").append(quote(Integer.valueOf(glyphs.size))).append(xmlClose).append("\n");
/*     */     
/* 218 */     int padLeft = 0, padRight = 0, padTop = 0, padX = 0, padY = 0;
/* 219 */     if (info != null) {
/* 220 */       padTop = info.padding.up;
/* 221 */       padLeft = info.padding.left;
/* 222 */       padRight = info.padding.right;
/* 223 */       padX = padLeft + padRight;
/* 224 */       padY = info.padding.up + info.padding.down;
/*     */     } 
/*     */ 
/*     */     
/* 228 */     for (int k = 0; k < glyphs.size; k++) {
/* 229 */       BitmapFont.Glyph g = (BitmapFont.Glyph)glyphs.get(k);
/* 230 */       boolean empty = (g.width == 0 || g.height == 0);
/* 231 */       buf.append(xmlTab).append(xmlOpen).append("char id=").append(quote(String.format("%-6s", new Object[] { Integer.valueOf(g.id) }), true)).append("x=")
/* 232 */         .append(quote(String.format("%-5s", new Object[] { Integer.valueOf(empty ? 0 : (g.srcX - padLeft)) }), true)).append("y=")
/* 233 */         .append(quote(String.format("%-5s", new Object[] { Integer.valueOf(empty ? 0 : (g.srcY - padRight)) }), true)).append("width=")
/* 234 */         .append(quote(String.format("%-5s", new Object[] { Integer.valueOf(empty ? 0 : (g.width + padX)) }), true)).append("height=")
/* 235 */         .append(quote(String.format("%-5s", new Object[] { Integer.valueOf(empty ? 0 : (g.height + padY)) }), true)).append("xoffset=")
/* 236 */         .append(quote(String.format("%-5s", new Object[] { Integer.valueOf(g.xoffset - padLeft) }), true)).append("yoffset=")
/* 237 */         .append(
/* 238 */           quote(String.format("%-5s", new Object[] { Integer.valueOf(fontData.flipped ? (g.yoffset + padTop) : -(g.height + g.yoffset + padTop)) }), true))
/* 239 */         .append("xadvance=").append(quote(String.format("%-5s", new Object[] { Integer.valueOf(g.xadvance) }), true)).append("page=")
/* 240 */         .append(quote(String.format("%-5s", new Object[] { Integer.valueOf(g.page) }), true)).append("chnl=").append(quote(Integer.valueOf(0), true)).append(xmlCloseSelf)
/* 241 */         .append("\n");
/*     */     } 
/*     */     
/* 244 */     if (xml) buf.append("\t</chars>\n");
/*     */ 
/*     */     
/* 247 */     int kernCount = 0;
/* 248 */     StringBuilder kernBuf = new StringBuilder();
/* 249 */     for (int m = 0; m < glyphs.size; m++) {
/* 250 */       for (int n = 0; n < glyphs.size; n++) {
/* 251 */         BitmapFont.Glyph first = (BitmapFont.Glyph)glyphs.get(m);
/* 252 */         BitmapFont.Glyph second = (BitmapFont.Glyph)glyphs.get(n);
/* 253 */         int kern = first.getKerning((char)second.id);
/* 254 */         if (kern != 0) {
/* 255 */           kernCount++;
/* 256 */           kernBuf.append(xmlTab).append(xmlOpen).append("kerning first=").append(quote(Integer.valueOf(first.id))).append(" second=")
/* 257 */             .append(quote(Integer.valueOf(second.id))).append(" amount=").append(quote(Integer.valueOf(kern), true)).append(xmlCloseSelf).append("\n");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 263 */     buf.append(xmlOpen).append("kernings count=").append(quote(Integer.valueOf(kernCount))).append(xmlClose).append("\n");
/* 264 */     buf.append(kernBuf);
/*     */     
/* 266 */     if (xml) {
/* 267 */       buf.append("\t</kernings>\n");
/* 268 */       buf.append("</font>");
/*     */     } 
/*     */     
/* 271 */     String charset = info.charset;
/* 272 */     if (charset != null && charset.length() == 0) charset = null;
/*     */     
/* 274 */     outFntFile.writeString(buf.toString(), false, charset);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeFont(BitmapFont.BitmapFontData fontData, Pixmap[] pages, FileHandle outFntFile, FontInfo info) {
/* 295 */     String[] pageRefs = writePixmaps(pages, outFntFile.parent(), outFntFile.nameWithoutExtension());
/*     */ 
/*     */     
/* 298 */     writeFont(fontData, pageRefs, outFntFile, info, pages[0].getWidth(), pages[0].getHeight());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] writePixmaps(Pixmap[] pages, FileHandle outputDir, String fileName) {
/* 316 */     if (pages == null || pages.length == 0) throw new IllegalArgumentException("no pixmaps supplied to BitmapFontWriter.write");
/*     */     
/* 318 */     String[] pageRefs = new String[pages.length];
/*     */     
/* 320 */     for (int i = 0; i < pages.length; i++) {
/* 321 */       String ref = (pages.length == 1) ? (fileName + ".png") : (fileName + "_" + i + ".png");
/*     */ 
/*     */       
/* 324 */       pageRefs[i] = ref;
/*     */ 
/*     */       
/* 327 */       PixmapIO.writePNG(outputDir.child(ref), pages[i]);
/*     */     } 
/* 329 */     return pageRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] writePixmaps(Array<PixmapPacker.Page> pages, FileHandle outputDir, String fileName) {
/* 340 */     Pixmap[] pix = new Pixmap[pages.size];
/* 341 */     for (int i = 0; i < pages.size; i++) {
/* 342 */       pix[i] = ((PixmapPacker.Page)pages.get(i)).getPixmap();
/*     */     }
/* 344 */     return writePixmaps(pix, outputDir, fileName);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\bmfont\BitmapFontWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */