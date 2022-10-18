/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.input.GestureDetector;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Event;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
/*     */ public class ActorGestureListener
/*     */   implements EventListener
/*     */ {
/*  32 */   static final Vector2 tmpCoords = new Vector2(); static final Vector2 tmpCoords2 = new Vector2();
/*     */   
/*     */   private final GestureDetector detector;
/*     */   InputEvent event;
/*     */   Actor actor;
/*     */   Actor touchDownTarget;
/*     */   
/*     */   public ActorGestureListener() {
/*  40 */     this(20.0F, 0.4F, 1.1F, 0.15F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ActorGestureListener(float halfTapSquareSize, float tapCountInterval, float longPressDuration, float maxFlingDelay) {
/*  45 */     this.detector = new GestureDetector(halfTapSquareSize, tapCountInterval, longPressDuration, maxFlingDelay, (GestureDetector.GestureListener)new GestureDetector.GestureAdapter() {
/*  46 */           private final Vector2 initialPointer1 = new Vector2(), initialPointer2 = new Vector2();
/*  47 */           private final Vector2 pointer1 = new Vector2(); private final Vector2 pointer2 = new Vector2();
/*     */           
/*     */           public boolean tap(float stageX, float stageY, int count, int button) {
/*  50 */             ActorGestureListener.this.actor.stageToLocalCoordinates(ActorGestureListener.tmpCoords.set(stageX, stageY));
/*  51 */             ActorGestureListener.this.tap(ActorGestureListener.this.event, ActorGestureListener.tmpCoords.x, ActorGestureListener.tmpCoords.y, count, button);
/*  52 */             return true;
/*     */           }
/*     */           
/*     */           public boolean longPress(float stageX, float stageY) {
/*  56 */             ActorGestureListener.this.actor.stageToLocalCoordinates(ActorGestureListener.tmpCoords.set(stageX, stageY));
/*  57 */             return ActorGestureListener.this.longPress(ActorGestureListener.this.actor, ActorGestureListener.tmpCoords.x, ActorGestureListener.tmpCoords.y);
/*     */           }
/*     */           
/*     */           public boolean fling(float velocityX, float velocityY, int button) {
/*  61 */             stageToLocalAmount(ActorGestureListener.tmpCoords.set(velocityX, velocityY));
/*  62 */             ActorGestureListener.this.fling(ActorGestureListener.this.event, ActorGestureListener.tmpCoords.x, ActorGestureListener.tmpCoords.y, button);
/*  63 */             return true;
/*     */           }
/*     */           
/*     */           public boolean pan(float stageX, float stageY, float deltaX, float deltaY) {
/*  67 */             stageToLocalAmount(ActorGestureListener.tmpCoords.set(deltaX, deltaY));
/*  68 */             deltaX = ActorGestureListener.tmpCoords.x;
/*  69 */             deltaY = ActorGestureListener.tmpCoords.y;
/*  70 */             ActorGestureListener.this.actor.stageToLocalCoordinates(ActorGestureListener.tmpCoords.set(stageX, stageY));
/*  71 */             ActorGestureListener.this.pan(ActorGestureListener.this.event, ActorGestureListener.tmpCoords.x, ActorGestureListener.tmpCoords.y, deltaX, deltaY);
/*  72 */             return true;
/*     */           }
/*     */           
/*     */           public boolean zoom(float initialDistance, float distance) {
/*  76 */             ActorGestureListener.this.zoom(ActorGestureListener.this.event, initialDistance, distance);
/*  77 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean pinch(Vector2 stageInitialPointer1, Vector2 stageInitialPointer2, Vector2 stagePointer1, Vector2 stagePointer2) {
/*  82 */             ActorGestureListener.this.actor.stageToLocalCoordinates(this.initialPointer1.set(stageInitialPointer1));
/*  83 */             ActorGestureListener.this.actor.stageToLocalCoordinates(this.initialPointer2.set(stageInitialPointer2));
/*  84 */             ActorGestureListener.this.actor.stageToLocalCoordinates(this.pointer1.set(stagePointer1));
/*  85 */             ActorGestureListener.this.actor.stageToLocalCoordinates(this.pointer2.set(stagePointer2));
/*  86 */             ActorGestureListener.this.pinch(ActorGestureListener.this.event, this.initialPointer1, this.initialPointer2, this.pointer1, this.pointer2);
/*  87 */             return true;
/*     */           }
/*     */           
/*     */           private void stageToLocalAmount(Vector2 amount) {
/*  91 */             ActorGestureListener.this.actor.stageToLocalCoordinates(amount);
/*  92 */             amount.sub(ActorGestureListener.this.actor.stageToLocalCoordinates(ActorGestureListener.tmpCoords2.set(0.0F, 0.0F)));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean handle(Event e) {
/*  98 */     if (!(e instanceof InputEvent)) return false; 
/*  99 */     InputEvent event = (InputEvent)e;
/*     */     
/* 101 */     switch (event.getType()) {
/*     */       case touchDown:
/* 103 */         this.actor = event.getListenerActor();
/* 104 */         this.touchDownTarget = event.getTarget();
/* 105 */         this.detector.touchDown(event.getStageX(), event.getStageY(), event.getPointer(), event.getButton());
/* 106 */         this.actor.stageToLocalCoordinates(tmpCoords.set(event.getStageX(), event.getStageY()));
/* 107 */         touchDown(event, tmpCoords.x, tmpCoords.y, event.getPointer(), event.getButton());
/* 108 */         return true;
/*     */       case touchUp:
/* 110 */         if (event.isTouchFocusCancel()) return false; 
/* 111 */         this.event = event;
/* 112 */         this.actor = event.getListenerActor();
/* 113 */         this.detector.touchUp(event.getStageX(), event.getStageY(), event.getPointer(), event.getButton());
/* 114 */         this.actor.stageToLocalCoordinates(tmpCoords.set(event.getStageX(), event.getStageY()));
/* 115 */         touchUp(event, tmpCoords.x, tmpCoords.y, event.getPointer(), event.getButton());
/* 116 */         return true;
/*     */       case touchDragged:
/* 118 */         this.event = event;
/* 119 */         this.actor = event.getListenerActor();
/* 120 */         this.detector.touchDragged(event.getStageX(), event.getStageY(), event.getPointer());
/* 121 */         return true;
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void touchDown(InputEvent event, float x, float y, int pointer, int button) {}
/*     */ 
/*     */   
/*     */   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {}
/*     */ 
/*     */   
/*     */   public void tap(InputEvent event, float x, float y, int count, int button) {}
/*     */ 
/*     */   
/*     */   public boolean longPress(Actor actor, float x, float y) {
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fling(InputEvent event, float velocityX, float velocityY, int button) {}
/*     */ 
/*     */   
/*     */   public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {}
/*     */ 
/*     */   
/*     */   public void zoom(InputEvent event, float initialDistance, float distance) {}
/*     */ 
/*     */   
/*     */   public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {}
/*     */ 
/*     */   
/*     */   public GestureDetector getGestureDetector() {
/* 155 */     return this.detector;
/*     */   }
/*     */   
/*     */   public Actor getTouchDownTarget() {
/* 159 */     return this.touchDownTarget;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\ActorGestureListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */