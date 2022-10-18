/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Event;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Pools;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
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
/*     */ public class Button
/*     */   extends Table
/*     */   implements Disableable
/*     */ {
/*     */   private ButtonStyle style;
/*     */   boolean isChecked;
/*     */   boolean isDisabled;
/*     */   ButtonGroup buttonGroup;
/*     */   private ClickListener clickListener;
/*     */   private boolean programmaticChangeEvents = true;
/*     */   
/*     */   public Button(Skin skin) {
/*  51 */     super(skin);
/*  52 */     initialize();
/*  53 */     setStyle(skin.<ButtonStyle>get(ButtonStyle.class));
/*  54 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public Button(Skin skin, String styleName) {
/*  58 */     super(skin);
/*  59 */     initialize();
/*  60 */     setStyle(skin.<ButtonStyle>get(styleName, ButtonStyle.class));
/*  61 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public Button(Actor child, Skin skin, String styleName) {
/*  65 */     this(child, skin.<ButtonStyle>get(styleName, ButtonStyle.class));
/*  66 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public Button(Actor child, ButtonStyle style) {
/*  70 */     initialize();
/*  71 */     add(child);
/*  72 */     setStyle(style);
/*  73 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public Button(ButtonStyle style) {
/*  77 */     initialize();
/*  78 */     setStyle(style);
/*  79 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public Button() {
/*  84 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() {
/*  88 */     setTouchable(Touchable.enabled);
/*  89 */     addListener((EventListener)(this.clickListener = new ClickListener() {
/*     */           public void clicked(InputEvent event, float x, float y) {
/*  91 */             if (Button.this.isDisabled())
/*  92 */               return;  Button.this.setChecked(!Button.this.isChecked, true);
/*     */           }
/*     */         }));
/*     */   }
/*     */   
/*     */   public Button(Drawable up) {
/*  98 */     this(new ButtonStyle(up, null, null));
/*     */   }
/*     */   
/*     */   public Button(Drawable up, Drawable down) {
/* 102 */     this(new ButtonStyle(up, down, null));
/*     */   }
/*     */   
/*     */   public Button(Drawable up, Drawable down, Drawable checked) {
/* 106 */     this(new ButtonStyle(up, down, checked));
/*     */   }
/*     */   
/*     */   public Button(Actor child, Skin skin) {
/* 110 */     this(child, skin.<ButtonStyle>get(ButtonStyle.class));
/*     */   }
/*     */   
/*     */   public void setChecked(boolean isChecked) {
/* 114 */     setChecked(isChecked, this.programmaticChangeEvents);
/*     */   }
/*     */   
/*     */   void setChecked(boolean isChecked, boolean fireEvent) {
/* 118 */     if (this.isChecked == isChecked)
/* 119 */       return;  if (this.buttonGroup != null && !this.buttonGroup.canCheck(this, isChecked))
/* 120 */       return;  this.isChecked = isChecked;
/*     */     
/* 122 */     if (fireEvent) {
/* 123 */       ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/* 124 */       if (fire((Event)changeEvent)) this.isChecked = !isChecked; 
/* 125 */       Pools.free(changeEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void toggle() {
/* 132 */     setChecked(!this.isChecked);
/*     */   }
/*     */   
/*     */   public boolean isChecked() {
/* 136 */     return this.isChecked;
/*     */   }
/*     */   
/*     */   public boolean isPressed() {
/* 140 */     return this.clickListener.isVisualPressed();
/*     */   }
/*     */   
/*     */   public boolean isOver() {
/* 144 */     return this.clickListener.isOver();
/*     */   }
/*     */   
/*     */   public ClickListener getClickListener() {
/* 148 */     return this.clickListener;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 152 */     return this.isDisabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean isDisabled) {
/* 157 */     this.isDisabled = isDisabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
/* 163 */     this.programmaticChangeEvents = programmaticChangeEvents;
/*     */   }
/*     */   
/*     */   public void setStyle(ButtonStyle style) {
/* 167 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/* 168 */     this.style = style;
/*     */     
/* 170 */     Drawable background = null;
/* 171 */     if (isPressed() && !isDisabled()) {
/* 172 */       background = (style.down == null) ? style.up : style.down;
/*     */     }
/* 174 */     else if (isDisabled() && style.disabled != null) {
/* 175 */       background = style.disabled;
/* 176 */     } else if (this.isChecked && style.checked != null) {
/* 177 */       background = (isOver() && style.checkedOver != null) ? style.checkedOver : style.checked;
/* 178 */     } else if (isOver() && style.over != null) {
/* 179 */       background = style.over;
/*     */     } else {
/* 181 */       background = style.up;
/*     */     } 
/* 183 */     setBackground(background);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStyle getStyle() {
/* 189 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public ButtonGroup getButtonGroup() {
/* 194 */     return this.buttonGroup;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 198 */     validate();
/*     */     
/* 200 */     boolean isDisabled = isDisabled();
/* 201 */     boolean isPressed = isPressed();
/* 202 */     boolean isChecked = isChecked();
/* 203 */     boolean isOver = isOver();
/*     */     
/* 205 */     Drawable background = null;
/* 206 */     if (isDisabled && this.style.disabled != null) {
/* 207 */       background = this.style.disabled;
/* 208 */     } else if (isPressed && this.style.down != null) {
/* 209 */       background = this.style.down;
/* 210 */     } else if (isChecked && this.style.checked != null) {
/* 211 */       background = (this.style.checkedOver != null && isOver) ? this.style.checkedOver : this.style.checked;
/* 212 */     } else if (isOver && this.style.over != null) {
/* 213 */       background = this.style.over;
/* 214 */     } else if (this.style.up != null) {
/* 215 */       background = this.style.up;
/* 216 */     }  setBackground(background);
/*     */     
/* 218 */     float offsetX = 0.0F, offsetY = 0.0F;
/* 219 */     if (isPressed && !isDisabled) {
/* 220 */       offsetX = this.style.pressedOffsetX;
/* 221 */       offsetY = this.style.pressedOffsetY;
/* 222 */     } else if (isChecked && !isDisabled) {
/* 223 */       offsetX = this.style.checkedOffsetX;
/* 224 */       offsetY = this.style.checkedOffsetY;
/*     */     } else {
/* 226 */       offsetX = this.style.unpressedOffsetX;
/* 227 */       offsetY = this.style.unpressedOffsetY;
/*     */     } 
/*     */     
/* 230 */     SnapshotArray snapshotArray = getChildren(); int i;
/* 231 */     for (i = 0; i < ((Array)snapshotArray).size; i++)
/* 232 */       ((Actor)snapshotArray.get(i)).moveBy(offsetX, offsetY); 
/* 233 */     super.draw(batch, parentAlpha);
/* 234 */     for (i = 0; i < ((Array)snapshotArray).size; i++) {
/* 235 */       ((Actor)snapshotArray.get(i)).moveBy(-offsetX, -offsetY);
/*     */     }
/* 237 */     Stage stage = getStage();
/* 238 */     if (stage != null && stage.getActionsRequestRendering() && isPressed != this.clickListener.isPressed())
/* 239 */       Gdx.graphics.requestRendering(); 
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 243 */     float width = super.getPrefWidth();
/* 244 */     if (this.style.up != null) width = Math.max(width, this.style.up.getMinWidth()); 
/* 245 */     if (this.style.down != null) width = Math.max(width, this.style.down.getMinWidth()); 
/* 246 */     if (this.style.checked != null) width = Math.max(width, this.style.checked.getMinWidth()); 
/* 247 */     return width;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 251 */     float height = super.getPrefHeight();
/* 252 */     if (this.style.up != null) height = Math.max(height, this.style.up.getMinHeight()); 
/* 253 */     if (this.style.down != null) height = Math.max(height, this.style.down.getMinHeight()); 
/* 254 */     if (this.style.checked != null) height = Math.max(height, this.style.checked.getMinHeight()); 
/* 255 */     return height;
/*     */   }
/*     */   
/*     */   public float getMinWidth() {
/* 259 */     return getPrefWidth();
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/* 263 */     return getPrefHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ButtonStyle
/*     */   {
/*     */     public Drawable up;
/*     */     
/*     */     public Drawable down;
/*     */     public Drawable over;
/*     */     public Drawable checked;
/*     */     public Drawable checkedOver;
/*     */     public Drawable disabled;
/*     */     
/*     */     public ButtonStyle(Drawable up, Drawable down, Drawable checked) {
/* 278 */       this.up = up;
/* 279 */       this.down = down;
/* 280 */       this.checked = checked;
/*     */     } public float pressedOffsetX; public float pressedOffsetY; public float unpressedOffsetX; public float unpressedOffsetY; public float checkedOffsetX; public float checkedOffsetY;
/*     */     public ButtonStyle() {}
/*     */     public ButtonStyle(ButtonStyle style) {
/* 284 */       this.up = style.up;
/* 285 */       this.down = style.down;
/* 286 */       this.over = style.over;
/* 287 */       this.checked = style.checked;
/* 288 */       this.checkedOver = style.checkedOver;
/* 289 */       this.disabled = style.disabled;
/* 290 */       this.pressedOffsetX = style.pressedOffsetX;
/* 291 */       this.pressedOffsetY = style.pressedOffsetY;
/* 292 */       this.unpressedOffsetX = style.unpressedOffsetX;
/* 293 */       this.unpressedOffsetY = style.unpressedOffsetY;
/* 294 */       this.checkedOffsetX = style.checkedOffsetX;
/* 295 */       this.checkedOffsetY = style.checkedOffsetY;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Button.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */