/*    */ package com.megacrit.cardcrawl.cards.colorless;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class DramaticEntrance extends AbstractCard {
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Dramatic Entrance"); public static final String ID = "Dramatic Entrance";
/*    */   
/*    */   public DramaticEntrance() {
/* 16 */     super("Dramatic Entrance", cardStrings.NAME, "colorless/attack/dramatic_entrance", 0, cardStrings.DESCRIPTION, AbstractCard.CardType.ATTACK, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ALL_ENEMY);
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
/* 27 */     this.baseDamage = 8;
/* 28 */     this.isMultiDamage = true;
/* 29 */     this.exhaust = true;
/* 30 */     this.isInnate = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 35 */     addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 40 */     if (!this.upgraded) {
/* 41 */       upgradeName();
/* 42 */       upgradeDamage(4);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 48 */     return new DramaticEntrance();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\colorless\DramaticEntrance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */