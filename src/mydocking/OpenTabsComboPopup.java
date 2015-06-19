package mydocking;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;

public class OpenTabsComboPopup extends BasicComboPopup {

   public OpenTabsComboPopup(JComboBox combo) {
      super(combo);
      Insets insets = getInsets();
      Dimension size = new Dimension(
            Math.min(248, getPreferredSize().width - (insets.left + insets.right)),
            getPopupHeightForRowCount(comboBox.getMaximumRowCount())
      );
      scroller.setMaximumSize(size);
      scroller.setPreferredSize(size);
      scroller.setMinimumSize(size);
      setPreferredSize(new Dimension(
            size.width + (insets.left + insets.right),
            size.height + (insets.top + insets.bottom)
      ));
   }
}
