package mydocking;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class OpenTabsListCellRenderer extends DefaultListCellRenderer {

   private final int maxWidth;
   private final Tab activeTab;

   public OpenTabsListCellRenderer(int maxWidth, Tab activeTab) {
      this.maxWidth = maxWidth;
      this.activeTab = activeTab;
   }

   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      Tab tab = (Tab) value;
      JLabel lbl = (JLabel) super.getListCellRendererComponent(list, tab.getTitle() + (tab == activeTab ? " \u2190" : ""), index, isSelected, cellHasFocus);
      lbl.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
      Dimension size = lbl.getPreferredSize();
      if (size.width > maxWidth) {
         lbl.setPreferredSize(new Dimension(maxWidth, size.height));
      }
      return lbl;
   }
}
