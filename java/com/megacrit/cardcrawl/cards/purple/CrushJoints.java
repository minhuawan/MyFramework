/*    */ package com.megacrit.cardcrawl.cards.purple;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DamageAction;
/*    */ import com.megacrit.cardcrawl.actions.watcher.CrushJointsAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.cards.DamageInfo;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class CrushJoints extends AbstractCard {
/* 16 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("CrushJoints"); public static final String ID = "CrushJoints";
/*    */   
/*    */   public CrushJoints() {
/* 19 */     super("CrushJoints", cardStrings.NAME, "purple/attack/crush_joints", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.ATTACK, AbstractCard.CardColor.PURPLE, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ENEMY);
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
/* 30 */     this.baseDamage = 8;
/* 31 */     this.baseMagicNumber = 1;
/* 32 */     this.magicNumber = this.baseMagicNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 37 */     addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
/* 38 */     addToBot((AbstractGameAction)new CrushJointsAction(m, this.magicNumber));
/*    */   }
/*    */ 
/*    */   
/*    */   public void triggerOnGlowCheck() {
/* 43 */     if (!AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty() && ((AbstractCard)AbstractDungeon.actionManager.cardsPlayedThisCombat
/* 44 */       .get(AbstractDungeon.actionManager.cardsPlayedThisCombat
/* 45 */         .size() - 1)).type == AbstractCard.CardType.SKILL) {
/* 46 */       this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
/*    */     } else {
/* 48 */       this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 54 */     if (!this.upgraded) {
/* 55 */       upgradeName();
/* 56 */       upgradeDamage(2);
/* 57 */       upgradeMagicNumber(1);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 63 */     return new CrushJoints();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\purple\CrushJoints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */