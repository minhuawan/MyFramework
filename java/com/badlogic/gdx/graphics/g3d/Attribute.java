/*    */ package com.badlogic.gdx.graphics.g3d;
/*    */ 
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
/*    */ public abstract class Attribute
/*    */   implements Comparable<Attribute>
/*    */ {
/* 27 */   private static final Array<String> types = new Array();
/*    */   public final long type;
/*    */   
/*    */   public static final long getAttributeType(String alias) {
/* 31 */     for (int i = 0; i < types.size; i++) {
/* 32 */       if (((String)types.get(i)).compareTo(alias) == 0) return 1L << i; 
/* 33 */     }  return 0L;
/*    */   }
/*    */   private final int typeBit;
/*    */   
/*    */   public static final String getAttributeAlias(long type) {
/* 38 */     int idx = -1;
/* 39 */     while (type != 0L && ++idx < 63 && (type >> idx & 0x1L) == 0L);
/*    */     
/* 41 */     return (idx >= 0 && idx < types.size) ? (String)types.get(idx) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static final long register(String alias) {
/* 49 */     long result = getAttributeType(alias);
/* 50 */     if (result > 0L) return result; 
/* 51 */     types.add(alias);
/* 52 */     return 1L << types.size - 1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Attribute(long type) {
/* 61 */     this.type = type;
/* 62 */     this.typeBit = Long.numberOfTrailingZeros(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract Attribute copy();
/*    */   
/*    */   protected boolean equals(Attribute other) {
/* 69 */     return (other.hashCode() == hashCode());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 74 */     if (obj == null) return false; 
/* 75 */     if (obj == this) return true; 
/* 76 */     if (!(obj instanceof Attribute)) return false; 
/* 77 */     Attribute other = (Attribute)obj;
/* 78 */     if (this.type != other.type) return false; 
/* 79 */     return equals(other);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 84 */     return getAttributeAlias(this.type);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 89 */     return 7489 * this.typeBit;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\Attribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */