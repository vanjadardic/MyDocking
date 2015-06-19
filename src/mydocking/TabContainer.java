package mydocking;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class TabContainer extends JPanel {

   private static final AtomicInteger nextId = new AtomicInteger();
   private Tab activeTab;
   private final JPanel tabBar;
   private final JPanel tabs;
   private final JScrollPane tabsScroll;
   private final JPanel controls;
   private final JButton buttonLeft;
   private final JButton buttonRight;
   private final JButton buttonDown;
   private final JPanel separator;
   private final JPanel tabArea;

   public TabContainer() {
      setLayout(new BorderLayout());
      setMinimumSize(new Dimension(0, 0));

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
      tabsScroll.setViewportView(tabs);

      tabBar.add(tabsScroll, BorderLayout.CENTER);

      controls = new JPanel();
      controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

      buttonLeft = new JButton(new ImageIcon(getClass().getResource("/mydocking/images/left.png")));
      buttonLeft.setFocusPainted(false);
      buttonLeft.setFocusable(false);
      buttonLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
      new ClickScrollHandler(buttonLeft, 400, 4, 150, 4, 50) {
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
      buttonRight.setMargin(new java.awt.Insets(0, 0, 0, 0));
      new ClickScrollHandler(buttonRight, 400, 4, 150, 4, 50) {
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
      buttonDown.setMargin(new java.awt.Insets(0, 0, 0, 0));
      buttonDown.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            JComboBox<Component> tmpCombo = new JComboBox<>(tabs.getComponents());
            tmpCombo.setMaximumRowCount(Integer.MAX_VALUE);
            tmpCombo.setRenderer(new OpenTabsListCellRenderer(250, activeTab));
            final OpenTabsComboPopup openTabsComboPopup = new OpenTabsComboPopup(tmpCombo);
            openTabsComboPopup.show(
                  e.getComponent(),
                  buttonDown.getWidth() - openTabsComboPopup.getPreferredSize().width,
                  buttonDown.getHeight()
            );
            openTabsComboPopup.getList().addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                  Tab tab = (Tab) openTabsComboPopup.getList().getSelectedValue();
                  openTabsComboPopup.hide();
                  if (e.getButton() == MouseEvent.BUTTON2) {
                     fireTabClosing(tab);
                  } else {
                     setActiveTab(tab);
                  }
               }
            });
         }
      });

      controls.add(buttonDown);

      tabBar.add(controls, BorderLayout.EAST);

      separator = new JPanel();
      separator.setPreferredSize(new Dimension(0, 2));
      tabBar.add(separator, BorderLayout.SOUTH);

      add(tabBar, BorderLayout.NORTH);

      tabArea = new JPanel();
      tabArea.setLayout(new java.awt.CardLayout());
      add(tabArea, BorderLayout.CENTER);
   }

   public void setActiveTab(Tab tab) {
      if (activeTab != null) {
         activeTab.setInactive();
      }
      activeTab = tab;
      activeTab.setActive();

      CardLayout cl = (CardLayout) (tabArea.getLayout());
      cl.show(tabArea, tab.getId());
      separator.setBackground(tab.getBackgroundActive());

      tabs.scrollRectToVisible(tab.getBounds());
   }

   public Tab addNewTab(String title, Component component) {
      String id = Integer.toString(nextId.getAndIncrement());

      final Tab newTab = new Tab(id, title, component);
      newTab.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
               setActiveTab(newTab);
            } else if (evt.getButton() == MouseEvent.BUTTON2) {
               fireTabClosing(newTab);
            }
         }
      });
      newTab.getCloseButton().addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            fireTabClosing(newTab);
         }
      });
      tabs.add(newTab);
      tabArea.add(component, id);
      validate();
      tabsResized();
      setActiveTab(newTab);

      return newTab;
   }

   public void closeTab(Tab tab) {
      if (tab == activeTab && tabs.getComponentCount() > 1) {
         int nextIndex = tabs.getComponentZOrder(tab) + 1;
         if (nextIndex == tabs.getComponentCount()) {
            nextIndex -= 2;
         }
         setActiveTab((Tab) tabs.getComponent(nextIndex));
      }
      removeTab(tab);
      fireTabClosed(tab);
   }

   public void removeTab(Tab tab) {
      tabs.remove(tab);
      tabBar.validate();
      tabBar.repaint();
      if (tabs.getComponentCount() == 0) {
         separator.setBackground(getBackground());
      }
      tabsResized();
      tabArea.remove(tab.getComponent());
   }

   private void tabsResized() {
      int width = tabs.getWidth();
      if (controls.isVisible()) {
         width -= controls.getWidth();
      }
      controls.setVisible(tabsScroll.getWidth() < width);
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
}