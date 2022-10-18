/*    */ package com.badlogic.gdx.maps.tiled;
/*    */ 
/*    */ import com.badlogic.gdx.maps.Map;
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ public class TiledMap
/*    */   extends Map
/*    */ {
/*    */   private TiledMapTileSets tilesets;
/*    */   private Array<? extends Disposable> ownedResources;
/*    */   
/*    */   public TiledMapTileSets getTileSets() {
/* 34 */     return this.tilesets;
/*    */   }
/*    */ 
/*    */   
/*    */   public TiledMap() {
/* 39 */     this.tilesets = new TiledMapTileSets();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOwnedResources(Array<? extends Disposable> resources) {
/* 46 */     this.ownedResources = resources;
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 51 */     if (this.ownedResources != null)
/* 52 */       for (Disposable resource : this.ownedResources)
/* 53 */         resource.dispose();  
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TiledMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */