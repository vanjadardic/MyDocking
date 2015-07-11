package mydocking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Demo extends JFrame {

   private static boolean errorDialogShown = false;
   private static int cnt = 0;
   private TabManager tabManager;
   private Preferences pref;

   public Demo() {
      pref = Preferences.userNodeForPackage(Demo.class);
      //pref.remove("mylayoutdemo");

      setTitle("MyLayout Demo");
      getContentPane().setLayout(new BorderLayout());

      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            try {
               String layout = tabManager.save();
               pref.put("demolayout", layout);
               pref.flush();
            } catch (Exception ex) {
               throw new RuntimeException(ex);
            }
         }
      });

      JButton addTabButton = new JButton("Add new tab");
      addTabButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            createTab();
         }
      });
      getContentPane().add(addTabButton, BorderLayout.NORTH);

      String layout = pref.get("demolayout", null);
      if (layout == null) {
         tabManager = new mydocking.TabManager();
         tabManager.setTabCustomDataHandler(new DemoTabCustomDataHandler());
         Tab first = createTab();
         createTab();
         createTab();
         createTab();
         createTab();
         createTab();
         createTab();
         createTab();
         tabManager.setActiveTab(first);
      } else {
         try {
            tabManager = TabManager.restore(layout, new DemoTabCustomDataHandler());
         } catch (Exception ex) {
            throw new RuntimeException(ex);
         }
      }
      getContentPane().add(tabManager, BorderLayout.CENTER);

      setBounds(0, 0, 1000, 600);

   }

   private Tab createTab() {
      TabColors[] c = new TabColors[]{TabColors.PURPLE, TabColors.RED, TabColors.GREEN, TabColors.YELLOW,
         TabColors.BLUE, TabColors.PINK, TabColors.LIME, TabColors.ORANGE};
      JPanel panel = new JPanel();
      panel.setBackground(new Color(
            0.5f + (float) Math.random() / 2,
            0.5f + (float) Math.random() / 2,
            0.5f + (float) Math.random() / 2
      ));
      return tabManager.addNewTab("New Tab #" + ++cnt, panel, c[(int) (Math.random() * c.length)]);
   }

   public static void main(String args[]) {
      Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
         @Override
         public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
            if (errorDialogShown) {
               return;
            }
            errorDialogShown = true;

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
            errorDialogShown = false;
            if (option == 1) {
               System.exit(1);
            }
         }
      });

      try {
         for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Windows".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (Exception ex) {
         throw new RuntimeException(ex);
      }

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new Demo().setVisible(true);
         }
      });
   }
}
