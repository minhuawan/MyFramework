/*     */ package com.badlogic.gdx.scenes.scene2d;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.DelayedRemovalArray;
/*     */ import com.badlogic.gdx.utils.Pools;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
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
/*     */ public class Actor
/*     */ {
/*     */   private Stage stage;
/*     */   Group parent;
/*  60 */   private final DelayedRemovalArray<EventListener> listeners = new DelayedRemovalArray(0);
/*  61 */   private final DelayedRemovalArray<EventListener> captureListeners = new DelayedRemovalArray(0);
/*  62 */   private final Array<Action> actions = new Array(0);
/*     */   private String name;
/*     */   private boolean visible = true;
/*  65 */   private Touchable touchable = Touchable.enabled; private boolean debug; float x; float y;
/*     */   float width;
/*     */   float height;
/*     */   float originX;
/*     */   float originY;
/*  70 */   float scaleX = 1.0F; float scaleY = 1.0F;
/*     */   float rotation;
/*  72 */   final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object userObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void act(float delta) {
/*  91 */     Array<Action> actions = this.actions;
/*  92 */     if (actions.size > 0) {
/*  93 */       if (this.stage != null && this.stage.getActionsRequestRendering()) Gdx.graphics.requestRendering(); 
/*  94 */       for (int i = 0; i < actions.size; i++) {
/*  95 */         Action action = (Action)actions.get(i);
/*  96 */         if (action.act(delta) && i < actions.size) {
/*  97 */           Action current = (Action)actions.get(i);
/*  98 */           int actionIndex = (current == action) ? i : actions.indexOf(action, true);
/*  99 */           if (actionIndex != -1) {
/* 100 */             actions.removeIndex(actionIndex);
/* 101 */             action.setActor(null);
/* 102 */             i--;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
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
/*     */   public boolean fire(Event event) {
/* 122 */     if (event.getStage() == null) event.setStage(getStage()); 
/* 123 */     event.setTarget(this);
/*     */ 
/*     */     
/* 126 */     Array<Group> ancestors = (Array<Group>)Pools.obtain(Array.class);
/* 127 */     Group parent = this.parent;
/* 128 */     while (parent != null) {
/* 129 */       ancestors.add(parent);
/* 130 */       parent = parent.parent;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 135 */       Object[] ancestorsArray = ancestors.items; int i;
/* 136 */       for (i = ancestors.size - 1; i >= 0; i--) {
/* 137 */         Group currentTarget = (Group)ancestorsArray[i];
/* 138 */         currentTarget.notify(event, true);
/* 139 */         if (event.isStopped()) return event.isCancelled();
/*     */       
/*     */       } 
/*     */       
/* 143 */       notify(event, true);
/* 144 */       if (event.isStopped()) return event.isCancelled();
/*     */ 
/*     */       
/* 147 */       notify(event, false);
/* 148 */       if (!event.getBubbles()) return event.isCancelled(); 
/* 149 */       if (event.isStopped()) return event.isCancelled();
/*     */       
/*     */       int n;
/* 152 */       for (i = 0, n = ancestors.size; i < n; i++) {
/* 153 */         ((Group)ancestorsArray[i]).notify(event, false);
/* 154 */         if (event.isStopped()) return event.isCancelled();
/*     */       
/*     */       } 
/* 157 */       return event.isCancelled();
/*     */     } finally {
/* 159 */       ancestors.clear();
/* 160 */       Pools.free(ancestors);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean notify(Event event, boolean capture) {
/* 170 */     if (event.getTarget() == null) throw new IllegalArgumentException("The event target cannot be null.");
/*     */     
/* 172 */     DelayedRemovalArray<EventListener> listeners = capture ? this.captureListeners : this.listeners;
/* 173 */     if (listeners.size == 0) return event.isCancelled();
/*     */     
/* 175 */     event.setListenerActor(this);
/* 176 */     event.setCapture(capture);
/* 177 */     if (event.getStage() == null) event.setStage(this.stage);
/*     */     
/* 179 */     listeners.begin();
/* 180 */     for (int i = 0, n = listeners.size; i < n; i++) {
/* 181 */       EventListener listener = (EventListener)listeners.get(i);
/* 182 */       if (listener.handle(event)) {
/* 183 */         event.handle();
/* 184 */         if (event instanceof InputEvent) {
/* 185 */           InputEvent inputEvent = (InputEvent)event;
/* 186 */           if (inputEvent.getType() == InputEvent.Type.touchDown) {
/* 187 */             event.getStage().addTouchFocus(listener, this, inputEvent.getTarget(), inputEvent.getPointer(), inputEvent
/* 188 */                 .getButton());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 193 */     listeners.end();
/*     */     
/* 195 */     return event.isCancelled();
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
/*     */   public Actor hit(float x, float y, boolean touchable) {
/* 209 */     if (touchable && this.touchable != Touchable.enabled) return null; 
/* 210 */     return (x >= 0.0F && x < this.width && y >= 0.0F && y < this.height) ? this : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove() {
/* 216 */     if (this.parent != null) return this.parent.removeActor(this, true); 
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addListener(EventListener listener) {
/* 224 */     if (listener == null) throw new IllegalArgumentException("listener cannot be null."); 
/* 225 */     if (!this.listeners.contains(listener, true)) {
/* 226 */       this.listeners.add(listener);
/* 227 */       return true;
/*     */     } 
/* 229 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removeListener(EventListener listener) {
/* 233 */     if (listener == null) throw new IllegalArgumentException("listener cannot be null."); 
/* 234 */     return this.listeners.removeValue(listener, true);
/*     */   }
/*     */   
/*     */   public Array<EventListener> getListeners() {
/* 238 */     return (Array<EventListener>)this.listeners;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addCaptureListener(EventListener listener) {
/* 244 */     if (listener == null) throw new IllegalArgumentException("listener cannot be null."); 
/* 245 */     if (!this.captureListeners.contains(listener, true)) this.captureListeners.add(listener); 
/* 246 */     return true;
/*     */   }
/*     */   
/*     */   public boolean removeCaptureListener(EventListener listener) {
/* 250 */     if (listener == null) throw new IllegalArgumentException("listener cannot be null."); 
/* 251 */     return this.captureListeners.removeValue(listener, true);
/*     */   }
/*     */   
/*     */   public Array<EventListener> getCaptureListeners() {
/* 255 */     return (Array<EventListener>)this.captureListeners;
/*     */   }
/*     */   
/*     */   public void addAction(Action action) {
/* 259 */     action.setActor(this);
/* 260 */     this.actions.add(action);
/*     */     
/* 262 */     if (this.stage != null && this.stage.getActionsRequestRendering()) Gdx.graphics.requestRendering(); 
/*     */   }
/*     */   
/*     */   public void removeAction(Action action) {
/* 266 */     if (this.actions.removeValue(action, true)) action.setActor(null); 
/*     */   }
/*     */   
/*     */   public Array<Action> getActions() {
/* 270 */     return this.actions;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasActions() {
/* 275 */     return (this.actions.size > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearActions() {
/* 280 */     for (int i = this.actions.size - 1; i >= 0; i--)
/* 281 */       ((Action)this.actions.get(i)).setActor(null); 
/* 282 */     this.actions.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearListeners() {
/* 287 */     this.listeners.clear();
/* 288 */     this.captureListeners.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 293 */     clearActions();
/* 294 */     clearListeners();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stage getStage() {
/* 299 */     return this.stage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setStage(Stage stage) {
/* 305 */     this.stage = stage;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDescendantOf(Actor actor) {
/* 310 */     if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 311 */     Actor parent = this;
/*     */     while (true) {
/* 313 */       if (parent == null) return false; 
/* 314 */       if (parent == actor) return true; 
/* 315 */       parent = parent.parent;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAscendantOf(Actor actor) {
/* 321 */     if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/*     */     while (true) {
/* 323 */       if (actor == null) return false; 
/* 324 */       if (actor == this) return true; 
/* 325 */       actor = actor.parent;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Actor> T firstAscendant(Class<T> type) {
/* 331 */     if (type == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 332 */     Actor actor = this;
/*     */     while (true) {
/* 334 */       if (ClassReflection.isInstance(type, actor)) return (T)actor; 
/* 335 */       actor = actor.getParent();
/* 336 */       if (actor == null)
/* 337 */         return null; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasParent() {
/* 342 */     return (this.parent != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Group getParent() {
/* 347 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setParent(Group parent) {
/* 353 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouchable() {
/* 358 */     return (this.touchable == Touchable.enabled);
/*     */   }
/*     */   
/*     */   public Touchable getTouchable() {
/* 362 */     return this.touchable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTouchable(Touchable touchable) {
/* 367 */     this.touchable = touchable;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 371 */     return this.visible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 376 */     this.visible = visible;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getUserObject() {
/* 381 */     return this.userObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUserObject(Object userObject) {
/* 386 */     this.userObject = userObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/* 391 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX(int alignment) {
/* 396 */     float x = this.x;
/* 397 */     if ((alignment & 0x10) != 0) {
/* 398 */       x += this.width;
/* 399 */     } else if ((alignment & 0x8) == 0) {
/* 400 */       x += this.width / 2.0F;
/* 401 */     }  return x;
/*     */   }
/*     */   
/*     */   public void setX(float x) {
/* 405 */     if (this.x != x) {
/* 406 */       this.x = x;
/* 407 */       positionChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 413 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(float y) {
/* 417 */     if (this.y != y) {
/* 418 */       this.y = y;
/* 419 */       positionChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY(int alignment) {
/* 425 */     float y = this.y;
/* 426 */     if ((alignment & 0x2) != 0) {
/* 427 */       y += this.height;
/* 428 */     } else if ((alignment & 0x4) == 0) {
/* 429 */       y += this.height / 2.0F;
/* 430 */     }  return y;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 435 */     if (this.x != x || this.y != y) {
/* 436 */       this.x = x;
/* 437 */       this.y = y;
/* 438 */       positionChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y, int alignment) {
/* 445 */     if ((alignment & 0x10) != 0) {
/* 446 */       x -= this.width;
/* 447 */     } else if ((alignment & 0x8) == 0) {
/* 448 */       x -= this.width / 2.0F;
/*     */     } 
/* 450 */     if ((alignment & 0x2) != 0) {
/* 451 */       y -= this.height;
/* 452 */     } else if ((alignment & 0x4) == 0) {
/* 453 */       y -= this.height / 2.0F;
/*     */     } 
/* 455 */     if (this.x != x || this.y != y) {
/* 456 */       this.x = x;
/* 457 */       this.y = y;
/* 458 */       positionChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveBy(float x, float y) {
/* 464 */     if (x != 0.0F || y != 0.0F) {
/* 465 */       this.x += x;
/* 466 */       this.y += y;
/* 467 */       positionChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getWidth() {
/* 472 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(float width) {
/* 476 */     if (this.width != width) {
/* 477 */       this.width = width;
/* 478 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getHeight() {
/* 483 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(float height) {
/* 487 */     if (this.height != height) {
/* 488 */       this.height = height;
/* 489 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTop() {
/* 495 */     return this.y + this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRight() {
/* 500 */     return this.x + this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void positionChanged() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sizeChanged() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rotationChanged() {}
/*     */ 
/*     */   
/*     */   public void setSize(float width, float height) {
/* 517 */     if (this.width != width || this.height != height) {
/* 518 */       this.width = width;
/* 519 */       this.height = height;
/* 520 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sizeBy(float size) {
/* 526 */     if (size != 0.0F) {
/* 527 */       this.width += size;
/* 528 */       this.height += size;
/* 529 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sizeBy(float width, float height) {
/* 535 */     if (width != 0.0F || height != 0.0F) {
/* 536 */       this.width += width;
/* 537 */       this.height += height;
/* 538 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBounds(float x, float y, float width, float height) {
/* 544 */     if (this.x != x || this.y != y) {
/* 545 */       this.x = x;
/* 546 */       this.y = y;
/* 547 */       positionChanged();
/*     */     } 
/* 549 */     if (this.width != width || this.height != height) {
/* 550 */       this.width = width;
/* 551 */       this.height = height;
/* 552 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getOriginX() {
/* 557 */     return this.originX;
/*     */   }
/*     */   
/*     */   public void setOriginX(float originX) {
/* 561 */     this.originX = originX;
/*     */   }
/*     */   
/*     */   public float getOriginY() {
/* 565 */     return this.originY;
/*     */   }
/*     */   
/*     */   public void setOriginY(float originY) {
/* 569 */     this.originY = originY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrigin(float originX, float originY) {
/* 574 */     this.originX = originX;
/* 575 */     this.originY = originY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrigin(int alignment) {
/* 580 */     if ((alignment & 0x8) != 0) {
/* 581 */       this.originX = 0.0F;
/* 582 */     } else if ((alignment & 0x10) != 0) {
/* 583 */       this.originX = this.width;
/*     */     } else {
/* 585 */       this.originX = this.width / 2.0F;
/*     */     } 
/* 587 */     if ((alignment & 0x4) != 0) {
/* 588 */       this.originY = 0.0F;
/* 589 */     } else if ((alignment & 0x2) != 0) {
/* 590 */       this.originY = this.height;
/*     */     } else {
/* 592 */       this.originY = this.height / 2.0F;
/*     */     } 
/*     */   }
/*     */   public float getScaleX() {
/* 596 */     return this.scaleX;
/*     */   }
/*     */   
/*     */   public void setScaleX(float scaleX) {
/* 600 */     this.scaleX = scaleX;
/*     */   }
/*     */   
/*     */   public float getScaleY() {
/* 604 */     return this.scaleY;
/*     */   }
/*     */   
/*     */   public void setScaleY(float scaleY) {
/* 608 */     this.scaleY = scaleY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScale(float scaleXY) {
/* 613 */     this.scaleX = scaleXY;
/* 614 */     this.scaleY = scaleXY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScale(float scaleX, float scaleY) {
/* 619 */     this.scaleX = scaleX;
/* 620 */     this.scaleY = scaleY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scaleBy(float scale) {
/* 625 */     this.scaleX += scale;
/* 626 */     this.scaleY += scale;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scaleBy(float scaleX, float scaleY) {
/* 631 */     this.scaleX += scaleX;
/* 632 */     this.scaleY += scaleY;
/*     */   }
/*     */   
/*     */   public float getRotation() {
/* 636 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public void setRotation(float degrees) {
/* 640 */     if (this.rotation != degrees) {
/* 641 */       this.rotation = degrees;
/* 642 */       rotationChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotateBy(float amountInDegrees) {
/* 648 */     if (amountInDegrees != 0.0F) {
/* 649 */       this.rotation += amountInDegrees;
/* 650 */       rotationChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 655 */     this.color.set(color);
/*     */   }
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/* 659 */     this.color.set(r, g, b, a);
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 664 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 670 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 677 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toFront() {
/* 682 */     setZIndex(2147483647);
/*     */   }
/*     */ 
/*     */   
/*     */   public void toBack() {
/* 687 */     setZIndex(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZIndex(int index) {
/* 694 */     if (index < 0) throw new IllegalArgumentException("ZIndex cannot be < 0."); 
/* 695 */     Group parent = this.parent;
/* 696 */     if (parent == null)
/* 697 */       return;  SnapshotArray<Actor> snapshotArray = parent.children;
/* 698 */     if (((Array)snapshotArray).size == 1)
/* 699 */       return;  index = Math.min(index, ((Array)snapshotArray).size - 1);
/* 700 */     if (snapshotArray.get(index) == this)
/* 701 */       return;  if (!snapshotArray.removeValue(this, true))
/* 702 */       return;  snapshotArray.insert(index, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getZIndex() {
/* 708 */     Group parent = this.parent;
/* 709 */     if (parent == null) return -1; 
/* 710 */     return parent.children.indexOf(this, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean clipBegin() {
/* 715 */     return clipBegin(this.x, this.y, this.width, this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean clipBegin(float x, float y, float width, float height) {
/* 724 */     if (width <= 0.0F || height <= 0.0F) return false; 
/* 725 */     Rectangle tableBounds = Rectangle.tmp;
/* 726 */     tableBounds.x = x;
/* 727 */     tableBounds.y = y;
/* 728 */     tableBounds.width = width;
/* 729 */     tableBounds.height = height;
/* 730 */     Stage stage = this.stage;
/* 731 */     Rectangle scissorBounds = (Rectangle)Pools.obtain(Rectangle.class);
/* 732 */     stage.calculateScissors(tableBounds, scissorBounds);
/* 733 */     if (ScissorStack.pushScissors(scissorBounds)) return true; 
/* 734 */     Pools.free(scissorBounds);
/* 735 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clipEnd() {
/* 740 */     Pools.free(ScissorStack.popScissors());
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 screenToLocalCoordinates(Vector2 screenCoords) {
/* 745 */     Stage stage = this.stage;
/* 746 */     if (stage == null) return screenCoords; 
/* 747 */     return stageToLocalCoordinates(stage.screenToStageCoordinates(screenCoords));
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 stageToLocalCoordinates(Vector2 stageCoords) {
/* 752 */     if (this.parent != null) this.parent.stageToLocalCoordinates(stageCoords); 
/* 753 */     parentToLocalCoordinates(stageCoords);
/* 754 */     return stageCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 localToStageCoordinates(Vector2 localCoords) {
/* 760 */     return localToAscendantCoordinates(null, localCoords);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 localToParentCoordinates(Vector2 localCoords) {
/* 765 */     float rotation = -this.rotation;
/* 766 */     float scaleX = this.scaleX;
/* 767 */     float scaleY = this.scaleY;
/* 768 */     float x = this.x;
/* 769 */     float y = this.y;
/* 770 */     if (rotation == 0.0F) {
/* 771 */       if (scaleX == 1.0F && scaleY == 1.0F) {
/* 772 */         localCoords.x += x;
/* 773 */         localCoords.y += y;
/*     */       } else {
/* 775 */         float originX = this.originX;
/* 776 */         float originY = this.originY;
/* 777 */         localCoords.x = (localCoords.x - originX) * scaleX + originX + x;
/* 778 */         localCoords.y = (localCoords.y - originY) * scaleY + originY + y;
/*     */       } 
/*     */     } else {
/* 781 */       float cos = (float)Math.cos((rotation * 0.017453292F));
/* 782 */       float sin = (float)Math.sin((rotation * 0.017453292F));
/* 783 */       float originX = this.originX;
/* 784 */       float originY = this.originY;
/* 785 */       float tox = (localCoords.x - originX) * scaleX;
/* 786 */       float toy = (localCoords.y - originY) * scaleY;
/* 787 */       localCoords.x = tox * cos + toy * sin + originX + x;
/* 788 */       localCoords.y = tox * -sin + toy * cos + originY + y;
/*     */     } 
/* 790 */     return localCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 localToAscendantCoordinates(Actor ascendant, Vector2 localCoords) {
/* 795 */     Actor actor = this;
/* 796 */     while (actor != null) {
/* 797 */       actor.localToParentCoordinates(localCoords);
/* 798 */       actor = actor.parent;
/* 799 */       if (actor == ascendant)
/*     */         break; 
/* 801 */     }  return localCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
/* 806 */     float rotation = this.rotation;
/* 807 */     float scaleX = this.scaleX;
/* 808 */     float scaleY = this.scaleY;
/* 809 */     float childX = this.x;
/* 810 */     float childY = this.y;
/* 811 */     if (rotation == 0.0F) {
/* 812 */       if (scaleX == 1.0F && scaleY == 1.0F) {
/* 813 */         parentCoords.x -= childX;
/* 814 */         parentCoords.y -= childY;
/*     */       } else {
/* 816 */         float originX = this.originX;
/* 817 */         float originY = this.originY;
/* 818 */         parentCoords.x = (parentCoords.x - childX - originX) / scaleX + originX;
/* 819 */         parentCoords.y = (parentCoords.y - childY - originY) / scaleY + originY;
/*     */       } 
/*     */     } else {
/* 822 */       float cos = (float)Math.cos((rotation * 0.017453292F));
/* 823 */       float sin = (float)Math.sin((rotation * 0.017453292F));
/* 824 */       float originX = this.originX;
/* 825 */       float originY = this.originY;
/* 826 */       float tox = parentCoords.x - childX - originX;
/* 827 */       float toy = parentCoords.y - childY - originY;
/* 828 */       parentCoords.x = (tox * cos + toy * sin) / scaleX + originX;
/* 829 */       parentCoords.y = (tox * -sin + toy * cos) / scaleY + originY;
/*     */     } 
/* 831 */     return parentCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawDebug(ShapeRenderer shapes) {
/* 836 */     drawDebugBounds(shapes);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawDebugBounds(ShapeRenderer shapes) {
/* 841 */     if (!this.debug)
/* 842 */       return;  shapes.set(ShapeRenderer.ShapeType.Line);
/* 843 */     shapes.setColor(this.stage.getDebugColor());
/* 844 */     shapes.rect(this.x, this.y, this.originX, this.originY, this.width, this.height, this.scaleX, this.scaleY, this.rotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebug(boolean enabled) {
/* 849 */     this.debug = enabled;
/* 850 */     if (enabled) Stage.debug = true; 
/*     */   }
/*     */   
/*     */   public boolean getDebug() {
/* 854 */     return this.debug;
/*     */   }
/*     */ 
/*     */   
/*     */   public Actor debug() {
/* 859 */     setDebug(true);
/* 860 */     return this;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 864 */     String name = this.name;
/* 865 */     if (name == null) {
/* 866 */       name = getClass().getName();
/* 867 */       int dotIndex = name.lastIndexOf('.');
/* 868 */       if (dotIndex != -1) name = name.substring(dotIndex + 1); 
/*     */     } 
/* 870 */     return name;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\Actor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */