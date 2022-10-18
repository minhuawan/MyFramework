/*    */ package com.megacrit.cardcrawl.helpers;
/*    */ 
/*    */ public class BadWordChecker
/*    */ {
/*  5 */   private static final String[] BAD_WORDS = new String[] { "2G1C", "2GIRLS1CUP", "4R5E", "5H1T", "5HIT", "A54", "A55", "ACR0T0M0PHILIA", "ALASKANPIPELINE", "ANAL", "ANILINGUS", "ANUS", "AR5E", "ARRSE", "ARSE", "ASS", "AXW0UND", "B!TCH", "B00BS", "B17CH", "B1TCH", "BABELAND", "BABYBATTER", "BABYJUICE", "BALLBAG", "BALLGAG", "BALLGRAVY", "BALLKICKING", "BALLLICKING", "BALLS", "BAMP0T", "BANGBR0S", "BANG0NESB0X", "BAREBACK", "BARELYLEGAL", "BARENAKED", "BASTINAD0", "BATTYB0Y", "BBW", "BDSM", "BEANER", "BEASTIAL", "BEAVER", "BEEFCURTAIN", "BENDER", "BESTIAL", "BI+CH", "BIATCH", "BIGBLACK", "BIGKN0CKERS", "BIMB0S", "BINT", "BIRDL0CK", "BITCH", "BL0NDEACTI0N", "BL00DCLAAT", "BL00DY", "BL0WJ0B", "BL0WME", "BL0WMUD", "BL0WY0URL0AD", "BLUEWAFFLE", "BLUMPKIN", "B0I0LAS", "B0LL0CK", "B0LL0K", "B0LL0X", "B0NDAGE", "B0NER", "B00B", "B00NG", "B000BS", "B0000BS", "B00000BS", "B0000000BS", "B00TYCALL", "BREASTS", "BREEDER", "BRUNETTEACTI0N", "BUCETA", "BUGGER", "BUKKAKE", "BULLETVIBE", "BUM", "BUNC0MBE", "BUSTAL0AD", "BUSTY", "BUTT", "C0CK", "CACAFUEG0", "CAMELT0E", "CAMGIRL", "CARPETMUNCHER", "CAWK", "CHESTICLE", "CHICHIMAN", "CHINC", "CHINK", "CHRIST0NABIKE", "CIPA", "CL1T", "CLEVELANDSTEAMER", "CLIT", "CL0VERCLAMPS", "CLUNGE", "CNUT", "C0CCYDYNIA", "C0CK", "C0FFIND0DGER", "C0K", "C00CHIE", "C00CHY", "C00N", "C00TER", "C0PR0LAGNIA", "C0PR0PHILIA", "C0PS0MEW00D", "C0X", "CRACKER", "CRAP", "CREAMPIE", "CRETIN", "CRIKEY", "CRIPPLE", "CR0TTE", "CUCK", "CUM", "CUNILINGUS", "CUNILLINGUS", "CUNNIE", "CUNNILINGUS", "CUNT", "CUS", "CUTR0PE", "CYALIS", "D1CK", "DAG0", "DAMN", "DARKIE", "DARN", "DEEPTHR0AT", "DEGG0", "DENDR0PHILIA", "DICK", "DIKE", "DILD0", "DINGLEBERRIES", "DINGLEBERRY", "DINK", "DIRSA", "DIRTYPILL0WS", "DIRTYSANCHEZ", "DIV", "DLCK", "D0GGIESTYLE", "D0GGIN", "D0GGYSTYLE", "D0GSTYLE", "D0LCETT", "D0MINATI0N", "D0MINATRIX", "D0MMES", "D0NKEYPUNCH", "D0NKEYRIBBER", "D00CHBAG", "D00KIE", "D00SH", "D0UBLED0NG", "D0UBLELIFT", "D0UBLEPENETRATI0N", "D0UCHE", "DPACTI0N", "DRYHUMP", "DUCHE", "DVDA", "DYKE", "EATHAIRPIE", "ECCHI", "EJACULATE", "EJACULATING", "EJACULATI0N", "EJAKULATE", "ER0TIC", "ER0TISM", "ESC0RT", "EUNUCH", "F4NNY", "FACIAL", "FAG", "FANNY", "FANYY", "FCUK", "FECAL", "FECK", "FEIST", "FELCH", "FELLATE", "FELLATI0", "FELTCH", "FEMD0M", "FENIAN", "FICE", "FIGGING", "FINGERBANG", "FINGERING", "FISTING", "FLAMER", "FLANGE", "FLAPS", "FLESHFLUTE", "FL0GTHEL0G", "F0AH", "F00K", "F00TFETISH", "F00TJ0B", "FRENCHIFY", "FR0TTING", "FUC", "FUDGEPACKER", "FUK", "FUQ", "FUTANARI", "FUX", "GANGBANG", "GASH", "GAY", "GENITALS", "GINGER", "GIPP0", "GIRL0N", "GIRLSG0NEWILD", "GIT", "G0ATCX", "G0ATSE", "G0D", "G0KKUN", "G00CH", "G00GIRL", "G00K", "G0REGASM", "GRING0", "GR0PE", "GSP0T", "GUID0", "GUR0", "HAMFLAP", "HANDJ0B", "HARDC0RE", "HARD0N", "HEEB", "HELL", "HENTAI", "HESHE", "HIRCISMUS", "H0", "HUGEFAT", "HUMPING", "HUN", "IAP", "IBERIANSLAP", "INCEST", "INTERC0URSE", "JACK0FF", "JAGGI", "JAG0FF", "JAILBAIT", "JAP", "JELLYD0NUT", "JERK", "JESUS", "JIGAB00", "JIGGAB00", "JIGGERB00", "JISM", "JIZ", "J0CK", "JUDASPRIEST", "JUGGS", "JUNGLEBUNNY", "KAFIR", "KAWK", "KIKE", "KINBAKU", "KINKSTER", "KINKY", "K0CK", "K0NDUM", "K00CH", "K00TCH", "KRAUT", "KUM", "KUNILINGUS", "KUNJA", "KUNT", "KWIF", "KYKE", "L3I+CH", "L3ITCH", "LABIA", "LADYB0Y", "LEATHERRESTRAINT", "LEATHERSTRAIGHTJACKET", "LEM0NPARTY", "LEN", "LESBIAN", "LESB0", "LEZZIE", "LMA0", "LMFA0", "L0LITA", "L00NEY", "L0VEMAKING", "LUST", "M0F0", "M0F0", "M45TERBATE", "MA5TERB8", "MA5TERBATE", "MAFUGLY", "MAKEMEC0ME", "MALESQUIRTING", "MAS0CHIST", "MASTERB8", "MASTERBAT*", "MASTERBAT3", "MASTERBATE", "MASTERBATI0N", "MASTURBATE", "MENAGEATR0IS", "MICK", "MICR0PHALLUS", "MIDDLEFINGER", "MIDGET", "MILF", "MINGE", "MISSI0NARYP0SITI0N", "M0F0", "M0F0", "M0NG", "M00M00F00F00", "M0UND0FVENUS", "MRHANDS", "MUFF", "MUNGING", "MUNTER", "MUTHA", "MUTHER", "N1GGA", "N1GGER", "NAMBLA", "NAWASHI", "NAZI", "NEGR0", "NIGAB00", "NIGG3R", "NIGG4H", "NIGGA", "NIGGER", "NIGLET", "NIGN0G", "NINNYHAMMER", "NIPPLE", "N0B", "N0NCE", "NSFWIMAGES", "NUDE", "NUDITY", "NUMBNUTS", "NUTSACK", "NUTTER", "0LDBAG", "0MG", "0M0RASHI", "0NEGUY0NEJAR", "0RGASIM", "0RGASM", "0RGY", "P0RN", "PAED0PHILE", "PAKI", "PAN00CH", "PANSY", "PANTIES", "PANTY", "PAWN", "PECKER", "PED0BEAR", "PED0PHILE", "PEGGING", "PENIS", "PHUCK", "PHUK", "PHUQ", "PIKEY", "PIMPIS", "PISS", "PLAYB0Y", "PLEASURECHEST", "P0LACK", "P0LESM0KER", "P0LL0CK", "P0NYPLAY", "P00F", "P00N", "P00P", "P0RCHM0NKEY", "P0RN", "PRICK", "PRINCEALBERTPIERCING", "PR0D", "PR0N", "PTHC", "PUBE", "PUNANI", "PUNANNY", "PUNANY", "PUNTA", "PUSSE", "PUSSI", "PUSSY", "PUST", "PUT0", "QUEAF", "QUEEF", "QUEER", "QUIM", "RAGHEAD", "RAPE", "RAPING", "RAPIST", "RECTUM", "REVERSEC0WGIRL", "RIMJAW", "RIMJ0B", "RIMMING", "R0SYPALM", "RUBBISH", "RUSKI", "RUSTYTR0MB0NE", "S&M", "S.0.B.", "SADISM", "SADIST", "SAMB0", "SANDBAR", "SANDLER", "SANGER", "SANT0RUM", "SAUSAGEQUEEN", "SCAT", "SCHIZ0", "SCHL0NG", "SCISS0RING", "SCREWING", "SCR0AT", "SCR0TE", "SCR0TUM", "SEKS", "SEMEN", "SEX", "SH!+", "SH!T", "SH1T", "SHAG", "SHEMALE", "SHI+", "SHIBARI", "SHIRTLIFTER", "SHIT", "SHIZ", "SHRIMPING", "SKANK", "SKEET", "SLAG", "SLANTEYE", "SL0PE", "SLUT", "SMEG", "SMUT", "SNATCH", "SN0WBALLING", "S0D0FF", "S0D0MIZE", "S0D0MY", "S0N0FAM0THERLESSG0AT", "SPAC", "SPADE", "SPIC", "SPL00GE", "SP00GE", "SP00K", "SPREADLEGS", "SPUNK", "STRAP0N", "STRAPPAD0", "STRIPCLUB", "STYLED0GGY", "SUCK", "SUICIDEGIRLS", "SULTRYW0MEN", "SWASTIKA", "SWEARW0RD", "SWINGER", "T1TT1E5", "T1TTIES", "TAFF", "TAIG", "TAINTEDL0VE", "TARD", "TART", "TASTEMY", "TEABAGGING", "TEETS", "TEEZ", "TESTICAL", "TESTICLE", "THREES0ME", "THR0ATING", "TIEDUP", "TIGHTWHITE", "TIT", "T0NGUEINA", "T0PLESS", "T0SSER", "T0WELHEAD", "TRANNY", "TRIBADISM", "TRUMPED", "TUBGIRL", "TURD", "TUSHY", "TW4T", "TWAT", "TWINK", "TW0FINGERS", "TWUNT", "UNDRESSING", "UPSKIRT", "URETHRAPLAY", "UR0PHILIA", "V14GRA", "V1GRA", "VAG", "VAJAYJAY", "VAJJ", "VENUSM0UND", "VEQTABLE", "VIAGRA", "VIBRAT0R", "VI0LETWAND", "VJAYJAY", "V0RAREPHILIA", "V0YEUR", "VULVA", "W00SE", "WANG", "WANK", "WETBACK", "WETDREAM", "WHITEP0WER", "WILLIES", "WILLY", "WIND0WLICKER", "W0G", "W0P", "WRAPPINGMEN", "WRINKLEDSTARFISH", "WTF", "XRATED", "XX", "YA0I", "YID", "YIFFY", "ZIBBI", "Z00PHILIA", "ZUBB" };
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
/*    */   
/*    */   public static boolean containsBadWord(String text) {
/* 68 */     String inputLower = text.toLowerCase();
/* 69 */     for (String badWord : BAD_WORDS) {
/* 70 */       if (inputLower.contains(badWord.toLowerCase())) {
/* 71 */         return true;
/*    */       }
/*    */     } 
/* 74 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\helpers\BadWordChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */