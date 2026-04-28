package ui;

import model.Task;
import repository.TaskRepositoryInMemory;
import service.TaskService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppFrame extends JFrame {
    private JTextField titleField;
    private JTextField descriptionField;
    private JLabel statusLabel;
    private JButton createButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton doneButton;
    private JButton pendingButton;
    private JButton cancelButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private final TaskService taskService;
    private Task selectedTask = null;

    private static final Color BG = new Color(30, 30, 30);
    private static final Color SURFACE = new Color(45, 45, 45);
    private static final Color ACCENT = new Color(100, 180, 255);
    private static final Color DANGER = new Color(220, 80, 80);
    private static final Color SUCCESS = new Color(80, 200, 120);
    private static final Color TEXT = new Color(220, 220, 220);
    private static final Color MUTED = new Color(140, 140, 140);
    private static final Font FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    public AppFrame() {
        this.taskService = new TaskService(new TaskRepositoryInMemory());
        setTitle("To-Do List");
        setSize(640, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);
        add(buildForm(), BorderLayout.NORTH);
        add(buildList(), BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new BorderLayout(5, 8));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(16, 16, 8, 16));

        titleField = styledField();
        descriptionField = styledField();
        statusLabel = new JLabel("Status: PENDING");
        statusLabel.setFont(FONT_BOLD);
        statusLabel.setForeground(MUTED);

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        fieldsPanel.setBackground(BG);
        fieldsPanel.add(styledLabel("Título:"));
        fieldsPanel.add(titleField);
        fieldsPanel.add(styledLabel("Descrição:"));
        fieldsPanel.add(descriptionField);
        fieldsPanel.add(statusLabel);
        fieldsPanel.add(new JLabel(""));

        createButton = styledButton("Criar", ACCENT);
        saveButton = styledButton("Salvar", ACCENT);
        deleteButton = styledButton("Deletar", DANGER);
        doneButton = styledButton("Concluir", SUCCESS);
        pendingButton = styledButton("Pendente", new Color(180, 140, 60));
        cancelButton = styledButton("Cancelar", MUTED);

        JPanel editButtons = new JPanel(new GridLayout(1, 5, 8, 0));
        editButtons.setBackground(BG);
        editButtons.add(doneButton);
        editButtons.add(pendingButton);
        editButtons.add(saveButton);
        editButtons.add(deleteButton);
        editButtons.add(cancelButton);

        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createPanel.setBackground(BG);
        createPanel.add(createButton);

        createButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            if (description.isBlank()) {
                taskService.createTask(title);
            } else {
                taskService.createTask(title, description);
            }
            refreshList();
            clearForm();
        });

        saveButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            taskService.changeTitle(selectedTask.getId(), title);
            if (!description.isBlank()) {
                taskService.changeDescription(selectedTask.getId(), description);
            }
            refreshList();
            deselect();
        });

        deleteButton.addActionListener(e -> {
            taskService.deleteTask(selectedTask.getId());
            refreshList();
            deselect();
        });

        doneButton.addActionListener(e -> {
            taskService.taskIsDone(selectedTask.getId());
            statusLabel.setText("Status: DONE");
            statusLabel.setForeground(SUCCESS);
            refreshList();
        });

        pendingButton.addActionListener(e -> {
            taskService.taskIsPending(selectedTask.getId());
            statusLabel.setText("Status: PENDING");
            statusLabel.setForeground(MUTED);
            refreshList();
        });

        cancelButton.addActionListener(e -> deselect());

        panel.add(fieldsPanel, BorderLayout.NORTH);
        panel.add(editButtons, BorderLayout.CENTER);
        panel.add(createPanel, BorderLayout.SOUTH);

        setEditMode(false);
        return panel;
    }

    private JScrollPane buildList() {
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setBackground(SURFACE);
        taskList.setForeground(TEXT);
        taskList.setFont(FONT);
        taskList.setSelectionBackground(new Color(60, 90, 130));
        taskList.setSelectionForeground(Color.WHITE);
        taskList.setFixedCellHeight(32);
        taskList.setBorder(new EmptyBorder(4, 8, 4, 8));

        taskList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && taskList.getSelectedIndex() != -1) {
                selectedTask = taskService.getAllTasks().get(taskList.getSelectedIndex());
                titleField.setText(selectedTask.getTitle());
                descriptionField.setText(selectedTask.getDescription() != null ? selectedTask.getDescription() : "");
                statusLabel.setText("Status: " + selectedTask.getStatus());
                statusLabel.setForeground(selectedTask.getStatus().name().equals("DONE") ? SUCCESS : MUTED);
                setEditMode(true);
            }
        });

        JScrollPane scroll = new JScrollPane(taskList);
        scroll.getViewport().setBackground(SURFACE);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)));
        scroll.setBackground(BG);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.setBorder(new EmptyBorder(0, 16, 16, 16));
        wrapper.add(scroll, BorderLayout.CENTER);

        JScrollPane result = new JScrollPane();
        result.setViewportView(wrapper);
        result.setBorder(null);

        return scroll;
    }

    private void setEditMode(boolean editing) {
        createButton.setVisible(!editing);
        saveButton.setVisible(editing);
        deleteButton.setVisible(editing);
        doneButton.setVisible(editing);
        pendingButton.setVisible(editing);
        cancelButton.setVisible(editing);
    }

    private void deselect() {
        selectedTask = null;
        taskList.clearSelection();
        setEditMode(false);
        clearForm();
    }

    private void refreshList() {
        listModel.clear();
        taskService.getAllTasks().forEach(t ->
                listModel.addElement("  " + (t.getStatus().name().equals("DONE") ? "✓" : "○") + "  [" + t.getId() + "] " + t.getTitle() + " — " + t.getStatus())
        );
    }

    private void clearForm() {
        titleField.setText("");
        descriptionField.setText("");
        statusLabel.setText("Status: PENDING");
        statusLabel.setForeground(MUTED);
    }

    private JTextField styledField() {
        JTextField field = new JTextField();
        field.setBackground(SURFACE);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return field;
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED);
        label.setFont(FONT_BOLD);
        return label;
    }

    private JButton styledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BOLD);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}