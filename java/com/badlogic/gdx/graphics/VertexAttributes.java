/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class VertexAttributes
/*     */   implements Iterable<VertexAttribute>, Comparable<VertexAttributes>
/*     */ {
/*     */   private final VertexAttribute[] attributes;
/*     */   public final int vertexSize;
/*     */   
/*     */   public static final class Usage
/*     */   {
/*     */     public static final int Position = 1;
/*     */     public static final int ColorUnpacked = 2;
/*     */     public static final int ColorPacked = 4;
/*     */     public static final int Normal = 8;
/*     */     public static final int TextureCoordinates = 16;
/*     */     public static final int Generic = 32;
/*     */     public static final int BoneWeight = 64;
/*     */     public static final int Tangent = 128;
/*     */     public static final int BiNormal = 256;
/*     */   }
/*  51 */   private long mask = -1L;
/*     */   
/*     */   private ReadonlyIterable<VertexAttribute> iterable;
/*     */ 
/*     */   
/*     */   public VertexAttributes(VertexAttribute... attributes) {
/*  57 */     if (attributes.length == 0) throw new IllegalArgumentException("attributes must be >= 1");
/*     */     
/*  59 */     VertexAttribute[] list = new VertexAttribute[attributes.length];
/*  60 */     for (int i = 0; i < attributes.length; i++) {
/*  61 */       list[i] = attributes[i];
/*     */     }
/*  63 */     this.attributes = list;
/*  64 */     this.vertexSize = calculateOffsets();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset(int usage, int defaultIfNotFound) {
/*  70 */     VertexAttribute vertexAttribute = findByUsage(usage);
/*  71 */     if (vertexAttribute == null) return defaultIfNotFound; 
/*  72 */     return vertexAttribute.offset / 4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset(int usage) {
/*  78 */     return getOffset(usage, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexAttribute findByUsage(int usage) {
/*  84 */     int len = size();
/*  85 */     for (int i = 0; i < len; i++) {
/*  86 */       if ((get(i)).usage == usage) return get(i); 
/*  87 */     }  return null;
/*     */   }
/*     */   
/*     */   private int calculateOffsets() {
/*  91 */     int count = 0;
/*  92 */     for (int i = 0; i < this.attributes.length; i++) {
/*  93 */       VertexAttribute attribute = this.attributes[i];
/*  94 */       attribute.offset = count;
/*  95 */       if (attribute.usage == 4) {
/*  96 */         count += 4;
/*     */       } else {
/*  98 */         count += 4 * attribute.numComponents;
/*     */       } 
/*     */     } 
/* 101 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 106 */     return this.attributes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VertexAttribute get(int index) {
/* 112 */     return this.attributes[index];
/*     */   }
/*     */   
/*     */   public String toString() {
/* 116 */     StringBuilder builder = new StringBuilder();
/* 117 */     builder.append("[");
/* 118 */     for (int i = 0; i < this.attributes.length; i++) {
/* 119 */       builder.append("(");
/* 120 */       builder.append((this.attributes[i]).alias);
/* 121 */       builder.append(", ");
/* 122 */       builder.append((this.attributes[i]).usage);
/* 123 */       builder.append(", ");
/* 124 */       builder.append((this.attributes[i]).numComponents);
/* 125 */       builder.append(", ");
/* 126 */       builder.append((this.attributes[i]).offset);
/* 127 */       builder.append(")");
/* 128 */       builder.append("\n");
/*     */     } 
/* 130 */     builder.append("]");
/* 131 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 136 */     if (obj == this) return true; 
/* 137 */     if (!(obj instanceof VertexAttributes)) return false; 
/* 138 */     VertexAttributes other = (VertexAttributes)obj;
/* 139 */     if (this.attributes.length != other.attributes.length) return false; 
/* 140 */     for (int i = 0; i < this.attributes.length; i++) {
/* 141 */       if (!this.attributes[i].equals(other.attributes[i])) return false; 
/*     */     } 
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 148 */     long result = (61 * this.attributes.length);
/* 149 */     for (int i = 0; i < this.attributes.length; i++)
/* 150 */       result = result * 61L + this.attributes[i].hashCode(); 
/* 151 */     return (int)(result ^ result >> 32L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMask() {
/* 158 */     if (this.mask == -1L) {
/* 159 */       long result = 0L;
/* 160 */       for (int i = 0; i < this.attributes.length; i++) {
/* 161 */         result |= (this.attributes[i]).usage;
/*     */       }
/* 163 */       this.mask = result;
/*     */     } 
/* 165 */     return this.mask;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(VertexAttributes o) {
/* 170 */     if (this.attributes.length != o.attributes.length) return this.attributes.length - o.attributes.length; 
/* 171 */     long m1 = getMask();
/* 172 */     long m2 = o.getMask();
/* 173 */     if (m1 != m2) return (m1 < m2) ? -1 : 1; 
/* 174 */     for (int i = this.attributes.length - 1; i >= 0; i--) {
/* 175 */       VertexAttribute va0 = this.attributes[i];
/* 176 */       VertexAttribute va1 = o.attributes[i];
/* 177 */       if (va0.usage != va1.usage) return va0.usage - va1.usage; 
/* 178 */       if (va0.unit != va1.unit) return va0.unit - va1.unit; 
/* 179 */       if (va0.numComponents != va1.numComponents) return va0.numComponents - va1.numComponents; 
/* 180 */       if (va0.normalized != va1.normalized) return va0.normalized ? 1 : -1; 
/* 181 */       if (va0.type != va1.type) return va0.type - va1.type; 
/*     */     } 
/* 183 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<VertexAttribute> iterator() {
/* 188 */     if (this.iterable == null) this.iterable = new ReadonlyIterable<VertexAttribute>(this.attributes); 
/* 189 */     return this.iterable.iterator();
/*     */   }
/*     */   
/*     */   private static class ReadonlyIterator<T> implements Iterator<T>, Iterable<T> {
/*     */     private final T[] array;
/*     */     int index;
/*     */     boolean valid = true;
/*     */     
/*     */     public ReadonlyIterator(T[] array) {
/* 198 */       this.array = array;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 203 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 204 */       return (this.index < this.array.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 209 */       if (this.index >= this.array.length) throw new NoSuchElementException(String.valueOf(this.index)); 
/* 210 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 211 */       return this.array[this.index++];
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 216 */       throw new GdxRuntimeException("Remove not allowed.");
/*     */     }
/*     */     
/*     */     public void reset() {
/* 220 */       this.index = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 225 */       return this;
/*     */     } }
/*     */   
/*     */   private static class ReadonlyIterable<T> implements Iterable<T> {
/*     */     private final T[] array;
/*     */     private VertexAttributes.ReadonlyIterator iterator1;
/*     */     private VertexAttributes.ReadonlyIterator iterator2;
/*     */     
/*     */     public ReadonlyIterable(T[] array) {
/* 234 */       this.array = array;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 239 */       if (this.iterator1 == null) {
/* 240 */         this.iterator1 = new VertexAttributes.ReadonlyIterator<T>(this.array);
/* 241 */         this.iterator2 = new VertexAttributes.ReadonlyIterator<T>(this.array);
/*     */       } 
/* 243 */       if (!this.iterator1.valid) {
/* 244 */         this.iterator1.index = 0;
/* 245 */         this.iterator1.valid = true;
/* 246 */         this.iterator2.valid = false;
/* 247 */         return this.iterator1;
/*     */       } 
/* 249 */       this.iterator2.index = 0;
/* 250 */       this.iterator2.valid = true;
/* 251 */       this.iterator1.valid = false;
/* 252 */       return this.iterator2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\VertexAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */