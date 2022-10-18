/*    */ package com.badlogic.gdx.maps;
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
/*    */ public class MapObject
/*    */ {
/* 23 */   private String name = "";
/* 24 */   private float opacity = 1.0F;
/*    */   private boolean visible = true;
/* 26 */   private MapProperties properties = new MapProperties();
/* 27 */   private Color color = Color.WHITE.cpy();
/*    */ 
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 36 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getColor() {
/* 41 */     return this.color;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setColor(Color color) {
/* 46 */     this.color = color;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getOpacity() {
/* 51 */     return this.opacity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOpacity(float opacity) {
/* 56 */     this.opacity = opacity;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVisible() {
/* 61 */     return this.visible;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setVisible(boolean visible) {
/* 66 */     this.visible = visible;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapProperties getProperties() {
/* 71 */     return this.properties;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\MapObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */