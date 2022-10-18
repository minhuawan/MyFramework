/*    */ package com.megacrit.cardcrawl.powers;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
/*    */ import com.megacrit.cardcrawl.cards.DamageInfo;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.PowerStrings;
/*    */ 
/*    */ public class JuggernautPower extends AbstractPower {
/*    */   public static final String POWER_ID = "Juggernaut";
/* 12 */   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Juggernaut");
/* 13 */   public static final String NAME = powerStrings.NAME;
/* 14 */   public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
/*    */   
/*    */   public JuggernautPower(AbstractCreature owner, int newAmount) {
/* 17 */     this.name = NAME;
/* 18 */     this.ID = "Juggernaut";
/* 19 */     this.owner = owner;
/* 20 */     this.amount = newAmount;
/* 21 */     updateDescription();
/* 22 */     loadRegion("juggernaut");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onGainedBlock(float blockAmount) {
/* 27 */     if (blockAmount > 0.0F) {
/* 28 */       flash();
/* 29 */       addToBot((AbstractGameAction)new DamageRandomEnemyAction(new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateDescription() {
/* 38 */     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\powers\JuggernautPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */