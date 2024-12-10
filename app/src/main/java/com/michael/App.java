/*
 * This source file was generated by the Gradle 'init' task
 */
package com.michael;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class App extends JFrame {
    //static reference to the instance of the App
    private static App instance;


    private TaskList taskList;
    private JList<String> taskListView;
    private JPanel centerPanel;
    private JPanel titlePanel;
    private final JPanel topbar;


    public static void main(String[] args) {
        // create and show the GUI on the event-dispatch thread
        // this is the only thread that should modify the GUI
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }

    public App() {
        // set the static instance reference
        instance = this;

        // set up the main window
        setTitle("Task Manager");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create task list
        taskList = new TaskList();

        // Create task list view
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(taskList.generateTaskListGUI(), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(taskListView), BorderLayout.CENTER);

        // Create toolbar
        JToolBar toolBar = new JToolBar();
        JButton addTaskButton = new JButton("Add Task");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        //create title panel
        titlePanel = new JPanel();
        titlePanel.add(new JLabel("Task Manager"));

        // event listeners
        addTaskButton.addActionListener(e -> {
            String taskName = JOptionPane.showInputDialog("Enter task name:");
            if (taskName == null || taskName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String taskDescription = JOptionPane.showInputDialog("Enter task description:");
            if (taskDescription == null || taskDescription.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task description is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String taskDueDate = JOptionPane.showInputDialog("Enter task due date:");
            if (taskDueDate == null || taskDueDate.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task due date is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] priorities = { "HIGH", "MEDIUM", "LOW" };
            String taskPriority = (String) JOptionPane.showInputDialog(null, "Select task priority:", "Task Priority",
                    JOptionPane.QUESTION_MESSAGE, null, priorities, "MEDIUM");
            if (taskPriority == null) {
                JOptionPane.showMessageDialog(this, "Task priority is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            taskList.addTask(new Task(taskList, taskName, taskDescription, taskDueDate, Task.Priority.valueOf(taskPriority)));
            refreshGUI();
        });
        saveButton.addActionListener(e -> {
            taskList.saveTasks();
        });
        loadButton.addActionListener(e -> {
            taskList.loadTasks();
            refreshGUI();
        });

        toolBar.add(addTaskButton);
        toolBar.add(saveButton);
        toolBar.add(loadButton);

        // Layout setup
        setLayout(new BorderLayout());
        topbar = new JPanel();
        topbar.add(titlePanel);
        topbar.add(toolBar);
        topbar.setLayout(new BoxLayout(topbar, BoxLayout.Y_AXIS));
        add(topbar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    public void refreshGUI() {
        // Remove the current task list view and replace it with a new one
        remove(centerPanel);
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(taskList.generateTaskListGUI(), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(taskListView), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    public static App getInstance() {
        return instance;
    }
}