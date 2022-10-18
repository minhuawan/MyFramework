/*     */ package com.codedisaster.steamworks;
/*     */ import java.io.File;
/*     */ 
/*     */ class SteamSharedLibraryLoader {
/*     */   private static final PLATFORM OS;
/*     */   private static final boolean IS_64_BIT;
/*     */   
/*     */   enum PLATFORM {
/*   9 */     Windows,
/*  10 */     Linux,
/*  11 */     MacOS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  17 */   private static final String SHARED_LIBRARY_EXTRACT_DIRECTORY = System.getProperty("com.codedisaster.steamworks.SharedLibraryExtractDirectory", "steamworks4j");
/*     */ 
/*     */   
/*  20 */   private static final String SHARED_LIBRARY_EXTRACT_PATH = System.getProperty("com.codedisaster.steamworks.SharedLibraryExtractPath", null);
/*     */ 
/*     */   
/*  23 */   private static final String SDK_REDISTRIBUTABLE_BIN_PATH = System.getProperty("com.codedisaster.steamworks.SDKRedistributableBinPath", "sdk/redistributable_bin");
/*     */ 
/*     */   
/*  26 */   private static final String SDK_LIBRARY_PATH = System.getProperty("com.codedisaster.steamworks.SDKLibraryPath", "sdk/public/steam/lib");
/*     */ 
/*     */   
/*  29 */   static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("com.codedisaster.steamworks.Debug", "false"));
/*     */ 
/*     */   
/*     */   static {
/*  33 */     String osName = System.getProperty("os.name");
/*  34 */     String osArch = System.getProperty("os.arch");
/*     */     
/*  36 */     if (osName.contains("Windows")) {
/*  37 */       OS = PLATFORM.Windows;
/*  38 */     } else if (osName.contains("Linux")) {
/*  39 */       OS = PLATFORM.Linux;
/*  40 */     } else if (osName.contains("Mac")) {
/*  41 */       OS = PLATFORM.MacOS;
/*     */     } else {
/*  43 */       throw new RuntimeException("Unknown host architecture: " + osName + ", " + osArch);
/*     */     } 
/*     */     
/*  46 */     IS_64_BIT = (osArch.equals("amd64") || osArch.equals("x86_64"));
/*     */   }
/*     */   
/*     */   private static String getPlatformLibName(String libName) {
/*  50 */     switch (OS) {
/*     */       case Windows:
/*  52 */         return libName + (IS_64_BIT ? "64" : "") + ".dll";
/*     */       case Linux:
/*  54 */         return "lib" + libName + ".so";
/*     */       case MacOS:
/*  56 */         return "lib" + libName + ".dylib";
/*     */     } 
/*     */     
/*  59 */     throw new RuntimeException("Unknown host architecture");
/*     */   }
/*     */   
/*     */   static String getSdkRedistributableBinPath() {
/*     */     File path;
/*  64 */     switch (OS) {
/*     */       case Windows:
/*  66 */         path = new File(SDK_REDISTRIBUTABLE_BIN_PATH, IS_64_BIT ? "win64" : "");
/*     */         break;
/*     */       case Linux:
/*  69 */         path = new File(SDK_REDISTRIBUTABLE_BIN_PATH, "linux64");
/*     */         break;
/*     */       case MacOS:
/*  72 */         path = new File(SDK_REDISTRIBUTABLE_BIN_PATH, "osx");
/*     */         break;
/*     */       default:
/*  75 */         return null;
/*     */     } 
/*     */     
/*  78 */     return path.exists() ? path.getPath() : null;
/*     */   }
/*     */   
/*     */   static String getSdkLibraryPath() {
/*     */     File path;
/*  83 */     switch (OS) {
/*     */       case Windows:
/*  85 */         path = new File(SDK_LIBRARY_PATH, IS_64_BIT ? "win64" : "win32");
/*     */         break;
/*     */       case Linux:
/*  88 */         path = new File(SDK_LIBRARY_PATH, "linux64");
/*     */         break;
/*     */       case MacOS:
/*  91 */         path = new File(SDK_LIBRARY_PATH, "osx");
/*     */         break;
/*     */       default:
/*  94 */         return null;
/*     */     } 
/*     */     
/*  97 */     return path.exists() ? path.getPath() : null;
/*     */   }
/*     */   
/*     */   static void loadLibrary(String libraryName, String libraryPath) throws SteamException {
/*     */     try {
/* 102 */       String librarySystemName = getPlatformLibName(libraryName);
/*     */       
/* 104 */       File librarySystemPath = discoverExtractLocation(SHARED_LIBRARY_EXTRACT_DIRECTORY + "/" + 
/* 105 */           Version.getVersion(), librarySystemName);
/*     */       
/* 107 */       if (libraryPath == null) {
/*     */         
/* 109 */         extractLibrary(librarySystemPath, librarySystemName);
/*     */       } else {
/*     */         
/* 112 */         File librarySourcePath = new File(libraryPath, librarySystemName);
/*     */         
/* 114 */         if (OS != PLATFORM.Windows) {
/*     */           
/* 116 */           extractLibrary(librarySystemPath, librarySourcePath);
/*     */         } else {
/*     */           
/* 119 */           librarySystemPath = librarySourcePath;
/*     */         } 
/*     */       } 
/*     */       
/* 123 */       String absolutePath = librarySystemPath.getCanonicalPath();
/* 124 */       System.load(absolutePath);
/* 125 */     } catch (IOException e) {
/* 126 */       throw new SteamException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void extractLibrary(File librarySystemPath, String librarySystemName) throws IOException {
/* 131 */     extractLibrary(librarySystemPath, SteamSharedLibraryLoader.class
/* 132 */         .getResourceAsStream("/" + librarySystemName));
/*     */   }
/*     */   
/*     */   private static void extractLibrary(File librarySystemPath, File librarySourcePath) throws IOException {
/* 136 */     extractLibrary(librarySystemPath, new FileInputStream(librarySourcePath));
/*     */   }
/*     */   
/*     */   private static void extractLibrary(File librarySystemPath, InputStream input) throws IOException {
/* 140 */     if (input != null) {
/* 141 */       try (FileOutputStream output = new FileOutputStream(librarySystemPath)) {
/* 142 */         byte[] buffer = new byte[4096];
/*     */         while (true) {
/* 144 */           int length = input.read(buffer);
/* 145 */           if (length == -1)
/* 146 */             break;  output.write(buffer, 0, length);
/*     */         } 
/* 148 */         output.close();
/* 149 */       } catch (IOException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 154 */         if (!librarySystemPath.exists()) {
/* 155 */           throw e;
/*     */         }
/*     */       } finally {
/* 158 */         input.close();
/*     */       } 
/*     */     } else {
/* 161 */       throw new IOException("Failed to read input stream for " + librarySystemPath.getCanonicalPath());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File discoverExtractLocation(String folderName, String fileName) throws IOException {
/* 171 */     if (SHARED_LIBRARY_EXTRACT_PATH != null) {
/* 172 */       File file = new File(SHARED_LIBRARY_EXTRACT_PATH, fileName);
/* 173 */       if (canWrite(file)) {
/* 174 */         return file;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 180 */     File path = new File(System.getProperty("java.io.tmpdir") + "/" + folderName, fileName);
/* 181 */     if (canWrite(path)) {
/* 182 */       return path;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 188 */       File file = File.createTempFile(folderName, null);
/* 189 */       if (file.delete()) {
/*     */         
/* 191 */         path = new File(file, fileName);
/* 192 */         if (canWrite(path)) {
/* 193 */           return path;
/*     */         }
/*     */       } 
/* 196 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     path = new File(System.getProperty("user.home") + "/." + folderName, fileName);
/* 203 */     if (canWrite(path)) {
/* 204 */       return path;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 209 */     path = new File(".tmp/" + folderName, fileName);
/* 210 */     if (canWrite(path)) {
/* 211 */       return path;
/*     */     }
/*     */     
/* 214 */     throw new IOException("No suitable extraction path found");
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canWrite(File file) {
/* 219 */     File folder = file.getParentFile();
/*     */     
/* 221 */     if (file.exists()) {
/* 222 */       if (!file.canWrite() || !canExecute(file)) {
/* 223 */         return false;
/*     */       }
/*     */     } else {
/* 226 */       if (!folder.exists() && 
/* 227 */         !folder.mkdirs()) {
/* 228 */         return false;
/*     */       }
/*     */       
/* 231 */       if (!folder.isDirectory()) {
/* 232 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 236 */     File testFile = new File(folder, UUID.randomUUID().toString());
/*     */     
/*     */     try {
/* 239 */       (new FileOutputStream(testFile)).close();
/* 240 */       return canExecute(testFile);
/* 241 */     } catch (IOException e) {
/* 242 */       return false;
/*     */     } finally {
/* 244 */       testFile.delete();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canExecute(File file) {
/*     */     try {
/* 251 */       if (file.canExecute()) {
/* 252 */         return true;
/*     */       }
/*     */       
/* 255 */       if (file.setExecutable(true)) {
/* 256 */         return file.canExecute();
/*     */       }
/* 258 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 262 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\codedisaster\steamworks\SteamSharedLibraryLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */