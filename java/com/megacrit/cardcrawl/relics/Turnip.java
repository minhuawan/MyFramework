/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class Turnip extends AbstractRelic {
/*    */   public static final String ID = "Turnip";
/*    */   
/*    */   public Turnip() {
/*  7 */     super("Turnip", "turnip.png", AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.FLAT);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 12 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 17 */     return new Turnip();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\Turnip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */