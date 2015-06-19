package mydocking;

import java.util.EventListener;

public interface TabClosedListener extends EventListener {

   public boolean tabClosed(Tab tab);
}
