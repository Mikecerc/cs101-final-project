package com.michael;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Task {
    public enum Priority {
        HIGH,
        MEDIUM,
        LOW
    }

    // Reference to the TaskList that this Task belongs to
    private final TaskList taskList;


    public boolean isComplete;
    public String taskName;
    public String taskDescription;
    public String taskDueDate;
    public Priority taskPriority;

    public Task(TaskList taskList, String taskName, String taskDescription, String taskDueDate, Priority taskPriority) {
        this.taskList = taskList;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDueDate = taskDueDate;
        this.taskPriority = taskPriority;
        this.isComplete = false;
    }
    
    /*
     * Create a JPanel that represents this Task
     * @return JPanel
     */
    public JPanel createUIElement() {
        // Create a new JPanel with GridBagLayout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set common constraints for all components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        // Create and add the toggle complete button
        JButton toggleCompleteButton = new JButton(isComplete ? "✔" : "✘");
        toggleCompleteButton.addActionListener(e -> {
            isComplete = !isComplete;
            toggleCompleteButton.setText(isComplete ? "✔" : "✘");
        });
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        panel.add(toggleCompleteButton, gbc);

        // Create and add the task name label
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JLabel taskNameLabel = new JLabel(taskName);
        taskNameLabel.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
        taskNameLabel.setPreferredSize(new Dimension(100, taskNameLabel.getPreferredSize().height));
        panel.add(taskNameLabel, gbc);

        // Create and add the task description area
        gbc.gridx = 2;
        gbc.weightx = 2.0;
        JTextArea taskDescriptionArea = new JTextArea(taskDescription);
        taskDescriptionArea.setLineWrap(true);
        taskDescriptionArea.setWrapStyleWord(true);
        taskDescriptionArea.setOpaque(false);
        taskDescriptionArea.setEditable(false);
        taskDescriptionArea.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
        taskDescriptionArea.setPreferredSize(new Dimension(200, 50));
        panel.add(taskDescriptionArea, gbc);

        // Create and add the task due date label
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        JLabel taskDueDateLabel = new JLabel(taskDueDate);
        taskDueDateLabel.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
        taskDueDateLabel.setPreferredSize(new Dimension(100, taskDueDateLabel.getPreferredSize().height));
        panel.add(taskDueDateLabel, gbc);

        // Create and add the task priority label
        gbc.gridx = 4;
        gbc.weightx = 1.0;
        JLabel taskPriorityLabel = new JLabel(taskPriority.toString());
        taskPriorityLabel.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
        taskPriorityLabel.setPreferredSize(new Dimension(100, taskPriorityLabel.getPreferredSize().height));
        panel.add(taskPriorityLabel, gbc);

        // Create and add the delete button
        gbc.gridx = 5;
        gbc.weightx = 0.5;
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            taskList.removeTask(this.taskName);
            App.getInstance().refreshGUI();
        });
        panel.add(deleteButton, gbc);

        return panel;
    }
}
