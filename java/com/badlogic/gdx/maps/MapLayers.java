/*    */ package com.badlogic.gdx.maps;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.reflect.ClassReflection;
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
/*    */ public class MapLayers
/*    */   implements Iterable<MapLayer>
/*    */ {
/* 26 */   private Array<MapLayer> layers = new Array();
/*    */ 
/*    */ 
/*    */   
/*    */   public MapLayer get(int index) {
/* 31 */     return (MapLayer)this.layers.get(index);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public MapLayer get(String name) {
/* 37 */     for (int i = 0, n = this.layers.size; i < n; i++) {
/* 38 */       MapLayer layer = (MapLayer)this.layers.get(i);
/* 39 */       if (name.equals(layer.getName())) {
/* 40 */         return layer;
/*    */       }
/*    */     } 
/* 43 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getIndex(String name) {
/* 48 */     return getIndex(get(name));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getIndex(MapLayer layer) {
/* 53 */     return this.layers.indexOf(layer, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCount() {
/* 58 */     return this.layers.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(MapLayer layer) {
/* 63 */     this.layers.add(layer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(int index) {
/* 68 */     this.layers.removeIndex(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(MapLayer layer) {
/* 73 */     this.layers.removeValue(layer, true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends MapLayer> Array<T> getByType(Class<T> type) {
/* 79 */     return getByType(type, new Array());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends MapLayer> Array<T> getByType(Class<T> type, Array<T> fill) {
/* 86 */     fill.clear();
/* 87 */     for (int i = 0, n = this.layers.size; i < n; i++) {
/* 88 */       MapLayer layer = (MapLayer)this.layers.get(i);
/* 89 */       if (ClassReflection.isInstance(type, layer)) {
/* 90 */         fill.add(layer);
/*    */       }
/*    */     } 
/* 93 */     return fill;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<MapLayer> iterator() {
/* 99 */     return this.layers.iterator();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\MapLayers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */