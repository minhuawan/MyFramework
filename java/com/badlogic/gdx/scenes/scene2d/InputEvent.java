/*     */ package com.badlogic.gdx.scenes.scene2d;
/*     */ 
/*     */ import com.badlogic.gdx.math.Vector2;
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
/*     */ public class InputEvent
/*     */   extends Event
/*     */ {
/*     */   private Type type;
/*     */   private float stageX;
/*     */   private float stageY;
/*     */   private int pointer;
/*     */   private int button;
/*     */   private int keyCode;
/*     */   private int scrollAmount;
/*     */   private char character;
/*     */   private Actor relatedActor;
/*     */   
/*     */   public void reset() {
/*  32 */     super.reset();
/*  33 */     this.relatedActor = null;
/*  34 */     this.button = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStageX() {
/*  39 */     return this.stageX;
/*     */   }
/*     */   
/*     */   public void setStageX(float stageX) {
/*  43 */     this.stageX = stageX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStageY() {
/*  48 */     return this.stageY;
/*     */   }
/*     */   
/*     */   public void setStageY(float stageY) {
/*  52 */     this.stageY = stageY;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  57 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(Type type) {
/*  61 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPointer() {
/*  67 */     return this.pointer;
/*     */   }
/*     */   
/*     */   public void setPointer(int pointer) {
/*  71 */     this.pointer = pointer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getButton() {
/*  77 */     return this.button;
/*     */   }
/*     */   
/*     */   public void setButton(int button) {
/*  81 */     this.button = button;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getKeyCode() {
/*  86 */     return this.keyCode;
/*     */   }
/*     */   
/*     */   public void setKeyCode(int keyCode) {
/*  90 */     this.keyCode = keyCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public char getCharacter() {
/*  95 */     return this.character;
/*     */   }
/*     */   
/*     */   public void setCharacter(char character) {
/*  99 */     this.character = character;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScrollAmount() {
/* 104 */     return this.scrollAmount;
/*     */   }
/*     */   
/*     */   public void setScrollAmount(int scrollAmount) {
/* 108 */     this.scrollAmount = scrollAmount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Actor getRelatedActor() {
/* 114 */     return this.relatedActor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRelatedActor(Actor relatedActor) {
/* 119 */     this.relatedActor = relatedActor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 toCoordinates(Actor actor, Vector2 actorCoords) {
/* 125 */     actorCoords.set(this.stageX, this.stageY);
/* 126 */     actor.stageToLocalCoordinates(actorCoords);
/* 127 */     return actorCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouchFocusCancel() {
/* 132 */     return (this.stageX == -2.14748365E9F || this.stageY == -2.14748365E9F);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 136 */     return this.type.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/* 142 */     touchDown,
/*     */     
/* 144 */     touchUp,
/*     */     
/* 146 */     touchDragged,
/*     */     
/* 148 */     mouseMoved,
/*     */     
/* 150 */     enter,
/*     */     
/* 152 */     exit,
/*     */     
/* 154 */     scrolled,
/*     */     
/* 156 */     keyDown,
/*     */     
/* 158 */     keyUp,
/*     */     
/* 160 */     keyTyped;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\InputEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */