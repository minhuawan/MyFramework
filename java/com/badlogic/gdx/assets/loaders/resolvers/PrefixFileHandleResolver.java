/*    */ package com.badlogic.gdx.assets.loaders.resolvers;
/*    */ 
/*    */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*    */ import com.badlogic.gdx.files.FileHandle;
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
/*    */ public class PrefixFileHandleResolver
/*    */   implements FileHandleResolver
/*    */ {
/*    */   private String prefix;
/*    */   private FileHandleResolver baseResolver;
/*    */   
/*    */   public PrefixFileHandleResolver(FileHandleResolver baseResolver, String prefix) {
/* 30 */     this.baseResolver = baseResolver;
/* 31 */     this.prefix = prefix;
/*    */   }
/*    */   
/*    */   public void setBaseResolver(FileHandleResolver baseResolver) {
/* 35 */     this.baseResolver = baseResolver;
/*    */   }
/*    */   
/*    */   public FileHandleResolver getBaseResolver() {
/* 39 */     return this.baseResolver;
/*    */   }
/*    */   
/*    */   public void setPrefix(String prefix) {
/* 43 */     this.prefix = prefix;
/*    */   }
/*    */   
/*    */   public String getPrefix() {
/* 47 */     return this.prefix;
/*    */   }
/*    */ 
/*    */   
/*    */   public FileHandle resolve(String fileName) {
/* 52 */     return this.baseResolver.resolve(this.prefix + fileName);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\resolvers\PrefixFileHandleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */