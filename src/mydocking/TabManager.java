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
import java.awt.Polygon;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class TabManager extends JPanel {

   private static int cnt = 0;
   private Point dropPoint = null;
   private ScrollHandler scrollLeft = null;
   private ScrollHandler scrollRight = null;

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
            setDropPoint(dtde.getLocation());
         }

         @Override
         public void dragOver(DropTargetDragEvent dtde) {
            if (!dtde.isDataFlavorSupported(Tab.DATA_FLAVOR) || dtde.getDropAction() != DnDConstants.ACTION_MOVE) {
               setDropPoint(null);
            } else {
               setDropPoint(dtde.getLocation());
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
            setDropPoint(null);
         }

         @Override
         public void drop(DropTargetDropEvent dtde) {
            try {
               setDropPoint(null);
               Tab tab = TabContainer.getTab((String) dtde.getTransferable().getTransferData(Tab.DATA_FLAVOR));
               TabContainer tabContainerSrc = tab.getTabContainer();
               if (tabContainerSrc == null) {
                  return;
               }
               DropLocation dropLocation = getDropLocation(dtde.getLocation());
               if (dropLocation.location == DropLocation.LLEFT || dropLocation.location == DropLocation.TTOP
                     || dropLocation.location == DropLocation.RRIGHT || dropLocation.location == DropLocation.BBOTTOM) {
                  if (tabContainerSrc == getComponent(0) && tabContainerSrc.getTabs().getComponentCount() == 1) {
                     return;
                  }
                  tabContainerSrc.removeTab(tab);
                  TabContainer tcNew = new TabContainer();
                  tcNew.addTab(tab);
                  int whichSplit = (dropLocation.location == DropLocation.LLEFT || dropLocation.location == DropLocation.RRIGHT)
                        ? JSplitPane.HORIZONTAL_SPLIT
                        : JSplitPane.VERTICAL_SPLIT;
                  JSplitPane split = createEmptyJSplitPane(whichSplit);
                  Component component = getComponent(0);
                  int dividerLocation;
                  switch (dropLocation.location) {
                     case DropLocation.TTOP:
                        dividerLocation = component.getHeight() / 3 - 2;
                        break;
                     case DropLocation.BBOTTOM:
                        dividerLocation = (component.getHeight() * 2) / 3 - 2;
                        break;
                     case DropLocation.LLEFT:
                        dividerLocation = component.getWidth() / 3 - 2;
                        break;
                     default:
                        dividerLocation = (component.getWidth() * 2) / 3 - 2;
                  }
                  remove(component);
                  add(split);
                  validate();
                  if (dropLocation.location == DropLocation.LLEFT || dropLocation.location == DropLocation.TTOP) {
                     split.setLeftComponent(tcNew);
                     split.setRightComponent(component);
                  } else {
                     split.setLeftComponent(component);
                     split.setRightComponent(tcNew);
                  }
                  split.setDividerLocation(dividerLocation);
               } else {
                  if (dropLocation.tabContainer == null) {
                     return;
                  }
                  if (tabContainerSrc == dropLocation.tabContainer && tabContainerSrc.getTabs().getComponentCount() == 1) {
                     return;
                  }
                  if (dropLocation.location == DropLocation.LEFT || dropLocation.location == DropLocation.TOP
                        || dropLocation.location == DropLocation.RIGHT || dropLocation.location == DropLocation.BOTTOM) {
                     tabContainerSrc.removeTab(tab);
                     TabContainer tcNew = new TabContainer();
                     tcNew.addTab(tab);
                     int whichSplit = (dropLocation.location == DropLocation.LEFT || dropLocation.location == DropLocation.RIGHT)
                           ? JSplitPane.HORIZONTAL_SPLIT
                           : JSplitPane.VERTICAL_SPLIT;
                     JSplitPane split = createEmptyJSplitPane(whichSplit);
                     int dividerLocation = (whichSplit == JSplitPane.HORIZONTAL_SPLIT ? dropLocation.tabContainer.getWidth() : dropLocation.tabContainer.getHeight()) / 2 - 2;
                     Container parent = dropLocation.tabContainer.getParent();
                     if (parent instanceof JSplitPane) {
                        JSplitPane splitParent = (JSplitPane) parent;
                        int dividerLocationParent = splitParent.getDividerLocation();
                        splitParent.add(split, (splitParent.getLeftComponent() == dropLocation.tabContainer)
                              ? JSplitPane.LEFT
                              : JSplitPane.RIGHT);
                        splitParent.setDividerLocation(dividerLocationParent);
                     } else {
                        parent.remove(dropLocation.tabContainer);
                        parent.add(split);
                        parent.validate();
                     }
                     if (dropLocation.location == DropLocation.LEFT || dropLocation.location == DropLocation.TOP) {
                        split.setLeftComponent(tcNew);
                        split.setRightComponent(dropLocation.tabContainer);
                     } else {
                        split.setLeftComponent(dropLocation.tabContainer);
                        split.setRightComponent(tcNew);
                     }
                     split.setDividerLocation(dividerLocation);
                  } else if (dropLocation.location == DropLocation.TABS) {
                     if (tabContainerSrc == dropLocation.tabContainer) {
                        if (dropLocation.tab == null) {
                           dropLocation.tabContainer.getTabs().setComponentZOrder(tab, dropLocation.tabContainer.getTabs().getComponentCount() - 1);
                        } else {
                           int sourceZ = dropLocation.tabContainer.getTabs().getComponentZOrder(tab);
                           int destZ = dropLocation.tabContainer.getTabs().getComponentZOrder(dropLocation.tab);
                           if (destZ < sourceZ) {
                              dropLocation.tabContainer.getTabs().setComponentZOrder(tab, destZ);
                           } else if (destZ > sourceZ) {
                              dropLocation.tabContainer.getTabs().setComponentZOrder(tab, dropLocation.tabContainer.getTabs().getComponentZOrder(dropLocation.tab) - 1);
                           }
                        }
                        dropLocation.tabContainer.validate();
                     } else if (dropLocation.location == DropLocation.TABS) {
                        tabContainerSrc.removeTab(tab);
                        if (dropLocation.tab != null) {
                           dropLocation.tabContainer.addTab(tab, dropLocation.tabContainer.getTabs().getComponentZOrder(dropLocation.tab));
                        } else {
                           dropLocation.tabContainer.addTab(tab);
                        }
                     }
                  }
               }
            } catch (UnsupportedFlavorException | IOException ex) {
               throw new RuntimeException(ex);
            }
         }
      }, true, null);

   }

   private Tab addTab(TabContainer tc) {
      TabColors[] c = new TabColors[]{TabColors.PURPLE, TabColors.RED, TabColors.GREEN, TabColors.YELLOW,
         TabColors.BLUE, TabColors.PINK, TabColors.LIME, TabColors.ORANGE};
      JPanel jp = new JPanel();
      jp.setBackground(new Color(0.5f + (float) Math.random() / 2, 0.5f + (float) Math.random() / 2, 0.5f + (float) Math.random() / 2));
      return tc.addNewTab("New Tab #" + ++cnt, jp, c[(int) (Math.random() * c.length)]);
   }

   public Point getDropPoint() {
      return dropPoint;
   }

   public void setDropPoint(Point dropPoint) {
      if (dropPoint == null || !dropPoint.equals(getDropPoint())) {
         this.dropPoint = dropPoint;
         repaint();
      }
      if (dropPoint == null) {
         if (scrollLeft != null) {
            scrollLeft.end();
            scrollLeft = null;
         }
         if (scrollRight != null) {
            scrollRight.end();
            scrollRight = null;
         }
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
         Dimension s = getSize();
         Insets i = getInsets();
         i.left += 1;
         i.top += 1;
         i.right += 2;
         i.bottom += 2;
         switch (dropLocation.location) {
            case DropLocation.TTOP:
               g2.drawRect(i.left, i.top, s.width - i.left - i.right, s.height / 3 - i.top);
               break;
            case DropLocation.BBOTTOM:
               g2.drawRect(i.left, (s.height * 2) / 3, s.width - i.left - i.right, s.height - s.height / 4 - i.bottom);
               break;
            case DropLocation.LLEFT:
               g2.drawRect(i.left, i.top, s.width / 3 - i.left, s.height - i.top - i.bottom);
               break;
            case DropLocation.RRIGHT:
               g2.drawRect((s.width * 2) / 3, i.top, s.width - s.width / 3 - i.right, s.height - i.top - i.bottom);
               break;
            default:
               g2.setColor(new Color(0xff, 0x60, 0x00, 0xa0));
               if (dropLocation.tabContainer != null) {
                  s = dropLocation.tabContainer.getSize();
                  i = dropLocation.tabContainer.getInsets();
                  i.left += 1;
                  i.top += 1;
                  i.right += 2;
                  i.bottom += 2;
                  Point offset = SwingUtilities.convertPoint(dropLocation.tabContainer, new Point(0, 0), this);
                  g2.translate(offset.x, offset.y);
                  switch (dropLocation.location) {
                     case DropLocation.TABS:
                        Tab tab;
                        int x = dropLocation.tabContainer.getTabsScroll().getHorizontalScrollBar().getValue();
                        if (dropLocation.tab != null) {
                           tab = dropLocation.tab;
                           x = tab.getBounds().x - x;
                        } else {
                           tab = dropLocation.previousTab;
                           x = tab.getBounds().x + tab.getBounds().width - x;
                        }
                        Polygon shape = new Polygon();
                        shape.addPoint(i.left, i.top + tab.getHeight() + 1);
                        shape.addPoint(i.left + x - tab.getWidth() / 2 - 1, i.top + tab.getHeight() + 1);
                        shape.addPoint(i.left + x - tab.getWidth() / 2 - 1, i.top);
                        shape.addPoint(i.left + x - tab.getWidth() / 2 - 1 + tab.getWidth() - 3, i.top);
                        shape.addPoint(i.left + x - tab.getWidth() / 2 - 1 + tab.getWidth() - 3, i.top + tab.getHeight() + 1);
                        shape.addPoint(s.width - i.right, i.top + tab.getHeight() + 1);
                        shape.addPoint(s.width - i.right, s.height - i.bottom);
                        shape.addPoint(i.left, s.height - i.bottom);
                        g2.draw(shape);
                        break;
                     case DropLocation.TOP:
                        g2.drawRect(i.left, i.top, s.width - i.left - i.right, s.height / 2 - i.top);
                        break;
                     case DropLocation.BOTTOM:
                        g2.drawRect(i.left, s.height / 2, s.width - i.left - i.right, s.height - s.height / 2 - i.bottom);
                        break;
                     case DropLocation.LEFT:
                        g2.drawRect(i.left, i.top, s.width / 2 - i.left, s.height - i.top - i.bottom);
                        break;
                     case DropLocation.RIGHT:
                        g2.drawRect(s.width / 2, i.top, s.width - s.width / 2 - i.right, s.height - i.top - i.bottom);
                        break;
                  }
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

      public static final int LEFT = 0;
      public static final int TOP = 1;
      public static final int RIGHT = 2;
      public static final int BOTTOM = 3;
      public static final int TABS = 4;
      public static final int LLEFT = 100;
      public static final int TTOP = 101;
      public static final int RRIGHT = 102;
      public static final int BBOTTOM = 103;

      public TabContainer tabContainer = null;
      public int location = -1;
      public Tab tab = null;
      public Tab previousTab = null;
   }

   private DropLocation getDropLocation(Point dropPoint) {
      final DropLocation dropLocation = new DropLocation();
      boolean isScrollLeft = false;
      boolean isScrollTop = false;
      Dimension size = getSize();
      if (dropPoint.y < 10) {
         dropLocation.location = DropLocation.TTOP;
      } else if (dropPoint.y > size.height - 10) {
         dropLocation.location = DropLocation.BBOTTOM;
      } else if (dropPoint.x < 10) {
         dropLocation.location = DropLocation.LLEFT;
      } else if (dropPoint.x > size.width - 10) {
         dropLocation.location = DropLocation.RRIGHT;
      } else {
         dropLocation.tabContainer = getTabContainer(dropPoint);
         if (dropLocation.tabContainer != null) {
            dropPoint = SwingUtilities.convertPoint(this, dropPoint, dropLocation.tabContainer);
            size = dropLocation.tabContainer.getSize();
            int tabsHeight = dropLocation.tabContainer.getTabs().getHeight() * 2;
            size.height -= tabsHeight;
            boolean isTop = false;
            if (dropPoint.y < tabsHeight) {
               dropLocation.location = DropLocation.TABS;
               isTop = true;
            } else if (dropPoint.y <= tabsHeight + size.height * 0.2f) {
               dropLocation.location = DropLocation.TOP;
            } else if (dropPoint.y >= tabsHeight + size.height * 0.8f) {
               dropLocation.location = DropLocation.BOTTOM;
            } else if (dropPoint.x <= size.width * 0.2f) {
               dropLocation.location = DropLocation.LEFT;
            } else if (dropPoint.x >= size.width * 0.8f) {
               dropLocation.location = DropLocation.RIGHT;
            } else {
               dropLocation.location = DropLocation.TABS;
            }
            if (dropLocation.location == DropLocation.TABS) {
               int width = 0;
               for (Component component : dropLocation.tabContainer.getTabs().getComponents()) {
                  width += component.getWidth();
               }
               width = Math.min(width, dropLocation.tabContainer.getTabsScroll().getWidth());
               if (!isTop) {
                  dropPoint.x = (int) Math.max((dropPoint.x - size.width * 0.2) / (size.width * 0.6) * width, 0);
               }
               if (dropPoint.x < width) {
                  if (dropPoint.x < 20) {
                     isScrollLeft = true;
                     if (scrollLeft == null) {
                        scrollLeft = new ScrollHandler(400, 4, 150, 8, 50) {
                           @Override
                           public void handle() {
                              BoundedRangeModel range = dropLocation.tabContainer.getTabsScroll().getHorizontalScrollBar().getModel();
                              range.setValue(range.getValue() - 50);
                              repaint();
                           }
                        };
                        scrollLeft.start();
                     }
                  } else if (dropPoint.x > width - 20) {
                     isScrollTop = true;
                     if (scrollRight == null) {
                        scrollRight = new ScrollHandler(400, 4, 150, 8, 50) {
                           @Override
                           public void handle() {
                              BoundedRangeModel range = dropLocation.tabContainer.getTabsScroll().getHorizontalScrollBar().getModel();
                              range.setValue(range.getValue() + 50);
                              repaint();
                           }
                        };
                        scrollRight.start();
                     }
                  }
                  Component c = dropLocation.tabContainer.getTabs().getComponentAt(dropPoint.x + dropLocation.tabContainer.getTabsScroll().getHorizontalScrollBar().getValue(), 0);
                  if (c instanceof Tab) {
                     dropLocation.tab = (Tab) c;
                     Point tabOffset = SwingUtilities.convertPoint(dropLocation.tabContainer, dropPoint, dropLocation.tab);
                     if (tabOffset.x > dropLocation.tab.getWidth() / 2) {
                        int index = dropLocation.tab.getParent().getComponentZOrder(dropLocation.tab) + 1;
                        if (index < dropLocation.tabContainer.getTabs().getComponentCount()) {
                           dropLocation.tab = (Tab) dropLocation.tab.getParent().getComponent(index);
                        } else {
                           dropLocation.previousTab = dropLocation.tab;
                           dropLocation.tab = null;
                        }
                     }
                  }
               } else {
                  dropLocation.location = DropLocation.TOP;
               }
            }
         }
      }
      if (!isScrollLeft && scrollLeft != null) {
         scrollLeft.end();
         scrollLeft = null;
      }
      if (!isScrollTop && scrollRight != null) {
         scrollRight.end();
         scrollRight = null;
      }
      return dropLocation;
   }

   private JSplitPane createEmptyJSplitPane(int whichSplit) {
      JSplitPane split = new JSplitPane(whichSplit, true);
      split.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      split.setDividerSize(4);
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
      return split;
   }
}
