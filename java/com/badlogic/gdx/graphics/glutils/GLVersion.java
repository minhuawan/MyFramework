/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class GLVersion
/*     */ {
/*     */   private int majorVersion;
/*     */   private int minorVersion;
/*     */   private int releaseVersion;
/*     */   private final String vendorString;
/*     */   private final String rendererString;
/*     */   private final Type type;
/*  38 */   private final String TAG = "GLVersion";
/*     */   
/*     */   public GLVersion(Application.ApplicationType appType, String versionString, String vendorString, String rendererString) {
/*  41 */     if (appType == Application.ApplicationType.Android) { this.type = Type.GLES; }
/*  42 */     else if (appType == Application.ApplicationType.iOS) { this.type = Type.GLES; }
/*  43 */     else if (appType == Application.ApplicationType.Desktop) { this.type = Type.OpenGL; }
/*  44 */     else if (appType == Application.ApplicationType.Applet) { this.type = Type.OpenGL; }
/*  45 */     else if (appType == Application.ApplicationType.WebGL) { this.type = Type.WebGL; }
/*  46 */     else { this.type = Type.NONE; }
/*     */     
/*  48 */     if (this.type == Type.GLES) {
/*     */       
/*  50 */       extractVersion("OpenGL ES (\\d(\\.\\d){0,2})", versionString);
/*  51 */     } else if (this.type == Type.WebGL) {
/*     */       
/*  53 */       extractVersion("WebGL (\\d(\\.\\d){0,2})", versionString);
/*  54 */     } else if (this.type == Type.OpenGL) {
/*     */       
/*  56 */       extractVersion("(\\d(\\.\\d){0,2})", versionString);
/*     */     } else {
/*  58 */       this.majorVersion = -1;
/*  59 */       this.minorVersion = -1;
/*  60 */       this.releaseVersion = -1;
/*  61 */       vendorString = "";
/*  62 */       rendererString = "";
/*     */     } 
/*     */     
/*  65 */     this.vendorString = vendorString;
/*  66 */     this.rendererString = rendererString;
/*     */   }
/*     */   
/*     */   private void extractVersion(String patternString, String versionString) {
/*  70 */     Pattern pattern = Pattern.compile(patternString);
/*  71 */     Matcher matcher = pattern.matcher(versionString);
/*  72 */     boolean found = matcher.find();
/*  73 */     if (found) {
/*  74 */       String result = matcher.group(1);
/*  75 */       String[] resultSplit = result.split("\\.");
/*  76 */       this.majorVersion = parseInt(resultSplit[0], 2);
/*  77 */       this.minorVersion = (resultSplit.length < 2) ? 0 : parseInt(resultSplit[1], 0);
/*  78 */       this.releaseVersion = (resultSplit.length < 3) ? 0 : parseInt(resultSplit[2], 0);
/*     */     } else {
/*  80 */       Gdx.app.log("GLVersion", "Invalid version string: " + versionString);
/*  81 */       this.majorVersion = 2;
/*  82 */       this.minorVersion = 0;
/*  83 */       this.releaseVersion = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int parseInt(String v, int defaultValue) {
/*     */     try {
/*  90 */       return Integer.parseInt(v);
/*  91 */     } catch (NumberFormatException nfe) {
/*  92 */       Gdx.app.error("LibGDX GL", "Error parsing number: " + v + ", assuming: " + defaultValue);
/*  93 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  99 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMajorVersion() {
/* 104 */     return this.majorVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinorVersion() {
/* 109 */     return this.minorVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReleaseVersion() {
/* 114 */     return this.releaseVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVendorString() {
/* 119 */     return this.vendorString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRendererString() {
/* 125 */     return this.rendererString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVersionEqualToOrHigher(int testMajorVersion, int testMinorVersion) {
/* 136 */     return (this.majorVersion > testMajorVersion || (this.majorVersion == testMajorVersion && this.minorVersion >= testMinorVersion));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDebugVersionString() {
/* 141 */     return "Type: " + this.type + "\nVersion: " + this.majorVersion + ":" + this.minorVersion + ":" + this.releaseVersion + "\nVendor: " + this.vendorString + "\nRenderer: " + this.rendererString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/* 148 */     OpenGL,
/* 149 */     GLES,
/* 150 */     WebGL,
/* 151 */     NONE;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\GLVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */