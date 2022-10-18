/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleChannels
/*     */ {
/*     */   private static int currentGlobalId;
/*     */   
/*     */   public static int newGlobalId() {
/*  33 */     return currentGlobalId++;
/*     */   }
/*     */   
/*     */   public static class TextureRegionInitializer
/*     */     implements ParallelArray.ChannelInitializer<ParallelArray.FloatChannel> {
/*     */     private static TextureRegionInitializer instance;
/*     */     
/*     */     public static TextureRegionInitializer get() {
/*  41 */       if (instance == null) instance = new TextureRegionInitializer(); 
/*  42 */       return instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public void init(ParallelArray.FloatChannel channel) {
/*  47 */       for (int i = 0, c = channel.data.length; i < c; i += channel.strideSize) {
/*  48 */         channel.data[i + 0] = 0.0F;
/*  49 */         channel.data[i + 1] = 0.0F;
/*  50 */         channel.data[i + 2] = 1.0F;
/*  51 */         channel.data[i + 3] = 1.0F;
/*  52 */         channel.data[i + 4] = 0.5F;
/*  53 */         channel.data[i + 5] = 0.5F;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ColorInitializer implements ParallelArray.ChannelInitializer<ParallelArray.FloatChannel> {
/*     */     private static ColorInitializer instance;
/*     */     
/*     */     public static ColorInitializer get() {
/*  62 */       if (instance == null) instance = new ColorInitializer(); 
/*  63 */       return instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public void init(ParallelArray.FloatChannel channel) {
/*  68 */       Arrays.fill(channel.data, 0, channel.data.length, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ScaleInitializer implements ParallelArray.ChannelInitializer<ParallelArray.FloatChannel> {
/*     */     private static ScaleInitializer instance;
/*     */     
/*     */     public static ScaleInitializer get() {
/*  76 */       if (instance == null) instance = new ScaleInitializer(); 
/*  77 */       return instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public void init(ParallelArray.FloatChannel channel) {
/*  82 */       Arrays.fill(channel.data, 0, channel.data.length, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Rotation2dInitializer implements ParallelArray.ChannelInitializer<ParallelArray.FloatChannel> {
/*     */     private static Rotation2dInitializer instance;
/*     */     
/*     */     public static Rotation2dInitializer get() {
/*  90 */       if (instance == null) instance = new Rotation2dInitializer(); 
/*  91 */       return instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public void init(ParallelArray.FloatChannel channel) {
/*  96 */       for (int i = 0, c = channel.data.length; i < c; i += channel.strideSize) {
/*  97 */         channel.data[i + 0] = 1.0F;
/*  98 */         channel.data[i + 1] = 0.0F;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Rotation3dInitializer implements ParallelArray.ChannelInitializer<ParallelArray.FloatChannel> {
/*     */     private static Rotation3dInitializer instance;
/*     */     
/*     */     public static Rotation3dInitializer get() {
/* 107 */       if (instance == null) instance = new Rotation3dInitializer(); 
/* 108 */       return instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public void init(ParallelArray.FloatChannel channel) {
/* 113 */       for (int i = 0, c = channel.data.length; i < c; i += channel.strideSize) {
/* 114 */         channel.data[i + 2] = 0.0F; channel.data[i + 1] = 0.0F; channel.data[i + 0] = 0.0F;
/*     */         
/* 116 */         channel.data[i + 3] = 1.0F;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final ParallelArray.ChannelDescriptor Life = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 3);
/* 124 */   public static final ParallelArray.ChannelDescriptor Position = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 3);
/* 125 */   public static final ParallelArray.ChannelDescriptor PreviousPosition = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 3);
/* 126 */   public static final ParallelArray.ChannelDescriptor Color = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 4);
/* 127 */   public static final ParallelArray.ChannelDescriptor TextureRegion = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 6);
/* 128 */   public static final ParallelArray.ChannelDescriptor Rotation2D = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 2);
/* 129 */   public static final ParallelArray.ChannelDescriptor Rotation3D = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 4);
/* 130 */   public static final ParallelArray.ChannelDescriptor Scale = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 1);
/* 131 */   public static final ParallelArray.ChannelDescriptor ModelInstance = new ParallelArray.ChannelDescriptor(newGlobalId(), ModelInstance.class, 1);
/* 132 */   public static final ParallelArray.ChannelDescriptor ParticleController = new ParallelArray.ChannelDescriptor(newGlobalId(), ParticleController.class, 1);
/* 133 */   public static final ParallelArray.ChannelDescriptor Acceleration = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 3);
/* 134 */   public static final ParallelArray.ChannelDescriptor AngularVelocity2D = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 1);
/* 135 */   public static final ParallelArray.ChannelDescriptor AngularVelocity3D = new ParallelArray.ChannelDescriptor(newGlobalId(), float.class, 3);
/* 136 */   public static final ParallelArray.ChannelDescriptor Interpolation = new ParallelArray.ChannelDescriptor(-1, float.class, 2);
/* 137 */   public static final ParallelArray.ChannelDescriptor Interpolation4 = new ParallelArray.ChannelDescriptor(-1, float.class, 4);
/* 138 */   public static final ParallelArray.ChannelDescriptor Interpolation6 = new ParallelArray.ChannelDescriptor(-1, float.class, 6); public static final int CurrentLifeOffset = 0;
/*     */   public static final int TotalLifeOffset = 1;
/*     */   public static final int LifePercentOffset = 2;
/*     */   public static final int RedOffset = 0;
/*     */   public static final int GreenOffset = 1;
/*     */   public static final int BlueOffset = 2;
/*     */   public static final int AlphaOffset = 3;
/*     */   public static final int InterpolationStartOffset = 0;
/*     */   public static final int InterpolationDiffOffset = 1;
/*     */   public static final int VelocityStrengthStartOffset = 0;
/*     */   public static final int VelocityStrengthDiffOffset = 1;
/*     */   public static final int VelocityThetaStartOffset = 0;
/*     */   public static final int VelocityThetaDiffOffset = 1;
/*     */   public static final int VelocityPhiStartOffset = 2;
/*     */   
/*     */   public ParticleChannels() {
/* 154 */     resetIds();
/*     */   }
/*     */   public static final int VelocityPhiDiffOffset = 3; public static final int XOffset = 0; public static final int YOffset = 1; public static final int ZOffset = 2; public static final int WOffset = 3; public static final int UOffset = 0; public static final int VOffset = 1; public static final int U2Offset = 2; public static final int V2Offset = 3; public static final int HalfWidthOffset = 4; public static final int HalfHeightOffset = 5; public static final int CosineOffset = 0; public static final int SineOffset = 1; private int currentId;
/*     */   public int newId() {
/* 158 */     return this.currentId++;
/*     */   }
/*     */   
/*     */   protected void resetIds() {
/* 162 */     this.currentId = currentGlobalId;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ParticleChannels.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */