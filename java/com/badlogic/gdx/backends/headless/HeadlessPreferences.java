/*     */ package com.badlogic.gdx.backends.headless;
/*     */ 
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Preferences;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public class HeadlessPreferences
/*     */   implements Preferences
/*     */ {
/*  36 */   private final Properties properties = new Properties();
/*     */   private final FileHandle file;
/*     */   
/*     */   public HeadlessPreferences(String name, String directory) {
/*  40 */     this(new HeadlessFileHandle(new File(directory, name), Files.FileType.External));
/*     */   }
/*     */   
/*     */   public HeadlessPreferences(FileHandle file) {
/*  44 */     this.file = file;
/*  45 */     if (!file.exists())
/*  46 */       return;  InputStream in = null;
/*     */     try {
/*  48 */       in = new BufferedInputStream(file.read());
/*  49 */       this.properties.loadFromXML(in);
/*  50 */     } catch (Throwable t) {
/*  51 */       t.printStackTrace();
/*     */     } finally {
/*  53 */       StreamUtils.closeQuietly(in);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putBoolean(String key, boolean val) {
/*  59 */     this.properties.put(key, Boolean.toString(val));
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putInteger(String key, int val) {
/*  65 */     this.properties.put(key, Integer.toString(val));
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putLong(String key, long val) {
/*  71 */     this.properties.put(key, Long.toString(val));
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putFloat(String key, float val) {
/*  77 */     this.properties.put(key, Float.toString(val));
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putString(String key, String val) {
/*  83 */     this.properties.put(key, val);
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences put(Map<String, ?> vals) {
/*  89 */     for (Map.Entry<String, ?> val : vals.entrySet()) {
/*  90 */       if (val.getValue() instanceof Boolean) putBoolean(val.getKey(), ((Boolean)val.getValue()).booleanValue()); 
/*  91 */       if (val.getValue() instanceof Integer) putInteger(val.getKey(), ((Integer)val.getValue()).intValue()); 
/*  92 */       if (val.getValue() instanceof Long) putLong(val.getKey(), ((Long)val.getValue()).longValue()); 
/*  93 */       if (val.getValue() instanceof String) putString(val.getKey(), (String)val.getValue()); 
/*  94 */       if (val.getValue() instanceof Float) putFloat(val.getKey(), ((Float)val.getValue()).floatValue()); 
/*     */     } 
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String key) {
/* 101 */     return getBoolean(key, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInteger(String key) {
/* 106 */     return getInteger(key, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(String key) {
/* 111 */     return getLong(key, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(String key) {
/* 116 */     return getFloat(key, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String key) {
/* 121 */     return getString(key, "");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String key, boolean defValue) {
/* 126 */     return Boolean.parseBoolean(this.properties.getProperty(key, Boolean.toString(defValue)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInteger(String key, int defValue) {
/* 131 */     return Integer.parseInt(this.properties.getProperty(key, Integer.toString(defValue)));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(String key, long defValue) {
/* 136 */     return Long.parseLong(this.properties.getProperty(key, Long.toString(defValue)));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(String key, float defValue) {
/* 141 */     return Float.parseFloat(this.properties.getProperty(key, Float.toString(defValue)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String key, String defValue) {
/* 146 */     return this.properties.getProperty(key, defValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, ?> get() {
/* 151 */     Map<String, Object> map = new HashMap<String, Object>();
/* 152 */     for (Map.Entry<Object, Object> val : this.properties.entrySet()) {
/* 153 */       if (val.getValue() instanceof Boolean)
/* 154 */         map.put((String)val.getKey(), Boolean.valueOf(Boolean.parseBoolean((String)val.getValue()))); 
/* 155 */       if (val.getValue() instanceof Integer) map.put((String)val.getKey(), Integer.valueOf(Integer.parseInt((String)val.getValue()))); 
/* 156 */       if (val.getValue() instanceof Long) map.put((String)val.getKey(), Long.valueOf(Long.parseLong((String)val.getValue()))); 
/* 157 */       if (val.getValue() instanceof String) map.put((String)val.getKey(), val.getValue()); 
/* 158 */       if (val.getValue() instanceof Float) map.put((String)val.getKey(), Float.valueOf(Float.parseFloat((String)val.getValue())));
/*     */     
/*     */     } 
/* 161 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(String key) {
/* 166 */     return this.properties.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 171 */     this.properties.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() {
/* 176 */     OutputStream out = null;
/*     */     try {
/* 178 */       out = new BufferedOutputStream(this.file.write(false));
/* 179 */       this.properties.storeToXML(out, null);
/* 180 */     } catch (Exception ex) {
/* 181 */       throw new GdxRuntimeException("Error writing preferences: " + this.file, ex);
/*     */     } finally {
/* 183 */       StreamUtils.closeQuietly(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 189 */     this.properties.remove(key);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\HeadlessPreferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */