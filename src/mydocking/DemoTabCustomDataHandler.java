package mydocking;

import java.awt.Color;
import javax.swing.JPanel;

public class DemoTabCustomDataHandler implements TabCustomDataHandler {

   @Override
   public byte[] saveCustomData(Tab tab) {
      JPanel panel = (JPanel) tab.getComponent();
      return String.format("%08x", panel.getBackground().getRGB()).getBytes();
   }

   @Override
   public void restoreCustomData(Tab tab, byte[] data) {
      JPanel panel = new JPanel();
      panel.setBackground(parseColor(new String(data)));
      tab.setComponent(panel);
   }

   private Color parseColor(String data) {
      return new Color(
            Integer.parseInt(data.substring(2, 4), 16),
            Integer.parseInt(data.substring(4, 6), 16),
            Integer.parseInt(data.substring(6, 8), 16),
            Integer.parseInt(data.substring(0, 2), 16)
      );
   }
}
