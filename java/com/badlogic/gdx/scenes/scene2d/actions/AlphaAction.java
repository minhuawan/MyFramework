/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
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
/*    */ public class AlphaAction
/*    */   extends TemporalAction
/*    */ {
/*    */   private float start;
/*    */   private float end;
/*    */   private Color color;
/*    */   
/*    */   protected void begin() {
/* 30 */     if (this.color == null) this.color = this.target.getColor(); 
/* 31 */     this.start = this.color.a;
/*    */   }
/*    */   
/*    */   protected void update(float percent) {
/* 35 */     this.color.a = this.start + (this.end - this.start) * percent;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 39 */     super.reset();
/* 40 */     this.color = null;
/*    */   }
/*    */   
/*    */   public Color getColor() {
/* 44 */     return this.color;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setColor(Color color) {
/* 50 */     this.color = color;
/*    */   }
/*    */   
/*    */   public float getAlpha() {
/* 54 */     return this.end;
/*    */   }
/*    */   
/*    */   public void setAlpha(float alpha) {
/* 58 */     this.end = alpha;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\AlphaAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */