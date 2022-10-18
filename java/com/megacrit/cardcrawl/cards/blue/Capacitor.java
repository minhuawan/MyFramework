/*    */ package com.megacrit.cardcrawl.cards.blue;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Capacitor extends AbstractCard {
/* 12 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Capacitor"); public static final String ID = "Capacitor";
/*    */   
/*    */   public Capacitor() {
/* 15 */     super("Capacitor", cardStrings.NAME, "blue/power/capacitor", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.POWER, AbstractCard.CardColor.BLUE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
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
/* 26 */     this.baseMagicNumber = 2;
/* 27 */     this.magicNumber = this.baseMagicNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 32 */     addToBot((AbstractGameAction)new IncreaseMaxOrbAction(this.magicNumber));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 37 */     if (!this.upgraded) {
/* 38 */       upgradeName();
/* 39 */       upgradeMagicNumber(1);
/* 40 */       this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
/* 41 */       initializeDescription();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 47 */     return new Capacitor();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\blue\Capacitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */