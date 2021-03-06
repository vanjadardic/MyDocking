package mydocking;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TabContainer extends JPanel {

   private static final AtomicInteger nextId = new AtomicInteger();
   private static final Map<String, Tab> allTabs = new HashMap<>();
   private final TabManager tabManager;
   private Tab activeTab;
   private Insets borderInsets;
   private final JPanel tabBar;
   private final JPanel tabs;
   private final JScrollPane tabsScroll;
   private final JPanel controls;
   private final JButton buttonLeft;
   private final JButton buttonRight;
   private final JButton buttonDown;
   private final JPanel tabArea;

   public TabContainer(TabManager tabManager) {
      this.tabManager = tabManager;
      setLayout(new BorderLayout());
      setMinimumSize(new Dimension(10, 10));

      tabBar = new JPanel();
      tabBar.setLayout(new BorderLayout());

      tabs = new JPanel();
      tabs.setLayout(new BoxLayout(tabs, BoxLayout.LINE_AXIS));

      tabsScroll = new JScrollPane();
      tabsScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      tabsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      tabsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      tabsScroll.setWheelScrollingEnabled(false);
      tabsScroll.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent evt) {
            tabsResized();
         }
      });
      tabsScroll.addMouseWheelListener(new MouseWheelListener() {
         @Override
         public void mouseWheelMoved(MouseWheelEvent evt) {
            BoundedRangeModel range = tabsScroll.getHorizontalScrollBar().getModel();
            range.setValue((int) (range.getValue() + evt.getPreciseWheelRotation() * 50));
         }
      });
      tabsScroll.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
         @Override
         public void adjustmentValueChanged(AdjustmentEvent e) {
            for (Component c : tabs.getComponents()) {
               Tab tab = (Tab) c;
               fireMouseEvent(tab);
               fireMouseEvent(tab.getCloseButton());
            }
         }

         private void fireMouseEvent(Component component) {
            Point point = component.getMousePosition();
            if (point == null) {
               point = new Point(MouseInfo.getPointerInfo().getLocation());
               SwingUtilities.convertPointFromScreen(point, component);
               MouseEvent mouseEvent = new MouseEvent(component, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, point.x, point.y, 0, false);
               for (MouseListener mouseListener : component.getMouseListeners()) {
                  mouseListener.mouseExited(mouseEvent);
               }
            } else {
               MouseEvent mouseEvent = new MouseEvent(component, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, point.x, point.y, 0, false);
               for (MouseListener mouseListener : component.getMouseListeners()) {
                  mouseListener.mouseEntered(mouseEvent);
               }
            }
         }
      });
      tabsScroll.setViewportView(tabs);

      tabBar.add(tabsScroll, BorderLayout.CENTER);

      controls = new JPanel();
      controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

      buttonLeft = new JButton(new ImageIcon(getClass().getResource("/mydocking/images/left.png")));
      buttonLeft.setFocusPainted(false);
      buttonLeft.setFocusable(false);
      buttonLeft.setMargin(new Insets(0, 0, 0, 0));
      new ClickScrollHandler(buttonLeft, 400, 4, 150, 8, 50) {
         @Override
         public void handle() {
            BoundedRangeModel range = tabsScroll.getHorizontalScrollBar().getModel();
            range.setValue(range.getValue() - 50);
         }
      };
      controls.add(buttonLeft);

      buttonRight = new JButton(new ImageIcon(getClass().getResource("/mydocking/images/right.png")));
      buttonRight.setFocusPainted(false);
      buttonRight.setFocusable(false);
      buttonRight.setMargin(new Insets(0, 0, 0, 0));
      new ClickScrollHandler(buttonRight, 400, 4, 150, 8, 50) {
         @Override
         public void handle() {
            BoundedRangeModel range = tabsScroll.getHorizontalScrollBar().getModel();
            range.setValue(range.getValue() + 50);
         }
      };
      controls.add(buttonRight);

      buttonDown = new JButton(new ImageIcon(getClass().getResource("/mydocking/images/down.png")));
      buttonDown.setFocusPainted(false);
      buttonDown.setFocusable(false);
      buttonDown.setMargin(new Insets(0, 0, 0, 0));
      buttonDown.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            final OpenTabsComboPopup openTabsComboPopup = new OpenTabsComboPopup(
                  tabs.getComponents(), activeTab, 250);
            openTabsComboPopup.getList().addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                  Tab tab = (Tab) openTabsComboPopup.getList().getSelectedValue();
                  if (e.getButton() == MouseEvent.BUTTON2) {
                     DefaultComboBoxModel dataModel = (DefaultComboBoxModel) openTabsComboPopup.getList().getModel();
                     int index = dataModel.getIndexOf(tab);
                     dataModel.removeElementAt(index);
                     if (dataModel.getSize() > 0) {
                        ListSelectionModel selectionModel = openTabsComboPopup.getList().getSelectionModel();
                        if (index < dataModel.getSize()) {
                           selectionModel.setSelectionInterval(index, index);
                        } else {
                           selectionModel.clearSelection();
                        }
                        openTabsComboPopup.resize();
                     } else {
                        openTabsComboPopup.hide();
                     }
                     fireTabClosing(tab);
                  } else {
                     setActiveTab(tab);
                     openTabsComboPopup.hide();
                  }
               }
            });
            openTabsComboPopup.show(
                  e.getComponent(),
                  buttonDown.getWidth() - openTabsComboPopup.getPreferredSize().width + 1,
                  buttonDown.getHeight()
            );
         }
      });

      controls.add(buttonDown);

      tabBar.add(controls, BorderLayout.EAST);

      add(tabBar, BorderLayout.NORTH);

      tabArea = new JPanel();
      setBorderInsets(new Insets(1, 1, 1, 1));
      tabArea.setLayout(new CardLayout());
      add(tabArea, BorderLayout.CENTER);

      addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            tabsResized();
         }
      });
   }

   public void save(Element root, TabCustomDataHandler tabCustomDataHandler) {
      root.setAttribute("tabsScroll", Integer.toString(tabsScroll.getHorizontalScrollBar().getValue()));
      Document doc = root.getOwnerDocument();
      for (Component c : tabs.getComponents()) {
         Element tabElement = doc.createElement("tab");
         ((Tab) c).save(tabElement, tabCustomDataHandler);
         root.appendChild(tabElement);
      }
   }

   public static TabContainer restore(TabContainer tabContainer, Element root, TabCustomDataHandler tabCustomDataHandler) throws IOException {
      NodeList tabNodes = root.getElementsByTagName("tab");
      Tab activeTab2 = null;
      for (int i = 0; i < tabNodes.getLength(); i++) {
         Tab tab = Tab.restore((Element) tabNodes.item(i), tabCustomDataHandler);
         if (tab.isActive()) {
            activeTab2 = tab;
         }
         tabContainer.addNewTab(tab);
      }
      if (activeTab2 != null) {
         tabContainer.setActiveTab(activeTab2);
      }
      final int tabsScrollValue = Integer.parseInt(root.getAttribute("tabsScroll"));
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            tabContainer.tabsScroll.getHorizontalScrollBar().setValue(tabsScrollValue);
         }
      });
      return tabContainer;
   }

   public void setActiveTab(Tab tab) {
      if (activeTab != null) {
         activeTab.setInactive();
      }
      activeTab = tab;
      activeTab.setActive();

      CardLayout cl = (CardLayout) (tabArea.getLayout());
      cl.show(tabArea, activeTab.getId());
      setBorderInsets(borderInsets);

      tabs.scrollRectToVisible(activeTab.getBounds());
      tabManager.setActiveTabContainer(this);
   }

   public void addTab(Tab tab) {
      addTab(tab, tabs.getComponentCount());
   }

   public void addTab(Tab tab, int index) {
      tabs.add(tab, index);
      tabArea.add(tab.getComponent(), tab.getId());
      validate();
      tabsResized();
      setActiveTab(tab);
   }

   public Tab addNewTab(String title, Component component, TabColors tabColors) {
      String id = Integer.toString(nextId.getAndIncrement());

      final Tab newTab = new Tab(id, title, component, tabColors);
      newTab.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
               newTab.getTabContainer().setActiveTab(newTab);
            } else if (evt.getButton() == MouseEvent.BUTTON2) {
               newTab.getTabContainer().fireTabClosing(newTab);
            }
         }
      });
      newTab.getCloseButton().addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            newTab.getTabContainer().fireTabClosing(newTab);
         }
      });
      addTab(newTab);

      allTabs.put(newTab.getId(), newTab);
      return newTab;
   }

   public void addNewTab(final Tab newTab) {
      newTab.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
               newTab.getTabContainer().setActiveTab(newTab);
            } else if (evt.getButton() == MouseEvent.BUTTON2) {
               newTab.getTabContainer().fireTabClosing(newTab);
            }
         }
      });
      newTab.getCloseButton().addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            newTab.getTabContainer().fireTabClosing(newTab);
         }
      });
      addTab(newTab);

      allTabs.put(newTab.getId(), newTab);
      int id = Integer.parseInt(newTab.getId());
      if (id >= nextId.get()) {
         nextId.set(id + 1);
      }
   }

   public void closeTab(Tab tab) {
      removeTab(tab);
      fireTabClosed(tab);
   }

   public void removeTab(Tab tab) {
      if (tab == activeTab && tabs.getComponentCount() > 1) {
         int nextIndex = tabs.getComponentZOrder(tab) + 1;
         if (nextIndex == tabs.getComponentCount()) {
            nextIndex -= 2;
         }
         setActiveTab((Tab) tabs.getComponent(nextIndex));
      }
      tabs.remove(tab);
      tabArea.remove(tab.getComponent());
      tabBar.validate();
      tabBar.repaint();
      tabsResized();
      if (tabs.getComponentCount() == 0) {
         Container parent = getParent();
         if (parent instanceof JSplitPane) {
            JSplitPane splitParent = (JSplitPane) parent;
            Component otherComponent = (splitParent.getLeftComponent() == this)
                  ? splitParent.getRightComponent()
                  : splitParent.getLeftComponent();
            Container parentOfParent = splitParent.getParent();
            if (parentOfParent instanceof JSplitPane) {
               JSplitPane splitParentOfParent = (JSplitPane) parentOfParent;
               int dividerLocation = splitParentOfParent.getDividerLocation();
               splitParentOfParent.add(otherComponent, (splitParentOfParent.getLeftComponent() == splitParent)
                     ? JSplitPane.LEFT
                     : JSplitPane.RIGHT);
               splitParentOfParent.setDividerLocation(dividerLocation);
            } else {
               parentOfParent.remove(splitParent);
               parentOfParent.add(otherComponent);
               parentOfParent.validate();
            }
            tabManager.layoutChanged();
         } else {
            activeTab = null;
            setBorderInsets(borderInsets);
         }
      }
   }

   public JPanel getTabs() {
      return tabs;
   }

   public JScrollPane getTabsScroll() {
      return tabsScroll;
   }

   private void tabsResized() {
      int width = tabs.getWidth();
      if (controls.isVisible()) {
         width -= controls.getWidth();
      }
      if (controls.isVisible() != tabsScroll.getWidth() < width) {
         controls.setVisible(!controls.isVisible());
         tabBar.validate();
      }
   }

   public void addTabClosingListener(TabClosingListener listener) {
      listenerList.add(TabClosingListener.class, listener);
   }

   public void removeTabClosingListener(TabClosingListener listener) {
      listenerList.remove(TabClosingListener.class, listener);
   }

   private void fireTabClosing(Tab tab) {
      Object[] listeners = listenerList.getListenerList();
      for (int i = 0; i < listeners.length; i = i + 2) {
         if (listeners[i] == TabClosingListener.class) {
            if (!((TabClosingListener) listeners[i + 1]).tabClosing(tab)) {
               return;
            }
         }
      }
      closeTab(tab);
   }

   public void addTabClosedListener(TabClosedListener listener) {
      listenerList.add(TabClosedListener.class, listener);
   }

   public void removeTabClosedListener(TabClosedListener listener) {
      listenerList.remove(TabClosedListener.class, listener);
   }

   private void fireTabClosed(Tab tab) {
      Object[] listeners = listenerList.getListenerList();
      for (int i = 0; i < listeners.length; i = i + 2) {
         if (listeners[i] == TabClosedListener.class) {
            ((TabClosedListener) listeners[i + 1]).tabClosed(tab);
         }
      }
   }

   public static Tab getTab(String id) {
      return allTabs.get(id);
   }

   public void setBorderInsets(Insets borderInsets) {
      this.borderInsets = borderInsets;
      this.borderInsets.top = 2;
      tabArea.setBorder(BorderFactory.createMatteBorder(
            this.borderInsets.top, this.borderInsets.left, this.borderInsets.bottom, this.borderInsets.right,
            activeTab == null ? getBackground() : activeTab.getTabColors().getBackgroundActive()
      ));
   }
}
