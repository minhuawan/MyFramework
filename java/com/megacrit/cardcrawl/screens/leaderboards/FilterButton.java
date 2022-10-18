/*     */ package com.megacrit.cardcrawl.screens.leaderboards;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.helpers.Hitbox;
/*     */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*     */ import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
/*     */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*     */ import com.megacrit.cardcrawl.localization.UIStrings;
/*     */ 
/*     */ public class FilterButton
/*     */ {
/*  18 */   private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("LeaderboardFilters");
/*  19 */   public static final String[] TEXT = uiStrings.TEXT;
/*  20 */   public LeaderboardType lType = null;
/*  21 */   public RegionSetting rType = null;
/*     */   public boolean active = false;
/*  23 */   public AbstractPlayer.PlayerClass pClass = null;
/*     */   private Texture img;
/*     */   private static final int W = 128;
/*  26 */   public Hitbox hb = new Hitbox(100.0F * Settings.scale, 100.0F * Settings.scale);
/*     */   public String label;
/*     */   
/*     */   public enum LeaderboardType {
/*  30 */     HIGH_SCORE, FASTEST_WIN, CONSECUTIVE_WINS, AVG_FLOOR, AVG_SCORE, SPIRE_LEVEL;
/*     */   }
/*     */   
/*     */   public enum RegionSetting {
/*  34 */     GLOBAL, FRIEND;
/*     */   }
/*     */   
/*     */   public FilterButton(String imgUrl, boolean active, AbstractPlayer.PlayerClass pClass, LeaderboardType lType, RegionSetting rType) {
/*  38 */     if (pClass != null) {
/*  39 */       switch (pClass) {
/*     */         case FRIEND:
/*  41 */           this.img = ImageMaster.FILTER_IRONCLAD;
/*     */           break;
/*     */         case GLOBAL:
/*  44 */           this.img = ImageMaster.FILTER_SILENT;
/*     */           break;
/*     */         case null:
/*  47 */           this.img = ImageMaster.FILTER_DEFECT;
/*     */           break;
/*     */         case null:
/*  50 */           this.img = ImageMaster.FILTER_WATCHER;
/*     */           break;
/*     */         default:
/*  53 */           this.img = ImageMaster.FILTER_IRONCLAD;
/*     */           break;
/*     */       } 
/*  56 */     } else if (lType != null) {
/*  57 */       switch (lType) {
/*     */         case FRIEND:
/*  59 */           this.img = ImageMaster.FILTER_CHAIN;
/*     */           break;
/*     */         case GLOBAL:
/*  62 */           this.img = ImageMaster.FILTER_TIME;
/*     */           break;
/*     */         case null:
/*  65 */           this.img = ImageMaster.FILTER_SCORE;
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/*  71 */           this.img = ImageMaster.FILTER_CHAIN;
/*     */           break;
/*     */       } 
/*     */     
/*  75 */     } else if (rType != null) {
/*  76 */       switch (rType) {
/*     */         case FRIEND:
/*  78 */           this.img = ImageMaster.FILTER_FRIENDS;
/*     */           break;
/*     */         
/*     */         default:
/*  82 */           this.img = ImageMaster.FILTER_GLOBAL;
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/*  87 */     this.lType = lType;
/*  88 */     this.rType = rType;
/*  89 */     this.active = active;
/*  90 */     this.pClass = pClass;
/*     */   }
/*     */   
/*     */   public FilterButton(String imgUrl, boolean active, AbstractPlayer.PlayerClass pClass) {
/*  94 */     this(imgUrl, active, pClass, null, null);
/*     */     
/*  96 */     switch (pClass) {
/*     */       case FRIEND:
/*  98 */         this.label = TEXT[0];
/*     */         break;
/*     */       case GLOBAL:
/* 101 */         this.label = TEXT[1];
/*     */         break;
/*     */       case null:
/* 104 */         this.label = TEXT[2];
/*     */         break;
/*     */       case null:
/* 107 */         this.label = TEXT[11];
/*     */         break;
/*     */       default:
/* 110 */         this.label = TEXT[0];
/*     */         break;
/*     */     } 
/*     */     
/* 114 */     if (active) {
/* 115 */       CardCrawlGame.mainMenuScreen.leaderboardsScreen.charLabel = LeaderboardScreen.TEXT[2] + ":  " + this.label;
/*     */     }
/*     */   }
/*     */   
/*     */   public FilterButton(String imgUrl, boolean active, LeaderboardType lType) {
/* 120 */     this(imgUrl, active, null, lType, null);
/*     */     
/* 122 */     switch (lType) {
/*     */       case null:
/* 124 */         this.label = TEXT[3];
/*     */         break;
/*     */       case null:
/* 127 */         this.label = TEXT[4];
/*     */         break;
/*     */       case FRIEND:
/* 130 */         this.label = TEXT[5];
/*     */         break;
/*     */       case GLOBAL:
/* 133 */         this.label = TEXT[6];
/*     */         break;
/*     */       case null:
/* 136 */         this.label = TEXT[7];
/*     */         break;
/*     */       case null:
/* 139 */         this.label = TEXT[8];
/*     */         break;
/*     */       default:
/* 142 */         this.label = TEXT[7];
/*     */         break;
/*     */     } 
/*     */     
/* 146 */     if (active) {
/* 147 */       CardCrawlGame.mainMenuScreen.leaderboardsScreen.typeLabel = LeaderboardScreen.TEXT[4] + ":  " + this.label;
/*     */     }
/*     */   }
/*     */   
/*     */   public FilterButton(String imgUrl, boolean active, RegionSetting rType) {
/* 152 */     this(imgUrl, active, null, null, rType);
/*     */     
/* 154 */     switch (rType) {
/*     */       case FRIEND:
/* 156 */         this.label = TEXT[9];
/*     */         break;
/*     */       case GLOBAL:
/* 159 */         this.label = TEXT[10];
/*     */         break;
/*     */       default:
/* 162 */         this.label = TEXT[9];
/*     */         break;
/*     */     } 
/*     */     
/* 166 */     if (active) {
/* 167 */       CardCrawlGame.mainMenuScreen.leaderboardsScreen.regionLabel = LeaderboardScreen.TEXT[3] + ":  " + this.label;
/*     */     }
/*     */   }
/*     */   
/*     */   public void update() {
/* 172 */     this.hb.update();
/* 173 */     if (this.hb.justHovered && !this.active) {
/* 174 */       CardCrawlGame.sound.play("UI_HOVER");
/*     */     }
/*     */     
/* 177 */     if (Settings.isControllerMode) {
/* 178 */       if (!this.active && this.hb.hovered && CInputActionSet.select.isJustPressed()) {
/* 179 */         CInputActionSet.select.unpress();
/* 180 */         this.hb.clicked = true;
/*     */       }
/*     */     
/* 183 */     } else if (!this.active && this.hb.hovered && InputHelper.justClickedLeft && !CardCrawlGame.mainMenuScreen.leaderboardsScreen.waiting) {
/*     */       
/* 185 */       CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
/* 186 */       this.hb.clickStarted = true;
/*     */     } 
/*     */ 
/*     */     
/* 190 */     if (this.hb.clicked) {
/* 191 */       this.hb.clicked = false;
/* 192 */       if (!this.active) {
/* 193 */         toggle(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void toggle(boolean refresh) {
/* 202 */     this.active = true;
/* 203 */     CardCrawlGame.mainMenuScreen.leaderboardsScreen.refresh = true;
/*     */     
/* 205 */     if (this.pClass != null) {
/* 206 */       for (FilterButton b : CardCrawlGame.mainMenuScreen.leaderboardsScreen.charButtons) {
/* 207 */         if (b != this) {
/* 208 */           b.active = false;
/*     */         }
/*     */       } 
/* 211 */       CardCrawlGame.mainMenuScreen.leaderboardsScreen.charLabel = LeaderboardScreen.TEXT[2] + ":  " + this.label;
/*     */       
/*     */       return;
/*     */     } 
/* 215 */     if (this.rType != null) {
/* 216 */       for (FilterButton b : CardCrawlGame.mainMenuScreen.leaderboardsScreen.regionButtons) {
/* 217 */         if (b != this) {
/* 218 */           b.active = false;
/*     */         }
/*     */       } 
/* 221 */       CardCrawlGame.mainMenuScreen.leaderboardsScreen.regionLabel = LeaderboardScreen.TEXT[3] + ":  " + this.label;
/*     */       
/*     */       return;
/*     */     } 
/* 225 */     if (this.lType != null) {
/* 226 */       for (FilterButton b : CardCrawlGame.mainMenuScreen.leaderboardsScreen.typeButtons) {
/* 227 */         if (b != this) {
/* 228 */           b.active = false;
/*     */         }
/*     */       } 
/* 231 */       CardCrawlGame.mainMenuScreen.leaderboardsScreen.typeLabel = LeaderboardScreen.TEXT[4] + ":  " + this.label;
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void render(SpriteBatch sb, float x, float y) {
/* 237 */     if (this.active) {
/* 238 */       sb.setColor(new Color(1.0F, 0.8F, 0.2F, 0.5F + (
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 243 */             MathUtils.cosDeg((float)(System.currentTimeMillis() / 4L % 360L)) + 1.25F) / 5.0F));
/* 244 */       sb.draw(ImageMaster.FILTER_GLOW_BG, x - 64.0F, y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
/*     */     } 
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
/*     */ 
/*     */     
/* 263 */     if (this.hb.hovered || this.active) {
/* 264 */       sb.setColor(Color.WHITE);
/* 265 */       sb.draw(this.img, x - 64.0F, y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 283 */       sb.setColor(Color.GRAY);
/* 284 */       sb.draw(this.img, x - 64.0F, y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
/*     */     } 
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
/*     */ 
/*     */     
/* 303 */     if (this.hb.hovered) {
/* 304 */       sb.setBlendFunction(770, 1);
/* 305 */       sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.25F));
/* 306 */       sb.draw(this.img, x - 64.0F, y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
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
/*     */       
/* 323 */       sb.setBlendFunction(770, 771);
/*     */     } 
/*     */     
/* 326 */     this.hb.move(x, y);
/* 327 */     this.hb.render(sb);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\screens\leaderboards\FilterButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */