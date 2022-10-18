/*    */ package com.megacrit.cardcrawl.cards.colorless;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Violence extends AbstractCard {
/* 12 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Violence"); public static final String ID = "Violence";
/*    */   
/*    */   public Violence() {
/* 15 */     super("Violence", cardStrings.NAME, "colorless/skill/violence", 0, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.NONE);
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
/* 26 */     this.baseMagicNumber = 3;
/* 27 */     this.magicNumber = this.baseMagicNumber;
/* 28 */     this.exhaust = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 33 */     addToBot((AbstractGameAction)new DrawPileToHandAction(this.magicNumber, AbstractCard.CardType.ATTACK));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 38 */     if (!this.upgraded) {
/* 39 */       upgradeName();
/* 40 */       upgradeMagicNumber(1);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 46 */     return new Violence();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\colorless\Violence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */