/*    */ package com.badlogic.gdx.tools.headers;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.tools.FileProcessor;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.File;
/*    */ import java.io.OutputStreamWriter;
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
/*    */ public class HeaderFixer
/*    */ {
/*    */   static int filesScanned;
/*    */   static int filesChanged;
/*    */   
/*    */   static class HeaderFileProcessor
/*    */     extends FileProcessor
/*    */   {
/*    */     final String header;
/*    */     
/*    */     public HeaderFileProcessor() {
/* 34 */       this.header = (new FileHandle("assets/licence-header.txt")).readString();
/* 35 */       addInputSuffix(new String[] { ".java" });
/* 36 */       setFlattenOutput(false);
/* 37 */       setRecursive(true);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void processFile(FileProcessor.Entry inputFile) throws Exception {
/* 42 */       HeaderFixer.filesScanned++;
/* 43 */       String content = (new FileHandle(inputFile.inputFile)).readString();
/* 44 */       if (content.trim().startsWith("package")) {
/* 45 */         System.out.println("File '" + inputFile.inputFile + "' header fixed");
/* 46 */         HeaderFixer.filesChanged++;
/* 47 */         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((new FileHandle(inputFile.outputFile)).write(false)));
/*    */         
/* 49 */         writer.write(this.header + "\n\n" + content);
/* 50 */         writer.close();
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     protected void processDir(FileProcessor.Entry inputDir, ArrayList<FileProcessor.Entry> value) throws Exception {}
/*    */   }
/*    */ 
/*    */   
/*    */   public static void process(String directory) throws Exception {
/* 60 */     HeaderFileProcessor processor = new HeaderFileProcessor();
/* 61 */     processor.process(new File(directory), new File(directory));
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 65 */     if (args.length != 1) {
/* 66 */       process("../../gdx/");
/* 67 */       process("../../backends/");
/* 68 */       process("../../tests/");
/* 69 */       process("../../extensions/");
/*    */     } else {
/* 71 */       process(args[0]);
/*    */     } 
/* 73 */     System.out.println("Changed " + filesChanged + " / " + filesScanned + " files.");
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\headers\HeaderFixer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */