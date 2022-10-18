/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class OddMushroom
/*    */   extends AbstractRelic {
/*    */   public static final String ID = "Odd Mushroom";
/*    */   public static final float VULN_EFFECTIVENESS = 1.25F;
/*    */   public static final int EFFECTIVENESS_STRING = 25;
/*    */   
/*    */   public OddMushroom() {
/* 10 */     super("Odd Mushroom", "mushroom.png", AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 15 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 20 */     return new OddMushroom();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\OddMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */