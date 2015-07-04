package mydocking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public abstract class ScrollHandler {

   private final int[] times;
   private final Timer timer;
   private int state;
   private int cnt;

   public ScrollHandler(int... timesArg) {
      this.times = timesArg;
      timer = new Timer(0, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            handle();
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
   }

   public void start() {
      state = cnt = 0;
      timer.setDelay(times[state]);
      timer.start();
   }

   public void end() {
      timer.stop();
   }

   public abstract void handle();
}
