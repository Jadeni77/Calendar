package calendar.view;

import java.awt.event.ActionListener;
import java.util.List;

import calendar.model.event.Event;

public interface IGUIView extends ICalendarView {

  void setAddEventButtonListener(ActionListener listener);

  void setEditEventButtonListener(ActionListener listener);

  void setCreateCalendarButtonListener(ActionListener listener);

  void setSwitchCalendarComboBoxListener(ActionListener listener);

  void setRefreshButtonListener(ActionListener listener);

  void addCalendar(String name);

  Event getSelectedEvent();

  String getSelectedCalendar();

  List<String> showAddEventDialog();

  List<String> showEditEventDialog(Event event);

  List<String> showCreateCalendarDialog();

  String getStartDate();

}
