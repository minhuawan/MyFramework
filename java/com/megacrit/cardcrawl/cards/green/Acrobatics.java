/*    */ package com.megacrit.cardcrawl.cards.green;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DiscardAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DrawCardAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Acrobatics extends AbstractCard {
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Acrobatics"); public static final String ID = "Acrobatics";
/*    */   
/*    */   public Acrobatics() {
/* 16 */     super("Acrobatics", cardStrings.NAME, "green/skill/acrobatics", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.GREEN, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.NONE);
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
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 33 */     addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, this.magicNumber));
/* 34 */     addToBot((AbstractGameAction)new DiscardAction((AbstractCreature)p, (AbstractCreature)p, 1, false));
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
/* 47 */     return new Acrobatics();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\green\Acrobatics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */