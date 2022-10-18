/*    */ package com.badlogic.gdx.maps.tiled;
/*    */ 
/*    */ import com.badlogic.gdx.maps.MapProperties;
/*    */ import com.badlogic.gdx.utils.IntMap;
/*    */ import java.util.Iterator;
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
/*    */ public class TiledMapTileSet
/*    */   implements Iterable<TiledMapTile>
/*    */ {
/*    */   private String name;
/*    */   private IntMap<TiledMapTile> tiles;
/*    */   private MapProperties properties;
/*    */   
/*    */   public String getName() {
/* 34 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 39 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapProperties getProperties() {
/* 44 */     return this.properties;
/*    */   }
/*    */ 
/*    */   
/*    */   public TiledMapTileSet() {
/* 49 */     this.tiles = new IntMap();
/* 50 */     this.properties = new MapProperties();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TiledMapTile getTile(int id) {
/* 58 */     return (TiledMapTile)this.tiles.get(id);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<TiledMapTile> iterator() {
/* 64 */     return this.tiles.values().iterator();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void putTile(int id, TiledMapTile tile) {
/* 72 */     this.tiles.put(id, tile);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeTile(int id) {
/* 77 */     this.tiles.remove(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 82 */     return this.tiles.size;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TiledMapTileSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */