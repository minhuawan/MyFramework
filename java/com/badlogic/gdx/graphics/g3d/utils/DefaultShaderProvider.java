/*    */ package com.badlogic.gdx.graphics.g3d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*    */ import com.badlogic.gdx.graphics.g3d.Shader;
/*    */ import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
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
/*    */ public class DefaultShaderProvider
/*    */   extends BaseShaderProvider
/*    */ {
/*    */   public final DefaultShader.Config config;
/*    */   
/*    */   public DefaultShaderProvider(DefaultShader.Config config) {
/* 29 */     this.config = (config == null) ? new DefaultShader.Config() : config;
/*    */   }
/*    */   
/*    */   public DefaultShaderProvider(String vertexShader, String fragmentShader) {
/* 33 */     this(new DefaultShader.Config(vertexShader, fragmentShader));
/*    */   }
/*    */   
/*    */   public DefaultShaderProvider(FileHandle vertexShader, FileHandle fragmentShader) {
/* 37 */     this(vertexShader.readString(), fragmentShader.readString());
/*    */   }
/*    */   
/*    */   public DefaultShaderProvider() {
/* 41 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Shader createShader(Renderable renderable) {
/* 46 */     return (Shader)new DefaultShader(renderable, this.config);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\DefaultShaderProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */