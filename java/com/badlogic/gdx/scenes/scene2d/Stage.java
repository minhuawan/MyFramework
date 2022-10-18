/*     */ package com.badlogic.gdx.scenes.scene2d;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.InputAdapter;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.ui.Table;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import com.badlogic.gdx.utils.Pools;
/*     */ import com.badlogic.gdx.utils.Scaling;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
/*     */ import com.badlogic.gdx.utils.viewport.ScalingViewport;
/*     */ import com.badlogic.gdx.utils.viewport.Viewport;
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
/*     */ public class Stage
/*     */   extends InputAdapter
/*     */   implements Disposable
/*     */ {
/*     */   static boolean debug;
/*     */   private Viewport viewport;
/*     */   private final Batch batch;
/*     */   private boolean ownsBatch;
/*     */   private Group root;
/*  73 */   private final Vector2 tempCoords = new Vector2();
/*  74 */   private final Actor[] pointerOverActors = new Actor[20];
/*  75 */   private final boolean[] pointerTouched = new boolean[20];
/*  76 */   private final int[] pointerScreenX = new int[20]; private int mouseScreenX; private int mouseScreenY; private Actor mouseOverActor;
/*  77 */   private final int[] pointerScreenY = new int[20];
/*     */   
/*     */   private Actor keyboardFocus;
/*     */   private Actor scrollFocus;
/*  81 */   private final SnapshotArray<TouchFocus> touchFocuses = new SnapshotArray(true, 4, TouchFocus.class); private boolean actionsRequestRendering = true; private ShapeRenderer debugShapes;
/*     */   private boolean debugInvisible;
/*     */   private boolean debugAll;
/*     */   private boolean debugUnderMouse;
/*     */   private boolean debugParentUnderMouse;
/*  86 */   private Table.Debug debugTableUnderMouse = Table.Debug.none;
/*  87 */   private final Color debugColor = new Color(0.0F, 1.0F, 0.0F, 0.85F);
/*     */ 
/*     */ 
/*     */   
/*     */   public Stage() {
/*  92 */     this((Viewport)new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), (Camera)new OrthographicCamera()), (Batch)new SpriteBatch());
/*     */     
/*  94 */     this.ownsBatch = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Stage(Viewport viewport) {
/* 100 */     this(viewport, (Batch)new SpriteBatch());
/* 101 */     this.ownsBatch = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stage(Viewport viewport, Batch batch) {
/* 108 */     if (viewport == null) throw new IllegalArgumentException("viewport cannot be null."); 
/* 109 */     if (batch == null) throw new IllegalArgumentException("batch cannot be null."); 
/* 110 */     this.viewport = viewport;
/* 111 */     this.batch = batch;
/*     */     
/* 113 */     this.root = new Group();
/* 114 */     this.root.setStage(this);
/*     */     
/* 116 */     viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
/*     */   }
/*     */   
/*     */   public void draw() {
/* 120 */     Camera camera = this.viewport.getCamera();
/* 121 */     camera.update();
/*     */     
/* 123 */     if (!this.root.isVisible())
/*     */       return; 
/* 125 */     Batch batch = this.batch;
/* 126 */     batch.setProjectionMatrix(camera.combined);
/* 127 */     batch.begin();
/* 128 */     this.root.draw(batch, 1.0F);
/* 129 */     batch.end();
/*     */     
/* 131 */     if (debug) drawDebug(); 
/*     */   }
/*     */   
/*     */   private void drawDebug() {
/* 135 */     if (this.debugShapes == null) {
/* 136 */       this.debugShapes = new ShapeRenderer();
/* 137 */       this.debugShapes.setAutoShapeType(true);
/*     */     } 
/*     */     
/* 140 */     if (this.debugUnderMouse || this.debugParentUnderMouse || this.debugTableUnderMouse != Table.Debug.none)
/* 141 */     { screenToStageCoordinates(this.tempCoords.set(Gdx.input.getX(), Gdx.input.getY()));
/* 142 */       Actor actor = hit(this.tempCoords.x, this.tempCoords.y, true);
/* 143 */       if (actor == null)
/*     */         return; 
/* 145 */       if (this.debugParentUnderMouse && actor.parent != null) actor = actor.parent;
/*     */       
/* 147 */       if (this.debugTableUnderMouse == Table.Debug.none) {
/* 148 */         actor.setDebug(true);
/*     */       } else {
/* 150 */         while (actor != null && 
/* 151 */           !(actor instanceof Table)) {
/* 152 */           actor = actor.parent;
/*     */         }
/* 154 */         if (actor == null)
/* 155 */           return;  ((Table)actor).debug(this.debugTableUnderMouse);
/*     */       } 
/*     */       
/* 158 */       if (this.debugAll && actor instanceof Group) ((Group)actor).debugAll();
/*     */       
/* 160 */       disableDebug(this.root, actor); }
/*     */     
/* 162 */     else if (this.debugAll) { this.root.debugAll(); }
/*     */ 
/*     */     
/* 165 */     Gdx.gl.glEnable(3042);
/* 166 */     this.debugShapes.setProjectionMatrix((this.viewport.getCamera()).combined);
/* 167 */     this.debugShapes.begin();
/* 168 */     this.root.drawDebug(this.debugShapes);
/* 169 */     this.debugShapes.end();
/*     */   }
/*     */ 
/*     */   
/*     */   private void disableDebug(Actor actor, Actor except) {
/* 174 */     if (actor == except)
/* 175 */       return;  actor.setDebug(false);
/* 176 */     if (actor instanceof Group) {
/* 177 */       SnapshotArray<Actor> children = ((Group)actor).children;
/* 178 */       for (int i = 0, n = children.size; i < n; i++) {
/* 179 */         disableDebug((Actor)children.get(i), except);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void act() {
/* 185 */     act(Math.min(Gdx.graphics.getDeltaTime(), 0.033333335F));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void act(float delta) {
/* 193 */     for (int pointer = 0, n = this.pointerOverActors.length; pointer < n; pointer++) {
/* 194 */       Actor overLast = this.pointerOverActors[pointer];
/*     */       
/* 196 */       if (!this.pointerTouched[pointer]) {
/* 197 */         if (overLast != null) {
/* 198 */           this.pointerOverActors[pointer] = null;
/* 199 */           screenToStageCoordinates(this.tempCoords.set(this.pointerScreenX[pointer], this.pointerScreenY[pointer]));
/*     */           
/* 201 */           InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 202 */           event.setType(InputEvent.Type.exit);
/* 203 */           event.setStage(this);
/* 204 */           event.setStageX(this.tempCoords.x);
/* 205 */           event.setStageY(this.tempCoords.y);
/* 206 */           event.setRelatedActor(overLast);
/* 207 */           event.setPointer(pointer);
/* 208 */           overLast.fire(event);
/* 209 */           Pools.free(event);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 214 */         this.pointerOverActors[pointer] = fireEnterAndExit(overLast, this.pointerScreenX[pointer], this.pointerScreenY[pointer], pointer);
/*     */       } 
/*     */     } 
/* 217 */     Application.ApplicationType type = Gdx.app.getType();
/* 218 */     if (type == Application.ApplicationType.Desktop || type == Application.ApplicationType.Applet || type == Application.ApplicationType.WebGL) {
/* 219 */       this.mouseOverActor = fireEnterAndExit(this.mouseOverActor, this.mouseScreenX, this.mouseScreenY, -1);
/*     */     }
/* 221 */     this.root.act(delta);
/*     */   }
/*     */ 
/*     */   
/*     */   private Actor fireEnterAndExit(Actor overLast, int screenX, int screenY, int pointer) {
/* 226 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/* 227 */     Actor over = hit(this.tempCoords.x, this.tempCoords.y, true);
/* 228 */     if (over == overLast) return overLast;
/*     */ 
/*     */     
/* 231 */     if (overLast != null) {
/* 232 */       InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 233 */       event.setStage(this);
/* 234 */       event.setStageX(this.tempCoords.x);
/* 235 */       event.setStageY(this.tempCoords.y);
/* 236 */       event.setPointer(pointer);
/* 237 */       event.setType(InputEvent.Type.exit);
/* 238 */       event.setRelatedActor(over);
/* 239 */       overLast.fire(event);
/* 240 */       Pools.free(event);
/*     */     } 
/*     */     
/* 243 */     if (over != null) {
/* 244 */       InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 245 */       event.setStage(this);
/* 246 */       event.setStageX(this.tempCoords.x);
/* 247 */       event.setStageY(this.tempCoords.y);
/* 248 */       event.setPointer(pointer);
/* 249 */       event.setType(InputEvent.Type.enter);
/* 250 */       event.setRelatedActor(overLast);
/* 251 */       over.fire(event);
/* 252 */       Pools.free(event);
/*     */     } 
/* 254 */     return over;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
/* 260 */     if (screenX < this.viewport.getScreenX() || screenX >= this.viewport.getScreenX() + this.viewport.getScreenWidth()) return false; 
/* 261 */     if (Gdx.graphics.getHeight() - screenY < this.viewport.getScreenY() || Gdx.graphics
/* 262 */       .getHeight() - screenY >= this.viewport.getScreenY() + this.viewport.getScreenHeight()) return false;
/*     */     
/* 264 */     this.pointerTouched[pointer] = true;
/* 265 */     this.pointerScreenX[pointer] = screenX;
/* 266 */     this.pointerScreenY[pointer] = screenY;
/*     */     
/* 268 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/*     */     
/* 270 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 271 */     event.setType(InputEvent.Type.touchDown);
/* 272 */     event.setStage(this);
/* 273 */     event.setStageX(this.tempCoords.x);
/* 274 */     event.setStageY(this.tempCoords.y);
/* 275 */     event.setPointer(pointer);
/* 276 */     event.setButton(button);
/*     */     
/* 278 */     Actor target = hit(this.tempCoords.x, this.tempCoords.y, true);
/* 279 */     if (target == null) {
/* 280 */       if (this.root.getTouchable() == Touchable.enabled) this.root.fire(event); 
/*     */     } else {
/* 282 */       target.fire(event);
/*     */     } 
/*     */     
/* 285 */     boolean handled = event.isHandled();
/* 286 */     Pools.free(event);
/* 287 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean touchDragged(int screenX, int screenY, int pointer) {
/* 293 */     this.pointerScreenX[pointer] = screenX;
/* 294 */     this.pointerScreenY[pointer] = screenY;
/* 295 */     this.mouseScreenX = screenX;
/* 296 */     this.mouseScreenY = screenY;
/*     */     
/* 298 */     if (this.touchFocuses.size == 0) return false;
/*     */     
/* 300 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/*     */     
/* 302 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 303 */     event.setType(InputEvent.Type.touchDragged);
/* 304 */     event.setStage(this);
/* 305 */     event.setStageX(this.tempCoords.x);
/* 306 */     event.setStageY(this.tempCoords.y);
/* 307 */     event.setPointer(pointer);
/*     */     
/* 309 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 310 */     TouchFocus[] focuses = (TouchFocus[])touchFocuses.begin();
/* 311 */     for (int i = 0, n = touchFocuses.size; i < n; i++) {
/* 312 */       TouchFocus focus = focuses[i];
/* 313 */       if (focus.pointer == pointer && 
/* 314 */         touchFocuses.contains(focus, true)) {
/* 315 */         event.setTarget(focus.target);
/* 316 */         event.setListenerActor(focus.listenerActor);
/* 317 */         if (focus.listener.handle(event)) event.handle(); 
/*     */       } 
/* 319 */     }  touchFocuses.end();
/*     */     
/* 321 */     boolean handled = event.isHandled();
/* 322 */     Pools.free(event);
/* 323 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
/* 329 */     this.pointerTouched[pointer] = false;
/* 330 */     this.pointerScreenX[pointer] = screenX;
/* 331 */     this.pointerScreenY[pointer] = screenY;
/*     */     
/* 333 */     if (this.touchFocuses.size == 0) return false;
/*     */     
/* 335 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/*     */     
/* 337 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 338 */     event.setType(InputEvent.Type.touchUp);
/* 339 */     event.setStage(this);
/* 340 */     event.setStageX(this.tempCoords.x);
/* 341 */     event.setStageY(this.tempCoords.y);
/* 342 */     event.setPointer(pointer);
/* 343 */     event.setButton(button);
/*     */     
/* 345 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 346 */     TouchFocus[] focuses = (TouchFocus[])touchFocuses.begin();
/* 347 */     for (int i = 0, n = touchFocuses.size; i < n; i++) {
/* 348 */       TouchFocus focus = focuses[i];
/* 349 */       if (focus.pointer == pointer && focus.button == button && 
/* 350 */         touchFocuses.removeValue(focus, true)) {
/* 351 */         event.setTarget(focus.target);
/* 352 */         event.setListenerActor(focus.listenerActor);
/* 353 */         if (focus.listener.handle(event)) event.handle(); 
/* 354 */         Pools.free(focus);
/*     */       } 
/* 356 */     }  touchFocuses.end();
/*     */     
/* 358 */     boolean handled = event.isHandled();
/* 359 */     Pools.free(event);
/* 360 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mouseMoved(int screenX, int screenY) {
/* 366 */     if (screenX < this.viewport.getScreenX() || screenX >= this.viewport.getScreenX() + this.viewport.getScreenWidth()) return false; 
/* 367 */     if (Gdx.graphics.getHeight() - screenY < this.viewport.getScreenY() || Gdx.graphics
/* 368 */       .getHeight() - screenY >= this.viewport.getScreenY() + this.viewport.getScreenHeight()) return false;
/*     */     
/* 370 */     this.mouseScreenX = screenX;
/* 371 */     this.mouseScreenY = screenY;
/*     */     
/* 373 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/*     */     
/* 375 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 376 */     event.setStage(this);
/* 377 */     event.setType(InputEvent.Type.mouseMoved);
/* 378 */     event.setStageX(this.tempCoords.x);
/* 379 */     event.setStageY(this.tempCoords.y);
/*     */     
/* 381 */     Actor target = hit(this.tempCoords.x, this.tempCoords.y, true);
/* 382 */     if (target == null) target = this.root;
/*     */     
/* 384 */     target.fire(event);
/* 385 */     boolean handled = event.isHandled();
/* 386 */     Pools.free(event);
/* 387 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean scrolled(int amount) {
/* 393 */     Actor target = (this.scrollFocus == null) ? this.root : this.scrollFocus;
/*     */     
/* 395 */     screenToStageCoordinates(this.tempCoords.set(this.mouseScreenX, this.mouseScreenY));
/*     */     
/* 397 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 398 */     event.setStage(this);
/* 399 */     event.setType(InputEvent.Type.scrolled);
/* 400 */     event.setScrollAmount(amount);
/* 401 */     event.setStageX(this.tempCoords.x);
/* 402 */     event.setStageY(this.tempCoords.y);
/* 403 */     target.fire(event);
/* 404 */     boolean handled = event.isHandled();
/* 405 */     Pools.free(event);
/* 406 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keyDown(int keyCode) {
/* 412 */     Actor target = (this.keyboardFocus == null) ? this.root : this.keyboardFocus;
/* 413 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 414 */     event.setStage(this);
/* 415 */     event.setType(InputEvent.Type.keyDown);
/* 416 */     event.setKeyCode(keyCode);
/* 417 */     target.fire(event);
/* 418 */     boolean handled = event.isHandled();
/* 419 */     Pools.free(event);
/* 420 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keyUp(int keyCode) {
/* 426 */     Actor target = (this.keyboardFocus == null) ? this.root : this.keyboardFocus;
/* 427 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 428 */     event.setStage(this);
/* 429 */     event.setType(InputEvent.Type.keyUp);
/* 430 */     event.setKeyCode(keyCode);
/* 431 */     target.fire(event);
/* 432 */     boolean handled = event.isHandled();
/* 433 */     Pools.free(event);
/* 434 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keyTyped(char character) {
/* 440 */     Actor target = (this.keyboardFocus == null) ? this.root : this.keyboardFocus;
/* 441 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 442 */     event.setStage(this);
/* 443 */     event.setType(InputEvent.Type.keyTyped);
/* 444 */     event.setCharacter(character);
/* 445 */     target.fire(event);
/* 446 */     boolean handled = event.isHandled();
/* 447 */     Pools.free(event);
/* 448 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTouchFocus(EventListener listener, Actor listenerActor, Actor target, int pointer, int button) {
/* 454 */     TouchFocus focus = (TouchFocus)Pools.obtain(TouchFocus.class);
/* 455 */     focus.listenerActor = listenerActor;
/* 456 */     focus.target = target;
/* 457 */     focus.listener = listener;
/* 458 */     focus.pointer = pointer;
/* 459 */     focus.button = button;
/* 460 */     this.touchFocuses.add(focus);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTouchFocus(EventListener listener, Actor listenerActor, Actor target, int pointer, int button) {
/* 466 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 467 */     for (int i = touchFocuses.size - 1; i >= 0; i--) {
/* 468 */       TouchFocus focus = (TouchFocus)touchFocuses.get(i);
/* 469 */       if (focus.listener == listener && focus.listenerActor == listenerActor && focus.target == target && focus.pointer == pointer && focus.button == button) {
/*     */         
/* 471 */         touchFocuses.removeIndex(i);
/* 472 */         Pools.free(focus);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelTouchFocus(Actor actor) {
/* 480 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 481 */     event.setStage(this);
/* 482 */     event.setType(InputEvent.Type.touchUp);
/* 483 */     event.setStageX(-2.14748365E9F);
/* 484 */     event.setStageY(-2.14748365E9F);
/*     */ 
/*     */ 
/*     */     
/* 488 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 489 */     TouchFocus[] items = (TouchFocus[])touchFocuses.begin();
/* 490 */     for (int i = 0, n = touchFocuses.size; i < n; i++) {
/* 491 */       TouchFocus focus = items[i];
/* 492 */       if (focus.listenerActor == actor && 
/* 493 */         touchFocuses.removeValue(focus, true)) {
/* 494 */         event.setTarget(focus.target);
/* 495 */         event.setListenerActor(focus.listenerActor);
/* 496 */         event.setPointer(focus.pointer);
/* 497 */         event.setButton(focus.button);
/* 498 */         focus.listener.handle(event);
/*     */       } 
/*     */     } 
/* 501 */     touchFocuses.end();
/*     */     
/* 503 */     Pools.free(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelTouchFocus() {
/* 511 */     cancelTouchFocusExcept(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelTouchFocusExcept(EventListener exceptListener, Actor exceptActor) {
/* 517 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 518 */     event.setStage(this);
/* 519 */     event.setType(InputEvent.Type.touchUp);
/* 520 */     event.setStageX(-2.14748365E9F);
/* 521 */     event.setStageY(-2.14748365E9F);
/*     */ 
/*     */ 
/*     */     
/* 525 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 526 */     TouchFocus[] items = (TouchFocus[])touchFocuses.begin();
/* 527 */     for (int i = 0, n = touchFocuses.size; i < n; i++) {
/* 528 */       TouchFocus focus = items[i];
/* 529 */       if ((focus.listener != exceptListener || focus.listenerActor != exceptActor) && 
/* 530 */         touchFocuses.removeValue(focus, true)) {
/* 531 */         event.setTarget(focus.target);
/* 532 */         event.setListenerActor(focus.listenerActor);
/* 533 */         event.setPointer(focus.pointer);
/* 534 */         event.setButton(focus.button);
/* 535 */         focus.listener.handle(event);
/*     */       } 
/*     */     } 
/* 538 */     touchFocuses.end();
/*     */     
/* 540 */     Pools.free(event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActor(Actor actor) {
/* 546 */     this.root.addActor(actor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAction(Action action) {
/* 552 */     this.root.addAction(action);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<Actor> getActors() {
/* 558 */     return (Array<Actor>)this.root.children;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addListener(EventListener listener) {
/* 564 */     return this.root.addListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeListener(EventListener listener) {
/* 570 */     return this.root.removeListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addCaptureListener(EventListener listener) {
/* 576 */     return this.root.addCaptureListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeCaptureListener(EventListener listener) {
/* 582 */     return this.root.removeCaptureListener(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 587 */     unfocusAll();
/* 588 */     this.root.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void unfocusAll() {
/* 593 */     setScrollFocus(null);
/* 594 */     setKeyboardFocus(null);
/* 595 */     cancelTouchFocus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void unfocus(Actor actor) {
/* 600 */     cancelTouchFocus(actor);
/* 601 */     if (this.scrollFocus != null && this.scrollFocus.isDescendantOf(actor)) setScrollFocus(null); 
/* 602 */     if (this.keyboardFocus != null && this.keyboardFocus.isDescendantOf(actor)) setKeyboardFocus(null);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setKeyboardFocus(Actor actor) {
/* 609 */     if (this.keyboardFocus == actor) return true; 
/* 610 */     FocusListener.FocusEvent event = (FocusListener.FocusEvent)Pools.obtain(FocusListener.FocusEvent.class);
/* 611 */     event.setStage(this);
/* 612 */     event.setType(FocusListener.FocusEvent.Type.keyboard);
/* 613 */     Actor oldKeyboardFocus = this.keyboardFocus;
/* 614 */     if (oldKeyboardFocus != null) {
/* 615 */       event.setFocused(false);
/* 616 */       event.setRelatedActor(actor);
/* 617 */       oldKeyboardFocus.fire((Event)event);
/*     */     } 
/* 619 */     boolean success = !event.isCancelled();
/* 620 */     if (success) {
/* 621 */       this.keyboardFocus = actor;
/* 622 */       if (actor != null) {
/* 623 */         event.setFocused(true);
/* 624 */         event.setRelatedActor(oldKeyboardFocus);
/* 625 */         actor.fire((Event)event);
/* 626 */         success = !event.isCancelled();
/* 627 */         if (!success) setKeyboardFocus(oldKeyboardFocus); 
/*     */       } 
/*     */     } 
/* 630 */     Pools.free(event);
/* 631 */     return success;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Actor getKeyboardFocus() {
/* 637 */     return this.keyboardFocus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setScrollFocus(Actor actor) {
/* 644 */     if (this.scrollFocus == actor) return true; 
/* 645 */     FocusListener.FocusEvent event = (FocusListener.FocusEvent)Pools.obtain(FocusListener.FocusEvent.class);
/* 646 */     event.setStage(this);
/* 647 */     event.setType(FocusListener.FocusEvent.Type.scroll);
/* 648 */     Actor oldScrollFocus = this.scrollFocus;
/* 649 */     if (oldScrollFocus != null) {
/* 650 */       event.setFocused(false);
/* 651 */       event.setRelatedActor(actor);
/* 652 */       oldScrollFocus.fire((Event)event);
/*     */     } 
/* 654 */     boolean success = !event.isCancelled();
/* 655 */     if (success) {
/* 656 */       this.scrollFocus = actor;
/* 657 */       if (actor != null) {
/* 658 */         event.setFocused(true);
/* 659 */         event.setRelatedActor(oldScrollFocus);
/* 660 */         actor.fire((Event)event);
/* 661 */         success = !event.isCancelled();
/* 662 */         if (!success) setScrollFocus(oldScrollFocus); 
/*     */       } 
/*     */     } 
/* 665 */     Pools.free(event);
/* 666 */     return success;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Actor getScrollFocus() {
/* 672 */     return this.scrollFocus;
/*     */   }
/*     */   
/*     */   public Batch getBatch() {
/* 676 */     return this.batch;
/*     */   }
/*     */   
/*     */   public Viewport getViewport() {
/* 680 */     return this.viewport;
/*     */   }
/*     */   
/*     */   public void setViewport(Viewport viewport) {
/* 684 */     this.viewport = viewport;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 689 */     return this.viewport.getWorldWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 694 */     return this.viewport.getWorldHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public Camera getCamera() {
/* 699 */     return this.viewport.getCamera();
/*     */   }
/*     */ 
/*     */   
/*     */   public Group getRoot() {
/* 704 */     return this.root;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoot(Group root) {
/* 710 */     this.root = root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Actor hit(float stageX, float stageY, boolean touchable) {
/* 719 */     this.root.parentToLocalCoordinates(this.tempCoords.set(stageX, stageY));
/* 720 */     return this.root.hit(this.tempCoords.x, this.tempCoords.y, touchable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 screenToStageCoordinates(Vector2 screenCoords) {
/* 726 */     this.viewport.unproject(screenCoords);
/* 727 */     return screenCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 stageToScreenCoordinates(Vector2 stageCoords) {
/* 733 */     this.viewport.project(stageCoords);
/* 734 */     stageCoords.y = this.viewport.getScreenHeight() - stageCoords.y;
/* 735 */     return stageCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 toScreenCoordinates(Vector2 coords, Matrix4 transformMatrix) {
/* 743 */     return this.viewport.toScreenCoordinates(coords, transformMatrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void calculateScissors(Rectangle localRect, Rectangle scissorRect) {
/*     */     Matrix4 transformMatrix;
/* 749 */     this.viewport.calculateScissors(this.batch.getTransformMatrix(), localRect, scissorRect);
/*     */     
/* 751 */     if (this.debugShapes != null && this.debugShapes.isDrawing()) {
/* 752 */       transformMatrix = this.debugShapes.getTransformMatrix();
/*     */     } else {
/* 754 */       transformMatrix = this.batch.getTransformMatrix();
/* 755 */     }  this.viewport.calculateScissors(transformMatrix, localRect, scissorRect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActionsRequestRendering(boolean actionsRequestRendering) {
/* 762 */     this.actionsRequestRendering = actionsRequestRendering;
/*     */   }
/*     */   
/*     */   public boolean getActionsRequestRendering() {
/* 766 */     return this.actionsRequestRendering;
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getDebugColor() {
/* 771 */     return this.debugColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugInvisible(boolean debugInvisible) {
/* 776 */     this.debugInvisible = debugInvisible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugAll(boolean debugAll) {
/* 781 */     if (this.debugAll == debugAll)
/* 782 */       return;  this.debugAll = debugAll;
/* 783 */     if (debugAll) {
/* 784 */       debug = true;
/*     */     } else {
/* 786 */       this.root.setDebug(false, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDebugUnderMouse(boolean debugUnderMouse) {
/* 791 */     if (this.debugUnderMouse == debugUnderMouse)
/* 792 */       return;  this.debugUnderMouse = debugUnderMouse;
/* 793 */     if (debugUnderMouse) {
/* 794 */       debug = true;
/*     */     } else {
/* 796 */       this.root.setDebug(false, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugParentUnderMouse(boolean debugParentUnderMouse) {
/* 802 */     if (this.debugParentUnderMouse == debugParentUnderMouse)
/* 803 */       return;  this.debugParentUnderMouse = debugParentUnderMouse;
/* 804 */     if (debugParentUnderMouse) {
/* 805 */       debug = true;
/*     */     } else {
/* 807 */       this.root.setDebug(false, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebugTableUnderMouse(Table.Debug debugTableUnderMouse) {
/* 814 */     if (debugTableUnderMouse == null) debugTableUnderMouse = Table.Debug.none; 
/* 815 */     if (this.debugTableUnderMouse == debugTableUnderMouse)
/* 816 */       return;  this.debugTableUnderMouse = debugTableUnderMouse;
/* 817 */     if (debugTableUnderMouse != Table.Debug.none) {
/* 818 */       debug = true;
/*     */     } else {
/* 820 */       this.root.setDebug(false, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugTableUnderMouse(boolean debugTableUnderMouse) {
/* 826 */     setDebugTableUnderMouse(debugTableUnderMouse ? Table.Debug.all : Table.Debug.none);
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 830 */     clear();
/* 831 */     if (this.ownsBatch) this.batch.dispose(); 
/*     */   }
/*     */   
/*     */   public static final class TouchFocus implements Pool.Poolable {
/*     */     EventListener listener;
/*     */     Actor listenerActor;
/*     */     Actor target;
/*     */     int pointer;
/*     */     int button;
/*     */     
/*     */     public void reset() {
/* 842 */       this.listenerActor = null;
/* 843 */       this.listener = null;
/* 844 */       this.target = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\Stage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */