/*    */ package com.megacrit.cardcrawl.cards.status;
/*    */ 
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Slimed extends AbstractCard {
/*    */   public static final String ID = "Slimed";
/* 11 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Slimed");
/*    */   
/*    */   public Slimed() {
/* 14 */     super("Slimed", cardStrings.NAME, "status/slimed", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.STATUS, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);
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
/* 25 */     this.exhaust = true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void upgrade() {}
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 38 */     return new Slimed();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\status\Slimed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */