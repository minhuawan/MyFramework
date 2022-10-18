/*    */ package com.badlogic.gdx.graphics.g3d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.GLTexture;
/*    */ import com.badlogic.gdx.graphics.Texture;
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
/*    */ public class TextureDescriptor<T extends GLTexture>
/*    */   implements Comparable<TextureDescriptor<T>>
/*    */ {
/* 23 */   public T texture = null;
/*    */   
/*    */   public Texture.TextureFilter minFilter;
/*    */   
/*    */   public Texture.TextureFilter magFilter;
/*    */   
/*    */   public Texture.TextureWrap uWrap;
/*    */   public Texture.TextureWrap vWrap;
/*    */   
/*    */   public TextureDescriptor(T texture, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, Texture.TextureWrap uWrap, Texture.TextureWrap vWrap) {
/* 33 */     set(texture, minFilter, magFilter, uWrap, vWrap);
/*    */   }
/*    */   
/*    */   public TextureDescriptor(T texture) {
/* 37 */     this(texture, null, null, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void set(T texture, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, Texture.TextureWrap uWrap, Texture.TextureWrap vWrap) {
/* 45 */     this.texture = texture;
/* 46 */     this.minFilter = minFilter;
/* 47 */     this.magFilter = magFilter;
/* 48 */     this.uWrap = uWrap;
/* 49 */     this.vWrap = vWrap;
/*    */   }
/*    */   
/*    */   public <V extends T> void set(TextureDescriptor<V> other) {
/* 53 */     this.texture = other.texture;
/* 54 */     this.minFilter = other.minFilter;
/* 55 */     this.magFilter = other.magFilter;
/* 56 */     this.uWrap = other.uWrap;
/* 57 */     this.vWrap = other.vWrap;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 62 */     if (obj == null) return false; 
/* 63 */     if (obj == this) return true; 
/* 64 */     if (!(obj instanceof TextureDescriptor)) return false; 
/* 65 */     TextureDescriptor<?> other = (TextureDescriptor)obj;
/* 66 */     return (other.texture == this.texture && other.minFilter == this.minFilter && other.magFilter == this.magFilter && other.uWrap == this.uWrap && other.vWrap == this.vWrap);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 72 */     long result = ((this.texture == null) ? 0L : ((GLTexture)this.texture).glTarget);
/* 73 */     result = 811L * result + ((this.texture == null) ? 0L : this.texture.getTextureObjectHandle());
/* 74 */     result = 811L * result + ((this.minFilter == null) ? 0L : this.minFilter.getGLEnum());
/* 75 */     result = 811L * result + ((this.magFilter == null) ? 0L : this.magFilter.getGLEnum());
/* 76 */     result = 811L * result + ((this.uWrap == null) ? 0L : this.uWrap.getGLEnum());
/* 77 */     result = 811L * result + ((this.vWrap == null) ? 0L : this.vWrap.getGLEnum());
/* 78 */     return (int)(result ^ result >> 32L);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(TextureDescriptor<T> o) {
/* 83 */     if (o == this) return 0; 
/* 84 */     int t1 = (this.texture == null) ? 0 : ((GLTexture)this.texture).glTarget;
/* 85 */     int t2 = (o.texture == null) ? 0 : ((GLTexture)o.texture).glTarget;
/* 86 */     if (t1 != t2) return t1 - t2; 
/* 87 */     int h1 = (this.texture == null) ? 0 : this.texture.getTextureObjectHandle();
/* 88 */     int h2 = (o.texture == null) ? 0 : o.texture.getTextureObjectHandle();
/* 89 */     if (h1 != h2) return h1 - h2; 
/* 90 */     if (this.minFilter != o.minFilter)
/* 91 */       return ((this.minFilter == null) ? 0 : this.minFilter.getGLEnum()) - ((o.minFilter == null) ? 0 : o.minFilter.getGLEnum()); 
/* 92 */     if (this.magFilter != o.magFilter)
/* 93 */       return ((this.magFilter == null) ? 0 : this.magFilter.getGLEnum()) - ((o.magFilter == null) ? 0 : o.magFilter.getGLEnum()); 
/* 94 */     if (this.uWrap != o.uWrap) return ((this.uWrap == null) ? 0 : this.uWrap.getGLEnum()) - ((o.uWrap == null) ? 0 : o.uWrap.getGLEnum()); 
/* 95 */     if (this.vWrap != o.vWrap) return ((this.vWrap == null) ? 0 : this.vWrap.getGLEnum()) - ((o.vWrap == null) ? 0 : o.vWrap.getGLEnum()); 
/* 96 */     return 0;
/*    */   }
/*    */   
/*    */   public TextureDescriptor() {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\TextureDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */