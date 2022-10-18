/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class RedCirclet extends AbstractRelic {
/*    */   public static final String ID = "Red Circlet";
/*    */   
/*    */   public RedCirclet() {
/*  7 */     super("Red Circlet", "redCirclet.png", AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 12 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 17 */     return new RedCirclet();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\RedCirclet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */