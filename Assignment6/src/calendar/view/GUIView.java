package calendar.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import calendar.model.event.Event;

public class GUIView extends JFrame implements IGUIView {
  private final DefaultTableModel eventTableModel;
  private final JComboBox<String> calendarComboBox; //the dropdown for calendars
  private final JLabel statusLabel; //label to show the status of the selected date and time
  private final JTable eventTable;
  private final JTabbedPane tabbedPane;
  private final JPanel monthPanel;
  private final JPanel schedulePanel;
  private final JPanel controlPanel;
  private final JLabel monthLabel; //from starter
  private YearMonth currentMonth; //from starter
  private JTextField startDateField; //the text field for start date indicate the date for the calendar
  //the startDateField does nothing right now besides showing the current date

  private JButton addEventButton;
  private JButton editEventButton;
  private JButton createCalendarButton;
  private JButton refreshButton;
  private JButton prevMonthButton;
  private JButton nextMonthButton;

  private Map<LocalDate, List<String>> events;



  //  private JButton addEventButton;
//  private JButton createCalendarButton;
//  private JButton editEventButton;
//  private JButton refreshButton;


  public GUIView() {
    this.eventTableModel = new DefaultTableModel();
    this.eventTable = new JTable(eventTableModel);
    this.tabbedPane = new JTabbedPane();
    this.monthPanel = new JPanel(new BorderLayout());
    this.schedulePanel = new JPanel(new BorderLayout());
    this.controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    this.currentMonth = YearMonth.now();
    this.monthLabel = new JLabel("", SwingConstants.CENTER);
    this.statusLabel = new JLabel("Ready");
    this.calendarComboBox = new JComboBox<>();

    this.events = new HashMap<>();


    setTitle("Calendar Application");
    setSize(1000, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initializeUI();
    setLocationRelativeTo(null);
//
//    //remove auto highlighting of the text field
//    startDateField.setFocusable(false);

  }

  /**
   * Initializes the user interface components of the calendar application.
   * This method sets up the main panel, control panel, event table, and other UI elements.
   */
  private void initializeUI() {
    setLayout(new BorderLayout());

    tabbedPane.addTab("Schedule View", schedulePanel);
    tabbedPane.addTab("Month View", monthPanel);
    add(tabbedPane, BorderLayout.CENTER);

    initializeScheduleView();

    initializeMonthView();

    initializeControlPanel();

    add(statusLabel, BorderLayout.SOUTH);


//    JPanel mainPanel = new JPanel(new BorderLayout());
//
//    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//
//    //Date Control
//    controlPanel.add(new JLabel("Start Date (yyyy-MM-dd):"));
//    startDateField = new JTextField(10);
//    startDateField.setText(LocalDate.now().toString());
//    controlPanel.add(startDateField);
//
//    refreshButton = new JButton("Show Schedule");
//    refreshButton.setActionCommand("refreshScheduleButton");
//    controlPanel.add(refreshButton);
//
//    //Action buttons
//    addEventButton = new JButton("Add Event");
//    addEventButton.setActionCommand("addEventButton");
//    controlPanel.add(addEventButton);
//
//    createCalendarButton = new JButton("Create Calendar");
//    createCalendarButton.setActionCommand("createCalendarButton");
//    controlPanel.add(createCalendarButton);
//
//    //Calendar Switching Dropdown
//    controlPanel.add(new JLabel("Calendar:"));
//    calendarComboBox = new JComboBox<>();
//    calendarComboBox.addItem("Default");
//    calendarComboBox.setActionCommand("calendarComboBox");
//    controlPanel.add(calendarComboBox);
//
//    mainPanel.add(controlPanel, BorderLayout.NORTH);
//
//    //Event Table
//    eventTableModel.addColumn("Subject");
//    eventTableModel.addColumn("Start Time");
//    eventTableModel.addColumn("End Time");
//    eventTable = new JTable(eventTableModel);
//    eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//    JScrollPane scrollPane = new JScrollPane(eventTable); //scrolling
//    mainPanel.add(scrollPane, BorderLayout.CENTER);
//
//    //Edit Event Button
//    editEventButton = new JButton("Edit Event");
//    editEventButton.setActionCommand("editEventButton");
//    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//    controlPanel.add(editEventButton);
//    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//    //Status Label
//    statusLabel = new JLabel("Ready");
//    mainPanel.add(statusLabel, BorderLayout.SOUTH);
//
//    add(mainPanel);

  }

  private void initializeControlPanel() {
    //calendar selection dropdown
    controlPanel.add(new JLabel("Calendar:"));
    calendarComboBox.addItem("Default");
    controlPanel.add(calendarComboBox);

    //date control
    controlPanel.add(new JLabel("Start Date:"));
    startDateField = new JTextField(LocalDate.now().toString(), 10);
    controlPanel.add(startDateField);

    //Action buttons
     addEventButton = new JButton("Add Event");
    addEventButton.setActionCommand("addEventButton");
    controlPanel.add(addEventButton);

    //create calendar button
     createCalendarButton = new JButton("Create Calendar");
    createCalendarButton.setActionCommand("createCalendarButton");
    controlPanel.add(createCalendarButton);

     refreshButton = new JButton("Refresh");
    refreshButton.setActionCommand("refreshScheduleButton");
    controlPanel.add(refreshButton);

    add(controlPanel, BorderLayout.NORTH);
  }

  //from previous version
  private void initializeScheduleView() {
    eventTableModel.addColumn("Subject");
    eventTableModel.addColumn("Start Time");
    eventTableModel.addColumn("End Time");

    JScrollPane scrollPane = new JScrollPane(eventTable);
    schedulePanel.add(scrollPane, BorderLayout.CENTER);

    //edit button
     editEventButton = new JButton("Edit Event");
    editEventButton.setActionCommand("editEventButton");
    schedulePanel.add(editEventButton, BorderLayout.SOUTH);
  }

  //from starter
  private void initializeMonthView() {
    JPanel navigatePanel = new JPanel(new BorderLayout());
     prevMonthButton = new JButton("<");
     nextMonthButton = new JButton(">");
    prevMonthButton.setActionCommand("prevMonthButton");
    nextMonthButton.setActionCommand("nextMonthButton");

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(prevMonthButton);
    buttonPanel.add(nextMonthButton);

    navigatePanel.add(buttonPanel, BorderLayout.WEST);
    navigatePanel.add(monthLabel, BorderLayout.CENTER);
    monthPanel.add(navigatePanel, BorderLayout.NORTH);

    updateMonthView();
  }

  private void updateMonthView() {
    //keep the navigation buttons and month label
    if (monthPanel.getComponentCount() > 1) {
      monthPanel.remove(1);
    }

    //create calendar grid
    JPanel gridPanel = new JPanel(new GridLayout(0, 7, 5, 5));
    gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    //set month label
    monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

    //add day headers
    for (DayOfWeek day : DayOfWeek.values()) {
      //mon tue wed thu fri sat sun
      JLabel label = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
      label.setHorizontalAlignment(SwingConstants.CENTER);
      gridPanel.add(label);
    }


    //fill in days
    int dayOfWeekValue = currentMonth.atDay(1).getDayOfWeek().getValue();
    for (int i = 1; i < dayOfWeekValue; i++) {
      gridPanel.add(new JLabel("")); //empty labels for leading days
    }

    //from starter
    //add day buttons
    for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
      LocalDate date = currentMonth.atDay(day);
      JButton dayButton = new JButton(String.valueOf(day));
      dayButton.addActionListener(e -> showEvents(date));
      gridPanel.add(dayButton);
    }

    monthPanel.add(gridPanel, BorderLayout.CENTER);
    monthPanel.revalidate();
    monthPanel.repaint();

  }

  //exactly from the starter
  private void showEvents(LocalDate date) {
    List<String> dayEvents = events.getOrDefault(date, new ArrayList<>());
    String eventList = dayEvents.isEmpty() ? "No events" : String.join("\n", dayEvents);
    String newEvent = JOptionPane.showInputDialog(monthPanel, "Events on " + date + ":\n" + eventList + "\n\nAdd new event:");
    if (newEvent != null && !newEvent.trim().isEmpty()) {
      dayEvents.add(newEvent);
      events.put(date, dayEvents);
    }
  }


  //fron starter
  @Override
  public void changeMonth(int direction) {
    currentMonth = currentMonth.plusMonths(direction);
    updateMonthView();
  }


  @Override
  public void setAddEventButtonListener(ActionListener listener) {
    // Set the listener for the buttons and combo box
    addEventButton.addActionListener(listener);
  }

  @Override
  public void setCreateCalendarButtonListener(ActionListener listener) {
    createCalendarButton.addActionListener(listener);
  }

  @Override
  public void setEditEventButtonListener(ActionListener listener) {
    editEventButton.addActionListener(listener);
  }

  @Override
  public void setRefreshButtonListener(ActionListener listener) {
    refreshButton.addActionListener(listener);
  }

  @Override
  public void setSwitchCalendarListener(ActionListener listener) {
    calendarComboBox.addActionListener(listener);
  }

  @Override
  public void setPrevMonthListener(ActionListener listener) {
    prevMonthButton.addActionListener(listener);
  }

  @Override
  public void setNextMonthListener(ActionListener listener) {
    nextMonthButton.addActionListener(listener);
  }

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
    return (String) calendarComboBox.getSelectedItem();
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
