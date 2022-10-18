/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ObjectSet;
/*     */ import com.badlogic.gdx.utils.OrderedSet;
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
/*     */ public class List<T>
/*     */   extends Widget
/*     */   implements Cullable
/*     */ {
/*     */   private ListStyle style;
/*  46 */   final Array<T> items = new Array();
/*  47 */   final ArraySelection<T> selection = new ArraySelection(this.items);
/*     */   
/*     */   private Rectangle cullingArea;
/*     */   private float prefWidth;
/*     */   private float prefHeight;
/*     */   
/*     */   public List(Skin skin) {
/*  54 */     this(skin.<ListStyle>get(ListStyle.class));
/*     */   }
/*     */   private float itemHeight; private float textOffsetX; private float textOffsetY;
/*     */   public List(Skin skin, String styleName) {
/*  58 */     this(skin.<ListStyle>get(styleName, ListStyle.class));
/*     */   }
/*     */   
/*     */   public List(ListStyle style) {
/*  62 */     this.selection.setActor(this);
/*  63 */     this.selection.setRequired(true);
/*     */     
/*  65 */     setStyle(style);
/*  66 */     setSize(getPrefWidth(), getPrefHeight());
/*     */     
/*  68 */     addListener((EventListener)new InputListener() {
/*     */           public boolean keyDown(InputEvent event, int keycode) {
/*  70 */             if (keycode == 29 && UIUtils.ctrl() && List.this.selection.getMultiple()) {
/*  71 */               List.this.selection.clear();
/*  72 */               List.this.selection.addAll(List.this.items);
/*  73 */               return true;
/*     */             } 
/*  75 */             return false;
/*     */           }
/*     */           
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  79 */             if (pointer == 0 && button != 0) return false; 
/*  80 */             if (List.this.selection.isDisabled()) return false; 
/*  81 */             if (List.this.selection.getMultiple()) List.this.getStage().setKeyboardFocus(List.this); 
/*  82 */             List.this.touchDown(y);
/*  83 */             return true;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   void touchDown(float y) {
/*  89 */     if (this.items.size == 0)
/*  90 */       return;  float height = getHeight();
/*  91 */     if (this.style.background != null) {
/*  92 */       height -= this.style.background.getTopHeight() + this.style.background.getBottomHeight();
/*  93 */       y -= this.style.background.getBottomHeight();
/*     */     } 
/*  95 */     int index = (int)((height - y) / this.itemHeight);
/*  96 */     index = Math.max(0, index);
/*  97 */     index = Math.min(this.items.size - 1, index);
/*  98 */     this.selection.choose(this.items.get(index));
/*     */   }
/*     */   
/*     */   public void setStyle(ListStyle style) {
/* 102 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/* 103 */     this.style = style;
/* 104 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListStyle getStyle() {
/* 110 */     return this.style;
/*     */   }
/*     */   
/*     */   public void layout() {
/* 114 */     BitmapFont font = this.style.font;
/* 115 */     Drawable selectedDrawable = this.style.selection;
/*     */     
/* 117 */     this.itemHeight = font.getCapHeight() - font.getDescent() * 2.0F;
/* 118 */     this.itemHeight += selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();
/*     */     
/* 120 */     this.textOffsetX = selectedDrawable.getLeftWidth();
/* 121 */     this.textOffsetY = selectedDrawable.getTopHeight() - font.getDescent();
/*     */     
/* 123 */     this.prefWidth = 0.0F;
/* 124 */     Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
/* 125 */     GlyphLayout layout = (GlyphLayout)layoutPool.obtain();
/* 126 */     for (int i = 0; i < this.items.size; i++) {
/* 127 */       layout.setText(font, toString((T)this.items.get(i)));
/* 128 */       this.prefWidth = Math.max(layout.width, this.prefWidth);
/*     */     } 
/* 130 */     layoutPool.free(layout);
/* 131 */     this.prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();
/* 132 */     this.prefHeight = this.items.size * this.itemHeight;
/*     */     
/* 134 */     Drawable background = this.style.background;
/* 135 */     if (background != null) {
/* 136 */       this.prefWidth += background.getLeftWidth() + background.getRightWidth();
/* 137 */       this.prefHeight += background.getTopHeight() + background.getBottomHeight();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 143 */     validate();
/*     */     
/* 145 */     BitmapFont font = this.style.font;
/* 146 */     Drawable selectedDrawable = this.style.selection;
/* 147 */     Color fontColorSelected = this.style.fontColorSelected;
/* 148 */     Color fontColorUnselected = this.style.fontColorUnselected;
/*     */     
/* 150 */     Color color = getColor();
/* 151 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*     */     
/* 153 */     float x = getX(), y = getY(), width = getWidth(), height = getHeight();
/* 154 */     float itemY = height;
/*     */     
/* 156 */     Drawable background = this.style.background;
/* 157 */     if (background != null) {
/* 158 */       background.draw(batch, x, y, width, height);
/* 159 */       float leftWidth = background.getLeftWidth();
/* 160 */       x += leftWidth;
/* 161 */       itemY -= background.getTopHeight();
/* 162 */       width -= leftWidth + background.getRightWidth();
/*     */     } 
/*     */     
/* 165 */     font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
/* 166 */     for (int i = 0; i < this.items.size; i++) {
/* 167 */       if (this.cullingArea == null || (itemY - this.itemHeight <= this.cullingArea.y + this.cullingArea.height && itemY >= this.cullingArea.y)) {
/* 168 */         T item = (T)this.items.get(i);
/* 169 */         boolean selected = this.selection.contains(item);
/* 170 */         if (selected) {
/* 171 */           selectedDrawable.draw(batch, x, y + itemY - this.itemHeight, width, this.itemHeight);
/* 172 */           font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
/*     */         } 
/* 174 */         drawItem(batch, font, i, item, x + this.textOffsetX, y + itemY - this.textOffsetY);
/* 175 */         if (selected) {
/* 176 */           font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
/*     */         }
/*     */       }
/* 179 */       else if (itemY < this.cullingArea.y) {
/*     */         break;
/*     */       } 
/* 182 */       itemY -= this.itemHeight;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected GlyphLayout drawItem(Batch batch, BitmapFont font, int index, T item, float x, float y) {
/* 187 */     return font.draw(batch, toString(item), x, y);
/*     */   }
/*     */   
/*     */   public ArraySelection<T> getSelection() {
/* 191 */     return this.selection;
/*     */   }
/*     */ 
/*     */   
/*     */   public T getSelected() {
/* 196 */     return (T)this.selection.first();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelected(T item) {
/* 202 */     if (this.items.contains(item, false)) {
/* 203 */       this.selection.set(item);
/* 204 */     } else if (this.selection.getRequired() && this.items.size > 0) {
/* 205 */       this.selection.set(this.items.first());
/*     */     } else {
/* 207 */       this.selection.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getSelectedIndex() {
/* 212 */     OrderedSet orderedSet = this.selection.items();
/* 213 */     return (((ObjectSet)orderedSet).size == 0) ? -1 : this.items.indexOf(orderedSet.first(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectedIndex(int index) {
/* 218 */     if (index < -1 || index >= this.items.size)
/* 219 */       throw new IllegalArgumentException("index must be >= -1 and < " + this.items.size + ": " + index); 
/* 220 */     if (index == -1) {
/* 221 */       this.selection.clear();
/*     */     } else {
/* 223 */       this.selection.set(this.items.get(index));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setItems(T... newItems) {
/* 228 */     if (newItems == null) throw new IllegalArgumentException("newItems cannot be null."); 
/* 229 */     float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();
/*     */     
/* 231 */     this.items.clear();
/* 232 */     this.items.addAll((Object[])newItems);
/* 233 */     this.selection.validate();
/*     */     
/* 235 */     invalidate();
/* 236 */     if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems(Array newItems) {
/* 242 */     if (newItems == null) throw new IllegalArgumentException("newItems cannot be null."); 
/* 243 */     float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();
/*     */     
/* 245 */     this.items.clear();
/* 246 */     this.items.addAll(newItems);
/* 247 */     this.selection.validate();
/*     */     
/* 249 */     invalidate();
/* 250 */     if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy(); 
/*     */   }
/*     */   
/*     */   public void clearItems() {
/* 254 */     if (this.items.size == 0)
/* 255 */       return;  this.items.clear();
/* 256 */     this.selection.clear();
/* 257 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<T> getItems() {
/* 262 */     return this.items;
/*     */   }
/*     */   
/*     */   public float getItemHeight() {
/* 266 */     return this.itemHeight;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 270 */     validate();
/* 271 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 275 */     validate();
/* 276 */     return this.prefHeight;
/*     */   }
/*     */   
/*     */   protected String toString(T obj) {
/* 280 */     return obj.toString();
/*     */   }
/*     */   
/*     */   public void setCullingArea(Rectangle cullingArea) {
/* 284 */     this.cullingArea = cullingArea;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ListStyle
/*     */   {
/*     */     public BitmapFont font;
/*     */     
/* 292 */     public Color fontColorSelected = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/* 293 */     public Color fontColorUnselected = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*     */     public Drawable selection;
/*     */     
/*     */     public Drawable background;
/*     */     
/*     */     public ListStyle() {}
/*     */     
/*     */     public ListStyle(BitmapFont font, Color fontColorSelected, Color fontColorUnselected, Drawable selection) {
/* 302 */       this.font = font;
/* 303 */       this.fontColorSelected.set(fontColorSelected);
/* 304 */       this.fontColorUnselected.set(fontColorUnselected);
/* 305 */       this.selection = selection;
/*     */     }
/*     */     
/*     */     public ListStyle(ListStyle style) {
/* 309 */       this.font = style.font;
/* 310 */       this.fontColorSelected.set(style.fontColorSelected);
/* 311 */       this.fontColorUnselected.set(style.fontColorUnselected);
/* 312 */       this.selection = style.selection;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\List.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */