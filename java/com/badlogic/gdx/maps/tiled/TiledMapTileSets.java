/*    */ package com.badlogic.gdx.maps.tiled;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ 
/*    */ 
/*    */ public class TiledMapTileSets
/*    */   implements Iterable<TiledMapTileSet>
/*    */ {
/* 30 */   private Array<TiledMapTileSet> tilesets = new Array();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TiledMapTileSet getTileSet(int index) {
/* 36 */     return (TiledMapTileSet)this.tilesets.get(index);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TiledMapTileSet getTileSet(String name) {
/* 42 */     for (TiledMapTileSet tileset : this.tilesets) {
/* 43 */       if (name.equals(tileset.getName())) {
/* 44 */         return tileset;
/*    */       }
/*    */     } 
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addTileSet(TiledMapTileSet tileset) {
/* 52 */     this.tilesets.add(tileset);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeTileSet(int index) {
/* 59 */     this.tilesets.removeIndex(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeTileSet(TiledMapTileSet tileset) {
/* 64 */     this.tilesets.removeValue(tileset, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TiledMapTile getTile(int id) {
/* 76 */     for (int i = this.tilesets.size - 1; i >= 0; i--) {
/* 77 */       TiledMapTileSet tileset = (TiledMapTileSet)this.tilesets.get(i);
/* 78 */       TiledMapTile tile = tileset.getTile(id);
/* 79 */       if (tile != null) {
/* 80 */         return tile;
/*    */       }
/*    */     } 
/* 83 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<TiledMapTileSet> iterator() {
/* 89 */     return this.tilesets.iterator();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TiledMapTileSets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */