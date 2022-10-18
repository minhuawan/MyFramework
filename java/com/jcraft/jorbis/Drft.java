/*      */ package com.jcraft.jorbis;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Drft
/*      */ {
/*      */   int n;
/*      */   float[] trigcache;
/*      */   int[] splitcache;
/*      */   
/*      */   void backward(float[] data) {
/*   35 */     if (this.n == 1)
/*      */       return; 
/*   37 */     drftb1(this.n, data, this.trigcache, this.trigcache, this.n, this.splitcache);
/*      */   }
/*      */   
/*      */   void init(int n) {
/*   41 */     this.n = n;
/*   42 */     this.trigcache = new float[3 * n];
/*   43 */     this.splitcache = new int[32];
/*   44 */     fdrffti(n, this.trigcache, this.splitcache);
/*      */   }
/*      */   
/*      */   void clear() {
/*   48 */     if (this.trigcache != null)
/*   49 */       this.trigcache = null; 
/*   50 */     if (this.splitcache != null)
/*   51 */       this.splitcache = null; 
/*      */   }
/*      */   
/*   54 */   static int[] ntryh = new int[] { 4, 2, 3, 5 };
/*   55 */   static float tpi = 6.2831855F;
/*   56 */   static float hsqt2 = 0.70710677F;
/*   57 */   static float taui = 0.8660254F;
/*   58 */   static float taur = -0.5F;
/*   59 */   static float sqrt2 = 1.4142135F;
/*      */ 
/*      */   
/*      */   static void drfti1(int n, float[] wa, int index, int[] ifac) {
/*   63 */     int ntry = 0, j = -1;
/*      */ 
/*      */ 
/*      */     
/*   67 */     int nl = n;
/*   68 */     int nf = 0;
/*      */     
/*   70 */     int state = 101; while (true) {
/*      */       int i; int nq;
/*      */       int nr;
/*   73 */       switch (state) {
/*      */         case 101:
/*   75 */           j++;
/*   76 */           if (j < 4)
/*   77 */           { ntry = ntryh[j]; }
/*      */           else
/*   79 */           { ntry += 2; } 
/*      */         case 104:
/*   81 */           nq = nl / ntry;
/*   82 */           nr = nl - ntry * nq;
/*   83 */           if (nr != 0) {
/*   84 */             state = 101;
/*      */             continue;
/*      */           } 
/*   87 */           nf++;
/*   88 */           ifac[nf + 1] = ntry;
/*   89 */           nl = nq;
/*   90 */           if (ntry != 2) {
/*   91 */             state = 107;
/*      */             continue;
/*      */           } 
/*   94 */           if (nf == 1) {
/*   95 */             state = 107;
/*      */             
/*      */             continue;
/*      */           } 
/*   99 */           for (i = 1; i < nf; i++) {
/*  100 */             int ib = nf - i + 1;
/*  101 */             ifac[ib + 1] = ifac[ib];
/*      */           } 
/*  103 */           ifac[2] = 2;
/*      */         case 107:
/*  105 */           if (nl != 1) {
/*  106 */             state = 104; continue;
/*      */           }  break;
/*      */       } 
/*  109 */     }  ifac[0] = n;
/*  110 */     ifac[1] = nf;
/*  111 */     float argh = tpi / n;
/*  112 */     int is = 0;
/*  113 */     int nfm1 = nf - 1;
/*  114 */     int l1 = 1;
/*      */     
/*  116 */     if (nfm1 == 0) {
/*      */       return;
/*      */     }
/*  119 */     for (int k1 = 0; k1 < nfm1; k1++) {
/*  120 */       int ip = ifac[k1 + 2];
/*  121 */       int ld = 0;
/*  122 */       int l2 = l1 * ip;
/*  123 */       int ido = n / l2;
/*  124 */       int ipm = ip - 1;
/*      */       
/*  126 */       for (j = 0; j < ipm; j++) {
/*  127 */         ld += l1;
/*  128 */         int i = is;
/*  129 */         float argld = ld * argh;
/*  130 */         float fi = 0.0F;
/*  131 */         for (int ii = 2; ii < ido; ii += 2) {
/*  132 */           fi++;
/*  133 */           float arg = fi * argld;
/*  134 */           wa[index + i++] = (float)Math.cos(arg);
/*  135 */           wa[index + i++] = (float)Math.sin(arg);
/*      */         } 
/*  137 */         is += ido;
/*      */       } 
/*  139 */       l1 = l2;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void fdrffti(int n, float[] wsave, int[] ifac) {
/*  147 */     if (n == 1)
/*      */       return; 
/*  149 */     drfti1(n, wsave, n, ifac);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void dradf2(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index) {
/*  158 */     int t1 = 0;
/*  159 */     int t2 = l1 * ido, t0 = t2;
/*  160 */     int t3 = ido << 1; int k;
/*  161 */     for (k = 0; k < l1; k++) {
/*  162 */       ch[t1 << 1] = cc[t1] + cc[t2];
/*  163 */       ch[(t1 << 1) + t3 - 1] = cc[t1] - cc[t2];
/*  164 */       t1 += ido;
/*  165 */       t2 += ido;
/*      */     } 
/*      */     
/*  168 */     if (ido < 2) {
/*      */       return;
/*      */     }
/*  171 */     if (ido != 2) {
/*  172 */       t1 = 0;
/*  173 */       t2 = t0;
/*  174 */       for (k = 0; k < l1; k++) {
/*  175 */         t3 = t2;
/*  176 */         int t4 = (t1 << 1) + (ido << 1);
/*  177 */         int t5 = t1;
/*  178 */         int t6 = t1 + t1;
/*  179 */         for (int i = 2; i < ido; i += 2) {
/*  180 */           t3 += 2;
/*  181 */           t4 -= 2;
/*  182 */           t5 += 2;
/*  183 */           t6 += 2;
/*  184 */           float tr2 = wa1[index + i - 2] * cc[t3 - 1] + wa1[index + i - 1] * cc[t3];
/*  185 */           float ti2 = wa1[index + i - 2] * cc[t3] - wa1[index + i - 1] * cc[t3 - 1];
/*  186 */           ch[t6] = cc[t5] + ti2;
/*  187 */           ch[t4] = ti2 - cc[t5];
/*  188 */           ch[t6 - 1] = cc[t5 - 1] + tr2;
/*  189 */           ch[t4 - 1] = cc[t5 - 1] - tr2;
/*      */         } 
/*  191 */         t1 += ido;
/*  192 */         t2 += ido;
/*      */       } 
/*  194 */       if (ido % 2 == 1) {
/*      */         return;
/*      */       }
/*      */     } 
/*  198 */     t3 = t2 = (t1 = ido) - 1;
/*  199 */     t2 += t0;
/*  200 */     for (k = 0; k < l1; k++) {
/*  201 */       ch[t1] = -cc[t2];
/*  202 */       ch[t1 - 1] = cc[t3];
/*  203 */       t1 += ido << 1;
/*  204 */       t2 += ido;
/*  205 */       t3 += ido;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void dradf4(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2, float[] wa3, int index3) {
/*  213 */     int t0 = l1 * ido;
/*      */     
/*  215 */     int t1 = t0;
/*  216 */     int t4 = t1 << 1;
/*  217 */     int t2 = t1 + (t1 << 1);
/*  218 */     int t3 = 0;
/*      */     int k;
/*  220 */     for (k = 0; k < l1; k++) {
/*  221 */       float tr1 = cc[t1] + cc[t2];
/*  222 */       float tr2 = cc[t3] + cc[t4];
/*      */       int i;
/*  224 */       ch[i = t3 << 2] = tr1 + tr2;
/*  225 */       ch[(ido << 2) + i - 1] = tr2 - tr1;
/*  226 */       ch[(i += ido << 1) - 1] = cc[t3] - cc[t4];
/*  227 */       ch[i] = cc[t2] - cc[t1];
/*      */       
/*  229 */       t1 += ido;
/*  230 */       t2 += ido;
/*  231 */       t3 += ido;
/*  232 */       t4 += ido;
/*      */     } 
/*  234 */     if (ido < 2) {
/*      */       return;
/*      */     }
/*  237 */     if (ido != 2) {
/*  238 */       t1 = 0;
/*  239 */       for (k = 0; k < l1; k++) {
/*  240 */         t2 = t1;
/*  241 */         t4 = t1 << 2;
/*  242 */         int m, j = (m = ido << 1) + t4;
/*  243 */         for (int i = 2; i < ido; i += 2) {
/*  244 */           t3 = t2 += 2;
/*  245 */           t4 += 2;
/*  246 */           j -= 2;
/*      */           
/*  248 */           t3 += t0;
/*  249 */           float cr2 = wa1[index1 + i - 2] * cc[t3 - 1] + wa1[index1 + i - 1] * cc[t3];
/*  250 */           float ci2 = wa1[index1 + i - 2] * cc[t3] - wa1[index1 + i - 1] * cc[t3 - 1];
/*  251 */           t3 += t0;
/*  252 */           float cr3 = wa2[index2 + i - 2] * cc[t3 - 1] + wa2[index2 + i - 1] * cc[t3];
/*  253 */           float ci3 = wa2[index2 + i - 2] * cc[t3] - wa2[index2 + i - 1] * cc[t3 - 1];
/*  254 */           t3 += t0;
/*  255 */           float cr4 = wa3[index3 + i - 2] * cc[t3 - 1] + wa3[index3 + i - 1] * cc[t3];
/*  256 */           float ci4 = wa3[index3 + i - 2] * cc[t3] - wa3[index3 + i - 1] * cc[t3 - 1];
/*      */           
/*  258 */           float tr1 = cr2 + cr4;
/*  259 */           float tr4 = cr4 - cr2;
/*  260 */           float ti1 = ci2 + ci4;
/*  261 */           float ti4 = ci2 - ci4;
/*      */           
/*  263 */           float ti2 = cc[t2] + ci3;
/*  264 */           float ti3 = cc[t2] - ci3;
/*  265 */           float tr2 = cc[t2 - 1] + cr3;
/*  266 */           float tr3 = cc[t2 - 1] - cr3;
/*      */           
/*  268 */           ch[t4 - 1] = tr1 + tr2;
/*  269 */           ch[t4] = ti1 + ti2;
/*      */           
/*  271 */           ch[j - 1] = tr3 - ti4;
/*  272 */           ch[j] = tr4 - ti3;
/*      */           
/*  274 */           ch[t4 + m - 1] = ti4 + tr3;
/*  275 */           ch[t4 + m] = tr4 + ti3;
/*      */           
/*  277 */           ch[j + m - 1] = tr2 - tr1;
/*  278 */           ch[j + m] = ti1 - ti2;
/*      */         } 
/*  280 */         t1 += ido;
/*      */       } 
/*  282 */       if ((ido & 0x1) != 0) {
/*      */         return;
/*      */       }
/*      */     } 
/*  286 */     t2 = (t1 = t0 + ido - 1) + (t0 << 1);
/*  287 */     t3 = ido << 2;
/*  288 */     t4 = ido;
/*  289 */     int t5 = ido << 1;
/*  290 */     int t6 = ido;
/*      */     
/*  292 */     for (k = 0; k < l1; k++) {
/*  293 */       float ti1 = -hsqt2 * (cc[t1] + cc[t2]);
/*  294 */       float tr1 = hsqt2 * (cc[t1] - cc[t2]);
/*      */       
/*  296 */       ch[t4 - 1] = tr1 + cc[t6 - 1];
/*  297 */       ch[t4 + t5 - 1] = cc[t6 - 1] - tr1;
/*      */       
/*  299 */       ch[t4] = ti1 - cc[t1 + t0];
/*  300 */       ch[t4 + t5] = ti1 + cc[t1 + t0];
/*      */       
/*  302 */       t1 += ido;
/*  303 */       t2 += ido;
/*  304 */       t4 += t3;
/*  305 */       t6 += ido;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void dradfg(int ido, int ip, int l1, int idl1, float[] cc, float[] c1, float[] c2, float[] ch, float[] ch2, float[] wa, int index) {
/*  312 */     int t2 = 0;
/*      */ 
/*      */     
/*  315 */     float dcp = 0.0F, dsp = 0.0F;
/*      */ 
/*      */     
/*  318 */     float arg = tpi / ip;
/*  319 */     dcp = (float)Math.cos(arg);
/*  320 */     dsp = (float)Math.sin(arg);
/*  321 */     int ipph = ip + 1 >> 1;
/*  322 */     int ipp2 = ip;
/*  323 */     int idp2 = ido;
/*  324 */     int nbd = ido - 1 >> 1;
/*  325 */     int t0 = l1 * ido;
/*  326 */     int t10 = ip * ido;
/*      */     
/*  328 */     int state = 100; while (true) {
/*      */       int n; int i1; int i2; int l; int ik; int is; int t1; int t3; int t4; int t5; float ai1; float ar1;
/*  330 */       switch (state) {
/*      */         case 101:
/*  332 */           if (ido == 1) {
/*  333 */             state = 119;
/*      */             continue;
/*      */           } 
/*  336 */           for (ik = 0; ik < idl1; ik++) {
/*  337 */             ch2[ik] = c2[ik];
/*      */           }
/*  339 */           t1 = 0;
/*  340 */           for (i1 = 1; i1 < ip; i1++) {
/*  341 */             t1 += t0;
/*  342 */             t2 = t1;
/*  343 */             for (int i3 = 0; i3 < l1; i3++) {
/*  344 */               ch[t2] = c1[t2];
/*  345 */               t2 += ido;
/*      */             } 
/*      */           } 
/*      */           
/*  349 */           is = -ido;
/*  350 */           t1 = 0;
/*  351 */           if (nbd > l1) {
/*  352 */             for (i1 = 1; i1 < ip; i1++) {
/*  353 */               t1 += t0;
/*  354 */               is += ido;
/*  355 */               t2 = -ido + t1;
/*  356 */               for (int i3 = 0; i3 < l1; i3++) {
/*  357 */                 int idij = is - 1;
/*  358 */                 t2 += ido;
/*  359 */                 int i5 = t2;
/*  360 */                 for (int i4 = 2; i4 < ido; i4 += 2) {
/*  361 */                   idij += 2;
/*  362 */                   i5 += 2;
/*  363 */                   ch[i5 - 1] = wa[index + idij - 1] * c1[i5 - 1] + wa[index + idij] * c1[i5];
/*  364 */                   ch[i5] = wa[index + idij - 1] * c1[i5] - wa[index + idij] * c1[i5 - 1];
/*      */                 }
/*      */               
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/*  371 */             for (i1 = 1; i1 < ip; i1++) {
/*  372 */               is += ido;
/*  373 */               int idij = is - 1;
/*  374 */               t1 += t0;
/*  375 */               t2 = t1;
/*  376 */               for (int i3 = 2; i3 < ido; i3 += 2) {
/*  377 */                 idij += 2;
/*  378 */                 t2 += 2;
/*  379 */                 int i5 = t2;
/*  380 */                 for (int i4 = 0; i4 < l1; i4++) {
/*  381 */                   ch[i5 - 1] = wa[index + idij - 1] * c1[i5 - 1] + wa[index + idij] * c1[i5];
/*  382 */                   ch[i5] = wa[index + idij - 1] * c1[i5] - wa[index + idij] * c1[i5 - 1];
/*  383 */                   i5 += ido;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  389 */           t1 = 0;
/*  390 */           t2 = ipp2 * t0;
/*  391 */           if (nbd < l1) {
/*  392 */             for (i1 = 1; i1 < ipph; i1++) {
/*  393 */               t1 += t0;
/*  394 */               t2 -= t0;
/*  395 */               int i4 = t1;
/*  396 */               int i5 = t2;
/*  397 */               for (int i3 = 2; i3 < ido; i3 += 2) {
/*  398 */                 i4 += 2;
/*  399 */                 i5 += 2;
/*  400 */                 int i7 = i4 - ido;
/*  401 */                 int t6 = i5 - ido;
/*  402 */                 for (int i6 = 0; i6 < l1; i6++) {
/*  403 */                   i7 += ido;
/*  404 */                   t6 += ido;
/*  405 */                   c1[i7 - 1] = ch[i7 - 1] + ch[t6 - 1];
/*  406 */                   c1[t6 - 1] = ch[i7] - ch[t6];
/*  407 */                   c1[i7] = ch[i7] + ch[t6];
/*  408 */                   c1[t6] = ch[t6 - 1] - ch[i7 - 1];
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/*  414 */             for (i1 = 1; i1 < ipph; i1++) {
/*  415 */               t1 += t0;
/*  416 */               t2 -= t0;
/*  417 */               int i4 = t1;
/*  418 */               int i5 = t2;
/*  419 */               for (int i3 = 0; i3 < l1; i3++) {
/*  420 */                 int i7 = i4;
/*  421 */                 int t6 = i5;
/*  422 */                 for (int i6 = 2; i6 < ido; i6 += 2) {
/*  423 */                   i7 += 2;
/*  424 */                   t6 += 2;
/*  425 */                   c1[i7 - 1] = ch[i7 - 1] + ch[t6 - 1];
/*  426 */                   c1[t6 - 1] = ch[i7] - ch[t6];
/*  427 */                   c1[i7] = ch[i7] + ch[t6];
/*  428 */                   c1[t6] = ch[t6 - 1] - ch[i7 - 1];
/*      */                 } 
/*  430 */                 i4 += ido;
/*  431 */                 i5 += ido;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         case 119:
/*  436 */           for (ik = 0; ik < idl1; ik++) {
/*  437 */             c2[ik] = ch2[ik];
/*      */           }
/*  439 */           t1 = 0;
/*  440 */           t2 = ipp2 * idl1;
/*  441 */           for (i1 = 1; i1 < ipph; i1++) {
/*  442 */             t1 += t0;
/*  443 */             t2 -= t0;
/*  444 */             int i4 = t1 - ido;
/*  445 */             int i5 = t2 - ido;
/*  446 */             for (int i3 = 0; i3 < l1; i3++) {
/*  447 */               i4 += ido;
/*  448 */               i5 += ido;
/*  449 */               c1[i4] = ch[i4] + ch[i5];
/*  450 */               c1[i5] = ch[i5] - ch[i4];
/*      */             } 
/*      */           } 
/*      */           
/*  454 */           ar1 = 1.0F;
/*  455 */           ai1 = 0.0F;
/*  456 */           t1 = 0;
/*  457 */           t2 = ipp2 * idl1;
/*  458 */           t3 = (ip - 1) * idl1;
/*  459 */           for (l = 1; l < ipph; l++) {
/*  460 */             t1 += idl1;
/*  461 */             t2 -= idl1;
/*  462 */             float ar1h = dcp * ar1 - dsp * ai1;
/*  463 */             ai1 = dcp * ai1 + dsp * ar1;
/*  464 */             ar1 = ar1h;
/*  465 */             int i3 = t1;
/*  466 */             int i4 = t2;
/*  467 */             int t6 = t3;
/*  468 */             int t7 = idl1;
/*      */             
/*  470 */             for (ik = 0; ik < idl1; ik++) {
/*  471 */               ch2[i3++] = c2[ik] + ar1 * c2[t7++];
/*  472 */               ch2[i4++] = ai1 * c2[t6++];
/*      */             } 
/*      */             
/*  475 */             float dc2 = ar1;
/*  476 */             float ds2 = ai1;
/*  477 */             float ar2 = ar1;
/*  478 */             float ai2 = ai1;
/*      */             
/*  480 */             i3 = idl1;
/*  481 */             i4 = (ipp2 - 1) * idl1;
/*  482 */             for (i1 = 2; i1 < ipph; i1++) {
/*  483 */               i3 += idl1;
/*  484 */               i4 -= idl1;
/*      */               
/*  486 */               float ar2h = dc2 * ar2 - ds2 * ai2;
/*  487 */               ai2 = dc2 * ai2 + ds2 * ar2;
/*  488 */               ar2 = ar2h;
/*      */               
/*  490 */               t6 = t1;
/*  491 */               t7 = t2;
/*  492 */               int t8 = i3;
/*  493 */               int t9 = i4;
/*  494 */               for (ik = 0; ik < idl1; ik++) {
/*  495 */                 ch2[t6++] = ch2[t6++] + ar2 * c2[t8++];
/*  496 */                 ch2[t7++] = ch2[t7++] + ai2 * c2[t9++];
/*      */               } 
/*      */             } 
/*      */           } 
/*  500 */           t1 = 0;
/*  501 */           for (i1 = 1; i1 < ipph; i1++) {
/*  502 */             t1 += idl1;
/*  503 */             t2 = t1;
/*  504 */             for (ik = 0; ik < idl1; ik++) {
/*  505 */               ch2[ik] = ch2[ik] + c2[t2++];
/*      */             }
/*      */           } 
/*  508 */           if (ido < l1) {
/*  509 */             state = 132;
/*      */             
/*      */             continue;
/*      */           } 
/*  513 */           t1 = 0;
/*  514 */           t2 = 0;
/*  515 */           for (i2 = 0; i2 < l1; i2++) {
/*  516 */             t3 = t1;
/*  517 */             int i4 = t2;
/*  518 */             for (int i3 = 0; i3 < ido; i3++)
/*  519 */               cc[i4++] = ch[t3++]; 
/*  520 */             t1 += ido;
/*  521 */             t2 += t10;
/*      */           } 
/*  523 */           state = 135;
/*      */ 
/*      */         
/*      */         case 132:
/*  527 */           for (n = 0; n < ido; n++) {
/*  528 */             t1 = n;
/*  529 */             t2 = n;
/*  530 */             for (i2 = 0; i2 < l1; i2++) {
/*  531 */               cc[t2] = ch[t1];
/*  532 */               t1 += ido;
/*  533 */               t2 += t10;
/*      */             } 
/*      */           } 
/*      */         case 135:
/*  537 */           t1 = 0;
/*  538 */           t2 = ido << 1;
/*  539 */           t3 = 0;
/*  540 */           t4 = ipp2 * t0;
/*  541 */           for (i1 = 1; i1 < ipph; i1++) {
/*  542 */             t1 += t2;
/*  543 */             t3 += t0;
/*  544 */             t4 -= t0;
/*      */             
/*  546 */             int i3 = t1;
/*  547 */             int t6 = t3;
/*  548 */             int t7 = t4;
/*      */             
/*  550 */             for (i2 = 0; i2 < l1; i2++) {
/*  551 */               cc[i3 - 1] = ch[t6];
/*  552 */               cc[i3] = ch[t7];
/*  553 */               i3 += t10;
/*  554 */               t6 += ido;
/*  555 */               t7 += ido;
/*      */             } 
/*      */           } 
/*      */           
/*  559 */           if (ido == 1)
/*      */             return; 
/*  561 */           if (nbd < l1) {
/*  562 */             state = 141;
/*      */             
/*      */             continue;
/*      */           } 
/*  566 */           t1 = -ido;
/*  567 */           t3 = 0;
/*  568 */           t4 = 0;
/*  569 */           t5 = ipp2 * t0;
/*  570 */           for (i1 = 1; i1 < ipph; i1++) {
/*  571 */             t1 += t2;
/*  572 */             t3 += t2;
/*  573 */             t4 += t0;
/*  574 */             t5 -= t0;
/*  575 */             int t6 = t1;
/*  576 */             int t7 = t3;
/*  577 */             int t8 = t4;
/*  578 */             int t9 = t5;
/*  579 */             for (i2 = 0; i2 < l1; i2++)
/*  580 */             { for (n = 2; n < ido; n += 2) {
/*  581 */                 int ic = idp2 - n;
/*  582 */                 cc[n + t7 - 1] = ch[n + t8 - 1] + ch[n + t9 - 1];
/*  583 */                 cc[ic + t6 - 1] = ch[n + t8 - 1] - ch[n + t9 - 1];
/*  584 */                 cc[n + t7] = ch[n + t8] + ch[n + t9];
/*  585 */                 cc[ic + t6] = ch[n + t9] - ch[n + t8];
/*      */               } 
/*  587 */               t6 += t10;
/*  588 */               t7 += t10;
/*  589 */               t8 += ido;
/*  590 */               t9 += ido; } 
/*      */           }  return;
/*      */         case 141:
/*      */           break;
/*      */       } 
/*  595 */     }  int i = -ido;
/*  596 */     int j = 0;
/*  597 */     int k = 0;
/*  598 */     int m = ipp2 * t0;
/*  599 */     for (byte b = 1; b < ipph; b++) {
/*  600 */       i += t2;
/*  601 */       j += t2;
/*  602 */       k += t0;
/*  603 */       m -= t0;
/*  604 */       for (int n = 2; n < ido; n += 2) {
/*  605 */         int t6 = idp2 + i - n;
/*  606 */         int t7 = n + j;
/*  607 */         int t8 = n + k;
/*  608 */         int t9 = n + m;
/*  609 */         for (int i1 = 0; i1 < l1; i1++) {
/*  610 */           cc[t7 - 1] = ch[t8 - 1] + ch[t9 - 1];
/*  611 */           cc[t6 - 1] = ch[t8 - 1] - ch[t9 - 1];
/*  612 */           cc[t7] = ch[t8] + ch[t9];
/*  613 */           cc[t6] = ch[t9] - ch[t8];
/*  614 */           t6 += t10;
/*  615 */           t7 += t10;
/*  616 */           t8 += ido;
/*  617 */           t9 += ido;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void drftf1(int n, float[] c, float[] ch, float[] wa, int[] ifac) {
/*  631 */     int nf = ifac[1];
/*  632 */     int na = 1;
/*  633 */     int l2 = n;
/*  634 */     int iw = n;
/*      */     
/*  636 */     for (int k1 = 0; k1 < nf; k1++) {
/*  637 */       int kh = nf - k1;
/*  638 */       int ip = ifac[kh + 1];
/*  639 */       int l1 = l2 / ip;
/*  640 */       int ido = n / l2;
/*  641 */       int idl1 = ido * l1;
/*  642 */       iw -= (ip - 1) * ido;
/*  643 */       na = 1 - na;
/*      */       
/*  645 */       int state = 100; while (true)
/*      */       { int ix2; int ix3;
/*  647 */         switch (state)
/*      */         { case 100:
/*  649 */             if (ip != 4) {
/*  650 */               state = 102;
/*      */               
/*      */               continue;
/*      */             } 
/*  654 */             ix2 = iw + ido;
/*  655 */             ix3 = ix2 + ido;
/*  656 */             if (na != 0) {
/*  657 */               dradf4(ido, l1, ch, c, wa, iw - 1, wa, ix2 - 1, wa, ix3 - 1);
/*      */             } else {
/*  659 */               dradf4(ido, l1, c, ch, wa, iw - 1, wa, ix2 - 1, wa, ix3 - 1);
/*  660 */             }  state = 110;
/*      */           
/*      */           case 102:
/*  663 */             if (ip != 2) {
/*  664 */               state = 104;
/*      */               continue;
/*      */             } 
/*  667 */             if (na != 0) {
/*  668 */               state = 103;
/*      */               continue;
/*      */             } 
/*  671 */             dradf2(ido, l1, c, ch, wa, iw - 1);
/*  672 */             state = 110;
/*      */           
/*      */           case 103:
/*  675 */             dradf2(ido, l1, ch, c, wa, iw - 1);
/*      */           case 104:
/*  677 */             if (ido == 1)
/*  678 */               na = 1 - na; 
/*  679 */             if (na != 0) {
/*  680 */               state = 109;
/*      */               continue;
/*      */             } 
/*  683 */             dradfg(ido, ip, l1, idl1, c, c, c, ch, ch, wa, iw - 1);
/*  684 */             na = 1;
/*  685 */             state = 110;
/*      */           
/*      */           case 109:
/*  688 */             dradfg(ido, ip, l1, idl1, ch, ch, ch, c, c, wa, iw - 1);
/*  689 */             na = 0; break;
/*      */           case 110:
/*  691 */             break; }  }  l2 = l1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  696 */     if (na == 1)
/*      */       return; 
/*  698 */     for (int i = 0; i < n; i++) {
/*  699 */       c[i] = ch[i];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void dradb2(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index) {
/*  707 */     int t0 = l1 * ido;
/*      */     
/*  709 */     int t1 = 0;
/*  710 */     int t2 = 0;
/*  711 */     int t3 = (ido << 1) - 1; int k;
/*  712 */     for (k = 0; k < l1; k++) {
/*  713 */       ch[t1] = cc[t2] + cc[t3 + t2];
/*  714 */       ch[t1 + t0] = cc[t2] - cc[t3 + t2];
/*  715 */       t2 = (t1 += ido) << 1;
/*      */     } 
/*      */     
/*  718 */     if (ido < 2)
/*      */       return; 
/*  720 */     if (ido != 2) {
/*  721 */       t1 = 0;
/*  722 */       t2 = 0;
/*  723 */       for (k = 0; k < l1; k++) {
/*  724 */         t3 = t1;
/*  725 */         int t4, t5 = (t4 = t2) + (ido << 1);
/*  726 */         int t6 = t0 + t1;
/*  727 */         for (int i = 2; i < ido; i += 2) {
/*  728 */           t3 += 2;
/*  729 */           t4 += 2;
/*  730 */           t5 -= 2;
/*  731 */           t6 += 2;
/*  732 */           ch[t3 - 1] = cc[t4 - 1] + cc[t5 - 1];
/*  733 */           float tr2 = cc[t4 - 1] - cc[t5 - 1];
/*  734 */           ch[t3] = cc[t4] - cc[t5];
/*  735 */           float ti2 = cc[t4] + cc[t5];
/*  736 */           ch[t6 - 1] = wa1[index + i - 2] * tr2 - wa1[index + i - 1] * ti2;
/*  737 */           ch[t6] = wa1[index + i - 2] * ti2 + wa1[index + i - 1] * tr2;
/*      */         } 
/*  739 */         t2 = (t1 += ido) << 1;
/*      */       } 
/*  741 */       if (ido % 2 == 1) {
/*      */         return;
/*      */       }
/*      */     } 
/*  745 */     t1 = ido - 1;
/*  746 */     t2 = ido - 1;
/*  747 */     for (k = 0; k < l1; k++) {
/*  748 */       ch[t1] = cc[t2] + cc[t2];
/*  749 */       ch[t1 + t0] = -(cc[t2 + 1] + cc[t2 + 1]);
/*  750 */       t1 += ido;
/*  751 */       t2 += ido << 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void dradb3(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2) {
/*  759 */     int t0 = l1 * ido;
/*      */     
/*  761 */     int t1 = 0;
/*  762 */     int t2 = t0 << 1;
/*  763 */     int t3 = ido << 1;
/*  764 */     int t4 = ido + (ido << 1);
/*  765 */     int t5 = 0; int k;
/*  766 */     for (k = 0; k < l1; k++) {
/*  767 */       float tr2 = cc[t3 - 1] + cc[t3 - 1];
/*  768 */       float cr2 = cc[t5] + taur * tr2;
/*  769 */       ch[t1] = cc[t5] + tr2;
/*  770 */       float ci3 = taui * (cc[t3] + cc[t3]);
/*  771 */       ch[t1 + t0] = cr2 - ci3;
/*  772 */       ch[t1 + t2] = cr2 + ci3;
/*  773 */       t1 += ido;
/*  774 */       t3 += t4;
/*  775 */       t5 += t4;
/*      */     } 
/*      */     
/*  778 */     if (ido == 1) {
/*      */       return;
/*      */     }
/*  781 */     t1 = 0;
/*  782 */     t3 = ido << 1;
/*  783 */     for (k = 0; k < l1; k++) {
/*  784 */       int t7 = t1 + (t1 << 1);
/*  785 */       int t6 = t5 = t7 + t3;
/*  786 */       int t8 = t1;
/*  787 */       int t9, t10 = (t9 = t1 + t0) + t0;
/*      */       
/*  789 */       for (int i = 2; i < ido; i += 2) {
/*  790 */         t5 += 2;
/*  791 */         t6 -= 2;
/*  792 */         t7 += 2;
/*  793 */         t8 += 2;
/*  794 */         t9 += 2;
/*  795 */         t10 += 2;
/*  796 */         float tr2 = cc[t5 - 1] + cc[t6 - 1];
/*  797 */         float cr2 = cc[t7 - 1] + taur * tr2;
/*  798 */         ch[t8 - 1] = cc[t7 - 1] + tr2;
/*  799 */         float ti2 = cc[t5] - cc[t6];
/*  800 */         float ci2 = cc[t7] + taur * ti2;
/*  801 */         ch[t8] = cc[t7] + ti2;
/*  802 */         float cr3 = taui * (cc[t5 - 1] - cc[t6 - 1]);
/*  803 */         float ci3 = taui * (cc[t5] + cc[t6]);
/*  804 */         float dr2 = cr2 - ci3;
/*  805 */         float dr3 = cr2 + ci3;
/*  806 */         float di2 = ci2 + cr3;
/*  807 */         float di3 = ci2 - cr3;
/*  808 */         ch[t9 - 1] = wa1[index1 + i - 2] * dr2 - wa1[index1 + i - 1] * di2;
/*  809 */         ch[t9] = wa1[index1 + i - 2] * di2 + wa1[index1 + i - 1] * dr2;
/*  810 */         ch[t10 - 1] = wa2[index2 + i - 2] * dr3 - wa2[index2 + i - 1] * di3;
/*  811 */         ch[t10] = wa2[index2 + i - 2] * di3 + wa2[index2 + i - 1] * dr3;
/*      */       } 
/*  813 */       t1 += ido;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void dradb4(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2, float[] wa3, int index3) {
/*  821 */     int t0 = l1 * ido;
/*      */     
/*  823 */     int t1 = 0;
/*  824 */     int t2 = ido << 2;
/*  825 */     int t3 = 0;
/*  826 */     int t6 = ido << 1; int k;
/*  827 */     for (k = 0; k < l1; k++) {
/*  828 */       int i = t3 + t6;
/*  829 */       int t5 = t1;
/*  830 */       float tr3 = cc[i - 1] + cc[i - 1];
/*  831 */       float tr4 = cc[i] + cc[i];
/*  832 */       float tr1 = cc[t3] - cc[(i += t6) - 1];
/*  833 */       float tr2 = cc[t3] + cc[i - 1];
/*  834 */       ch[t5] = tr2 + tr3;
/*  835 */       ch[t5 += t0] = tr1 - tr4;
/*  836 */       ch[t5 += t0] = tr2 - tr3;
/*  837 */       ch[t5 += t0] = tr1 + tr4;
/*  838 */       t1 += ido;
/*  839 */       t3 += t2;
/*      */     } 
/*      */     
/*  842 */     if (ido < 2)
/*      */       return; 
/*  844 */     if (ido != 2) {
/*  845 */       t1 = 0;
/*  846 */       for (k = 0; k < l1; k++) {
/*  847 */         int j, t5 = (j = t3 = (t2 = t1 << 2) + t6) + t6;
/*  848 */         int t7 = t1;
/*  849 */         for (int i = 2; i < ido; i += 2) {
/*  850 */           t2 += 2;
/*  851 */           t3 += 2;
/*  852 */           j -= 2;
/*  853 */           t5 -= 2;
/*  854 */           t7 += 2;
/*  855 */           float ti1 = cc[t2] + cc[t5];
/*  856 */           float ti2 = cc[t2] - cc[t5];
/*  857 */           float ti3 = cc[t3] - cc[j];
/*  858 */           float tr4 = cc[t3] + cc[j];
/*  859 */           float tr1 = cc[t2 - 1] - cc[t5 - 1];
/*  860 */           float tr2 = cc[t2 - 1] + cc[t5 - 1];
/*  861 */           float ti4 = cc[t3 - 1] - cc[j - 1];
/*  862 */           float tr3 = cc[t3 - 1] + cc[j - 1];
/*  863 */           ch[t7 - 1] = tr2 + tr3;
/*  864 */           float cr3 = tr2 - tr3;
/*  865 */           ch[t7] = ti2 + ti3;
/*  866 */           float ci3 = ti2 - ti3;
/*  867 */           float cr2 = tr1 - tr4;
/*  868 */           float cr4 = tr1 + tr4;
/*  869 */           float ci2 = ti1 + ti4;
/*  870 */           float ci4 = ti1 - ti4;
/*      */           int t8;
/*  872 */           ch[(t8 = t7 + t0) - 1] = wa1[index1 + i - 2] * cr2 - wa1[index1 + i - 1] * ci2;
/*  873 */           ch[t8] = wa1[index1 + i - 2] * ci2 + wa1[index1 + i - 1] * cr2;
/*  874 */           ch[(t8 += t0) - 1] = wa2[index2 + i - 2] * cr3 - wa2[index2 + i - 1] * ci3;
/*  875 */           ch[t8] = wa2[index2 + i - 2] * ci3 + wa2[index2 + i - 1] * cr3;
/*  876 */           ch[(t8 += t0) - 1] = wa3[index3 + i - 2] * cr4 - wa3[index3 + i - 1] * ci4;
/*  877 */           ch[t8] = wa3[index3 + i - 2] * ci4 + wa3[index3 + i - 1] * cr4;
/*      */         } 
/*  879 */         t1 += ido;
/*      */       } 
/*  881 */       if (ido % 2 == 1) {
/*      */         return;
/*      */       }
/*      */     } 
/*  885 */     t1 = ido;
/*  886 */     t2 = ido << 2;
/*  887 */     t3 = ido - 1;
/*  888 */     int t4 = ido + (ido << 1);
/*  889 */     for (k = 0; k < l1; k++) {
/*  890 */       int t5 = t3;
/*  891 */       float ti1 = cc[t1] + cc[t4];
/*  892 */       float ti2 = cc[t4] - cc[t1];
/*  893 */       float tr1 = cc[t1 - 1] - cc[t4 - 1];
/*  894 */       float tr2 = cc[t1 - 1] + cc[t4 - 1];
/*  895 */       ch[t5] = tr2 + tr2;
/*  896 */       ch[t5 += t0] = sqrt2 * (tr1 - ti1);
/*  897 */       ch[t5 += t0] = ti2 + ti2;
/*  898 */       ch[t5 += t0] = -sqrt2 * (tr1 + ti1);
/*      */       
/*  900 */       t3 += ido;
/*  901 */       t1 += t2;
/*  902 */       t4 += t2;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void dradbg(int ido, int ip, int l1, int idl1, float[] cc, float[] c1, float[] c2, float[] ch, float[] ch2, float[] wa, int index) {
/*  909 */     int ipph = 0, t0 = 0, t10 = 0;
/*      */     
/*  911 */     int nbd = 0;
/*  912 */     float dcp = 0.0F, dsp = 0.0F;
/*  913 */     int ipp2 = 0;
/*      */     
/*  915 */     int state = 100; while (true) {
/*      */       int m; int n; int k; int l; int ik; int is; int t1; int t2; int t3; int t5; int t7; int t9; float ai1; float ar1;
/*      */       float arg;
/*  918 */       switch (state) {
/*      */         case 100:
/*  920 */           t10 = ip * ido;
/*  921 */           t0 = l1 * ido;
/*  922 */           arg = tpi / ip;
/*  923 */           dcp = (float)Math.cos(arg);
/*  924 */           dsp = (float)Math.sin(arg);
/*  925 */           nbd = ido - 1 >>> 1;
/*  926 */           ipp2 = ip;
/*  927 */           ipph = ip + 1 >>> 1;
/*  928 */           if (ido < l1) {
/*  929 */             state = 103;
/*      */             continue;
/*      */           } 
/*  932 */           t1 = 0;
/*  933 */           t2 = 0;
/*  934 */           for (k = 0; k < l1; k++) {
/*  935 */             int i2 = t1;
/*  936 */             int t4 = t2;
/*  937 */             for (int i1 = 0; i1 < ido; i1++) {
/*  938 */               ch[i2] = cc[t4];
/*  939 */               i2++;
/*  940 */               t4++;
/*      */             } 
/*  942 */             t1 += ido;
/*  943 */             t2 += t10;
/*      */           } 
/*  945 */           state = 106;
/*      */         
/*      */         case 103:
/*  948 */           t1 = 0;
/*  949 */           for (m = 0; m < ido; m++) {
/*  950 */             t2 = t1;
/*  951 */             int i1 = t1;
/*  952 */             for (k = 0; k < l1; k++) {
/*  953 */               ch[t2] = cc[i1];
/*  954 */               t2 += ido;
/*  955 */               i1 += t10;
/*      */             } 
/*  957 */             t1++;
/*      */           } 
/*      */         case 106:
/*  960 */           t1 = 0;
/*  961 */           t2 = ipp2 * t0;
/*  962 */           t7 = t5 = ido << 1;
/*  963 */           for (n = 1; n < ipph; n++) {
/*  964 */             t1 += t0;
/*  965 */             t2 -= t0;
/*  966 */             int i1 = t1;
/*  967 */             int t4 = t2;
/*  968 */             int t6 = t5;
/*  969 */             for (k = 0; k < l1; k++) {
/*  970 */               ch[i1] = cc[t6 - 1] + cc[t6 - 1];
/*  971 */               ch[t4] = cc[t6] + cc[t6];
/*  972 */               i1 += ido;
/*  973 */               t4 += ido;
/*  974 */               t6 += t10;
/*      */             } 
/*  976 */             t5 += t7;
/*      */           } 
/*  978 */           if (ido == 1) {
/*  979 */             state = 116;
/*      */             continue;
/*      */           } 
/*  982 */           if (nbd < l1) {
/*  983 */             state = 112;
/*      */             
/*      */             continue;
/*      */           } 
/*  987 */           t1 = 0;
/*  988 */           t2 = ipp2 * t0;
/*  989 */           t7 = 0;
/*  990 */           for (n = 1; n < ipph; n++) {
/*  991 */             t1 += t0;
/*  992 */             t2 -= t0;
/*  993 */             int i1 = t1;
/*  994 */             int t4 = t2;
/*      */             
/*  996 */             t7 += ido << 1;
/*  997 */             int t8 = t7;
/*  998 */             for (k = 0; k < l1; k++) {
/*  999 */               t5 = i1;
/* 1000 */               int t6 = t4;
/* 1001 */               int i2 = t8;
/* 1002 */               int t11 = t8;
/* 1003 */               for (m = 2; m < ido; m += 2) {
/* 1004 */                 t5 += 2;
/* 1005 */                 t6 += 2;
/* 1006 */                 i2 += 2;
/* 1007 */                 t11 -= 2;
/* 1008 */                 ch[t5 - 1] = cc[i2 - 1] + cc[t11 - 1];
/* 1009 */                 ch[t6 - 1] = cc[i2 - 1] - cc[t11 - 1];
/* 1010 */                 ch[t5] = cc[i2] - cc[t11];
/* 1011 */                 ch[t6] = cc[i2] + cc[t11];
/*      */               } 
/* 1013 */               i1 += ido;
/* 1014 */               t4 += ido;
/* 1015 */               t8 += t10;
/*      */             } 
/*      */           } 
/* 1018 */           state = 116;
/*      */         
/*      */         case 112:
/* 1021 */           t1 = 0;
/* 1022 */           t2 = ipp2 * t0;
/* 1023 */           t7 = 0;
/* 1024 */           for (n = 1; n < ipph; n++) {
/* 1025 */             t1 += t0;
/* 1026 */             t2 -= t0;
/* 1027 */             int i1 = t1;
/* 1028 */             int t4 = t2;
/* 1029 */             t7 += ido << 1;
/* 1030 */             int t8 = t7;
/* 1031 */             int i2 = t7;
/* 1032 */             for (m = 2; m < ido; m += 2) {
/* 1033 */               i1 += 2;
/* 1034 */               t4 += 2;
/* 1035 */               t8 += 2;
/* 1036 */               i2 -= 2;
/* 1037 */               t5 = i1;
/* 1038 */               int t6 = t4;
/* 1039 */               int t11 = t8;
/* 1040 */               int t12 = i2;
/* 1041 */               for (k = 0; k < l1; k++) {
/* 1042 */                 ch[t5 - 1] = cc[t11 - 1] + cc[t12 - 1];
/* 1043 */                 ch[t6 - 1] = cc[t11 - 1] - cc[t12 - 1];
/* 1044 */                 ch[t5] = cc[t11] - cc[t12];
/* 1045 */                 ch[t6] = cc[t11] + cc[t12];
/* 1046 */                 t5 += ido;
/* 1047 */                 t6 += ido;
/* 1048 */                 t11 += t10;
/* 1049 */                 t12 += t10;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         case 116:
/* 1054 */           ar1 = 1.0F;
/* 1055 */           ai1 = 0.0F;
/* 1056 */           t1 = 0;
/* 1057 */           t9 = t2 = ipp2 * idl1;
/* 1058 */           t3 = (ip - 1) * idl1;
/* 1059 */           for (l = 1; l < ipph; l++) {
/* 1060 */             t1 += idl1;
/* 1061 */             t2 -= idl1;
/*      */             
/* 1063 */             float ar1h = dcp * ar1 - dsp * ai1;
/* 1064 */             ai1 = dcp * ai1 + dsp * ar1;
/* 1065 */             ar1 = ar1h;
/* 1066 */             int t4 = t1;
/* 1067 */             t5 = t2;
/* 1068 */             int t6 = 0;
/* 1069 */             t7 = idl1;
/* 1070 */             int t8 = t3; int i1;
/* 1071 */             for (i1 = 0; i1 < idl1; i1++) {
/* 1072 */               c2[t4++] = ch2[t6++] + ar1 * ch2[t7++];
/* 1073 */               c2[t5++] = ai1 * ch2[t8++];
/*      */             } 
/* 1075 */             float dc2 = ar1;
/* 1076 */             float ds2 = ai1;
/* 1077 */             float ar2 = ar1;
/* 1078 */             float ai2 = ai1;
/*      */             
/* 1080 */             t6 = idl1;
/* 1081 */             t7 = t9 - idl1;
/* 1082 */             for (n = 2; n < ipph; n++) {
/* 1083 */               t6 += idl1;
/* 1084 */               t7 -= idl1;
/* 1085 */               float ar2h = dc2 * ar2 - ds2 * ai2;
/* 1086 */               ai2 = dc2 * ai2 + ds2 * ar2;
/* 1087 */               ar2 = ar2h;
/* 1088 */               t4 = t1;
/* 1089 */               t5 = t2;
/* 1090 */               int t11 = t6;
/* 1091 */               int t12 = t7;
/* 1092 */               for (i1 = 0; i1 < idl1; i1++) {
/* 1093 */                 c2[t4++] = c2[t4++] + ar2 * ch2[t11++];
/* 1094 */                 c2[t5++] = c2[t5++] + ai2 * ch2[t12++];
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 1099 */           t1 = 0;
/* 1100 */           for (n = 1; n < ipph; n++) {
/* 1101 */             t1 += idl1;
/* 1102 */             t2 = t1;
/* 1103 */             for (int i1 = 0; i1 < idl1; i1++) {
/* 1104 */               ch2[i1] = ch2[i1] + ch2[t2++];
/*      */             }
/*      */           } 
/* 1107 */           t1 = 0;
/* 1108 */           t2 = ipp2 * t0;
/* 1109 */           for (n = 1; n < ipph; n++) {
/* 1110 */             t1 += t0;
/* 1111 */             t2 -= t0;
/* 1112 */             t3 = t1;
/* 1113 */             int t4 = t2;
/* 1114 */             for (k = 0; k < l1; k++) {
/* 1115 */               ch[t3] = c1[t3] - c1[t4];
/* 1116 */               ch[t4] = c1[t3] + c1[t4];
/* 1117 */               t3 += ido;
/* 1118 */               t4 += ido;
/*      */             } 
/*      */           } 
/*      */           
/* 1122 */           if (ido == 1) {
/* 1123 */             state = 132;
/*      */             continue;
/*      */           } 
/* 1126 */           if (nbd < l1) {
/* 1127 */             state = 128;
/*      */             
/*      */             continue;
/*      */           } 
/* 1131 */           t1 = 0;
/* 1132 */           t2 = ipp2 * t0;
/* 1133 */           for (n = 1; n < ipph; n++) {
/* 1134 */             t1 += t0;
/* 1135 */             t2 -= t0;
/* 1136 */             t3 = t1;
/* 1137 */             int t4 = t2;
/* 1138 */             for (k = 0; k < l1; k++) {
/* 1139 */               t5 = t3;
/* 1140 */               int t6 = t4;
/* 1141 */               for (m = 2; m < ido; m += 2) {
/* 1142 */                 t5 += 2;
/* 1143 */                 t6 += 2;
/* 1144 */                 ch[t5 - 1] = c1[t5 - 1] - c1[t6];
/* 1145 */                 ch[t6 - 1] = c1[t5 - 1] + c1[t6];
/* 1146 */                 ch[t5] = c1[t5] + c1[t6 - 1];
/* 1147 */                 ch[t6] = c1[t5] - c1[t6 - 1];
/*      */               } 
/* 1149 */               t3 += ido;
/* 1150 */               t4 += ido;
/*      */             } 
/*      */           } 
/* 1153 */           state = 132;
/*      */         
/*      */         case 128:
/* 1156 */           t1 = 0;
/* 1157 */           t2 = ipp2 * t0;
/* 1158 */           for (n = 1; n < ipph; n++) {
/* 1159 */             t1 += t0;
/* 1160 */             t2 -= t0;
/* 1161 */             t3 = t1;
/* 1162 */             int t4 = t2;
/* 1163 */             for (m = 2; m < ido; m += 2) {
/* 1164 */               t3 += 2;
/* 1165 */               t4 += 2;
/* 1166 */               t5 = t3;
/* 1167 */               int t6 = t4;
/* 1168 */               for (k = 0; k < l1; k++) {
/* 1169 */                 ch[t5 - 1] = c1[t5 - 1] - c1[t6];
/* 1170 */                 ch[t6 - 1] = c1[t5 - 1] + c1[t6];
/* 1171 */                 ch[t5] = c1[t5] + c1[t6 - 1];
/* 1172 */                 ch[t6] = c1[t5] - c1[t6 - 1];
/* 1173 */                 t5 += ido;
/* 1174 */                 t6 += ido;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         case 132:
/* 1179 */           if (ido == 1) {
/*      */             return;
/*      */           }
/* 1182 */           for (ik = 0; ik < idl1; ik++) {
/* 1183 */             c2[ik] = ch2[ik];
/*      */           }
/* 1185 */           t1 = 0;
/* 1186 */           for (n = 1; n < ip; n++) {
/* 1187 */             t2 = t1 += t0;
/* 1188 */             for (k = 0; k < l1; k++) {
/* 1189 */               c1[t2] = ch[t2];
/* 1190 */               t2 += ido;
/*      */             } 
/*      */           } 
/*      */           
/* 1194 */           if (nbd > l1) {
/* 1195 */             state = 139;
/*      */             
/*      */             continue;
/*      */           } 
/* 1199 */           is = -ido - 1;
/* 1200 */           t1 = 0;
/* 1201 */           for (n = 1; n < ip; n++) {
/* 1202 */             is += ido;
/* 1203 */             t1 += t0;
/* 1204 */             int idij = is;
/* 1205 */             t2 = t1;
/* 1206 */             for (m = 2; m < ido; m += 2) {
/* 1207 */               t2 += 2;
/* 1208 */               idij += 2;
/* 1209 */               t3 = t2;
/* 1210 */               for (k = 0; k < l1; k++) {
/* 1211 */                 c1[t3 - 1] = wa[index + idij - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3];
/* 1212 */                 c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1];
/* 1213 */                 t3 += ido;
/*      */               } 
/*      */             } 
/*      */           }  return;
/*      */         case 139:
/*      */           break;
/*      */       } 
/* 1220 */     }  int i = -ido - 1;
/* 1221 */     int j = 0;
/* 1222 */     for (byte b = 1; b < ip; b++) {
/* 1223 */       i += ido;
/* 1224 */       j += t0;
/* 1225 */       int t2 = j;
/* 1226 */       for (int k = 0; k < l1; k++) {
/* 1227 */         int idij = i;
/* 1228 */         int t3 = t2;
/* 1229 */         for (int m = 2; m < ido; m += 2) {
/* 1230 */           idij += 2;
/* 1231 */           t3 += 2;
/* 1232 */           c1[t3 - 1] = wa[index + idij - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3];
/* 1233 */           c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1];
/*      */         } 
/* 1235 */         t2 += ido;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void drftb1(int n, float[] c, float[] ch, float[] wa, int index, int[] ifac) {
/* 1245 */     int l2 = 0;
/*      */     
/* 1247 */     int ip = 0, ido = 0, idl1 = 0;
/*      */     
/* 1249 */     int nf = ifac[1];
/* 1250 */     int na = 0;
/* 1251 */     int l1 = 1;
/* 1252 */     int iw = 1;
/*      */     
/* 1254 */     for (int k1 = 0; k1 < nf; k1++) {
/* 1255 */       int state = 100; while (true) {
/*      */         int ix2; int ix3;
/* 1257 */         switch (state)
/*      */         { case 100:
/* 1259 */             ip = ifac[k1 + 2];
/* 1260 */             l2 = ip * l1;
/* 1261 */             ido = n / l2;
/* 1262 */             idl1 = ido * l1;
/* 1263 */             if (ip != 4) {
/* 1264 */               state = 103;
/*      */               continue;
/*      */             } 
/* 1267 */             ix2 = iw + ido;
/* 1268 */             ix3 = ix2 + ido;
/*      */             
/* 1270 */             if (na != 0) {
/* 1271 */               dradb4(ido, l1, ch, c, wa, index + iw - 1, wa, index + ix2 - 1, wa, index + ix3 - 1);
/*      */             } else {
/*      */               
/* 1274 */               dradb4(ido, l1, c, ch, wa, index + iw - 1, wa, index + ix2 - 1, wa, index + ix3 - 1);
/*      */             } 
/* 1276 */             na = 1 - na;
/* 1277 */             state = 115;
/*      */           
/*      */           case 103:
/* 1280 */             if (ip != 2) {
/* 1281 */               state = 106;
/*      */               
/*      */               continue;
/*      */             } 
/* 1285 */             if (na != 0) {
/* 1286 */               dradb2(ido, l1, ch, c, wa, index + iw - 1);
/*      */             } else {
/* 1288 */               dradb2(ido, l1, c, ch, wa, index + iw - 1);
/* 1289 */             }  na = 1 - na;
/* 1290 */             state = 115;
/*      */ 
/*      */           
/*      */           case 106:
/* 1294 */             if (ip != 3) {
/* 1295 */               state = 109;
/*      */               
/*      */               continue;
/*      */             } 
/* 1299 */             ix2 = iw + ido;
/* 1300 */             if (na != 0) {
/* 1301 */               dradb3(ido, l1, ch, c, wa, index + iw - 1, wa, index + ix2 - 1);
/*      */             } else {
/* 1303 */               dradb3(ido, l1, c, ch, wa, index + iw - 1, wa, index + ix2 - 1);
/* 1304 */             }  na = 1 - na;
/* 1305 */             state = 115;
/*      */           
/*      */           case 109:
/* 1308 */             if (na != 0) {
/* 1309 */               dradbg(ido, ip, l1, idl1, ch, ch, ch, c, c, wa, index + iw - 1);
/*      */             } else {
/* 1311 */               dradbg(ido, ip, l1, idl1, c, c, c, ch, ch, wa, index + iw - 1);
/* 1312 */             }  if (ido == 1)
/* 1313 */               na = 1 - na;  break;
/*      */           case 115:
/*      */             break; } 
/* 1316 */       }  l1 = l2;
/* 1317 */       iw += (ip - 1) * ido;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1322 */     if (na == 0)
/*      */       return; 
/* 1324 */     for (int i = 0; i < n; i++)
/* 1325 */       c[i] = ch[i]; 
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\jcraft\jorbis\Drft.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */