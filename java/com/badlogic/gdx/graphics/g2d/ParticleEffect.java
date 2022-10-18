/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Writer;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ public class ParticleEffect
/*     */   implements Disposable
/*     */ {
/*     */   private final Array<ParticleEmitter> emitters;
/*     */   private BoundingBox bounds;
/*     */   private boolean ownsTexture;
/*     */   
/*     */   public ParticleEffect() {
/*  43 */     this.emitters = new Array(8);
/*     */   }
/*     */   
/*     */   public ParticleEffect(ParticleEffect effect) {
/*  47 */     this.emitters = new Array(true, effect.emitters.size);
/*  48 */     for (int i = 0, n = effect.emitters.size; i < n; i++)
/*  49 */       this.emitters.add(new ParticleEmitter((ParticleEmitter)effect.emitters.get(i))); 
/*     */   }
/*     */   
/*     */   public void start() {
/*  53 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/*  54 */       ((ParticleEmitter)this.emitters.get(i)).start(); 
/*     */   }
/*     */   
/*     */   public void reset() {
/*  58 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/*  59 */       ((ParticleEmitter)this.emitters.get(i)).reset(); 
/*     */   }
/*     */   
/*     */   public void update(float delta) {
/*  63 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/*  64 */       ((ParticleEmitter)this.emitters.get(i)).update(delta); 
/*     */   }
/*     */   
/*     */   public void draw(Batch spriteBatch) {
/*  68 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/*  69 */       ((ParticleEmitter)this.emitters.get(i)).draw(spriteBatch); 
/*     */   }
/*     */   
/*     */   public void draw(Batch spriteBatch, float delta) {
/*  73 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/*  74 */       ((ParticleEmitter)this.emitters.get(i)).draw(spriteBatch, delta); 
/*     */   }
/*     */   
/*     */   public void allowCompletion() {
/*  78 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/*  79 */       ((ParticleEmitter)this.emitters.get(i)).allowCompletion(); 
/*     */   }
/*     */   
/*     */   public boolean isComplete() {
/*  83 */     for (int i = 0, n = this.emitters.size; i < n; i++) {
/*  84 */       ParticleEmitter emitter = (ParticleEmitter)this.emitters.get(i);
/*  85 */       if (!emitter.isComplete()) return false; 
/*     */     } 
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public void setDuration(int duration) {
/*  91 */     for (int i = 0, n = this.emitters.size; i < n; i++) {
/*  92 */       ParticleEmitter emitter = (ParticleEmitter)this.emitters.get(i);
/*  93 */       emitter.setContinuous(false);
/*  94 */       emitter.duration = duration;
/*  95 */       emitter.durationTimer = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 100 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/* 101 */       ((ParticleEmitter)this.emitters.get(i)).setPosition(x, y); 
/*     */   }
/*     */   
/*     */   public void setFlip(boolean flipX, boolean flipY) {
/* 105 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/* 106 */       ((ParticleEmitter)this.emitters.get(i)).setFlip(flipX, flipY); 
/*     */   }
/*     */   
/*     */   public void flipY() {
/* 110 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/* 111 */       ((ParticleEmitter)this.emitters.get(i)).flipY(); 
/*     */   }
/*     */   
/*     */   public Array<ParticleEmitter> getEmitters() {
/* 115 */     return this.emitters;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleEmitter findEmitter(String name) {
/* 120 */     for (int i = 0, n = this.emitters.size; i < n; i++) {
/* 121 */       ParticleEmitter emitter = (ParticleEmitter)this.emitters.get(i);
/* 122 */       if (emitter.getName().equals(name)) return emitter; 
/*     */     } 
/* 124 */     return null;
/*     */   }
/*     */   
/*     */   public void save(Writer output) throws IOException {
/* 128 */     int index = 0;
/* 129 */     for (int i = 0, n = this.emitters.size; i < n; i++) {
/* 130 */       ParticleEmitter emitter = (ParticleEmitter)this.emitters.get(i);
/* 131 */       if (index++ > 0) output.write("\n\n"); 
/* 132 */       emitter.save(output);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void load(FileHandle effectFile, FileHandle imagesDir) {
/* 137 */     loadEmitters(effectFile);
/* 138 */     loadEmitterImages(imagesDir);
/*     */   }
/*     */   
/*     */   public void load(FileHandle effectFile, TextureAtlas atlas) {
/* 142 */     load(effectFile, atlas, null);
/*     */   }
/*     */   
/*     */   public void load(FileHandle effectFile, TextureAtlas atlas, String atlasPrefix) {
/* 146 */     loadEmitters(effectFile);
/* 147 */     loadEmitterImages(atlas, atlasPrefix);
/*     */   }
/*     */   
/*     */   public void loadEmitters(FileHandle effectFile) {
/* 151 */     InputStream input = effectFile.read();
/* 152 */     this.emitters.clear();
/* 153 */     BufferedReader reader = null;
/*     */     try {
/* 155 */       reader = new BufferedReader(new InputStreamReader(input), 512);
/*     */       do {
/* 157 */         ParticleEmitter emitter = new ParticleEmitter(reader);
/* 158 */         this.emitters.add(emitter);
/* 159 */       } while (reader.readLine() != null && 
/* 160 */         reader.readLine() != null);
/*     */     }
/* 162 */     catch (IOException ex) {
/* 163 */       throw new GdxRuntimeException("Error loading effect: " + effectFile, ex);
/*     */     } finally {
/* 165 */       StreamUtils.closeQuietly(reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadEmitterImages(TextureAtlas atlas) {
/* 170 */     loadEmitterImages(atlas, null);
/*     */   }
/*     */   
/*     */   public void loadEmitterImages(TextureAtlas atlas, String atlasPrefix) {
/* 174 */     for (int i = 0, n = this.emitters.size; i < n; i++) {
/* 175 */       ParticleEmitter emitter = (ParticleEmitter)this.emitters.get(i);
/* 176 */       String imagePath = emitter.getImagePath();
/* 177 */       if (imagePath != null) {
/* 178 */         String imageName = (new File(imagePath.replace('\\', '/'))).getName();
/* 179 */         int lastDotIndex = imageName.lastIndexOf('.');
/* 180 */         if (lastDotIndex != -1) imageName = imageName.substring(0, lastDotIndex); 
/* 181 */         if (atlasPrefix != null) imageName = atlasPrefix + imageName; 
/* 182 */         Sprite sprite = atlas.createSprite(imageName);
/* 183 */         if (sprite == null) throw new IllegalArgumentException("SpriteSheet missing image: " + imageName); 
/* 184 */         emitter.setSprite(sprite);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void loadEmitterImages(FileHandle imagesDir) {
/* 189 */     this.ownsTexture = true;
/* 190 */     HashMap<String, Sprite> loadedSprites = new HashMap<String, Sprite>(this.emitters.size);
/* 191 */     for (int i = 0, n = this.emitters.size; i < n; i++) {
/* 192 */       ParticleEmitter emitter = (ParticleEmitter)this.emitters.get(i);
/* 193 */       String imagePath = emitter.getImagePath();
/* 194 */       if (imagePath != null) {
/* 195 */         String imageName = (new File(imagePath.replace('\\', '/'))).getName();
/* 196 */         Sprite sprite = loadedSprites.get(imageName);
/* 197 */         if (sprite == null) {
/* 198 */           sprite = new Sprite(loadTexture(imagesDir.child(imageName)));
/* 199 */           loadedSprites.put(imageName, sprite);
/*     */         } 
/* 201 */         emitter.setSprite(sprite);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   protected Texture loadTexture(FileHandle file) {
/* 206 */     return new Texture(file, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 211 */     if (!this.ownsTexture)
/* 212 */       return;  for (int i = 0, n = this.emitters.size; i < n; i++) {
/* 213 */       ParticleEmitter emitter = (ParticleEmitter)this.emitters.get(i);
/* 214 */       emitter.getSprite().getTexture().dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 220 */     if (this.bounds == null) this.bounds = new BoundingBox();
/*     */     
/* 222 */     BoundingBox bounds = this.bounds;
/* 223 */     bounds.inf();
/* 224 */     for (ParticleEmitter emitter : this.emitters)
/* 225 */       bounds.ext(emitter.getBoundingBox()); 
/* 226 */     return bounds;
/*     */   }
/*     */   
/*     */   public void scaleEffect(float scaleFactor) {
/* 230 */     for (ParticleEmitter particleEmitter : this.emitters) {
/* 231 */       particleEmitter.getScale().setHigh(particleEmitter.getScale().getHighMin() * scaleFactor, particleEmitter
/* 232 */           .getScale().getHighMax() * scaleFactor);
/* 233 */       particleEmitter.getScale().setLow(particleEmitter.getScale().getLowMin() * scaleFactor, particleEmitter
/* 234 */           .getScale().getLowMax() * scaleFactor);
/*     */       
/* 236 */       particleEmitter.getVelocity().setHigh(particleEmitter.getVelocity().getHighMin() * scaleFactor, particleEmitter
/* 237 */           .getVelocity().getHighMax() * scaleFactor);
/* 238 */       particleEmitter.getVelocity().setLow(particleEmitter.getVelocity().getLowMin() * scaleFactor, particleEmitter
/* 239 */           .getVelocity().getLowMax() * scaleFactor);
/*     */       
/* 241 */       particleEmitter.getGravity().setHigh(particleEmitter.getGravity().getHighMin() * scaleFactor, particleEmitter
/* 242 */           .getGravity().getHighMax() * scaleFactor);
/* 243 */       particleEmitter.getGravity().setLow(particleEmitter.getGravity().getLowMin() * scaleFactor, particleEmitter
/* 244 */           .getGravity().getLowMax() * scaleFactor);
/*     */       
/* 246 */       particleEmitter.getWind().setHigh(particleEmitter.getWind().getHighMin() * scaleFactor, particleEmitter
/* 247 */           .getWind().getHighMax() * scaleFactor);
/* 248 */       particleEmitter.getWind().setLow(particleEmitter.getWind().getLowMin() * scaleFactor, particleEmitter
/* 249 */           .getWind().getLowMax() * scaleFactor);
/*     */       
/* 251 */       particleEmitter.getSpawnWidth().setHigh(particleEmitter.getSpawnWidth().getHighMin() * scaleFactor, particleEmitter
/* 252 */           .getSpawnWidth().getHighMax() * scaleFactor);
/* 253 */       particleEmitter.getSpawnWidth().setLow(particleEmitter.getSpawnWidth().getLowMin() * scaleFactor, particleEmitter
/* 254 */           .getSpawnWidth().getLowMax() * scaleFactor);
/*     */       
/* 256 */       particleEmitter.getSpawnHeight().setHigh(particleEmitter.getSpawnHeight().getHighMin() * scaleFactor, particleEmitter
/* 257 */           .getSpawnHeight().getHighMax() * scaleFactor);
/* 258 */       particleEmitter.getSpawnHeight().setLow(particleEmitter.getSpawnHeight().getLowMin() * scaleFactor, particleEmitter
/* 259 */           .getSpawnHeight().getLowMax() * scaleFactor);
/*     */       
/* 261 */       particleEmitter.getXOffsetValue().setLow(particleEmitter.getXOffsetValue().getLowMin() * scaleFactor, particleEmitter
/* 262 */           .getXOffsetValue().getLowMax() * scaleFactor);
/*     */       
/* 264 */       particleEmitter.getYOffsetValue().setLow(particleEmitter.getYOffsetValue().getLowMin() * scaleFactor, particleEmitter
/* 265 */           .getYOffsetValue().getLowMax() * scaleFactor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmittersCleanUpBlendFunction(boolean cleanUpBlendFunction) {
/* 276 */     for (int i = 0, n = this.emitters.size; i < n; i++)
/* 277 */       ((ParticleEmitter)this.emitters.get(i)).setCleansUpBlendFunction(cleanUpBlendFunction); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\ParticleEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */