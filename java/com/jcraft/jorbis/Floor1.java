/*     */ package com.jcraft.jorbis;
/*     */ 
/*     */ import com.jcraft.jogg.Buffer;
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
/*     */ class Floor1
/*     */   extends FuncFloor
/*     */ {
/*     */   static final int floor1_rangedb = 140;
/*     */   static final int VIF_POSIT = 63;
/*     */   
/*     */   void pack(Object i, Buffer opb) {
/*  36 */     InfoFloor1 info = (InfoFloor1)i;
/*     */     
/*  38 */     int count = 0;
/*     */     
/*  40 */     int maxposit = info.postlist[1];
/*  41 */     int maxclass = -1;
/*     */ 
/*     */     
/*  44 */     opb.write(info.partitions, 5); int j;
/*  45 */     for (j = 0; j < info.partitions; j++) {
/*  46 */       opb.write(info.partitionclass[j], 4);
/*  47 */       if (maxclass < info.partitionclass[j]) {
/*  48 */         maxclass = info.partitionclass[j];
/*     */       }
/*     */     } 
/*     */     
/*  52 */     for (j = 0; j < maxclass + 1; j++) {
/*  53 */       opb.write(info.class_dim[j] - 1, 3);
/*  54 */       opb.write(info.class_subs[j], 2);
/*  55 */       if (info.class_subs[j] != 0) {
/*  56 */         opb.write(info.class_book[j], 8);
/*     */       }
/*  58 */       for (int m = 0; m < 1 << info.class_subs[j]; m++) {
/*  59 */         opb.write(info.class_subbook[j][m] + 1, 8);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  64 */     opb.write(info.mult - 1, 2);
/*  65 */     opb.write(Util.ilog2(maxposit), 4);
/*  66 */     int rangebits = Util.ilog2(maxposit);
/*     */     int k;
/*  68 */     for (j = 0, k = 0; j < info.partitions; j++) {
/*  69 */       count += info.class_dim[info.partitionclass[j]];
/*  70 */       for (; k < count; k++) {
/*  71 */         opb.write(info.postlist[k + 2], rangebits);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   Object unpack(Info vi, Buffer opb) {
/*  77 */     int count = 0, maxclass = -1;
/*  78 */     InfoFloor1 info = new InfoFloor1();
/*     */ 
/*     */     
/*  81 */     info.partitions = opb.read(5); int j;
/*  82 */     for (j = 0; j < info.partitions; j++) {
/*  83 */       info.partitionclass[j] = opb.read(4);
/*  84 */       if (maxclass < info.partitionclass[j]) {
/*  85 */         maxclass = info.partitionclass[j];
/*     */       }
/*     */     } 
/*     */     
/*  89 */     for (j = 0; j < maxclass + 1; j++) {
/*  90 */       info.class_dim[j] = opb.read(3) + 1;
/*  91 */       info.class_subs[j] = opb.read(2);
/*  92 */       if (info.class_subs[j] < 0) {
/*  93 */         info.free();
/*  94 */         return null;
/*     */       } 
/*  96 */       if (info.class_subs[j] != 0) {
/*  97 */         info.class_book[j] = opb.read(8);
/*     */       }
/*  99 */       if (info.class_book[j] < 0 || info.class_book[j] >= vi.books) {
/* 100 */         info.free();
/* 101 */         return null;
/*     */       } 
/* 103 */       for (int i = 0; i < 1 << info.class_subs[j]; i++) {
/* 104 */         info.class_subbook[j][i] = opb.read(8) - 1;
/* 105 */         if (info.class_subbook[j][i] < -1 || info.class_subbook[j][i] >= vi.books) {
/* 106 */           info.free();
/* 107 */           return null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 113 */     info.mult = opb.read(2) + 1;
/* 114 */     int rangebits = opb.read(4);
/*     */     int k;
/* 116 */     for (j = 0, k = 0; j < info.partitions; j++) {
/* 117 */       count += info.class_dim[info.partitionclass[j]];
/* 118 */       for (; k < count; k++) {
/* 119 */         int t = info.postlist[k + 2] = opb.read(rangebits);
/* 120 */         if (t < 0 || t >= 1 << rangebits) {
/* 121 */           info.free();
/* 122 */           return null;
/*     */         } 
/*     */       } 
/*     */     } 
/* 126 */     info.postlist[0] = 0;
/* 127 */     info.postlist[1] = 1 << rangebits;
/*     */     
/* 129 */     return info;
/*     */   }
/*     */   
/*     */   Object look(DspState vd, InfoMode mi, Object i) {
/* 133 */     int _n = 0;
/*     */     
/* 135 */     int[] sortpointer = new int[65];
/*     */ 
/*     */ 
/*     */     
/* 139 */     InfoFloor1 info = (InfoFloor1)i;
/* 140 */     LookFloor1 look = new LookFloor1();
/* 141 */     look.vi = info;
/* 142 */     look.n = info.postlist[1];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int j;
/*     */ 
/*     */ 
/*     */     
/* 151 */     for (j = 0; j < info.partitions; j++) {
/* 152 */       _n += info.class_dim[info.partitionclass[j]];
/*     */     }
/* 154 */     _n += 2;
/* 155 */     look.posts = _n;
/*     */ 
/*     */     
/* 158 */     for (j = 0; j < _n; j++) {
/* 159 */       sortpointer[j] = j;
/*     */     }
/*     */     
/*     */     int k;
/*     */     
/* 164 */     for (k = 0; k < _n - 1; k++) {
/* 165 */       for (int m = k; m < _n; m++) {
/* 166 */         if (info.postlist[sortpointer[k]] > info.postlist[sortpointer[m]]) {
/* 167 */           int foo = sortpointer[m];
/* 168 */           sortpointer[m] = sortpointer[k];
/* 169 */           sortpointer[k] = foo;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 175 */     for (k = 0; k < _n; k++) {
/* 176 */       look.forward_index[k] = sortpointer[k];
/*     */     }
/*     */     
/* 179 */     for (k = 0; k < _n; k++) {
/* 180 */       look.reverse_index[look.forward_index[k]] = k;
/*     */     }
/*     */     
/* 183 */     for (k = 0; k < _n; k++) {
/* 184 */       look.sorted_index[k] = info.postlist[look.forward_index[k]];
/*     */     }
/*     */ 
/*     */     
/* 188 */     switch (info.mult) {
/*     */       case 1:
/* 190 */         look.quant_q = 256;
/*     */         break;
/*     */       case 2:
/* 193 */         look.quant_q = 128;
/*     */         break;
/*     */       case 3:
/* 196 */         look.quant_q = 86;
/*     */         break;
/*     */       case 4:
/* 199 */         look.quant_q = 64;
/*     */         break;
/*     */       default:
/* 202 */         look.quant_q = -1;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 207 */     for (k = 0; k < _n - 2; k++) {
/* 208 */       int lo = 0;
/* 209 */       int hi = 1;
/* 210 */       int lx = 0;
/* 211 */       int hx = look.n;
/* 212 */       int currentx = info.postlist[k + 2];
/* 213 */       for (int m = 0; m < k + 2; m++) {
/* 214 */         int x = info.postlist[m];
/* 215 */         if (x > lx && x < currentx) {
/* 216 */           lo = m;
/* 217 */           lx = x;
/*     */         } 
/* 219 */         if (x < hx && x > currentx) {
/* 220 */           hi = m;
/* 221 */           hx = x;
/*     */         } 
/*     */       } 
/* 224 */       look.loneighbor[k] = lo;
/* 225 */       look.hineighbor[k] = hi;
/*     */     } 
/*     */     
/* 228 */     return look;
/*     */   }
/*     */ 
/*     */   
/*     */   void free_info(Object i) {}
/*     */ 
/*     */   
/*     */   void free_look(Object i) {}
/*     */ 
/*     */   
/*     */   void free_state(Object vs) {}
/*     */   
/*     */   int forward(Block vb, Object i, float[] in, float[] out, Object vs) {
/* 241 */     return 0;
/*     */   }
/*     */   
/*     */   Object inverse1(Block vb, Object ii, Object memo) {
/* 245 */     LookFloor1 look = (LookFloor1)ii;
/* 246 */     InfoFloor1 info = look.vi;
/* 247 */     CodeBook[] books = vb.vd.fullbooks;
/*     */ 
/*     */     
/* 250 */     if (vb.opb.read(1) == 1) {
/* 251 */       int[] fit_value = null;
/* 252 */       if (memo instanceof int[]) {
/* 253 */         fit_value = (int[])memo;
/*     */       }
/* 255 */       if (fit_value == null || fit_value.length < look.posts) {
/* 256 */         fit_value = new int[look.posts];
/*     */       } else {
/*     */         
/* 259 */         for (int k = 0; k < fit_value.length; k++) {
/* 260 */           fit_value[k] = 0;
/*     */         }
/*     */       } 
/* 263 */       fit_value[0] = vb.opb.read(Util.ilog(look.quant_q - 1));
/* 264 */       fit_value[1] = vb.opb.read(Util.ilog(look.quant_q - 1));
/*     */       
/*     */       int i, j;
/* 267 */       for (i = 0, j = 2; i < info.partitions; i++) {
/* 268 */         int clss = info.partitionclass[i];
/* 269 */         int cdim = info.class_dim[clss];
/* 270 */         int csubbits = info.class_subs[clss];
/* 271 */         int csub = 1 << csubbits;
/* 272 */         int cval = 0;
/*     */ 
/*     */         
/* 275 */         if (csubbits != 0) {
/* 276 */           cval = books[info.class_book[clss]].decode(vb.opb);
/*     */           
/* 278 */           if (cval == -1) {
/* 279 */             return null;
/*     */           }
/*     */         } 
/*     */         
/* 283 */         for (int k = 0; k < cdim; k++) {
/* 284 */           int book = info.class_subbook[clss][cval & csub - 1];
/* 285 */           cval >>>= csubbits;
/* 286 */           if (book >= 0) {
/* 287 */             fit_value[j + k] = books[book].decode(vb.opb); if (books[book].decode(vb.opb) == -1) {
/* 288 */               return null;
/*     */             }
/*     */           } else {
/*     */             
/* 292 */             fit_value[j + k] = 0;
/*     */           } 
/*     */         } 
/* 295 */         j += cdim;
/*     */       } 
/*     */ 
/*     */       
/* 299 */       for (i = 2; i < look.posts; i++) {
/* 300 */         int predicted = render_point(info.postlist[look.loneighbor[i - 2]], info.postlist[look.hineighbor[i - 2]], fit_value[look.loneighbor[i - 2]], fit_value[look.hineighbor[i - 2]], info.postlist[i]);
/*     */ 
/*     */ 
/*     */         
/* 304 */         int hiroom = look.quant_q - predicted;
/* 305 */         int loroom = predicted;
/* 306 */         int room = ((hiroom < loroom) ? hiroom : loroom) << 1;
/* 307 */         int val = fit_value[i];
/*     */         
/* 309 */         if (val != 0) {
/* 310 */           if (val >= room) {
/* 311 */             if (hiroom > loroom) {
/* 312 */               val -= loroom;
/*     */             } else {
/*     */               
/* 315 */               val = -1 - val - hiroom;
/*     */             }
/*     */           
/*     */           }
/* 319 */           else if ((val & 0x1) != 0) {
/* 320 */             val = -(val + 1 >>> 1);
/*     */           } else {
/*     */             
/* 323 */             val >>= 1;
/*     */           } 
/*     */ 
/*     */           
/* 327 */           fit_value[i] = val + predicted;
/* 328 */           fit_value[look.loneighbor[i - 2]] = fit_value[look.loneighbor[i - 2]] & 0x7FFF;
/* 329 */           fit_value[look.hineighbor[i - 2]] = fit_value[look.hineighbor[i - 2]] & 0x7FFF;
/*     */         } else {
/*     */           
/* 332 */           fit_value[i] = predicted | 0x8000;
/*     */         } 
/*     */       } 
/* 335 */       return fit_value;
/*     */     } 
/*     */     
/* 338 */     return null;
/*     */   }
/*     */   
/*     */   private static int render_point(int x0, int x1, int y0, int y1, int x) {
/* 342 */     y0 &= 0x7FFF;
/* 343 */     y1 &= 0x7FFF;
/*     */ 
/*     */     
/* 346 */     int dy = y1 - y0;
/* 347 */     int adx = x1 - x0;
/* 348 */     int ady = Math.abs(dy);
/* 349 */     int err = ady * (x - x0);
/*     */     
/* 351 */     int off = err / adx;
/* 352 */     if (dy < 0)
/* 353 */       return y0 - off; 
/* 354 */     return y0 + off;
/*     */   }
/*     */ 
/*     */   
/*     */   int inverse2(Block vb, Object i, Object memo, float[] out) {
/* 359 */     LookFloor1 look = (LookFloor1)i;
/* 360 */     InfoFloor1 info = look.vi;
/* 361 */     int n = vb.vd.vi.blocksizes[vb.mode] / 2;
/*     */     
/* 363 */     if (memo != null) {
/*     */       
/* 365 */       int[] fit_value = (int[])memo;
/* 366 */       int hx = 0;
/* 367 */       int lx = 0;
/* 368 */       int ly = fit_value[0] * info.mult; int k;
/* 369 */       for (k = 1; k < look.posts; k++) {
/* 370 */         int current = look.forward_index[k];
/* 371 */         int hy = fit_value[current] & 0x7FFF;
/* 372 */         if (hy == fit_value[current]) {
/* 373 */           hy *= info.mult;
/* 374 */           hx = info.postlist[current];
/*     */           
/* 376 */           render_line(lx, hx, ly, hy, out);
/*     */           
/* 378 */           lx = hx;
/* 379 */           ly = hy;
/*     */         } 
/*     */       } 
/* 382 */       for (k = hx; k < n; k++) {
/* 383 */         out[k] = out[k] * out[k - 1];
/*     */       }
/* 385 */       return 1;
/*     */     } 
/* 387 */     for (int j = 0; j < n; j++) {
/* 388 */       out[j] = 0.0F;
/*     */     }
/* 390 */     return 0;
/*     */   }
/*     */   
/* 393 */   private static float[] FLOOR_fromdB_LOOKUP = new float[] { 1.0649863E-7F, 1.1341951E-7F, 1.2079015E-7F, 1.2863978E-7F, 1.369995E-7F, 1.459025E-7F, 1.5538409E-7F, 1.6548181E-7F, 1.7623574E-7F, 1.8768856E-7F, 1.998856E-7F, 2.128753E-7F, 2.2670913E-7F, 2.4144197E-7F, 2.5713223E-7F, 2.7384212E-7F, 2.9163792E-7F, 3.1059022E-7F, 3.307741E-7F, 3.5226967E-7F, 3.7516213E-7F, 3.995423E-7F, 4.255068E-7F, 4.5315863E-7F, 4.8260745E-7F, 5.1397E-7F, 5.4737063E-7F, 5.829419E-7F, 6.208247E-7F, 6.611694E-7F, 7.041359E-7F, 7.4989464E-7F, 7.98627E-7F, 8.505263E-7F, 9.057983E-7F, 9.646621E-7F, 1.0273513E-6F, 1.0941144E-6F, 1.1652161E-6F, 1.2409384E-6F, 1.3215816E-6F, 1.4074654E-6F, 1.4989305E-6F, 1.5963394E-6F, 1.7000785E-6F, 1.8105592E-6F, 1.9282195E-6F, 2.053526E-6F, 2.1869757E-6F, 2.3290977E-6F, 2.4804558E-6F, 2.6416496E-6F, 2.813319E-6F, 2.9961443E-6F, 3.1908505E-6F, 3.39821E-6F, 3.619045E-6F, 3.8542307E-6F, 4.1047006E-6F, 4.371447E-6F, 4.6555283E-6F, 4.958071E-6F, 5.280274E-6F, 5.623416E-6F, 5.988857E-6F, 6.3780467E-6F, 6.7925284E-6F, 7.2339453E-6F, 7.704048E-6F, 8.2047E-6F, 8.737888E-6F, 9.305725E-6F, 9.910464E-6F, 1.0554501E-5F, 1.1240392E-5F, 1.1970856E-5F, 1.2748789E-5F, 1.3577278E-5F, 1.4459606E-5F, 1.5399271E-5F, 1.6400005E-5F, 1.7465769E-5F, 1.8600793E-5F, 1.9809577E-5F, 2.1096914E-5F, 2.2467912E-5F, 2.3928002E-5F, 2.5482977E-5F, 2.7139005E-5F, 2.890265E-5F, 3.078091E-5F, 3.2781227E-5F, 3.4911533E-5F, 3.718028E-5F, 3.9596467E-5F, 4.2169668E-5F, 4.491009E-5F, 4.7828602E-5F, 5.0936775E-5F, 5.424693E-5F, 5.7772202E-5F, 6.152657E-5F, 6.552491E-5F, 6.9783084E-5F, 7.4317984E-5F, 7.914758E-5F, 8.429104E-5F, 8.976875E-5F, 9.560242E-5F, 1.0181521E-4F, 1.0843174E-4F, 1.1547824E-4F, 1.2298267E-4F, 1.3097477E-4F, 1.3948625E-4F, 1.4855085E-4F, 1.5820454E-4F, 1.6848555E-4F, 1.7943469E-4F, 1.9109536E-4F, 2.0351382E-4F, 2.167393E-4F, 2.3082423E-4F, 2.4582449E-4F, 2.6179955E-4F, 2.7881275E-4F, 2.9693157E-4F, 3.1622787E-4F, 3.3677815E-4F, 3.5866388E-4F, 3.8197188E-4F, 4.0679457E-4F, 4.3323037E-4F, 4.613841E-4F, 4.913675E-4F, 5.2329927E-4F, 5.573062E-4F, 5.935231E-4F, 6.320936E-4F, 6.731706E-4F, 7.16917E-4F, 7.635063E-4F, 8.1312325E-4F, 8.6596457E-4F, 9.2223985E-4F, 9.821722E-4F, 0.0010459992F, 0.0011139743F, 0.0011863665F, 0.0012634633F, 0.0013455702F, 0.0014330129F, 0.0015261382F, 0.0016253153F, 0.0017309374F, 0.0018434235F, 0.0019632196F, 0.0020908006F, 0.0022266726F, 0.0023713743F, 0.0025254795F, 0.0026895993F, 0.0028643848F, 0.0030505287F, 0.003248769F, 0.0034598925F, 0.0036847359F, 0.0039241905F, 0.0041792067F, 0.004450795F, 0.004740033F, 0.005048067F, 0.0053761187F, 0.005725489F, 0.0060975635F, 0.0064938175F, 0.0069158226F, 0.0073652514F, 0.007843887F, 0.008353627F, 0.008896492F, 0.009474637F, 0.010090352F, 0.01074608F, 0.011444421F, 0.012188144F, 0.012980198F, 0.013823725F, 0.014722068F, 0.015678791F, 0.016697686F, 0.017782796F, 0.018938422F, 0.020169148F, 0.021479854F, 0.022875736F, 0.02436233F, 0.025945531F, 0.027631618F, 0.029427277F, 0.031339627F, 0.03337625F, 0.035545226F, 0.037855156F, 0.0403152F, 0.042935107F, 0.045725275F, 0.048696756F, 0.05186135F, 0.05523159F, 0.05882085F, 0.062643364F, 0.06671428F, 0.07104975F, 0.075666964F, 0.08058423F, 0.08582105F, 0.09139818F, 0.097337745F, 0.1036633F, 0.11039993F, 0.11757434F, 0.12521498F, 0.13335215F, 0.14201812F, 0.15124726F, 0.16107617F, 0.1715438F, 0.18269168F, 0.19456401F, 0.20720787F, 0.22067343F, 0.23501402F, 0.25028655F, 0.26655158F, 0.28387362F, 0.3023213F, 0.32196787F, 0.34289113F, 0.36517414F, 0.3889052F, 0.41417846F, 0.44109413F, 0.4697589F, 0.50028646F, 0.53279793F, 0.5674221F, 0.6042964F, 0.64356697F, 0.6853896F, 0.72993004F, 0.777365F, 0.8278826F, 0.88168305F, 0.9389798F, 1.0F };
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static void render_line(int x0, int x1, int y0, int y1, float[] d) {
/* 456 */     int dy = y1 - y0;
/* 457 */     int adx = x1 - x0;
/* 458 */     int ady = Math.abs(dy);
/* 459 */     int base = dy / adx;
/* 460 */     int sy = (dy < 0) ? (base - 1) : (base + 1);
/* 461 */     int x = x0;
/* 462 */     int y = y0;
/* 463 */     int err = 0;
/*     */     
/* 465 */     ady -= Math.abs(base * adx);
/*     */     
/* 467 */     d[x] = d[x] * FLOOR_fromdB_LOOKUP[y];
/* 468 */     while (++x < x1) {
/* 469 */       err += ady;
/* 470 */       if (err >= adx) {
/* 471 */         err -= adx;
/* 472 */         y += sy;
/*     */       } else {
/*     */         
/* 475 */         y += base;
/*     */       } 
/* 477 */       d[x] = d[x] * FLOOR_fromdB_LOOKUP[y];
/*     */     } 
/*     */   }
/*     */   
/*     */   class InfoFloor1
/*     */   {
/*     */     static final int VIF_POSIT = 63;
/*     */     static final int VIF_CLASS = 16;
/*     */     static final int VIF_PARTS = 31;
/*     */     int partitions;
/* 487 */     int[] partitionclass = new int[31];
/*     */     
/* 489 */     int[] class_dim = new int[16];
/* 490 */     int[] class_subs = new int[16];
/* 491 */     int[] class_book = new int[16];
/* 492 */     int[][] class_subbook = new int[16][];
/*     */     
/*     */     int mult;
/* 495 */     int[] postlist = new int[65];
/*     */     
/*     */     float maxover;
/*     */     
/*     */     float maxunder;
/*     */     
/*     */     float maxerr;
/*     */     
/*     */     int twofitminsize;
/*     */     int twofitminused;
/*     */     int twofitweight;
/*     */     float twofitatten;
/*     */     int unusedminsize;
/*     */     int unusedmin_n;
/*     */     int n;
/*     */     
/*     */     InfoFloor1() {
/* 512 */       for (int i = 0; i < this.class_subbook.length; i++) {
/* 513 */         this.class_subbook[i] = new int[8];
/*     */       }
/*     */     }
/*     */     
/*     */     void free() {
/* 518 */       this.partitionclass = null;
/* 519 */       this.class_dim = null;
/* 520 */       this.class_subs = null;
/* 521 */       this.class_book = null;
/* 522 */       this.class_subbook = (int[][])null;
/* 523 */       this.postlist = null;
/*     */     }
/*     */     
/*     */     Object copy_info() {
/* 527 */       InfoFloor1 info = this;
/* 528 */       InfoFloor1 ret = new InfoFloor1();
/*     */       
/* 530 */       ret.partitions = info.partitions;
/* 531 */       System.arraycopy(info.partitionclass, 0, ret.partitionclass, 0, 31);
/*     */       
/* 533 */       System.arraycopy(info.class_dim, 0, ret.class_dim, 0, 16);
/* 534 */       System.arraycopy(info.class_subs, 0, ret.class_subs, 0, 16);
/* 535 */       System.arraycopy(info.class_book, 0, ret.class_book, 0, 16);
/*     */       
/* 537 */       for (int j = 0; j < 16; j++) {
/* 538 */         System.arraycopy(info.class_subbook[j], 0, ret.class_subbook[j], 0, 8);
/*     */       }
/*     */       
/* 541 */       ret.mult = info.mult;
/* 542 */       System.arraycopy(info.postlist, 0, ret.postlist, 0, 65);
/*     */       
/* 544 */       ret.maxover = info.maxover;
/* 545 */       ret.maxunder = info.maxunder;
/* 546 */       ret.maxerr = info.maxerr;
/*     */       
/* 548 */       ret.twofitminsize = info.twofitminsize;
/* 549 */       ret.twofitminused = info.twofitminused;
/* 550 */       ret.twofitweight = info.twofitweight;
/* 551 */       ret.twofitatten = info.twofitatten;
/* 552 */       ret.unusedminsize = info.unusedminsize;
/* 553 */       ret.unusedmin_n = info.unusedmin_n;
/*     */       
/* 555 */       ret.n = info.n;
/*     */       
/* 557 */       return ret;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   class LookFloor1
/*     */   {
/*     */     static final int VIF_POSIT = 63;
/* 565 */     int[] sorted_index = new int[65];
/* 566 */     int[] forward_index = new int[65];
/* 567 */     int[] reverse_index = new int[65];
/* 568 */     int[] hineighbor = new int[63];
/* 569 */     int[] loneighbor = new int[63];
/*     */     
/*     */     int posts;
/*     */     
/*     */     int n;
/*     */     int quant_q;
/*     */     Floor1.InfoFloor1 vi;
/*     */     int phrasebits;
/*     */     int postbits;
/*     */     int frames;
/*     */     
/*     */     void free() {
/* 581 */       this.sorted_index = null;
/* 582 */       this.forward_index = null;
/* 583 */       this.reverse_index = null;
/* 584 */       this.hineighbor = null;
/* 585 */       this.loneighbor = null;
/*     */     }
/*     */   }
/*     */   
/*     */   class Lsfit_acc {
/*     */     long x0;
/*     */     long x1;
/*     */     long xa;
/*     */     long ya;
/*     */     long x2a;
/*     */     long y2a;
/*     */     long xya;
/*     */     long n;
/*     */     long an;
/*     */     long un;
/*     */     long edgey0;
/*     */     long edgey1;
/*     */   }
/*     */   
/*     */   class EchstateFloor1 {
/*     */     int[] codewords;
/*     */     float[] curve;
/*     */     long frameno;
/*     */     long codes;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\jcraft\jorbis\Floor1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */