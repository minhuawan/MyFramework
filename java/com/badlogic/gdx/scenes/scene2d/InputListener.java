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
/*     */ public class InputListener
/*     */   implements EventListener
/*     */ {
/*  37 */   private static final Vector2 tmpCoords = new Vector2();
/*     */   
/*     */   public boolean handle(Event e) {
/*  40 */     if (!(e instanceof InputEvent)) return false; 
/*  41 */     InputEvent event = (InputEvent)e;
/*     */     
/*  43 */     switch (event.getType()) {
/*     */       case keyDown:
/*  45 */         return keyDown(event, event.getKeyCode());
/*     */       case keyUp:
/*  47 */         return keyUp(event, event.getKeyCode());
/*     */       case keyTyped:
/*  49 */         return keyTyped(event, event.getCharacter());
/*     */     } 
/*     */     
/*  52 */     event.toCoordinates(event.getListenerActor(), tmpCoords);
/*     */     
/*  54 */     switch (event.getType()) {
/*     */       case touchDown:
/*  56 */         return touchDown(event, tmpCoords.x, tmpCoords.y, event.getPointer(), event.getButton());
/*     */       case touchUp:
/*  58 */         touchUp(event, tmpCoords.x, tmpCoords.y, event.getPointer(), event.getButton());
/*  59 */         return true;
/*     */       case touchDragged:
/*  61 */         touchDragged(event, tmpCoords.x, tmpCoords.y, event.getPointer());
/*  62 */         return true;
/*     */       case mouseMoved:
/*  64 */         return mouseMoved(event, tmpCoords.x, tmpCoords.y);
/*     */       case scrolled:
/*  66 */         return scrolled(event, tmpCoords.x, tmpCoords.y, event.getScrollAmount());
/*     */       case enter:
/*  68 */         enter(event, tmpCoords.x, tmpCoords.y, event.getPointer(), event.getRelatedActor());
/*  69 */         return false;
/*     */       case exit:
/*  71 */         exit(event, tmpCoords.x, tmpCoords.y, event.getPointer(), event.getRelatedActor());
/*  72 */         return false;
/*     */     } 
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void touchDragged(InputEvent event, float x, float y, int pointer) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mouseMoved(InputEvent event, float x, float y) {
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean scrolled(InputEvent event, float x, float y, int amount) {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyDown(InputEvent event, int keycode) {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyUp(InputEvent event, int keycode) {
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyTyped(InputEvent event, char character) {
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\InputListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */