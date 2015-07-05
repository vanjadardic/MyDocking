package mydocking;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboPopup;

public class OpenTabsComboPopup extends BasicComboPopup {

   private final int maxWidth;

   public OpenTabsComboPopup(Component[] components, Tab activeTab, int maxWidth) {
      super(new JComboBox(components));
      this.maxWidth = maxWidth;

      comboBox.setMaximumRowCount(Integer.MAX_VALUE);
      comboBox.setRenderer(new OpenTabsListCellRenderer(maxWidth, activeTab, getInsets()));
      list.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseExited(MouseEvent e) {
            list.clearSelection();
         }
      });
      list.clearSelection();
      scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      resize();
   }

   public void resize() {
      Insets insets = getInsets();
      Dimension size = new Dimension(
            Math.min(maxWidth, getPreferredSize().width) - (insets.left + insets.right),
            getPopupHeightForRowCount(comboBox.getMaximumRowCount())
      );
      scroller.setMaximumSize(size);
      scroller.setPreferredSize(size);
      scroller.setMinimumSize(size);
      setPreferredSize(new Dimension(
            size.width + (insets.left + insets.right),
            size.height + (insets.top + insets.bottom)
      ));
      Window w = SwingUtilities.getWindowAncestor(this);
      if (w != null) {
         w.setSize(size.width + insets.left + insets.right, size.height + insets.top + insets.bottom);
      }
   }
}
