/*     */ package com.badlogic.gdx.scenes.scene2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.math.Affine2;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class Group
/*     */   extends Actor
/*     */   implements Cullable
/*     */ {
/*  36 */   private static final Vector2 tmp = new Vector2();
/*     */   
/*  38 */   final SnapshotArray<Actor> children = new SnapshotArray(true, 4, Actor.class);
/*  39 */   private final Affine2 worldTransform = new Affine2();
/*  40 */   private final Matrix4 computedTransform = new Matrix4();
/*  41 */   private final Matrix4 oldTransform = new Matrix4();
/*     */   boolean transform = true;
/*     */   private Rectangle cullingArea;
/*     */   
/*     */   public void act(float delta) {
/*  46 */     super.act(delta);
/*  47 */     Actor[] actors = (Actor[])this.children.begin();
/*  48 */     for (int i = 0, n = this.children.size; i < n; i++)
/*  49 */       actors[i].act(delta); 
/*  50 */     this.children.end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*  56 */     if (this.transform) applyTransform(batch, computeTransform()); 
/*  57 */     drawChildren(batch, parentAlpha);
/*  58 */     if (this.transform) resetTransform(batch);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawChildren(Batch batch, float parentAlpha) {
/*  66 */     parentAlpha *= this.color.a;
/*  67 */     SnapshotArray<Actor> children = this.children;
/*  68 */     Actor[] actors = (Actor[])children.begin();
/*  69 */     Rectangle cullingArea = this.cullingArea;
/*  70 */     if (cullingArea != null) {
/*     */       
/*  72 */       float cullLeft = cullingArea.x;
/*  73 */       float cullRight = cullLeft + cullingArea.width;
/*  74 */       float cullBottom = cullingArea.y;
/*  75 */       float cullTop = cullBottom + cullingArea.height;
/*  76 */       if (this.transform) {
/*  77 */         for (int i = 0, n = children.size; i < n; i++) {
/*  78 */           Actor child = actors[i];
/*  79 */           if (child.isVisible()) {
/*  80 */             float cx = child.x, cy = child.y;
/*  81 */             if (cx <= cullRight && cy <= cullTop && cx + child.width >= cullLeft && cy + child.height >= cullBottom)
/*  82 */               child.draw(batch, parentAlpha); 
/*     */           } 
/*     */         } 
/*     */       } else {
/*  86 */         float offsetX = this.x, offsetY = this.y;
/*  87 */         this.x = 0.0F;
/*  88 */         this.y = 0.0F;
/*  89 */         for (int i = 0, n = children.size; i < n; i++) {
/*  90 */           Actor child = actors[i];
/*  91 */           if (child.isVisible()) {
/*  92 */             float cx = child.x, cy = child.y;
/*  93 */             if (cx <= cullRight && cy <= cullTop && cx + child.width >= cullLeft && cy + child.height >= cullBottom) {
/*  94 */               child.x = cx + offsetX;
/*  95 */               child.y = cy + offsetY;
/*  96 */               child.draw(batch, parentAlpha);
/*  97 */               child.x = cx;
/*  98 */               child.y = cy;
/*     */             } 
/*     */           } 
/* 101 */         }  this.x = offsetX;
/* 102 */         this.y = offsetY;
/*     */       }
/*     */     
/*     */     }
/* 106 */     else if (this.transform) {
/* 107 */       for (int i = 0, n = children.size; i < n; i++) {
/* 108 */         Actor child = actors[i];
/* 109 */         if (child.isVisible()) {
/* 110 */           child.draw(batch, parentAlpha);
/*     */         }
/*     */       } 
/*     */     } else {
/* 114 */       float offsetX = this.x, offsetY = this.y;
/* 115 */       this.x = 0.0F;
/* 116 */       this.y = 0.0F;
/* 117 */       for (int i = 0, n = children.size; i < n; i++) {
/* 118 */         Actor child = actors[i];
/* 119 */         if (child.isVisible()) {
/* 120 */           float cx = child.x, cy = child.y;
/* 121 */           child.x = cx + offsetX;
/* 122 */           child.y = cy + offsetY;
/* 123 */           child.draw(batch, parentAlpha);
/* 124 */           child.x = cx;
/* 125 */           child.y = cy;
/*     */         } 
/* 127 */       }  this.x = offsetX;
/* 128 */       this.y = offsetY;
/*     */     } 
/*     */     
/* 131 */     children.end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawDebug(ShapeRenderer shapes) {
/* 137 */     drawDebugBounds(shapes);
/* 138 */     if (this.transform) applyTransform(shapes, computeTransform()); 
/* 139 */     drawDebugChildren(shapes);
/* 140 */     if (this.transform) resetTransform(shapes);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawDebugChildren(ShapeRenderer shapes) {
/* 148 */     SnapshotArray<Actor> children = this.children;
/* 149 */     Actor[] actors = (Actor[])children.begin();
/*     */     
/* 151 */     if (this.transform) {
/* 152 */       for (int i = 0, n = children.size; i < n; i++) {
/* 153 */         Actor child = actors[i];
/* 154 */         if (child.isVisible() && (
/* 155 */           child.getDebug() || child instanceof Group))
/* 156 */           child.drawDebug(shapes); 
/*     */       } 
/* 158 */       shapes.flush();
/*     */     } else {
/*     */       
/* 161 */       float offsetX = this.x, offsetY = this.y;
/* 162 */       this.x = 0.0F;
/* 163 */       this.y = 0.0F;
/* 164 */       for (int i = 0, n = children.size; i < n; i++) {
/* 165 */         Actor child = actors[i];
/* 166 */         if (child.isVisible() && (
/* 167 */           child.getDebug() || child instanceof Group)) {
/* 168 */           float cx = child.x, cy = child.y;
/* 169 */           child.x = cx + offsetX;
/* 170 */           child.y = cy + offsetY;
/* 171 */           child.drawDebug(shapes);
/* 172 */           child.x = cx;
/* 173 */           child.y = cy;
/*     */         } 
/* 175 */       }  this.x = offsetX;
/* 176 */       this.y = offsetY;
/*     */     } 
/* 178 */     children.end();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Matrix4 computeTransform() {
/* 183 */     Affine2 worldTransform = this.worldTransform;
/* 184 */     float originX = this.originX, originY = this.originY;
/* 185 */     worldTransform.setToTrnRotScl(this.x + originX, this.y + originY, this.rotation, this.scaleX, this.scaleY);
/* 186 */     if (originX != 0.0F || originY != 0.0F) worldTransform.translate(-originX, -originY);
/*     */ 
/*     */     
/* 189 */     Group parentGroup = this.parent;
/* 190 */     while (parentGroup != null && 
/* 191 */       !parentGroup.transform) {
/* 192 */       parentGroup = parentGroup.parent;
/*     */     }
/* 194 */     if (parentGroup != null) worldTransform.preMul(parentGroup.worldTransform);
/*     */     
/* 196 */     this.computedTransform.set(worldTransform);
/* 197 */     return this.computedTransform;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyTransform(Batch batch, Matrix4 transform) {
/* 203 */     this.oldTransform.set(batch.getTransformMatrix());
/* 204 */     batch.setTransformMatrix(transform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetTransform(Batch batch) {
/* 210 */     batch.setTransformMatrix(this.oldTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyTransform(ShapeRenderer shapes, Matrix4 transform) {
/* 217 */     this.oldTransform.set(shapes.getTransformMatrix());
/* 218 */     shapes.setTransformMatrix(transform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetTransform(ShapeRenderer shapes) {
/* 224 */     shapes.setTransformMatrix(this.oldTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCullingArea(Rectangle cullingArea) {
/* 231 */     this.cullingArea = cullingArea;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getCullingArea() {
/* 237 */     return this.cullingArea;
/*     */   }
/*     */   
/*     */   public Actor hit(float x, float y, boolean touchable) {
/* 241 */     if (touchable && getTouchable() == Touchable.disabled) return null; 
/* 242 */     Vector2 point = tmp;
/* 243 */     Actor[] childrenArray = (Actor[])this.children.items;
/* 244 */     for (int i = this.children.size - 1; i >= 0; i--) {
/* 245 */       Actor child = childrenArray[i];
/* 246 */       if (child.isVisible()) {
/* 247 */         child.parentToLocalCoordinates(point.set(x, y));
/* 248 */         Actor hit = child.hit(point.x, point.y, touchable);
/* 249 */         if (hit != null) return hit; 
/*     */       } 
/* 251 */     }  return super.hit(x, y, touchable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void childrenChanged() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActor(Actor actor) {
/* 261 */     if (actor.parent != null) {
/* 262 */       if (actor.parent == this)
/* 263 */         return;  actor.parent.removeActor(actor, false);
/*     */     } 
/* 265 */     this.children.add(actor);
/* 266 */     actor.setParent(this);
/* 267 */     actor.setStage(getStage());
/* 268 */     childrenChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorAt(int index, Actor actor) {
/* 275 */     if (actor.parent != null) {
/* 276 */       if (actor.parent == this)
/* 277 */         return;  actor.parent.removeActor(actor, false);
/*     */     } 
/* 279 */     if (index >= this.children.size) {
/* 280 */       this.children.add(actor);
/*     */     } else {
/* 282 */       this.children.insert(index, actor);
/* 283 */     }  actor.setParent(this);
/* 284 */     actor.setStage(getStage());
/* 285 */     childrenChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorBefore(Actor actorBefore, Actor actor) {
/* 291 */     if (actor.parent != null) {
/* 292 */       if (actor.parent == this)
/* 293 */         return;  actor.parent.removeActor(actor, false);
/*     */     } 
/* 295 */     int index = this.children.indexOf(actorBefore, true);
/* 296 */     this.children.insert(index, actor);
/* 297 */     actor.setParent(this);
/* 298 */     actor.setStage(getStage());
/* 299 */     childrenChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorAfter(Actor actorAfter, Actor actor) {
/* 305 */     if (actor.parent != null) {
/* 306 */       if (actor.parent == this)
/* 307 */         return;  actor.parent.removeActor(actor, false);
/*     */     } 
/* 309 */     int index = this.children.indexOf(actorAfter, true);
/* 310 */     if (index == this.children.size) {
/* 311 */       this.children.add(actor);
/*     */     } else {
/* 313 */       this.children.insert(index + 1, actor);
/* 314 */     }  actor.setParent(this);
/* 315 */     actor.setStage(getStage());
/* 316 */     childrenChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeActor(Actor actor) {
/* 321 */     return removeActor(actor, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeActor(Actor actor, boolean unfocus) {
/* 330 */     if (!this.children.removeValue(actor, true)) return false; 
/* 331 */     if (unfocus) {
/* 332 */       Stage stage = getStage();
/* 333 */       if (stage != null) stage.unfocus(actor); 
/*     */     } 
/* 335 */     actor.setParent(null);
/* 336 */     actor.setStage(null);
/* 337 */     childrenChanged();
/* 338 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearChildren() {
/* 343 */     Actor[] actors = (Actor[])this.children.begin();
/* 344 */     for (int i = 0, n = this.children.size; i < n; i++) {
/* 345 */       Actor child = actors[i];
/* 346 */       child.setStage(null);
/* 347 */       child.setParent(null);
/*     */     } 
/* 349 */     this.children.end();
/* 350 */     this.children.clear();
/* 351 */     childrenChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 356 */     super.clear();
/* 357 */     clearChildren();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Actor> T findActor(String name) {
/* 363 */     SnapshotArray<Actor> snapshotArray = this.children; int i, n;
/* 364 */     for (i = 0, n = ((Array)snapshotArray).size; i < n; i++) {
/* 365 */       if (name.equals(((Actor)snapshotArray.get(i)).getName())) return (T)snapshotArray.get(i); 
/* 366 */     }  for (i = 0, n = ((Array)snapshotArray).size; i < n; i++) {
/* 367 */       Actor child = (Actor)snapshotArray.get(i);
/* 368 */       if (child instanceof Group) {
/* 369 */         Actor actor = ((Group)child).findActor(name);
/* 370 */         if (actor != null) return (T)actor; 
/*     */       } 
/*     */     } 
/* 373 */     return null;
/*     */   }
/*     */   
/*     */   protected void setStage(Stage stage) {
/* 377 */     super.setStage(stage);
/* 378 */     Actor[] childrenArray = (Actor[])this.children.items;
/* 379 */     for (int i = 0, n = this.children.size; i < n; i++) {
/* 380 */       childrenArray[i].setStage(stage);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean swapActor(int first, int second) {
/* 385 */     int maxIndex = this.children.size;
/* 386 */     if (first < 0 || first >= maxIndex) return false; 
/* 387 */     if (second < 0 || second >= maxIndex) return false; 
/* 388 */     this.children.swap(first, second);
/* 389 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean swapActor(Actor first, Actor second) {
/* 394 */     int firstIndex = this.children.indexOf(first, true);
/* 395 */     int secondIndex = this.children.indexOf(second, true);
/* 396 */     if (firstIndex == -1 || secondIndex == -1) return false; 
/* 397 */     this.children.swap(firstIndex, secondIndex);
/* 398 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public SnapshotArray<Actor> getChildren() {
/* 403 */     return this.children;
/*     */   }
/*     */   
/*     */   public boolean hasChildren() {
/* 407 */     return (this.children.size > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransform(boolean transform) {
/* 416 */     this.transform = transform;
/*     */   }
/*     */   
/*     */   public boolean isTransform() {
/* 420 */     return this.transform;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 localToDescendantCoordinates(Actor descendant, Vector2 localCoords) {
/* 426 */     Group parent = descendant.parent;
/* 427 */     if (parent == null) throw new IllegalArgumentException("Child is not a descendant: " + descendant);
/*     */     
/* 429 */     if (parent != this) localToDescendantCoordinates(parent, localCoords);
/*     */     
/* 431 */     descendant.parentToLocalCoordinates(localCoords);
/* 432 */     return localCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebug(boolean enabled, boolean recursively) {
/* 437 */     setDebug(enabled);
/* 438 */     if (recursively) {
/* 439 */       for (Actor child : this.children) {
/* 440 */         if (child instanceof Group) {
/* 441 */           ((Group)child).setDebug(enabled, recursively); continue;
/*     */         } 
/* 443 */         child.setDebug(enabled);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Group debugAll() {
/* 451 */     setDebug(true, true);
/* 452 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 457 */     StringBuilder buffer = new StringBuilder(128);
/* 458 */     toString(buffer, 1);
/* 459 */     buffer.setLength(buffer.length() - 1);
/* 460 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   void toString(StringBuilder buffer, int indent) {
/* 464 */     buffer.append(super.toString());
/* 465 */     buffer.append('\n');
/*     */     
/* 467 */     Actor[] actors = (Actor[])this.children.begin();
/* 468 */     for (int i = 0, n = this.children.size; i < n; i++) {
/* 469 */       for (int ii = 0; ii < indent; ii++)
/* 470 */         buffer.append("|  "); 
/* 471 */       Actor actor = actors[i];
/* 472 */       if (actor instanceof Group) {
/* 473 */         ((Group)actor).toString(buffer, indent + 1);
/*     */       } else {
/* 475 */         buffer.append(actor);
/* 476 */         buffer.append('\n');
/*     */       } 
/*     */     } 
/* 479 */     this.children.end();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\Group.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */