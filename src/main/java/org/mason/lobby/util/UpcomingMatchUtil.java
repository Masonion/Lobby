package org.mason.lobby.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import org.bukkit.ChatColor;

public class UpcomingMatchUtil {
    private Connection connection;
    private static final String HOST = "51.161.199.97";
    private static final int PORT = 3306;
    private static final String DATABASE = "s4_UHCCalendar";
    private static final String USERNAME = "u4_aFDkHJQiHS";
    private static final String PASSWORD = "P0Y^JXeUN9.3XqR.M.JBNjX8";

    public UpcomingMatchUtil() {
        try {
            connect(HOST, PORT, DATABASE, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void connect(String host, int port, String database, String username, String password) throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.connection = DriverManager.getConnection(url, username, password);
    }

    public String checkUpcomingGamesAndPrint() {
        String sql = "SELECT * FROM uhc_calendar WHERE unix_time > UNIX_TIMESTAMP() ORDER BY unix_time ASC LIMIT 1";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                StringBuilder builder = new StringBuilder();

                builder.append(ChatColor.RED).append("Time: ")
                        .append(ChatColor.WHITE).append(getTimeUntil(resultSet.getLong("unix_time"))).append("\n");

                builder.append(ChatColor.AQUA).append("Version: ")
                        .append(ChatColor.WHITE).append(resultSet.getString("version")).append("\n");

                builder.append(ChatColor.AQUA).append("Host: ")
                        .append(ChatColor.WHITE).append(resultSet.getString("host")).append("\n");

                builder.append(ChatColor.AQUA).append("Scenarios: ")
                        .append(ChatColor.WHITE).append(formatScenarios(resultSet.getString("scenarios")).toString()).append("\n");

                builder.append(ChatColor.AQUA).append("Team size: ")
                        .append(ChatColor.WHITE).append(getTeamSize(resultSet.getInt("team_size")));

                return builder.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getTeamSize(int teamSize) {
        if (teamSize == 1) {
            return "FFA";
        } else {
            return "To" + teamSize;
        }
    }

    private String getTimeUntil(long unixTime) {
        TimeZone tz = TimeZone.getTimeZone("Australia/Sydney");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(tz);

        long currentTime = System.currentTimeMillis() / 1000L; // Current time in seconds
        long timeDifference = unixTime - currentTime; // Time difference in seconds

        Calendar matchTime = Calendar.getInstance();
        matchTime.setTimeInMillis(unixTime * 1000);  // unixTime is in seconds, convert it to milliseconds

        String formattedTime = sdf.format(matchTime.getTime());
        String timeRemaining = "";

        if (timeDifference < 3600) {
            // Less than an hour
            timeRemaining = timeDifference / 60 + " minutes";
        } else if (timeDifference < 86400) {
            // Less than a day
            timeRemaining = timeDifference / 3600 + " hours";
        } else {
            // More than a day
            timeRemaining = timeDifference / 86400 + " days";
        }

        return formattedTime + " (In " + timeRemaining + ")";
    }

    private List<String> formatScenarios(String scenarios) {
        String[] scenarioArray = scenarios.split(","); // Assuming scenarios are comma-separated
        List<String> formattedScenarios = new ArrayList<>();

        int lineLength = 0;
        StringBuilder line = new StringBuilder();
        for (String scenario : scenarioArray) {
            if (lineLength < 4) {
                if (lineLength > 0) {
                    line.append(", ");
                }
                line.append(scenario.trim());
                lineLength++;
            } else {
                formattedScenarios.add(line.toString());
                line = new StringBuilder(scenario.trim());
                lineLength = 1;
            }
        }

        // Add the last line if it isn't empty
        if (line.length() > 0) {
            formattedScenarios.add(line.toString());
        }

        return formattedScenarios;
    }
}