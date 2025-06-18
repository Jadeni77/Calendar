package calendar.controller.guicontroller;

import java.util.List;

import calendar.model.event.Event;


public class MockGUIController implements IGUIController, CalendarObserver {
  public StringBuilder log;

  public MockGUIController(StringBuilder log) {
    this.log = log;
  }

  public MockGUIController() {
    log = new StringBuilder();
  }

  @Override
  public void handleAddEvent(String subject, String name, String description,
                             String location, String start, String end) {
    log.append("Add Event\n");
  }

  @Override
  public void handleEditEvent(String editedProperty, String newValue) {
    log.append("Edit Event\n");
  }

  @Override
  public void handleCreateCalendar(String calendarName, String timeZone) {
    log.append("Create Calendar\n");
  }

  @Override
  public void handleSwitchCalendar() {
    log.append("Switch Calendar\n");
  }

  @Override
  public void handleRefreshSchedule() {
    log.append("Refresh Schedule\n");
  }

  @Override
  public void handlePrevMonth() {
    log.append("Previous Month\n");
  }

  @Override
  public void handleNextMonth() {
    log.append("Next Month\n");
  }

  @Override
  public void start() {
    log.append("Start\n");
  }

  @Override
  public void eventsUpdated(List<Event> events) {
    log.append("Events updated\n");
  }
}
