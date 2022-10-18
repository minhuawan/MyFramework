/*    */ package com.megacrit.cardcrawl.cards.green;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.cards.tempCards.Shiv;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class BladeDance extends AbstractCard {
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Blade Dance"); public static final String ID = "Blade Dance";
/*    */   
/*    */   public BladeDance() {
/* 16 */     super("Blade Dance", cardStrings.NAME, "green/skill/blade_dance", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.GREEN, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.NONE);
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
/* 27 */     this.baseMagicNumber = 3;
/* 28 */     this.magicNumber = this.baseMagicNumber;
/* 29 */     this.cardsToPreview = (AbstractCard)new Shiv();
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 34 */     addToBot((AbstractGameAction)new MakeTempCardInHandAction((AbstractCard)new Shiv(), this.magicNumber));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 39 */     if (!this.upgraded) {
/* 40 */       upgradeName();
/* 41 */       upgradeMagicNumber(1);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 47 */     return new BladeDance();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\green\BladeDance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */