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
/*    */ public class Material
/*    */   extends Attributes
/*    */ {
/* 22 */   private static int counter = 0;
/*    */   
/*    */   public String id;
/*    */ 
/*    */   
/*    */   public Material() {
/* 28 */     this("mtl" + ++counter);
/*    */   }
/*    */ 
/*    */   
/*    */   public Material(String id) {
/* 33 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public Material(Attribute... attributes) {
/* 38 */     this();
/* 39 */     set(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   public Material(String id, Attribute... attributes) {
/* 44 */     this(id);
/* 45 */     set(attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   public Material(Array<Attribute> attributes) {
/* 50 */     this();
/* 51 */     set((Iterable<Attribute>)attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   public Material(String id, Array<Attribute> attributes) {
/* 56 */     this(id);
/* 57 */     set((Iterable<Attribute>)attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   public Material(Material copyFrom) {
/* 62 */     this(copyFrom.id, copyFrom);
/*    */   }
/*    */ 
/*    */   
/*    */   public Material(String id, Material copyFrom) {
/* 67 */     this(id);
/* 68 */     for (Attribute attr : copyFrom) {
/* 69 */       set(attr.copy());
/*    */     }
/*    */   }
/*    */   
/*    */   public final Material copy() {
/* 74 */     return new Material(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 79 */     return super.hashCode() + 3 * this.id.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 84 */     return (other instanceof Material && (other == this || (((Material)other).id.equals(this.id) && super.equals(other))));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\Material.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */