/*    */ package com.megacrit.cardcrawl.cards.blue;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.defect.RedoAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Recursion extends AbstractCard {
/* 12 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Redo"); public static final String ID = "Redo";
/*    */   
/*    */   public Recursion() {
/* 15 */     super("Redo", cardStrings.NAME, "blue/skill/recursion", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.BLUE, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);
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
/* 29 */     addToBot((AbstractGameAction)new RedoAction());
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 34 */     if (!this.upgraded) {
/* 35 */       upgradeName();
/* 36 */       upgradeBaseCost(0);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 42 */     return new Recursion();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\blue\Recursion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */