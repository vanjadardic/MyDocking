package mydocking;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class TabManager extends JPanel {

   private static int cnt = 0;
   private Point dropPoint = null;

   public TabManager() {
      setLayout(new GridLayout(1, 1));

      TabContainer tc = new TabContainer();
      addTab(tc);
      addTab(tc);
      addTab(tc);
      addTab(tc);
      addTab(tc);
      addTab(tc);
      addTab(tc);
      addTab(tc);

      add(tc);

      DropTarget dropTarget = new DropTarget(this, DnDConstants.ACTION_MOVE, new DropTargetListener() {
         @Override
         public void dragEnter(DropTargetDragEvent dtde) {
            if (!dtde.isDataFlavorSupported(Tab.DATA_FLAVOR) || dtde.getDropAction() != DnDConstants.ACTION_MOVE) {
               dtde.rejectDrag();
               return;
            }
            TabManager.this.setDropPoint(dtde.getLocation());
         }

         @Override
         public void dragOver(DropTargetDragEvent dtde) {
            if (!dtde.isDataFlavorSupported(Tab.DATA_FLAVOR) || dtde.getDropAction() != DnDConstants.ACTION_MOVE) {
               TabManager.this.setDropPoint(null);
            } else {
               TabManager.this.setDropPoint(dtde.getLocation());
            }
         }

         @Override
         public void dropActionChanged(DropTargetDragEvent dtde) {
            if (!dtde.isDataFlavorSupported(Tab.DATA_FLAVOR) || dtde.getDropAction() != DnDConstants.ACTION_MOVE) {
               dtde.rejectDrag();
            }

         }

         @Override
         public void dragExit(DropTargetEvent dte) {
            TabManager.this.setDropPoint(null);

         }

         @Override
         public void drop(DropTargetDropEvent dtde) {
            try {
               Tab tab = TabContainer.getTab((String) dtde.getTransferable().getTransferData(Tab.DATA_FLAVOR));
               //System.out.println(tab);
               TabContainer tcSrc = tab.getTabContainer();
               if (tcSrc == null) {
                  return;
               }
               TabContainer tcDest = getTabContainer(dtde.getLocation());
               if (tcDest == null) {
                  return;
               }
               Dimension size = tcSrc.getSize();
               Point dropPointTmp = SwingUtilities.convertPoint(TabManager.this, dropPoint, tcSrc);
               if (dropPointTmp.y <= size.height * 0.2f) {
                  System.out.println("top");
               } else if (dropPointTmp.y >= size.height * 0.8f) {
                  System.out.println("bottom");
               } else if (dropPointTmp.x <= size.width * 0.2f) {
                  System.out.println("left");
                  int width = tcDest.getWidth();
                  tcSrc.removeTab(tab);
                  Container container = tcDest.getParent();
                  container.remove(tcDest);

                  TabContainer newTc = new TabContainer();
                  newTc.addTab(tab);

                  //TODO handle slight resize
                  //TODO haddle removing last tab

                  JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, newTc, tcDest);
                  split.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                  split.setDividerSize(4);
                  split.setResizeWeight(0.5);
                  split.setDividerLocation(width / 2 - 2);
                  split.setUI(new BasicSplitPaneUI() {
                     @Override
                     public BasicSplitPaneDivider createDefaultDivider() {
                        return new BasicSplitPaneDivider(this) {
                           @Override
                           public void setBorder(Border b) {
                           }
                        };
                     }
                  });
                  container.add(split);
               } else if (dropPointTmp.x >= size.width * 0.8f) {
                  System.out.println("right");
               }
            } catch (UnsupportedFlavorException | IOException ex) {
               throw new RuntimeException(ex);
            }
            TabManager.this.setDropPoint(null);
         }
      }, true, null);

   }

   private Tab addTab(TabContainer tc) {
      JPanel jp = new JPanel();
      jp.setBackground(new Color(0.5f + (float) Math.random() / 2, 0.5f + (float) Math.random() / 2, 0.5f + (float) Math.random() / 2));
      return tc.addNewTab("New Tab #" + ++cnt, jp);
   }

   public Point getDropPoint() {
      return dropPoint;
   }

   public void setDropPoint(Point dropPoint) {
      if (dropPoint == null || !dropPoint.equals(getDropPoint())) {
         this.dropPoint = dropPoint;
         repaint();
      }
   }

   @Override
   public void paint(Graphics g) {
      super.paint(g);
      if (dropPoint != null) {
         Graphics2D g2 = (Graphics2D) g;
         g2.setStroke(new BasicStroke(3));
         g2.setColor(new Color(0xff, 0x60, 0x00, 0xa0));

         TabContainer tc = getTabContainer(dropPoint);
         //System.out.println(c);

         if (tc != null) {
            Dimension size = tc.getSize();
            Insets insets = tc.getInsets();
            insets.left += 1;
            insets.top += 1;
            insets.right += 2;
            insets.bottom += 2;
            Point offset = SwingUtilities.convertPoint(tc, new Point(0, 0), this);
            Point dropPointTmp = SwingUtilities.convertPoint(this, dropPoint, tc);
            g2.translate(offset.x, offset.y);
            if (dropPointTmp.y <= size.height * 0.2f) {
               g2.drawRect(insets.left, insets.top, size.width - insets.left - insets.right, size.height / 2 - insets.top);
            } else if (dropPointTmp.y >= size.height * 0.8f) {
               g2.drawRect(insets.left, size.height / 2, size.width - insets.left - insets.right, size.height - size.height / 2 - insets.bottom);
            } else if (dropPointTmp.x <= size.width * 0.2f) {
               g2.drawRect(insets.left, insets.top, size.width / 2 - insets.left, size.height - insets.top - insets.bottom);
            } else if (dropPointTmp.x >= size.width * 0.8f) {
               g2.drawRect(size.width / 2, insets.top, size.width - size.width / 2 - insets.right, size.height - insets.top - insets.bottom);
            }
         }
      }
   }

   public TabContainer getTabContainer(Point location) {
      Component c = this;
      while (true) {
         Component c2 = c.getComponentAt(location);
         if (c2 == null || c2 == c) {
            return null;
         }
         if (c2 instanceof TabContainer) {
            return (TabContainer) c2;
         }
         location = SwingUtilities.convertPoint(c, location, c2);
         c = c2;
      }
   }
}
