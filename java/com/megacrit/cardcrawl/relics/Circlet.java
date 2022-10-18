/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ public class Circlet extends AbstractRelic {
/*    */   public static final String ID = "Circlet";
/*    */   
/*    */   public Circlet() {
/*  7 */     super("Circlet", "circlet.png", AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
/*  8 */     this.counter = 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 13 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEquip() {
/* 18 */     flash();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onUnequip() {}
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 27 */     return new Circlet();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\Circlet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */