/*    */ package com.badlogic.gdx;
/*    */ 
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*    */ public class Version
/*    */ {
/*    */   public static final String VERSION = "1.9.5";
/*    */   public static final int MAJOR;
/*    */   public static final int MINOR;
/*    */   public static final int REVISION;
/*    */   
/*    */   static {
/*    */     try {
/* 39 */       String[] v = "1.9.5".split("\\.");
/* 40 */       MAJOR = (v.length < 1) ? 0 : Integer.valueOf(v[0]).intValue();
/* 41 */       MINOR = (v.length < 2) ? 0 : Integer.valueOf(v[1]).intValue();
/* 42 */       REVISION = (v.length < 3) ? 0 : Integer.valueOf(v[2]).intValue();
/*    */     }
/* 44 */     catch (Throwable t) {
/*    */       
/* 46 */       throw new GdxRuntimeException("Invalid version 1.9.5", t);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean isHigher(int major, int minor, int revision) {
/* 51 */     return isHigherEqual(major, minor, revision + 1);
/*    */   }
/*    */   
/*    */   public static boolean isHigherEqual(int major, int minor, int revision) {
/* 55 */     if (MAJOR != major)
/* 56 */       return (MAJOR > major); 
/* 57 */     if (MINOR != minor)
/* 58 */       return (MINOR > minor); 
/* 59 */     return (REVISION >= revision);
/*    */   }
/*    */   
/*    */   public static boolean isLower(int major, int minor, int revision) {
/* 63 */     return isLowerEqual(major, minor, revision - 1);
/*    */   }
/*    */   
/*    */   public static boolean isLowerEqual(int major, int minor, int revision) {
/* 67 */     if (MAJOR != major)
/* 68 */       return (MAJOR < major); 
/* 69 */     if (MINOR != minor)
/* 70 */       return (MINOR < minor); 
/* 71 */     return (REVISION <= revision);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\Version.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */