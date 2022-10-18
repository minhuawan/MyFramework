/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class MarkOfTheBloom extends AbstractRelic {
/*    */   public static final String ID = "Mark of the Bloom";
/*    */   
/*    */   public MarkOfTheBloom() {
/*  7 */     super("Mark of the Bloom", "bloom.png", AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 12 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public int onPlayerHeal(int healAmount) {
/* 17 */     flash();
/* 18 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 23 */     return new MarkOfTheBloom();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\MarkOfTheBloom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */