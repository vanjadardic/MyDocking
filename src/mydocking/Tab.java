package mydocking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tab extends JPanel {

   public static final DataFlavor DATA_FLAVOR = getDataFlavor();
   private final String id;
   private final JLabel title;
   private final JButton closeButton;
   private final Component component;
   private boolean isActive;
   private Color backgroundInactive;
   private Color backgroundHover;
   private Color backgroundActive;
   private Color foregroundInactive;
   private Color foregroundHover;
   private Color foregroundActive;

   public Tab(String id, String titleText, Component component) {
      this.id = id;
      this.component = component;
      isActive = false;

      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
      setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 3));

      title = new JLabel(titleText);
      title.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
      add(title);

      closeButton = new JButton();
      closeButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      closeButton.setMargin(new Insets(0, 0, 0, 0));
      closeButton.setIcon(new ImageIcon(getClass().getResource("/mydocking/images/close.png")));
      closeButton.setRolloverIcon(new ImageIcon(getClass().getResource("/mydocking/images/close-hover.png")));
      closeButton.setPressedIcon(new ImageIcon(getClass().getResource("/mydocking/images/close-pressed.png")));
      closeButton.setFocusPainted(false);
      closeButton.setFocusable(false);
      add(closeButton);

      setBackgroundActive(new Color(0x00, 0x7a, 0xcc));
      setBackgroundHover(new Color(0x1c, 0x97, 0xea));
      setBackgroundInactive(new Color(0xcc, 0xce, 0xdb));

      setForegroundActive(new Color(0xff, 0xff, 0xff));
      setForegroundHover(new Color(0xff, 0xff, 0xff));
      setForegroundInactive(new Color(0x6d, 0x6d, 0x6d));

      addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            Tab.this.mouseEntered();
         }

         @Override
         public void mouseExited(MouseEvent e) {
            Tab.this.mouseExited(e);
         }
      });
      closeButton.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            Tab.this.mouseEntered();
         }

         @Override
         public void mouseExited(MouseEvent e) {
            e.translatePoint(closeButton.getX(), closeButton.getY());
            Tab.this.mouseExited(e);
         }
      });

      setInactive();

      final DragSource source = new DragSource();
      source.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new DragGestureListener() {
         @Override
         public void dragGestureRecognized(DragGestureEvent dge) {
            Image img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            paint(img.getGraphics());
            source.startDrag(dge, Cursor.getDefaultCursor(), img, dge.getDragOrigin(), new Transferable() {
               @Override
               public DataFlavor[] getTransferDataFlavors() {
                  return new DataFlavor[]{DATA_FLAVOR};
               }

               @Override
               public boolean isDataFlavorSupported(DataFlavor flavor) {
                  return DATA_FLAVOR.equals(flavor);
               }

               @Override
               public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                  if (isDataFlavorSupported(flavor)) {
                     return Tab.this.id;
                  } else {
                     throw new UnsupportedFlavorException(flavor);
                  }
               }
            }, null);
         }
      });
   }

   private static DataFlavor getDataFlavor() {
      try {
         return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + "-" + System.currentTimeMillis() + ";class=\"" + Tab.class.getName() + "\"");
      } catch (ClassNotFoundException ex) {
         return new DataFlavor(Tab.class, null);
      }
   }

   public void setActive() {
      setBackground(getBackgroundActive());
      setForeground(getForegroundActive());
      isActive = true;
   }

   public void setHover() {
      setBackground(getBackgroundHover());
      setForeground(getForegroundHover());
   }

   public void setInactive() {
      setBackground(getBackgroundInactive());
      setForeground(getForegroundInactive());
      isActive = false;
   }

   public void mouseEntered() {
      if (!isActive) {
         setHover();
      }
   }

   public void mouseExited(MouseEvent e) {
      if (!isActive) {
         Rectangle r = new Rectangle();
         computeVisibleRect(r);
         if (!r.contains(e.getPoint())) {
            setInactive();
         }
      }
   }

   public String getId() {
      return id;
   }

   public String getTitle() {
      return title.getText();
   }

   public void setTitle(String title) {
      this.title.setText(title);
      repaint();
   }

   public JButton getCloseButton() {
      return closeButton;
   }

   public Component getComponent() {
      return component;
   }

   @Override
   public void setForeground(Color foregroundColor) {
      if (title != null) {
         title.setForeground(foregroundColor);
         repaint();
      }
   }

   public Color getBackgroundInactive() {
      return backgroundInactive;
   }

   public void setBackgroundInactive(Color backgroundInactive) {
      this.backgroundInactive = backgroundInactive;
   }

   public Color getBackgroundHover() {
      return backgroundHover;
   }

   public void setBackgroundHover(Color backgroundHover) {
      this.backgroundHover = backgroundHover;
   }

   public Color getBackgroundActive() {
      return backgroundActive;
   }

   public void setBackgroundActive(Color backgroundActive) {
      this.backgroundActive = backgroundActive;
   }

   public Color getForegroundInactive() {
      return foregroundInactive;
   }

   public void setForegroundInactive(Color foregroundInactive) {
      this.foregroundInactive = foregroundInactive;
   }

   public Color getForegroundHover() {
      return foregroundHover;
   }

   public void setForegroundHover(Color foregroundHover) {
      this.foregroundHover = foregroundHover;
   }

   public Color getForegroundActive() {
      return foregroundActive;
   }

   public void setForegroundActive(Color foregroundActive) {
      this.foregroundActive = foregroundActive;
   }

   public TabContainer getTabContainer() {
      Component c = this;
      while (true) {
         Component c2 = c.getParent();
         if (c2 == null || c2 == c) {
            return null;
         }
         if (c2 instanceof TabContainer) {
            return (TabContainer) c2;
         }
         c = c2;
      }
   }
}
