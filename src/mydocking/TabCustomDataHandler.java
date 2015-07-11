package mydocking;

public interface TabCustomDataHandler {

   public byte[] saveCustomData(Tab tab);

   public void restoreCustomData(Tab tab, byte[] data);
}
