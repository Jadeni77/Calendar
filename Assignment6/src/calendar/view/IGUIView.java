package calendar.view;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import calendar.model.event.Event;

public interface IGUIView extends ICalendarView {

  void setAddEventButtonListener(ActionListener listener);

  void setEditEventButtonListener(ActionListener listener);

  void setCreateCalendarButtonListener(ActionListener listener);

  void setSwitchCalendarListener(ActionListener listener);

  void setRefreshButtonListener(ActionListener listener);

  void setPrevMonthListener(ActionListener listener);

  void setNextMonthListener(ActionListener listener);

  void addCalendar(String name);

  Event getSelectedEvent();

  String getSelectedCalendar();

  List<String> showAddEventDialog();

  List<String> showEditEventDialog(Event event);

  List<String> showCreateCalendarDialog();

  void changeMonth(int direction);

  void refreshEvents(List<Event> events);

  void switchToScheduleView();

  String getStartDate();

  void setStartDate(LocalDate startDate);

  void showEventsForDate(LocalDate date);

}
