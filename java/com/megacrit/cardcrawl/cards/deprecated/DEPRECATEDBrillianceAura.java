/*    */ package com.megacrit.cardcrawl.cards.deprecated;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DrawCardAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class DEPRECATEDBrillianceAura extends AbstractCard {
/* 12 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("BrillianceAura"); public static final String ID = "BrillianceAura";
/*    */   
/*    */   public DEPRECATEDBrillianceAura() {
/* 15 */     super("BrillianceAura", cardStrings.NAME, null, 2, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.PURPLE, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.NONE);
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
/* 26 */     this.selfRetain = true;
/* 27 */     this.exhaust = true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void atTurnStartPreDraw() {
/* 37 */     addToBot((AbstractGameAction)new DrawCardAction(1));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 42 */     if (!this.upgraded) {
/* 43 */       upgradeName();
/* 44 */       upgradeBaseCost(1);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 50 */     return new DEPRECATEDBrillianceAura();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\deprecated\DEPRECATEDBrillianceAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */