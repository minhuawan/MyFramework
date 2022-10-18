/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import com.badlogic.gdx.utils.Pools;
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
/*     */ 
/*     */ public class TextArea
/*     */   extends TextField
/*     */ {
/*     */   IntArray linesBreak;
/*     */   private String lastText;
/*     */   int cursorLine;
/*     */   int firstLineShowing;
/*     */   private int linesShowing;
/*     */   float moveOffset;
/*     */   private float prefRows;
/*     */   
/*     */   public TextArea(String text, Skin skin) {
/*  57 */     super(text, skin);
/*     */   }
/*     */   
/*     */   public TextArea(String text, Skin skin, String styleName) {
/*  61 */     super(text, skin, styleName);
/*     */   }
/*     */   
/*     */   public TextArea(String text, TextField.TextFieldStyle style) {
/*  65 */     super(text, style);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initialize() {
/*  70 */     super.initialize();
/*  71 */     this.writeEnters = true;
/*  72 */     this.linesBreak = new IntArray();
/*  73 */     this.cursorLine = 0;
/*  74 */     this.firstLineShowing = 0;
/*  75 */     this.moveOffset = -1.0F;
/*  76 */     this.linesShowing = 0;
/*     */   }
/*     */   
/*     */   protected int letterUnderCursor(float x) {
/*  80 */     if (this.linesBreak.size > 0) {
/*  81 */       if (this.cursorLine * 2 >= this.linesBreak.size) {
/*  82 */         return this.text.length();
/*     */       }
/*  84 */       float[] glyphPositions = this.glyphPositions.items;
/*  85 */       int start = this.linesBreak.items[this.cursorLine * 2];
/*  86 */       x += glyphPositions[start];
/*  87 */       int end = this.linesBreak.items[this.cursorLine * 2 + 1];
/*  88 */       int i = start;
/*  89 */       for (; i < end && 
/*  90 */         glyphPositions[i] <= x; i++);
/*  91 */       if (glyphPositions[i] - x <= x - glyphPositions[i - 1]) return i; 
/*  92 */       return Math.max(0, i - 1);
/*     */     } 
/*     */     
/*  95 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefRows(float prefRows) {
/* 101 */     this.prefRows = prefRows;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPrefHeight() {
/* 106 */     if (this.prefRows <= 0.0F) {
/* 107 */       return super.getPrefHeight();
/*     */     }
/* 109 */     float prefHeight = this.textHeight * this.prefRows;
/* 110 */     if (this.style.background != null) {
/* 111 */       prefHeight = Math.max(prefHeight + this.style.background.getBottomHeight() + this.style.background.getTopHeight(), this.style.background
/* 112 */           .getMinHeight());
/*     */     }
/* 114 */     return prefHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLines() {
/* 120 */     return this.linesBreak.size / 2 + (newLineAtEnd() ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean newLineAtEnd() {
/* 125 */     return (this.text.length() != 0 && (this.text
/* 126 */       .charAt(this.text.length() - 1) == '\n' || this.text.charAt(this.text.length() - 1) == '\r'));
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveCursorLine(int line) {
/* 131 */     if (line < 0) {
/* 132 */       this.cursorLine = 0;
/* 133 */       this.cursor = 0;
/* 134 */       this.moveOffset = -1.0F;
/* 135 */     } else if (line >= getLines()) {
/* 136 */       int newLine = getLines() - 1;
/* 137 */       this.cursor = this.text.length();
/* 138 */       if (line > getLines() || newLine == this.cursorLine) {
/* 139 */         this.moveOffset = -1.0F;
/*     */       }
/* 141 */       this.cursorLine = newLine;
/* 142 */     } else if (line != this.cursorLine) {
/* 143 */       if (this.moveOffset < 0.0F) {
/* 144 */         this
/* 145 */           .moveOffset = (this.linesBreak.size <= this.cursorLine * 2) ? 0.0F : (this.glyphPositions.get(this.cursor) - this.glyphPositions.get(this.linesBreak.get(this.cursorLine * 2)));
/*     */       }
/* 147 */       this.cursorLine = line;
/* 148 */       this.cursor = (this.cursorLine * 2 >= this.linesBreak.size) ? this.text.length() : this.linesBreak.get(this.cursorLine * 2);
/* 149 */       while (this.cursor < this.text.length() && this.cursor <= this.linesBreak.get(this.cursorLine * 2 + 1) - 1 && this.glyphPositions
/* 150 */         .get(this.cursor) - this.glyphPositions.get(this.linesBreak.get(this.cursorLine * 2)) < this.moveOffset) {
/* 151 */         this.cursor++;
/*     */       }
/* 153 */       showCursor();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void updateCurrentLine() {
/* 159 */     int index = calculateCurrentLineIndex(this.cursor);
/* 160 */     int line = index / 2;
/*     */ 
/*     */     
/* 163 */     if (index % 2 == 0 || index + 1 >= this.linesBreak.size || this.cursor != this.linesBreak.items[index] || this.linesBreak.items[index + 1] != this.linesBreak.items[index])
/*     */     {
/* 165 */       if (line < this.linesBreak.size / 2 || this.text.length() == 0 || this.text.charAt(this.text.length() - 1) == '\n' || this.text
/* 166 */         .charAt(this.text.length() - 1) == '\r') {
/* 167 */         this.cursorLine = line;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void showCursor() {
/* 174 */     updateCurrentLine();
/* 175 */     if (this.cursorLine != this.firstLineShowing) {
/* 176 */       int step = (this.cursorLine >= this.firstLineShowing) ? 1 : -1;
/* 177 */       while (this.firstLineShowing > this.cursorLine || this.firstLineShowing + this.linesShowing - 1 < this.cursorLine) {
/* 178 */         this.firstLineShowing += step;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int calculateCurrentLineIndex(int cursor) {
/* 185 */     int index = 0;
/* 186 */     while (index < this.linesBreak.size && cursor > this.linesBreak.items[index]) {
/* 187 */       index++;
/*     */     }
/* 189 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sizeChanged() {
/* 196 */     this.lastText = null;
/*     */ 
/*     */     
/* 199 */     BitmapFont font = this.style.font;
/* 200 */     Drawable background = this.style.background;
/* 201 */     float availableHeight = getHeight() - ((background == null) ? 0.0F : (background.getBottomHeight() + background.getTopHeight()));
/* 202 */     this.linesShowing = (int)Math.floor((availableHeight / font.getLineHeight()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getTextY(BitmapFont font, Drawable background) {
/* 207 */     float textY = getHeight();
/* 208 */     if (background != null) {
/* 209 */       textY = (int)(textY - background.getTopHeight());
/*     */     }
/* 211 */     return textY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawSelection(Drawable selection, Batch batch, BitmapFont font, float x, float y) {
/* 216 */     int i = this.firstLineShowing * 2;
/* 217 */     float offsetY = 0.0F;
/* 218 */     int minIndex = Math.min(this.cursor, this.selectionStart);
/* 219 */     int maxIndex = Math.max(this.cursor, this.selectionStart);
/* 220 */     while (i + 1 < this.linesBreak.size && i < (this.firstLineShowing + this.linesShowing) * 2) {
/*     */       
/* 222 */       int lineStart = this.linesBreak.get(i);
/* 223 */       int lineEnd = this.linesBreak.get(i + 1);
/*     */       
/* 225 */       if ((minIndex >= lineStart || minIndex >= lineEnd || maxIndex >= lineStart || maxIndex >= lineEnd) && (minIndex <= lineStart || minIndex <= lineEnd || maxIndex <= lineStart || maxIndex <= lineEnd)) {
/*     */ 
/*     */         
/* 228 */         int start = Math.max(this.linesBreak.get(i), minIndex);
/* 229 */         int end = Math.min(this.linesBreak.get(i + 1), maxIndex);
/*     */         
/* 231 */         float selectionX = this.glyphPositions.get(start) - this.glyphPositions.get(this.linesBreak.get(i));
/* 232 */         float selectionWidth = this.glyphPositions.get(end) - this.glyphPositions.get(start);
/*     */         
/* 234 */         selection.draw(batch, x + selectionX + this.fontOffset, y - this.textHeight - font.getDescent() - offsetY, selectionWidth, font
/* 235 */             .getLineHeight());
/*     */       } 
/*     */       
/* 238 */       offsetY += font.getLineHeight();
/* 239 */       i += 2;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawText(Batch batch, BitmapFont font, float x, float y) {
/* 245 */     float offsetY = 0.0F;
/* 246 */     for (int i = this.firstLineShowing * 2; i < (this.firstLineShowing + this.linesShowing) * 2 && i < this.linesBreak.size; i += 2) {
/* 247 */       font.draw(batch, this.displayText, x, y + offsetY, this.linesBreak.items[i], this.linesBreak.items[i + 1], 0.0F, 8, false);
/* 248 */       offsetY -= font.getLineHeight();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawCursor(Drawable cursorPatch, Batch batch, BitmapFont font, float x, float y) {
/* 255 */     float textOffset = (this.cursor >= this.glyphPositions.size || this.cursorLine * 2 >= this.linesBreak.size) ? 0.0F : (this.glyphPositions.get(this.cursor) - this.glyphPositions.get(this.linesBreak.items[this.cursorLine * 2]));
/* 256 */     cursorPatch.draw(batch, x + textOffset + this.fontOffset + (font.getData()).cursorX, y - font
/* 257 */         .getDescent() / 2.0F - (this.cursorLine - this.firstLineShowing + 1) * font.getLineHeight(), cursorPatch.getMinWidth(), font
/* 258 */         .getLineHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void calculateOffsets() {
/* 263 */     super.calculateOffsets();
/* 264 */     if (!this.text.equals(this.lastText)) {
/* 265 */       this.lastText = this.text;
/* 266 */       BitmapFont font = this.style.font;
/*     */       
/* 268 */       float maxWidthLine = getWidth() - ((this.style.background != null) ? (this.style.background.getLeftWidth() + this.style.background.getRightWidth()) : 0.0F);
/* 269 */       this.linesBreak.clear();
/* 270 */       int lineStart = 0;
/* 271 */       int lastSpace = 0;
/*     */       
/* 273 */       Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
/* 274 */       GlyphLayout layout = (GlyphLayout)layoutPool.obtain();
/* 275 */       for (int i = 0; i < this.text.length(); i++) {
/* 276 */         char lastCharacter = this.text.charAt(i);
/* 277 */         if (lastCharacter == '\r' || lastCharacter == '\n') {
/* 278 */           this.linesBreak.add(lineStart);
/* 279 */           this.linesBreak.add(i);
/* 280 */           lineStart = i + 1;
/*     */         } else {
/* 282 */           lastSpace = continueCursor(i, 0) ? lastSpace : i;
/* 283 */           layout.setText(font, this.text.subSequence(lineStart, i + 1));
/* 284 */           if (layout.width > maxWidthLine) {
/* 285 */             if (lineStart >= lastSpace) {
/* 286 */               lastSpace = i - 1;
/*     */             }
/* 288 */             this.linesBreak.add(lineStart);
/* 289 */             this.linesBreak.add(lastSpace + 1);
/* 290 */             lineStart = lastSpace + 1;
/* 291 */             lastSpace = lineStart;
/*     */           } 
/*     */         } 
/*     */       } 
/* 295 */       layoutPool.free(layout);
/*     */       
/* 297 */       if (lineStart < this.text.length()) {
/* 298 */         this.linesBreak.add(lineStart);
/* 299 */         this.linesBreak.add(this.text.length());
/*     */       } 
/* 301 */       showCursor();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected InputListener createInputListener() {
/* 307 */     return (InputListener)new TextAreaListener();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelection(int selectionStart, int selectionEnd) {
/* 312 */     super.setSelection(selectionStart, selectionEnd);
/* 313 */     updateCurrentLine();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void moveCursor(boolean forward, boolean jump) {
/* 318 */     int count = forward ? 1 : -1;
/* 319 */     int index = this.cursorLine * 2 + count;
/* 320 */     if (index >= 0 && index + 1 < this.linesBreak.size && this.linesBreak.items[index] == this.cursor && this.linesBreak.items[index + 1] == this.cursor) {
/*     */       
/* 322 */       this.cursorLine += count;
/* 323 */       if (jump) {
/* 324 */         super.moveCursor(forward, jump);
/*     */       }
/* 326 */       showCursor();
/*     */     } else {
/* 328 */       super.moveCursor(forward, jump);
/*     */     } 
/* 330 */     updateCurrentLine();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean continueCursor(int index, int offset) {
/* 336 */     int pos = calculateCurrentLineIndex(index + offset);
/* 337 */     return (super.continueCursor(index, offset) && (pos < 0 || pos >= this.linesBreak.size - 2 || this.linesBreak.items[pos + 1] != index || this.linesBreak.items[pos + 1] == this.linesBreak.items[pos + 2]));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCursorLine() {
/* 342 */     return this.cursorLine;
/*     */   }
/*     */   
/*     */   public int getFirstLineShowing() {
/* 346 */     return this.firstLineShowing;
/*     */   }
/*     */   
/*     */   public int getLinesShowing() {
/* 350 */     return this.linesShowing;
/*     */   }
/*     */   
/*     */   public float getCursorX() {
/* 354 */     return this.textOffset + this.fontOffset + (this.style.font.getData()).cursorX;
/*     */   }
/*     */   
/*     */   public float getCursorY() {
/* 358 */     BitmapFont font = this.style.font;
/* 359 */     return -(-font.getDescent() / 2.0F - (this.cursorLine - this.firstLineShowing + 1) * font.getLineHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public class TextAreaListener
/*     */     extends TextField.TextFieldClickListener
/*     */   {
/*     */     protected void setCursorPosition(float x, float y) {
/* 367 */       TextArea.this.moveOffset = -1.0F;
/*     */       
/* 369 */       Drawable background = TextArea.this.style.background;
/* 370 */       BitmapFont font = TextArea.this.style.font;
/*     */       
/* 372 */       float height = TextArea.this.getHeight();
/*     */       
/* 374 */       if (background != null) {
/* 375 */         height -= background.getTopHeight();
/* 376 */         x -= background.getLeftWidth();
/*     */       } 
/* 378 */       x = Math.max(0.0F, x);
/* 379 */       if (background != null) {
/* 380 */         y -= background.getTopHeight();
/*     */       }
/*     */       
/* 383 */       TextArea.this.cursorLine = (int)Math.floor(((height - y) / font.getLineHeight())) + TextArea.this.firstLineShowing;
/* 384 */       TextArea.this.cursorLine = Math.max(0, Math.min(TextArea.this.cursorLine, TextArea.this.getLines() - 1));
/*     */       
/* 386 */       super.setCursorPosition(x, y);
/* 387 */       TextArea.this.updateCurrentLine();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyDown(InputEvent event, int keycode) {
/* 392 */       boolean result = super.keyDown(event, keycode);
/* 393 */       Stage stage = TextArea.this.getStage();
/* 394 */       if (stage != null && stage.getKeyboardFocus() == TextArea.this) {
/* 395 */         boolean repeat = false;
/* 396 */         boolean shift = (Gdx.input.isKeyPressed(59) || Gdx.input.isKeyPressed(60));
/* 397 */         if (keycode == 20) {
/* 398 */           if (shift) {
/* 399 */             if (!TextArea.this.hasSelection) {
/* 400 */               TextArea.this.selectionStart = TextArea.this.cursor;
/* 401 */               TextArea.this.hasSelection = true;
/*     */             } 
/*     */           } else {
/* 404 */             TextArea.this.clearSelection();
/*     */           } 
/* 406 */           TextArea.this.moveCursorLine(TextArea.this.cursorLine + 1);
/* 407 */           repeat = true;
/*     */         }
/* 409 */         else if (keycode == 19) {
/* 410 */           if (shift) {
/* 411 */             if (!TextArea.this.hasSelection) {
/* 412 */               TextArea.this.selectionStart = TextArea.this.cursor;
/* 413 */               TextArea.this.hasSelection = true;
/*     */             } 
/*     */           } else {
/* 416 */             TextArea.this.clearSelection();
/*     */           } 
/* 418 */           TextArea.this.moveCursorLine(TextArea.this.cursorLine - 1);
/* 419 */           repeat = true;
/*     */         } else {
/*     */           
/* 422 */           TextArea.this.moveOffset = -1.0F;
/*     */         } 
/* 424 */         if (repeat) {
/* 425 */           scheduleKeyRepeatTask(keycode);
/*     */         }
/* 427 */         TextArea.this.showCursor();
/* 428 */         return true;
/*     */       } 
/* 430 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyTyped(InputEvent event, char character) {
/* 435 */       boolean result = super.keyTyped(event, character);
/* 436 */       TextArea.this.showCursor();
/* 437 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void goHome(boolean jump) {
/* 442 */       if (jump) {
/* 443 */         TextArea.this.cursor = 0;
/* 444 */       } else if (TextArea.this.cursorLine * 2 < TextArea.this.linesBreak.size) {
/* 445 */         TextArea.this.cursor = TextArea.this.linesBreak.get(TextArea.this.cursorLine * 2);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void goEnd(boolean jump) {
/* 451 */       if (jump || TextArea.this.cursorLine >= TextArea.this.getLines()) {
/* 452 */         TextArea.this.cursor = TextArea.this.text.length();
/* 453 */       } else if (TextArea.this.cursorLine * 2 + 1 < TextArea.this.linesBreak.size) {
/* 454 */         TextArea.this.cursor = TextArea.this.linesBreak.get(TextArea.this.cursorLine * 2 + 1);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\TextArea.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */