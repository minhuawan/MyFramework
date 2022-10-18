/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
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
/*     */ public abstract class RegionInfluencer
/*     */   extends Influencer
/*     */ {
/*     */   public Array<AspectTextureRegion> regions;
/*     */   ParallelArray.FloatChannel regionChannel;
/*     */   
/*     */   public static class Single
/*     */     extends RegionInfluencer
/*     */   {
/*     */     public Single() {}
/*     */     
/*     */     public Single(Single regionInfluencer) {
/*  37 */       super(regionInfluencer);
/*     */     }
/*     */     
/*     */     public Single(TextureRegion textureRegion) {
/*  41 */       super(new TextureRegion[] { textureRegion });
/*     */     }
/*     */     
/*     */     public Single(Texture texture) {
/*  45 */       super(texture);
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/*  50 */       RegionInfluencer.AspectTextureRegion region = ((RegionInfluencer.AspectTextureRegion[])this.regions.items)[0];
/*  51 */       for (int i = 0, c = this.controller.emitter.maxParticleCount * this.regionChannel.strideSize; i < c; i += this.regionChannel.strideSize) {
/*  52 */         this.regionChannel.data[i + 0] = region.u;
/*  53 */         this.regionChannel.data[i + 1] = region.v;
/*  54 */         this.regionChannel.data[i + 2] = region.u2;
/*  55 */         this.regionChannel.data[i + 3] = region.v2;
/*  56 */         this.regionChannel.data[i + 4] = 0.5F;
/*  57 */         this.regionChannel.data[i + 5] = region.halfInvAspectRatio;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Single copy() {
/*  63 */       return new Single(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Random
/*     */     extends RegionInfluencer
/*     */   {
/*     */     public Random() {}
/*     */     
/*     */     public Random(Random regionInfluencer) {
/*  73 */       super(regionInfluencer);
/*     */     }
/*     */     
/*     */     public Random(TextureRegion textureRegion) {
/*  77 */       super(new TextureRegion[] { textureRegion });
/*     */     }
/*     */     
/*     */     public Random(Texture texture) {
/*  81 */       super(texture);
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  86 */       for (int i = startIndex * this.regionChannel.strideSize, c = i + count * this.regionChannel.strideSize; i < c; i += this.regionChannel.strideSize) {
/*  87 */         RegionInfluencer.AspectTextureRegion region = (RegionInfluencer.AspectTextureRegion)this.regions.random();
/*  88 */         this.regionChannel.data[i + 0] = region.u;
/*  89 */         this.regionChannel.data[i + 1] = region.v;
/*  90 */         this.regionChannel.data[i + 2] = region.u2;
/*  91 */         this.regionChannel.data[i + 3] = region.v2;
/*  92 */         this.regionChannel.data[i + 4] = 0.5F;
/*  93 */         this.regionChannel.data[i + 5] = region.halfInvAspectRatio;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Random copy() {
/*  99 */       return new Random(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Animated
/*     */     extends RegionInfluencer
/*     */   {
/*     */     ParallelArray.FloatChannel lifeChannel;
/*     */     
/*     */     public Animated() {}
/*     */     
/*     */     public Animated(Animated regionInfluencer) {
/* 112 */       super(regionInfluencer);
/*     */     }
/*     */     
/*     */     public Animated(TextureRegion textureRegion) {
/* 116 */       super(new TextureRegion[] { textureRegion });
/*     */     }
/*     */     
/*     */     public Animated(Texture texture) {
/* 120 */       super(texture);
/*     */     }
/*     */ 
/*     */     
/*     */     public void allocateChannels() {
/* 125 */       super.allocateChannels();
/* 126 */       this.lifeChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.Life);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 131 */       for (int i = 0, l = 2, c = this.controller.particles.size * this.regionChannel.strideSize; i < c; i += this.regionChannel.strideSize, l += this.lifeChannel.strideSize) {
/* 132 */         RegionInfluencer.AspectTextureRegion region = (RegionInfluencer.AspectTextureRegion)this.regions.get((int)(this.lifeChannel.data[l] * (this.regions.size - 1)));
/* 133 */         this.regionChannel.data[i + 0] = region.u;
/* 134 */         this.regionChannel.data[i + 1] = region.v;
/* 135 */         this.regionChannel.data[i + 2] = region.u2;
/* 136 */         this.regionChannel.data[i + 3] = region.v2;
/* 137 */         this.regionChannel.data[i + 4] = 0.5F;
/* 138 */         this.regionChannel.data[i + 5] = region.halfInvAspectRatio;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Animated copy() {
/* 144 */       return new Animated(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AspectTextureRegion {
/*     */     public float u;
/*     */     public float v;
/*     */     public float u2;
/*     */     public float v2;
/*     */     public float halfInvAspectRatio;
/*     */     
/*     */     public AspectTextureRegion() {}
/*     */     
/*     */     public AspectTextureRegion(AspectTextureRegion aspectTextureRegion) {
/* 158 */       set(aspectTextureRegion);
/*     */     }
/*     */     
/*     */     public AspectTextureRegion(TextureRegion region) {
/* 162 */       set(region);
/*     */     }
/*     */     
/*     */     public void set(TextureRegion region) {
/* 166 */       this.u = region.getU();
/* 167 */       this.v = region.getV();
/* 168 */       this.u2 = region.getU2();
/* 169 */       this.v2 = region.getV2();
/* 170 */       this.halfInvAspectRatio = 0.5F * region.getRegionHeight() / region.getRegionWidth();
/*     */     }
/*     */     
/*     */     public void set(AspectTextureRegion aspectTextureRegion) {
/* 174 */       this.u = aspectTextureRegion.u;
/* 175 */       this.v = aspectTextureRegion.v;
/* 176 */       this.u2 = aspectTextureRegion.u2;
/* 177 */       this.v2 = aspectTextureRegion.v2;
/* 178 */       this.halfInvAspectRatio = aspectTextureRegion.halfInvAspectRatio;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegionInfluencer(int regionsCount) {
/* 186 */     this.regions = new Array(false, regionsCount, AspectTextureRegion.class);
/*     */   }
/*     */   
/*     */   public RegionInfluencer() {
/* 190 */     this(1);
/* 191 */     AspectTextureRegion aspectRegion = new AspectTextureRegion();
/* 192 */     aspectRegion.u = aspectRegion.v = 0.0F;
/* 193 */     aspectRegion.u2 = aspectRegion.v2 = 1.0F;
/* 194 */     aspectRegion.halfInvAspectRatio = 0.5F;
/* 195 */     this.regions.add(aspectRegion);
/*     */   }
/*     */ 
/*     */   
/*     */   public RegionInfluencer(TextureRegion... regions) {
/* 200 */     this.regions = new Array(false, regions.length, AspectTextureRegion.class);
/* 201 */     add(regions);
/*     */   }
/*     */   
/*     */   public RegionInfluencer(Texture texture) {
/* 205 */     this(new TextureRegion[] { new TextureRegion(texture) });
/*     */   }
/*     */   
/*     */   public RegionInfluencer(RegionInfluencer regionInfluencer) {
/* 209 */     this(regionInfluencer.regions.size);
/* 210 */     this.regions.ensureCapacity(regionInfluencer.regions.size);
/* 211 */     for (int i = 0; i < regionInfluencer.regions.size; i++) {
/* 212 */       this.regions.add(new AspectTextureRegion((AspectTextureRegion)regionInfluencer.regions.get(i)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void add(TextureRegion... regions) {
/* 217 */     this.regions.ensureCapacity(regions.length);
/* 218 */     for (TextureRegion region : regions) {
/* 219 */       this.regions.add(new AspectTextureRegion(region));
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 224 */     this.regions.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 229 */     this.regionChannel = (ParallelArray.FloatChannel)this.controller.particles.addChannel(ParticleChannels.TextureRegion);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 234 */     json.writeValue("regions", this.regions, Array.class, AspectTextureRegion.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 239 */     this.regions.clear();
/* 240 */     this.regions.addAll((Array)json.readValue("regions", Array.class, AspectTextureRegion.class, jsonData));
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\RegionInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */