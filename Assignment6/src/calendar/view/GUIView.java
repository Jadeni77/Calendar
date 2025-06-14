package calendar.view;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import calendar.model.event.Event;

public class GUIView extends JFrame implements IGUIView {
  private final DefaultTableModel eventTableModel;
  private JTextField startDateField; //the text field for start date indicate the date for the calendar
  private JComboBox<String> calendarComboBox; //the dropdown for calendars
  private JLabel statusLabel; //label to show the status of the selected date and time
  private JTable eventTable;

  public GUIView() {
    this.eventTableModel = new DefaultTableModel();
    setTitle("Calendar Application");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initializeUI();
    setLocationRelativeTo(null);
  }

  /**
   * Initializes the user interface components of the calendar application.
   * This method sets up the main panel, control panel, event table, and other UI elements.
   */
  private void initializeUI() {
    JPanel mainPanel = new JPanel(new BorderLayout());

    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    //Date Control
    controlPanel.add(new JLabel("Start Date (yyyy-MM-dd):"));
    startDateField = new JTextField(10);
    startDateField.setText(LocalDate.now().toString());
    controlPanel.add(startDateField);

    JButton refreshButton = new JButton("Show Schedule");
    refreshButton.setActionCommand("refreshScheduleButton");
    controlPanel.add(refreshButton);

    //Action buttons
    JButton addEventButton = new JButton("Add Event");
    addEventButton.setActionCommand("addEventButton");
    controlPanel.add(addEventButton);

    JButton createCalendarButton = new JButton("Create Calendar");
    createCalendarButton.setActionCommand("createCalendarButton");
    controlPanel.add(createCalendarButton);

    //Calendar Switching Dropdown
    controlPanel.add(new JLabel("Calendar:"));
    calendarComboBox = new JComboBox<>();
    calendarComboBox.addItem("Default");
    calendarComboBox.setActionCommand("calendarComboBox");
    controlPanel.add(calendarComboBox);

    mainPanel.add(controlPanel, BorderLayout.NORTH);

    //Event Table
    eventTableModel.addColumn("Subject");
    eventTableModel.addColumn("Start Time");
    eventTableModel.addColumn("End Time");
    eventTable = new JTable(eventTableModel);
    eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(eventTable); //scrolling
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    //Edit Event Button
    JButton editEventButton = new JButton("Edit Event");
    editEventButton.setActionCommand("editEventButton");
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    controlPanel.add(editEventButton);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    //Status Label
    statusLabel = new JLabel("Ready");
    mainPanel.add(statusLabel, BorderLayout.SOUTH);

    add(mainPanel);

  }

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
    //add a calendar to the dropdown
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
    return startDateField.getText();
  }


  @Override
  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
    updateStatusLabel(message);

  }

  @Override
  public void displayException(Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage());
    updateStatusLabel("Error: " + e.getMessage());

  }

  @Override
  public void printEvents(List<Event> events) {

  }

  private void updateStatusLabel(String message) {
    statusLabel.setText(message);
  }
}
