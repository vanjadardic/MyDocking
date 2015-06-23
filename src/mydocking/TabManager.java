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
               TabContainer tcSrc = tab.getTabContainer();
               if (tcSrc == null) {
                  return;
               }
               DropLocation dropLocation = getDropLocation(dtde.getLocation());
               if (dropLocation.tabContainer == null || dropLocation.location == null) {
                  return;
               }

               setDropPoint(null);
               tcSrc.removeTab(tab);
               TabContainer tcNew = new TabContainer();
               tcNew.addTab(tab);

               int whichSplit = ("left".equals(dropLocation.location) || "right".equals(dropLocation.location))
                     ? JSplitPane.HORIZONTAL_SPLIT
                     : JSplitPane.VERTICAL_SPLIT;
               JSplitPane split = new JSplitPane(whichSplit, true);
               split.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
               split.setDividerSize(4);
               split.setResizeWeight(0.5);
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

               int dividerLocation = (whichSplit == JSplitPane.HORIZONTAL_SPLIT ? dropLocation.tabContainer.getWidth() : dropLocation.tabContainer.getHeight()) / 2 - 2;
               Container parent = dropLocation.tabContainer.getParent();
               if (parent instanceof JSplitPane) {
                  JSplitPane splitParent = (JSplitPane) parent;
                  int dividerLocation2 = splitParent.getDividerLocation();
                  splitParent.add(split, (splitParent.getLeftComponent() == dropLocation.tabContainer)
                        ? JSplitPane.LEFT
                        : JSplitPane.RIGHT);
                  splitParent.setDividerLocation(dividerLocation2);
               } else {
                  parent.remove(dropLocation.tabContainer);
                  parent.add(split);
                  parent.validate();
               }
               if ("left".equals(dropLocation.location) || "top".equals(dropLocation.location)) {
                  split.setLeftComponent(tcNew);
                  split.setRightComponent(dropLocation.tabContainer);
               }
               if ("right".equals(dropLocation.location) || "bottom".equals(dropLocation.location)) {
                  split.setLeftComponent(dropLocation.tabContainer);
                  split.setRightComponent(tcNew);
               }
               split.setDividerLocation(dividerLocation);
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

         DropLocation dropLocation = getDropLocation(dropPoint);
         if (dropLocation.tabContainer != null && dropLocation.location != null) {
            Dimension size = dropLocation.tabContainer.getSize();
            Insets insets = dropLocation.tabContainer.getInsets();
            insets.left += 1;
            insets.top += 1;
            insets.right += 2;
            insets.bottom += 2;
            Point offset = SwingUtilities.convertPoint(dropLocation.tabContainer, new Point(0, 0), this);
            g2.translate(offset.x, offset.y);
            switch (dropLocation.location) {
               case "top":
                  g2.drawRect(insets.left, insets.top, size.width - insets.left - insets.right, size.height / 2 - insets.top);
                  break;
               case "bottom":
                  g2.drawRect(insets.left, size.height / 2, size.width - insets.left - insets.right, size.height - size.height / 2 - insets.bottom);
                  break;
               case "left":
                  g2.drawRect(insets.left, insets.top, size.width / 2 - insets.left, size.height - insets.top - insets.bottom);
                  break;
               case "right":
                  g2.drawRect(size.width / 2, insets.top, size.width - size.width / 2 - insets.right, size.height - insets.top - insets.bottom);
                  break;
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

   private class DropLocation {

      public TabContainer tabContainer;
      public String location;
   }

   private DropLocation getDropLocation(Point dropPoint) {
      DropLocation dropLocation = new DropLocation();
      dropLocation.tabContainer = getTabContainer(dropPoint);
      if (dropLocation.tabContainer != null) {
         Dimension size = dropLocation.tabContainer.getSize();
         Point dropPointTmp = SwingUtilities.convertPoint(this, dropPoint, dropLocation.tabContainer);
         if (dropPointTmp.y <= size.height * 0.2f) {
            dropLocation.location = "top";
         } else if (dropPointTmp.y >= size.height * 0.8f) {
            dropLocation.location = "bottom";
         } else if (dropPointTmp.x <= size.width * 0.2f) {
            dropLocation.location = "left";
         } else if (dropPointTmp.x >= size.width * 0.8f) {
            dropLocation.location = "right";
         }
      }
      return dropLocation;
   }
}
