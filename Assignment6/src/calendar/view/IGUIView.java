package calendar.view;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import calendar.model.event.Event;

public interface IGUIView extends ICalendarView {

  void setListeners(ActionListener listener);

  void addCalendar(String name);

  String getSelectedEventSubject();

  String getSelectedEventStart();

  String getSelectedEventEnd();

  String getSelectedCalendar();

  List<String> showAddEventDialog();

  List<String> showEditEventDialog();

  List<String> showCreateCalendarDialog();

  void changeMonth(int direction);

  void switchToScheduleView();

  String getStartDate();

  void setStartDate(LocalDate startDate);

  void showEventsForDate(LocalDate date);

}
