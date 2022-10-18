/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ 
/*    */ public class ShaderProgramLoader
/*    */   extends AsynchronousAssetLoader<ShaderProgram, ShaderProgramLoader.ShaderProgramParameter>
/*    */ {
/* 39 */   private String vertexFileSuffix = ".vert";
/* 40 */   private String fragmentFileSuffix = ".frag";
/*    */   
/*    */   public ShaderProgramLoader(FileHandleResolver resolver) {
/* 43 */     super(resolver);
/*    */   }
/*    */   
/*    */   public ShaderProgramLoader(FileHandleResolver resolver, String vertexFileSuffix, String fragmentFileSuffix) {
/* 47 */     super(resolver);
/* 48 */     this.vertexFileSuffix = vertexFileSuffix;
/* 49 */     this.fragmentFileSuffix = fragmentFileSuffix;
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ShaderProgramParameter parameter) {
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, ShaderProgramParameter parameter) {}
/*    */ 
/*    */   
/*    */   public ShaderProgram loadSync(AssetManager manager, String fileName, FileHandle file, ShaderProgramParameter parameter) {
/* 63 */     String vertFileName = null, fragFileName = null;
/* 64 */     if (parameter != null) {
/* 65 */       if (parameter.vertexFile != null) vertFileName = parameter.vertexFile; 
/* 66 */       if (parameter.fragmentFile != null) fragFileName = parameter.fragmentFile; 
/*    */     } 
/* 68 */     if (vertFileName == null && fileName.endsWith(this.fragmentFileSuffix)) {
/* 69 */       vertFileName = fileName.substring(0, fileName.length() - this.fragmentFileSuffix.length()) + this.vertexFileSuffix;
/*    */     }
/* 71 */     if (fragFileName == null && fileName.endsWith(this.vertexFileSuffix)) {
/* 72 */       fragFileName = fileName.substring(0, fileName.length() - this.vertexFileSuffix.length()) + this.fragmentFileSuffix;
/*    */     }
/* 74 */     FileHandle vertexFile = (vertFileName == null) ? file : resolve(vertFileName);
/* 75 */     FileHandle fragmentFile = (fragFileName == null) ? file : resolve(fragFileName);
/* 76 */     String vertexCode = vertexFile.readString();
/* 77 */     String fragmentCode = vertexFile.equals(fragmentFile) ? vertexCode : fragmentFile.readString();
/* 78 */     if (parameter != null) {
/* 79 */       if (parameter.prependVertexCode != null) vertexCode = parameter.prependVertexCode + vertexCode; 
/* 80 */       if (parameter.prependFragmentCode != null) fragmentCode = parameter.prependFragmentCode + fragmentCode;
/*    */     
/*    */     } 
/* 83 */     ShaderProgram shaderProgram = new ShaderProgram(vertexCode, fragmentCode);
/* 84 */     if ((parameter == null || parameter.logOnCompileFailure) && !shaderProgram.isCompiled()) {
/* 85 */       manager.getLogger().error("ShaderProgram " + fileName + " failed to compile:\n" + shaderProgram.getLog());
/*    */     }
/*    */     
/* 88 */     return shaderProgram;
/*    */   }
/*    */   
/*    */   public static class ShaderProgramParameter extends AssetLoaderParameters<ShaderProgram> {
/*    */     public String vertexFile;
/*    */     public String fragmentFile;
/*    */     public boolean logOnCompileFailure = true;
/*    */     public String prependVertexCode;
/*    */     public String prependFragmentCode;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\ShaderProgramLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */