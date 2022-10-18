/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Action;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.actions.Actions;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectBox<T>
/*     */   extends Widget
/*     */   implements Disableable
/*     */ {
/*  59 */   static final Vector2 temp = new Vector2();
/*     */   
/*     */   SelectBoxStyle style;
/*  62 */   final Array<T> items = new Array();
/*  63 */   final ArraySelection<T> selection = new ArraySelection(this.items);
/*     */   
/*     */   SelectBoxList<T> selectBoxList;
/*     */   
/*     */   private float prefWidth;
/*  68 */   private GlyphLayout layout = new GlyphLayout(); private float prefHeight; private ClickListener clickListener; boolean disabled;
/*     */   
/*     */   public SelectBox(Skin skin) {
/*  71 */     this(skin.<SelectBoxStyle>get(SelectBoxStyle.class));
/*     */   }
/*     */   
/*     */   public SelectBox(Skin skin, String styleName) {
/*  75 */     this(skin.<SelectBoxStyle>get(styleName, SelectBoxStyle.class));
/*     */   }
/*     */   
/*     */   public SelectBox(SelectBoxStyle style) {
/*  79 */     setStyle(style);
/*  80 */     setSize(getPrefWidth(), getPrefHeight());
/*     */     
/*  82 */     this.selection.setActor(this);
/*  83 */     this.selection.setRequired(true);
/*     */     
/*  85 */     this.selectBoxList = new SelectBoxList<T>(this);
/*     */     
/*  87 */     addListener((EventListener)(this.clickListener = new ClickListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  89 */             if (pointer == 0 && button != 0) return false; 
/*  90 */             if (SelectBox.this.disabled) return false; 
/*  91 */             if (SelectBox.this.selectBoxList.hasParent()) {
/*  92 */               SelectBox.this.hideList();
/*     */             } else {
/*  94 */               SelectBox.this.showList();
/*  95 */             }  return true;
/*     */           }
/*     */         }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxListCount(int maxListCount) {
/* 103 */     this.selectBoxList.maxListCount = maxListCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxListCount() {
/* 108 */     return this.selectBoxList.maxListCount;
/*     */   }
/*     */   
/*     */   protected void setStage(Stage stage) {
/* 112 */     if (stage == null) this.selectBoxList.hide(); 
/* 113 */     super.setStage(stage);
/*     */   }
/*     */   
/*     */   public void setStyle(SelectBoxStyle style) {
/* 117 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/* 118 */     this.style = style;
/* 119 */     if (this.selectBoxList != null) {
/* 120 */       this.selectBoxList.setStyle(style.scrollStyle);
/* 121 */       this.selectBoxList.list.setStyle(style.listStyle);
/*     */     } 
/* 123 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SelectBoxStyle getStyle() {
/* 129 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems(T... newItems) {
/* 134 */     if (newItems == null) throw new IllegalArgumentException("newItems cannot be null."); 
/* 135 */     float oldPrefWidth = getPrefWidth();
/*     */     
/* 137 */     this.items.clear();
/* 138 */     this.items.addAll((Object[])newItems);
/* 139 */     this.selection.validate();
/* 140 */     this.selectBoxList.list.setItems(this.items);
/*     */     
/* 142 */     invalidate();
/* 143 */     if (oldPrefWidth != getPrefWidth()) invalidateHierarchy();
/*     */   
/*     */   }
/*     */   
/*     */   public void setItems(Array<T> newItems) {
/* 148 */     if (newItems == null) throw new IllegalArgumentException("newItems cannot be null."); 
/* 149 */     float oldPrefWidth = getPrefWidth();
/*     */     
/* 151 */     this.items.clear();
/* 152 */     this.items.addAll(newItems);
/* 153 */     this.selection.validate();
/* 154 */     this.selectBoxList.list.setItems(this.items);
/*     */     
/* 156 */     invalidate();
/* 157 */     if (oldPrefWidth != getPrefWidth()) invalidateHierarchy(); 
/*     */   }
/*     */   
/*     */   public void clearItems() {
/* 161 */     if (this.items.size == 0)
/* 162 */       return;  this.items.clear();
/* 163 */     this.selection.clear();
/* 164 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<T> getItems() {
/* 169 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   public void layout() {
/* 174 */     Drawable bg = this.style.background;
/* 175 */     BitmapFont font = this.style.font;
/*     */     
/* 177 */     if (bg != null) {
/* 178 */       this.prefHeight = Math.max(bg.getTopHeight() + bg.getBottomHeight() + font.getCapHeight() - font.getDescent() * 2.0F, bg
/* 179 */           .getMinHeight());
/*     */     } else {
/* 181 */       this.prefHeight = font.getCapHeight() - font.getDescent() * 2.0F;
/*     */     } 
/* 183 */     float maxItemWidth = 0.0F;
/* 184 */     Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
/* 185 */     GlyphLayout layout = (GlyphLayout)layoutPool.obtain();
/* 186 */     for (int i = 0; i < this.items.size; i++) {
/* 187 */       layout.setText(font, toString((T)this.items.get(i)));
/* 188 */       maxItemWidth = Math.max(layout.width, maxItemWidth);
/*     */     } 
/* 190 */     layoutPool.free(layout);
/*     */     
/* 192 */     this.prefWidth = maxItemWidth;
/* 193 */     if (bg != null) this.prefWidth += bg.getLeftWidth() + bg.getRightWidth();
/*     */     
/* 195 */     List.ListStyle listStyle = this.style.listStyle;
/* 196 */     ScrollPane.ScrollPaneStyle scrollStyle = this.style.scrollStyle;
/* 197 */     float listWidth = maxItemWidth + listStyle.selection.getLeftWidth() + listStyle.selection.getRightWidth();
/* 198 */     if (scrollStyle.background != null)
/* 199 */       listWidth += scrollStyle.background.getLeftWidth() + scrollStyle.background.getRightWidth(); 
/* 200 */     if (this.selectBoxList == null || !this.selectBoxList.disableY)
/* 201 */       listWidth += Math.max((this.style.scrollStyle.vScroll != null) ? this.style.scrollStyle.vScroll.getMinWidth() : 0.0F, (this.style.scrollStyle.vScrollKnob != null) ? this.style.scrollStyle.vScrollKnob
/* 202 */           .getMinWidth() : 0.0F); 
/* 203 */     this.prefWidth = Math.max(this.prefWidth, listWidth);
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*     */     Drawable background;
/* 208 */     validate();
/*     */ 
/*     */     
/* 211 */     if (this.disabled && this.style.backgroundDisabled != null) {
/* 212 */       background = this.style.backgroundDisabled;
/* 213 */     } else if (this.selectBoxList.hasParent() && this.style.backgroundOpen != null) {
/* 214 */       background = this.style.backgroundOpen;
/* 215 */     } else if (this.clickListener.isOver() && this.style.backgroundOver != null) {
/* 216 */       background = this.style.backgroundOver;
/* 217 */     } else if (this.style.background != null) {
/* 218 */       background = this.style.background;
/*     */     } else {
/* 220 */       background = null;
/* 221 */     }  BitmapFont font = this.style.font;
/* 222 */     Color fontColor = (this.disabled && this.style.disabledFontColor != null) ? this.style.disabledFontColor : this.style.fontColor;
/*     */     
/* 224 */     Color color = getColor();
/* 225 */     float x = getX();
/* 226 */     float y = getY();
/* 227 */     float width = getWidth();
/* 228 */     float height = getHeight();
/*     */     
/* 230 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/* 231 */     if (background != null) background.draw(batch, x, y, width, height);
/*     */     
/* 233 */     T selected = (T)this.selection.first();
/* 234 */     if (selected != null) {
/* 235 */       String string = toString(selected);
/* 236 */       if (background != null) {
/* 237 */         width -= background.getLeftWidth() + background.getRightWidth();
/* 238 */         height -= background.getBottomHeight() + background.getTopHeight();
/* 239 */         x += background.getLeftWidth();
/* 240 */         y += (int)(height / 2.0F + background.getBottomHeight() + (font.getData()).capHeight / 2.0F);
/*     */       } else {
/* 242 */         y += (int)(height / 2.0F + (font.getData()).capHeight / 2.0F);
/*     */       } 
/* 244 */       font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
/* 245 */       this.layout.setText(font, string, 0, string.length(), font.getColor(), width, 8, false, "...");
/* 246 */       font.draw(batch, this.layout, x, y);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ArraySelection<T> getSelection() {
/* 253 */     return this.selection;
/*     */   }
/*     */ 
/*     */   
/*     */   public T getSelected() {
/* 258 */     return (T)this.selection.first();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelected(T item) {
/* 263 */     if (this.items.contains(item, false)) {
/* 264 */       this.selection.set(item);
/* 265 */     } else if (this.items.size > 0) {
/* 266 */       this.selection.set(this.items.first());
/*     */     } else {
/* 268 */       this.selection.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getSelectedIndex() {
/* 273 */     OrderedSet orderedSet = this.selection.items();
/* 274 */     return (((ObjectSet)orderedSet).size == 0) ? -1 : this.items.indexOf(orderedSet.first(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectedIndex(int index) {
/* 279 */     this.selection.set(this.items.get(index));
/*     */   }
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 283 */     if (disabled && !this.disabled) hideList(); 
/* 284 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 288 */     return this.disabled;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 292 */     validate();
/* 293 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 297 */     validate();
/* 298 */     return this.prefHeight;
/*     */   }
/*     */   
/*     */   protected String toString(T obj) {
/* 302 */     return obj.toString();
/*     */   }
/*     */   
/*     */   public void showList() {
/* 306 */     if (this.items.size == 0)
/* 307 */       return;  this.selectBoxList.show(getStage());
/*     */   }
/*     */   
/*     */   public void hideList() {
/* 311 */     this.selectBoxList.hide();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<T> getList() {
/* 316 */     return this.selectBoxList.list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScrollingDisabled(boolean y) {
/* 321 */     this.selectBoxList.setScrollingDisabled(true, y);
/* 322 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */   
/*     */   public ScrollPane getScrollPane() {
/* 327 */     return this.selectBoxList;
/*     */   }
/*     */   
/*     */   protected void onShow(Actor selectBoxList, boolean below) {
/* 331 */     (selectBoxList.getColor()).a = 0.0F;
/* 332 */     selectBoxList.addAction((Action)Actions.fadeIn(0.3F, Interpolation.fade));
/*     */   }
/*     */   
/*     */   protected void onHide(Actor selectBoxList) {
/* 336 */     (selectBoxList.getColor()).a = 1.0F;
/* 337 */     selectBoxList.addAction((Action)Actions.sequence((Action)Actions.fadeOut(0.15F, Interpolation.fade), (Action)Actions.removeActor()));
/*     */   }
/*     */   
/*     */   static class SelectBoxList<T>
/*     */     extends ScrollPane {
/*     */     private final SelectBox<T> selectBox;
/*     */     int maxListCount;
/* 344 */     private final Vector2 screenPosition = new Vector2();
/*     */     final List<T> list;
/*     */     private InputListener hideListener;
/*     */     private Actor previousScrollFocus;
/*     */     
/*     */     public SelectBoxList(final SelectBox<T> selectBox) {
/* 350 */       super((Actor)null, selectBox.style.scrollStyle);
/* 351 */       this.selectBox = selectBox;
/*     */       
/* 353 */       setOverscroll(false, false);
/* 354 */       setFadeScrollBars(false);
/* 355 */       setScrollingDisabled(true, false);
/*     */       
/* 357 */       this.list = new List<T>(selectBox.style.listStyle)
/*     */         {
/*     */           protected String toString(T obj) {
/* 360 */             return selectBox.toString(obj);
/*     */           }
/*     */         };
/* 363 */       this.list.setTouchable(Touchable.disabled);
/* 364 */       setWidget(this.list);
/*     */       
/* 366 */       this.list.addListener((EventListener)new ClickListener() {
/*     */             public void clicked(InputEvent event, float x, float y) {
/* 368 */               selectBox.selection.choose(SelectBox.SelectBoxList.this.list.getSelected());
/* 369 */               SelectBox.SelectBoxList.this.hide();
/*     */             }
/*     */             
/*     */             public boolean mouseMoved(InputEvent event, float x, float y) {
/* 373 */               SelectBox.SelectBoxList.this.list.setSelectedIndex(Math.min(selectBox.items.size - 1, (int)((SelectBox.SelectBoxList.this.list.getHeight() - y) / SelectBox.SelectBoxList.this.list.getItemHeight())));
/* 374 */               return true;
/*     */             }
/*     */           });
/*     */       
/* 378 */       addListener((EventListener)new InputListener() {
/*     */             public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
/* 380 */               if (toActor == null || !SelectBox.SelectBoxList.this.isAscendantOf(toActor)) SelectBox.SelectBoxList.this.list.selection.set(selectBox.getSelected());
/*     */             
/*     */             }
/*     */           });
/* 384 */       this.hideListener = new InputListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/* 386 */             Actor target = event.getTarget();
/* 387 */             if (SelectBox.SelectBoxList.this.isAscendantOf(target)) return false; 
/* 388 */             SelectBox.SelectBoxList.this.list.selection.set(selectBox.getSelected());
/* 389 */             SelectBox.SelectBoxList.this.hide();
/* 390 */             return false;
/*     */           }
/*     */           
/*     */           public boolean keyDown(InputEvent event, int keycode) {
/* 394 */             if (keycode == 131) SelectBox.SelectBoxList.this.hide(); 
/* 395 */             return false;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public void show(Stage stage) {
/* 401 */       if (this.list.isTouchable())
/*     */         return; 
/* 403 */       stage.removeCaptureListener((EventListener)this.hideListener);
/* 404 */       stage.addCaptureListener((EventListener)this.hideListener);
/* 405 */       stage.addActor((Actor)this);
/*     */       
/* 407 */       this.selectBox.localToStageCoordinates(this.screenPosition.set(0.0F, 0.0F));
/*     */ 
/*     */       
/* 410 */       float itemHeight = this.list.getItemHeight();
/* 411 */       float height = itemHeight * ((this.maxListCount <= 0) ? this.selectBox.items.size : Math.min(this.maxListCount, this.selectBox.items.size));
/* 412 */       Drawable scrollPaneBackground = (getStyle()).background;
/* 413 */       if (scrollPaneBackground != null) height += scrollPaneBackground.getTopHeight() + scrollPaneBackground.getBottomHeight(); 
/* 414 */       Drawable listBackground = (this.list.getStyle()).background;
/* 415 */       if (listBackground != null) height += listBackground.getTopHeight() + listBackground.getBottomHeight();
/*     */       
/* 417 */       float heightBelow = this.screenPosition.y;
/* 418 */       float heightAbove = (stage.getCamera()).viewportHeight - this.screenPosition.y - this.selectBox.getHeight();
/* 419 */       boolean below = true;
/* 420 */       if (height > heightBelow) {
/* 421 */         if (heightAbove > heightBelow) {
/* 422 */           below = false;
/* 423 */           height = Math.min(height, heightAbove);
/*     */         } else {
/* 425 */           height = heightBelow;
/*     */         } 
/*     */       }
/* 428 */       if (below) {
/* 429 */         setY(this.screenPosition.y - height);
/*     */       } else {
/* 431 */         setY(this.screenPosition.y + this.selectBox.getHeight());
/* 432 */       }  setX(this.screenPosition.x);
/* 433 */       setHeight(height);
/* 434 */       validate();
/* 435 */       float width = Math.max(getPrefWidth(), this.selectBox.getWidth());
/* 436 */       if (getPrefHeight() > height && !this.disableY) width += getScrollBarWidth(); 
/* 437 */       setWidth(width);
/*     */       
/* 439 */       validate();
/* 440 */       scrollTo(0.0F, this.list.getHeight() - this.selectBox.getSelectedIndex() * itemHeight - itemHeight / 2.0F, 0.0F, 0.0F, true, true);
/* 441 */       updateVisualScroll();
/*     */       
/* 443 */       this.previousScrollFocus = null;
/* 444 */       Actor actor = stage.getScrollFocus();
/* 445 */       if (actor != null && !actor.isDescendantOf((Actor)this)) this.previousScrollFocus = actor; 
/* 446 */       stage.setScrollFocus((Actor)this);
/*     */       
/* 448 */       this.list.selection.set(this.selectBox.getSelected());
/* 449 */       this.list.setTouchable(Touchable.enabled);
/* 450 */       clearActions();
/* 451 */       this.selectBox.onShow((Actor)this, below);
/*     */     }
/*     */     
/*     */     public void hide() {
/* 455 */       if (!this.list.isTouchable() || !hasParent())
/* 456 */         return;  this.list.setTouchable(Touchable.disabled);
/*     */       
/* 458 */       Stage stage = getStage();
/* 459 */       if (stage != null) {
/* 460 */         stage.removeCaptureListener((EventListener)this.hideListener);
/* 461 */         if (this.previousScrollFocus != null && this.previousScrollFocus.getStage() == null) this.previousScrollFocus = null; 
/* 462 */         Actor actor = stage.getScrollFocus();
/* 463 */         if (actor == null || isAscendantOf(actor)) stage.setScrollFocus(this.previousScrollFocus);
/*     */       
/*     */       } 
/* 466 */       clearActions();
/* 467 */       this.selectBox.onHide((Actor)this);
/*     */     }
/*     */     
/*     */     public void draw(Batch batch, float parentAlpha) {
/* 471 */       this.selectBox.localToStageCoordinates(SelectBox.temp.set(0.0F, 0.0F));
/* 472 */       if (!SelectBox.temp.equals(this.screenPosition)) hide(); 
/* 473 */       super.draw(batch, parentAlpha);
/*     */     }
/*     */     
/*     */     public void act(float delta) {
/* 477 */       super.act(delta);
/* 478 */       toFront();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SelectBoxStyle
/*     */   {
/*     */     public BitmapFont font;
/*     */     
/* 487 */     public Color fontColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*     */     public Color disabledFontColor;
/*     */     
/*     */     public Drawable background;
/*     */     
/*     */     public ScrollPane.ScrollPaneStyle scrollStyle;
/*     */     public List.ListStyle listStyle;
/*     */     public Drawable backgroundOver;
/*     */     public Drawable backgroundOpen;
/*     */     public Drawable backgroundDisabled;
/*     */     
/*     */     public SelectBoxStyle() {}
/*     */     
/*     */     public SelectBoxStyle(BitmapFont font, Color fontColor, Drawable background, ScrollPane.ScrollPaneStyle scrollStyle, List.ListStyle listStyle) {
/* 502 */       this.font = font;
/* 503 */       this.fontColor.set(fontColor);
/* 504 */       this.background = background;
/* 505 */       this.scrollStyle = scrollStyle;
/* 506 */       this.listStyle = listStyle;
/*     */     }
/*     */     
/*     */     public SelectBoxStyle(SelectBoxStyle style) {
/* 510 */       this.font = style.font;
/* 511 */       this.fontColor.set(style.fontColor);
/* 512 */       if (style.disabledFontColor != null) this.disabledFontColor = new Color(style.disabledFontColor); 
/* 513 */       this.background = style.background;
/* 514 */       this.backgroundOver = style.backgroundOver;
/* 515 */       this.backgroundOpen = style.backgroundOpen;
/* 516 */       this.backgroundDisabled = style.backgroundDisabled;
/* 517 */       this.scrollStyle = new ScrollPane.ScrollPaneStyle(style.scrollStyle);
/* 518 */       this.listStyle = new List.ListStyle(style.listStyle);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\SelectBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */