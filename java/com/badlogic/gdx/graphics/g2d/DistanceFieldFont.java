/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class DistanceFieldFont
/*     */   extends BitmapFont
/*     */ {
/*     */   private float distanceFieldSmoothing;
/*     */   
/*     */   public DistanceFieldFont(BitmapFont.BitmapFontData data, Array<TextureRegion> pageRegions, boolean integer) {
/*  40 */     super(data, pageRegions, integer);
/*     */   }
/*     */   
/*     */   public DistanceFieldFont(BitmapFont.BitmapFontData data, TextureRegion region, boolean integer) {
/*  44 */     super(data, region, integer);
/*     */   }
/*     */   
/*     */   public DistanceFieldFont(FileHandle fontFile, boolean flip) {
/*  48 */     super(fontFile, flip);
/*     */   }
/*     */   
/*     */   public DistanceFieldFont(FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer) {
/*  52 */     super(fontFile, imageFile, flip, integer);
/*     */   }
/*     */   
/*     */   public DistanceFieldFont(FileHandle fontFile, FileHandle imageFile, boolean flip) {
/*  56 */     super(fontFile, imageFile, flip);
/*     */   }
/*     */   
/*     */   public DistanceFieldFont(FileHandle fontFile, TextureRegion region, boolean flip) {
/*  60 */     super(fontFile, region, flip);
/*     */   }
/*     */   
/*     */   public DistanceFieldFont(FileHandle fontFile, TextureRegion region) {
/*  64 */     super(fontFile, region);
/*     */   }
/*     */   
/*     */   public DistanceFieldFont(FileHandle fontFile) {
/*  68 */     super(fontFile);
/*     */   }
/*     */   
/*     */   protected void load(BitmapFont.BitmapFontData data) {
/*  72 */     super.load(data);
/*     */ 
/*     */     
/*  75 */     Array<TextureRegion> regions = getRegions();
/*  76 */     for (TextureRegion region : regions) {
/*  77 */       region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
/*     */     }
/*     */   }
/*     */   
/*     */   public BitmapFontCache newFontCache() {
/*  82 */     return new DistanceFieldFontCache(this, this.integer);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDistanceFieldSmoothing() {
/*  87 */     return this.distanceFieldSmoothing;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDistanceFieldSmoothing(float distanceFieldSmoothing) {
/*  93 */     this.distanceFieldSmoothing = distanceFieldSmoothing;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShaderProgram createDistanceFieldShader() {
/*  99 */     String vertexShader = "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projTrans;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main() {\n\tv_color = a_color;\n\tv_color.a = v_color.a * (255.0/254.0);\n\tv_texCoords = a_texCoord0;\n\tgl_Position =  u_projTrans * a_position;\n}\n";
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
/* 113 */     String fragmentShader = "#ifdef GL_ES\n\tprecision mediump float;\n\tprecision mediump int;\n#endif\n\nuniform sampler2D u_texture;\nuniform float u_smoothing;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\n\nvoid main() {\n\tif (u_smoothing > 0.0) {\n\t\tfloat smoothing = 0.25 / u_smoothing;\n\t\tfloat distance = texture2D(u_texture, v_texCoords).a;\n\t\tfloat alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);\n\t\tgl_FragColor = vec4(v_color.rgb, alpha * v_color.a);\n\t} else {\n\t\tgl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n\t}\n}\n";
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
/* 134 */     ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
/* 135 */     if (!shader.isCompiled())
/* 136 */       throw new IllegalArgumentException("Error compiling distance field shader: " + shader.getLog()); 
/* 137 */     return shader;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DistanceFieldFontCache
/*     */     extends BitmapFontCache
/*     */   {
/*     */     public DistanceFieldFontCache(DistanceFieldFont font) {
/* 145 */       super(font, font.usesIntegerPositions());
/*     */     }
/*     */     
/*     */     public DistanceFieldFontCache(DistanceFieldFont font, boolean integer) {
/* 149 */       super(font, integer);
/*     */     }
/*     */     
/*     */     private float getSmoothingFactor() {
/* 153 */       DistanceFieldFont font = (DistanceFieldFont)getFont();
/* 154 */       return font.getDistanceFieldSmoothing() * font.getScaleX();
/*     */     }
/*     */     
/*     */     private void setSmoothingUniform(Batch spriteBatch, float smoothing) {
/* 158 */       spriteBatch.flush();
/* 159 */       spriteBatch.getShader().setUniformf("u_smoothing", smoothing);
/*     */     }
/*     */ 
/*     */     
/*     */     public void draw(Batch spriteBatch) {
/* 164 */       setSmoothingUniform(spriteBatch, getSmoothingFactor());
/* 165 */       super.draw(spriteBatch);
/* 166 */       setSmoothingUniform(spriteBatch, 0.0F);
/*     */     }
/*     */ 
/*     */     
/*     */     public void draw(Batch spriteBatch, int start, int end) {
/* 171 */       setSmoothingUniform(spriteBatch, getSmoothingFactor());
/* 172 */       super.draw(spriteBatch, start, end);
/* 173 */       setSmoothingUniform(spriteBatch, 0.0F);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\DistanceFieldFont.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */