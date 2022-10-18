/*    */ package com.megacrit.cardcrawl.cards.colorless;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*    */ import com.megacrit.cardcrawl.powers.PanachePower;
/*    */ 
/*    */ public class Panache extends AbstractCard {
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Panache"); public static final String ID = "Panache";
/*    */   
/*    */   public Panache() {
/* 16 */     super("Panache", cardStrings.NAME, "colorless/power/panache", 0, cardStrings.DESCRIPTION, AbstractCard.CardType.POWER, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
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
/* 27 */     this.baseMagicNumber = 10;
/* 28 */     this.magicNumber = this.baseMagicNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 33 */     addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new PanachePower((AbstractCreature)p, this.magicNumber), this.magicNumber));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 38 */     if (!this.upgraded) {
/* 39 */       upgradeName();
/* 40 */       upgradeMagicNumber(4);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 46 */     return new Panache();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\colorless\Panache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */