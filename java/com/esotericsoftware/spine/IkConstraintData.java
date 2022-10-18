/*    */ package com.esotericsoftware.spine;
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
/*    */ public class IkConstraintData
/*    */ {
/*    */   final String name;
/* 38 */   final Array<BoneData> bones = new Array();
/*    */   BoneData target;
/* 40 */   int bendDirection = 1;
/* 41 */   float mix = 1.0F;
/*    */   
/*    */   public IkConstraintData(String name) {
/* 44 */     if (name == null) throw new IllegalArgumentException("name cannot be null."); 
/* 45 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 49 */     return this.name;
/*    */   }
/*    */   
/*    */   public Array<BoneData> getBones() {
/* 53 */     return this.bones;
/*    */   }
/*    */   
/*    */   public BoneData getTarget() {
/* 57 */     return this.target;
/*    */   }
/*    */   
/*    */   public void setTarget(BoneData target) {
/* 61 */     if (target == null) throw new IllegalArgumentException("target cannot be null."); 
/* 62 */     this.target = target;
/*    */   }
/*    */   
/*    */   public int getBendDirection() {
/* 66 */     return this.bendDirection;
/*    */   }
/*    */   
/*    */   public void setBendDirection(int bendDirection) {
/* 70 */     this.bendDirection = bendDirection;
/*    */   }
/*    */   
/*    */   public float getMix() {
/* 74 */     return this.mix;
/*    */   }
/*    */   
/*    */   public void setMix(float mix) {
/* 78 */     this.mix = mix;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 82 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\esotericsoftware\spine\IkConstraintData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */