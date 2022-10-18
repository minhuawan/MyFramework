/*     */ package com.badlogic.gdx.tools.hiero;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.ConfigurableEffect;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class HieroSettings
/*     */ {
/*     */   private static final String RENDER_TYPE = "render_type";
/*  40 */   private String fontName = "Arial"; private boolean bold; private boolean italic; private boolean mono; private float gamma; private int paddingTop;
/*  41 */   private int fontSize = 12; private int paddingLeft; private int paddingBottom;
/*     */   private int paddingRight;
/*     */   private int paddingAdvanceX;
/*     */   private int paddingAdvanceY;
/*  45 */   private int glyphPageWidth = 512; private int glyphPageHeight = 512;
/*  46 */   private String glyphText = "";
/*  47 */   private final List effects = new ArrayList();
/*     */   private boolean nativeRendering;
/*     */   private boolean font2Active = false;
/*  50 */   private String font2File = "";
/*  51 */   private int renderType = UnicodeFont.RenderType.FreeType.ordinal();
/*     */ 
/*     */   
/*     */   public HieroSettings() {}
/*     */ 
/*     */   
/*     */   public HieroSettings(String hieroFileRef) {
/*     */     try {
/*  59 */       BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.absolute(hieroFileRef).read(), "UTF-8"));
/*     */       while (true) {
/*  61 */         String line = reader.readLine();
/*  62 */         if (line == null)
/*  63 */           break;  line = line.trim();
/*  64 */         if (line.length() == 0)
/*  65 */           continue;  String[] pieces = line.split("=", 2);
/*  66 */         String name = pieces[0].trim();
/*  67 */         String value = pieces[1];
/*  68 */         if (name.equals("font.name")) {
/*  69 */           this.fontName = value; continue;
/*  70 */         }  if (name.equals("font.size")) {
/*  71 */           this.fontSize = Integer.parseInt(value); continue;
/*  72 */         }  if (name.equals("font.gamma")) {
/*  73 */           this.gamma = Float.parseFloat(value); continue;
/*  74 */         }  if (name.equals("font.mono")) {
/*  75 */           this.mono = Boolean.parseBoolean(value); continue;
/*  76 */         }  if (name.equals("font.size")) {
/*  77 */           this.fontSize = Integer.parseInt(value); continue;
/*  78 */         }  if (name.equals("font.bold")) {
/*  79 */           this.bold = Boolean.parseBoolean(value); continue;
/*  80 */         }  if (name.equals("font.italic")) {
/*  81 */           this.italic = Boolean.parseBoolean(value); continue;
/*  82 */         }  if (name.equals("font2.file")) {
/*  83 */           this.font2File = value; continue;
/*  84 */         }  if (name.equals("font2.use")) {
/*  85 */           this.font2Active = Boolean.parseBoolean(value); continue;
/*  86 */         }  if (name.equals("pad.top")) {
/*  87 */           this.paddingTop = Integer.parseInt(value); continue;
/*  88 */         }  if (name.equals("pad.right")) {
/*  89 */           this.paddingRight = Integer.parseInt(value); continue;
/*  90 */         }  if (name.equals("pad.bottom")) {
/*  91 */           this.paddingBottom = Integer.parseInt(value); continue;
/*  92 */         }  if (name.equals("pad.left")) {
/*  93 */           this.paddingLeft = Integer.parseInt(value); continue;
/*  94 */         }  if (name.equals("pad.advance.x")) {
/*  95 */           this.paddingAdvanceX = Integer.parseInt(value); continue;
/*  96 */         }  if (name.equals("pad.advance.y")) {
/*  97 */           this.paddingAdvanceY = Integer.parseInt(value); continue;
/*  98 */         }  if (name.equals("glyph.page.width")) {
/*  99 */           this.glyphPageWidth = Integer.parseInt(value); continue;
/* 100 */         }  if (name.equals("glyph.page.height")) {
/* 101 */           this.glyphPageHeight = Integer.parseInt(value); continue;
/* 102 */         }  if (name.equals("glyph.native.rendering")) {
/* 103 */           this.nativeRendering = Boolean.parseBoolean(value); continue;
/* 104 */         }  if (name.equals("glyph.text")) {
/* 105 */           this.glyphText = value; continue;
/* 106 */         }  if (name.equals("render_type")) {
/* 107 */           this.renderType = Integer.parseInt(value); continue;
/* 108 */         }  if (name.equals("effect.class")) {
/*     */           try {
/* 110 */             this.effects.add(Class.forName(value).newInstance());
/* 111 */           } catch (Throwable ex) {
/* 112 */             throw new GdxRuntimeException("Unable to create effect instance: " + value, ex);
/*     */           }  continue;
/* 114 */         }  if (name.startsWith("effect.")) {
/*     */           
/* 116 */           name = name.substring(7);
/* 117 */           ConfigurableEffect effect = this.effects.get(this.effects.size() - 1);
/* 118 */           List values = effect.getValues();
/* 119 */           for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
/* 120 */             ConfigurableEffect.Value effectValue = iter.next();
/* 121 */             if (effectValue.getName().equals(name)) {
/* 122 */               effectValue.setString(value);
/*     */               break;
/*     */             } 
/*     */           } 
/* 126 */           effect.setValues(values);
/*     */         } 
/*     */       } 
/* 129 */       reader.close();
/* 130 */     } catch (Throwable ex) {
/* 131 */       throw new GdxRuntimeException("Unable to load Hiero font file: " + hieroFileRef, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingTop() {
/* 137 */     return this.paddingTop;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingTop(int paddingTop) {
/* 142 */     this.paddingTop = paddingTop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingLeft() {
/* 147 */     return this.paddingLeft;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingLeft(int paddingLeft) {
/* 152 */     this.paddingLeft = paddingLeft;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingBottom() {
/* 157 */     return this.paddingBottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingBottom(int paddingBottom) {
/* 162 */     this.paddingBottom = paddingBottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingRight() {
/* 167 */     return this.paddingRight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingRight(int paddingRight) {
/* 172 */     this.paddingRight = paddingRight;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingAdvanceX() {
/* 177 */     return this.paddingAdvanceX;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingAdvanceX(int paddingAdvanceX) {
/* 182 */     this.paddingAdvanceX = paddingAdvanceX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingAdvanceY() {
/* 187 */     return this.paddingAdvanceY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingAdvanceY(int paddingAdvanceY) {
/* 192 */     this.paddingAdvanceY = paddingAdvanceY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlyphPageWidth() {
/* 197 */     return this.glyphPageWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGlyphPageWidth(int glyphPageWidth) {
/* 202 */     this.glyphPageWidth = glyphPageWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlyphPageHeight() {
/* 207 */     return this.glyphPageHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGlyphPageHeight(int glyphPageHeight) {
/* 212 */     this.glyphPageHeight = glyphPageHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFontName() {
/* 218 */     return this.fontName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFontName(String fontName) {
/* 224 */     this.fontName = fontName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFontSize() {
/* 230 */     return this.fontSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFontSize(int fontSize) {
/* 236 */     this.fontSize = fontSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBold() {
/* 242 */     return this.bold;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBold(boolean bold) {
/* 248 */     this.bold = bold;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isItalic() {
/* 254 */     return this.italic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItalic(boolean italic) {
/* 260 */     this.italic = italic;
/*     */   }
/*     */ 
/*     */   
/*     */   public List getEffects() {
/* 265 */     return this.effects;
/*     */   }
/*     */   
/*     */   public boolean getNativeRendering() {
/* 269 */     return this.nativeRendering;
/*     */   }
/*     */   
/*     */   public void setNativeRendering(boolean nativeRendering) {
/* 273 */     this.nativeRendering = nativeRendering;
/*     */   }
/*     */   
/*     */   public String getGlyphText() {
/* 277 */     return this.glyphText.replace("\\n", "\n");
/*     */   }
/*     */   
/*     */   public void setGlyphText(String text) {
/* 281 */     this.glyphText = text.replace("\n", "\\n");
/*     */   }
/*     */   
/*     */   public String getFont2File() {
/* 285 */     return this.font2File;
/*     */   }
/*     */   
/*     */   public void setFont2File(String filename) {
/* 289 */     this.font2File = filename;
/*     */   }
/*     */   
/*     */   public boolean isFont2Active() {
/* 293 */     return this.font2Active;
/*     */   }
/*     */   
/*     */   public void setFont2Active(boolean active) {
/* 297 */     this.font2Active = active;
/*     */   }
/*     */   
/*     */   public boolean isMono() {
/* 301 */     return this.mono;
/*     */   }
/*     */   
/*     */   public void setMono(boolean mono) {
/* 305 */     this.mono = mono;
/*     */   }
/*     */   
/*     */   public float getGamma() {
/* 309 */     return this.gamma;
/*     */   }
/*     */   
/*     */   public void setGamma(float gamma) {
/* 313 */     this.gamma = gamma;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save(File file) throws IOException {
/* 319 */     PrintStream out = new PrintStream(file, "UTF-8");
/* 320 */     out.println("font.name=" + this.fontName);
/* 321 */     out.println("font.size=" + this.fontSize);
/* 322 */     out.println("font.bold=" + this.bold);
/* 323 */     out.println("font.italic=" + this.italic);
/* 324 */     out.println("font.gamma=" + this.gamma);
/* 325 */     out.println("font.mono=" + this.mono);
/* 326 */     out.println();
/* 327 */     out.println("font2.file=" + this.font2File);
/* 328 */     out.println("font2.use=" + this.font2Active);
/* 329 */     out.println();
/* 330 */     out.println("pad.top=" + this.paddingTop);
/* 331 */     out.println("pad.right=" + this.paddingRight);
/* 332 */     out.println("pad.bottom=" + this.paddingBottom);
/* 333 */     out.println("pad.left=" + this.paddingLeft);
/* 334 */     out.println("pad.advance.x=" + this.paddingAdvanceX);
/* 335 */     out.println("pad.advance.y=" + this.paddingAdvanceY);
/* 336 */     out.println();
/* 337 */     out.println("glyph.native.rendering=" + this.nativeRendering);
/* 338 */     out.println("glyph.page.width=" + this.glyphPageWidth);
/* 339 */     out.println("glyph.page.height=" + this.glyphPageHeight);
/* 340 */     out.println("glyph.text=" + this.glyphText);
/* 341 */     out.println();
/* 342 */     out.println("render_type=" + this.renderType);
/* 343 */     out.println();
/* 344 */     for (Iterator<ConfigurableEffect> iter = this.effects.iterator(); iter.hasNext(); ) {
/* 345 */       ConfigurableEffect effect = iter.next();
/* 346 */       out.println("effect.class=" + effect.getClass().getName());
/* 347 */       for (Iterator<ConfigurableEffect.Value> iter2 = effect.getValues().iterator(); iter2.hasNext(); ) {
/* 348 */         ConfigurableEffect.Value value = iter2.next();
/* 349 */         out.println("effect." + value.getName() + "=" + value.getString());
/*     */       } 
/* 351 */       out.println();
/*     */     } 
/* 353 */     out.close();
/*     */   }
/*     */   
/*     */   public void setRenderType(int renderType) {
/* 357 */     this.renderType = renderType;
/*     */   }
/*     */   
/*     */   public int getRenderType() {
/* 361 */     return this.renderType;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hiero\HieroSettings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */