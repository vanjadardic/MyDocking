package mydocking;

import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Demo extends javax.swing.JFrame {

   private static boolean exceptionDialogShown = false;

   public Demo() {
      initComponents();
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      tabManager1 = new mydocking.TabManager();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setTitle("MyLayout Demo");
      getContentPane().setLayout(new java.awt.GridLayout(1, 1));
      getContentPane().add(tabManager1);

      setBounds(0, 0, 1000, 600);
   }// </editor-fold>//GEN-END:initComponents

   public static void main(String args[]) {
      Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
         @Override
         public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
            if (exceptionDialogShown) {
               return;
            }
            exceptionDialogShown = true;

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setPreferredSize(new Dimension(800, 300));
            JTextArea textArea = new JTextArea(sw.toString());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.getViewport().setView(textArea);

            int option = JOptionPane.showOptionDialog(
                  null,
                  scrollPane,
                  "Unhandled exception has been thrown!",
                  JOptionPane.DEFAULT_OPTION,
                  JOptionPane.ERROR_MESSAGE,
                  null,
                  new Object[]{"Dismiss", "Close application"},
                  null
            );
            exceptionDialogShown = false;
            if (option == 1) {
               System.exit(1);
            }
         }
      });

      try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Windows".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (Exception ex) {
      }

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new Demo().setVisible(true);
         }
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private mydocking.TabManager tabManager1;
   // End of variables declaration//GEN-END:variables
}
