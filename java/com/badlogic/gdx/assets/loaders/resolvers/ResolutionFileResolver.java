/*     */ package com.badlogic.gdx.assets.loaders.resolvers;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.files.FileHandle;
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
/*     */ public class ResolutionFileResolver
/*     */   implements FileHandleResolver
/*     */ {
/*     */   protected final FileHandleResolver baseResolver;
/*     */   protected final Resolution[] descriptors;
/*     */   
/*     */   public static class Resolution
/*     */   {
/*     */     public final int portraitWidth;
/*     */     public final int portraitHeight;
/*     */     public final String folder;
/*     */     
/*     */     public Resolution(int portraitWidth, int portraitHeight, String folder) {
/*  66 */       this.portraitWidth = portraitWidth;
/*  67 */       this.portraitHeight = portraitHeight;
/*  68 */       this.folder = folder;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolutionFileResolver(FileHandleResolver baseResolver, Resolution... descriptors) {
/*  79 */     if (descriptors.length == 0) throw new IllegalArgumentException("At least one Resolution needs to be supplied."); 
/*  80 */     this.baseResolver = baseResolver;
/*  81 */     this.descriptors = descriptors;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileHandle resolve(String fileName) {
/*  86 */     Resolution bestResolution = choose(this.descriptors);
/*  87 */     FileHandle originalHandle = new FileHandle(fileName);
/*  88 */     FileHandle handle = this.baseResolver.resolve(resolve(originalHandle, bestResolution.folder));
/*  89 */     if (!handle.exists()) handle = this.baseResolver.resolve(fileName); 
/*  90 */     return handle;
/*     */   }
/*     */   
/*     */   protected String resolve(FileHandle originalHandle, String suffix) {
/*  94 */     String parentString = "";
/*  95 */     FileHandle parent = originalHandle.parent();
/*  96 */     if (parent != null && !parent.name().equals("")) {
/*  97 */       parentString = parent + "/";
/*     */     }
/*  99 */     return parentString + suffix + "/" + originalHandle.name();
/*     */   }
/*     */   
/*     */   public static Resolution choose(Resolution... descriptors) {
/* 103 */     int w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
/*     */ 
/*     */     
/* 106 */     Resolution best = descriptors[0];
/* 107 */     if (w < h) {
/* 108 */       for (int i = 0, n = descriptors.length; i < n; i++) {
/* 109 */         Resolution other = descriptors[i];
/* 110 */         if (w >= other.portraitWidth && other.portraitWidth >= best.portraitWidth && h >= other.portraitHeight && other.portraitHeight >= best.portraitHeight)
/* 111 */           best = descriptors[i]; 
/*     */       } 
/*     */     } else {
/* 114 */       for (int i = 0, n = descriptors.length; i < n; i++) {
/* 115 */         Resolution other = descriptors[i];
/* 116 */         if (w >= other.portraitHeight && other.portraitHeight >= best.portraitHeight && h >= other.portraitWidth && other.portraitWidth >= best.portraitWidth)
/* 117 */           best = descriptors[i]; 
/*     */       } 
/*     */     } 
/* 120 */     return best;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\resolvers\ResolutionFileResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */