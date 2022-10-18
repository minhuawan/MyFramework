/*    */ package com.megacrit.cardcrawl.cards.purple;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Ragnarok extends AbstractCard {
/*    */   public static final String ID = "Ragnarok";
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Ragnarok");
/*    */   
/*    */   public Ragnarok() {
/* 16 */     super("Ragnarok", cardStrings.NAME, "purple/attack/ragnarok", 3, cardStrings.DESCRIPTION, AbstractCard.CardType.ATTACK, AbstractCard.CardColor.PURPLE, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL_ENEMY);
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
/* 27 */     this.baseDamage = 5;
/* 28 */     this.baseMagicNumber = 5;
/* 29 */     this.magicNumber = this.baseMagicNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 34 */     for (int i = 0; i < this.magicNumber; i++) {
/* 35 */       addToBot((AbstractGameAction)new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.LIGHTNING));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 41 */     if (!this.upgraded) {
/* 42 */       upgradeName();
/* 43 */       upgradeDamage(1);
/* 44 */       upgradeMagicNumber(1);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 50 */     return new Ragnarok();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\purple\Ragnarok.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */