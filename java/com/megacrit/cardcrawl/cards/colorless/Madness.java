/*    */ package com.megacrit.cardcrawl.cards.colorless;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.unique.MadnessAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Madness extends AbstractCard {
/* 12 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Madness"); public static final String ID = "Madness";
/*    */   
/*    */   public Madness() {
/* 15 */     super("Madness", cardStrings.NAME, "colorless/skill/madness", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
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
/* 26 */     this.exhaust = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 31 */     addToBot((AbstractGameAction)new MadnessAction());
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 36 */     if (!this.upgraded) {
/* 37 */       upgradeName();
/* 38 */       upgradeBaseCost(0);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 44 */     return new Madness();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\colorless\Madness.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */