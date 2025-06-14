package calendar.controller.guiadapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import calendar.controller.guicontroller.GUIController;
import calendar.controller.guicontroller.IGUIController;
import calendar.view.ICalendarView;

/**
 * An adapter class that implements the ViewEventListenerAdapter and ActionListener interfaces.
 * It acts as a bridge between the Swing UI (GUIView) and your controller (GUIController).
 * It listens to UI events (button clicks, etc.) and translates them into controller method calls.
 * It implements both ViewEventListener (custom events) and ActionListener (Swing events),
 * so you can wire it to both custom and standard UI events.
 */
public class EventListenerAdaptor implements ActionListener {
  private final IGUIController controller;

  public EventListenerAdaptor(IGUIController controller) {
    this.controller = controller;
    // this.guiVIew.setListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String source = e.getActionCommand();

    switch (source) {
      case "addEventButton":
        controller.handleAddEvent();
        break;
      case "editEventButton":
        controller.handleEditEvent();
        break;
      case "createCalendarButton":
        controller.handleCreateCalendar();
        break;
      case "switchCalendarButton":
        controller.handleSwitchCalendar();
        break;
      case "refreshScheduleButton":
        controller.handleRefreshSchedule();
        break;
      default:
        //guiView.displayMessage("Unknown action: " + source);
    }
  }
}
