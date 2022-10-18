/*    */ package com.megacrit.cardcrawl.cards.green;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*    */ import com.megacrit.cardcrawl.powers.PhantasmalPower;
/*    */ 
/*    */ public class PhantasmalKiller extends AbstractCard {
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Phantasmal Killer"); public static final String ID = "Phantasmal Killer";
/*    */   
/*    */   public PhantasmalKiller() {
/* 16 */     super("Phantasmal Killer", cardStrings.NAME, "green/skill/phantasmal_killer", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.GREEN, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
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
/* 30 */     addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new PhantasmalPower((AbstractCreature)p, 1), 1));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 35 */     if (!this.upgraded) {
/* 36 */       upgradeName();
/* 37 */       upgradeBaseCost(0);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 43 */     return new PhantasmalKiller();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\green\PhantasmalKiller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */