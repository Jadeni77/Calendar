package calendar.view;

import java.util.List;

import calendar.model.event.Event;

public interface IGUIView extends ICalendarView {

  void addCalendar(String name);

  Event getSelectedEvent();

  String getSelectedCalendar();

  List<String> showAddEventDialog();

  List<String> showEditEventDialog(Event event);

  List<String> showCreateCalendarDialog();

  String getStartDate();

}
