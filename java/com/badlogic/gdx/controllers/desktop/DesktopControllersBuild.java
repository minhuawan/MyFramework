/*    */ package com.badlogic.gdx.controllers.desktop;
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
/*    */ public class DesktopControllersBuild
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 28 */     (new NativeCodeGenerator()).generate("src/", "bin/", "jni/");
/* 29 */     BuildConfig buildConfig = new BuildConfig("gdx-controllers-desktop");
/*    */     
/* 31 */     String[] windowsSrc = { "*.cpp", "ois-v1-4svn/src/*.cpp", "ois-v1-4svn/src/win32/*.cpp" };
/*    */     
/* 33 */     String[] linuxSrc = { "*.cpp", "ois-v1-4svn/src/*.cpp", "ois-v1-4svn/src/linux/*.cpp" };
/*    */     
/* 35 */     String[] mac64Src = { "*.cpp", "ois-v1-4svn/src/*.cpp", "ois-v1-4svn/src/mac/*.mm", "ois-v1-4svn/src/mac/MacHIDManager.cpp", "ois-v1-4svn/src/mac/MacJoyStick.cpp" };
/*    */ 
/*    */     
/* 38 */     String[] includes = { "ois-v1-4svn/includes", "dinput/" };
/*    */     
/* 40 */     BuildTarget win32home = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, false);
/* 41 */     win32home.buildFileName = "build-windows32home.xml";
/* 42 */     win32home.excludeFromMasterBuildFile = true;
/* 43 */     win32home.is64Bit = false;
/* 44 */     win32home.compilerPrefix = "";
/* 45 */     win32home.cppIncludes = windowsSrc;
/* 46 */     win32home.headerDirs = includes;
/* 47 */     win32home.cIncludes = new String[0];
/* 48 */     win32home.libraries = "-ldinput8 -ldxguid";
/*    */     
/* 50 */     BuildTarget win32 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, false);
/* 51 */     win32.cppIncludes = windowsSrc;
/* 52 */     win32.headerDirs = includes;
/* 53 */     win32.libraries = "-ldinput8 -ldxguid";
/*    */     
/* 55 */     BuildTarget win64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Windows, true);
/* 56 */     win64.cppIncludes = windowsSrc;
/* 57 */     win64.headerDirs = includes;
/* 58 */     win64.libraries = "-ldinput8 -ldxguid";
/*    */     
/* 60 */     BuildTarget lin32 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, false);
/* 61 */     lin32.cppIncludes = linuxSrc;
/* 62 */     lin32.headerDirs = includes;
/* 63 */     lin32.libraries = "-lX11";
/*    */     
/* 65 */     BuildTarget lin64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.Linux, true);
/* 66 */     lin64.cppIncludes = linuxSrc;
/* 67 */     lin64.headerDirs = includes;
/* 68 */     lin64.libraries = "-lX11";
/*    */     
/* 70 */     BuildTarget mac = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.MacOsX, false);
/* 71 */     mac.cppIncludes = mac64Src;
/* 72 */     mac.headerDirs = includes;
/* 73 */     mac.cppFlags += " -x objective-c++";
/* 74 */     mac.libraries = "-framework CoreServices -framework Carbon -framework IOKit -framework Cocoa";
/*    */     
/* 76 */     BuildTarget mac64 = BuildTarget.newDefaultTarget(BuildTarget.TargetOs.MacOsX, true);
/* 77 */     mac64.cppIncludes = mac64Src;
/* 78 */     mac64.headerDirs = includes;
/* 79 */     mac64.cppFlags += " -x objective-c++";
/* 80 */     mac64.libraries = "-framework CoreServices -framework Carbon -framework IOKit -framework Cocoa";
/*    */     
/* 82 */     (new AntScriptGenerator()).generate(buildConfig, new BuildTarget[] { win32home, win32, win64, lin32, lin64, mac, mac64 });
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\desktop\DesktopControllersBuild.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */