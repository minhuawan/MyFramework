/*     */ package com.megacrit.cardcrawl.actions.unique;
/*     */ 
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*     */ import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
/*     */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.helpers.ModHelper;
/*     */ import com.megacrit.cardcrawl.helpers.MonsterHelper;
/*     */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*     */ import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
/*     */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*     */ import com.megacrit.cardcrawl.powers.MinionPower;
/*     */ import com.megacrit.cardcrawl.powers.SlowPower;
/*     */ import com.megacrit.cardcrawl.powers.StrengthPower;
/*     */ import com.megacrit.cardcrawl.relics.AbstractRelic;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SummonGremlinAction
/*     */   extends AbstractGameAction
/*     */ {
/*  34 */   private static final Logger logger = LogManager.getLogger(SummonGremlinAction.class.getName());
/*     */   
/*     */   private AbstractMonster m;
/*     */   
/*     */   public SummonGremlinAction(AbstractMonster[] gremlins) {
/*  39 */     if (Settings.FAST_MODE) {
/*  40 */       this.startDuration = Settings.ACTION_DUR_FAST;
/*     */     } else {
/*  42 */       this.startDuration = Settings.ACTION_DUR_LONG;
/*     */     } 
/*  44 */     this.duration = this.startDuration;
/*  45 */     int slot = identifySlot(gremlins);
/*     */     
/*  47 */     if (slot == -1) {
/*  48 */       logger.info("INCORRECTLY ATTEMPTED TO CHANNEL GREMLIN.");
/*     */       
/*     */       return;
/*     */     } 
/*  52 */     this.m = getRandomGremlin(slot);
/*  53 */     gremlins[slot] = this.m;
/*     */     
/*  55 */     for (AbstractRelic r : AbstractDungeon.player.relics) {
/*  56 */       r.onSpawnMonster(this.m);
/*     */     }
/*     */   }
/*     */   
/*     */   private int identifySlot(AbstractMonster[] gremlins) {
/*  61 */     for (int i = 0; i < gremlins.length; i++) {
/*  62 */       if (gremlins[i] == null || (gremlins[i]).isDying) {
/*  63 */         return i;
/*     */       }
/*     */     } 
/*  66 */     return -1;
/*     */   }
/*     */   
/*     */   private AbstractMonster getRandomGremlin(int slot) {
/*  70 */     ArrayList<String> pool = new ArrayList<>();
/*  71 */     pool.add("GremlinWarrior");
/*  72 */     pool.add("GremlinWarrior");
/*  73 */     pool.add("GremlinThief");
/*  74 */     pool.add("GremlinThief");
/*  75 */     pool.add("GremlinFat");
/*  76 */     pool.add("GremlinFat");
/*  77 */     pool.add("GremlinTsundere");
/*  78 */     pool.add("GremlinWizard");
/*     */ 
/*     */     
/*  81 */     switch (slot)
/*     */     { case 0:
/*  83 */         x = GremlinLeader.POSX[0];
/*  84 */         y = GremlinLeader.POSY[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 100 */         return MonsterHelper.getGremlin(pool.get(AbstractDungeon.aiRng.random(0, pool.size() - 1)), x, y);case 1: x = GremlinLeader.POSX[1]; y = GremlinLeader.POSY[1]; return MonsterHelper.getGremlin(pool.get(AbstractDungeon.aiRng.random(0, pool.size() - 1)), x, y);case 2: x = GremlinLeader.POSX[2]; y = GremlinLeader.POSY[2]; return MonsterHelper.getGremlin(pool.get(AbstractDungeon.aiRng.random(0, pool.size() - 1)), x, y); }  float x = GremlinLeader.POSX[0]; float y = GremlinLeader.POSY[0]; return MonsterHelper.getGremlin(pool.get(AbstractDungeon.aiRng.random(0, pool.size() - 1)), x, y);
/*     */   }
/*     */   
/*     */   private int getSmartPosition() {
/* 104 */     int position = 0;
/* 105 */     for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
/* 106 */       if (this.m.drawX > mo.drawX) {
/* 107 */         position++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 112 */     return position;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/* 117 */     if (this.duration == this.startDuration) {
/* 118 */       this.m.animX = 1200.0F * Settings.xScale;
/* 119 */       this.m.init();
/* 120 */       this.m.applyPowers();
/* 121 */       (AbstractDungeon.getCurrRoom()).monsters.addMonster(getSmartPosition(), this.m);
/*     */       
/* 123 */       if (ModHelper.isModEnabled("Lethality")) {
/* 124 */         addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this.m, (AbstractCreature)this.m, (AbstractPower)new StrengthPower((AbstractCreature)this.m, 3), 3));
/*     */       }
/*     */       
/* 127 */       if (ModHelper.isModEnabled("Time Dilation")) {
/* 128 */         addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this.m, (AbstractCreature)this.m, (AbstractPower)new SlowPower((AbstractCreature)this.m, 0)));
/*     */       }
/*     */       
/* 131 */       addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this.m, (AbstractCreature)this.m, (AbstractPower)new MinionPower((AbstractCreature)this.m)));
/*     */     } 
/*     */     
/* 134 */     tickDuration();
/*     */     
/* 136 */     if (this.isDone) {
/* 137 */       this.m.animX = 0.0F;
/* 138 */       this.m.showHealthBar();
/* 139 */       this.m.usePreBattleAction();
/*     */     } else {
/* 141 */       this.m.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\action\\unique\SummonGremlinAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */