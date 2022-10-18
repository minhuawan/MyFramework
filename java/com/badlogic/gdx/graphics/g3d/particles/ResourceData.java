/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ import com.badlogic.gdx.utils.reflect.ReflectionException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceData<T>
/*     */   implements Json.Serializable
/*     */ {
/*     */   public static interface Configurable<T>
/*     */   {
/*     */     void save(AssetManager param1AssetManager, ResourceData<T> param1ResourceData);
/*     */     
/*     */     void load(AssetManager param1AssetManager, ResourceData<T> param1ResourceData);
/*     */   }
/*     */   
/*     */   public static class SaveData
/*     */     implements Json.Serializable
/*     */   {
/*     */     ObjectMap<String, Object> data;
/*     */     IntArray assets;
/*     */     private int loadIndex;
/*     */     protected ResourceData resources;
/*     */     
/*     */     public SaveData() {
/*  60 */       this.data = new ObjectMap();
/*  61 */       this.assets = new IntArray();
/*  62 */       this.loadIndex = 0;
/*     */     }
/*     */     
/*     */     public SaveData(ResourceData resources) {
/*  66 */       this.data = new ObjectMap();
/*  67 */       this.assets = new IntArray();
/*  68 */       this.loadIndex = 0;
/*  69 */       this.resources = resources;
/*     */     }
/*     */     
/*     */     public <K> void saveAsset(String filename, Class<K> type) {
/*  73 */       int i = this.resources.getAssetData(filename, type);
/*  74 */       if (i == -1) {
/*  75 */         this.resources.sharedAssets.add(new ResourceData.AssetData<K>(filename, type));
/*  76 */         i = this.resources.sharedAssets.size - 1;
/*     */       } 
/*  78 */       this.assets.add(i);
/*     */     }
/*     */     
/*     */     public void save(String key, Object value) {
/*  82 */       this.data.put(key, value);
/*     */     }
/*     */     
/*     */     public AssetDescriptor loadAsset() {
/*  86 */       if (this.loadIndex == this.assets.size) return null; 
/*  87 */       ResourceData.AssetData data = (ResourceData.AssetData)this.resources.sharedAssets.get(this.assets.get(this.loadIndex++));
/*  88 */       return new AssetDescriptor(data.filename, data.type);
/*     */     }
/*     */     
/*     */     public <K> K load(String key) {
/*  92 */       return (K)this.data.get(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/*  97 */       json.writeValue("data", this.data, ObjectMap.class);
/*  98 */       json.writeValue("indices", this.assets.toArray(), int[].class);
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 103 */       this.data = (ObjectMap<String, Object>)json.readValue("data", ObjectMap.class, jsonData);
/* 104 */       this.assets.addAll((int[])json.readValue("indices", int[].class, jsonData));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AssetData<T>
/*     */     implements Json.Serializable
/*     */   {
/*     */     public String filename;
/*     */     public Class<T> type;
/*     */     
/*     */     public AssetData() {}
/*     */     
/*     */     public AssetData(String filename, Class<T> type) {
/* 117 */       this.filename = filename;
/* 118 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(Json json) {
/* 123 */       json.writeValue("filename", this.filename);
/* 124 */       json.writeValue("type", this.type.getName());
/*     */     }
/*     */ 
/*     */     
/*     */     public void read(Json json, JsonValue jsonData) {
/* 129 */       this.filename = (String)json.readValue("filename", String.class, jsonData);
/* 130 */       String className = (String)json.readValue("type", String.class, jsonData);
/*     */       try {
/* 132 */         this.type = ClassReflection.forName(className);
/* 133 */       } catch (ReflectionException e) {
/* 134 */         throw new GdxRuntimeException("Class not found: " + className, e);
/*     */       } 
/*     */     }
/*     */   }
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
/* 152 */   private ObjectMap<String, SaveData> uniqueData = new ObjectMap();
/* 153 */   private Array<SaveData> data = new Array(true, 3, SaveData.class);
/* 154 */   Array<AssetData> sharedAssets = new Array();
/* 155 */   private int currentLoadIndex = 0;
/*     */   public T resource;
/*     */   
/*     */   public ResourceData(T resource) {
/* 159 */     this();
/* 160 */     this.resource = resource;
/*     */   }
/*     */   public ResourceData() {}
/*     */   <K> int getAssetData(String filename, Class<K> type) {
/* 164 */     int i = 0;
/* 165 */     for (AssetData data : this.sharedAssets) {
/* 166 */       if (data.filename.equals(filename) && data.type.equals(type)) {
/* 167 */         return i;
/*     */       }
/* 169 */       i++;
/*     */     } 
/* 171 */     return -1;
/*     */   }
/*     */   
/*     */   public Array<AssetDescriptor> getAssetDescriptors() {
/* 175 */     Array<AssetDescriptor> descriptors = new Array();
/* 176 */     for (AssetData data : this.sharedAssets) {
/* 177 */       descriptors.add(new AssetDescriptor(data.filename, data.type));
/*     */     }
/* 179 */     return descriptors;
/*     */   }
/*     */   
/*     */   public Array<AssetData> getAssets() {
/* 183 */     return this.sharedAssets;
/*     */   }
/*     */ 
/*     */   
/*     */   public SaveData createSaveData() {
/* 188 */     SaveData saveData = new SaveData(this);
/* 189 */     this.data.add(saveData);
/* 190 */     return saveData;
/*     */   }
/*     */ 
/*     */   
/*     */   public SaveData createSaveData(String key) {
/* 195 */     SaveData saveData = new SaveData(this);
/* 196 */     if (this.uniqueData.containsKey(key)) throw new RuntimeException("Key already used, data must be unique, use a different key"); 
/* 197 */     this.uniqueData.put(key, saveData);
/* 198 */     return saveData;
/*     */   }
/*     */ 
/*     */   
/*     */   public SaveData getSaveData() {
/* 203 */     return (SaveData)this.data.get(this.currentLoadIndex++);
/*     */   }
/*     */ 
/*     */   
/*     */   public SaveData getSaveData(String key) {
/* 208 */     return (SaveData)this.uniqueData.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 213 */     json.writeValue("unique", this.uniqueData, ObjectMap.class);
/* 214 */     json.writeValue("data", this.data, Array.class, SaveData.class);
/* 215 */     json.writeValue("assets", this.sharedAssets.toArray(AssetData.class), AssetData[].class);
/* 216 */     json.writeValue("resource", this.resource, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 221 */     this.uniqueData = (ObjectMap<String, SaveData>)json.readValue("unique", ObjectMap.class, jsonData);
/* 222 */     for (ObjectMap.Entries<ObjectMap.Entry<String, SaveData>> entries = this.uniqueData.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, SaveData> entry = entries.next();
/* 223 */       ((SaveData)entry.value).resources = this; }
/*     */ 
/*     */     
/* 226 */     this.data = (Array<SaveData>)json.readValue("data", Array.class, SaveData.class, jsonData);
/* 227 */     for (SaveData saveData : this.data) {
/* 228 */       saveData.resources = this;
/*     */     }
/*     */     
/* 231 */     this.sharedAssets.addAll((Array)json.readValue("assets", Array.class, AssetData.class, jsonData));
/* 232 */     this.resource = (T)json.readValue("resource", null, jsonData);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ResourceData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */