/*    */ package com.megacrit.cardcrawl.cards.deprecated;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*    */ import com.megacrit.cardcrawl.powers.deprecated.DEPRECATEDFlowPower;
/*    */ 
/*    */ public class DEPRECATEDFlow extends AbstractCard {
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Flow"); public static final String ID = "Flow";
/*    */   
/*    */   public DEPRECATEDFlow() {
/* 16 */     super("Flow", cardStrings.NAME, "green/power/after_image", 3, cardStrings.DESCRIPTION, AbstractCard.CardType.POWER, AbstractCard.CardColor.PURPLE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 30 */     addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new DEPRECATEDFlowPower((AbstractCreature)p, 1), 1));
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 35 */     return new DEPRECATEDFlow();
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 40 */     if (!this.upgraded) {
/* 41 */       upgradeName();
/* 42 */       upgradeBaseCost(2);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\deprecated\DEPRECATEDFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */