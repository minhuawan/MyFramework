/*    */ package com.badlogic.gdx.maps;
/*    */ 
/*    */ import com.badlogic.gdx.utils.ObjectMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapProperties
/*    */ {
/* 33 */   private ObjectMap<String, Object> properties = new ObjectMap();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean containsKey(String key) {
/* 39 */     return this.properties.containsKey(key);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object get(String key) {
/* 45 */     return this.properties.get(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T get(String key, Class<T> clazz) {
/* 54 */     return (T)get(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T get(String key, T defaultValue, Class<T> clazz) {
/* 64 */     Object object = get(key);
/* 65 */     return (object == null) ? defaultValue : (T)object;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void put(String key, Object value) {
/* 71 */     this.properties.put(key, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void putAll(MapProperties properties) {
/* 76 */     this.properties.putAll(properties.properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(String key) {
/* 81 */     this.properties.remove(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 86 */     this.properties.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<String> getKeys() {
/* 91 */     return (Iterator<String>)this.properties.keys();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Object> getValues() {
/* 96 */     return (Iterator<Object>)this.properties.values();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\MapProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */