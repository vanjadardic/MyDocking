package mydocking;

import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class CustomSplitPaneDivider extends BasicSplitPaneDivider {

   public CustomSplitPaneDivider(BasicSplitPaneUI ui) {
      super(ui);
   }

   @Override
   public void setBorder(Border b) {
   }

   @Override
   public void setBasicSplitPaneUI(BasicSplitPaneUI newUI) {
      if (splitPane != null) {
         splitPane.removePropertyChangeListener(this);
         if (mouseHandler != null) {
            splitPane.removeMouseListener(mouseHandler);
            splitPane.removeMouseMotionListener(mouseHandler);
            removeMouseListener(mouseHandler);
            removeMouseMotionListener(mouseHandler);
            mouseHandler = null;
         }
      }
      splitPaneUI = newUI;
      if (newUI != null) {
         splitPane = newUI.getSplitPane();
         if (splitPane != null) {
            if (mouseHandler == null) {
               mouseHandler = new MyMouseHandler();
            }
            splitPane.addMouseListener(mouseHandler);
            splitPane.addMouseMotionListener(mouseHandler);
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
            splitPane.addPropertyChangeListener(this);
            if (splitPane.isOneTouchExpandable()) {
               oneTouchExpandableChanged();
            }
         }
      } else {
         splitPane = null;
      }
   }

   protected class MyMouseHandler extends MouseHandler {

      @Override
      public void mousePressed(MouseEvent e) {
         if ((e.getSource() == CustomSplitPaneDivider.this || e.getSource() == splitPane)
               && dragger == null && splitPane.isEnabled()) {
            Component newHiddenDivider = splitPaneUI.getNonContinuousLayoutDivider();

            if (hiddenDivider != newHiddenDivider) {
               if (hiddenDivider != null) {
                  hiddenDivider.removeMouseListener(this);
                  hiddenDivider.removeMouseMotionListener(this);
               }
               hiddenDivider = newHiddenDivider;
               if (hiddenDivider != null) {
                  hiddenDivider.addMouseMotionListener(this);
                  hiddenDivider.addMouseListener(this);
               }
            }
            if (splitPane.getLeftComponent() != null
                  && splitPane.getRightComponent() != null) {
               prepareForDragging2();
               if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
                  MyDragController dragger2 = new MyDragController(e);
                  if (!dragger2.isValid()) {
                     finishDraggingTo2();
                     dragger = null;
                  } else {
                     prepareForDragging();
                     dragger2.continueDrag(e);
                     dragger = dragger2;
                  }
               } else {
                  MyVerticalDragController dragger2 = new MyVerticalDragController(e);
                  if (!dragger2.isValid()) {
                     finishDraggingTo2();
                     dragger = null;
                  } else {
                     prepareForDragging();
                     dragger2.continueDrag(e);
                     dragger = dragger2;
                  }
               }
            }
            e.consume();
         }
      }
   }

   protected void prepareForDragging2() {
   }

   protected void finishDraggingTo2() {
   }

   @Override
   protected void finishDraggingTo(int location) {
      finishDraggingTo2();
      super.finishDraggingTo(location);
   }

   protected class MyDragController extends DragController {

      public MyDragController(MouseEvent e) {
         super(e);
      }

      @Override
      protected boolean isValid() {
         return super.isValid();
      }

      @Override
      protected void continueDrag(MouseEvent e) {
         super.continueDrag(e);
      }
   }

   protected class MyVerticalDragController extends VerticalDragController {

      public MyVerticalDragController(MouseEvent e) {
         super(e);
      }

      @Override
      protected boolean isValid() {
         return super.isValid();
      }

      @Override
      protected void continueDrag(MouseEvent e) {
         super.continueDrag(e);
      }
   }
}
