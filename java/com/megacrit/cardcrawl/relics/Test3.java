/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class Test3 extends AbstractRelic {
/*    */   public static final String ID = "Test 3";
/*    */   
/*    */   public Test3() {
/*  7 */     super("Test 3", "test3.png", AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.FLAT);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 12 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEquip() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 22 */     return new Test3();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\Test3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */