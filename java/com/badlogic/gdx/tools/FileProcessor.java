/*     */ package com.badlogic.gdx.tools;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class FileProcessor
/*     */ {
/*     */   FilenameFilter inputFilter;
/*     */   
/*  36 */   Comparator<File> comparator = new Comparator<File>() {
/*     */       public int compare(File o1, File o2) {
/*  38 */         return o1.getName().compareTo(o2.getName());
/*     */       }
/*     */     };
/*  41 */   Array<Pattern> inputRegex = new Array();
/*     */   String outputSuffix;
/*  43 */   ArrayList<Entry> outputFiles = new ArrayList<Entry>();
/*     */   boolean recursive = true;
/*     */   boolean flattenOutput;
/*     */   
/*  47 */   Comparator<Entry> entryComparator = new Comparator<Entry>() {
/*     */       public int compare(FileProcessor.Entry o1, FileProcessor.Entry o2) {
/*  49 */         return FileProcessor.this.comparator.compare(o1.inputFile, o2.inputFile);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public FileProcessor() {}
/*     */ 
/*     */   
/*     */   public FileProcessor(FileProcessor processor) {
/*  58 */     this.inputFilter = processor.inputFilter;
/*  59 */     this.comparator = processor.comparator;
/*  60 */     this.inputRegex.addAll(processor.inputRegex);
/*  61 */     this.outputSuffix = processor.outputSuffix;
/*  62 */     this.recursive = processor.recursive;
/*  63 */     this.flattenOutput = processor.flattenOutput;
/*     */   }
/*     */   
/*     */   public FileProcessor setInputFilter(FilenameFilter inputFilter) {
/*  67 */     this.inputFilter = inputFilter;
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileProcessor setComparator(Comparator<File> comparator) {
/*  73 */     this.comparator = comparator;
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileProcessor addInputSuffix(String... suffixes) {
/*  79 */     for (String suffix : suffixes) {
/*  80 */       addInputRegex(new String[] { "(?i).*" + Pattern.quote(suffix) });
/*  81 */     }  return this;
/*     */   }
/*     */   
/*     */   public FileProcessor addInputRegex(String... regexes) {
/*  85 */     for (String regex : regexes)
/*  86 */       this.inputRegex.add(Pattern.compile(regex)); 
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileProcessor setOutputSuffix(String outputSuffix) {
/*  92 */     this.outputSuffix = outputSuffix;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public FileProcessor setFlattenOutput(boolean flattenOutput) {
/*  97 */     this.flattenOutput = flattenOutput;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileProcessor setRecursive(boolean recursive) {
/* 103 */     this.recursive = recursive;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<Entry> process(String inputFileOrDir, String outputRoot) throws Exception {
/* 110 */     return process(new File(inputFileOrDir), (outputRoot == null) ? null : new File(outputRoot));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<Entry> process(File inputFileOrDir, File outputRoot) throws Exception {
/* 117 */     if (!inputFileOrDir.exists()) throw new IllegalArgumentException("Input file does not exist: " + inputFileOrDir.getAbsolutePath()); 
/* 118 */     if (inputFileOrDir.isFile()) {
/* 119 */       return process(new File[] { inputFileOrDir }, outputRoot);
/*     */     }
/* 121 */     return process(inputFileOrDir.listFiles(), outputRoot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<Entry> process(File[] files, File outputRoot) throws Exception {
/* 128 */     if (outputRoot == null) outputRoot = new File(""); 
/* 129 */     this.outputFiles.clear();
/*     */     
/* 131 */     LinkedHashMap<File, ArrayList<Entry>> dirToEntries = new LinkedHashMap<File, ArrayList<Entry>>();
/* 132 */     process(files, outputRoot, outputRoot, dirToEntries, 0);
/*     */     
/* 134 */     ArrayList<Entry> allEntries = new ArrayList<Entry>();
/* 135 */     for (Map.Entry<File, ArrayList<Entry>> mapEntry : dirToEntries.entrySet()) {
/* 136 */       ArrayList<Entry> dirEntries = mapEntry.getValue();
/* 137 */       if (this.comparator != null) Collections.sort(dirEntries, this.entryComparator);
/*     */       
/* 139 */       File inputDir = mapEntry.getKey();
/* 140 */       File newOutputDir = null;
/* 141 */       if (this.flattenOutput) {
/* 142 */         newOutputDir = outputRoot;
/* 143 */       } else if (!dirEntries.isEmpty()) {
/* 144 */         newOutputDir = ((Entry)dirEntries.get(0)).outputDir;
/* 145 */       }  String outputName = inputDir.getName();
/* 146 */       if (this.outputSuffix != null) outputName = outputName.replaceAll("(.*)\\..*", "$1") + this.outputSuffix;
/*     */       
/* 148 */       Entry entry = new Entry();
/* 149 */       entry.inputFile = mapEntry.getKey();
/* 150 */       entry.outputDir = newOutputDir;
/* 151 */       if (newOutputDir != null) {
/* 152 */         entry.outputFile = (newOutputDir.length() == 0L) ? new File(outputName) : new File(newOutputDir, outputName);
/*     */       }
/*     */       try {
/* 155 */         processDir(entry, dirEntries);
/* 156 */       } catch (Exception ex) {
/* 157 */         throw new Exception("Error processing directory: " + entry.inputFile.getAbsolutePath(), ex);
/*     */       } 
/* 159 */       allEntries.addAll(dirEntries);
/*     */     } 
/*     */     
/* 162 */     if (this.comparator != null) Collections.sort(allEntries, this.entryComparator); 
/* 163 */     for (Entry entry : allEntries) {
/*     */       try {
/* 165 */         processFile(entry);
/* 166 */       } catch (Exception ex) {
/* 167 */         throw new Exception("Error processing file: " + entry.inputFile.getAbsolutePath(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     return this.outputFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void process(File[] files, File outputRoot, File outputDir, LinkedHashMap<File, ArrayList<Entry>> dirToEntries, int depth) {
/* 177 */     for (File file : files) {
/* 178 */       File dir = file.getParentFile();
/* 179 */       ArrayList<Entry> entries = dirToEntries.get(dir);
/* 180 */       if (entries == null) {
/* 181 */         entries = new ArrayList<Entry>();
/* 182 */         dirToEntries.put(dir, entries);
/*     */       } 
/*     */     } 
/*     */     
/* 186 */     for (File file : files) {
/* 187 */       if (file.isFile()) {
/* 188 */         if (this.inputRegex.size > 0) {
/* 189 */           boolean found = false;
/* 190 */           for (Pattern pattern : this.inputRegex) {
/* 191 */             if (pattern.matcher(file.getName()).matches()) {
/* 192 */               found = true;
/*     */             }
/*     */           } 
/*     */           
/* 196 */           if (!found)
/*     */             continue; 
/*     */         } 
/* 199 */         File dir = file.getParentFile();
/* 200 */         if (this.inputFilter != null && !this.inputFilter.accept(dir, file.getName()))
/*     */           continue; 
/* 202 */         String outputName = file.getName();
/* 203 */         if (this.outputSuffix != null) outputName = outputName.replaceAll("(.*)\\..*", "$1") + this.outputSuffix;
/*     */         
/* 205 */         Entry entry = new Entry();
/* 206 */         entry.depth = depth;
/* 207 */         entry.inputFile = file;
/* 208 */         entry.outputDir = outputDir;
/*     */         
/* 210 */         if (this.flattenOutput) {
/* 211 */           entry.outputFile = new File(outputRoot, outputName);
/*     */         } else {
/* 213 */           entry.outputFile = new File(outputDir, outputName);
/*     */         } 
/*     */         
/* 216 */         ((ArrayList<Entry>)dirToEntries.get(dir)).add(entry);
/*     */       } 
/* 218 */       if (this.recursive && file.isDirectory()) {
/* 219 */         File subdir = (outputDir.getPath().length() == 0) ? new File(file.getName()) : new File(outputDir, file.getName());
/* 220 */         process(file.listFiles(this.inputFilter), outputRoot, subdir, dirToEntries, depth + 1);
/*     */       } 
/*     */       continue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processFile(Entry entry) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processDir(Entry entryDir, ArrayList<Entry> files) throws Exception {}
/*     */ 
/*     */   
/*     */   protected void addProcessedFile(Entry entry) {
/* 236 */     this.outputFiles.add(entry);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Entry
/*     */   {
/*     */     public File inputFile;
/*     */     
/*     */     public File outputDir;
/*     */     public File outputFile;
/*     */     public int depth;
/*     */     
/*     */     public Entry() {}
/*     */     
/*     */     public Entry(File inputFile, File outputFile) {
/* 251 */       this.inputFile = inputFile;
/* 252 */       this.outputFile = outputFile;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 256 */       return this.inputFile.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\FileProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */