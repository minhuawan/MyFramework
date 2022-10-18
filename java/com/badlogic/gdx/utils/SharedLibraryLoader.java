/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.UUID;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
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
/*     */ public class SharedLibraryLoader
/*     */ {
/*  37 */   public static boolean isWindows = System.getProperty("os.name").contains("Windows");
/*  38 */   public static boolean isLinux = System.getProperty("os.name").contains("Linux");
/*  39 */   public static boolean isMac = System.getProperty("os.name").contains("Mac");
/*     */   public static boolean isIos = false;
/*     */   public static boolean isAndroid = false;
/*  42 */   public static boolean isARM = System.getProperty("os.arch").startsWith("arm");
/*  43 */   public static boolean is64Bit = (System.getProperty("os.arch").equals("amd64") || 
/*  44 */     System.getProperty("os.arch").equals("x86_64"));
/*     */ 
/*     */   
/*  47 */   public static String abi = (System.getProperty("sun.arch.abi") != null) ? System.getProperty("sun.arch.abi") : "";
/*     */   
/*     */   static {
/*  50 */     String vm = System.getProperty("java.runtime.name");
/*  51 */     if (vm != null && vm.contains("Android Runtime")) {
/*  52 */       isAndroid = true;
/*  53 */       isWindows = false;
/*  54 */       isLinux = false;
/*  55 */       isMac = false;
/*  56 */       is64Bit = false;
/*     */     } 
/*  58 */     if (!isAndroid && !isWindows && !isLinux && !isMac) {
/*  59 */       isIos = true;
/*  60 */       is64Bit = false;
/*     */     } 
/*     */   }
/*     */   
/*  64 */   private static final HashSet<String> loadedLibraries = new HashSet<String>();
/*     */ 
/*     */   
/*     */   private String nativesJar;
/*     */ 
/*     */   
/*     */   public SharedLibraryLoader() {}
/*     */ 
/*     */   
/*     */   public SharedLibraryLoader(String nativesJar) {
/*  74 */     this.nativesJar = nativesJar;
/*     */   }
/*     */ 
/*     */   
/*     */   public String crc(InputStream input) {
/*  79 */     if (input == null) throw new IllegalArgumentException("input cannot be null."); 
/*  80 */     CRC32 crc = new CRC32();
/*  81 */     byte[] buffer = new byte[4096];
/*     */     try {
/*     */       while (true) {
/*  84 */         int length = input.read(buffer);
/*  85 */         if (length == -1)
/*  86 */           break;  crc.update(buffer, 0, length);
/*     */       } 
/*  88 */     } catch (Exception ex) {
/*  89 */       StreamUtils.closeQuietly(input);
/*     */     } 
/*  91 */     return Long.toString(crc.getValue(), 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public String mapLibraryName(String libraryName) {
/*  96 */     if (isWindows) return libraryName + (is64Bit ? "64.dll" : ".dll"); 
/*  97 */     if (isLinux) return "lib" + libraryName + (isARM ? ("arm" + abi) : "") + (is64Bit ? "64.so" : ".so"); 
/*  98 */     if (isMac) return "lib" + libraryName + (is64Bit ? "64.dylib" : ".dylib"); 
/*  99 */     return libraryName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(String libraryName) {
/* 106 */     if (isIos)
/*     */       return; 
/* 108 */     synchronized (SharedLibraryLoader.class) {
/* 109 */       if (isLoaded(libraryName))
/* 110 */         return;  String platformName = mapLibraryName(libraryName);
/*     */       try {
/* 112 */         if (isAndroid) {
/* 113 */           System.loadLibrary(platformName);
/*     */         } else {
/* 115 */           loadFile(platformName);
/* 116 */         }  setLoaded(libraryName);
/* 117 */       } catch (Throwable ex) {
/* 118 */         throw new GdxRuntimeException("Couldn't load shared library '" + platformName + "' for target: " + 
/* 119 */             System.getProperty("os.name") + (is64Bit ? ", 64-bit" : ", 32-bit"), ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private InputStream readFile(String path) {
/* 125 */     if (this.nativesJar == null) {
/* 126 */       InputStream input = SharedLibraryLoader.class.getResourceAsStream("/" + path);
/* 127 */       if (input == null) throw new GdxRuntimeException("Unable to read file for extraction: " + path); 
/* 128 */       return input;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 133 */       ZipFile file = new ZipFile(this.nativesJar);
/* 134 */       ZipEntry entry = file.getEntry(path);
/* 135 */       if (entry == null) throw new GdxRuntimeException("Couldn't find '" + path + "' in JAR: " + this.nativesJar); 
/* 136 */       return file.getInputStream(entry);
/* 137 */     } catch (IOException ex) {
/* 138 */       throw new GdxRuntimeException("Error reading '" + path + "' in JAR: " + this.nativesJar, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File extractFile(String sourcePath, String dirName) throws IOException {
/*     */     try {
/* 149 */       String sourceCrc = crc(readFile(sourcePath));
/* 150 */       if (dirName == null) dirName = sourceCrc;
/*     */       
/* 152 */       File extractedFile = getExtractedFile(dirName, (new File(sourcePath)).getName());
/* 153 */       if (extractedFile == null) {
/* 154 */         extractedFile = getExtractedFile(UUID.randomUUID().toString(), (new File(sourcePath)).getName());
/* 155 */         if (extractedFile == null) throw new GdxRuntimeException("Unable to find writable path to extract file. Is the user home directory writable?");
/*     */       
/*     */       } 
/* 158 */       return extractFile(sourcePath, sourceCrc, extractedFile);
/* 159 */     } catch (RuntimeException ex) {
/*     */       
/* 161 */       File file = new File(System.getProperty("java.library.path"), sourcePath);
/* 162 */       if (file.exists()) return file; 
/* 163 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void extractFileTo(String sourcePath, File dir) throws IOException {
/* 172 */     extractFile(sourcePath, crc(readFile(sourcePath)), new File(dir, (new File(sourcePath)).getName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File getExtractedFile(String dirName, String fileName) {
/* 180 */     File idealFile = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + dirName, fileName);
/* 181 */     if (canWrite(idealFile)) return idealFile;
/*     */ 
/*     */     
/*     */     try {
/* 185 */       File file1 = File.createTempFile(dirName, null);
/* 186 */       if (file1.delete()) {
/* 187 */         file1 = new File(file1, fileName);
/* 188 */         if (canWrite(file1)) return file1; 
/*     */       } 
/* 190 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */     
/* 194 */     File file = new File(System.getProperty("user.home") + "/.libgdx/" + dirName, fileName);
/* 195 */     if (canWrite(file)) return file;
/*     */ 
/*     */     
/* 198 */     file = new File(".temp/" + dirName, fileName);
/* 199 */     if (canWrite(file)) return file;
/*     */ 
/*     */     
/* 202 */     if (System.getenv("APP_SANDBOX_CONTAINER_ID") != null) return idealFile;
/*     */     
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canWrite(File file) {
/* 209 */     File testFile, parent = file.getParentFile();
/*     */     
/* 211 */     if (file.exists()) {
/* 212 */       if (!file.canWrite() || !canExecute(file)) return false;
/*     */       
/* 214 */       testFile = new File(parent, UUID.randomUUID().toString());
/*     */     } else {
/* 216 */       parent.mkdirs();
/* 217 */       if (!parent.isDirectory()) return false; 
/* 218 */       testFile = file;
/*     */     } 
/*     */     try {
/* 221 */       (new FileOutputStream(testFile)).close();
/* 222 */       if (!canExecute(testFile)) return false; 
/* 223 */       return true;
/* 224 */     } catch (Throwable ex) {
/* 225 */       return false;
/*     */     } finally {
/* 227 */       testFile.delete();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canExecute(File file) {
/*     */     try {
/* 233 */       Method canExecute = File.class.getMethod("canExecute", new Class[0]);
/* 234 */       if (((Boolean)canExecute.invoke(file, new Object[0])).booleanValue()) return true;
/*     */       
/* 236 */       Method setExecutable = File.class.getMethod("setExecutable", new Class[] { boolean.class, boolean.class });
/* 237 */       setExecutable.invoke(file, new Object[] { Boolean.valueOf(true), Boolean.valueOf(false) });
/*     */       
/* 239 */       return ((Boolean)canExecute.invoke(file, new Object[0])).booleanValue();
/* 240 */     } catch (Exception exception) {
/*     */       
/* 242 */       return false;
/*     */     } 
/*     */   }
/*     */   private File extractFile(String sourcePath, String sourceCrc, File extractedFile) throws IOException {
/* 246 */     String extractedCrc = null;
/* 247 */     if (extractedFile.exists()) {
/*     */       try {
/* 249 */         extractedCrc = crc(new FileInputStream(extractedFile));
/* 250 */       } catch (FileNotFoundException fileNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 255 */     if (extractedCrc == null || !extractedCrc.equals(sourceCrc)) {
/*     */       try {
/* 257 */         InputStream input = readFile(sourcePath);
/* 258 */         extractedFile.getParentFile().mkdirs();
/* 259 */         FileOutputStream output = new FileOutputStream(extractedFile);
/* 260 */         byte[] buffer = new byte[4096];
/*     */         while (true) {
/* 262 */           int length = input.read(buffer);
/* 263 */           if (length == -1)
/* 264 */             break;  output.write(buffer, 0, length);
/*     */         } 
/* 266 */         input.close();
/* 267 */         output.close();
/* 268 */       } catch (IOException ex) {
/* 269 */         throw new GdxRuntimeException("Error extracting file: " + sourcePath + "\nTo: " + extractedFile.getAbsolutePath(), ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 274 */     return extractedFile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadFile(String sourcePath) {
/* 280 */     String sourceCrc = crc(readFile(sourcePath));
/*     */     
/* 282 */     String fileName = (new File(sourcePath)).getName();
/*     */ 
/*     */     
/* 285 */     File file = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + sourceCrc, fileName);
/*     */     
/* 287 */     Throwable ex = loadFile(sourcePath, sourceCrc, file);
/* 288 */     if (ex == null) {
/*     */       return;
/*     */     }
/*     */     
/* 292 */     try { file = File.createTempFile(sourceCrc, null);
/* 293 */       if (file.delete() && loadFile(sourcePath, sourceCrc, file) == null)
/* 294 */         return;  } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */     
/* 298 */     file = new File(System.getProperty("user.home") + "/.libgdx/" + sourceCrc, fileName);
/* 299 */     if (loadFile(sourcePath, sourceCrc, file) == null) {
/*     */       return;
/*     */     }
/* 302 */     file = new File(".temp/" + sourceCrc, fileName);
/* 303 */     if (loadFile(sourcePath, sourceCrc, file) == null) {
/*     */       return;
/*     */     }
/* 306 */     file = new File(System.getProperty("java.library.path"), sourcePath);
/* 307 */     if (file.exists()) {
/* 308 */       System.load(file.getAbsolutePath());
/*     */       
/*     */       return;
/*     */     } 
/* 312 */     throw new GdxRuntimeException(ex);
/*     */   }
/*     */ 
/*     */   
/*     */   private Throwable loadFile(String sourcePath, String sourceCrc, File extractedFile) {
/*     */     try {
/* 318 */       System.load(extractFile(sourcePath, sourceCrc, extractedFile).getAbsolutePath());
/* 319 */       return null;
/* 320 */     } catch (Throwable ex) {
/* 321 */       return ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void setLoaded(String libraryName) {
/* 327 */     loadedLibraries.add(libraryName);
/*     */   }
/*     */   
/*     */   public static synchronized boolean isLoaded(String libraryName) {
/* 331 */     return loadedLibraries.contains(libraryName);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\SharedLibraryLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */