/*     */ package com.badlogic.gdx.graphics.g3d;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ public class Attributes
/*     */   implements Iterable<Attribute>, Comparator<Attribute>, Comparable<Attributes>
/*     */ {
/*     */   protected long mask;
/*  26 */   protected final Array<Attribute> attributes = new Array();
/*     */   
/*     */   protected boolean sorted = true;
/*     */ 
/*     */   
/*     */   public final void sort() {
/*  32 */     if (!this.sorted) {
/*  33 */       this.attributes.sort(this);
/*  34 */       this.sorted = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getMask() {
/*  40 */     return this.mask;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Attribute get(long type) {
/*  46 */     if (has(type)) for (int i = 0; i < this.attributes.size; i++) {
/*  47 */         if (((Attribute)this.attributes.get(i)).type == type) return (Attribute)this.attributes.get(i); 
/*  48 */       }   return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T extends Attribute> T get(Class<T> clazz, long type) {
/*  54 */     return (T)get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Array<Attribute> get(Array<Attribute> out, long type) {
/*  60 */     for (int i = 0; i < this.attributes.size; i++) {
/*  61 */       if ((((Attribute)this.attributes.get(i)).type & type) != 0L) out.add(this.attributes.get(i)); 
/*  62 */     }  return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  67 */     this.mask = 0L;
/*  68 */     this.attributes.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  73 */     return this.attributes.size;
/*     */   }
/*     */   
/*     */   private final void enable(long mask) {
/*  77 */     this.mask |= mask;
/*     */   }
/*     */   
/*     */   private final void disable(long mask) {
/*  81 */     this.mask &= mask ^ 0xFFFFFFFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Attribute attribute) {
/*  86 */     int idx = indexOf(attribute.type);
/*  87 */     if (idx < 0) {
/*  88 */       enable(attribute.type);
/*  89 */       this.attributes.add(attribute);
/*  90 */       this.sorted = false;
/*     */     } else {
/*  92 */       this.attributes.set(idx, attribute);
/*     */     } 
/*  94 */     sort();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Attribute attribute1, Attribute attribute2) {
/*  99 */     set(attribute1);
/* 100 */     set(attribute2);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Attribute attribute1, Attribute attribute2, Attribute attribute3) {
/* 105 */     set(attribute1);
/* 106 */     set(attribute2);
/* 107 */     set(attribute3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(Attribute attribute1, Attribute attribute2, Attribute attribute3, Attribute attribute4) {
/* 113 */     set(attribute1);
/* 114 */     set(attribute2);
/* 115 */     set(attribute3);
/* 116 */     set(attribute4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(Attribute... attributes) {
/* 122 */     for (Attribute attr : attributes) {
/* 123 */       set(attr);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Iterable<Attribute> attributes) {
/* 129 */     for (Attribute attr : attributes) {
/* 130 */       set(attr);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void remove(long mask) {
/* 136 */     for (int i = this.attributes.size - 1; i >= 0; i--) {
/* 137 */       long type = ((Attribute)this.attributes.get(i)).type;
/* 138 */       if ((mask & type) == type) {
/* 139 */         this.attributes.removeIndex(i);
/* 140 */         disable(type);
/* 141 */         this.sorted = false;
/*     */       } 
/*     */     } 
/* 144 */     sort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean has(long type) {
/* 151 */     return (type != 0L && (this.mask & type) == type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int indexOf(long type) {
/* 156 */     if (has(type)) for (int i = 0; i < this.attributes.size; i++) {
/* 157 */         if (((Attribute)this.attributes.get(i)).type == type) return i; 
/* 158 */       }   return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean same(Attributes other, boolean compareValues) {
/* 166 */     if (other == this) return true; 
/* 167 */     if (other == null || this.mask != other.mask) return false; 
/* 168 */     if (!compareValues) return true; 
/* 169 */     sort();
/* 170 */     other.sort();
/* 171 */     for (int i = 0; i < this.attributes.size; i++) {
/* 172 */       if (!((Attribute)this.attributes.get(i)).equals((Attribute)other.attributes.get(i))) return false; 
/* 173 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean same(Attributes other) {
/* 179 */     return same(other, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int compare(Attribute arg0, Attribute arg1) {
/* 185 */     return (int)(arg0.type - arg1.type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Iterator<Attribute> iterator() {
/* 191 */     return this.attributes.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int attributesHash() {
/* 197 */     sort();
/* 198 */     int n = this.attributes.size;
/* 199 */     long result = 71L + this.mask;
/* 200 */     int m = 1;
/* 201 */     for (int i = 0; i < n; i++)
/* 202 */       result += this.mask * ((Attribute)this.attributes.get(i)).hashCode() * (m = m * 7 & 0xFFFF); 
/* 203 */     return (int)(result ^ result >> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 208 */     return attributesHash();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 213 */     if (!(other instanceof Attributes)) return false; 
/* 214 */     if (other == this) return true; 
/* 215 */     return same((Attributes)other, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Attributes other) {
/* 220 */     if (other == this)
/* 221 */       return 0; 
/* 222 */     if (this.mask != other.mask)
/* 223 */       return (this.mask < other.mask) ? -1 : 1; 
/* 224 */     sort();
/* 225 */     other.sort();
/* 226 */     for (int i = 0; i < this.attributes.size; i++) {
/* 227 */       int c = ((Attribute)this.attributes.get(i)).compareTo((Attribute)other.attributes.get(i));
/* 228 */       if (c != 0)
/* 229 */         return (c < 0) ? -1 : ((c > 0) ? 1 : 0); 
/*     */     } 
/* 231 */     return 0;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\Attributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */