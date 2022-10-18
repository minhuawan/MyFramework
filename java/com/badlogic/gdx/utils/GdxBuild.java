/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ import com.badlogic.gdx.jnigen.AntScriptGenerator;
/*    */ import com.badlogic.gdx.jnigen.BuildConfig;
/*    */ import com.badlogic.gdx.jnigen.BuildTarget;
/*    */ import com.badlogic.gdx.jnigen.NativeCodeGenerator;
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
/*    */ public class GdxBuild
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 30 */     String JNI_DIR = "jni";
/* 31 */     String LIBS_DIR = "libs";
/*    */ 
/*    */     
/* 34 */     (new NativeCodeGenerator()).generate("src", "bin", JNI_DIR, new String[] { "**/*" }, null);
/*    */     
/* 36 */     String[] excludeCpp = { "android/**", "iosgl/**" };
/*    */ 
/*    */ 
/*    */     
/* 40 */     BuildTarget win32home = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, false);
/* 41 */     win32home.compilerPrefix = "";
/* 42 */     win32home.buildFileName = "build-windows32home.xml";
/* 43 */     win32home.excludeFromMasterBuildFile = true;
/* 44 */     win32home.cppExcludes = excludeCpp;
/* 45 */     BuildTarget win32 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, false);
/* 46 */     win32.cppExcludes = excludeCpp;
/* 47 */     BuildTarget win64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, true);
/* 48 */     win64.cppExcludes = excludeCpp;
/* 49 */     BuildTarget lin32 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, false);
/* 50 */     lin32.cppExcludes = excludeCpp;
/* 51 */     BuildTarget lin64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, true);
/* 52 */     lin64.cppExcludes = excludeCpp;
/* 53 */     BuildTarget android = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Android, false);
/* 54 */     android.linkerFlags += " -lGLESv2 -llog";
/* 55 */     android.cppExcludes = new String[] { "iosgl/**" };
/* 56 */     BuildTarget mac = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.MacOsX, false);
/* 57 */     mac.cppExcludes = excludeCpp;
/* 58 */     BuildTarget mac64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.MacOsX, true);
/* 59 */     mac64.cppExcludes = excludeCpp;
/* 60 */     BuildTarget ios = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.IOS, false);
/* 61 */     ios.cppExcludes = new String[] { "android/**" };
/* 62 */     ios.headerDirs = new String[] { "iosgl" };
/* 63 */     (new AntScriptGenerator()).generate(new BuildConfig("gdx", "../target/native", LIBS_DIR, JNI_DIR), new BuildTarget[] { mac, mac64, win32home, win32, win64, lin32, lin64, android, ios });
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\GdxBuild.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */