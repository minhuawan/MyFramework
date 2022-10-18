/*      */ package com.badlogic.gdx.scenes.scene2d.ui;
/*      */ 
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.g2d.Batch;
/*      */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*      */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*      */ import com.badlogic.gdx.math.Vector2;
/*      */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*      */ import com.badlogic.gdx.scenes.scene2d.Event;
/*      */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.Group;
/*      */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*      */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
/*      */ import com.badlogic.gdx.utils.Array;
/*      */ import com.badlogic.gdx.utils.Clipboard;
/*      */ import com.badlogic.gdx.utils.FloatArray;
/*      */ import com.badlogic.gdx.utils.Pools;
/*      */ import com.badlogic.gdx.utils.TimeUtils;
/*      */ import com.badlogic.gdx.utils.Timer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TextField
/*      */   extends Widget
/*      */   implements Disableable
/*      */ {
/*      */   private static final char BACKSPACE = '\b';
/*      */   protected static final char ENTER_DESKTOP = '\r';
/*      */   protected static final char ENTER_ANDROID = '\n';
/*      */   private static final char TAB = '\t';
/*      */   private static final char DELETE = '';
/*      */   private static final char BULLET = '';
/*   73 */   private static final Vector2 tmp1 = new Vector2();
/*   74 */   private static final Vector2 tmp2 = new Vector2();
/*   75 */   private static final Vector2 tmp3 = new Vector2();
/*      */   
/*   77 */   public static float keyRepeatInitialTime = 0.4F;
/*   78 */   public static float keyRepeatTime = 0.1F;
/*      */   protected String text;
/*      */   protected int cursor;
/*      */   protected int selectionStart;
/*      */   protected boolean hasSelection;
/*      */   protected boolean writeEnters;
/*   84 */   protected final GlyphLayout layout = new GlyphLayout();
/*   85 */   protected final FloatArray glyphPositions = new FloatArray();
/*      */   
/*      */   TextFieldStyle style;
/*      */   private String messageText;
/*      */   protected CharSequence displayText;
/*      */   Clipboard clipboard;
/*      */   InputListener inputListener;
/*      */   TextFieldListener listener;
/*      */   TextFieldFilter filter;
/*   94 */   OnscreenKeyboard keyboard = new DefaultOnscreenKeyboard();
/*      */   boolean focusTraversal = true;
/*   96 */   private int textHAlign = 8; boolean onlyFontChars = true; boolean disabled;
/*      */   private float selectionX;
/*      */   private float selectionWidth;
/*   99 */   String undoText = "";
/*      */   
/*      */   long lastChangeTime;
/*      */   boolean passwordMode;
/*      */   private StringBuilder passwordBuffer;
/*  104 */   private char passwordCharacter = ''; protected float fontOffset; protected float textHeight;
/*      */   protected float textOffset;
/*      */   float renderOffset;
/*      */   private int visibleTextStart;
/*      */   private int visibleTextEnd;
/*  109 */   private int maxLength = 0;
/*      */   
/*  111 */   private float blinkTime = 0.32F;
/*      */   
/*      */   boolean cursorOn = true;
/*      */   long lastBlink;
/*  115 */   KeyRepeatTask keyRepeatTask = new KeyRepeatTask();
/*      */   boolean programmaticChangeEvents;
/*      */   
/*      */   public TextField(String text, Skin skin) {
/*  119 */     this(text, skin.<TextFieldStyle>get(TextFieldStyle.class));
/*      */   }
/*      */   
/*      */   public TextField(String text, Skin skin, String styleName) {
/*  123 */     this(text, skin.<TextFieldStyle>get(styleName, TextFieldStyle.class));
/*      */   }
/*      */   
/*      */   public TextField(String text, TextFieldStyle style) {
/*  127 */     setStyle(style);
/*  128 */     this.clipboard = Gdx.app.getClipboard();
/*  129 */     initialize();
/*  130 */     setText(text);
/*  131 */     setSize(getPrefWidth(), getPrefHeight());
/*      */   }
/*      */   
/*      */   protected void initialize() {
/*  135 */     addListener((EventListener)(this.inputListener = createInputListener()));
/*      */   }
/*      */   
/*      */   protected InputListener createInputListener() {
/*  139 */     return (InputListener)new TextFieldClickListener();
/*      */   }
/*      */   
/*      */   protected int letterUnderCursor(float x) {
/*  143 */     x -= this.textOffset + this.fontOffset - (this.style.font.getData()).cursorX - this.glyphPositions.get(this.visibleTextStart);
/*  144 */     Drawable background = getBackgroundDrawable();
/*  145 */     if (background != null) x -= this.style.background.getLeftWidth(); 
/*  146 */     int n = this.glyphPositions.size;
/*  147 */     float[] glyphPositions = this.glyphPositions.items;
/*  148 */     for (int i = 1; i < n; i++) {
/*  149 */       if (glyphPositions[i] > x) {
/*  150 */         if (glyphPositions[i] - x <= x - glyphPositions[i - 1]) return i; 
/*  151 */         return i - 1;
/*      */       } 
/*      */     } 
/*  154 */     return n - 1;
/*      */   }
/*      */   
/*      */   protected boolean isWordCharacter(char c) {
/*  158 */     return Character.isLetterOrDigit(c);
/*      */   }
/*      */   
/*      */   protected int[] wordUnderCursor(int at) {
/*  162 */     String text = this.text;
/*  163 */     int start = at, right = text.length(), left = 0, index = start;
/*  164 */     if (at >= text.length()) {
/*  165 */       left = text.length();
/*  166 */       right = 0;
/*      */     } else {
/*  168 */       for (; index < right; index++) {
/*  169 */         if (!isWordCharacter(text.charAt(index))) {
/*  170 */           right = index;
/*      */           break;
/*      */         } 
/*      */       } 
/*  174 */       for (index = start - 1; index > -1; index--) {
/*  175 */         if (!isWordCharacter(text.charAt(index))) {
/*  176 */           left = index + 1;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  181 */     return new int[] { left, right };
/*      */   }
/*      */   
/*      */   int[] wordUnderCursor(float x) {
/*  185 */     return wordUnderCursor(letterUnderCursor(x));
/*      */   }
/*      */   
/*      */   boolean withinMaxLength(int size) {
/*  189 */     return (this.maxLength <= 0 || size < this.maxLength);
/*      */   }
/*      */   
/*      */   public void setMaxLength(int maxLength) {
/*  193 */     this.maxLength = maxLength;
/*      */   }
/*      */   
/*      */   public int getMaxLength() {
/*  197 */     return this.maxLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOnlyFontChars(boolean onlyFontChars) {
/*  204 */     this.onlyFontChars = onlyFontChars;
/*      */   }
/*      */   
/*      */   public void setStyle(TextFieldStyle style) {
/*  208 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  209 */     this.style = style;
/*  210 */     this.textHeight = style.font.getCapHeight() - style.font.getDescent() * 2.0F;
/*  211 */     invalidateHierarchy();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TextFieldStyle getStyle() {
/*  217 */     return this.style;
/*      */   }
/*      */   
/*      */   protected void calculateOffsets() {
/*  221 */     float visibleWidth = getWidth();
/*  222 */     Drawable background = getBackgroundDrawable();
/*  223 */     if (background != null) visibleWidth -= background.getLeftWidth() + background.getRightWidth();
/*      */     
/*  225 */     int glyphCount = this.glyphPositions.size;
/*  226 */     float[] glyphPositions = this.glyphPositions.items;
/*      */ 
/*      */     
/*  229 */     float distance = glyphPositions[Math.max(0, this.cursor - 1)] + this.renderOffset;
/*  230 */     if (distance <= 0.0F) {
/*  231 */       this.renderOffset -= distance;
/*      */     } else {
/*  233 */       int index = Math.min(glyphCount - 1, this.cursor + 1);
/*  234 */       float minX = glyphPositions[index] - visibleWidth;
/*  235 */       if (-this.renderOffset < minX) this.renderOffset = -minX;
/*      */     
/*      */     } 
/*      */     
/*  239 */     float maxOffset = 0.0F;
/*  240 */     float width = glyphPositions[glyphCount - 1];
/*  241 */     for (int i = glyphCount - 2; i >= 0; i--) {
/*  242 */       float x = glyphPositions[i];
/*  243 */       if (width - x > visibleWidth)
/*  244 */         break;  maxOffset = x;
/*      */     } 
/*  246 */     if (-this.renderOffset > maxOffset) this.renderOffset = -maxOffset;
/*      */ 
/*      */     
/*  249 */     this.visibleTextStart = 0;
/*  250 */     float startX = 0.0F;
/*  251 */     for (int j = 0; j < glyphCount; j++) {
/*  252 */       if (glyphPositions[j] >= -this.renderOffset) {
/*  253 */         this.visibleTextStart = Math.max(0, j);
/*  254 */         startX = glyphPositions[j];
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  260 */     int length = Math.min(this.displayText.length(), glyphPositions.length - 1);
/*  261 */     this.visibleTextEnd = Math.min(length, this.cursor + 1);
/*  262 */     for (; this.visibleTextEnd <= length && 
/*  263 */       glyphPositions[this.visibleTextEnd] <= startX + visibleWidth; this.visibleTextEnd++);
/*  264 */     this.visibleTextEnd = Math.max(0, this.visibleTextEnd - 1);
/*      */     
/*  266 */     if ((this.textHAlign & 0x8) == 0) {
/*  267 */       this.textOffset = visibleWidth - glyphPositions[this.visibleTextEnd] - startX;
/*  268 */       if ((this.textHAlign & 0x1) != 0) this.textOffset = Math.round(this.textOffset * 0.5F); 
/*      */     } else {
/*  270 */       this.textOffset = startX + this.renderOffset;
/*      */     } 
/*      */     
/*  273 */     if (this.hasSelection) {
/*  274 */       int minIndex = Math.min(this.cursor, this.selectionStart);
/*  275 */       int maxIndex = Math.max(this.cursor, this.selectionStart);
/*  276 */       float minX = Math.max(glyphPositions[minIndex] - glyphPositions[this.visibleTextStart], -this.textOffset);
/*  277 */       float maxX = Math.min(glyphPositions[maxIndex] - glyphPositions[this.visibleTextStart], visibleWidth - this.textOffset);
/*  278 */       this.selectionX = minX;
/*  279 */       this.selectionWidth = maxX - minX - (this.style.font.getData()).cursorX;
/*      */     } 
/*      */   }
/*      */   
/*      */   private Drawable getBackgroundDrawable() {
/*  284 */     Stage stage = getStage();
/*  285 */     boolean focused = (stage != null && stage.getKeyboardFocus() == this);
/*  286 */     return (this.disabled && this.style.disabledBackground != null) ? this.style.disabledBackground : ((focused && this.style.focusedBackground != null) ? this.style.focusedBackground : this.style.background);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Batch batch, float parentAlpha) {
/*  292 */     Stage stage = getStage();
/*  293 */     boolean focused = (stage != null && stage.getKeyboardFocus() == this);
/*  294 */     if (!focused) this.keyRepeatTask.cancel();
/*      */     
/*  296 */     BitmapFont font = this.style.font;
/*  297 */     Color fontColor = (this.disabled && this.style.disabledFontColor != null) ? this.style.disabledFontColor : ((focused && this.style.focusedFontColor != null) ? this.style.focusedFontColor : this.style.fontColor);
/*      */     
/*  299 */     Drawable selection = this.style.selection;
/*  300 */     Drawable cursorPatch = this.style.cursor;
/*  301 */     Drawable background = getBackgroundDrawable();
/*      */     
/*  303 */     Color color = getColor();
/*  304 */     float x = getX();
/*  305 */     float y = getY();
/*  306 */     float width = getWidth();
/*  307 */     float height = getHeight();
/*      */     
/*  309 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*  310 */     float bgLeftWidth = 0.0F, bgRightWidth = 0.0F;
/*  311 */     if (background != null) {
/*  312 */       background.draw(batch, x, y, width, height);
/*  313 */       bgLeftWidth = background.getLeftWidth();
/*  314 */       bgRightWidth = background.getRightWidth();
/*      */     } 
/*      */     
/*  317 */     float textY = getTextY(font, background);
/*  318 */     calculateOffsets();
/*      */     
/*  320 */     if (focused && this.hasSelection && selection != null) {
/*  321 */       drawSelection(selection, batch, font, x + bgLeftWidth, y + textY);
/*      */     }
/*      */     
/*  324 */     float yOffset = font.isFlipped() ? -this.textHeight : 0.0F;
/*  325 */     if (this.displayText.length() == 0) {
/*  326 */       if (!focused && this.messageText != null) {
/*  327 */         if (this.style.messageFontColor != null) {
/*  328 */           font.setColor(this.style.messageFontColor.r, this.style.messageFontColor.g, this.style.messageFontColor.b, this.style.messageFontColor.a * color.a * parentAlpha);
/*      */         } else {
/*      */           
/*  331 */           font.setColor(0.7F, 0.7F, 0.7F, color.a * parentAlpha);
/*  332 */         }  BitmapFont messageFont = (this.style.messageFont != null) ? this.style.messageFont : font;
/*  333 */         messageFont.draw(batch, this.messageText, x + bgLeftWidth, y + textY + yOffset, 0, this.messageText.length(), width - bgLeftWidth - bgRightWidth, this.textHAlign, false, "...");
/*      */       } 
/*      */     } else {
/*      */       
/*  337 */       font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * color.a * parentAlpha);
/*  338 */       drawText(batch, font, x + bgLeftWidth, y + textY + yOffset);
/*      */     } 
/*  340 */     if (focused && !this.disabled) {
/*  341 */       blink();
/*  342 */       if (this.cursorOn && cursorPatch != null) {
/*  343 */         drawCursor(cursorPatch, batch, font, x + bgLeftWidth, y + textY);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected float getTextY(BitmapFont font, Drawable background) {
/*  349 */     float height = getHeight();
/*  350 */     float textY = this.textHeight / 2.0F + font.getDescent();
/*  351 */     if (background != null) {
/*  352 */       float bottom = background.getBottomHeight();
/*  353 */       textY = textY + (height - background.getTopHeight() - bottom) / 2.0F + bottom;
/*      */     } else {
/*  355 */       textY += height / 2.0F;
/*      */     } 
/*  357 */     if (font.usesIntegerPositions()) textY = (int)textY; 
/*  358 */     return textY;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void drawSelection(Drawable selection, Batch batch, BitmapFont font, float x, float y) {
/*  363 */     selection.draw(batch, x + this.textOffset + this.selectionX + this.fontOffset, y - this.textHeight - font.getDescent(), this.selectionWidth, this.textHeight);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void drawText(Batch batch, BitmapFont font, float x, float y) {
/*  368 */     font.draw(batch, this.displayText, x + this.textOffset, y, this.visibleTextStart, this.visibleTextEnd, 0.0F, 8, false);
/*      */   }
/*      */   
/*      */   protected void drawCursor(Drawable cursorPatch, Batch batch, BitmapFont font, float x, float y) {
/*  372 */     cursorPatch.draw(batch, x + this.textOffset + this.glyphPositions
/*  373 */         .get(this.cursor) - this.glyphPositions.get(this.visibleTextStart) + this.fontOffset + (font.getData()).cursorX, y - this.textHeight - font
/*  374 */         .getDescent(), cursorPatch.getMinWidth(), this.textHeight);
/*      */   }
/*      */   
/*      */   void updateDisplayText() {
/*  378 */     BitmapFont font = this.style.font;
/*  379 */     BitmapFont.BitmapFontData data = font.getData();
/*  380 */     String text = this.text;
/*  381 */     int textLength = text.length();
/*      */     
/*  383 */     StringBuilder buffer = new StringBuilder();
/*  384 */     for (int i = 0; i < textLength; i++) {
/*  385 */       char c = text.charAt(i);
/*  386 */       buffer.append(data.hasGlyph(c) ? c : 32);
/*      */     } 
/*  388 */     String newDisplayText = buffer.toString();
/*      */     
/*  390 */     if (this.passwordMode && data.hasGlyph(this.passwordCharacter)) {
/*  391 */       if (this.passwordBuffer == null) this.passwordBuffer = new StringBuilder(newDisplayText.length()); 
/*  392 */       if (this.passwordBuffer.length() > textLength) {
/*  393 */         this.passwordBuffer.setLength(textLength);
/*      */       } else {
/*  395 */         for (int j = this.passwordBuffer.length(); j < textLength; j++)
/*  396 */           this.passwordBuffer.append(this.passwordCharacter); 
/*      */       } 
/*  398 */       this.displayText = this.passwordBuffer;
/*      */     } else {
/*  400 */       this.displayText = newDisplayText;
/*      */     } 
/*  402 */     this.layout.setText(font, this.displayText);
/*  403 */     this.glyphPositions.clear();
/*  404 */     float x = 0.0F;
/*  405 */     if (this.layout.runs.size > 0) {
/*  406 */       GlyphLayout.GlyphRun run = (GlyphLayout.GlyphRun)this.layout.runs.first();
/*  407 */       FloatArray xAdvances = run.xAdvances;
/*  408 */       this.fontOffset = xAdvances.first();
/*  409 */       for (int j = 1, n = xAdvances.size; j < n; j++) {
/*  410 */         this.glyphPositions.add(x);
/*  411 */         x += xAdvances.get(j);
/*      */       } 
/*      */     } else {
/*  414 */       this.fontOffset = 0.0F;
/*  415 */     }  this.glyphPositions.add(x);
/*      */     
/*  417 */     if (this.selectionStart > newDisplayText.length()) this.selectionStart = textLength; 
/*      */   }
/*      */   
/*      */   private void blink() {
/*  421 */     if (!Gdx.graphics.isContinuousRendering()) {
/*  422 */       this.cursorOn = true;
/*      */       return;
/*      */     } 
/*  425 */     long time = TimeUtils.nanoTime();
/*  426 */     if ((float)(time - this.lastBlink) / 1.0E9F > this.blinkTime) {
/*  427 */       this.cursorOn = !this.cursorOn;
/*  428 */       this.lastBlink = time;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void copy() {
/*  434 */     if (this.hasSelection && !this.passwordMode) {
/*  435 */       this.clipboard.setContents(this.text.substring(Math.min(this.cursor, this.selectionStart), Math.max(this.cursor, this.selectionStart)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void cut() {
/*  442 */     cut(this.programmaticChangeEvents);
/*      */   }
/*      */   
/*      */   void cut(boolean fireChangeEvent) {
/*  446 */     if (this.hasSelection && !this.passwordMode) {
/*  447 */       copy();
/*  448 */       this.cursor = delete(fireChangeEvent);
/*  449 */       updateDisplayText();
/*      */     } 
/*      */   }
/*      */   
/*      */   void paste(String content, boolean fireChangeEvent) {
/*  454 */     if (content == null)
/*  455 */       return;  StringBuilder buffer = new StringBuilder();
/*  456 */     int textLength = this.text.length();
/*  457 */     if (this.hasSelection) textLength -= Math.abs(this.cursor - this.selectionStart); 
/*  458 */     BitmapFont.BitmapFontData data = this.style.font.getData();
/*  459 */     for (int i = 0, n = content.length(); i < n && 
/*  460 */       withinMaxLength(textLength + buffer.length()); i++) {
/*  461 */       char c = content.charAt(i);
/*  462 */       if ((this.writeEnters && (c == '\n' || c == '\r')) || (
/*  463 */         c != '\r' && c != '\n' && (
/*  464 */         !this.onlyFontChars || data.hasGlyph(c)) && (
/*  465 */         this.filter == null || this.filter.acceptChar(this, c))))
/*      */       {
/*  467 */         buffer.append(c); } 
/*      */     } 
/*  469 */     content = buffer.toString();
/*      */     
/*  471 */     if (this.hasSelection) this.cursor = delete(fireChangeEvent); 
/*  472 */     if (fireChangeEvent) {
/*  473 */       changeText(this.text, insert(this.cursor, content, this.text));
/*      */     } else {
/*  475 */       this.text = insert(this.cursor, content, this.text);
/*  476 */     }  updateDisplayText();
/*  477 */     this.cursor += content.length();
/*      */   }
/*      */   
/*      */   String insert(int position, CharSequence text, String to) {
/*  481 */     if (to.length() == 0) return text.toString(); 
/*  482 */     return to.substring(0, position) + text + to.substring(position, to.length());
/*      */   }
/*      */   
/*      */   int delete(boolean fireChangeEvent) {
/*  486 */     int from = this.selectionStart;
/*  487 */     int to = this.cursor;
/*  488 */     int minIndex = Math.min(from, to);
/*  489 */     int maxIndex = Math.max(from, to);
/*      */     
/*  491 */     String newText = ((minIndex > 0) ? this.text.substring(0, minIndex) : "") + ((maxIndex < this.text.length()) ? this.text.substring(maxIndex, this.text.length()) : "");
/*  492 */     if (fireChangeEvent) {
/*  493 */       changeText(this.text, newText);
/*      */     } else {
/*  495 */       this.text = newText;
/*  496 */     }  clearSelection();
/*  497 */     return minIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void next(boolean up) {
/*  503 */     Stage stage = getStage();
/*  504 */     if (stage == null)
/*  505 */       return;  TextField current = this;
/*      */     while (true) {
/*  507 */       current.getParent().localToStageCoordinates(tmp1.set(getX(), getY()));
/*  508 */       TextField textField = current.findNextTextField(stage.getActors(), (TextField)null, tmp2, tmp1, up);
/*  509 */       if (textField == null) {
/*  510 */         if (up) {
/*  511 */           tmp1.set(Float.MIN_VALUE, Float.MIN_VALUE);
/*      */         } else {
/*  513 */           tmp1.set(Float.MAX_VALUE, Float.MAX_VALUE);
/*  514 */         }  textField = current.findNextTextField(getStage().getActors(), (TextField)null, tmp2, tmp1, up);
/*      */       } 
/*  516 */       if (textField == null) {
/*  517 */         Gdx.input.setOnscreenKeyboardVisible(false);
/*      */         break;
/*      */       } 
/*  520 */       if (stage.setKeyboardFocus(textField))
/*  521 */         break;  current = textField;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private TextField findNextTextField(Array<Actor> actors, TextField best, Vector2 bestCoords, Vector2 currentCoords, boolean up) {
/*  527 */     for (int i = 0, n = actors.size; i < n; i++) {
/*  528 */       Actor actor = (Actor)actors.get(i);
/*  529 */       if (actor != this)
/*  530 */         if (actor instanceof TextField) {
/*  531 */           TextField textField = (TextField)actor;
/*  532 */           if (!textField.isDisabled() && textField.focusTraversal) {
/*  533 */             Vector2 actorCoords = actor.getParent().localToStageCoordinates(tmp3.set(actor.getX(), actor.getY()));
/*  534 */             if ((((actorCoords.y < currentCoords.y || (actorCoords.y == currentCoords.y && actorCoords.x > currentCoords.x)) ? 1 : 0) ^ up) != 0 && (
/*  535 */               best == null || (((actorCoords.y > bestCoords.y || (actorCoords.y == bestCoords.y && actorCoords.x < bestCoords.x)) ? 1 : 0) ^ up) != 0)) {
/*      */               
/*  537 */               best = (TextField)actor;
/*  538 */               bestCoords.set(actorCoords);
/*      */             } 
/*      */           } 
/*  541 */         } else if (actor instanceof Group) {
/*  542 */           best = findNextTextField((Array<Actor>)((Group)actor).getChildren(), best, bestCoords, currentCoords, up);
/*      */         }  
/*  544 */     }  return best;
/*      */   }
/*      */   
/*      */   public InputListener getDefaultInputListener() {
/*  548 */     return this.inputListener;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTextFieldListener(TextFieldListener listener) {
/*  553 */     this.listener = listener;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTextFieldFilter(TextFieldFilter filter) {
/*  558 */     this.filter = filter;
/*      */   }
/*      */   
/*      */   public TextFieldFilter getTextFieldFilter() {
/*  562 */     return this.filter;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFocusTraversal(boolean focusTraversal) {
/*  567 */     this.focusTraversal = focusTraversal;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMessageText() {
/*  572 */     return this.messageText;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMessageText(String messageText) {
/*  578 */     this.messageText = messageText;
/*      */   }
/*      */ 
/*      */   
/*      */   public void appendText(String str) {
/*  583 */     if (str == null) str = "";
/*      */     
/*  585 */     clearSelection();
/*  586 */     this.cursor = this.text.length();
/*  587 */     paste(str, this.programmaticChangeEvents);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setText(String str) {
/*  592 */     if (str == null) str = ""; 
/*  593 */     if (str.equals(this.text))
/*      */       return; 
/*  595 */     clearSelection();
/*  596 */     String oldText = this.text;
/*  597 */     this.text = "";
/*  598 */     paste(str, false);
/*  599 */     if (this.programmaticChangeEvents) changeText(oldText, this.text); 
/*  600 */     this.cursor = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getText() {
/*  605 */     return this.text;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean changeText(String oldText, String newText) {
/*  611 */     if (newText.equals(oldText)) return false; 
/*  612 */     this.text = newText;
/*  613 */     ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/*  614 */     boolean cancelled = fire((Event)changeEvent);
/*  615 */     this.text = cancelled ? oldText : newText;
/*  616 */     Pools.free(changeEvent);
/*  617 */     return !cancelled;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
/*  623 */     this.programmaticChangeEvents = programmaticChangeEvents;
/*      */   }
/*      */   
/*      */   public boolean getProgrammaticChangeEvents() {
/*  627 */     return this.programmaticChangeEvents;
/*      */   }
/*      */   
/*      */   public int getSelectionStart() {
/*  631 */     return this.selectionStart;
/*      */   }
/*      */   
/*      */   public String getSelection() {
/*  635 */     return this.hasSelection ? this.text.substring(Math.min(this.selectionStart, this.cursor), Math.max(this.selectionStart, this.cursor)) : "";
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSelection(int selectionStart, int selectionEnd) {
/*  640 */     if (selectionStart < 0) throw new IllegalArgumentException("selectionStart must be >= 0"); 
/*  641 */     if (selectionEnd < 0) throw new IllegalArgumentException("selectionEnd must be >= 0"); 
/*  642 */     selectionStart = Math.min(this.text.length(), selectionStart);
/*  643 */     selectionEnd = Math.min(this.text.length(), selectionEnd);
/*  644 */     if (selectionEnd == selectionStart) {
/*  645 */       clearSelection();
/*      */       return;
/*      */     } 
/*  648 */     if (selectionEnd < selectionStart) {
/*  649 */       int temp = selectionEnd;
/*  650 */       selectionEnd = selectionStart;
/*  651 */       selectionStart = temp;
/*      */     } 
/*      */     
/*  654 */     this.hasSelection = true;
/*  655 */     this.selectionStart = selectionStart;
/*  656 */     this.cursor = selectionEnd;
/*      */   }
/*      */   
/*      */   public void selectAll() {
/*  660 */     setSelection(0, this.text.length());
/*      */   }
/*      */   
/*      */   public void clearSelection() {
/*  664 */     this.hasSelection = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCursorPosition(int cursorPosition) {
/*  669 */     if (cursorPosition < 0) throw new IllegalArgumentException("cursorPosition must be >= 0"); 
/*  670 */     clearSelection();
/*  671 */     this.cursor = Math.min(cursorPosition, this.text.length());
/*      */   }
/*      */   
/*      */   public int getCursorPosition() {
/*  675 */     return this.cursor;
/*      */   }
/*      */ 
/*      */   
/*      */   public OnscreenKeyboard getOnscreenKeyboard() {
/*  680 */     return this.keyboard;
/*      */   }
/*      */   
/*      */   public void setOnscreenKeyboard(OnscreenKeyboard keyboard) {
/*  684 */     this.keyboard = keyboard;
/*      */   }
/*      */   
/*      */   public void setClipboard(Clipboard clipboard) {
/*  688 */     this.clipboard = clipboard;
/*      */   }
/*      */   
/*      */   public float getPrefWidth() {
/*  692 */     return 150.0F;
/*      */   }
/*      */   
/*      */   public float getPrefHeight() {
/*  696 */     float topAndBottom = 0.0F, minHeight = 0.0F;
/*  697 */     if (this.style.background != null) {
/*  698 */       topAndBottom = Math.max(topAndBottom, this.style.background.getBottomHeight() + this.style.background.getTopHeight());
/*  699 */       minHeight = Math.max(minHeight, this.style.background.getMinHeight());
/*      */     } 
/*  701 */     if (this.style.focusedBackground != null) {
/*  702 */       topAndBottom = Math.max(topAndBottom, this.style.focusedBackground
/*  703 */           .getBottomHeight() + this.style.focusedBackground.getTopHeight());
/*  704 */       minHeight = Math.max(minHeight, this.style.focusedBackground.getMinHeight());
/*      */     } 
/*  706 */     if (this.style.disabledBackground != null) {
/*  707 */       topAndBottom = Math.max(topAndBottom, this.style.disabledBackground
/*  708 */           .getBottomHeight() + this.style.disabledBackground.getTopHeight());
/*  709 */       minHeight = Math.max(minHeight, this.style.disabledBackground.getMinHeight());
/*      */     } 
/*  711 */     return Math.max(topAndBottom + this.textHeight, minHeight);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAlignment(int alignment) {
/*  717 */     this.textHAlign = alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPasswordMode(boolean passwordMode) {
/*  723 */     this.passwordMode = passwordMode;
/*  724 */     updateDisplayText();
/*      */   }
/*      */   
/*      */   public boolean isPasswordMode() {
/*  728 */     return this.passwordMode;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPasswordCharacter(char passwordCharacter) {
/*  734 */     this.passwordCharacter = passwordCharacter;
/*  735 */     if (this.passwordMode) updateDisplayText(); 
/*      */   }
/*      */   
/*      */   public void setBlinkTime(float blinkTime) {
/*  739 */     this.blinkTime = blinkTime;
/*      */   }
/*      */   
/*      */   public void setDisabled(boolean disabled) {
/*  743 */     this.disabled = disabled;
/*      */   }
/*      */   
/*      */   public boolean isDisabled() {
/*  747 */     return this.disabled;
/*      */   }
/*      */   
/*      */   protected void moveCursor(boolean forward, boolean jump) {
/*  751 */     int limit = forward ? this.text.length() : 0;
/*  752 */     int charOffset = forward ? 0 : -1; do {  }
/*  753 */     while ((forward ? (++this.cursor < limit) : (--this.cursor > limit)) && jump && 
/*  754 */       continueCursor(this.cursor, charOffset));
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean continueCursor(int index, int offset) {
/*  759 */     char c = this.text.charAt(index + offset);
/*  760 */     return isWordCharacter(c);
/*      */   }
/*      */   
/*      */   class KeyRepeatTask extends Timer.Task {
/*      */     int keycode;
/*      */     
/*      */     public void run() {
/*  767 */       TextField.this.inputListener.keyDown(null, this.keycode);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static interface TextFieldListener
/*      */   {
/*      */     void keyTyped(TextField param1TextField, char param1Char);
/*      */   }
/*      */   
/*      */   public static interface TextFieldFilter
/*      */   {
/*      */     boolean acceptChar(TextField param1TextField, char param1Char);
/*      */     
/*      */     public static class DigitsOnlyFilter
/*      */       implements TextFieldFilter
/*      */     {
/*      */       public boolean acceptChar(TextField textField, char c) {
/*  785 */         return Character.isDigit(c);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface OnscreenKeyboard
/*      */   {
/*      */     void show(boolean param1Boolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DefaultOnscreenKeyboard
/*      */     implements OnscreenKeyboard
/*      */   {
/*      */     public void show(boolean visible) {
/*  803 */       Gdx.input.setOnscreenKeyboardVisible(visible);
/*      */     }
/*      */   }
/*      */   
/*      */   public class TextFieldClickListener
/*      */     extends ClickListener {
/*      */     public void clicked(InputEvent event, float x, float y) {
/*  810 */       int count = getTapCount() % 4;
/*  811 */       if (count == 0) TextField.this.clearSelection(); 
/*  812 */       if (count == 2) {
/*  813 */         int[] array = TextField.this.wordUnderCursor(x);
/*  814 */         TextField.this.setSelection(array[0], array[1]);
/*      */       } 
/*  816 */       if (count == 3) TextField.this.selectAll(); 
/*      */     }
/*      */     
/*      */     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  820 */       if (!super.touchDown(event, x, y, pointer, button)) return false; 
/*  821 */       if (pointer == 0 && button != 0) return false; 
/*  822 */       if (TextField.this.disabled) return true; 
/*  823 */       setCursorPosition(x, y);
/*  824 */       TextField.this.selectionStart = TextField.this.cursor;
/*  825 */       Stage stage = TextField.this.getStage();
/*  826 */       if (stage != null) stage.setKeyboardFocus(TextField.this); 
/*  827 */       TextField.this.keyboard.show(true);
/*  828 */       TextField.this.hasSelection = true;
/*  829 */       return true;
/*      */     }
/*      */     
/*      */     public void touchDragged(InputEvent event, float x, float y, int pointer) {
/*  833 */       super.touchDragged(event, x, y, pointer);
/*  834 */       setCursorPosition(x, y);
/*      */     }
/*      */     
/*      */     public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  838 */       if (TextField.this.selectionStart == TextField.this.cursor) TextField.this.hasSelection = false; 
/*  839 */       super.touchUp(event, x, y, pointer, button);
/*      */     }
/*      */     
/*      */     protected void setCursorPosition(float x, float y) {
/*  843 */       TextField.this.lastBlink = 0L;
/*  844 */       TextField.this.cursorOn = false;
/*  845 */       TextField.this.cursor = TextField.this.letterUnderCursor(x);
/*      */     }
/*      */     
/*      */     protected void goHome(boolean jump) {
/*  849 */       TextField.this.cursor = 0;
/*      */     }
/*      */     
/*      */     protected void goEnd(boolean jump) {
/*  853 */       TextField.this.cursor = TextField.this.text.length();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean keyDown(InputEvent event, int keycode) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   4: getfield disabled : Z
/*      */       //   7: ifeq -> 12
/*      */       //   10: iconst_0
/*      */       //   11: ireturn
/*      */       //   12: aload_0
/*      */       //   13: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   16: lconst_0
/*      */       //   17: putfield lastBlink : J
/*      */       //   20: aload_0
/*      */       //   21: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   24: iconst_0
/*      */       //   25: putfield cursorOn : Z
/*      */       //   28: aload_0
/*      */       //   29: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   32: invokevirtual getStage : ()Lcom/badlogic/gdx/scenes/scene2d/Stage;
/*      */       //   35: astore_3
/*      */       //   36: aload_3
/*      */       //   37: ifnull -> 51
/*      */       //   40: aload_3
/*      */       //   41: invokevirtual getKeyboardFocus : ()Lcom/badlogic/gdx/scenes/scene2d/Actor;
/*      */       //   44: aload_0
/*      */       //   45: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   48: if_acmpeq -> 53
/*      */       //   51: iconst_0
/*      */       //   52: ireturn
/*      */       //   53: iconst_0
/*      */       //   54: istore #4
/*      */       //   56: invokestatic ctrl : ()Z
/*      */       //   59: istore #5
/*      */       //   61: iload #5
/*      */       //   63: ifeq -> 80
/*      */       //   66: aload_0
/*      */       //   67: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   70: getfield passwordMode : Z
/*      */       //   73: ifne -> 80
/*      */       //   76: iconst_1
/*      */       //   77: goto -> 81
/*      */       //   80: iconst_0
/*      */       //   81: istore #6
/*      */       //   83: iload #5
/*      */       //   85: ifeq -> 217
/*      */       //   88: iload_2
/*      */       //   89: bipush #50
/*      */       //   91: if_icmpne -> 117
/*      */       //   94: aload_0
/*      */       //   95: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   98: aload_0
/*      */       //   99: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   102: getfield clipboard : Lcom/badlogic/gdx/utils/Clipboard;
/*      */       //   105: invokeinterface getContents : ()Ljava/lang/String;
/*      */       //   110: iconst_1
/*      */       //   111: invokevirtual paste : (Ljava/lang/String;Z)V
/*      */       //   114: iconst_1
/*      */       //   115: istore #4
/*      */       //   117: iload_2
/*      */       //   118: bipush #31
/*      */       //   120: if_icmpeq -> 130
/*      */       //   123: iload_2
/*      */       //   124: sipush #133
/*      */       //   127: if_icmpne -> 139
/*      */       //   130: aload_0
/*      */       //   131: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   134: invokevirtual copy : ()V
/*      */       //   137: iconst_1
/*      */       //   138: ireturn
/*      */       //   139: iload_2
/*      */       //   140: bipush #52
/*      */       //   142: if_icmpne -> 155
/*      */       //   145: aload_0
/*      */       //   146: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   149: iconst_1
/*      */       //   150: invokevirtual cut : (Z)V
/*      */       //   153: iconst_1
/*      */       //   154: ireturn
/*      */       //   155: iload_2
/*      */       //   156: bipush #29
/*      */       //   158: if_icmpne -> 170
/*      */       //   161: aload_0
/*      */       //   162: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   165: invokevirtual selectAll : ()V
/*      */       //   168: iconst_1
/*      */       //   169: ireturn
/*      */       //   170: iload_2
/*      */       //   171: bipush #54
/*      */       //   173: if_icmpne -> 217
/*      */       //   176: aload_0
/*      */       //   177: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   180: getfield text : Ljava/lang/String;
/*      */       //   183: astore #7
/*      */       //   185: aload_0
/*      */       //   186: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   189: aload_0
/*      */       //   190: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   193: getfield undoText : Ljava/lang/String;
/*      */       //   196: invokevirtual setText : (Ljava/lang/String;)V
/*      */       //   199: aload_0
/*      */       //   200: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   203: aload #7
/*      */       //   205: putfield undoText : Ljava/lang/String;
/*      */       //   208: aload_0
/*      */       //   209: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   212: invokevirtual updateDisplayText : ()V
/*      */       //   215: iconst_1
/*      */       //   216: ireturn
/*      */       //   217: invokestatic shift : ()Z
/*      */       //   220: ifeq -> 374
/*      */       //   223: iload_2
/*      */       //   224: sipush #133
/*      */       //   227: if_icmpne -> 250
/*      */       //   230: aload_0
/*      */       //   231: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   234: aload_0
/*      */       //   235: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   238: getfield clipboard : Lcom/badlogic/gdx/utils/Clipboard;
/*      */       //   241: invokeinterface getContents : ()Ljava/lang/String;
/*      */       //   246: iconst_1
/*      */       //   247: invokevirtual paste : (Ljava/lang/String;Z)V
/*      */       //   250: iload_2
/*      */       //   251: bipush #112
/*      */       //   253: if_icmpne -> 264
/*      */       //   256: aload_0
/*      */       //   257: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   260: iconst_1
/*      */       //   261: invokevirtual cut : (Z)V
/*      */       //   264: aload_0
/*      */       //   265: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   268: getfield cursor : I
/*      */       //   271: istore #7
/*      */       //   273: iload_2
/*      */       //   274: bipush #21
/*      */       //   276: if_icmpne -> 295
/*      */       //   279: aload_0
/*      */       //   280: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   283: iconst_0
/*      */       //   284: iload #6
/*      */       //   286: invokevirtual moveCursor : (ZZ)V
/*      */       //   289: iconst_1
/*      */       //   290: istore #4
/*      */       //   292: goto -> 344
/*      */       //   295: iload_2
/*      */       //   296: bipush #22
/*      */       //   298: if_icmpne -> 317
/*      */       //   301: aload_0
/*      */       //   302: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   305: iconst_1
/*      */       //   306: iload #6
/*      */       //   308: invokevirtual moveCursor : (ZZ)V
/*      */       //   311: iconst_1
/*      */       //   312: istore #4
/*      */       //   314: goto -> 344
/*      */       //   317: iload_2
/*      */       //   318: iconst_3
/*      */       //   319: if_icmpne -> 331
/*      */       //   322: aload_0
/*      */       //   323: iload #6
/*      */       //   325: invokevirtual goHome : (Z)V
/*      */       //   328: goto -> 344
/*      */       //   331: iload_2
/*      */       //   332: sipush #132
/*      */       //   335: if_icmpne -> 464
/*      */       //   338: aload_0
/*      */       //   339: iload #6
/*      */       //   341: invokevirtual goEnd : (Z)V
/*      */       //   344: aload_0
/*      */       //   345: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   348: getfield hasSelection : Z
/*      */       //   351: ifne -> 371
/*      */       //   354: aload_0
/*      */       //   355: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   358: iload #7
/*      */       //   360: putfield selectionStart : I
/*      */       //   363: aload_0
/*      */       //   364: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   367: iconst_1
/*      */       //   368: putfield hasSelection : Z
/*      */       //   371: goto -> 464
/*      */       //   374: iload_2
/*      */       //   375: bipush #21
/*      */       //   377: if_icmpne -> 400
/*      */       //   380: aload_0
/*      */       //   381: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   384: iconst_0
/*      */       //   385: iload #6
/*      */       //   387: invokevirtual moveCursor : (ZZ)V
/*      */       //   390: aload_0
/*      */       //   391: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   394: invokevirtual clearSelection : ()V
/*      */       //   397: iconst_1
/*      */       //   398: istore #4
/*      */       //   400: iload_2
/*      */       //   401: bipush #22
/*      */       //   403: if_icmpne -> 426
/*      */       //   406: aload_0
/*      */       //   407: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   410: iconst_1
/*      */       //   411: iload #6
/*      */       //   413: invokevirtual moveCursor : (ZZ)V
/*      */       //   416: aload_0
/*      */       //   417: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   420: invokevirtual clearSelection : ()V
/*      */       //   423: iconst_1
/*      */       //   424: istore #4
/*      */       //   426: iload_2
/*      */       //   427: iconst_3
/*      */       //   428: if_icmpne -> 444
/*      */       //   431: aload_0
/*      */       //   432: iload #6
/*      */       //   434: invokevirtual goHome : (Z)V
/*      */       //   437: aload_0
/*      */       //   438: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   441: invokevirtual clearSelection : ()V
/*      */       //   444: iload_2
/*      */       //   445: sipush #132
/*      */       //   448: if_icmpne -> 464
/*      */       //   451: aload_0
/*      */       //   452: iload #6
/*      */       //   454: invokevirtual goEnd : (Z)V
/*      */       //   457: aload_0
/*      */       //   458: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   461: invokevirtual clearSelection : ()V
/*      */       //   464: aload_0
/*      */       //   465: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   468: aload_0
/*      */       //   469: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   472: getfield cursor : I
/*      */       //   475: iconst_0
/*      */       //   476: aload_0
/*      */       //   477: getfield this$0 : Lcom/badlogic/gdx/scenes/scene2d/ui/TextField;
/*      */       //   480: getfield text : Ljava/lang/String;
/*      */       //   483: invokevirtual length : ()I
/*      */       //   486: invokestatic clamp : (III)I
/*      */       //   489: putfield cursor : I
/*      */       //   492: iload #4
/*      */       //   494: ifeq -> 502
/*      */       //   497: aload_0
/*      */       //   498: iload_2
/*      */       //   499: invokevirtual scheduleKeyRepeatTask : (I)V
/*      */       //   502: iconst_1
/*      */       //   503: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #857	-> 0
/*      */       //   #859	-> 12
/*      */       //   #860	-> 20
/*      */       //   #862	-> 28
/*      */       //   #863	-> 36
/*      */       //   #865	-> 53
/*      */       //   #866	-> 56
/*      */       //   #867	-> 61
/*      */       //   #869	-> 83
/*      */       //   #870	-> 88
/*      */       //   #871	-> 94
/*      */       //   #872	-> 114
/*      */       //   #874	-> 117
/*      */       //   #875	-> 130
/*      */       //   #876	-> 137
/*      */       //   #878	-> 139
/*      */       //   #879	-> 145
/*      */       //   #880	-> 153
/*      */       //   #882	-> 155
/*      */       //   #883	-> 161
/*      */       //   #884	-> 168
/*      */       //   #886	-> 170
/*      */       //   #887	-> 176
/*      */       //   #888	-> 185
/*      */       //   #889	-> 199
/*      */       //   #890	-> 208
/*      */       //   #891	-> 215
/*      */       //   #895	-> 217
/*      */       //   #896	-> 223
/*      */       //   #897	-> 250
/*      */       //   #900	-> 264
/*      */       //   #903	-> 273
/*      */       //   #904	-> 279
/*      */       //   #905	-> 289
/*      */       //   #906	-> 292
/*      */       //   #908	-> 295
/*      */       //   #909	-> 301
/*      */       //   #910	-> 311
/*      */       //   #911	-> 314
/*      */       //   #913	-> 317
/*      */       //   #914	-> 322
/*      */       //   #915	-> 328
/*      */       //   #917	-> 331
/*      */       //   #918	-> 338
/*      */       //   #923	-> 344
/*      */       //   #924	-> 354
/*      */       //   #925	-> 363
/*      */       //   #927	-> 371
/*      */       //   #930	-> 374
/*      */       //   #931	-> 380
/*      */       //   #932	-> 390
/*      */       //   #933	-> 397
/*      */       //   #935	-> 400
/*      */       //   #936	-> 406
/*      */       //   #937	-> 416
/*      */       //   #938	-> 423
/*      */       //   #940	-> 426
/*      */       //   #941	-> 431
/*      */       //   #942	-> 437
/*      */       //   #944	-> 444
/*      */       //   #945	-> 451
/*      */       //   #946	-> 457
/*      */       //   #949	-> 464
/*      */       //   #951	-> 492
/*      */       //   #952	-> 497
/*      */       //   #954	-> 502
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   185	32	7	oldText	Ljava/lang/String;
/*      */       //   273	98	7	temp	I
/*      */       //   0	504	0	this	Lcom/badlogic/gdx/scenes/scene2d/ui/TextField$TextFieldClickListener;
/*      */       //   0	504	1	event	Lcom/badlogic/gdx/scenes/scene2d/InputEvent;
/*      */       //   0	504	2	keycode	I
/*      */       //   36	468	3	stage	Lcom/badlogic/gdx/scenes/scene2d/Stage;
/*      */       //   56	448	4	repeat	Z
/*      */       //   61	443	5	ctrl	Z
/*      */       //   83	421	6	jump	Z
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void scheduleKeyRepeatTask(int keycode) {
/*  958 */       if (!TextField.this.keyRepeatTask.isScheduled() || TextField.this.keyRepeatTask.keycode != keycode) {
/*  959 */         TextField.this.keyRepeatTask.keycode = keycode;
/*  960 */         TextField.this.keyRepeatTask.cancel();
/*  961 */         Timer.schedule(TextField.this.keyRepeatTask, TextField.keyRepeatInitialTime, TextField.keyRepeatTime);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean keyUp(InputEvent event, int keycode) {
/*  966 */       if (TextField.this.disabled) return false; 
/*  967 */       TextField.this.keyRepeatTask.cancel();
/*  968 */       return true;
/*      */     }
/*      */     
/*      */     public boolean keyTyped(InputEvent event, char character) {
/*  972 */       if (TextField.this.disabled) return false;
/*      */ 
/*      */       
/*  975 */       switch (character) {
/*      */         case '\b':
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\r':
/*      */           break;
/*      */         default:
/*  982 */           if (character < ' ') return false; 
/*      */           break;
/*      */       } 
/*  985 */       Stage stage = TextField.this.getStage();
/*  986 */       if (stage == null || stage.getKeyboardFocus() != TextField.this) return false;
/*      */       
/*  988 */       if (UIUtils.isMac && Gdx.input.isKeyPressed(63)) return true;
/*      */       
/*  990 */       if ((character == '\t' || character == '\n') && TextField.this.focusTraversal) {
/*  991 */         TextField.this.next(UIUtils.shift());
/*      */       } else {
/*  993 */         boolean delete = (character == '');
/*  994 */         boolean backspace = (character == '\b');
/*  995 */         boolean enter = (character == '\r' || character == '\n');
/*  996 */         boolean add = enter ? TextField.this.writeEnters : ((!TextField.this.onlyFontChars || TextField.this.style.font.getData().hasGlyph(character)));
/*  997 */         boolean remove = (backspace || delete);
/*  998 */         if (add || remove) {
/*  999 */           String oldText = TextField.this.text;
/* 1000 */           int oldCursor = TextField.this.cursor;
/* 1001 */           if (TextField.this.hasSelection) {
/* 1002 */             TextField.this.cursor = TextField.this.delete(false);
/*      */           } else {
/* 1004 */             if (backspace && TextField.this.cursor > 0) {
/* 1005 */               TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor - 1) + TextField.this.text.substring(TextField.this.cursor--);
/* 1006 */               TextField.this.renderOffset = 0.0F;
/*      */             } 
/* 1008 */             if (delete && TextField.this.cursor < TextField.this.text.length()) {
/* 1009 */               TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor) + TextField.this.text.substring(TextField.this.cursor + 1);
/*      */             }
/*      */           } 
/* 1012 */           if (add && !remove) {
/*      */             
/* 1014 */             if (!enter && TextField.this.filter != null && !TextField.this.filter.acceptChar(TextField.this, character)) return true; 
/* 1015 */             if (!TextField.this.withinMaxLength(TextField.this.text.length())) return true; 
/* 1016 */             String insertion = enter ? "\n" : String.valueOf(character);
/* 1017 */             TextField.this.text = TextField.this.insert(TextField.this.cursor++, insertion, TextField.this.text);
/*      */           } 
/* 1019 */           String tempUndoText = TextField.this.undoText;
/* 1020 */           if (TextField.this.changeText(oldText, TextField.this.text)) {
/* 1021 */             long time = System.currentTimeMillis();
/* 1022 */             if (time - 750L > TextField.this.lastChangeTime) TextField.this.undoText = oldText; 
/* 1023 */             TextField.this.lastChangeTime = time;
/*      */           } else {
/* 1025 */             TextField.this.cursor = oldCursor;
/* 1026 */           }  TextField.this.updateDisplayText();
/*      */         } 
/*      */       } 
/* 1029 */       if (TextField.this.listener != null) TextField.this.listener.keyTyped(TextField.this, character); 
/* 1030 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class TextFieldStyle
/*      */   {
/*      */     public BitmapFont font;
/*      */     
/*      */     public Color fontColor;
/*      */     public Color focusedFontColor;
/*      */     public Color disabledFontColor;
/*      */     public Drawable background;
/*      */     public Drawable focusedBackground;
/*      */     public Drawable disabledBackground;
/*      */     public Drawable cursor;
/*      */     public Drawable selection;
/*      */     public BitmapFont messageFont;
/*      */     public Color messageFontColor;
/*      */     
/*      */     public TextFieldStyle() {}
/*      */     
/*      */     public TextFieldStyle(BitmapFont font, Color fontColor, Drawable cursor, Drawable selection, Drawable background) {
/* 1053 */       this.background = background;
/* 1054 */       this.cursor = cursor;
/* 1055 */       this.font = font;
/* 1056 */       this.fontColor = fontColor;
/* 1057 */       this.selection = selection;
/*      */     }
/*      */     
/*      */     public TextFieldStyle(TextFieldStyle style) {
/* 1061 */       this.messageFont = style.messageFont;
/* 1062 */       if (style.messageFontColor != null) this.messageFontColor = new Color(style.messageFontColor); 
/* 1063 */       this.background = style.background;
/* 1064 */       this.focusedBackground = style.focusedBackground;
/* 1065 */       this.disabledBackground = style.disabledBackground;
/* 1066 */       this.cursor = style.cursor;
/* 1067 */       this.font = style.font;
/* 1068 */       if (style.fontColor != null) this.fontColor = new Color(style.fontColor); 
/* 1069 */       if (style.focusedFontColor != null) this.focusedFontColor = new Color(style.focusedFontColor); 
/* 1070 */       if (style.disabledFontColor != null) this.disabledFontColor = new Color(style.disabledFontColor); 
/* 1071 */       this.selection = style.selection;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\TextField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */