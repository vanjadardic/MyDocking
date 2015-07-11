package mydocking;

import java.awt.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TabColors {

   public static final TabColors PURPLE = new TabColors(
         new Color(0x2C, 0x06, 0x83), new Color(0x41, 0x0A, 0xBF), new Color(0xE8, 0xE8, 0xE8),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors RED = new TabColors(
         new Color(0xBF, 0x16, 0x00), new Color(0xFF, 0x1E, 0x00), new Color(0xE8, 0xE8, 0xE8),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors GREEN = new TabColors(
         new Color(0x00, 0x8F, 0x26), new Color(0x00, 0xCC, 0x36), new Color(0xE8, 0xE8, 0xE8),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors YELLOW = new TabColors(
         new Color(0xBF, 0xA3, 0x00), new Color(0xFF, 0xDA, 0x00), new Color(0xE8, 0xE8, 0xE8),
         new Color(0xFF, 0xFF, 0xFF), new Color(0x40, 0x40, 0x40), new Color(0x60, 0x60, 0x60));
   public static final TabColors BLUE = new TabColors(
         new Color(0x05, 0x3D, 0x7D), new Color(0x07, 0x5B, 0xB9), new Color(0xE8, 0xE8, 0xE8),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors PINK = new TabColors(
         new Color(0x89, 0x00, 0x6F), new Color(0xC6, 0x00, 0xA0), new Color(0xE8, 0xE8, 0xE8),
         new Color(0xFF, 0xFF, 0xFF), new Color(0xFF, 0xFF, 0xFF), new Color(0x60, 0x60, 0x60));
   public static final TabColors LIME = new TabColors(
         new Color(0x8D, 0xB6, 0x00), new Color(0xBF, 0xF6, 0x00), new Color(0xE8, 0xE8, 0xE8),
         new Color(0xFF, 0xFF, 0xFF), new Color(0x40, 0x40, 0x40), new Color(0x60, 0x60, 0x60));
   public static final TabColors ORANGE = new TabColors(
         new Color(0xBF, 0x73, 0x00), new Color(0xFF, 0x9A, 0x00), new Color(0xE8, 0xE8, 0xE8),
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

   public void save(Element root) {
      Document doc = root.getOwnerDocument();
      root.appendChild(doc.createTextNode(
            String.format("%08x", backgroundActive.getRGB())
            + String.format("%08x", backgroundHover.getRGB())
            + String.format("%08x", backgroundInactive.getRGB())
            + String.format("%08x", foregroundActive.getRGB())
            + String.format("%08x", foregroundHover.getRGB())
            + String.format("%08x", foregroundInactive.getRGB())
      ));
   }

   public static TabColors restore(Element root) {
      String data = root.getTextContent();
      return new TabColors(
            parseColor(data.substring(0, 8)),
            parseColor(data.substring(8, 16)),
            parseColor(data.substring(16, 24)),
            parseColor(data.substring(24, 32)),
            parseColor(data.substring(32, 40)),
            parseColor(data.substring(40, 48))
      );
   }

   private static Color parseColor(String data) {
      return new Color(
            Integer.parseInt(data.substring(2, 4), 16),
            Integer.parseInt(data.substring(4, 6), 16),
            Integer.parseInt(data.substring(6, 8), 16),
            Integer.parseInt(data.substring(0, 2), 16)
      );
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
