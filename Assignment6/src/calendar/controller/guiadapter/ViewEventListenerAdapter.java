package calendar.controller.guiadapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ViewEventListenerAdapter represents an adapter for a Swing GUI view and its controller,
 * allowing the view to relay any ActionEvents triggered by button presses to the controller
 * for processing.
 */
public interface ViewEventListenerAdapter extends ActionListener {

  /**
   * Invoked when an action occurs.
   * @param e the event to be processed
   */
  void actionPerformed(ActionEvent e);

}
