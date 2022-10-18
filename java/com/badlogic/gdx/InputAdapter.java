/*    */ package com.badlogic.gdx;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InputAdapter
/*    */   implements InputProcessor
/*    */ {
/*    */   public boolean keyDown(int keycode) {
/* 24 */     return false;
/*    */   }
/*    */   
/*    */   public boolean keyUp(int keycode) {
/* 28 */     return false;
/*    */   }
/*    */   
/*    */   public boolean keyTyped(char character) {
/* 32 */     return false;
/*    */   }
/*    */   
/*    */   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   public boolean touchDragged(int screenX, int screenY, int pointer) {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mouseMoved(int screenX, int screenY) {
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean scrolled(int amount) {
/* 54 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\InputAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */