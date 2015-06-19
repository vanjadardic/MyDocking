package mydocking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.Timer;

public abstract class ClickScrollHandler {

   private final int[] times;
   private final Timer timer;
   private int state;
   private int cnt;

   public ClickScrollHandler(JButton button, int... timesArg) {
      this.times = timesArg;
      timer = new Timer(0, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ClickScrollHandler.this.handle();
            if (times.length > state + 2) {
               cnt++;
               if (cnt == times[state + 1]) {
                  state += 2;
                  cnt = 0;
                  timer.setDelay(times[state]);
               }
            }
         }
      });
      button.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            state = cnt = 0;
            timer.setDelay(times[state]);
            timer.start();
         }

         @Override
         public void mouseReleased(MouseEvent evt) {
            timer.stop();
         }
      });
   }

   public abstract void handle();
}
