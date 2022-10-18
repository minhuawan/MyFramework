/*    */ package com.megacrit.cardcrawl.cards.status;
/*    */ 
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Wound extends AbstractCard {
/*    */   public static final String ID = "Wound";
/* 11 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Wound");
/*    */   
/*    */   public Wound() {
/* 14 */     super("Wound", cardStrings.NAME, "status/wound", -2, cardStrings.DESCRIPTION, AbstractCard.CardType.STATUS, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.NONE);
/*    */   }
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
/*    */   public void upgrade() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 36 */     return new Wound();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\status\Wound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */