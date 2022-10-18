/*    */ package com.badlogic.gdx.maps.objects;
/*    */ 
/*    */ import com.badlogic.gdx.maps.MapObject;
/*    */ import com.badlogic.gdx.math.Rectangle;
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
/*    */ public class RectangleMapObject
/*    */   extends MapObject
/*    */ {
/*    */   private Rectangle rectangle;
/*    */   
/*    */   public Rectangle getRectangle() {
/* 29 */     return this.rectangle;
/*    */   }
/*    */ 
/*    */   
/*    */   public RectangleMapObject() {
/* 34 */     this(0.0F, 0.0F, 1.0F, 1.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RectangleMapObject(float x, float y, float width, float height) {
/* 45 */     this.rectangle = new Rectangle(x, y, width, height);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\objects\RectangleMapObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */