package calendar.controller.guiadapter;

import java.awt.event.ActionEvent;
import java.util.List;

import calendar.controller.guicontroller.IGUIController;
import calendar.view.IGUIView;

/**
 * An adapter class that implements the ViewEventListenerAdapter and ActionListener interfaces.
 * It acts as a bridge between the Swing UI (GUIView) and your controller (GUIController).
 * It listens to UI events (button clicks, etc.) and translates them into controller method calls.
 * It implements both ViewEventListener (custom events) and ActionListener (Swing events),
 * so you can wire it to both custom and standard UI events.
 */
public class EventListenerAdapter implements ViewEventListenerAdapter {
  private final IGUIController controller;
  private final IGUIView view;

  /**
   * Initializes an EventListenerAdapter with the given controller and model.
   * @param controller the given controller
   * @param view the given view
   */
  public EventListenerAdapter(IGUIController controller, IGUIView view) {
    this.controller = controller;
    this.view = view;
    this.view.setListeners(this);
  }

  /**
   * Invoked when any listeners from the view are triggered because of a button press.
   * @param e the ActionEvent to be processed
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String source = e.getActionCommand();

    switch (source) {
      case "addEventButton":
        List<String> eventsData = view.showAddEventDialog();
        if (eventsData != null && eventsData.size() == 6) {
          String subject = eventsData.get(0);
          String start = eventsData.get(1);
          String end = eventsData.get(2);
          String description = eventsData.get(3);
          String location = eventsData.get(4);
          String status = eventsData.get(5);
          controller.handleAddEvent(subject, start, end, description, location, status);
        }
        break;
      case "editEventButton":
        List<String> newData = view.showEditEventDialog();
        if (newData != null && newData.size() == 2) {
          String editedProperty = newData.get(0);
          String newValue = newData.get(1);
          controller.handleEditEvent(editedProperty, newValue);
        }
        break;
      case "createCalendarButton":
        List<String> calendarData = view.showCreateCalendarDialog();
        if (calendarData != null && calendarData.size() == 2) {
          String calendarName = calendarData.get(0);
          String timeZone = calendarData.get(1);
          controller.handleCreateCalendar(calendarName, timeZone);
        }
        break;
      case "switchCalendarButton":
        controller.handleSwitchCalendar();
        break;
      case "refreshScheduleButton":
        controller.handleRefreshSchedule();
        break;
      case "prevMonthButton":
        controller.handlePrevMonth();
        break;
      case "nextMonthButton":
        controller.handleNextMonth();
        break;
      default:
        view.displayMessage("Unknown action: " + source);
    }
  }
}
