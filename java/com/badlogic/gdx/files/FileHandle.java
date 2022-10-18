/*     */ package com.badlogic.gdx.files;
/*     */ 
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileHandle
/*     */ {
/*     */   protected File file;
/*     */   protected Files.FileType type;
/*     */   
/*     */   protected FileHandle() {}
/*     */   
/*     */   public FileHandle(String fileName) {
/*  62 */     this.file = new File(fileName);
/*  63 */     this.type = Files.FileType.Absolute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileHandle(File file) {
/*  70 */     this.file = file;
/*  71 */     this.type = Files.FileType.Absolute;
/*     */   }
/*     */   
/*     */   protected FileHandle(String fileName, Files.FileType type) {
/*  75 */     this.type = type;
/*  76 */     this.file = new File(fileName);
/*     */   }
/*     */   
/*     */   protected FileHandle(File file, Files.FileType type) {
/*  80 */     this.file = file;
/*  81 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String path() {
/*  87 */     return this.file.getPath().replace('\\', '/');
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  92 */     return this.file.getName();
/*     */   }
/*     */   
/*     */   public String extension() {
/*  96 */     String name = this.file.getName();
/*  97 */     int dotIndex = name.lastIndexOf('.');
/*  98 */     if (dotIndex == -1) return ""; 
/*  99 */     return name.substring(dotIndex + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String nameWithoutExtension() {
/* 104 */     String name = this.file.getName();
/* 105 */     int dotIndex = name.lastIndexOf('.');
/* 106 */     if (dotIndex == -1) return name; 
/* 107 */     return name.substring(0, dotIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String pathWithoutExtension() {
/* 113 */     String path = this.file.getPath().replace('\\', '/');
/* 114 */     int dotIndex = path.lastIndexOf('.');
/* 115 */     if (dotIndex == -1) return path; 
/* 116 */     return path.substring(0, dotIndex);
/*     */   }
/*     */   
/*     */   public Files.FileType type() {
/* 120 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public File file() {
/* 126 */     if (this.type == Files.FileType.External) return new File(Gdx.files.getExternalStoragePath(), this.file.getPath()); 
/* 127 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream read() {
/* 133 */     if (this.type == Files.FileType.Classpath || (this.type == Files.FileType.Internal && !file().exists()) || (this.type == Files.FileType.Local && 
/* 134 */       !file().exists())) {
/* 135 */       InputStream input = FileHandle.class.getResourceAsStream("/" + this.file.getPath().replace('\\', '/'));
/* 136 */       if (input == null) throw new GdxRuntimeException("File not found: " + this.file + " (" + this.type + ")"); 
/* 137 */       return input;
/*     */     } 
/*     */     try {
/* 140 */       return new FileInputStream(file());
/* 141 */     } catch (Exception ex) {
/* 142 */       if (file().isDirectory())
/* 143 */         throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex); 
/* 144 */       throw new GdxRuntimeException("Error reading file: " + this.file + " (" + this.type + ")", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedInputStream read(int bufferSize) {
/* 151 */     return new BufferedInputStream(read(), bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Reader reader() {
/* 157 */     return new InputStreamReader(read());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Reader reader(String charset) {
/* 163 */     InputStream stream = read();
/*     */     try {
/* 165 */       return new InputStreamReader(stream, charset);
/* 166 */     } catch (UnsupportedEncodingException ex) {
/* 167 */       StreamUtils.closeQuietly(stream);
/* 168 */       throw new GdxRuntimeException("Error reading file: " + this, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedReader reader(int bufferSize) {
/* 175 */     return new BufferedReader(new InputStreamReader(read()), bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedReader reader(int bufferSize, String charset) {
/*     */     try {
/* 182 */       return new BufferedReader(new InputStreamReader(read(), charset), bufferSize);
/* 183 */     } catch (UnsupportedEncodingException ex) {
/* 184 */       throw new GdxRuntimeException("Error reading file: " + this, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String readString() {
/* 191 */     return readString(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readString(String charset) {
/* 198 */     StringBuilder output = new StringBuilder(estimateLength());
/* 199 */     InputStreamReader reader = null;
/*     */     try {
/* 201 */       if (charset == null) {
/* 202 */         reader = new InputStreamReader(read());
/*     */       } else {
/* 204 */         reader = new InputStreamReader(read(), charset);
/* 205 */       }  char[] buffer = new char[256];
/*     */       while (true) {
/* 207 */         int length = reader.read(buffer);
/* 208 */         if (length == -1)
/* 209 */           break;  output.append(buffer, 0, length);
/*     */       } 
/* 211 */     } catch (IOException ex) {
/* 212 */       throw new GdxRuntimeException("Error reading layout file: " + this, ex);
/*     */     } finally {
/* 214 */       StreamUtils.closeQuietly(reader);
/*     */     } 
/* 216 */     return output.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] readBytes() {
/* 222 */     InputStream input = read();
/*     */     try {
/* 224 */       return StreamUtils.copyStreamToByteArray(input, estimateLength());
/* 225 */     } catch (IOException ex) {
/* 226 */       throw new GdxRuntimeException("Error reading file: " + this, ex);
/*     */     } finally {
/* 228 */       StreamUtils.closeQuietly(input);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int estimateLength() {
/* 233 */     int length = (int)length();
/* 234 */     return (length != 0) ? length : 512;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int readBytes(byte[] bytes, int offset, int size) {
/* 243 */     InputStream input = read();
/* 244 */     int position = 0;
/*     */     try {
/*     */       while (true) {
/* 247 */         int count = input.read(bytes, offset + position, size - position);
/* 248 */         if (count <= 0)
/* 249 */           break;  position += count;
/*     */       } 
/* 251 */     } catch (IOException ex) {
/* 252 */       throw new GdxRuntimeException("Error reading file: " + this, ex);
/*     */     } finally {
/* 254 */       StreamUtils.closeQuietly(input);
/*     */     } 
/* 256 */     return position - offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream write(boolean append) {
/* 264 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot write to a classpath file: " + this.file); 
/* 265 */     if (this.type == Files.FileType.Internal) throw new GdxRuntimeException("Cannot write to an internal file: " + this.file); 
/* 266 */     parent().mkdirs();
/*     */     try {
/* 268 */       return new FileOutputStream(file(), append);
/* 269 */     } catch (Exception ex) {
/* 270 */       if (file().isDirectory())
/* 271 */         throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex); 
/* 272 */       throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream write(boolean append, int bufferSize) {
/* 282 */     return new BufferedOutputStream(write(append), bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(InputStream input, boolean append) {
/* 291 */     OutputStream output = null;
/*     */     try {
/* 293 */       output = write(append);
/* 294 */       StreamUtils.copyStream(input, output);
/* 295 */     } catch (Exception ex) {
/* 296 */       throw new GdxRuntimeException("Error stream writing to file: " + this.file + " (" + this.type + ")", ex);
/*     */     } finally {
/* 298 */       StreamUtils.closeQuietly(input);
/* 299 */       StreamUtils.closeQuietly(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Writer writer(boolean append) {
/* 309 */     return writer(append, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Writer writer(boolean append, String charset) {
/* 318 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot write to a classpath file: " + this.file); 
/* 319 */     if (this.type == Files.FileType.Internal) throw new GdxRuntimeException("Cannot write to an internal file: " + this.file); 
/* 320 */     parent().mkdirs();
/*     */     try {
/* 322 */       FileOutputStream output = new FileOutputStream(file(), append);
/* 323 */       if (charset == null) {
/* 324 */         return new OutputStreamWriter(output);
/*     */       }
/* 326 */       return new OutputStreamWriter(output, charset);
/* 327 */     } catch (IOException ex) {
/* 328 */       if (file().isDirectory())
/* 329 */         throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex); 
/* 330 */       throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeString(String string, boolean append) {
/* 339 */     writeString(string, append, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeString(String string, boolean append, String charset) {
/* 348 */     Writer writer = null;
/*     */     try {
/* 350 */       writer = writer(append, charset);
/* 351 */       writer.write(string);
/* 352 */     } catch (Exception ex) {
/* 353 */       throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
/*     */     } finally {
/* 355 */       StreamUtils.closeQuietly(writer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeBytes(byte[] bytes, boolean append) {
/* 364 */     OutputStream output = write(append);
/*     */     try {
/* 366 */       output.write(bytes);
/* 367 */     } catch (IOException ex) {
/* 368 */       throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
/*     */     } finally {
/* 370 */       StreamUtils.closeQuietly(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeBytes(byte[] bytes, int offset, int length, boolean append) {
/* 379 */     OutputStream output = write(append);
/*     */     try {
/* 381 */       output.write(bytes, offset, length);
/* 382 */     } catch (IOException ex) {
/* 383 */       throw new GdxRuntimeException("Error writing file: " + this.file + " (" + this.type + ")", ex);
/*     */     } finally {
/* 385 */       StreamUtils.closeQuietly(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileHandle[] list() {
/* 394 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot list a classpath directory: " + this.file); 
/* 395 */     String[] relativePaths = file().list();
/* 396 */     if (relativePaths == null) return new FileHandle[0]; 
/* 397 */     FileHandle[] handles = new FileHandle[relativePaths.length];
/* 398 */     for (int i = 0, n = relativePaths.length; i < n; i++)
/* 399 */       handles[i] = child(relativePaths[i]); 
/* 400 */     return handles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileHandle[] list(FileFilter filter) {
/* 409 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot list a classpath directory: " + this.file); 
/* 410 */     File file = file();
/* 411 */     String[] relativePaths = file.list();
/* 412 */     if (relativePaths == null) return new FileHandle[0]; 
/* 413 */     FileHandle[] handles = new FileHandle[relativePaths.length];
/* 414 */     int count = 0;
/* 415 */     for (int i = 0, n = relativePaths.length; i < n; i++) {
/* 416 */       String path = relativePaths[i];
/* 417 */       FileHandle child = child(path);
/* 418 */       if (filter.accept(child.file())) {
/* 419 */         handles[count] = child;
/* 420 */         count++;
/*     */       } 
/* 422 */     }  if (count < relativePaths.length) {
/* 423 */       FileHandle[] newHandles = new FileHandle[count];
/* 424 */       System.arraycopy(handles, 0, newHandles, 0, count);
/* 425 */       handles = newHandles;
/*     */     } 
/* 427 */     return handles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileHandle[] list(FilenameFilter filter) {
/* 436 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot list a classpath directory: " + this.file); 
/* 437 */     File file = file();
/* 438 */     String[] relativePaths = file.list();
/* 439 */     if (relativePaths == null) return new FileHandle[0]; 
/* 440 */     FileHandle[] handles = new FileHandle[relativePaths.length];
/* 441 */     int count = 0;
/* 442 */     for (int i = 0, n = relativePaths.length; i < n; i++) {
/* 443 */       String path = relativePaths[i];
/* 444 */       if (filter.accept(file, path)) {
/* 445 */         handles[count] = child(path);
/* 446 */         count++;
/*     */       } 
/* 448 */     }  if (count < relativePaths.length) {
/* 449 */       FileHandle[] newHandles = new FileHandle[count];
/* 450 */       System.arraycopy(handles, 0, newHandles, 0, count);
/* 451 */       handles = newHandles;
/*     */     } 
/* 453 */     return handles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileHandle[] list(String suffix) {
/* 461 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot list a classpath directory: " + this.file); 
/* 462 */     String[] relativePaths = file().list();
/* 463 */     if (relativePaths == null) return new FileHandle[0]; 
/* 464 */     FileHandle[] handles = new FileHandle[relativePaths.length];
/* 465 */     int count = 0;
/* 466 */     for (int i = 0, n = relativePaths.length; i < n; i++) {
/* 467 */       String path = relativePaths[i];
/* 468 */       if (path.endsWith(suffix)) {
/* 469 */         handles[count] = child(path);
/* 470 */         count++;
/*     */       } 
/* 472 */     }  if (count < relativePaths.length) {
/* 473 */       FileHandle[] newHandles = new FileHandle[count];
/* 474 */       System.arraycopy(handles, 0, newHandles, 0, count);
/* 475 */       handles = newHandles;
/*     */     } 
/* 477 */     return handles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 484 */     if (this.type == Files.FileType.Classpath) return false; 
/* 485 */     return file().isDirectory();
/*     */   }
/*     */ 
/*     */   
/*     */   public FileHandle child(String name) {
/* 490 */     if (this.file.getPath().length() == 0) return new FileHandle(new File(name), this.type); 
/* 491 */     return new FileHandle(new File(this.file, name), this.type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileHandle sibling(String name) {
/* 497 */     if (this.file.getPath().length() == 0) throw new GdxRuntimeException("Cannot get the sibling of the root."); 
/* 498 */     return new FileHandle(new File(this.file.getParent(), name), this.type);
/*     */   }
/*     */   
/*     */   public FileHandle parent() {
/* 502 */     File parent = this.file.getParentFile();
/* 503 */     if (parent == null)
/* 504 */       if (this.type == Files.FileType.Absolute) {
/* 505 */         parent = new File("/");
/*     */       } else {
/* 507 */         parent = new File("");
/*     */       }  
/* 509 */     return new FileHandle(parent, this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mkdirs() {
/* 514 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot mkdirs with a classpath file: " + this.file); 
/* 515 */     if (this.type == Files.FileType.Internal) throw new GdxRuntimeException("Cannot mkdirs with an internal file: " + this.file); 
/* 516 */     file().mkdirs();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 522 */     switch (this.type) {
/*     */       case Internal:
/* 524 */         if (file().exists()) return true;
/*     */       
/*     */       case Classpath:
/* 527 */         return (FileHandle.class.getResource("/" + this.file.getPath().replace('\\', '/')) != null);
/*     */     } 
/* 529 */     return file().exists();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean delete() {
/* 535 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot delete a classpath file: " + this.file); 
/* 536 */     if (this.type == Files.FileType.Internal) throw new GdxRuntimeException("Cannot delete an internal file: " + this.file); 
/* 537 */     return file().delete();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean deleteDirectory() {
/* 543 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot delete a classpath file: " + this.file); 
/* 544 */     if (this.type == Files.FileType.Internal) throw new GdxRuntimeException("Cannot delete an internal file: " + this.file); 
/* 545 */     return deleteDirectory(file());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void emptyDirectory() {
/* 551 */     emptyDirectory(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void emptyDirectory(boolean preserveTree) {
/* 557 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot delete a classpath file: " + this.file); 
/* 558 */     if (this.type == Files.FileType.Internal) throw new GdxRuntimeException("Cannot delete an internal file: " + this.file); 
/* 559 */     emptyDirectory(file(), preserveTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyTo(FileHandle dest) {
/* 571 */     boolean sourceDir = isDirectory();
/* 572 */     if (!sourceDir) {
/* 573 */       if (dest.isDirectory()) dest = dest.child(name()); 
/* 574 */       copyFile(this, dest);
/*     */       return;
/*     */     } 
/* 577 */     if (dest.exists()) {
/* 578 */       if (!dest.isDirectory()) throw new GdxRuntimeException("Destination exists but is not a directory: " + dest); 
/*     */     } else {
/* 580 */       dest.mkdirs();
/* 581 */       if (!dest.isDirectory()) throw new GdxRuntimeException("Destination directory cannot be created: " + dest); 
/*     */     } 
/* 583 */     if (!sourceDir) dest = dest.child(name()); 
/* 584 */     copyDirectory(this, dest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void moveTo(FileHandle dest) {
/* 591 */     if (this.type == Files.FileType.Classpath) throw new GdxRuntimeException("Cannot move a classpath file: " + this.file); 
/* 592 */     if (this.type == Files.FileType.Internal) throw new GdxRuntimeException("Cannot move an internal file: " + this.file); 
/* 593 */     copyTo(dest);
/* 594 */     delete();
/* 595 */     if (exists() && isDirectory()) deleteDirectory();
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public long length() {
/* 601 */     if (this.type == Files.FileType.Classpath || (this.type == Files.FileType.Internal && !this.file.exists())) {
/* 602 */       InputStream input = read();
/*     */       
/* 604 */       try { return input.available(); }
/* 605 */       catch (Exception exception) {  }
/*     */       finally
/* 607 */       { StreamUtils.closeQuietly(input); }
/*     */       
/* 609 */       return 0L;
/*     */     } 
/* 611 */     return file().length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastModified() {
/* 618 */     return file().lastModified();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 623 */     if (!(obj instanceof FileHandle)) return false; 
/* 624 */     FileHandle other = (FileHandle)obj;
/* 625 */     return (this.type == other.type && path().equals(other.path()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 630 */     int hash = 1;
/* 631 */     hash = hash * 37 + this.type.hashCode();
/* 632 */     hash = hash * 67 + path().hashCode();
/* 633 */     return hash;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 637 */     return this.file.getPath().replace('\\', '/');
/*     */   }
/*     */   
/*     */   public static FileHandle tempFile(String prefix) {
/*     */     try {
/* 642 */       return new FileHandle(File.createTempFile(prefix, null));
/* 643 */     } catch (IOException ex) {
/* 644 */       throw new GdxRuntimeException("Unable to create temp file.", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static FileHandle tempDirectory(String prefix) {
/*     */     try {
/* 650 */       File file = File.createTempFile(prefix, null);
/* 651 */       if (!file.delete()) throw new IOException("Unable to delete temp file: " + file); 
/* 652 */       if (!file.mkdir()) throw new IOException("Unable to create temp directory: " + file); 
/* 653 */       return new FileHandle(file);
/* 654 */     } catch (IOException ex) {
/* 655 */       throw new GdxRuntimeException("Unable to create temp file.", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emptyDirectory(File file, boolean preserveTree) {
/* 660 */     if (file.exists()) {
/* 661 */       File[] files = file.listFiles();
/* 662 */       if (files != null)
/* 663 */         for (int i = 0, n = files.length; i < n; i++) {
/* 664 */           if (!files[i].isDirectory()) {
/* 665 */             files[i].delete();
/* 666 */           } else if (preserveTree) {
/* 667 */             emptyDirectory(files[i], true);
/*     */           } else {
/* 669 */             deleteDirectory(files[i]);
/*     */           } 
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean deleteDirectory(File file) {
/* 676 */     emptyDirectory(file, false);
/* 677 */     return file.delete();
/*     */   }
/*     */   
/*     */   private static void copyFile(FileHandle source, FileHandle dest) {
/*     */     try {
/* 682 */       dest.write(source.read(), false);
/* 683 */     } catch (Exception ex) {
/* 684 */       throw new GdxRuntimeException("Error copying source file: " + source.file + " (" + source.type + ")\nTo destination: " + dest.file + " (" + dest.type + ")", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void copyDirectory(FileHandle sourceDir, FileHandle destDir) {
/* 690 */     destDir.mkdirs();
/* 691 */     FileHandle[] files = sourceDir.list();
/* 692 */     for (int i = 0, n = files.length; i < n; i++) {
/* 693 */       FileHandle srcFile = files[i];
/* 694 */       FileHandle destFile = destDir.child(srcFile.name());
/* 695 */       if (srcFile.isDirectory()) {
/* 696 */         copyDirectory(srcFile, destFile);
/*     */       } else {
/* 698 */         copyFile(srcFile, destFile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\files\FileHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */