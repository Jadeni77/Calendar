package calendar.view;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import calendar.model.event.Event;

/**
 * This class represents a mock GUIView class used to test other components which use or
 * work with a GUIView.
 */
public class MockGUIView implements IGUIView {
  public StringBuilder log;
  private String selectedEventSubject;
  private String selectedEventStart;
  private String selectedEventEnd;

  /**
   * Initializes a new MockGUIView with the given StringBuilder as its log.
   * @param log
   */
  public MockGUIView(StringBuilder log) {
    this.log = log;
  }

  /**
   * Initializes a new MockGUIView with the given StringBuilder as its log and the subject, start,
   * and end of a fake event to be selected within the mock in order to allow its methods to return
   * the proper information.
   * @param log
   * @param subject
   * @param start
   * @param end
   */
  public MockGUIView(StringBuilder log, String subject, String start, String end) {
    this.log = log;
    this.selectedEventSubject = subject;
    this.selectedEventStart = start;
    this.selectedEventEnd = end;
  }

  @Override
  public void setListeners(ActionListener listener) {
    log.append("Listener set\n");
  }

  @Override
  public void addCalendar(String name) {
    log.append("Calendar added: ").append(name).append("\n");
  }

  @Override
  public String getSelectedEventSubject() {
    log.append("Selected event subject queried\n");
    return selectedEventSubject;
  }

  @Override
  public String getSelectedEventStart() {
    log.append("Selected event start date/time queried\n");
    return selectedEventStart;
  }

  @Override
  public String getSelectedEventEnd() {
    log.append("Selected event end date/time queried\n");
    return selectedEventEnd;
  }

  @Override
  public String getSelectedCalendar() {
    log.append("Selected event calendar queried\n");
    return "selected calendar";
  }

  @Override
  public List<String> showAddEventDialog() {
    log.append("Add Event dialog shown\n");
    return new ArrayList<>(Arrays.asList("add event subject", "add event start", "add event end",
            "add event description", "add event location", "add event status"));
  }

  @Override
  public List<String> showEditEventDialog() {
    log.append("Edit Event dialog shown\n");
    return new ArrayList<>(Arrays.asList("edit event property", "edit event new value"));
  }

  @Override
  public List<String> showCreateCalendarDialog() {
    log.append("Create Calendar dialog shown\n");
    return new ArrayList<>(Arrays.asList("create calendar name", "create calendar timezone"));
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.now().format(formatter);
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
    log.append("Printing events\n");
  }

  @Override
  public void showStatusOnDayTime(boolean status) {
    log.append("Status on Day/Time: ").append(status).append("\n");
  }
}
