package SpaceInvaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Leaderboard {
    private static final String LEADERBOARD_FILE = "leaderboard.txt";

    private List<ScoreEntry> leaderboardData;

    public Leaderboard() {
        leaderboardData = new ArrayList<>();
    }

    private void loadLeaderboardData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LEADERBOARD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    ScoreEntry entry = new ScoreEntry(name, score);
                    leaderboardData.add(entry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class ScoreEntry {
        private String name;
        private int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    public void initialize() {
        loadLeaderboardData();
        displayLeaderboard();
    }

    private void displayLeaderboard() {
        StringBuilder leaderboardText = new StringBuilder();

        for (ScoreEntry entry : leaderboardData) {
            leaderboardText.append(entry.name).append(": ").append(entry.score).append("\n");
        }

        JOptionPane.showMessageDialog(null, leaderboardText.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }
}

