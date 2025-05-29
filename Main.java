
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main {
    private static final String[] allWords = {
        "apple", "baker", "chair", "drink", "eagle", "fruit", "grape", "house", "input", "jelly",
        "knife", "lemon", "money", "night", "ocean", "piano", "queen", "river", "smart", "table",
        "under", "vivid", "water", "xenon", "youth", "zebra", "ant", "bat", "cat", "dog",
        "egg", "fan", "gum", "hat", "ice", "jam", "key", "lip", "man", "net",
        "owl", "pen", "rat", "sun", "top", "urn", "van", "win", "box", "yak"
    };

    private static int level = 1; // Default to 25 words
    private static String textToType;
    private static int currentCharIndex = 0; // Current character index in the text
    private static int correctChars = 0;
    private static int totalTyped = 0; 
    private static long startTime = 0;
    private static boolean started = false;
    private static StringBuilder typedSoFar = new StringBuilder(); 
    private static JTextPane typingPane;
    private static JLabel scoreLabel; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Typing Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(new Color(30, 30, 40));
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            // Header section
            JPanel topPanel = new JPanel();
            topPanel.setBackground(new Color(30, 30, 40));
            topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            JLabel title = new JLabel("Typing Speed Test");
            title.setFont(new Font("Monospaced", Font.BOLD, 26));
            title.setForeground(Color.WHITE);

            // Add title to top panel
            topPanel.add(title);
            mainPanel.add(topPanel);

            // Typing Test Label
            JLabel typingTestLabel = new JLabel("Typing Test");
            typingTestLabel.setFont(new Font("Arial", Font.BOLD, 18));
            typingTestLabel.setForeground(Color.WHITE);
            typingTestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(typingTestLabel);

            // Score display
            scoreLabel = new JLabel("Press any key to start...", SwingConstants.CENTER);
            scoreLabel.setForeground(Color.CYAN);
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(scoreLabel);

            // Typing pane
            typingPane = new JTextPane();
            typingPane.setContentType("text/html");
            textToType = generateRandomText(level); // Initialize before first use
            typingPane.setText(generateStyledText());
            typingPane.setFont(new Font("Consolas", Font.PLAIN, 24));
            typingPane.setBackground(new Color(50, 50, 60));
            typingPane.setEditable(false);
            typingPane.setFocusable(true);
            typingPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.add(typingPane);

            typingPane.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (!started) {
                        started = true;
                        startTime = System.currentTimeMillis();
                    }

                    if (currentCharIndex >= textToType.length()) return;

                    char typedChar = e.getKeyChar();
                    typedSoFar.append(typedChar);
                    if (typedChar == textToType.charAt(currentCharIndex)) {
                        correctChars++;
                    }
                    totalTyped++;
                    currentCharIndex++;
                    typingPane.setText(generateStyledText());


if (currentCharIndex == textToType.length()) {
                        long endTime = System.currentTimeMillis();
                        long timeTakenMillis = endTime - startTime;
                        double minutes = timeTakenMillis / 60000.0;
                        int wpm = (int) ((correctChars / 5.0) / minutes);
                        int accuracy = totalTyped > 0 ? (correctChars * 100 / totalTyped) : 100;
                        scoreLabel.setText(String.format("Done! WPM: %d | Accuracy: %d%%", wpm, accuracy));
                    }
                }
            });

            // Buttons Panel
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setBackground(new Color(30, 30, 40));
            buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centers the buttons

            // Buttons for selecting word count and reset
            JButton btn10Words = createButton("10 words");
            JButton btn25Words = createButton("25 words");
            JButton btn50Words = createButton("50 words");
            JButton btnReset = createButton("Reset");

            // Set action for each button
            btn10Words.addActionListener(e -> setLevelAndRestart(0));
            btn25Words.addActionListener(e -> setLevelAndRestart(1));
            btn50Words.addActionListener(e -> setLevelAndRestart(2));
            btnReset.addActionListener(e -> restartTest());

            // Add buttons to buttons panel
            buttonsPanel.add(btn10Words);
            buttonsPanel.add(btn25Words);
            buttonsPanel.add(btn50Words);
            buttonsPanel.add(btnReset);

            mainPanel.add(buttonsPanel); // Add buttons below the typing area

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
            typingPane.requestFocusInWindow();
        });
    }

    // Creates a button with a consistent appearance
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding around button
        button.setFocusPainted(false); // No focus border
        button.setContentAreaFilled(false); // Make button background transparent
        return button;
    }

    // Changes the level and restarts the typing test
    private static void setLevelAndRestart(int selectedLevel) {
        level = selectedLevel;
        restartTest();
    }

    // Restarts the typing test
    private static void restartTest() {
        textToType = generateRandomText(level);
        currentCharIndex = 0;
        correctChars = 0;
        totalTyped = 0;
        started = false;
        typedSoFar = new StringBuilder();
        typingPane.setText(generateStyledText());
        scoreLabel.setText("Press any key to start...");
        typingPane.requestFocusInWindow();
    }

    // Generates random text based on the selected level
    private static String generateRandomText(int level) {
        Random random = new Random();
        int wordsCount = (level == 0) ? 10 : (level == 1) ? 25 : 50;
        StringBuilder randomText = new StringBuilder();
        for (int i = 0; i < wordsCount; i++) {
            randomText.append(allWords[random.nextInt(allWords.length)]).append(" ");
        }
        return randomText.toString().trim();
    }


// Generates styled text for display in the typing area
    private static String generateStyledText() {
        StringBuilder sb = new StringBuilder("<html><div style='font-family:monospace; font-size:18px;font-weight:bold;'>");
        for (int i = 0; i < textToType.length(); i++) {
            if (i < typedSoFar.length()) {
                if (typedSoFar.charAt(i) == textToType.charAt(i)) {
                    sb.append("<span style='color:lime;'>").append(textToType.charAt(i)).append("</span>");
                } else {
                    sb.append("<span style='color:red;'>").append(textToType.charAt(i)).append("</span>");
                }
            } else if (i == typedSoFar.length()) {
                sb.append("<span style='background-color:yellow;color:black;'>").append(textToType.charAt(i)).append("</span>");
            } else {
                sb.append("<span style='color:white;'>").append(textToType.charAt(i)).append("</span>");
            }
        }
        sb.append("</div></html>");       
        return sb.toString();
    }
}