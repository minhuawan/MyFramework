/*    */ package com.megacrit.cardcrawl.cards.red;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.GainBlockAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.core.Settings;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Defend_Red extends AbstractCard {
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Defend_R"); public static final String ID = "Defend_R";
/*    */   
/*    */   public Defend_Red() {
/* 16 */     super("Defend_R", cardStrings.NAME, "red/skill/defend", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.RED, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.SELF);
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
/* 27 */     this.baseBlock = 5;
/* 28 */     this.tags.add(AbstractCard.CardTags.STARTER_DEFEND);
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 33 */     if (Settings.isDebug) {
/* 34 */       addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, 50));
/*    */     } else {
/* 36 */       addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 42 */     if (!this.upgraded) {
/* 43 */       upgradeName();
/* 44 */       upgradeBlock(3);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 50 */     return new Defend_Red();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\red\Defend_Red.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */