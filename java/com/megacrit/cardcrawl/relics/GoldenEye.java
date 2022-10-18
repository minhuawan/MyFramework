/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class GoldenEye extends AbstractRelic {
/*    */   public static final String ID = "GoldenEye";
/*    */   
/*    */   public GoldenEye() {
/*  7 */     super("GoldenEye", "golden_eye.png", AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.HEAVY);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 12 */     return this.DESCRIPTIONS[0] + '\002' + this.DESCRIPTIONS[1];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 17 */     return new GoldenEye();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\GoldenEye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */