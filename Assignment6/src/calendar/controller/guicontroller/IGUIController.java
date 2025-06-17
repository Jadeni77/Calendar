package calendar.controller.guicontroller;

import calendar.controller.ICalendarController;

public interface IGUIController extends ICalendarController {

  void handleAddEvent();

  void handleEditEvent();

  void handleCreateCalendar();

  void handleSwitchCalendar();

  void handleRefreshSchedule();

  void handlePrevMonth();

  void handleNextMonth();
}
