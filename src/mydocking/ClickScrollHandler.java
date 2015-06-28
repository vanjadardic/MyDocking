package mydocking;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public abstract class ClickScrollHandler extends ScrollHandler {

   public ClickScrollHandler(JButton button, int... timesArg) {
      super(timesArg);
      button.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            start();
         }

         @Override
         public void mouseReleased(MouseEvent evt) {
            end();
         }
      });
   }
}
