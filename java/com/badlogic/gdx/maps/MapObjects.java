/*     */ package com.badlogic.gdx.maps;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapObjects
/*     */   implements Iterable<MapObject>
/*     */ {
/*  31 */   private Array<MapObject> objects = new Array();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapObject get(int index) {
/*  37 */     return (MapObject)this.objects.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapObject get(String name) {
/*  43 */     for (int i = 0, n = this.objects.size; i < n; i++) {
/*  44 */       MapObject object = (MapObject)this.objects.get(i);
/*  45 */       if (name.equals(object.getName())) {
/*  46 */         return object;
/*     */       }
/*     */     } 
/*  49 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIndex(String name) {
/*  54 */     return getIndex(get(name));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIndex(MapObject object) {
/*  59 */     return this.objects.indexOf(object, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCount() {
/*  64 */     return this.objects.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(MapObject object) {
/*  69 */     this.objects.add(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(int index) {
/*  74 */     this.objects.removeIndex(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(MapObject object) {
/*  79 */     this.objects.removeValue(object, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends MapObject> Array<T> getByType(Class<T> type) {
/*  85 */     return getByType(type, new Array());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends MapObject> Array<T> getByType(Class<T> type, Array<T> fill) {
/*  92 */     fill.clear();
/*  93 */     for (int i = 0, n = this.objects.size; i < n; i++) {
/*  94 */       MapObject object = (MapObject)this.objects.get(i);
/*  95 */       if (ClassReflection.isInstance(type, object)) {
/*  96 */         fill.add(object);
/*     */       }
/*     */     } 
/*  99 */     return fill;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<MapObject> iterator() {
/* 105 */     return this.objects.iterator();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\MapObjects.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */