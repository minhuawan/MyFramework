/*     */ package com.megacrit.cardcrawl.ui;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*     */ 
/*     */ public class SpeechWord {
/*     */   private BitmapFont font;
/*     */   private DialogWord.WordEffect effect;
/*     */   private DialogWord.WordColor wColor;
/*     */   public String word;
/*     */   private float x;
/*     */   private float y;
/*     */   private float target_x;
/*  20 */   public int line = 0; private float target_y; private float offset_x;
/*     */   private float offset_y;
/*  22 */   private float timer = 0.0F; private Color color;
/*     */   private Color targetColor;
/*  24 */   private float scale = 1.0F; private float targetScale = 1.0F;
/*  25 */   private static final float BUMP_OFFSET = 20.0F * Settings.scale;
/*     */   private static GlyphLayout gl;
/*     */   private static final float COLOR_LERP_SPEED = 8.0F;
/*  28 */   private static final float SHAKE_AMT = 2.0F * Settings.scale;
/*  29 */   private static final float DIALOG_FADE_Y = 50.0F * Settings.scale;
/*     */ 
/*     */   
/*  32 */   private static final float WAVY_DIST = 3.0F * Settings.scale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float SHAKE_INTERVAL = 0.02F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpeechWord(BitmapFont font, String word, DialogWord.AppearEffect a_effect, DialogWord.WordEffect effect, DialogWord.WordColor wColor, float x, float y, int line) {
/*  45 */     if (gl == null) {
/*  46 */       gl = new GlyphLayout();
/*     */     }
/*     */     
/*  49 */     this.font = font;
/*  50 */     this.effect = effect;
/*  51 */     this.wColor = wColor;
/*  52 */     this.word = word;
/*  53 */     this.x = x;
/*  54 */     this.y = y;
/*  55 */     this.target_x = x;
/*  56 */     this.target_y = y;
/*  57 */     this.targetColor = getColor();
/*  58 */     this.line = line;
/*  59 */     this.color = new Color(this.targetColor.r, this.targetColor.g, this.targetColor.b, 0.0F);
/*     */     
/*  61 */     if (effect == DialogWord.WordEffect.WAVY) {
/*  62 */       this.timer = MathUtils.random(1.5707964F);
/*     */     }
/*     */     
/*  65 */     switch (a_effect) {
/*     */ 
/*     */       
/*     */       case WAVY:
/*  69 */         this.y -= BUMP_OFFSET;
/*  70 */         this.scale = 0.0F;
/*     */         break;
/*     */       case SLOW_WAVY:
/*  73 */         this.y -= BUMP_OFFSET;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Color getColor() {
/*  82 */     switch (this.wColor) {
/*     */       case SHAKY:
/*  84 */         return new Color(1.0F, 0.2F, 0.3F, 1.0F);
/*     */       case WAVY:
/*  86 */         return new Color(0.3F, 1.0F, 0.1F, 1.0F);
/*     */       case SLOW_WAVY:
/*  88 */         return Settings.BLUE_TEXT_COLOR.cpy();
/*     */       case null:
/*  90 */         return Settings.GOLD_COLOR.cpy();
/*     */       case null:
/*  92 */         return Settings.CREAM_COLOR.cpy();
/*     */     } 
/*  94 */     return Color.DARK_GRAY.cpy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/* 100 */     if (this.x != this.target_x) {
/* 101 */       this.x = MathUtils.lerp(this.x, this.target_x, Gdx.graphics.getDeltaTime() * 12.0F);
/*     */     }
/* 103 */     if (this.y != this.target_y) {
/* 104 */       this.y = MathUtils.lerp(this.y, this.target_y, Gdx.graphics.getDeltaTime() * 12.0F);
/*     */     }
/*     */ 
/*     */     
/* 108 */     this.color = this.color.lerp(this.targetColor, Gdx.graphics.getDeltaTime() * 8.0F);
/*     */ 
/*     */     
/* 111 */     if (this.scale != this.targetScale) {
/* 112 */       this.scale = MathHelper.scaleLerpSnap(this.scale, this.targetScale);
/*     */     }
/*     */     
/* 115 */     applyEffects();
/*     */   }
/*     */   
/*     */   private void applyEffects() {
/* 119 */     switch (this.effect) {
/*     */       case SHAKY:
/* 121 */         this.timer -= Gdx.graphics.getDeltaTime();
/* 122 */         if (this.timer < 0.0F) {
/* 123 */           this.offset_x = MathUtils.random(-SHAKE_AMT, SHAKE_AMT);
/* 124 */           this.offset_y = MathUtils.random(-SHAKE_AMT, SHAKE_AMT);
/* 125 */           this.timer = 0.02F;
/*     */         } 
/*     */         break;
/*     */       case WAVY:
/* 129 */         this.timer += Gdx.graphics.getDeltaTime() * 5.0F;
/*     */         break;
/*     */       case SLOW_WAVY:
/* 132 */         this.timer += Gdx.graphics.getDeltaTime() * 2.5F;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fadeOut() {
/* 141 */     this.targetColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
/*     */   }
/*     */   
/*     */   public void dialogFadeOut() {
/* 145 */     this.targetColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
/* 146 */     this.target_y -= DIALOG_FADE_Y;
/*     */   }
/*     */   
/*     */   public void shiftY(float shiftAmount) {
/* 150 */     this.target_y += shiftAmount;
/*     */   }
/*     */   
/*     */   public void shiftX(float shiftAmount) {
/* 154 */     this.target_x += shiftAmount;
/*     */   }
/*     */   
/*     */   public void setX(float newX) {
/* 158 */     this.target_x = newX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DialogWord.WordEffect identifyWordEffect(String word) {
/* 168 */     if (word.length() > 2) {
/* 169 */       if (word.charAt(0) == '@' && word.charAt(word.length() - 1) == '@')
/* 170 */         return DialogWord.WordEffect.SHAKY; 
/* 171 */       if (word.charAt(0) == '~' && word.charAt(word.length() - 1) == '~') {
/* 172 */         return DialogWord.WordEffect.WAVY;
/*     */       }
/*     */     } 
/*     */     
/* 176 */     return DialogWord.WordEffect.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DialogWord.WordColor identifyWordColor(String word)
/*     */   {
/* 186 */     if (word.charAt(0) == '#') {
/* 187 */       switch (word.charAt(1)) {
/*     */         case 'r':
/* 189 */           return DialogWord.WordColor.RED;
/*     */         case 'g':
/* 191 */           return DialogWord.WordColor.GREEN;
/*     */         case 'b':
/* 193 */           return DialogWord.WordColor.BLUE;
/*     */         case 'y':
/* 195 */           return DialogWord.WordColor.GOLD;
/*     */       } 
/*     */     
/*     */     }
/* 199 */     return DialogWord.WordColor.DEFAULT; } public void render(SpriteBatch sb) { float charOffset; int j;
/*     */     float charOffset3;
/*     */     int j3;
/*     */     float charOffset2;
/* 203 */     this.font.setColor(this.color);
/* 204 */     this.font.getData().setScale(this.scale);
/*     */     
/* 206 */     switch (this.effect) {
/*     */       case WAVY:
/* 208 */         charOffset = 0.0F;
/* 209 */         j = 0;
/*     */         
/* 211 */         for (char c : this.word.toCharArray()) {
/* 212 */           String i = Character.toString(c);
/* 213 */           gl.setText(this.font, i);
/* 214 */           this.font.draw((Batch)sb, i, this.x + this.offset_x + charOffset, this.y + 
/*     */ 
/*     */ 
/*     */               
/* 218 */               MathUtils.cosDeg((float)((System.currentTimeMillis() + (j * 70)) / 4L % 360L)) * WAVY_DIST);
/* 219 */           charOffset += gl.width;
/* 220 */           j++;
/*     */         } 
/*     */         break;
/*     */       case SLOW_WAVY:
/* 224 */         charOffset3 = 0.0F;
/* 225 */         j3 = 0;
/*     */         
/* 227 */         for (char c : this.word.toCharArray()) {
/* 228 */           String i = Character.toString(c);
/* 229 */           gl.setText(this.font, i);
/* 230 */           this.font.draw((Batch)sb, i, this.x + this.offset_x + charOffset3, this.y + 
/*     */ 
/*     */ 
/*     */               
/* 234 */               MathUtils.cosDeg((float)((System.currentTimeMillis() + (j3 * 70)) / 4L % 360L)) * WAVY_DIST / 2.0F);
/* 235 */           charOffset3 += gl.width;
/* 236 */           j3++;
/*     */         } 
/*     */         break;
/*     */       case SHAKY:
/* 240 */         charOffset2 = 0.0F;
/* 241 */         for (char c : this.word.toCharArray()) {
/* 242 */           String i = Character.toString(c);
/* 243 */           gl.setText(this.font, i);
/* 244 */           this.font.draw((Batch)sb, i, this.x + 
/*     */ 
/*     */               
/* 247 */               MathUtils.random(-2.0F, 2.0F) * Settings.scale + charOffset2, this.y + 
/* 248 */               MathUtils.random(-2.0F, 2.0F) * Settings.scale);
/* 249 */           charOffset2 += gl.width;
/*     */         } 
/*     */         break;
/*     */       default:
/* 253 */         this.font.draw((Batch)sb, this.word, this.x + this.offset_x, this.y + this.offset_y);
/*     */         break;
/*     */     } 
/*     */     
/* 257 */     this.font.getData().setScale(1.0F); }
/*     */ 
/*     */   
/*     */   public void render(SpriteBatch sb, float y2) {
/* 261 */     this.font.setColor(this.color);
/* 262 */     this.font.getData().setScale(this.scale);
/* 263 */     this.font.draw((Batch)sb, this.word, this.x + this.offset_x, this.y + this.offset_y + y2);
/* 264 */     this.font.getData().setScale(1.0F);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcraw\\ui\SpeechWord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */