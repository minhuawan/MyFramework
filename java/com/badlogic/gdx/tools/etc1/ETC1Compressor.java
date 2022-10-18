/*    */ package com.badlogic.gdx.tools.etc1;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.Pixmap;
/*    */ import com.badlogic.gdx.graphics.glutils.ETC1;
/*    */ import com.badlogic.gdx.tools.FileProcessor;
/*    */ import com.badlogic.gdx.utils.GdxNativesLoader;
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
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
/*    */ public class ETC1Compressor
/*    */ {
/*    */   static class ETC1FileProcessor
/*    */     extends FileProcessor
/*    */   {
/*    */     ETC1FileProcessor() {
/* 32 */       addInputSuffix(new String[] { ".png" });
/* 33 */       addInputSuffix(new String[] { ".jpg" });
/* 34 */       addInputSuffix(new String[] { ".jpeg" });
/* 35 */       addInputSuffix(new String[] { ".bmp" });
/* 36 */       setOutputSuffix(".etc1");
/*    */     }
/*    */ 
/*    */     
/*    */     protected void processFile(FileProcessor.Entry entry) throws Exception {
/* 41 */       System.out.println("Processing " + entry.inputFile);
/* 42 */       Pixmap pixmap = new Pixmap(new FileHandle(entry.inputFile));
/* 43 */       if (pixmap.getFormat() != Pixmap.Format.RGB888 && pixmap.getFormat() != Pixmap.Format.RGB565) {
/* 44 */         System.out.println("Converting from " + pixmap.getFormat() + " to RGB888!");
/* 45 */         Pixmap tmp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGB888);
/* 46 */         tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
/* 47 */         pixmap.dispose();
/* 48 */         pixmap = tmp;
/*    */       } 
/* 50 */       ETC1.encodeImagePKM(pixmap).write(new FileHandle(entry.outputFile));
/* 51 */       pixmap.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     protected void processDir(FileProcessor.Entry entryDir, ArrayList<FileProcessor.Entry> value) throws Exception {
/* 56 */       if (!entryDir.outputDir.exists() && 
/* 57 */         !entryDir.outputDir.mkdirs()) {
/* 58 */         throw new Exception("Couldn't create output directory '" + entryDir.outputDir + "'");
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static void process(String inputDirectory, String outputDirectory, boolean recursive, boolean flatten) throws Exception {
/* 65 */     GdxNativesLoader.load();
/* 66 */     ETC1FileProcessor processor = new ETC1FileProcessor();
/* 67 */     processor.setRecursive(recursive);
/* 68 */     processor.setFlattenOutput(flatten);
/* 69 */     processor.process(new File(inputDirectory), new File(outputDirectory));
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 73 */     if (args.length != 2) {
/* 74 */       System.out.println("ETC1Compressor <input-dir> <output-dir>");
/* 75 */       System.exit(-1);
/*    */     } 
/* 77 */     process(args[0], args[1], true, false);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\etc1\ETC1Compressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */