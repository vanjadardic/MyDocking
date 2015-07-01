package mydocking;

import java.awt.Color;

public class TabColors {

   public static final TabColors PURPLE = new TabColors(
         new Color(0x2C, 0x06, 0x83), new Color(0x41, 0x0A, 0xBF), new Color(0xE0, 0xE0, 0xE0),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors RED = new TabColors(
         new Color(0xBF, 0x16, 0x00), new Color(0xFF, 0x1E, 0x00), new Color(0xE0, 0xE0, 0xE0),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors GREEN = new TabColors(
         new Color(0x00, 0x8F, 0x26), new Color(0x00, 0xCC, 0x36), new Color(0xE0, 0xE0, 0xE0),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors YELLOW = new TabColors(
         new Color(0xBF, 0xA3, 0x00), new Color(0xFF, 0xDA, 0x00), new Color(0xE0, 0xE0, 0xE0),
         new Color(0xFF, 0xFF, 0xFF), new Color(0x40, 0x40, 0x40), new Color(0x60, 0x60, 0x60));
   public static final TabColors BLUE = new TabColors(
         new Color(0x05, 0x3D, 0x7D), new Color(0x07, 0x5B, 0xB9), new Color(0xE0, 0xE0, 0xE0),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors PINK = new TabColors(
         new Color(0x89, 0x00, 0x6F), new Color(0xC6, 0x00, 0xA0), new Color(0xE0, 0xE0, 0xE0),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors LIME = new TabColors(
         new Color(0x8D, 0xB6, 0x00), new Color(0xBF, 0xF6, 0x00), new Color(0xE0, 0xE0, 0xE0),
         new Color(0xFF, 0xFF, 0xFF), new Color(0x40, 0x40, 0x40), new Color(0x60, 0x60, 0x60));
   public static final TabColors ORANGE = new TabColors(
         new Color(0xBF, 0x73, 0x00), new Color(0xFF, 0x9A, 0x00), new Color(0xE0, 0xE0, 0xE0),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));

   private final Color backgroundActive;
   private final Color backgroundHover;
   private final Color backgroundInactive;
   private final Color foregroundActive;
   private final Color foregroundHover;
   private final Color foregroundInactive;

   public TabColors(Color backgroundActive, Color backgroundHover, Color backgroundInactive, Color foregroundActive, Color foregroundHover, Color foregroundInactive) {
      this.backgroundActive = backgroundActive;
      this.backgroundHover = backgroundHover;
      this.backgroundInactive = backgroundInactive;
      this.foregroundActive = foregroundActive;
      this.foregroundHover = foregroundHover;
      this.foregroundInactive = foregroundInactive;
   }

   public Color getBackgroundActive() {
      return backgroundActive;
   }

   public Color getBackgroundHover() {
      return backgroundHover;
   }

   public Color getBackgroundInactive() {
      return backgroundInactive;
   }

   public Color getForegroundActive() {
      return foregroundActive;
   }

   public Color getForegroundHover() {
      return foregroundHover;
   }

   public Color getForegroundInactive() {
      return foregroundInactive;
   }
}
