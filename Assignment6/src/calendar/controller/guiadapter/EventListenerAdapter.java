package calendar.controller.guiadapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

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

  public EventListenerAdapter(IGUIController controller, IGUIView view) {
    this.controller = controller;
    this.view = view;
    this.view.setListeners(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String source = e.getActionCommand();

    if (e.getSource() instanceof JComboBox) {
      controller.handleSwitchCalendar();
      return;
    }

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
