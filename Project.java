import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Project extends JFrame implements ActionListener {

    // Digital Clock
    JLabel clockLabel;

    // Alarm
    JTextField alarmField;
    JButton setAlarmButton;
    JLabel alarmStatusLabel;
    String alarmTime = "";

    // Stopwatch
    JLabel stopwatchLabel;
    JButton startButton, stopButton, resetButton;
    Timer stopwatchTimer;
    int seconds = 0, minutes = 0, hours = 0;
    boolean isRunning = false;

    public Project() {
        setTitle("Clock Application");
        setSize(400, 450);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

        // ========== Top Panel - Digital Clock ==========
        clockLabel = new JLabel("Time: --:--:--", JLabel.CENTER);
        clockLabel.setFont(new Font("Arial", Font.BOLD, 26));
        clockLabel.setForeground(new Color(33, 37, 41)); // Dark navy text
        add(clockLabel, BorderLayout.NORTH);
        startClock();

        // ========== Center Panel - Alarm ==========
        JPanel alarmPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        alarmPanel.setBorder(BorderFactory.createTitledBorder("Alarm"));
        alarmPanel.setBackground(Color.WHITE);

        alarmField = new JTextField();
        setAlarmButton = new JButton("Set Alarm");
        alarmStatusLabel = new JLabel("No alarm set");

        styleButton(setAlarmButton);
        alarmStatusLabel.setForeground(new Color(0, 102, 51)); // Greenish tone

        alarmPanel.add(new JLabel("Alarm Time (HH:mm):"));
        alarmPanel.add(alarmField);
        alarmPanel.add(setAlarmButton);
        alarmPanel.add(alarmStatusLabel);

        setAlarmButton.addActionListener(this);
        add(alarmPanel, BorderLayout.CENTER);

        // ========== Bottom Panel - Stopwatch ==========
        JPanel stopwatchPanel = new JPanel(new FlowLayout());
        stopwatchPanel.setBorder(BorderFactory.createTitledBorder("Stopwatch"));
        stopwatchPanel.setBackground(Color.WHITE);

        stopwatchLabel = new JLabel("00:00:00");
        stopwatchLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        stopwatchLabel.setForeground(new Color(0, 0, 102));

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");

        styleButton(startButton);
        styleButton(stopButton);
        styleButton(resetButton);

        stopwatchPanel.add(stopwatchLabel);
        stopwatchPanel.add(startButton);
        stopwatchPanel.add(stopButton);
        stopwatchPanel.add(resetButton);

        startButton.addActionListener(this);
        stopButton.addActionListener(this);
        resetButton.addActionListener(this);

        add(stopwatchPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    void styleButton(JButton button) {
        button.setBackground(new Color(173, 216, 230)); // Light blue
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    void startClock() {
        Timer timer = new Timer(1000, e -> {
            LocalTime now = LocalTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
            clockLabel.setText("Time: " + now.format(format));

            // Check alarm
            DateTimeFormatter alarmFormat = DateTimeFormatter.ofPattern("HH:mm");
            if (!alarmTime.equals("") && now.format(alarmFormat).equals(alarmTime)) {
                JOptionPane.showMessageDialog(this, "⏰ Alarm! It's " + alarmTime);
                alarmTime = "";
                alarmStatusLabel.setText("No alarm set");
            }
        });
        timer.start();
    }

    void startStopwatch() {
        stopwatchTimer = new Timer(1000, e -> {
            if (isRunning) {
                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                    if (minutes == 60) {
                        minutes = 0;
                        hours++;
                    }
                }
                stopwatchLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
        });
        stopwatchTimer.start();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == setAlarmButton) {
            alarmTime = alarmField.getText().trim();
            alarmStatusLabel.setText("Alarm set for: " + alarmTime);
        }

        if (e.getSource() == startButton) {
            isRunning = true;
            if (stopwatchTimer == null) {
                startStopwatch();
            }
        }

        if (e.getSource() == stopButton) {
            isRunning = false;
        }

        if (e.getSource() == resetButton) {
            isRunning = false;
            hours = minutes = seconds = 0;
            stopwatchLabel.setText("00:00:00");
        }
    }

    public static void main(String[] args) {
        new Project();
    }
}
