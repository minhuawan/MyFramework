/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
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
/*     */ public class I18NBundle
/*     */ {
/*     */   private static final String DEFAULT_ENCODING = "UTF-8";
/*  71 */   private static final Locale ROOT_LOCALE = new Locale("", "", "");
/*     */ 
/*     */   
/*     */   private static boolean simpleFormatter = false;
/*     */ 
/*     */   
/*     */   private static boolean exceptionOnMissingKey = true;
/*     */ 
/*     */   
/*     */   private I18NBundle parent;
/*     */ 
/*     */   
/*     */   private Locale locale;
/*     */   
/*     */   private ObjectMap<String, String> properties;
/*     */   
/*     */   private TextFormatter formatter;
/*     */ 
/*     */   
/*     */   public static boolean getSimpleFormatter() {
/*  91 */     return simpleFormatter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setSimpleFormatter(boolean enabled) {
/*  97 */     simpleFormatter = enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getExceptionOnMissingKey() {
/* 104 */     return exceptionOnMissingKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setExceptionOnMissingKey(boolean enabled) {
/* 111 */     exceptionOnMissingKey = enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static I18NBundle createBundle(FileHandle baseFileHandle) {
/* 121 */     return createBundleImpl(baseFileHandle, Locale.getDefault(), "UTF-8");
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
/*     */   public static I18NBundle createBundle(FileHandle baseFileHandle, Locale locale) {
/* 133 */     return createBundleImpl(baseFileHandle, locale, "UTF-8");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static I18NBundle createBundle(FileHandle baseFileHandle, String encoding) {
/* 144 */     return createBundleImpl(baseFileHandle, Locale.getDefault(), encoding);
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
/*     */   
/*     */   public static I18NBundle createBundle(FileHandle baseFileHandle, Locale locale, String encoding) {
/* 157 */     return createBundleImpl(baseFileHandle, locale, encoding);
/*     */   }
/*     */   
/*     */   private static I18NBundle createBundleImpl(FileHandle baseFileHandle, Locale locale, String encoding) {
/* 161 */     if (baseFileHandle == null || locale == null || encoding == null) throw new NullPointerException();
/*     */     
/* 163 */     I18NBundle bundle = null;
/* 164 */     I18NBundle baseBundle = null;
/* 165 */     Locale targetLocale = locale;
/*     */     
/*     */     do {
/* 168 */       List<Locale> candidateLocales = getCandidateLocales(targetLocale);
/*     */ 
/*     */       
/* 171 */       bundle = loadBundleChain(baseFileHandle, encoding, candidateLocales, 0, baseBundle);
/*     */ 
/*     */       
/* 174 */       if (bundle != null) {
/* 175 */         Locale bundleLocale = bundle.getLocale();
/* 176 */         boolean isBaseBundle = bundleLocale.equals(ROOT_LOCALE);
/*     */         
/* 178 */         if (!isBaseBundle || bundleLocale.equals(locale)) {
/*     */           break;
/*     */         }
/*     */         
/* 182 */         if (candidateLocales.size() == 1 && bundleLocale.equals(candidateLocales.get(0))) {
/*     */           break;
/*     */         }
/*     */         
/* 186 */         if (isBaseBundle && baseBundle == null)
/*     */         {
/* 188 */           baseBundle = bundle;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 193 */       targetLocale = getFallbackLocale(targetLocale);
/*     */     }
/* 195 */     while (targetLocale != null);
/*     */     
/* 197 */     if (bundle == null) {
/* 198 */       if (baseBundle == null)
/*     */       {
/* 200 */         throw new MissingResourceException("Can't find bundle for base file handle " + baseFileHandle.path() + ", locale " + locale, baseFileHandle + "_" + locale, "");
/*     */       }
/*     */ 
/*     */       
/* 204 */       bundle = baseBundle;
/*     */     } 
/*     */     
/* 207 */     return bundle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Locale> getCandidateLocales(Locale locale) {
/* 259 */     String language = locale.getLanguage();
/* 260 */     String country = locale.getCountry();
/* 261 */     String variant = locale.getVariant();
/*     */     
/* 263 */     List<Locale> locales = new ArrayList<Locale>(4);
/* 264 */     if (variant.length() > 0) {
/* 265 */       locales.add(locale);
/*     */     }
/* 267 */     if (country.length() > 0) {
/* 268 */       locales.add((locales.size() == 0) ? locale : new Locale(language, country));
/*     */     }
/* 270 */     if (language.length() > 0) {
/* 271 */       locales.add((locales.size() == 0) ? locale : new Locale(language));
/*     */     }
/* 273 */     locales.add(ROOT_LOCALE);
/* 274 */     return locales;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Locale getFallbackLocale(Locale locale) {
/* 291 */     Locale defaultLocale = Locale.getDefault();
/* 292 */     return locale.equals(defaultLocale) ? null : defaultLocale;
/*     */   }
/*     */ 
/*     */   
/*     */   private static I18NBundle loadBundleChain(FileHandle baseFileHandle, String encoding, List<Locale> candidateLocales, int candidateIndex, I18NBundle baseBundle) {
/* 297 */     Locale targetLocale = candidateLocales.get(candidateIndex);
/* 298 */     I18NBundle parent = null;
/* 299 */     if (candidateIndex != candidateLocales.size() - 1) {
/*     */       
/* 301 */       parent = loadBundleChain(baseFileHandle, encoding, candidateLocales, candidateIndex + 1, baseBundle);
/* 302 */     } else if (baseBundle != null && targetLocale.equals(ROOT_LOCALE)) {
/* 303 */       return baseBundle;
/*     */     } 
/*     */ 
/*     */     
/* 307 */     I18NBundle bundle = loadBundle(baseFileHandle, encoding, targetLocale);
/* 308 */     if (bundle != null) {
/* 309 */       bundle.parent = parent;
/* 310 */       return bundle;
/*     */     } 
/*     */     
/* 313 */     return parent;
/*     */   }
/*     */ 
/*     */   
/*     */   private static I18NBundle loadBundle(FileHandle baseFileHandle, String encoding, Locale targetLocale) {
/* 318 */     I18NBundle bundle = null;
/* 319 */     Reader reader = null;
/*     */     try {
/* 321 */       FileHandle fileHandle = toFileHandle(baseFileHandle, targetLocale);
/* 322 */       if (checkFileExistence(fileHandle)) {
/*     */         
/* 324 */         bundle = new I18NBundle();
/*     */ 
/*     */         
/* 327 */         reader = fileHandle.reader(encoding);
/* 328 */         bundle.load(reader);
/*     */       } 
/* 330 */     } catch (IOException e) {
/* 331 */       throw new GdxRuntimeException(e);
/*     */     } finally {
/*     */       
/* 334 */       StreamUtils.closeQuietly(reader);
/*     */     } 
/* 336 */     if (bundle != null) {
/* 337 */       bundle.setLocale(targetLocale);
/*     */     }
/*     */     
/* 340 */     return bundle;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean checkFileExistence(FileHandle fh) {
/*     */     try {
/* 347 */       fh.read().close();
/* 348 */       return true;
/* 349 */     } catch (Exception e) {
/* 350 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void load(Reader reader) throws IOException {
/* 361 */     this.properties = new ObjectMap<String, String>();
/* 362 */     PropertiesUtils.load(this.properties, reader);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FileHandle toFileHandle(FileHandle baseFileHandle, Locale locale) {
/* 383 */     StringBuilder sb = new StringBuilder(baseFileHandle.name());
/* 384 */     if (!locale.equals(ROOT_LOCALE)) {
/* 385 */       String language = locale.getLanguage();
/* 386 */       String country = locale.getCountry();
/* 387 */       String variant = locale.getVariant();
/* 388 */       boolean emptyLanguage = "".equals(language);
/* 389 */       boolean emptyCountry = "".equals(country);
/* 390 */       boolean emptyVariant = "".equals(variant);
/*     */       
/* 392 */       if (!emptyLanguage || !emptyCountry || !emptyVariant) {
/* 393 */         sb.append('_');
/* 394 */         if (!emptyVariant) {
/* 395 */           sb.append(language).append('_').append(country).append('_').append(variant);
/* 396 */         } else if (!emptyCountry) {
/* 397 */           sb.append(language).append('_').append(country);
/*     */         } else {
/* 399 */           sb.append(language);
/*     */         } 
/*     */       } 
/*     */     } 
/* 403 */     return baseFileHandle.sibling(sb.append(".properties").toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 411 */     return this.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setLocale(Locale locale) {
/* 418 */     this.locale = locale;
/* 419 */     this.formatter = new TextFormatter(locale, !simpleFormatter);
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
/*     */   public final String get(String key) {
/* 431 */     String result = this.properties.get(key);
/* 432 */     if (result == null) {
/* 433 */       if (this.parent != null) result = this.parent.get(key); 
/* 434 */       if (result == null) {
/* 435 */         if (exceptionOnMissingKey) {
/* 436 */           throw new MissingResourceException("Can't find bundle key " + key, getClass().getName(), key);
/*     */         }
/* 438 */         return "???" + key + "???";
/*     */       } 
/*     */     } 
/* 441 */     return result;
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
/*     */   public String format(String key, Object... args) {
/* 453 */     return this.formatter.format(get(key), args);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\I18NBundle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */