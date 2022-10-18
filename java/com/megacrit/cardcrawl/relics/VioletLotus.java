/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
/*    */ import com.megacrit.cardcrawl.stances.AbstractStance;
/*    */ 
/*    */ public class VioletLotus extends AbstractRelic {
/*    */   public static final String ID = "VioletLotus";
/*    */   
/*    */   public VioletLotus() {
/* 11 */     super("VioletLotus", "violet_lotus.png", AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.MAGICAL);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 16 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public void onChangeStance(AbstractStance prevStance, AbstractStance newStance) {
/* 21 */     if (!prevStance.ID.equals(newStance.ID) && prevStance.ID.equals("Calm")) {
/* 22 */       flash();
/* 23 */       addToBot((AbstractGameAction)new GainEnergyAction(1));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 29 */     return new VioletLotus();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\VioletLotus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */