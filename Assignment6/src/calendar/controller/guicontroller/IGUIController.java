package calendar.controller.guicontroller;

import calendar.controller.ICalendarController;

public interface IGUIController extends ICalendarController {

  void handleAddEvent(String subject, String start, String end, String description,
                      String location, String status);

  void handleEditEvent(String editedProperty, String newValue);

  void handleCreateCalendar(String calendarName, String timeZone);

  void handleSwitchCalendar();

  void handleRefreshSchedule();

  void handlePrevMonth();

  void handleNextMonth();
}
