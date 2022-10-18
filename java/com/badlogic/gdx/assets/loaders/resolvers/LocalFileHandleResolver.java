/*    */ package com.badlogic.gdx.assets.loaders.resolvers;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ 
/*    */ public class LocalFileHandleResolver
/*    */   implements FileHandleResolver
/*    */ {
/*    */   public FileHandle resolve(String fileName) {
/* 11 */     return Gdx.files.local(fileName);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\resolvers\LocalFileHandleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */