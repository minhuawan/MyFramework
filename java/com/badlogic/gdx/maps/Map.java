/*    */ package com.badlogic.gdx.maps;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Disposable;
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
/*    */ 
/*    */ public class Map
/*    */   implements Disposable
/*    */ {
/* 44 */   private MapLayers layers = new MapLayers();
/* 45 */   private MapProperties properties = new MapProperties();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MapLayers getLayers() {
/* 54 */     return this.layers;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapProperties getProperties() {
/* 59 */     return this.properties;
/*    */   }
/*    */   
/*    */   public void dispose() {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\Map.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */