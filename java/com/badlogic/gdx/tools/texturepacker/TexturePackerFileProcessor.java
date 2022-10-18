/*     */ package com.badlogic.gdx.tools.texturepacker;
/*     */ 
/*     */ import com.badlogic.gdx.tools.FileProcessor;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonReader;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.regex.Matcher;
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
/*     */ public class TexturePackerFileProcessor
/*     */   extends FileProcessor
/*     */ {
/*     */   private final TexturePacker.Settings defaultSettings;
/*  38 */   private ObjectMap<File, TexturePacker.Settings> dirToSettings = new ObjectMap();
/*  39 */   private Json json = new Json();
/*     */   private String packFileName;
/*     */   private File root;
/*  42 */   ArrayList<File> ignoreDirs = new ArrayList<File>();
/*     */   
/*     */   public TexturePackerFileProcessor() {
/*  45 */     this(new TexturePacker.Settings(), "pack.atlas");
/*     */   }
/*     */   
/*     */   public TexturePackerFileProcessor(TexturePacker.Settings defaultSettings, String packFileName) {
/*  49 */     this.defaultSettings = defaultSettings;
/*     */     
/*  51 */     if (packFileName.toLowerCase().endsWith(defaultSettings.atlasExtension.toLowerCase()))
/*  52 */       packFileName = packFileName.substring(0, packFileName.length() - defaultSettings.atlasExtension.length()); 
/*  53 */     this.packFileName = packFileName;
/*     */     
/*  55 */     setFlattenOutput(true);
/*  56 */     addInputSuffix(new String[] { ".png", ".jpg", ".jpeg" });
/*     */   }
/*     */   
/*     */   public ArrayList<FileProcessor.Entry> process(File inputFile, File outputRoot) throws Exception {
/*  60 */     this.root = inputFile;
/*     */ 
/*     */     
/*  63 */     final ArrayList<File> settingsFiles = new ArrayList<File>();
/*  64 */     FileProcessor settingsProcessor = new FileProcessor() {
/*     */         protected void processFile(FileProcessor.Entry inputFile) throws Exception {
/*  66 */           settingsFiles.add(inputFile.inputFile);
/*     */         }
/*     */       };
/*  69 */     settingsProcessor.addInputRegex(new String[] { "pack\\.json" });
/*  70 */     settingsProcessor.process(inputFile, null);
/*     */     
/*  72 */     Collections.sort(settingsFiles, new Comparator<File>() {
/*     */           public int compare(File file1, File file2) {
/*  74 */             return file1.toString().length() - file2.toString().length();
/*     */           }
/*     */         });
/*  77 */     for (File settingsFile : settingsFiles) {
/*     */       
/*  79 */       TexturePacker.Settings settings = null;
/*  80 */       File parent = settingsFile.getParentFile();
/*     */       
/*  82 */       while (!parent.equals(this.root)) {
/*  83 */         parent = parent.getParentFile();
/*  84 */         settings = (TexturePacker.Settings)this.dirToSettings.get(parent);
/*  85 */         if (settings != null) {
/*  86 */           settings = new TexturePacker.Settings(settings);
/*     */           break;
/*     */         } 
/*     */       } 
/*  90 */       if (settings == null) settings = new TexturePacker.Settings(this.defaultSettings);
/*     */       
/*  92 */       merge(settings, settingsFile);
/*  93 */       this.dirToSettings.put(settingsFile.getParentFile(), settings);
/*     */     } 
/*     */ 
/*     */     
/*  97 */     return super.process(inputFile, outputRoot);
/*     */   }
/*     */   
/*     */   private void merge(TexturePacker.Settings settings, File settingsFile) {
/*     */     try {
/* 102 */       this.json.readFields(settings, (new JsonReader()).parse(new FileReader(settingsFile)));
/* 103 */     } catch (Exception ex) {
/* 104 */       throw new GdxRuntimeException("Error reading settings file: " + settingsFile, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<FileProcessor.Entry> process(File[] files, File outputRoot) throws Exception {
/* 110 */     if (outputRoot.exists()) {
/*     */       
/* 112 */       File settingsFile = new File(this.root, "pack.json");
/* 113 */       TexturePacker.Settings rootSettings = this.defaultSettings;
/* 114 */       if (settingsFile.exists()) {
/* 115 */         rootSettings = new TexturePacker.Settings(rootSettings);
/* 116 */         merge(rootSettings, settingsFile);
/*     */       } 
/*     */       
/* 119 */       String atlasExtension = (rootSettings.atlasExtension == null) ? "" : rootSettings.atlasExtension;
/* 120 */       atlasExtension = Pattern.quote(atlasExtension);
/*     */       
/* 122 */       for (int i = 0, n = rootSettings.scale.length; i < n; i++) {
/* 123 */         FileProcessor deleteProcessor = new FileProcessor() {
/*     */             protected void processFile(FileProcessor.Entry inputFile) throws Exception {
/* 125 */               inputFile.inputFile.delete();
/*     */             }
/*     */           };
/* 128 */         deleteProcessor.setRecursive(false);
/*     */         
/* 130 */         String scaledPackFileName = rootSettings.getScaledPackFileName(this.packFileName, i);
/* 131 */         File packFile = new File(scaledPackFileName);
/*     */         
/* 133 */         String prefix = packFile.getName();
/* 134 */         int dotIndex = prefix.lastIndexOf('.');
/* 135 */         if (dotIndex != -1) prefix = prefix.substring(0, dotIndex); 
/* 136 */         deleteProcessor.addInputRegex(new String[] { "(?i)" + prefix + "\\d*\\.(png|jpg|jpeg)" });
/* 137 */         deleteProcessor.addInputRegex(new String[] { "(?i)" + prefix + atlasExtension });
/*     */         
/* 139 */         String dir = packFile.getParent();
/* 140 */         if (dir == null) {
/* 141 */           deleteProcessor.process(outputRoot, null);
/* 142 */         } else if ((new File(outputRoot + "/" + dir)).exists()) {
/* 143 */           deleteProcessor.process(outputRoot + "/" + dir, null);
/*     */         } 
/*     */       } 
/* 146 */     }  return super.process(files, outputRoot);
/*     */   }
/*     */   
/*     */   protected void processDir(FileProcessor.Entry inputDir, ArrayList<FileProcessor.Entry> files) throws Exception {
/* 150 */     if (this.ignoreDirs.contains(inputDir.inputFile)) {
/*     */       return;
/*     */     }
/* 153 */     TexturePacker.Settings settings = null;
/* 154 */     File parent = inputDir.inputFile;
/*     */     while (true) {
/* 156 */       settings = (TexturePacker.Settings)this.dirToSettings.get(parent);
/* 157 */       if (settings != null || 
/* 158 */         parent == null || parent.equals(this.root))
/* 159 */         break;  parent = parent.getParentFile();
/*     */     } 
/* 161 */     if (settings == null) settings = this.defaultSettings;
/*     */     
/* 163 */     if (settings.ignore)
/*     */       return; 
/* 165 */     if (settings.combineSubdirectories)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 175 */       files = (new FileProcessor(this) { protected void processDir(FileProcessor.Entry entryDir, ArrayList<FileProcessor.Entry> files) { TexturePackerFileProcessor.this.ignoreDirs.add(entryDir.inputFile); } protected void processFile(FileProcessor.Entry entry) { addProcessedFile(entry); } }).process(inputDir.inputFile, null);
/*     */     }
/*     */     
/* 178 */     if (files.isEmpty()) {
/*     */       return;
/*     */     }
/* 181 */     Collections.sort(files, new Comparator<FileProcessor.Entry>() {
/* 182 */           final Pattern digitSuffix = Pattern.compile("(.*?)(\\d+)$");
/*     */           
/*     */           public int compare(FileProcessor.Entry entry1, FileProcessor.Entry entry2) {
/* 185 */             String full1 = entry1.inputFile.getName();
/* 186 */             int dotIndex = full1.lastIndexOf('.');
/* 187 */             if (dotIndex != -1) full1 = full1.substring(0, dotIndex);
/*     */             
/* 189 */             String full2 = entry2.inputFile.getName();
/* 190 */             dotIndex = full2.lastIndexOf('.');
/* 191 */             if (dotIndex != -1) full2 = full2.substring(0, dotIndex);
/*     */             
/* 193 */             String name1 = full1, name2 = full2;
/* 194 */             int num1 = 0, num2 = 0;
/*     */             
/* 196 */             Matcher matcher = this.digitSuffix.matcher(full1);
/* 197 */             if (matcher.matches()) {
/*     */               try {
/* 199 */                 num1 = Integer.parseInt(matcher.group(2));
/* 200 */                 name1 = matcher.group(1);
/* 201 */               } catch (Exception exception) {}
/*     */             }
/*     */             
/* 204 */             matcher = this.digitSuffix.matcher(full2);
/* 205 */             if (matcher.matches()) {
/*     */               try {
/* 207 */                 num2 = Integer.parseInt(matcher.group(2));
/* 208 */                 name2 = matcher.group(1);
/* 209 */               } catch (Exception exception) {}
/*     */             }
/*     */             
/* 212 */             int compare = name1.compareTo(name2);
/* 213 */             if (compare != 0 || num1 == num2) return compare; 
/* 214 */             return num1 - num2;
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 219 */     if (!settings.silent) System.out.println(inputDir.inputFile.getName()); 
/* 220 */     TexturePacker packer = new TexturePacker(this.root, settings);
/* 221 */     for (FileProcessor.Entry file : files)
/* 222 */       packer.addImage(file.inputFile); 
/* 223 */     packer.pack(inputDir.outputDir, this.packFileName);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\texturepacker\TexturePackerFileProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */