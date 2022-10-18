/*    */ package com.badlogic.gdx.maps;
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
/*    */ public class MapLayer
/*    */ {
/* 21 */   private String name = "";
/* 22 */   private float opacity = 1.0F;
/*    */   private boolean visible = true;
/* 24 */   private MapObjects objects = new MapObjects();
/* 25 */   private MapProperties properties = new MapProperties();
/*    */ 
/*    */   
/*    */   public String getName() {
/* 29 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 34 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getOpacity() {
/* 39 */     return this.opacity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOpacity(float opacity) {
/* 44 */     this.opacity = opacity;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapObjects getObjects() {
/* 49 */     return this.objects;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVisible() {
/* 54 */     return this.visible;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setVisible(boolean visible) {
/* 59 */     this.visible = visible;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapProperties getProperties() {
/* 64 */     return this.properties;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\MapLayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */