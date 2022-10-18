/*      */ package com.badlogic.gdx.tools.hiero;
/*      */ 
/*      */ import com.badlogic.gdx.ApplicationAdapter;
/*      */ import com.badlogic.gdx.ApplicationListener;
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.GlyphPage;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.ColorEffect;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.ConfigurableEffect;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.DistanceFieldEffect;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.EffectUtil;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.GradientEffect;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.OutlineEffect;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.OutlineWobbleEffect;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.OutlineZigzagEffect;
/*      */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.ShadowEffect;
/*      */ import com.badlogic.gdx.utils.StringBuilder;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.FileDialog;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.Font;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.prefs.Preferences;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.DefaultComboBoxModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JCheckBox;
/*      */ import javax.swing.JColorChooser;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JFormattedTextField;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRadioButton;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JSpinner;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.JTextPane;
/*      */ import javax.swing.JWindow;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SpinnerNumberModel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import org.lwjgl.opengl.GL11;
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
/*      */ 
/*      */ public class Hiero
/*      */   extends JFrame
/*      */ {
/*      */   UnicodeFont unicodeFont;
/*  118 */   Color renderingBackgroundColor = Color.BLACK;
/*  119 */   List<EffectPanel> effectPanels = new ArrayList<EffectPanel>();
/*      */   
/*      */   Preferences prefs;
/*      */   ColorEffect colorEffect;
/*      */   boolean batchMode = false;
/*      */   JScrollPane appliedEffectsScroll;
/*      */   JPanel appliedEffectsPanel;
/*      */   JButton addEffectButton;
/*      */   JTextPane sampleTextPane;
/*      */   JSpinner padAdvanceXSpinner;
/*      */   JList effectsList;
/*      */   LwjglCanvas rendererCanvas;
/*      */   JPanel gamePanel;
/*      */   JTextField fontFileText;
/*      */   JRadioButton fontFileRadio;
/*      */   JRadioButton systemFontRadio;
/*      */   JSpinner padBottomSpinner;
/*      */   JSpinner padLeftSpinner;
/*      */   JSpinner padRightSpinner;
/*      */   JSpinner padTopSpinner;
/*      */   JList fontList;
/*      */   JSpinner fontSizeSpinner;
/*      */   JSpinner gammaSpinner;
/*      */   DefaultComboBoxModel fontListModel;
/*      */   JLabel backgroundColorLabel;
/*      */   JButton browseButton;
/*      */   JSpinner padAdvanceYSpinner;
/*      */   JCheckBox italicCheckBox;
/*      */   JCheckBox boldCheckBox;
/*      */   JCheckBox monoCheckBox;
/*      */   JRadioButton javaRadio;
/*      */   JRadioButton nativeRadio;
/*      */   JRadioButton freeTypeRadio;
/*      */   JLabel glyphsTotalLabel;
/*      */   JLabel glyphPagesTotalLabel;
/*      */   JComboBox glyphPageHeightCombo;
/*      */   JComboBox glyphPageWidthCombo;
/*      */   JComboBox glyphPageCombo;
/*      */   JPanel glyphCachePanel;
/*      */   JRadioButton glyphCacheRadio;
/*      */   JRadioButton sampleTextRadio;
/*      */   DefaultComboBoxModel glyphPageComboModel;
/*      */   JButton resetCacheButton;
/*      */   JButton sampleAsciiButton;
/*      */   JButton sampleNeheButton;
/*      */   JButton sampleExtendedButton;
/*      */   DefaultComboBoxModel effectsListModel;
/*      */   JMenuItem openMenuItem;
/*      */   JMenuItem saveMenuItem;
/*      */   JMenuItem exitMenuItem;
/*      */   JMenuItem saveBMFontMenuItem;
/*      */   File saveBmFontFile;
/*  171 */   String lastSaveFilename = ""; String lastSaveBMFilename = ""; String lastOpenFilename = "";
/*      */   JPanel effectsPanel;
/*      */   JScrollPane effectsScroll;
/*      */   JPanel unicodePanel;
/*      */   
/*      */   public Hiero(String[] args) {
/*  177 */     super("Hiero v5 - Bitmap Font Tool");
/*  178 */     Splash splash = new Splash(this, "/splash.jpg", 2000);
/*  179 */     initialize();
/*  180 */     splash.close();
/*      */     
/*  182 */     this.rendererCanvas = new LwjglCanvas((ApplicationListener)new Renderer());
/*  183 */     this.gamePanel.add(this.rendererCanvas.getCanvas());
/*      */     
/*  185 */     this.prefs = Preferences.userNodeForPackage(Hiero.class);
/*  186 */     Color backgroundColor = EffectUtil.fromString(this.prefs.get("background", "000000"));
/*  187 */     this.backgroundColorLabel.setIcon(getColorIcon(backgroundColor));
/*  188 */     this
/*  189 */       .renderingBackgroundColor = new Color(backgroundColor.getRed() / 255.0F, backgroundColor.getGreen() / 255.0F, backgroundColor.getBlue() / 255.0F, 1.0F);
/*  190 */     this.fontList.setSelectedValue(this.prefs.get("system.font", "Arial"), true);
/*  191 */     this.fontFileText.setText(this.prefs.get("font.file", ""));
/*      */     
/*  193 */     Color foregroundColor = EffectUtil.fromString(this.prefs.get("foreground", "ffffff"));
/*  194 */     this.colorEffect = new ColorEffect();
/*  195 */     this.colorEffect.setColor(foregroundColor);
/*  196 */     this.effectsListModel.addElement(this.colorEffect);
/*  197 */     this.effectsListModel.addElement(new GradientEffect());
/*  198 */     this.effectsListModel.addElement(new OutlineEffect());
/*  199 */     this.effectsListModel.addElement(new OutlineWobbleEffect());
/*  200 */     this.effectsListModel.addElement(new OutlineZigzagEffect());
/*  201 */     this.effectsListModel.addElement(new ShadowEffect());
/*  202 */     this.effectsListModel.addElement(new DistanceFieldEffect());
/*  203 */     new EffectPanel((ConfigurableEffect)this.colorEffect);
/*      */     
/*  205 */     parseArgs(args);
/*      */     
/*  207 */     addWindowListener(new WindowAdapter() {
/*      */           public void windowClosed(WindowEvent event) {
/*  209 */             System.exit(0);
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*  214 */     updateFontSelector();
/*  215 */     setVisible(true);
/*      */   }
/*      */   JPanel bitmapPanel; static final String NEHE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghijklmnopqrstuvwxyz\n1234567890 \n\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*\000"; public static final String EXTENDED_CHARS;
/*      */   void initialize() {
/*  219 */     initializeComponents();
/*  220 */     initializeMenus();
/*  221 */     initializeEvents();
/*      */     
/*  223 */     setSize(1024, 600);
/*  224 */     setLocationRelativeTo((Component)null);
/*  225 */     setDefaultCloseOperation(2);
/*      */   }
/*      */   
/*      */   private void parseArgs(String[] args) {
/*  229 */     float scale = 1.0F;
/*      */     
/*  231 */     for (int i = 0; i < args.length; i++) {
/*  232 */       String param = args[i];
/*  233 */       boolean more = (i < args.length - 1);
/*      */       
/*  235 */       if (param.equals("-b") || param.equals("--batch")) {
/*  236 */         this.batchMode = true;
/*  237 */       } else if (more && (param.equals("-s") || param.equals("--scale"))) {
/*  238 */         scale = Float.parseFloat(args[++i]);
/*  239 */       } else if (more && (param.equals("-i") || param.equals("--input"))) {
/*  240 */         File f = new File(args[++i]);
/*  241 */         open(f);
/*  242 */         this.fontFileRadio.setText("");
/*  243 */         updateFont();
/*  244 */       } else if (more && (param.equals("-o") || param.equals("--output"))) {
/*  245 */         File f = new File(args[++i]);
/*  246 */         saveBm(f);
/*      */       } else {
/*  248 */         System.err.println("Unknown parameter: " + param);
/*  249 */         exit(3);
/*      */       } 
/*      */     } 
/*      */     
/*  253 */     this.fontSizeSpinner.setValue(Integer.valueOf((int)(0.5F + Math.max(4.0F, scale * ((Integer)this.fontSizeSpinner.getValue()).intValue()))));
/*      */   }
/*      */   
/*      */   void updateFontSelector() {
/*  257 */     boolean useFile = this.fontFileRadio.isSelected();
/*  258 */     this.fontList.setEnabled(!useFile);
/*  259 */     this.fontFileText.setEnabled(useFile);
/*  260 */     this.browseButton.setEnabled(useFile);
/*      */   }
/*      */   
/*      */   void updateFont() {
/*  264 */     int fontSize = ((Integer)this.fontSizeSpinner.getValue()).intValue();
/*      */     
/*  266 */     File file = null;
/*  267 */     if (this.fontFileRadio.isSelected()) {
/*  268 */       file = new File(this.fontFileText.getText());
/*  269 */       if (!file.exists() || !file.isFile()) file = null;
/*      */     
/*      */     } 
/*  272 */     boolean isFreeType = this.freeTypeRadio.isSelected();
/*  273 */     boolean isNative = this.nativeRadio.isSelected();
/*  274 */     boolean isJava = this.javaRadio.isSelected();
/*  275 */     this.addEffectButton.setVisible(isJava);
/*  276 */     this.effectsScroll.setVisible(isJava);
/*  277 */     this.appliedEffectsScroll.setVisible(isJava);
/*  278 */     this.boldCheckBox.setEnabled(!isFreeType);
/*  279 */     this.italicCheckBox.setEnabled(!isFreeType);
/*  280 */     this.bitmapPanel.setVisible(isFreeType);
/*  281 */     this.unicodePanel.setVisible(!isFreeType);
/*  282 */     updateFontSelector();
/*      */     
/*  284 */     UnicodeFont unicodeFont = null;
/*  285 */     if (file != null) {
/*      */       
/*      */       try {
/*      */         
/*  289 */         unicodeFont = new UnicodeFont(this.fontFileText.getText(), fontSize, this.boldCheckBox.isSelected(), this.italicCheckBox.isSelected());
/*  290 */       } catch (Throwable ex) {
/*  291 */         ex.printStackTrace();
/*  292 */         this.fontFileRadio.setSelected(false);
/*      */       } 
/*      */     }
/*      */     
/*  296 */     if (unicodeFont == null)
/*      */     {
/*      */       
/*  299 */       unicodeFont = new UnicodeFont(Font.decode(this.fontList.getSelectedValue()), fontSize, this.boldCheckBox.isSelected(), this.italicCheckBox.isSelected());
/*      */     }
/*      */     
/*  302 */     unicodeFont.setMono(this.monoCheckBox.isSelected());
/*  303 */     unicodeFont.setGamma(((Number)this.gammaSpinner.getValue()).floatValue());
/*  304 */     unicodeFont.setPaddingTop(((Number)this.padTopSpinner.getValue()).intValue());
/*  305 */     unicodeFont.setPaddingRight(((Number)this.padRightSpinner.getValue()).intValue());
/*  306 */     unicodeFont.setPaddingBottom(((Number)this.padBottomSpinner.getValue()).intValue());
/*  307 */     unicodeFont.setPaddingLeft(((Number)this.padLeftSpinner.getValue()).intValue());
/*  308 */     unicodeFont.setPaddingAdvanceX(((Number)this.padAdvanceXSpinner.getValue()).intValue());
/*  309 */     unicodeFont.setPaddingAdvanceY(((Number)this.padAdvanceYSpinner.getValue()).intValue());
/*  310 */     unicodeFont.setGlyphPageWidth(((Number)this.glyphPageWidthCombo.getSelectedItem()).intValue());
/*  311 */     unicodeFont.setGlyphPageHeight(((Number)this.glyphPageHeightCombo.getSelectedItem()).intValue());
/*  312 */     if (this.nativeRadio.isSelected()) {
/*  313 */       unicodeFont.setRenderType(UnicodeFont.RenderType.Native);
/*  314 */     } else if (this.freeTypeRadio.isSelected()) {
/*  315 */       unicodeFont.setRenderType(UnicodeFont.RenderType.FreeType);
/*      */     } else {
/*  317 */       unicodeFont.setRenderType(UnicodeFont.RenderType.Java);
/*      */     } 
/*  319 */     for (Iterator<EffectPanel> iter = this.effectPanels.iterator(); iter.hasNext(); ) {
/*  320 */       EffectPanel panel = iter.next();
/*  321 */       unicodeFont.getEffects().add(panel.getEffect());
/*      */     } 
/*      */     
/*  324 */     int size = this.sampleTextPane.getFont().getSize();
/*  325 */     if (size < 14) size = 14; 
/*  326 */     this.sampleTextPane.setFont(unicodeFont.getFont().deriveFont(size));
/*      */     
/*  328 */     if (this.unicodeFont != null) this.unicodeFont.dispose(); 
/*  329 */     this.unicodeFont = unicodeFont;
/*      */     
/*  331 */     updateFontSelector();
/*      */   }
/*      */   
/*      */   void saveBm(File file) {
/*  335 */     this.saveBmFontFile = file;
/*      */   }
/*      */   
/*      */   void save(File file) throws IOException {
/*  339 */     HieroSettings settings = new HieroSettings();
/*  340 */     settings.setFontName(this.fontList.getSelectedValue());
/*  341 */     settings.setFontSize(((Integer)this.fontSizeSpinner.getValue()).intValue());
/*  342 */     settings.setFont2File(this.fontFileText.getText());
/*  343 */     settings.setFont2Active(this.fontFileRadio.isSelected());
/*  344 */     settings.setBold(this.boldCheckBox.isSelected());
/*  345 */     settings.setItalic(this.italicCheckBox.isSelected());
/*  346 */     settings.setMono(this.monoCheckBox.isSelected());
/*  347 */     settings.setGamma(((Number)this.gammaSpinner.getValue()).floatValue());
/*  348 */     settings.setPaddingTop(((Number)this.padTopSpinner.getValue()).intValue());
/*  349 */     settings.setPaddingRight(((Number)this.padRightSpinner.getValue()).intValue());
/*  350 */     settings.setPaddingBottom(((Number)this.padBottomSpinner.getValue()).intValue());
/*  351 */     settings.setPaddingLeft(((Number)this.padLeftSpinner.getValue()).intValue());
/*  352 */     settings.setPaddingAdvanceX(((Number)this.padAdvanceXSpinner.getValue()).intValue());
/*  353 */     settings.setPaddingAdvanceY(((Number)this.padAdvanceYSpinner.getValue()).intValue());
/*  354 */     settings.setGlyphPageWidth(((Number)this.glyphPageWidthCombo.getSelectedItem()).intValue());
/*  355 */     settings.setGlyphPageHeight(((Number)this.glyphPageHeightCombo.getSelectedItem()).intValue());
/*  356 */     settings.setGlyphText(this.sampleTextPane.getText());
/*  357 */     if (this.nativeRadio.isSelected()) {
/*  358 */       settings.setRenderType(UnicodeFont.RenderType.Native.ordinal());
/*  359 */     } else if (this.freeTypeRadio.isSelected()) {
/*  360 */       settings.setRenderType(UnicodeFont.RenderType.FreeType.ordinal());
/*      */     } else {
/*  362 */       settings.setRenderType(UnicodeFont.RenderType.Java.ordinal());
/*  363 */     }  for (Iterator<EffectPanel> iter = this.effectPanels.iterator(); iter.hasNext(); ) {
/*  364 */       EffectPanel panel = iter.next();
/*  365 */       settings.getEffects().add(panel.getEffect());
/*      */     } 
/*  367 */     settings.save(file);
/*      */   }
/*      */   
/*      */   void open(File file) {
/*  371 */     EffectPanel[] panels = this.effectPanels.<EffectPanel>toArray(new EffectPanel[this.effectPanels.size()]);
/*  372 */     for (int i = 0; i < panels.length; i++) {
/*  373 */       panels[i].remove();
/*      */     }
/*  375 */     HieroSettings settings = new HieroSettings(file.getAbsolutePath());
/*  376 */     this.fontList.setSelectedValue(settings.getFontName(), true);
/*  377 */     this.fontSizeSpinner.setValue(new Integer(settings.getFontSize()));
/*  378 */     this.boldCheckBox.setSelected(settings.isBold());
/*  379 */     this.italicCheckBox.setSelected(settings.isItalic());
/*  380 */     this.monoCheckBox.setSelected(settings.isMono());
/*  381 */     this.gammaSpinner.setValue(new Float(settings.getGamma()));
/*  382 */     this.padTopSpinner.setValue(new Integer(settings.getPaddingTop()));
/*  383 */     this.padRightSpinner.setValue(new Integer(settings.getPaddingRight()));
/*  384 */     this.padBottomSpinner.setValue(new Integer(settings.getPaddingBottom()));
/*  385 */     this.padLeftSpinner.setValue(new Integer(settings.getPaddingLeft()));
/*  386 */     this.padAdvanceXSpinner.setValue(new Integer(settings.getPaddingAdvanceX()));
/*  387 */     this.padAdvanceYSpinner.setValue(new Integer(settings.getPaddingAdvanceY()));
/*  388 */     this.glyphPageWidthCombo.setSelectedItem(new Integer(settings.getGlyphPageWidth()));
/*  389 */     this.glyphPageHeightCombo.setSelectedItem(new Integer(settings.getGlyphPageHeight()));
/*  390 */     if (settings.getRenderType() == UnicodeFont.RenderType.Native.ordinal()) {
/*  391 */       this.nativeRadio.setSelected(true);
/*  392 */     } else if (settings.getRenderType() == UnicodeFont.RenderType.FreeType.ordinal()) {
/*  393 */       this.freeTypeRadio.setSelected(true);
/*  394 */     } else if (settings.getRenderType() == UnicodeFont.RenderType.Java.ordinal()) {
/*  395 */       this.javaRadio.setSelected(true);
/*  396 */     }  String gt = settings.getGlyphText();
/*  397 */     if (gt.length() > 0) {
/*  398 */       this.sampleTextPane.setText(settings.getGlyphText());
/*      */     }
/*      */     
/*  401 */     String font2 = settings.getFont2File();
/*  402 */     if (font2.length() > 0) {
/*  403 */       this.fontFileText.setText(font2);
/*      */     } else {
/*  405 */       this.fontFileText.setText(this.prefs.get("font.file", ""));
/*      */     } 
/*  407 */     this.fontFileRadio.setSelected(settings.isFont2Active());
/*  408 */     this.systemFontRadio.setSelected(!settings.isFont2Active());
/*      */     
/*  410 */     for (Iterator<ConfigurableEffect> iter = settings.getEffects().iterator(); iter.hasNext(); ) {
/*  411 */       ConfigurableEffect settingsEffect = iter.next();
/*  412 */       for (int j = 0, n = this.effectsListModel.getSize(); j < n; j++) {
/*  413 */         ConfigurableEffect effect = this.effectsListModel.getElementAt(j);
/*  414 */         if (effect.getClass() == settingsEffect.getClass()) {
/*  415 */           effect.setValues(settingsEffect.getValues());
/*  416 */           new EffectPanel(effect);
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  422 */     updateFont();
/*      */   }
/*      */   
/*      */   void exit(final int exitCode) {
/*  426 */     this.rendererCanvas.stop();
/*  427 */     EventQueue.invokeLater(new Runnable() {
/*      */           public void run() {
/*  429 */             System.exit(exitCode);
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   private void initializeEvents() {
/*  435 */     this.fontList.addListSelectionListener(new ListSelectionListener() {
/*      */           public void valueChanged(ListSelectionEvent evt) {
/*  437 */             if (evt.getValueIsAdjusting())
/*  438 */               return;  Hiero.this.prefs.put("system.font", Hiero.this.fontList.getSelectedValue());
/*  439 */             Hiero.this.updateFont();
/*      */           }
/*      */         });
/*      */     class FontUpdateListener
/*      */       implements ChangeListener, ActionListener {
/*      */       public void stateChanged(ChangeEvent evt) {
/*  445 */         Hiero.this.updateFont();
/*      */       }
/*      */       
/*      */       public void actionPerformed(ActionEvent evt) {
/*  449 */         Hiero.this.updateFont();
/*      */       }
/*      */       
/*      */       public void addSpinners(JSpinner[] spinners) {
/*  453 */         for (int i = 0; i < spinners.length; i++) {
/*  454 */           final JSpinner spinner = spinners[i];
/*  455 */           spinner.addChangeListener(this);
/*  456 */           ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
/*      */                 String lastText;
/*      */                 
/*      */                 public void keyReleased(KeyEvent evt) {
/*  460 */                   JFormattedTextField textField = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
/*  461 */                   String text = textField.getText();
/*  462 */                   if (text.length() == 0)
/*  463 */                     return;  if (text.equals(this.lastText))
/*  464 */                     return;  this.lastText = text;
/*  465 */                   int caretPosition = textField.getCaretPosition();
/*      */                   try {
/*  467 */                     spinner.setValue(Integer.valueOf(text));
/*  468 */                   } catch (NumberFormatException numberFormatException) {}
/*      */                   
/*  470 */                   textField.setCaretPosition(caretPosition);
/*      */                 }
/*      */               });
/*      */         } 
/*      */       }
/*      */     };
/*  476 */     FontUpdateListener listener = new FontUpdateListener();
/*      */     
/*  478 */     listener.addSpinners(new JSpinner[] { this.padTopSpinner, this.padRightSpinner, this.padBottomSpinner, this.padLeftSpinner, this.padAdvanceXSpinner, this.padAdvanceYSpinner });
/*      */     
/*  480 */     this.fontSizeSpinner.addChangeListener(listener);
/*  481 */     this.gammaSpinner.addChangeListener(listener);
/*      */     
/*  483 */     this.glyphPageWidthCombo.addActionListener(listener);
/*  484 */     this.glyphPageHeightCombo.addActionListener(listener);
/*  485 */     this.boldCheckBox.addActionListener(listener);
/*  486 */     this.italicCheckBox.addActionListener(listener);
/*  487 */     this.monoCheckBox.addActionListener(listener);
/*  488 */     this.resetCacheButton.addActionListener(listener);
/*  489 */     this.javaRadio.addActionListener(listener);
/*  490 */     this.nativeRadio.addActionListener(listener);
/*  491 */     this.freeTypeRadio.addActionListener(listener);
/*      */     
/*  493 */     this.sampleTextRadio.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  495 */             Hiero.this.glyphCachePanel.setVisible(false);
/*      */           }
/*      */         });
/*  498 */     this.glyphCacheRadio.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  500 */             Hiero.this.glyphCachePanel.setVisible(true);
/*      */           }
/*      */         });
/*      */     
/*  504 */     this.fontFileText.getDocument().addDocumentListener(new DocumentListener() {
/*      */           public void removeUpdate(DocumentEvent evt) {
/*  506 */             changed();
/*      */           }
/*      */           
/*      */           public void insertUpdate(DocumentEvent evt) {
/*  510 */             changed();
/*      */           }
/*      */           
/*      */           public void changedUpdate(DocumentEvent evt) {
/*  514 */             changed();
/*      */           }
/*      */           
/*      */           private void changed() {
/*  518 */             File file = new File(Hiero.this.fontFileText.getText());
/*  519 */             if (Hiero.this.fontList.isEnabled() && (!file.exists() || !file.isFile()))
/*  520 */               return;  Hiero.this.prefs.put("font.file", Hiero.this.fontFileText.getText());
/*  521 */             Hiero.this.updateFont();
/*      */           }
/*      */         });
/*      */     
/*  525 */     ActionListener al = new ActionListener() {
/*      */         public void actionPerformed(ActionEvent evt) {
/*  527 */           Hiero.this.updateFontSelector();
/*  528 */           Hiero.this.updateFont();
/*      */         }
/*      */       };
/*      */     
/*  532 */     this.systemFontRadio.addActionListener(al);
/*  533 */     this.fontFileRadio.addActionListener(al);
/*      */     
/*  535 */     this.browseButton.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  537 */             FileDialog dialog = new FileDialog(Hiero.this, "Choose TrueType font file", 0);
/*  538 */             dialog.setLocationRelativeTo((Component)null);
/*  539 */             dialog.setFile("*.ttf");
/*  540 */             dialog.setDirectory(Hiero.this.prefs.get("dir.font", ""));
/*  541 */             dialog.setVisible(true);
/*  542 */             if (dialog.getDirectory() != null) {
/*  543 */               Hiero.this.prefs.put("dir.font", dialog.getDirectory());
/*      */             }
/*      */             
/*  546 */             String fileName = dialog.getFile();
/*  547 */             if (fileName == null)
/*  548 */               return;  Hiero.this.fontFileText.setText((new File(dialog.getDirectory(), fileName)).getAbsolutePath());
/*      */           }
/*      */         });
/*      */     
/*  552 */     this.backgroundColorLabel.addMouseListener(new MouseAdapter() {
/*      */           public void mouseClicked(MouseEvent evt) {
/*  554 */             Color color = JColorChooser.showDialog(null, "Choose a background color", 
/*  555 */                 EffectUtil.fromString(Hiero.this.prefs.get("background", "000000")));
/*  556 */             if (color == null)
/*  557 */               return;  Hiero.this.renderingBackgroundColor = new Color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
/*  558 */             Hiero.this.backgroundColorLabel.setIcon(Hiero.getColorIcon(color));
/*  559 */             Hiero.this.prefs.put("background", EffectUtil.toString(color));
/*      */           }
/*      */         });
/*      */     
/*  563 */     this.effectsList.addListSelectionListener(new ListSelectionListener() {
/*      */           public void valueChanged(ListSelectionEvent evt) {
/*  565 */             ConfigurableEffect selectedEffect = Hiero.this.effectsList.getSelectedValue();
/*  566 */             boolean enabled = (selectedEffect != null);
/*  567 */             for (Iterator<Hiero.EffectPanel> iter = Hiero.this.effectPanels.iterator(); iter.hasNext(); ) {
/*  568 */               ConfigurableEffect effect = ((Hiero.EffectPanel)iter.next()).getEffect();
/*  569 */               if (effect == selectedEffect) {
/*  570 */                 enabled = false;
/*      */                 break;
/*      */               } 
/*      */             } 
/*  574 */             Hiero.this.addEffectButton.setEnabled(enabled);
/*      */           }
/*      */         });
/*      */     
/*  578 */     this.effectsList.addMouseListener(new MouseAdapter() {
/*      */           public void mouseClicked(MouseEvent evt) {
/*  580 */             if (evt.getClickCount() == 2 && Hiero.this.addEffectButton.isEnabled()) Hiero.this.addEffectButton.doClick();
/*      */           
/*      */           }
/*      */         });
/*  584 */     this.addEffectButton.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  586 */             new Hiero.EffectPanel(Hiero.this.effectsList.getSelectedValue());
/*      */           }
/*      */         });
/*      */     
/*  590 */     this.openMenuItem.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  592 */             FileDialog dialog = new FileDialog(Hiero.this, "Open Hiero settings file", 0);
/*  593 */             dialog.setLocationRelativeTo((Component)null);
/*  594 */             dialog.setFile("*.hiero");
/*  595 */             dialog.setDirectory(Hiero.this.prefs.get("dir.open", ""));
/*  596 */             dialog.setVisible(true);
/*  597 */             if (dialog.getDirectory() != null) {
/*  598 */               Hiero.this.prefs.put("dir.open", dialog.getDirectory());
/*      */             }
/*      */             
/*  601 */             String fileName = dialog.getFile();
/*  602 */             if (fileName == null)
/*  603 */               return;  Hiero.this.lastOpenFilename = fileName;
/*  604 */             Hiero.this.open(new File(dialog.getDirectory(), fileName));
/*      */           }
/*      */         });
/*      */     
/*  608 */     this.saveMenuItem.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  610 */             FileDialog dialog = new FileDialog(Hiero.this, "Save Hiero settings file", 1);
/*  611 */             dialog.setLocationRelativeTo((Component)null);
/*  612 */             dialog.setFile("*.hiero");
/*  613 */             dialog.setDirectory(Hiero.this.prefs.get("dir.save", ""));
/*      */             
/*  615 */             if (Hiero.this.lastSaveFilename.length() > 0) {
/*  616 */               dialog.setFile(Hiero.this.lastSaveFilename);
/*  617 */             } else if (Hiero.this.lastOpenFilename.length() > 0) {
/*  618 */               dialog.setFile(Hiero.this.lastOpenFilename);
/*      */             } 
/*      */             
/*  621 */             dialog.setVisible(true);
/*      */             
/*  623 */             if (dialog.getDirectory() != null) {
/*  624 */               Hiero.this.prefs.put("dir.save", dialog.getDirectory());
/*      */             }
/*      */             
/*  627 */             String fileName = dialog.getFile();
/*  628 */             if (fileName == null)
/*  629 */               return;  if (!fileName.endsWith(".hiero")) fileName = fileName + ".hiero"; 
/*  630 */             Hiero.this.lastSaveFilename = fileName;
/*  631 */             File file = new File(dialog.getDirectory(), fileName);
/*      */             try {
/*  633 */               Hiero.this.save(file);
/*  634 */             } catch (IOException ex) {
/*  635 */               throw new RuntimeException("Error saving Hiero settings file: " + file.getAbsolutePath(), ex);
/*      */             } 
/*      */           }
/*      */         });
/*      */     
/*  640 */     this.saveBMFontMenuItem.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  642 */             FileDialog dialog = new FileDialog(Hiero.this, "Save BMFont files", 1);
/*  643 */             dialog.setLocationRelativeTo((Component)null);
/*  644 */             dialog.setFile("*.fnt");
/*  645 */             dialog.setDirectory(Hiero.this.prefs.get("dir.savebm", ""));
/*      */             
/*  647 */             if (Hiero.this.lastSaveBMFilename.length() > 0) {
/*  648 */               dialog.setFile(Hiero.this.lastSaveBMFilename);
/*  649 */             } else if (Hiero.this.lastOpenFilename.length() > 0) {
/*  650 */               dialog.setFile(Hiero.this.lastOpenFilename.replace(".hiero", ".fnt"));
/*      */             } 
/*      */             
/*  653 */             dialog.setVisible(true);
/*  654 */             if (dialog.getDirectory() != null) {
/*  655 */               Hiero.this.prefs.put("dir.savebm", dialog.getDirectory());
/*      */             }
/*      */             
/*  658 */             String fileName = dialog.getFile();
/*  659 */             if (fileName == null)
/*  660 */               return;  Hiero.this.lastSaveBMFilename = fileName;
/*  661 */             Hiero.this.saveBm(new File(dialog.getDirectory(), fileName));
/*      */           }
/*      */         });
/*      */     
/*  665 */     this.exitMenuItem.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  667 */             Hiero.this.dispose();
/*      */           }
/*      */         });
/*      */     
/*  671 */     this.sampleNeheButton.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  673 */             Hiero.this.sampleTextPane.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghijklmnopqrstuvwxyz\n1234567890 \n\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*\000");
/*  674 */             Hiero.this.resetCacheButton.doClick();
/*      */           }
/*      */         });
/*      */     
/*  678 */     this.sampleAsciiButton.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  680 */             StringBuilder buffer = new StringBuilder();
/*  681 */             buffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nabcdefghijklmnopqrstuvwxyz\n1234567890 \n\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*\000");
/*  682 */             buffer.append('\n');
/*  683 */             int count = 0;
/*  684 */             for (int i = 33; i <= 255; i++) {
/*  685 */               if (buffer.indexOf(Character.toString((char)i)) == -1) {
/*  686 */                 buffer.append((char)i);
/*  687 */                 if (++count % 30 == 0) buffer.append('\n'); 
/*      */               } 
/*  689 */             }  Hiero.this.sampleTextPane.setText(buffer.toString());
/*  690 */             Hiero.this.resetCacheButton.doClick();
/*      */           }
/*      */         });
/*      */     
/*  694 */     this.sampleExtendedButton.addActionListener(new ActionListener() {
/*      */           public void actionPerformed(ActionEvent evt) {
/*  696 */             Hiero.this.sampleTextPane.setText(Hiero.EXTENDED_CHARS);
/*  697 */             Hiero.this.resetCacheButton.doClick();
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   private void initializeComponents() {
/*  703 */     getContentPane().setLayout(new GridBagLayout());
/*  704 */     JPanel leftSidePanel = new JPanel();
/*  705 */     leftSidePanel.setLayout(new GridBagLayout());
/*  706 */     getContentPane().add(leftSidePanel, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/*      */ 
/*      */     
/*  709 */     JPanel fontPanel = new JPanel();
/*  710 */     leftSidePanel.add(fontPanel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
/*      */     
/*  712 */     fontPanel.setLayout(new GridBagLayout());
/*  713 */     fontPanel.setBorder(BorderFactory.createTitledBorder("Font"));
/*      */     
/*  715 */     this.fontSizeSpinner = new JSpinner(new SpinnerNumberModel(32, 0, 256, 1));
/*  716 */     fontPanel.add(this.fontSizeSpinner, new GridBagConstraints(1, 3, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 10), 0, 0));
/*      */     
/*  718 */     ((JSpinner.DefaultEditor)this.fontSizeSpinner.getEditor()).getTextField().setColumns(2);
/*      */ 
/*      */     
/*  721 */     JScrollPane fontScroll = new JScrollPane();
/*  722 */     fontPanel.add(fontScroll, new GridBagConstraints(1, 1, 3, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */     
/*  725 */     this
/*  726 */       .fontListModel = new DefaultComboBoxModel<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
/*  727 */     this.fontList = new JList();
/*  728 */     fontScroll.setViewportView(this.fontList);
/*  729 */     this.fontList.setModel(this.fontListModel);
/*  730 */     this.fontList.setVisibleRowCount(6);
/*  731 */     this.fontList.setSelectedIndex(0);
/*  732 */     fontScroll.setMinimumSize(new Dimension(220, (this.fontList.getPreferredScrollableViewportSize()).height));
/*      */ 
/*      */ 
/*      */     
/*  736 */     this.systemFontRadio = new JRadioButton("System:", true);
/*  737 */     fontPanel.add(this.systemFontRadio, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 12, 0, new Insets(0, 5, 0, 5), 0, 0));
/*      */     
/*  739 */     this.systemFontRadio.setMargin(new Insets(0, 0, 0, 0));
/*      */ 
/*      */     
/*  742 */     this.fontFileRadio = new JRadioButton("File:");
/*  743 */     fontPanel.add(this.fontFileRadio, new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 5, 5, 5), 0, 0));
/*      */     
/*  745 */     this.fontFileRadio.setMargin(new Insets(0, 0, 0, 0));
/*      */ 
/*      */     
/*  748 */     this.fontFileText = new JTextField();
/*  749 */     fontPanel.add(this.fontFileText, new GridBagConstraints(1, 2, 2, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 5, 0), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  753 */     fontPanel.add(new JLabel("Size:"), new GridBagConstraints(0, 3, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  758 */     this.unicodePanel = new JPanel(new GridBagLayout());
/*  759 */     fontPanel.add(this.unicodePanel, new GridBagConstraints(2, 3, 2, 1, 0.0D, 0.0D, 13, 2, new Insets(0, 0, 0, 5), 0, 0));
/*      */ 
/*      */     
/*  762 */     this.boldCheckBox = new JCheckBox("Bold");
/*  763 */     this.unicodePanel.add(this.boldCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  767 */     this.italicCheckBox = new JCheckBox("Italic");
/*  768 */     this.unicodePanel.add(this.italicCheckBox, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  773 */     this.bitmapPanel = new JPanel(new GridBagLayout());
/*  774 */     fontPanel.add(this.bitmapPanel, new GridBagConstraints(2, 3, 2, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 5), 0, 0));
/*      */ 
/*      */     
/*  777 */     this.bitmapPanel.add(new JLabel("Gamma:"), new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  781 */     this.gammaSpinner = new JSpinner(new SpinnerNumberModel(1.7999999523162842D, 0.0D, 30.0D, 0.01D));
/*  782 */     ((JSpinner.DefaultEditor)this.gammaSpinner.getEditor()).getTextField().setColumns(2);
/*  783 */     this.bitmapPanel.add(this.gammaSpinner, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 5, 10), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  787 */     this.monoCheckBox = new JCheckBox("Mono");
/*  788 */     this.bitmapPanel.add(this.monoCheckBox, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  793 */     this.browseButton = new JButton("...");
/*  794 */     fontPanel.add(this.browseButton, new GridBagConstraints(3, 2, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */     
/*  796 */     this.browseButton.setMargin(new Insets(0, 0, 0, 0));
/*      */ 
/*      */     
/*  799 */     fontPanel.add(new JLabel("Rendering:"), new GridBagConstraints(0, 4, 1, 1, 0.0D, 0.0D, 12, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  803 */     JPanel jPanel1 = new JPanel(new GridBagLayout());
/*  804 */     fontPanel.add(jPanel1, new GridBagConstraints(1, 4, 3, 1, 0.0D, 0.0D, 18, 0, new Insets(0, 0, 0, 0), 0, 0));
/*      */ 
/*      */     
/*  807 */     this.freeTypeRadio = new JRadioButton("FreeType");
/*  808 */     jPanel1.add(this.freeTypeRadio, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  812 */     this.javaRadio = new JRadioButton("Java");
/*  813 */     jPanel1.add(this.javaRadio, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  817 */     this.nativeRadio = new JRadioButton("Native");
/*  818 */     jPanel1.add(this.nativeRadio, new GridBagConstraints(2, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  822 */     ButtonGroup buttonGroup = new ButtonGroup();
/*  823 */     buttonGroup.add(this.systemFontRadio);
/*  824 */     buttonGroup.add(this.fontFileRadio);
/*  825 */     buttonGroup = new ButtonGroup();
/*  826 */     buttonGroup.add(this.freeTypeRadio);
/*  827 */     buttonGroup.add(this.javaRadio);
/*  828 */     buttonGroup.add(this.nativeRadio);
/*  829 */     this.freeTypeRadio.setSelected(true);
/*      */ 
/*      */     
/*  832 */     JPanel samplePanel = new JPanel();
/*  833 */     leftSidePanel.add(samplePanel, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 1, new Insets(5, 0, 5, 5), 0, 0));
/*      */     
/*  835 */     samplePanel.setLayout(new GridBagLayout());
/*  836 */     samplePanel.setBorder(BorderFactory.createTitledBorder("Sample Text"));
/*      */     
/*  838 */     JScrollPane textScroll = new JScrollPane();
/*  839 */     samplePanel.add(textScroll, new GridBagConstraints(0, 0, 4, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */     
/*  842 */     this.sampleTextPane = new JTextPane();
/*  843 */     textScroll.setViewportView(this.sampleTextPane);
/*      */ 
/*      */ 
/*      */     
/*  847 */     this.sampleNeheButton = new JButton();
/*  848 */     this.sampleNeheButton.setText("NEHE");
/*  849 */     samplePanel.add(this.sampleNeheButton, new GridBagConstraints(2, 1, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  853 */     this.sampleAsciiButton = new JButton();
/*  854 */     this.sampleAsciiButton.setText("ASCII");
/*  855 */     samplePanel.add(this.sampleAsciiButton, new GridBagConstraints(3, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  859 */     this.sampleExtendedButton = new JButton();
/*  860 */     this.sampleExtendedButton.setText("Extended");
/*  861 */     samplePanel.add(this.sampleExtendedButton, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 13, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  866 */     JPanel renderingPanel = new JPanel();
/*  867 */     leftSidePanel.add(renderingPanel, new GridBagConstraints(0, 1, 2, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 5, 5, 5), 0, 0));
/*      */     
/*  869 */     renderingPanel.setBorder(BorderFactory.createTitledBorder("Rendering"));
/*  870 */     renderingPanel.setLayout(new GridBagLayout());
/*      */     
/*  872 */     JPanel wrapperPanel = new JPanel();
/*  873 */     renderingPanel.add(wrapperPanel, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 5, 5, 5), 0, 0));
/*      */     
/*  875 */     wrapperPanel.setLayout(new BorderLayout());
/*  876 */     wrapperPanel.setBackground(Color.white);
/*      */     
/*  878 */     this.gamePanel = new JPanel();
/*  879 */     wrapperPanel.add(this.gamePanel);
/*  880 */     this.gamePanel.setLayout(new BorderLayout());
/*  881 */     this.gamePanel.setBackground(Color.white);
/*      */ 
/*      */ 
/*      */     
/*  885 */     this.glyphCachePanel = new JPanel()
/*      */       {
/*      */         private int maxWidth;
/*      */         
/*      */         public Dimension getPreferredSize() {
/*  890 */           Dimension size = super.getPreferredSize();
/*  891 */           this.maxWidth = Math.max(this.maxWidth, size.width);
/*  892 */           size.width = this.maxWidth;
/*  893 */           return size;
/*      */         }
/*      */       };
/*  896 */     this.glyphCachePanel.setVisible(false);
/*  897 */     renderingPanel.add(this.glyphCachePanel, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 11, 2, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/*  899 */     this.glyphCachePanel.setLayout(new GridBagLayout());
/*      */     
/*  901 */     this.glyphCachePanel.add(new JLabel("Glyphs:"), new GridBagConstraints(0, 4, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  905 */     this.glyphCachePanel.add(new JLabel("Pages:"), new GridBagConstraints(0, 3, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  909 */     this.glyphCachePanel.add(new JLabel("Page width:"), new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  913 */     this.glyphCachePanel.add(new JLabel("Page height:"), new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  917 */     this.glyphPageWidthCombo = new JComboBox(new DefaultComboBoxModel((Object[])new Integer[] { new Integer(32), new Integer(64), new Integer(128), new Integer(256), new Integer(512), new Integer(1024), new Integer(2048) }));
/*      */     
/*  919 */     this.glyphCachePanel.add(this.glyphPageWidthCombo, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */     
/*  921 */     this.glyphPageWidthCombo.setSelectedIndex(4);
/*      */ 
/*      */     
/*  924 */     this.glyphPageHeightCombo = new JComboBox(new DefaultComboBoxModel((Object[])new Integer[] { new Integer(32), new Integer(64), new Integer(128), new Integer(256), new Integer(512), new Integer(1024), new Integer(2048) }));
/*      */     
/*  926 */     this.glyphCachePanel.add(this.glyphPageHeightCombo, new GridBagConstraints(1, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */     
/*  928 */     this.glyphPageHeightCombo.setSelectedIndex(4);
/*      */ 
/*      */     
/*  931 */     this.resetCacheButton = new JButton("Reset Cache");
/*  932 */     this.glyphCachePanel.add(this.resetCacheButton, new GridBagConstraints(0, 6, 2, 1, 1.0D, 0.0D, 10, 0, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  936 */     this.glyphPagesTotalLabel = new JLabel("1");
/*  937 */     this.glyphCachePanel.add(this.glyphPagesTotalLabel, new GridBagConstraints(1, 3, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  941 */     this.glyphsTotalLabel = new JLabel("0");
/*  942 */     this.glyphCachePanel.add(this.glyphsTotalLabel, new GridBagConstraints(1, 4, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  946 */     this.glyphPageComboModel = new DefaultComboBoxModel();
/*  947 */     this.glyphPageCombo = new JComboBox();
/*  948 */     this.glyphCachePanel.add(this.glyphPageCombo, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */     
/*  950 */     this.glyphPageCombo.setModel(this.glyphPageComboModel);
/*      */ 
/*      */     
/*  953 */     this.glyphCachePanel.add(new JLabel("View:"), new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  958 */     JPanel radioButtonsPanel = new JPanel();
/*  959 */     renderingPanel.add(radioButtonsPanel, new GridBagConstraints(0, 0, 2, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/*  961 */     radioButtonsPanel.setLayout(new GridBagLayout());
/*      */     
/*  963 */     this.sampleTextRadio = new JRadioButton("Sample text");
/*  964 */     radioButtonsPanel.add(this.sampleTextRadio, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */     
/*  966 */     this.sampleTextRadio.setSelected(true);
/*      */ 
/*      */     
/*  969 */     this.glyphCacheRadio = new JRadioButton("Glyph cache");
/*  970 */     radioButtonsPanel.add(this.glyphCacheRadio, new GridBagConstraints(3, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  974 */     radioButtonsPanel.add(new JLabel("Background:"), new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  978 */     this.backgroundColorLabel = new JLabel();
/*  979 */     radioButtonsPanel.add(this.backgroundColorLabel, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 5, 5), 0, 0));
/*      */ 
/*      */     
/*  982 */     ButtonGroup buttonGroup1 = new ButtonGroup();
/*  983 */     buttonGroup1.add(this.glyphCacheRadio);
/*  984 */     buttonGroup1.add(this.sampleTextRadio);
/*      */ 
/*      */     
/*  987 */     JPanel rightSidePanel = new JPanel();
/*  988 */     rightSidePanel.setLayout(new GridBagLayout());
/*  989 */     getContentPane().add(rightSidePanel, new GridBagConstraints(1, 0, 1, 2, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/*      */ 
/*      */     
/*  992 */     JPanel paddingPanel = new JPanel();
/*  993 */     paddingPanel.setLayout(new GridBagLayout());
/*  994 */     rightSidePanel.add(paddingPanel, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 5, 5), 0, 0));
/*      */     
/*  996 */     paddingPanel.setBorder(BorderFactory.createTitledBorder("Padding"));
/*      */     
/*  998 */     this.padTopSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
/*  999 */     paddingPanel.add(this.padTopSpinner, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/* 1001 */     ((JSpinner.DefaultEditor)this.padTopSpinner.getEditor()).getTextField().setColumns(2);
/*      */ 
/*      */     
/* 1004 */     this.padRightSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
/* 1005 */     paddingPanel.add(this.padRightSpinner, new GridBagConstraints(2, 2, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 5), 0, 0));
/*      */     
/* 1007 */     ((JSpinner.DefaultEditor)this.padRightSpinner.getEditor()).getTextField().setColumns(2);
/*      */ 
/*      */     
/* 1010 */     this.padLeftSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
/* 1011 */     paddingPanel.add(this.padLeftSpinner, new GridBagConstraints(0, 2, 1, 1, 1.0D, 0.0D, 13, 0, new Insets(0, 5, 0, 0), 0, 0));
/*      */     
/* 1013 */     ((JSpinner.DefaultEditor)this.padLeftSpinner.getEditor()).getTextField().setColumns(2);
/*      */ 
/*      */     
/* 1016 */     this.padBottomSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
/* 1017 */     paddingPanel.add(this.padBottomSpinner, new GridBagConstraints(1, 3, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/* 1019 */     ((JSpinner.DefaultEditor)this.padBottomSpinner.getEditor()).getTextField().setColumns(2);
/*      */ 
/*      */     
/* 1022 */     JPanel advancePanel = new JPanel();
/* 1023 */     FlowLayout advancePanelLayout = new FlowLayout();
/* 1024 */     advancePanel.setLayout(advancePanelLayout);
/* 1025 */     paddingPanel.add(advancePanel, new GridBagConstraints(0, 4, 3, 1, 1.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*      */ 
/*      */     
/* 1028 */     advancePanel.add(new JLabel("X:"));
/*      */ 
/*      */     
/* 1031 */     this.padAdvanceXSpinner = new JSpinner(new SpinnerNumberModel(-2, -999, 999, 1));
/* 1032 */     advancePanel.add(this.padAdvanceXSpinner);
/* 1033 */     ((JSpinner.DefaultEditor)this.padAdvanceXSpinner.getEditor()).getTextField().setColumns(2);
/*      */ 
/*      */     
/* 1036 */     advancePanel.add(new JLabel("Y:"));
/*      */ 
/*      */     
/* 1039 */     this.padAdvanceYSpinner = new JSpinner(new SpinnerNumberModel(-2, -999, 999, 1));
/* 1040 */     advancePanel.add(this.padAdvanceYSpinner);
/* 1041 */     ((JSpinner.DefaultEditor)this.padAdvanceYSpinner.getEditor()).getTextField().setColumns(2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1046 */     this.effectsPanel = new JPanel();
/* 1047 */     this.effectsPanel.setLayout(new GridBagLayout());
/* 1048 */     rightSidePanel.add(this.effectsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(5, 0, 5, 5), 0, 0));
/*      */     
/* 1050 */     this.effectsPanel.setBorder(BorderFactory.createTitledBorder("Effects"));
/* 1051 */     this.effectsPanel.setMinimumSize(new Dimension(210, 1));
/*      */     
/* 1053 */     this.effectsScroll = new JScrollPane();
/* 1054 */     this.effectsPanel.add(this.effectsScroll, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 11, 2, new Insets(0, 5, 5, 5), 0, 0));
/*      */ 
/*      */     
/* 1057 */     this.effectsListModel = new DefaultComboBoxModel();
/* 1058 */     this.effectsList = new JList();
/* 1059 */     this.effectsScroll.setViewportView(this.effectsList);
/* 1060 */     this.effectsList.setModel(this.effectsListModel);
/* 1061 */     this.effectsList.setVisibleRowCount(7);
/* 1062 */     this.effectsScroll.setMinimumSize(this.effectsList.getPreferredScrollableViewportSize());
/*      */ 
/*      */ 
/*      */     
/* 1066 */     this.addEffectButton = new JButton("Add");
/* 1067 */     this.effectsPanel.add(this.addEffectButton, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 5, 6, 5), 0, 0));
/*      */     
/* 1069 */     this.addEffectButton.setEnabled(false);
/*      */ 
/*      */     
/* 1072 */     this.appliedEffectsScroll = new JScrollPane();
/* 1073 */     this.effectsPanel.add(this.appliedEffectsScroll, new GridBagConstraints(1, 3, 1, 1, 1.0D, 1.0D, 11, 1, new Insets(0, 0, 5, 0), 0, 0));
/*      */     
/* 1075 */     this.appliedEffectsScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
/* 1076 */     this.appliedEffectsScroll.setHorizontalScrollBarPolicy(31);
/*      */     
/* 1078 */     JPanel panel = new JPanel();
/* 1079 */     panel.setLayout(new GridBagLayout());
/* 1080 */     this.appliedEffectsScroll.setViewportView(panel);
/*      */     
/* 1082 */     this.appliedEffectsPanel = new JPanel();
/* 1083 */     this.appliedEffectsPanel.setLayout(new GridBagLayout());
/* 1084 */     panel.add(this.appliedEffectsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 11, 2, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/* 1086 */     this.appliedEffectsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeMenus() {
/* 1095 */     JMenuBar menuBar = new JMenuBar();
/* 1096 */     setJMenuBar(menuBar);
/*      */     
/* 1098 */     JMenu fileMenu = new JMenu();
/* 1099 */     menuBar.add(fileMenu);
/* 1100 */     fileMenu.setText("File");
/* 1101 */     fileMenu.setMnemonic(70);
/*      */     
/* 1103 */     this.openMenuItem = new JMenuItem("Open Hiero settings file...");
/* 1104 */     this.openMenuItem.setMnemonic(79);
/* 1105 */     this.openMenuItem.setAccelerator(KeyStroke.getKeyStroke(79, 2));
/* 1106 */     fileMenu.add(this.openMenuItem);
/*      */ 
/*      */     
/* 1109 */     this.saveMenuItem = new JMenuItem("Save Hiero settings file...");
/* 1110 */     this.saveMenuItem.setMnemonic(83);
/* 1111 */     this.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(83, 2));
/* 1112 */     fileMenu.add(this.saveMenuItem);
/*      */     
/* 1114 */     fileMenu.addSeparator();
/*      */     
/* 1116 */     this.saveBMFontMenuItem = new JMenuItem("Save BMFont files (text)...");
/* 1117 */     this.saveBMFontMenuItem.setMnemonic(66);
/* 1118 */     this.saveBMFontMenuItem.setAccelerator(KeyStroke.getKeyStroke(66, 2));
/* 1119 */     fileMenu.add(this.saveBMFontMenuItem);
/*      */     
/* 1121 */     fileMenu.addSeparator();
/*      */     
/* 1123 */     this.exitMenuItem = new JMenuItem("Exit");
/* 1124 */     this.exitMenuItem.setMnemonic(88);
/* 1125 */     fileMenu.add(this.exitMenuItem);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Icon getColorIcon(Color color) {
/* 1132 */     BufferedImage image = new BufferedImage(32, 16, 1);
/* 1133 */     Graphics g = image.getGraphics();
/* 1134 */     g.setColor(color);
/* 1135 */     g.fillRect(1, 1, 30, 14);
/* 1136 */     g.setColor(Color.black);
/* 1137 */     g.drawRect(0, 0, 31, 15);
/* 1138 */     return new ImageIcon(image);
/*      */   }
/*      */   
/*      */   private class EffectPanel extends JPanel {
/* 1142 */     final Color selectedColor = new Color(11653865);
/*      */     
/*      */     final ConfigurableEffect effect;
/*      */     
/*      */     List values;
/*      */     
/*      */     JButton upButton;
/*      */     JButton downButton;
/*      */     JButton deleteButton;
/*      */     private JPanel valuesPanel;
/*      */     JLabel nameLabel;
/* 1153 */     GridBagConstraints constrains = new GridBagConstraints(0, -1, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0);
/*      */ 
/*      */     
/*      */     EffectPanel(ConfigurableEffect effect) {
/* 1157 */       this.effect = effect;
/* 1158 */       Hiero.this.effectPanels.add(this);
/* 1159 */       Hiero.this.effectsList.getListSelectionListeners()[0].valueChanged(null);
/*      */       
/* 1161 */       setLayout(new GridBagLayout());
/* 1162 */       setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
/* 1163 */       Hiero.this.appliedEffectsPanel.add(this, this.constrains);
/*      */       
/* 1165 */       JPanel titlePanel = new JPanel();
/* 1166 */       titlePanel.setLayout(new LayoutManager()
/*      */           {
/*      */             public void removeLayoutComponent(Component comp) {}
/*      */             
/*      */             public Dimension preferredLayoutSize(Container parent) {
/* 1171 */               return null;
/*      */             }
/*      */             
/*      */             public Dimension minimumLayoutSize(Container parent) {
/* 1175 */               return null;
/*      */             }
/*      */             
/*      */             public void layoutContainer(Container parent) {
/* 1179 */               Dimension buttonSize = Hiero.EffectPanel.this.upButton.getPreferredSize();
/* 1180 */               int upButtonX = Hiero.EffectPanel.this.getWidth() - buttonSize.width * 3 - 6 - 5;
/* 1181 */               Hiero.EffectPanel.this.upButton.setBounds(upButtonX, 0, buttonSize.width, buttonSize.height);
/* 1182 */               Hiero.EffectPanel.this.downButton.setBounds(Hiero.EffectPanel.this.getWidth() - buttonSize.width * 2 - 3 - 5, 0, buttonSize.width, buttonSize.height);
/* 1183 */               Hiero.EffectPanel.this.deleteButton.setBounds(Hiero.EffectPanel.this.getWidth() - buttonSize.width - 5, 0, buttonSize.width, buttonSize.height);
/*      */               
/* 1185 */               Dimension labelSize = Hiero.EffectPanel.this.nameLabel.getPreferredSize();
/* 1186 */               Hiero.EffectPanel.this.nameLabel.setBounds(5, buttonSize.height / 2 - labelSize.height / 2, Hiero.EffectPanel.this.getWidth() - 5, labelSize.height);
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*      */             public void addLayoutComponent(String name, Component comp) {}
/*      */           });
/* 1193 */       this.upButton = new JButton();
/* 1194 */       titlePanel.add(this.upButton);
/* 1195 */       this.upButton.setText("Up");
/* 1196 */       this.upButton.setMargin(new Insets(0, 0, 0, 0));
/* 1197 */       Font font = this.upButton.getFont();
/* 1198 */       this.upButton.setFont(new Font(font.getName(), font.getStyle(), font.getSize() - 2));
/*      */ 
/*      */       
/* 1201 */       this.downButton = new JButton();
/* 1202 */       titlePanel.add(this.downButton);
/* 1203 */       this.downButton.setText("Down");
/* 1204 */       this.downButton.setMargin(new Insets(0, 0, 0, 0));
/* 1205 */       font = this.downButton.getFont();
/* 1206 */       this.downButton.setFont(new Font(font.getName(), font.getStyle(), font.getSize() - 2));
/*      */ 
/*      */       
/* 1209 */       this.deleteButton = new JButton();
/* 1210 */       titlePanel.add(this.deleteButton);
/* 1211 */       this.deleteButton.setText("X");
/* 1212 */       this.deleteButton.setMargin(new Insets(0, 0, 0, 0));
/* 1213 */       font = this.deleteButton.getFont();
/* 1214 */       this.deleteButton.setFont(new Font(font.getName(), font.getStyle(), font.getSize() - 2));
/*      */ 
/*      */       
/* 1217 */       this.nameLabel = new JLabel(effect.toString());
/* 1218 */       titlePanel.add(this.nameLabel);
/* 1219 */       font = this.nameLabel.getFont();
/* 1220 */       this.nameLabel.setFont(new Font(font.getName(), 1, font.getSize()));
/*      */       
/* 1222 */       titlePanel.setPreferredSize(new Dimension(0, 
/* 1223 */             Math.max((this.nameLabel.getPreferredSize()).height, (this.deleteButton.getPreferredSize()).height)));
/* 1224 */       add(titlePanel, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 10, 1, new Insets(5, 0, 0, 5), 0, 0));
/*      */       
/* 1226 */       titlePanel.setOpaque(false);
/*      */ 
/*      */       
/* 1229 */       this.valuesPanel = new JPanel();
/* 1230 */       this.valuesPanel.setOpaque(false);
/* 1231 */       this.valuesPanel.setLayout(new GridBagLayout());
/* 1232 */       add(this.valuesPanel, new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 10, 5, 0), 0, 0));
/*      */ 
/*      */ 
/*      */       
/* 1236 */       this.upButton.addActionListener(new ActionListener() {
/*      */             public void actionPerformed(ActionEvent evt) {
/* 1238 */               int currentIndex = Hiero.this.effectPanels.indexOf(Hiero.EffectPanel.this);
/* 1239 */               if (currentIndex > 0) {
/* 1240 */                 Hiero.EffectPanel.this.moveEffect(currentIndex - 1);
/* 1241 */                 Hiero.this.updateFont();
/* 1242 */                 Hiero.EffectPanel.this.updateUpDownButtons();
/*      */               } 
/*      */             }
/*      */           });
/*      */       
/* 1247 */       this.downButton.addActionListener(new ActionListener() {
/*      */             public void actionPerformed(ActionEvent evt) {
/* 1249 */               int currentIndex = Hiero.this.effectPanels.indexOf(Hiero.EffectPanel.this);
/* 1250 */               if (currentIndex < Hiero.this.effectPanels.size() - 1) {
/* 1251 */                 Hiero.EffectPanel.this.moveEffect(currentIndex + 1);
/* 1252 */                 Hiero.this.updateFont();
/* 1253 */                 Hiero.EffectPanel.this.updateUpDownButtons();
/*      */               } 
/*      */             }
/*      */           });
/*      */       
/* 1258 */       this.deleteButton.addActionListener(new ActionListener() {
/*      */             public void actionPerformed(ActionEvent evt) {
/* 1260 */               Hiero.EffectPanel.this.remove();
/* 1261 */               Hiero.this.updateFont();
/* 1262 */               Hiero.EffectPanel.this.updateUpDownButtons();
/*      */             }
/*      */           });
/*      */       
/* 1266 */       updateValues();
/* 1267 */       Hiero.this.updateFont();
/* 1268 */       updateUpDownButtons();
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1272 */       Hiero.this.effectPanels.remove(this);
/* 1273 */       Hiero.this.appliedEffectsPanel.remove(this);
/* 1274 */       Hiero.this.getContentPane().validate();
/* 1275 */       Hiero.this.effectsList.getListSelectionListeners()[0].valueChanged(null);
/*      */     }
/*      */     
/*      */     public void updateValues() {
/* 1279 */       Hiero.this.prefs.put("foreground", EffectUtil.toString(Hiero.this.colorEffect.getColor()));
/* 1280 */       this.valuesPanel.removeAll();
/* 1281 */       this.values = this.effect.getValues();
/* 1282 */       for (Iterator<ConfigurableEffect.Value> iter = this.values.iterator(); iter.hasNext();)
/* 1283 */         addValue(iter.next()); 
/*      */     }
/*      */     
/*      */     public void updateUpDownButtons() {
/* 1287 */       for (int index = 0; index < Hiero.this.effectPanels.size(); index++) {
/* 1288 */         EffectPanel effectPanel = Hiero.this.effectPanels.get(index);
/* 1289 */         if (index == 0) {
/* 1290 */           effectPanel.upButton.setEnabled(false);
/*      */         } else {
/* 1292 */           effectPanel.upButton.setEnabled(true);
/*      */         } 
/*      */         
/* 1295 */         if (index == Hiero.this.effectPanels.size() - 1) {
/* 1296 */           effectPanel.downButton.setEnabled(false);
/*      */         } else {
/* 1298 */           effectPanel.downButton.setEnabled(true);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void moveEffect(int newIndex) {
/* 1304 */       Hiero.this.appliedEffectsPanel.remove(this);
/* 1305 */       Hiero.this.effectPanels.remove(this);
/* 1306 */       Hiero.this.appliedEffectsPanel.add(this, this.constrains, newIndex);
/* 1307 */       Hiero.this.effectPanels.add(newIndex, this);
/*      */     }
/*      */     
/*      */     public void addValue(final ConfigurableEffect.Value value) {
/* 1311 */       JLabel valueNameLabel = new JLabel(value.getName() + ":");
/* 1312 */       this.valuesPanel.add(valueNameLabel, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 5), 0, 0));
/*      */ 
/*      */       
/* 1315 */       final JLabel valueValueLabel = new JLabel();
/* 1316 */       this.valuesPanel.add(valueValueLabel, new GridBagConstraints(1, -1, 1, 1, 1.0D, 0.0D, 17, 1, new Insets(0, 0, 0, 5), 0, 0));
/*      */       
/* 1318 */       valueValueLabel.setOpaque(true);
/* 1319 */       if (value.getObject() instanceof Color) {
/* 1320 */         valueValueLabel.setIcon(Hiero.getColorIcon((Color)value.getObject()));
/*      */       } else {
/* 1322 */         valueValueLabel.setText(value.toString());
/*      */       } 
/* 1324 */       valueValueLabel.addMouseListener(new MouseAdapter() {
/*      */             public void mouseEntered(MouseEvent evt) {
/* 1326 */               valueValueLabel.setBackground(Hiero.EffectPanel.this.selectedColor);
/*      */             }
/*      */             
/*      */             public void mouseExited(MouseEvent evt) {
/* 1330 */               valueValueLabel.setBackground((Color)null);
/*      */             }
/*      */             
/*      */             public void mouseClicked(MouseEvent evt) {
/* 1334 */               Object oldObject = value.getObject();
/* 1335 */               value.showDialog();
/* 1336 */               if (!value.getObject().equals(oldObject)) {
/* 1337 */                 Hiero.EffectPanel.this.effect.setValues(Hiero.EffectPanel.this.values);
/* 1338 */                 Hiero.EffectPanel.this.updateValues();
/* 1339 */                 Hiero.this.updateFont();
/*      */               } 
/*      */             }
/*      */           });
/*      */     }
/*      */     
/*      */     public ConfigurableEffect getEffect() {
/* 1346 */       return this.effect;
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1350 */       if (this == obj) return true; 
/* 1351 */       if (obj == null) return false; 
/* 1352 */       if (getClass() != obj.getClass()) return false; 
/* 1353 */       EffectPanel other = (EffectPanel)obj;
/* 1354 */       if (this.effect == null)
/* 1355 */       { if (other.effect != null) return false;  }
/* 1356 */       else if (!this.effect.equals(other.effect)) { return false; }
/* 1357 */        return true;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class Splash
/*      */     extends JWindow {
/*      */     final int minMillis;
/*      */     final long startTime;
/*      */     
/*      */     public Splash(Frame frame, String imageFile, int minMillis) {
/* 1367 */       super(frame);
/* 1368 */       this.minMillis = minMillis;
/* 1369 */       getContentPane().add(new JLabel(new ImageIcon(Splash.class.getResource(imageFile))), "Center");
/* 1370 */       pack();
/* 1371 */       setLocationRelativeTo((Component)null);
/* 1372 */       setVisible(true);
/* 1373 */       this.startTime = System.currentTimeMillis();
/*      */     }
/*      */     
/*      */     public void close() {
/* 1377 */       final long endTime = System.currentTimeMillis();
/* 1378 */       (new Thread(new Runnable() {
/*      */             public void run() {
/* 1380 */               if (endTime - Hiero.Splash.this.startTime < Hiero.Splash.this.minMillis) {
/* 1381 */                 Hiero.Splash.this.addMouseListener(new MouseAdapter() {
/*      */                       public void mousePressed(MouseEvent evt) {
/* 1383 */                         Hiero.Splash.this.dispose();
/*      */                       }
/*      */                     },  );
/*      */                 try {
/* 1387 */                   Thread.sleep(Hiero.Splash.this.minMillis - endTime - Hiero.Splash.this.startTime);
/* 1388 */                 } catch (InterruptedException interruptedException) {}
/*      */               } 
/*      */               
/* 1391 */               EventQueue.invokeLater(new Runnable() {
/*      */                     public void run() {
/* 1393 */                       Hiero.Splash.this.dispose();
/*      */                     }
/*      */                   },  );
/*      */             }
/* 1397 */           },  "Splash")).start();
/*      */     } }
/*      */   
/*      */   class Renderer extends ApplicationAdapter {
/*      */     SpriteBatch batch;
/*      */     int width;
/*      */     int height;
/*      */     
/*      */     public void create() {
/* 1406 */       GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
/* 1407 */       GL11.glClearDepth(1.0D);
/* 1408 */       GL11.glDisable(2896);
/*      */       
/* 1410 */       this.batch = new SpriteBatch();
/*      */       
/* 1412 */       Hiero.this.sampleNeheButton.doClick();
/*      */     }
/*      */     
/*      */     public void resize(int width, int height) {
/* 1416 */       this.width = width;
/* 1417 */       this.height = height;
/* 1418 */       this.batch.getProjectionMatrix().setToOrtho2D(0.0F, 0.0F, width, height);
/*      */     }
/*      */     
/*      */     public void render() {
/* 1422 */       int viewWidth = Gdx.graphics.getWidth();
/* 1423 */       int viewHeight = Gdx.graphics.getHeight();
/*      */       
/* 1425 */       if (Hiero.this.sampleTextRadio.isSelected()) {
/* 1426 */         GL11.glClearColor(Hiero.this.renderingBackgroundColor.r, Hiero.this.renderingBackgroundColor.g, Hiero.this.renderingBackgroundColor.b, Hiero.this.renderingBackgroundColor.a);
/*      */         
/* 1428 */         GL11.glClear(16384);
/*      */       } else {
/* 1430 */         GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 1431 */         GL11.glClear(16384);
/*      */       } 
/*      */       
/* 1434 */       String sampleText = Hiero.this.sampleTextPane.getText();
/*      */       
/* 1436 */       GL11.glEnable(3553);
/* 1437 */       GL11.glEnableClientState(32888);
/* 1438 */       GL11.glEnableClientState(32884);
/*      */       
/* 1440 */       GL11.glEnable(3042);
/* 1441 */       GL11.glBlendFunc(770, 771);
/*      */       
/* 1443 */       GL11.glViewport(0, 0, this.width, this.height);
/* 1444 */       GL11.glScissor(0, 0, this.width, this.height);
/*      */       
/* 1446 */       GL11.glMatrixMode(5889);
/* 1447 */       GL11.glLoadIdentity();
/* 1448 */       GL11.glOrtho(0.0D, this.width, this.height, 0.0D, 1.0D, -1.0D);
/* 1449 */       GL11.glMatrixMode(5888);
/* 1450 */       GL11.glLoadIdentity();
/*      */       
/* 1452 */       Hiero.this.unicodeFont.addGlyphs(sampleText);
/*      */       
/* 1454 */       if (!Hiero.this.unicodeFont.getEffects().isEmpty() && Hiero.this.unicodeFont.loadGlyphs(64)) {
/* 1455 */         Hiero.this.glyphPageComboModel.removeAllElements();
/* 1456 */         int pageCount = Hiero.this.unicodeFont.getGlyphPages().size();
/* 1457 */         int glyphCount = 0;
/* 1458 */         for (int i = 0; i < pageCount; i++) {
/* 1459 */           Hiero.this.glyphPageComboModel.addElement("Page " + (i + 1));
/* 1460 */           glyphCount += ((GlyphPage)Hiero.this.unicodeFont.getGlyphPages().get(i)).getGlyphs().size();
/*      */         } 
/* 1462 */         Hiero.this.glyphPagesTotalLabel.setText(String.valueOf(pageCount));
/* 1463 */         Hiero.this.glyphsTotalLabel.setText(String.valueOf(glyphCount));
/*      */       } 
/*      */       
/* 1466 */       if (Hiero.this.sampleTextRadio.isSelected()) {
/* 1467 */         int offset = Hiero.this.unicodeFont.getYOffset(sampleText);
/* 1468 */         if (offset > 0) offset = 0; 
/* 1469 */         Hiero.this.unicodeFont.drawString(10.0F, 12.0F, sampleText, Color.WHITE, 0, sampleText.length());
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1474 */         int index = Hiero.this.glyphPageCombo.getSelectedIndex();
/* 1475 */         List<GlyphPage> pages = Hiero.this.unicodeFont.getGlyphPages();
/* 1476 */         if (index >= 0 && index < pages.size()) {
/* 1477 */           Texture texture = ((GlyphPage)pages.get(Hiero.this.glyphPageCombo.getSelectedIndex())).getTexture();
/*      */           
/* 1479 */           GL11.glDisable(3553);
/* 1480 */           GL11.glColor4f(Hiero.this.renderingBackgroundColor.r, Hiero.this.renderingBackgroundColor.g, Hiero.this.renderingBackgroundColor.b, Hiero.this.renderingBackgroundColor.a);
/*      */           
/* 1482 */           GL11.glBegin(7);
/* 1483 */           GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/* 1484 */           GL11.glVertex3f(0.0F, texture.getHeight(), 0.0F);
/* 1485 */           GL11.glVertex3f(texture.getWidth(), texture.getHeight(), 0.0F);
/* 1486 */           GL11.glVertex3f(texture.getWidth(), 0.0F, 0.0F);
/* 1487 */           GL11.glEnd();
/* 1488 */           GL11.glEnable(3553);
/*      */           
/* 1490 */           texture.bind();
/* 1491 */           GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 1492 */           GL11.glBegin(7);
/* 1493 */           GL11.glTexCoord2f(0.0F, 0.0F);
/* 1494 */           GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/*      */           
/* 1496 */           GL11.glTexCoord2f(0.0F, 1.0F);
/* 1497 */           GL11.glVertex3f(0.0F, texture.getHeight(), 0.0F);
/*      */           
/* 1499 */           GL11.glTexCoord2f(1.0F, 1.0F);
/* 1500 */           GL11.glVertex3f(texture.getWidth(), texture.getHeight(), 0.0F);
/*      */           
/* 1502 */           GL11.glTexCoord2f(1.0F, 0.0F);
/* 1503 */           GL11.glVertex3f(texture.getWidth(), 0.0F, 0.0F);
/* 1504 */           GL11.glEnd();
/*      */         } 
/*      */       } 
/*      */       
/* 1508 */       GL11.glDisable(3553);
/* 1509 */       GL11.glDisableClientState(32888);
/* 1510 */       GL11.glDisableClientState(32884);
/*      */       
/* 1512 */       if (Hiero.this.saveBmFontFile != null) {
/*      */         try {
/* 1514 */           BMFontUtil bmFont = new BMFontUtil(Hiero.this.unicodeFont);
/* 1515 */           bmFont.save(Hiero.this.saveBmFontFile);
/*      */           
/* 1517 */           if (Hiero.this.batchMode) {
/* 1518 */             Hiero.this.exit(0);
/*      */           }
/* 1520 */         } catch (Throwable ex) {
/* 1521 */           System.out.println("Error saving BMFont files: " + Hiero.this.saveBmFontFile.getAbsolutePath());
/* 1522 */           ex.printStackTrace();
/*      */         } finally {
/* 1524 */           Hiero.this.saveBmFontFile = null;
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
/*      */   static {
/* 1537 */     StringBuilder buffer = new StringBuilder();
/* 1538 */     int i = 0;
/* 1539 */     for (int c : new int[] { 0, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 884, 885, 890, 891, 892, 893, 894, 900, 901, 902, 903, 904, 905, 906, 908, 910, 911, 912, 913, 914, 915, 916, 917, 918, 919, 920, 921, 922, 923, 924, 925, 926, 927, 928, 929, 931, 932, 933, 934, 935, 936, 937, 938, 939, 940, 941, 942, 943, 944, 945, 946, 947, 948, 949, 950, 951, 952, 953, 954, 955, 956, 957, 958, 959, 960, 961, 962, 963, 964, 965, 966, 967, 968, 969, 970, 971, 972, 973, 974, 976, 977, 978, 979, 980, 981, 982, 983, 984, 985, 986, 987, 988, 989, 990, 991, 992, 993, 994, 995, 996, 997, 998, 999, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069, 1070, 1071, 1072, 1073, 1074, 1075, 1076, 1077, 1078, 1079, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087, 1088, 1089, 1090, 1091, 1092, 1093, 1094, 1095, 1096, 1097, 1098, 1099, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1113, 1114, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 1125, 1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133, 1134, 1135, 1136, 1137, 1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 1146, 1147, 1148, 1149, 1150, 1151, 1152, 1153, 1154, 1155, 1156, 1157, 1158, 1159, 1160, 1161, 1162, 1163, 1164, 1165, 1166, 1167, 1168, 1169, 1170, 1171, 1172, 1173, 1174, 1175, 1176, 1177, 1178, 1179, 1180, 1181, 1182, 1183, 1184, 1185, 1186, 1187, 1188, 1189, 1190, 1191, 1192, 1193, 1194, 1195, 1196, 1197, 1198, 1199, 1200, 1201, 1202, 1203, 1204, 1205, 1206, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, 1236, 1237, 1238, 1239, 1240, 1241, 1242, 1243, 1244, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1252, 1253, 1254, 1255, 1256, 1257, 1258, 1259, 1260, 1261, 1262, 1263, 1264, 1265, 1266, 1267, 1268, 1269, 1270, 1271, 1272, 1273, 1274, 1275, 1276, 1277, 1278, 1279, 1280, 1281, 1282, 1283, 1284, 1285, 1286, 1287, 1288, 1289, 1290, 1291, 1292, 1293, 1294, 1295, 1296, 1297, 1298, 1299, 1300, 1301, 1302, 1303, 1304, 1305, 1306, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 8192, 8193, 8194, 8195, 8196, 8197, 8198, 8199, 8200, 8201, 8202, 8203, 8204, 8205, 8206, 8207, 8210, 8211, 8212, 8213, 8214, 8215, 8216, 8217, 8218, 8219, 8220, 8221, 8222, 8223, 8224, 8225, 8226, 8230, 8234, 8235, 8236, 8237, 8238, 8239, 8240, 8242, 8243, 8244, 8249, 8250, 8252, 8254, 8260, 8286, 8298, 8299, 8300, 8301, 8302, 8303, 8352, 8353, 8354, 8355, 8356, 8357, 8358, 8359, 8360, 8361, 8363, 8364, 8365, 8366, 8367, 8368, 8369, 8370, 8371, 8372, 8373, 8377, 8378, 11360, 11361, 11362, 11363, 11364, 11365, 11366, 11367, 11368, 11369, 11370, 11371, 11372, 11373, 11377, 11378, 11379, 11380, 11381, 11382, 11383 }) {
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
/* 1578 */       i++;
/* 1579 */       if (i > 26) {
/* 1580 */         i = 0;
/* 1581 */         buffer.append("\r\n");
/*      */       } 
/* 1583 */       buffer.append((char)c);
/*      */     } 
/* 1585 */     EXTENDED_CHARS = buffer.toString();
/*      */   }
/*      */   
/*      */   public static void main(final String[] args) throws Exception {
/* 1589 */     SwingUtilities.invokeLater(new Runnable() {
/*      */           public void run() {
/* 1591 */             new Hiero(args);
/*      */           }
/*      */         });
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hiero\Hiero.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */