/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
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
/*     */ public class Tooltip<T extends Actor>
/*     */   extends InputListener
/*     */ {
/*  30 */   static Vector2 tmp = new Vector2();
/*     */   
/*     */   private final TooltipManager manager;
/*     */   final Container<T> container;
/*     */   boolean instant;
/*     */   boolean always;
/*     */   Actor targetActor;
/*     */   
/*     */   public Tooltip(T contents) {
/*  39 */     this(contents, TooltipManager.getInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public Tooltip(T contents, TooltipManager manager) {
/*  44 */     this.manager = manager;
/*     */     
/*  46 */     this.container = new Container<T>((Actor)contents) {
/*     */         public void act(float delta) {
/*  48 */           super.act(delta);
/*  49 */           if (Tooltip.this.targetActor != null && Tooltip.this.targetActor.getStage() == null) remove(); 
/*     */         }
/*     */       };
/*  52 */     this.container.setTouchable(Touchable.disabled);
/*     */   }
/*     */   
/*     */   public TooltipManager getManager() {
/*  56 */     return this.manager;
/*     */   }
/*     */   
/*     */   public Container<T> getContainer() {
/*  60 */     return this.container;
/*     */   }
/*     */   
/*     */   public void setActor(T contents) {
/*  64 */     this.container.setActor(contents);
/*     */   }
/*     */   
/*     */   public T getActor() {
/*  68 */     return this.container.getActor();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInstant(boolean instant) {
/*  73 */     this.instant = instant;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAlways(boolean always) {
/*  78 */     this.always = always;
/*     */   }
/*     */   
/*     */   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  82 */     if (this.instant) {
/*  83 */       this.container.toFront();
/*  84 */       return false;
/*     */     } 
/*  86 */     this.manager.touchDown(this);
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseMoved(InputEvent event, float x, float y) {
/*  91 */     if (this.container.hasParent()) return false; 
/*  92 */     setContainerPosition(event.getListenerActor(), x, y);
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   private void setContainerPosition(Actor actor, float x, float y) {
/*  97 */     this.targetActor = actor;
/*  98 */     Stage stage = actor.getStage();
/*  99 */     if (stage == null)
/*     */       return; 
/* 101 */     this.container.pack();
/* 102 */     float offsetX = this.manager.offsetX, offsetY = this.manager.offsetY, dist = this.manager.edgeDistance;
/* 103 */     Vector2 point = actor.localToStageCoordinates(tmp.set(x + offsetX, y - offsetY - this.container.getHeight()));
/* 104 */     if (point.y < dist) point = actor.localToStageCoordinates(tmp.set(x + offsetX, y + offsetY)); 
/* 105 */     if (point.x < dist) point.x = dist; 
/* 106 */     if (point.x + this.container.getWidth() > stage.getWidth() - dist) point.x = stage.getWidth() - dist - this.container.getWidth(); 
/* 107 */     if (point.y + this.container.getHeight() > stage.getHeight() - dist) point.y = stage.getHeight() - dist - this.container.getHeight(); 
/* 108 */     this.container.setPosition(point.x, point.y);
/*     */     
/* 110 */     point = actor.localToStageCoordinates(tmp.set(actor.getWidth() / 2.0F, actor.getHeight() / 2.0F));
/* 111 */     point.sub(this.container.getX(), this.container.getY());
/* 112 */     this.container.setOrigin(point.x, point.y);
/*     */   }
/*     */   
/*     */   public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
/* 116 */     if (pointer != -1)
/* 117 */       return;  if (Gdx.input.isTouched())
/* 118 */       return;  Actor actor = event.getListenerActor();
/* 119 */     if (fromActor != null && fromActor.isDescendantOf(actor))
/* 120 */       return;  setContainerPosition(actor, x, y);
/* 121 */     this.manager.enter(this);
/*     */   }
/*     */   
/*     */   public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
/* 125 */     if (toActor != null && toActor.isDescendantOf(event.getListenerActor()))
/* 126 */       return;  hide();
/*     */   }
/*     */   
/*     */   public void hide() {
/* 130 */     this.manager.hide(this);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Tooltip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */