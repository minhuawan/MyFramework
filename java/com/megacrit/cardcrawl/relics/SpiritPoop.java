/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class SpiritPoop extends AbstractRelic {
/*    */   public static final String ID = "Spirit Poop";
/*    */   
/*    */   public SpiritPoop() {
/*  7 */     super("Spirit Poop", "spiritPoop.png", AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 12 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 17 */     return new SpiritPoop();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\SpiritPoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */