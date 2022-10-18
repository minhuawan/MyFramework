/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
/*    */ import com.badlogic.gdx.scenes.scene2d.Touchable;
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
/*    */ public class TouchableAction
/*    */   extends Action
/*    */ {
/*    */   private Touchable touchable;
/*    */   
/*    */   public boolean act(float delta) {
/* 29 */     this.target.setTouchable(this.touchable);
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   public Touchable getTouchable() {
/* 34 */     return this.touchable;
/*    */   }
/*    */   
/*    */   public void setTouchable(Touchable touchable) {
/* 38 */     this.touchable = touchable;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\TouchableAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */