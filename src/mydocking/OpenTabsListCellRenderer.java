package mydocking;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class OpenTabsListCellRenderer extends DefaultListCellRenderer {

   private final int maxWidth;
   private final Tab activeTab;
   private final Insets insets;

   public OpenTabsListCellRenderer(int maxWidth, Tab activeTab, Insets insets) {
      this.maxWidth = maxWidth;
      this.activeTab = activeTab;
      this.insets = insets;
   }

   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      Tab tab = (Tab) value;
      JLabel lbl = (JLabel) super.getListCellRendererComponent(list, tab.getTitle() + (tab == activeTab ? " \u2190" : ""), index, isSelected, cellHasFocus);
      lbl.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
      Dimension size = lbl.getPreferredSize();
      if (size.width > maxWidth) {
         lbl.setPreferredSize(new Dimension(maxWidth - (insets.left + insets.right), size.height));
      }
      return lbl;
   }
}
