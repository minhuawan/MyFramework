/*     */ package com.megacrit.cardcrawl.vfx;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*     */ import com.megacrit.cardcrawl.ui.DialogWord;
/*     */ 
/*     */ public class SpeechBubble extends AbstractGameEffect {
/*     */   private static final int RAW_W = 512;
/*  15 */   private static final float SHADOW_OFFSET = 16.0F * Settings.scale;
/*  16 */   private static final float WAVY_DISTANCE = 2.0F * Settings.scale;
/*  17 */   private static final float ADJUST_X = 170.0F * Settings.scale;
/*  18 */   private static final float ADJUST_Y = 116.0F * Settings.scale;
/*     */   
/*     */   private static final float FADE_TIME = 0.3F;
/*  21 */   private float shadow_offset = 0.0F;
/*     */   private float x;
/*     */   private float y;
/*  24 */   private float scaleTimer = 0.3F; private float wavy_y; private float wavyHelper;
/*     */   private boolean facingRight;
/*  26 */   private Color shadowColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
/*     */   
/*     */   public SpeechBubble(float x, float y, String msg, boolean isPlayer) {
/*  29 */     this(x, y, 2.0F, msg, isPlayer);
/*     */   }
/*     */   
/*     */   public SpeechBubble(float x, float y, float duration, String msg, boolean isPlayer) {
/*  33 */     float effect_x = -170.0F * Settings.scale;
/*  34 */     if (isPlayer) {
/*  35 */       effect_x = 170.0F * Settings.scale;
/*     */     }
/*     */     
/*  38 */     AbstractDungeon.effectsQueue.add(new SpeechTextEffect(x + effect_x, y + 124.0F * Settings.scale, duration, msg, DialogWord.AppearEffect.BUMP_IN));
/*     */ 
/*     */     
/*  41 */     if (isPlayer) {
/*  42 */       this.x = x + ADJUST_X;
/*     */     } else {
/*  44 */       this.x = x - ADJUST_X;
/*     */     } 
/*  46 */     this.y = y + ADJUST_Y;
/*  47 */     this.color = new Color(0.8F, 0.9F, 0.9F, 0.0F);
/*  48 */     this.duration = duration;
/*  49 */     this.facingRight = !isPlayer;
/*     */   }
/*     */   
/*     */   public void update() {
/*  53 */     updateScale();
/*     */     
/*  55 */     this.wavyHelper += Gdx.graphics.getDeltaTime() * 4.0F;
/*  56 */     this.wavy_y = MathUtils.sin(this.wavyHelper) * WAVY_DISTANCE;
/*     */     
/*  58 */     this.duration -= Gdx.graphics.getDeltaTime();
/*  59 */     if (this.duration < 0.0F) {
/*  60 */       this.isDone = true;
/*     */     }
/*     */     
/*  63 */     if (this.duration > 0.3F) {
/*  64 */       this.color.a = MathUtils.lerp(this.color.a, 1.0F, Gdx.graphics.getDeltaTime() * 12.0F);
/*     */     } else {
/*  66 */       this.color.a = MathUtils.lerp(this.color.a, 0.0F, Gdx.graphics.getDeltaTime() * 12.0F);
/*     */     } 
/*  68 */     this.shadow_offset = MathUtils.lerp(this.shadow_offset, SHADOW_OFFSET, Gdx.graphics.getDeltaTime() * 4.0F);
/*     */   }
/*     */   
/*     */   private void updateScale() {
/*  72 */     this.scaleTimer -= Gdx.graphics.getDeltaTime();
/*  73 */     if (this.scaleTimer < 0.0F) {
/*  74 */       this.scaleTimer = 0.0F;
/*     */     }
/*     */     
/*  77 */     if (Settings.isMobile) {
/*  78 */       this.scale = Interpolation.swingIn.apply(Settings.scale * 1.15F, Settings.scale / 2.0F, this.scaleTimer / 0.3F);
/*     */     } else {
/*  80 */       this.scale = Interpolation.swingIn.apply(Settings.scale, Settings.scale / 2.0F, this.scaleTimer / 0.3F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(SpriteBatch sb) {
/*  86 */     this.color.a /= 4.0F;
/*  87 */     sb.setColor(this.shadowColor);
/*  88 */     sb.draw(ImageMaster.SPEECH_BUBBLE_IMG, this.x - 256.0F + this.shadow_offset, this.y - 256.0F + this.wavy_y - this.shadow_offset, 256.0F, 256.0F, 512.0F, 512.0F, this.scale, this.scale, this.rotation, 0, 0, 512, 512, this.facingRight, false);
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
/* 106 */     sb.setColor(this.color);
/* 107 */     sb.draw(ImageMaster.SPEECH_BUBBLE_IMG, this.x - 256.0F, this.y - 256.0F + this.wavy_y, 256.0F, 256.0F, 512.0F, 512.0F, this.scale, this.scale, this.rotation, 0, 0, 512, 512, this.facingRight, false);
/*     */   }
/*     */   
/*     */   public void dispose() {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\vfx\SpeechBubble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */