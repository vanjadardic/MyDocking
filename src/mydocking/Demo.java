package mydocking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Demo extends javax.swing.JFrame {

   public Demo() {
      initComponents();

      addTab();
      addTab();
      addTab();
      addTab();
      addTab();
      addTab();
      addTab();
      addTab();
      addTab();
      final Tab lastTab = addTab();

      setVisible(true);

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            tabContainer1.setActiveTab(lastTab);
         }
      });
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jPopupMenu1 = new javax.swing.JPopupMenu();
      jMenuItem1 = new javax.swing.JMenuItem();
      jMenuItem2 = new javax.swing.JMenuItem();
      jTextField1 = new javax.swing.JTextField();
      jButton1 = new javax.swing.JButton();
      tabContainer1 = new mydocking.TabContainer();
      jLabel1 = new javax.swing.JLabel();

      jMenuItem1.setText("jMenuItem1");
      jPopupMenu1.add(jMenuItem1);

      jMenuItem2.setText("jMenuItem2");
      jPopupMenu1.add(jMenuItem2);

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setTitle("MyLayout Demo");

      jTextField1.setText("jTextField1");

      jButton1.setText("Add tab");
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
         }
      });

      tabContainer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 102), 2));

      jLabel1.setText("Title");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(tabContainer1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(jLabel1)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jButton1)
                  .addGap(0, 324, Short.MAX_VALUE)))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton1)
               .addComponent(jLabel1))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(tabContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
            .addContainerGap())
      );

      setBounds(0, 0, 674, 440);
   }// </editor-fold>//GEN-END:initComponents

   private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      addTab();
   }//GEN-LAST:event_jButton1ActionPerformed

   private Tab addTab() {
      JPanel jj = new JPanel();
      jj.setBackground(new Color(0.5f + (float) Math.random() / 2, 0.5f + (float) Math.random() / 2, 0.5f + (float) Math.random() / 2));
      final Tab tab = tabContainer1.addNewTab(jTextField1.getText(), jj);
      tab.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
               jPopupMenu1.show(tab, e.getX(), e.getY());
            }
         }
      });
      return tab;
   }

   public static void main(String args[]) {
      Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
         @Override
         public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
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
            new Demo();
         }
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JMenuItem jMenuItem1;
   private javax.swing.JMenuItem jMenuItem2;
   private javax.swing.JPopupMenu jPopupMenu1;
   private javax.swing.JTextField jTextField1;
   private mydocking.TabContainer tabContainer1;
   // End of variables declaration//GEN-END:variables
}
