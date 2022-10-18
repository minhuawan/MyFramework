/*    */ package com.megacrit.cardcrawl.relics;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.GainBlockAction;
/*    */ import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ 
/*    */ public class HornCleat extends AbstractRelic {
/*    */   public static final String ID = "HornCleat";
/*    */   
/*    */   public HornCleat() {
/* 12 */     super("HornCleat", "horn_cleat.png", AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.HEAVY);
/*    */   }
/*    */   private static final int TURN_ACTIVATION = 2;
/*    */   
/*    */   public String getUpdatedDescription() {
/* 17 */     return this.DESCRIPTIONS[0] + '\016' + this.DESCRIPTIONS[1];
/*    */   }
/*    */   
/*    */   public void atBattleStart() {
/* 21 */     this.counter = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void atTurnStart() {
/* 26 */     if (!this.grayscale) {
/* 27 */       this.counter++;
/*    */     }
/* 29 */     if (this.counter == 2) {
/* 30 */       flash();
/* 31 */       addToBot((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
/* 32 */       addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, 14));
/* 33 */       this.counter = -1;
/* 34 */       this.grayscale = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onVictory() {
/* 39 */     this.counter = -1;
/* 40 */     this.grayscale = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 45 */     return new HornCleat();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\HornCleat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */