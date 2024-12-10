package com.michael;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TaskList {
    private final HashMap<String,Task> tasks;
    public TaskList() {
        tasks = new HashMap<>();
    }

    public void addTask(Task task) {
        tasks.put(task.taskName, task);
    }

    public Task removeTask(String taskName) {
        if (!tasks.isEmpty()) {
            return tasks.remove(taskName);
        } else {
            return null;
        }
    }
    public class TaskData {
        public String taskName;
        public String taskDescription;
        public String taskDueDate;
        public Task.Priority taskPriority;
        public boolean isComplete;

        private TaskData(Task task) {
            this.taskName = task.taskName;
            this.taskDescription = task.taskDescription;
            this.taskDueDate = task.taskDueDate;
            this.taskPriority = task.taskPriority;
            this.isComplete = task.isComplete;
        }
    }

    public Collection<TaskData> getTasks() {
        //return tasks.values as a collection while removing the reference 

        return tasks.values().stream().map(task -> new TaskData(task)).collect(Collectors.toList());
    }

    /*
     * Saves the tasks to a file.
     * 
     */
    public void saveTasks() {
        Collection<TaskData> taskCollection = this.getTasks();
        //convert the tasks to json
        Gson gson = new Gson();
        String json = gson.toJson(taskCollection.toArray());
        

        //prompt the user for a location to save the json file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".json")) {
                filePath += ".json";
            }
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(json);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occurred while saving tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }       
    
    }
    /*
     * loads the tasks from a file.
     * @returns void
     */
    public void loadTasks() {
        //prompt the user for a location to load the json file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to load");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                String json = new String(Files.readAllBytes(Paths.get(filePath)));
                Gson gson = new Gson();
                TaskData[] taskData = gson.fromJson(json, TaskData[].class);
                for (TaskData data : taskData) {
                    Task task = new Task(this, data.taskName, data.taskDescription, data.taskDueDate, data.taskPriority);
                    task.isComplete = data.isComplete;
                    tasks.put(task.taskName, task);
                }
            } catch (JsonSyntaxException e) {
                JOptionPane.showMessageDialog(null, "An error occurred while parsing tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occurred while loading tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Generates a GUI panel that displays a list of tasks.
     *
     * @return a JPanel containing UI elements for each task in the task list.
     */
    public JPanel generateTaskListGUI() {
        // Create a new JPanel with BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel header = new JPanel();
        header.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Create header labels
        Font headerFont = new Font("Arial", Font.BOLD, 14); // Change font style and size
    
        // Set common constraints for all components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        JLabel completedLabel = new JLabel("Completed?");
        completedLabel.setFont(headerFont);
        header.add(completedLabel, gbc);
        
        // Create and add the task name label
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JLabel taskNameLabel = new JLabel("Task Name");
        taskNameLabel.setFont(headerFont);
        header.add(taskNameLabel, gbc);
        
        // Create and add the task description label
        gbc.gridx = 2;
        gbc.weightx = 2.0;
        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setFont(headerFont);
        header.add(descriptionLabel, gbc);

        // Create and add the task due date label
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        JLabel dueDateLabel = new JLabel("Due Date");
        dueDateLabel.setFont(headerFont);
        header.add(dueDateLabel, gbc);
        
        // Create and add the task priority label
        gbc.gridx = 4;
        gbc.weightx = 1.0;
        JLabel priorityLabel = new JLabel("Priority");
        priorityLabel.setFont(headerFont);
        header.add(priorityLabel, gbc);
        
        // Create and add the delete label
        gbc.gridx = 5;
        gbc.weightx = 0.5;
        JLabel deleteLabel = new JLabel("Delete");
        deleteLabel.setFont(headerFont);
        header.add(deleteLabel, gbc);
        
        panel.add(header);
        
        // Add a UI element for each task
        for (Task task : tasks.values()) {
            panel.add(task.createUIElement());
        }
        return panel;
    }


}
