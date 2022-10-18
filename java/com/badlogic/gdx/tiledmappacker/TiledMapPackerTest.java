/*    */ package com.badlogic.gdx.tiledmappacker;
/*    */ 
/*    */ import java.io.File;
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
/*    */ public class TiledMapPackerTest
/*    */ {
/*    */   public enum TestType
/*    */   {
/* 28 */     NoArgs, DefaultUsage, Verbose, StripUnused, CombineTilesets, UnusedAndCombine, BadOption;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 32 */     String path = "../../tests/gdx-tests-android/assets/data/maps/";
/* 33 */     String input = path + "tiled-atlas-src";
/* 34 */     String output = path + "tiled-atlas-processed/deleteMe";
/* 35 */     String verboseOpt = "-v";
/* 36 */     String unused = "--strip-unused";
/* 37 */     String combine = "--combine-tilesets";
/* 38 */     String badOpt = "bad";
/*    */     
/* 40 */     File outputDir = new File(output);
/* 41 */     if (outputDir.exists()) {
/* 42 */       System.out.println("Please run TiledMapPackerTestRender or delete \"deleteMe\" folder located in");
/* 43 */       System.out.println("gdx-tests-android: assets/data/maps/tiled-atlas-processed/deleteMe");
/*    */       
/*    */       return;
/*    */     } 
/* 47 */     TestType testType = TestType.DefaultUsage;
/*    */     
/* 49 */     String[] noArgs = new String[0];
/* 50 */     String[] defaultUsage = { input, output };
/* 51 */     String[] verbose = { input, output, verboseOpt };
/* 52 */     String[] stripUnused = { input, output, unused };
/* 53 */     String[] combineTilesets = { input, output, combine };
/* 54 */     String[] unusedAndCombine = { input, output, unused, combine };
/* 55 */     String[] badOption = { input, output, unused, verboseOpt, combine, badOpt };
/*    */     
/* 57 */     switch (testType) {
/*    */       case NoArgs:
/* 59 */         TiledMapPacker.main(noArgs);
/*    */         break;
/*    */       case DefaultUsage:
/* 62 */         TiledMapPacker.main(defaultUsage);
/*    */         break;
/*    */       case Verbose:
/* 65 */         TiledMapPacker.main(verbose);
/*    */         break;
/*    */       case StripUnused:
/* 68 */         TiledMapPacker.main(stripUnused);
/*    */         break;
/*    */       case CombineTilesets:
/* 71 */         TiledMapPacker.main(combineTilesets);
/*    */         break;
/*    */       case UnusedAndCombine:
/* 74 */         TiledMapPacker.main(unusedAndCombine);
/*    */         break;
/*    */       case BadOption:
/* 77 */         TiledMapPacker.main(badOption);
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tiledmappacker\TiledMapPackerTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */