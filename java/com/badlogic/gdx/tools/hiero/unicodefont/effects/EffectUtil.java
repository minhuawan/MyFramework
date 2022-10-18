/*     */ package com.badlogic.gdx.tools.hiero.unicodefont.effects;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.SpinnerNumberModel;
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
/*     */ public class EffectUtil
/*     */ {
/*  51 */   private static BufferedImage scratchImage = new BufferedImage(256, 256, 2);
/*     */ 
/*     */ 
/*     */   
/*     */   public static BufferedImage getScratchImage() {
/*  56 */     Graphics2D g = (Graphics2D)scratchImage.getGraphics();
/*  57 */     g.setComposite(AlphaComposite.Clear);
/*  58 */     g.fillRect(0, 0, 256, 256);
/*  59 */     g.setComposite(AlphaComposite.SrcOver);
/*  60 */     g.setColor(Color.white);
/*  61 */     return scratchImage;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConfigurableEffect.Value colorValue(String name, Color currentValue) {
/*  66 */     return new DefaultValue(name, toString(currentValue)) {
/*     */         public void showDialog() {
/*  68 */           Color newColor = JColorChooser.showDialog(null, "Choose a color", EffectUtil.fromString(this.value));
/*  69 */           if (newColor != null) this.value = EffectUtil.toString(newColor); 
/*     */         }
/*     */         
/*     */         public Object getObject() {
/*  73 */           return EffectUtil.fromString(this.value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConfigurableEffect.Value intValue(String name, final int currentValue, final String description) {
/*  80 */     return new DefaultValue(name, String.valueOf(currentValue)) {
/*     */         public void showDialog() {
/*  82 */           JSpinner spinner = new JSpinner(new SpinnerNumberModel(currentValue, -32768, 32767, 1));
/*  83 */           if (showValueDialog(spinner, description)) this.value = String.valueOf(spinner.getValue()); 
/*     */         }
/*     */         
/*     */         public Object getObject() {
/*  87 */           return Integer.valueOf(this.value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConfigurableEffect.Value floatValue(String name, final float currentValue, final float min, final float max, final String description) {
/*  95 */     return new DefaultValue(name, String.valueOf(currentValue)) {
/*     */         public void showDialog() {
/*  97 */           JSpinner spinner = new JSpinner(new SpinnerNumberModel(currentValue, min, max, 0.10000000149011612D));
/*  98 */           if (showValueDialog(spinner, description)) this.value = String.valueOf(((Double)spinner.getValue()).floatValue()); 
/*     */         }
/*     */         
/*     */         public Object getObject() {
/* 102 */           return Float.valueOf(this.value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConfigurableEffect.Value booleanValue(String name, final boolean currentValue, final String description) {
/* 109 */     return new DefaultValue(name, String.valueOf(currentValue)) {
/*     */         public void showDialog() {
/* 111 */           JCheckBox checkBox = new JCheckBox();
/* 112 */           checkBox.setSelected(currentValue);
/* 113 */           if (showValueDialog(checkBox, description)) this.value = String.valueOf(checkBox.isSelected()); 
/*     */         }
/*     */         
/*     */         public Object getObject() {
/* 117 */           return Boolean.valueOf(this.value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConfigurableEffect.Value optionValue(String name, final String currentValue, final String[][] options, final String description) {
/* 126 */     return new DefaultValue(name, currentValue.toString()) {
/*     */         public void showDialog() {
/* 128 */           int selectedIndex = -1;
/* 129 */           DefaultComboBoxModel<String> model = new DefaultComboBoxModel();
/* 130 */           for (int i = 0; i < options.length; i++) {
/* 131 */             model.addElement(options[i][0]);
/* 132 */             if (getValue(i).equals(currentValue)) selectedIndex = i; 
/*     */           } 
/* 134 */           JComboBox<String> comboBox = new JComboBox<String>(model);
/* 135 */           comboBox.setSelectedIndex(selectedIndex);
/* 136 */           if (showValueDialog(comboBox, description)) this.value = getValue(comboBox.getSelectedIndex()); 
/*     */         }
/*     */         
/*     */         private String getValue(int i) {
/* 140 */           if ((options[i]).length == 1) return options[i][0]; 
/* 141 */           return options[i][1];
/*     */         }
/*     */         
/*     */         public String toString() {
/* 145 */           for (int i = 0; i < options.length; i++) {
/* 146 */             if (getValue(i).equals(this.value)) return options[i][0].toString(); 
/* 147 */           }  return "";
/*     */         }
/*     */         
/*     */         public Object getObject() {
/* 151 */           return this.value;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(Color color) {
/* 158 */     if (color == null) throw new IllegalArgumentException("color cannot be null."); 
/* 159 */     String r = Integer.toHexString(color.getRed());
/* 160 */     if (r.length() == 1) r = "0" + r; 
/* 161 */     String g = Integer.toHexString(color.getGreen());
/* 162 */     if (g.length() == 1) g = "0" + g; 
/* 163 */     String b = Integer.toHexString(color.getBlue());
/* 164 */     if (b.length() == 1) b = "0" + b; 
/* 165 */     return r + g + b;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Color fromString(String rgb) {
/* 170 */     if (rgb == null || rgb.length() != 6) return Color.white; 
/* 171 */     return new Color(Integer.parseInt(rgb.substring(0, 2), 16), Integer.parseInt(rgb.substring(2, 4), 16), Integer.parseInt(rgb
/* 172 */           .substring(4, 6), 16));
/*     */   }
/*     */   
/*     */   private static abstract class DefaultValue
/*     */     implements ConfigurableEffect.Value {
/*     */     String value;
/*     */     String name;
/*     */     
/*     */     public DefaultValue(String name, String value) {
/* 181 */       this.value = value;
/* 182 */       this.name = name;
/*     */     }
/*     */     
/*     */     public void setString(String value) {
/* 186 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String getString() {
/* 190 */       return this.value;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 194 */       return this.name;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 198 */       if (this.value == null) return ""; 
/* 199 */       return this.value.toString();
/*     */     }
/*     */     
/*     */     public boolean showValueDialog(final JComponent component, String description) {
/* 203 */       EffectUtil.ValueDialog dialog = new EffectUtil.ValueDialog(component, this.name, description);
/* 204 */       dialog.setTitle(this.name);
/* 205 */       dialog.setLocationRelativeTo(null);
/* 206 */       EventQueue.invokeLater(new Runnable() {
/*     */             public void run() {
/* 208 */               JComponent focusComponent = component;
/* 209 */               if (focusComponent instanceof JSpinner)
/* 210 */                 focusComponent = ((JSpinner.DefaultEditor)((JSpinner)component).getEditor()).getTextField(); 
/* 211 */               focusComponent.requestFocusInWindow();
/*     */             }
/*     */           });
/* 214 */       dialog.setVisible(true);
/* 215 */       return dialog.okPressed;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ValueDialog
/*     */     extends JDialog {
/*     */     public boolean okPressed = false;
/*     */     
/*     */     public ValueDialog(JComponent component, String name, String description) {
/* 224 */       setDefaultCloseOperation(2);
/* 225 */       setLayout(new GridBagLayout());
/* 226 */       setModal(true);
/*     */       
/* 228 */       if (component instanceof JSpinner) {
/* 229 */         ((JSpinner.DefaultEditor)((JSpinner)component).getEditor()).getTextField().setColumns(4);
/*     */       }
/* 231 */       JPanel descriptionPanel = new JPanel();
/* 232 */       descriptionPanel.setLayout(new GridBagLayout());
/* 233 */       getContentPane().add(descriptionPanel, new GridBagConstraints(0, 0, 2, 1, 1.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */       
/* 237 */       descriptionPanel.setBackground(Color.white);
/* 238 */       descriptionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
/*     */       
/* 240 */       JTextArea descriptionText = new JTextArea(description);
/* 241 */       descriptionPanel.add(descriptionText, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
/*     */       
/* 243 */       descriptionText.setWrapStyleWord(true);
/* 244 */       descriptionText.setLineWrap(true);
/* 245 */       descriptionText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/* 246 */       descriptionText.setEditable(false);
/*     */ 
/*     */       
/* 249 */       JPanel panel = new JPanel();
/* 250 */       getContentPane().add(panel, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 10, 0, new Insets(5, 5, 0, 5), 0, 0));
/*     */ 
/*     */ 
/*     */       
/* 254 */       panel.add(new JLabel(name + ":"));
/* 255 */       panel.add(component);
/*     */       
/* 257 */       JPanel buttonPanel = new JPanel();
/* 258 */       getContentPane().add(buttonPanel, new GridBagConstraints(0, 2, 2, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 263 */       JButton okButton = new JButton("OK");
/* 264 */       buttonPanel.add(okButton);
/* 265 */       okButton.addActionListener(new ActionListener() {
/*     */             public void actionPerformed(ActionEvent evt) {
/* 267 */               EffectUtil.ValueDialog.this.okPressed = true;
/* 268 */               EffectUtil.ValueDialog.this.setVisible(false);
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 273 */       JButton cancelButton = new JButton("Cancel");
/* 274 */       buttonPanel.add(cancelButton);
/* 275 */       cancelButton.addActionListener(new ActionListener() {
/*     */             public void actionPerformed(ActionEvent evt) {
/* 277 */               EffectUtil.ValueDialog.this.setVisible(false);
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 282 */       setSize(new Dimension(320, 175));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\EffectUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */