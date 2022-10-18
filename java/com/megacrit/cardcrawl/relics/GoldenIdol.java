/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class GoldenIdol
/*    */   extends AbstractRelic {
/*    */   public static final String ID = "Golden Idol";
/*    */   public static final float MULTIPLIER = 0.25F;
/*    */   
/*    */   public GoldenIdol() {
/*  9 */     super("Golden Idol", "goldenIdolRelic.png", AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 14 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 19 */     return new GoldenIdol();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\GoldenIdol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */