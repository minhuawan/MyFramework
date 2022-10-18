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
/*    */ public class ColorAction
/*    */   extends TemporalAction
/*    */ {
/*    */   private float startR;
/*    */   private float startG;
/*    */   private float startB;
/*    */   private float startA;
/*    */   private Color color;
/* 28 */   private final Color end = new Color();
/*    */   
/*    */   protected void begin() {
/* 31 */     if (this.color == null) this.color = this.target.getColor(); 
/* 32 */     this.startR = this.color.r;
/* 33 */     this.startG = this.color.g;
/* 34 */     this.startB = this.color.b;
/* 35 */     this.startA = this.color.a;
/*    */   }
/*    */   
/*    */   protected void update(float percent) {
/* 39 */     float r = this.startR + (this.end.r - this.startR) * percent;
/* 40 */     float g = this.startG + (this.end.g - this.startG) * percent;
/* 41 */     float b = this.startB + (this.end.b - this.startB) * percent;
/* 42 */     float a = this.startA + (this.end.a - this.startA) * percent;
/* 43 */     this.color.set(r, g, b, a);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 47 */     super.reset();
/* 48 */     this.color = null;
/*    */   }
/*    */   
/*    */   public Color getColor() {
/* 52 */     return this.color;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setColor(Color color) {
/* 58 */     this.color = color;
/*    */   }
/*    */   
/*    */   public Color getEndColor() {
/* 62 */     return this.end;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEndColor(Color color) {
/* 67 */     this.end.set(color);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\ColorAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */