/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class Ginger extends AbstractRelic {
/*    */   public static final String ID = "Ginger";
/*    */   
/*    */   public Ginger() {
/*  7 */     super("Ginger", "ginger.png", AbstractRelic.RelicTier.RARE, AbstractRelic.LandingSound.FLAT);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 12 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 17 */     return new Ginger();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\Ginger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */