package calendar.controller.guiadapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface ViewEventListenerAdapter extends ActionListener {

  /**
   * Invoked when an action occurs.
   * @param e the event to be processed
   */
  void actionPerformed(ActionEvent e);

}
