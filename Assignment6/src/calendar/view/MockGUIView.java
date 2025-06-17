package calendar.view;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import calendar.model.event.Event;

public class MockGUIView implements IGUIView {
  public StringBuilder log;

  public MockGUIView(StringBuilder log) {
    this.log = log;
  }

  public MockGUIView() {
    log = new StringBuilder();
  }

  @Override
  public void setListeners(ActionListener listener) {
    log.append("Listener set: ").append(listener).append("\n");
  }

  @Override
  public void addCalendar(String name) {
    log.append("Calendar added: ").append(name).append("\n");
  }

  @Override
  public String getSelectedEventSubject() {
    log.append("Selected event subject queried\n");
    return "selected event subject";
  }

  @Override
  public String getSelectedEventStart() {
    log.append("Selected event start date/time queried\n");
    return "selected event start";
  }

  @Override
  public String getSelectedEventEnd() {
    log.append("Selected event end date/time queried\n");
    return "selected event end";
  }

  @Override
  public String getSelectedCalendar() {
    log.append("Selected event calendar queried\n");
    return "selected calendar";
  }

  @Override
  public List<String> showAddEventDialog() {
    log.append("Add Event dialog shown\n");
    return List.of(); // TODO change
  }

  @Override
  public List<String> showEditEventDialog() {
    log.append("Edit Event dialog shown\n");
    return List.of(); // TODO change
  }

  @Override
  public List<String> showCreateCalendarDialog() {
    log.append("Create Calendar dialog shown\n");
    return List.of(); // TODO change
  }

  @Override
  public void changeMonth(int direction) {
    log.append("Months changed by: \n").append(direction).append("\n");
  }

  @Override
  public void switchToScheduleView() {
    log.append("Switch to Schedule View\n");
  }

  @Override
  public String getStartDate() {
    log.append("Start date queried\n");
    return "start date";
  }

  @Override
  public void setStartDate(LocalDate startDate) {
    log.append("Start date set to: \n").append(startDate).append("\n");
  }

  @Override
  public void showEventsForDate(LocalDate date) {
    log.append("Events shown for date: \n").append(date).append("\n");
  }

  @Override
  public void displayMessage(String message) {
    log.append("Message displayed: ").append(message).append("\n");
  }

  @Override
  public void displayException(Exception e) {
    log.append("Message displayed: ").append(e.getMessage()).append("\n");
  }

  @Override
  public void printEvents(List<Event> events) {
    log.append("Printing events:\n");
    for (Event event : events) {
      log.append(event.toString()).append("\n");
    }
  }

  @Override
  public void showStatusOnDayTime(boolean status) {
    log.append("Status on Day/Time: ").append(status).append("\n");
  }
}
