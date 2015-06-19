package mydocking;

import java.util.EventListener;

public interface TabClosingListener extends EventListener {

   public boolean tabClosing(Tab tab);
}
