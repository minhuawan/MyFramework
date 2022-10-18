/*     */ package com.badlogic.gdx.graphics.g2d.freetype;
/*     */ 
/*     */ import com.badlogic.gdx.jnigen.AntScriptGenerator;
/*     */ import com.badlogic.gdx.jnigen.BuildConfig;
/*     */ import com.badlogic.gdx.jnigen.BuildTarget;
/*     */ import com.badlogic.gdx.jnigen.NativeCodeGenerator;
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
/*     */ public class FreetypeBuild
/*     */ {
/*     */   public static void main(String[] args) throws Exception {
/*  28 */     String[] headers = { "freetype-2.6.2/include" };
/*  29 */     String[] sources = { "freetype-2.6.2/src/base/ftsystem.c", "freetype-2.6.2/src/base/ftinit.c", "freetype-2.6.2/src/base/ftdebug.c", "freetype-2.6.2/src/base/ftbase.c", "freetype-2.6.2/src/base/ftbbox.c", "freetype-2.6.2/src/base/ftglyph.c", "freetype-2.6.2/src/base/ftbdf.c", "freetype-2.6.2/src/base/ftbitmap.c", "freetype-2.6.2/src/base/ftcid.c", "freetype-2.6.2/src/base/ftfstype.c", "freetype-2.6.2/src/base/ftgasp.c", "freetype-2.6.2/src/base/ftgxval.c", "freetype-2.6.2/src/base/ftlcdfil.c", "freetype-2.6.2/src/base/ftmm.c", "freetype-2.6.2/src/base/ftotval.c", "freetype-2.6.2/src/base/ftpatent.c", "freetype-2.6.2/src/base/ftpfr.c", "freetype-2.6.2/src/base/ftstroke.c", "freetype-2.6.2/src/base/ftsynth.c", "freetype-2.6.2/src/base/fttype1.c", "freetype-2.6.2/src/base/ftwinfnt.c", "freetype-2.6.2/src/base/ftxf86.c", "freetype-2.6.2/src/bdf/bdf.c", "freetype-2.6.2/src/cff/cff.c", "freetype-2.6.2/src/cid/type1cid.c", "freetype-2.6.2/src/pcf/pcf.c", "freetype-2.6.2/src/pfr/pfr.c", "freetype-2.6.2/src/sfnt/sfnt.c", "freetype-2.6.2/src/truetype/truetype.c", "freetype-2.6.2/src/type1/type1.c", "freetype-2.6.2/src/type42/type42.c", "freetype-2.6.2/src/winfonts/winfnt.c", "freetype-2.6.2/src/raster/raster.c", "freetype-2.6.2/src/smooth/smooth.c", "freetype-2.6.2/src/autofit/autofit.c", "freetype-2.6.2/src/cache/ftcache.c", "freetype-2.6.2/src/gzip/ftgzip.c", "freetype-2.6.2/src/lzw/ftlzw.c", "freetype-2.6.2/src/bzip2/ftbzip2.c", "freetype-2.6.2/src/gxvalid/gxvalid.c", "freetype-2.6.2/src/otvalid/otvalid.c", "freetype-2.6.2/src/psaux/psaux.c", "freetype-2.6.2/src/pshinter/pshinter.c", "freetype-2.6.2/src/psnames/psnames.c" };
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
/*  71 */     BuildTarget win32home = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, false);
/*  72 */     win32home.compilerPrefix = "";
/*  73 */     win32home.buildFileName = "build-windows32home.xml";
/*  74 */     win32home.excludeFromMasterBuildFile = true;
/*  75 */     win32home.headerDirs = headers;
/*  76 */     win32home.cIncludes = sources;
/*  77 */     win32home.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*  78 */     win32home.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*     */     
/*  80 */     BuildTarget win32 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, false);
/*  81 */     win32.headerDirs = headers;
/*  82 */     win32.cIncludes = sources;
/*  83 */     win32.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*  84 */     win32.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*     */     
/*  86 */     BuildTarget win64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, true);
/*  87 */     win64.headerDirs = headers;
/*  88 */     win64.cIncludes = sources;
/*  89 */     win64.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*  90 */     win64.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*     */     
/*  92 */     BuildTarget lin32 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, false);
/*  93 */     lin32.headerDirs = headers;
/*  94 */     lin32.cIncludes = sources;
/*  95 */     lin32.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*  96 */     lin32.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*     */     
/*  98 */     BuildTarget lin64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, true);
/*  99 */     lin64.headerDirs = headers;
/* 100 */     lin64.cIncludes = sources;
/* 101 */     lin64.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/* 102 */     lin64.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*     */     
/* 104 */     BuildTarget mac = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.MacOsX, false);
/* 105 */     mac.headerDirs = headers;
/* 106 */     mac.cIncludes = sources;
/* 107 */     mac.cFlags += " -DFT2_BUILD_LIBRARY";
/* 108 */     mac.cppFlags += " -DFT2_BUILD_LIBRARY";
/* 109 */     mac.linkerFlags += " -framework CoreServices -framework Carbon";
/*     */     
/* 111 */     BuildTarget mac64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.MacOsX, true);
/* 112 */     mac64.headerDirs = headers;
/* 113 */     mac64.cIncludes = sources;
/* 114 */     mac64.cFlags += " -DFT2_BUILD_LIBRARY";
/* 115 */     mac64.cppFlags += " -DFT2_BUILD_LIBRARY";
/* 116 */     mac64.linkerFlags += " -framework CoreServices -framework Carbon";
/*     */     
/* 118 */     BuildTarget android = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Android, false);
/* 119 */     android.headerDirs = headers;
/* 120 */     android.cIncludes = sources;
/* 121 */     android.cFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/* 122 */     android.cppFlags += " -std=c99 -DFT2_BUILD_LIBRARY";
/*     */     
/* 124 */     BuildTarget ios = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.IOS, false);
/* 125 */     ios.headerDirs = headers;
/* 126 */     ios.cIncludes = sources;
/* 127 */     ios.cFlags += " -DFT2_BUILD_LIBRARY";
/* 128 */     ios.cppFlags += " -DFT2_BUILD_LIBRARY";
/*     */     
/* 130 */     (new NativeCodeGenerator()).generate();
/* 131 */     (new AntScriptGenerator())
/* 132 */       .generate(new BuildConfig("gdx-freetype"), new BuildTarget[] { win32home, win32, win64, lin32, lin64, mac, mac64, android, ios });
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\freetype\FreetypeBuild.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */