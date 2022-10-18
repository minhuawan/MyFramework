/*     */ package com.badlogic.gdx.backends.lwjgl;
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
/*     */ public class LwjglPreferences
/*     */   implements Preferences
/*     */ {
/*     */   private final String name;
/*  37 */   private final Properties properties = new Properties();
/*     */   private final FileHandle file;
/*     */   
/*     */   public LwjglPreferences(String name, String directory) {
/*  41 */     this(new LwjglFileHandle(new File(directory, name), Files.FileType.External));
/*     */   }
/*     */   
/*     */   public LwjglPreferences(FileHandle file) {
/*  45 */     this.name = file.name();
/*  46 */     this.file = file;
/*  47 */     if (!file.exists())
/*  48 */       return;  InputStream in = null;
/*     */     try {
/*  50 */       in = new BufferedInputStream(file.read());
/*  51 */       this.properties.loadFromXML(in);
/*  52 */     } catch (Throwable t) {
/*  53 */       t.printStackTrace();
/*     */     } finally {
/*  55 */       StreamUtils.closeQuietly(in);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putBoolean(String key, boolean val) {
/*  61 */     this.properties.put(key, Boolean.toString(val));
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putInteger(String key, int val) {
/*  67 */     this.properties.put(key, Integer.toString(val));
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putLong(String key, long val) {
/*  73 */     this.properties.put(key, Long.toString(val));
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putFloat(String key, float val) {
/*  79 */     this.properties.put(key, Float.toString(val));
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences putString(String key, String val) {
/*  85 */     this.properties.put(key, val);
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Preferences put(Map<String, ?> vals) {
/*  91 */     for (Map.Entry<String, ?> val : vals.entrySet()) {
/*  92 */       if (val.getValue() instanceof Boolean) putBoolean(val.getKey(), ((Boolean)val.getValue()).booleanValue()); 
/*  93 */       if (val.getValue() instanceof Integer) putInteger(val.getKey(), ((Integer)val.getValue()).intValue()); 
/*  94 */       if (val.getValue() instanceof Long) putLong(val.getKey(), ((Long)val.getValue()).longValue()); 
/*  95 */       if (val.getValue() instanceof String) putString(val.getKey(), (String)val.getValue()); 
/*  96 */       if (val.getValue() instanceof Float) putFloat(val.getKey(), ((Float)val.getValue()).floatValue()); 
/*     */     } 
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String key) {
/* 103 */     return getBoolean(key, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInteger(String key) {
/* 108 */     return getInteger(key, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(String key) {
/* 113 */     return getLong(key, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(String key) {
/* 118 */     return getFloat(key, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String key) {
/* 123 */     return getString(key, "");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String key, boolean defValue) {
/* 128 */     return Boolean.parseBoolean(this.properties.getProperty(key, Boolean.toString(defValue)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInteger(String key, int defValue) {
/* 133 */     return Integer.parseInt(this.properties.getProperty(key, Integer.toString(defValue)));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(String key, long defValue) {
/* 138 */     return Long.parseLong(this.properties.getProperty(key, Long.toString(defValue)));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(String key, float defValue) {
/* 143 */     return Float.parseFloat(this.properties.getProperty(key, Float.toString(defValue)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String key, String defValue) {
/* 148 */     return this.properties.getProperty(key, defValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, ?> get() {
/* 153 */     Map<String, Object> map = new HashMap<String, Object>();
/* 154 */     for (Map.Entry<Object, Object> val : this.properties.entrySet()) {
/* 155 */       if (val.getValue() instanceof Boolean)
/* 156 */         map.put((String)val.getKey(), Boolean.valueOf(Boolean.parseBoolean((String)val.getValue()))); 
/* 157 */       if (val.getValue() instanceof Integer) map.put((String)val.getKey(), Integer.valueOf(Integer.parseInt((String)val.getValue()))); 
/* 158 */       if (val.getValue() instanceof Long) map.put((String)val.getKey(), Long.valueOf(Long.parseLong((String)val.getValue()))); 
/* 159 */       if (val.getValue() instanceof String) map.put((String)val.getKey(), val.getValue()); 
/* 160 */       if (val.getValue() instanceof Float) map.put((String)val.getKey(), Float.valueOf(Float.parseFloat((String)val.getValue())));
/*     */     
/*     */     } 
/* 163 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(String key) {
/* 168 */     return this.properties.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 173 */     this.properties.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() {
/* 178 */     OutputStream out = null;
/*     */     try {
/* 180 */       out = new BufferedOutputStream(this.file.write(false));
/* 181 */       this.properties.storeToXML(out, null);
/* 182 */     } catch (Exception ex) {
/* 183 */       throw new GdxRuntimeException("Error writing preferences: " + this.file, ex);
/*     */     } finally {
/* 185 */       StreamUtils.closeQuietly(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 191 */     this.properties.remove(key);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglPreferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */