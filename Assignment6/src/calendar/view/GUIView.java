package calendar.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

  private DateTimeFormatter formatter;

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

    this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

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
    eventTable.setRowSelectionAllowed(true);
    eventTable.setColumnSelectionAllowed(false);
    eventTable.setDefaultEditor(Object.class, null);
    eventTable.getTableHeader().setReorderingAllowed(false);

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
  public String getSelectedEventSubject() {
    int selectedRow = eventTable.getSelectedRow();
    if (selectedRow == -1) {
      throw new IllegalArgumentException("No event selected!");
    }
    return (String) eventTable.getValueAt(selectedRow, 0);
  }

  @Override
  public String getSelectedEventStart() {
    int selectedRow = eventTable.getSelectedRow();
    if (selectedRow == -1) {
      throw new IllegalArgumentException("No event selected!");
    }
    return (String) eventTable.getValueAt(selectedRow, 1);
  }

  @Override
  public String getSelectedEventEnd() {
    int selectedRow = eventTable.getSelectedRow();
    if (selectedRow == -1) {
      throw new IllegalArgumentException("No event selected!");
    }
    return (String) eventTable.getValueAt(selectedRow, 2);
  }

  @Override
  public String getSelectedCalendar() {
    return (String) calendarComboBox.getSelectedItem();
  }

  @Override
  public List<String> showAddEventDialog() {
    List<String> result = new ArrayList<>();

    JDialog dialog = new JDialog(this, "Input Dialog", true);
    dialog.setLayout(new BorderLayout());
    dialog.setTitle("Add Event");
    dialog.setResizable(false);

    JPanel popup = new JPanel();
    popup.setLayout(new BoxLayout(popup, BoxLayout.Y_AXIS));

    JPanel textPanel1 = new JPanel();
    JTextField textField1 = new JTextField(20);
    JLabel label1 = new JLabel("Name: ");
    textPanel1.add(label1);
    textPanel1.add(textField1);

    JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel dateTimeLabel = new JLabel("Start Date/Time: ");
    SpinnerDateModel dateTimeModel = new SpinnerDateModel();
    JSpinner dateSpinner = new JSpinner(dateTimeModel);
    dateTimePanel.add(dateTimeLabel);
    dateTimePanel.add(dateSpinner);

    JPanel dateTimePanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel dateTimeLabel2 = new JLabel("End Date/Time: ");
    Date plusOneMin = new Date(new Date().getTime() + 60000);
    SpinnerDateModel dateTimeModel2 = new SpinnerDateModel(
            plusOneMin, null, null, Calendar.MINUTE);
    JSpinner dateSpinner2 = new JSpinner(dateTimeModel2);
    dateTimePanel2.add(dateTimeLabel2);
    dateTimePanel2.add(dateSpinner2);

    JButton okButton = new JButton("OK");
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(okButton);

    popup.add(textPanel1);
    popup.add(dateTimePanel);
    popup.add(dateTimePanel2);

    dialog.add(popup);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    okButton.addActionListener(e -> {
      String name = textField1.getText();
      Date startDate = (Date) dateSpinner.getValue();
      Date endDate = (Date) dateSpinner2.getValue();
      LocalDateTime startLDT = startDate.toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
      LocalDateTime endLDT = endDate.toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
      String startDateStr = this.formatter.format(startLDT);
      String endDateStr = this.formatter.format(endLDT);

      if (name.trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Please enter a name!");
        return;
      } if (startDateStr.trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Please enter a start date!");
        return;
      } if (endDateStr.trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Please enter a end date!");
        return;
      }

      result.add(name);
      result.add(startDateStr);
      result.add(endDateStr);
      dialog.dispose();
    });

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);

    return result;
  }

  @Override
  public List<String> showEditEventDialog() {
    ArrayList<String> result = new ArrayList<>();

    ArrayList<String> selectedCard = new ArrayList<>(List.of("Subject"));

    JDialog dialog = new JDialog(this, "Input Dialog", true);
    dialog.setLayout(new BorderLayout());
    dialog.setTitle("Edit Event");
    dialog.setResizable(false);

    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);

    JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel subjectLabel = new JLabel("Enter a new subject: ");
    JTextField textField1 = new JTextField(20);
    subjectPanel.add(subjectLabel);
    subjectPanel.add(textField1);
    JPanel subjectWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    subjectWrapper.add(subjectPanel);

    JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel startLabel = new JLabel("Enter a new start date/time: ");
    SpinnerDateModel dateTimeModel = new SpinnerDateModel();
    JSpinner dateSpinner = new JSpinner(dateTimeModel);
    startPanel.add(startLabel);
    startPanel.add(dateSpinner);
    JPanel startWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    startWrapper.add(startPanel);

    JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel endLabel = new JLabel("Enter a new end date/time: ");
    JSpinner dateSpinner2 = new JSpinner(dateTimeModel);
    endPanel.add(endLabel);
    endPanel.add(dateSpinner2);
    JPanel endWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    endWrapper.add(endPanel);

    cardPanel.add(subjectWrapper, "Subject");
    cardPanel.add(startWrapper, "Start");
    cardPanel.add(endWrapper, "End");

    String[] options = {"Subject", "Start", "End"};
    JComboBox<String> cardSelector = new JComboBox<>(options);
    cardSelector.addActionListener(e -> {
      String selected = (String) cardSelector.getSelectedItem();
      selectedCard.set(0, selected);
      cardLayout.show(cardPanel, selected);
    });

    JButton okButton = new JButton("OK");
    okButton.addActionListener(e -> {
      switch (selectedCard.get(0)) {
        case "Subject":
          result.add("subject");
          result.add(textField1.getText());
          break;
        case "Start":
          result.add("start");
          Date startDate = (Date) dateSpinner.getValue();
          LocalDateTime startLDT = startDate.toInstant()
                  .atZone(ZoneId.systemDefault())
                  .toLocalDateTime();
          String startDateStr = this.formatter.format(startLDT);
          result.add(startDateStr);
          break;
        case "End":
          result.add("end");
          Date endDate = (Date) dateSpinner2.getValue();
          LocalDateTime endLDT = endDate.toInstant()
                  .atZone(ZoneId.systemDefault())
                  .toLocalDateTime();
          String endDateStr = this.formatter.format(endLDT);
          result.add(endDateStr);
          break;
      }
      dialog.dispose();
    });

    JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel selectorLabel = new JLabel("Choose a property to edit: ");
    selectorPanel.add(selectorLabel);
    selectorPanel.add(cardSelector);

    JPanel okButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    okButtonPanel.add(okButton);

    // Assemble dialog
    dialog.add(cardPanel, BorderLayout.CENTER);
    dialog.add(selectorPanel, BorderLayout.NORTH);
    dialog.add(okButtonPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);

    return result;
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
  public void refreshEvents(List<Event> events) {
    this.eventTableModel.setRowCount(0);
    for (Event e : events) {
      eventTableModel.addRow(new Object[]{
              e.getSubject(),
              e.getStartDateTime().format(formatter),
              e.getEndDateTime().format(formatter),
      });
    }
  }

  @Override
  public void printEvents(List<Event> events) {

  }

  private void updateStatusLabel(String message) {
    statusLabel.setText(message);
  }
}
