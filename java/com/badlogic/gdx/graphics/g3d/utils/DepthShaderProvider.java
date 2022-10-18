/*    */ package com.badlogic.gdx.graphics.g3d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*    */ import com.badlogic.gdx.graphics.g3d.Shader;
/*    */ import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
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
/*    */ public class DepthShaderProvider
/*    */   extends BaseShaderProvider
/*    */ {
/*    */   public final DepthShader.Config config;
/*    */   
/*    */   public DepthShaderProvider(DepthShader.Config config) {
/* 29 */     this.config = (config == null) ? new DepthShader.Config() : config;
/*    */   }
/*    */   
/*    */   public DepthShaderProvider(String vertexShader, String fragmentShader) {
/* 33 */     this(new DepthShader.Config(vertexShader, fragmentShader));
/*    */   }
/*    */   
/*    */   public DepthShaderProvider(FileHandle vertexShader, FileHandle fragmentShader) {
/* 37 */     this(vertexShader.readString(), fragmentShader.readString());
/*    */   }
/*    */   
/*    */   public DepthShaderProvider() {
/* 41 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Shader createShader(Renderable renderable) {
/* 46 */     return (Shader)new DepthShader(renderable, this.config);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\DepthShaderProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */