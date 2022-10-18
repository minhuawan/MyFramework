/*    */ package com.megacrit.cardcrawl.powers;
/*    */ 
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.PowerStrings;
/*    */ 
/*    */ public class ResurrectPower extends AbstractPower {
/*    */   public static final String POWER_ID = "Life Link";
/*  9 */   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Life Link");
/* 10 */   public static final String NAME = powerStrings.NAME;
/* 11 */   public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
/*    */   
/*    */   public ResurrectPower(AbstractCreature owner) {
/* 14 */     this.name = NAME;
/* 15 */     this.ID = "Life Link";
/* 16 */     this.owner = owner;
/* 17 */     updateDescription();
/* 18 */     loadRegion("regrow");
/* 19 */     this.type = AbstractPower.PowerType.BUFF;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateDescription() {
/* 24 */     this.description = DESCRIPTIONS[0];
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\powers\ResurrectPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */