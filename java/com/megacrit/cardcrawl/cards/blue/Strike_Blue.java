/*    */ package com.megacrit.cardcrawl.cards.blue;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DamageAction;
/*    */ import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.cards.DamageInfo;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.core.Settings;
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class Strike_Blue extends AbstractCard {
/* 17 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Strike_B"); public static final String ID = "Strike_B";
/*    */   
/*    */   public Strike_Blue() {
/* 20 */     super("Strike_B", cardStrings.NAME, "blue/attack/strike", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.ATTACK, AbstractCard.CardColor.BLUE, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.ENEMY);
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
/* 31 */     this.baseDamage = 6;
/* 32 */     this.tags.add(AbstractCard.CardTags.STRIKE);
/* 33 */     this.tags.add(AbstractCard.CardTags.STARTER_STRIKE);
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 38 */     if (Settings.isDebug) {
/* 39 */       if (Settings.isInfo) {
/* 40 */         this.multiDamage = new int[(AbstractDungeon.getCurrRoom()).monsters.monsters.size()];
/* 41 */         for (int i = 0; i < (AbstractDungeon.getCurrRoom()).monsters.monsters.size(); i++) {
/* 42 */           this.multiDamage[i] = 150;
/*    */         }
/* 44 */         addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
/*    */       } else {
/* 46 */         addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, 150, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
/*    */       } 
/*    */     } else {
/* 49 */       addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 55 */     if (!this.upgraded) {
/* 56 */       upgradeName();
/* 57 */       upgradeDamage(3);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 63 */     return new Strike_Blue();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\blue\Strike_Blue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */