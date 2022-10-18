/*    */ package com.megacrit.cardcrawl.cards.deprecated;
/*    */ 
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class DEPRECATEDCalm extends AbstractCard {
/*    */   public static final String ID = "Calm";
/* 11 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Calm");
/*    */   
/*    */   public DEPRECATEDCalm() {
/* 14 */     super("Calm", cardStrings.NAME, null, 1, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.PURPLE, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.SELF);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 33 */     if (!this.upgraded) {
/* 34 */       upgradeName();
/* 35 */       upgradeBaseCost(0);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 41 */     return new DEPRECATEDCalm();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\deprecated\DEPRECATEDCalm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */