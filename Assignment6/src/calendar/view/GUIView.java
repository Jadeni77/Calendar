package calendar.view;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import calendar.model.CalendarObserver;
import calendar.model.event.Event;

public class GUIView extends JFrame implements IGUIView {
  private final DefaultTableModel eventTableModel;
  private JTextField startDateField; //the text field for start date indicate the date for the calendar
  private JComboBox<String> calendarComboBox; //the dropdown for calendars
  private JLabel statusLabel; //label to show the status of the selected date and time
  private JTable eventTable;

  public GUIView() {
    setTitle("Calendar Application");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initializeUI();
    setLocationRelativeTo(null);
  }

  //this method initializes the UI components
  //add all the buttons and components
  private void initializeUI() {}

//  public void setListener(ActionListener l) {
//    // component.addActionListener(l);
//    // ...
//    // ...
//  }
//
//  private void switchCalendar() {
//
//  }

//  public List<String> showDialogueAddEvent() {
//    // Show a dialog to add an event
//    String subject = JOptionPane.showInputDialog(this, "Enter event subject:");
//    String start = JOptionPane.showInputDialog(this, "Enter start time (YYYY-MM-DD HH:MM):");
//    String end = JOptionPane.showInputDialog(this, "Enter end time (YYYY-MM-DD HH:MM):");
//
//    if (subject != null && start != null && end != null) {
//      // Call the controller to handle adding the event
//      // controller.handleAddEvent(subject, start, end);
//    }
//
//  }

  @Override
  public void showStatusOnDayTime(boolean status) {
    //dont need it in GUI
  }

  @Override
  public void addCalendar(String name) {
    calendarComboBox.addItem(name);
  }

  @Override
  public Event getSelectedEvent() {
    return null;
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
  public List<String> showEditEventDialog(Event event) {
    return List.of();
  }

  @Override
  public List<String> showCreateCalendarDialog() {
    return List.of();
  }

  @Override
  public String getStartDate() {
    return "";
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
}
