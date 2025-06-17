package calendar.view;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import calendar.model.event.Event;

public class MockGUIView implements IGUIView {
  @Override
  public void setListeners(ActionListener listener) {

  }

  @Override
  public void addCalendar(String name) {

  }

  @Override
  public String getSelectedEventSubject() {
    return "";
  }

  @Override
  public String getSelectedEventStart() {
    return "";
  }

  @Override
  public String getSelectedEventEnd() {
    return "";
  }

  @Override
  public String getSelectedCalendar() {
    return "";
  }

  @Override
  public List<String> showAddEventDialog() {
    return List.of();
  }

  @Override
  public List<String> showEditEventDialog() {
    return List.of();
  }

  @Override
  public List<String> showCreateCalendarDialog() {
    return List.of();
  }

  @Override
  public void changeMonth(int direction) {

  }

  @Override
  public void refreshEvents(List<Event> events) {

  }

  @Override
  public void switchToScheduleView() {

  }

  @Override
  public String getStartDate() {
    return "";
  }

  @Override
  public void setStartDate(LocalDate startDate) {

  }

  @Override
  public void showEventsForDate(LocalDate date) {

  }

  @Override
  public void displayMessage(String message) {

  }

  @Override
  public void displayException(Exception e) {

  }

  @Override
  public void printEvents(List<Event> events) {

  }

  @Override
  public void showStatusOnDayTime(boolean status) {

  }
}
