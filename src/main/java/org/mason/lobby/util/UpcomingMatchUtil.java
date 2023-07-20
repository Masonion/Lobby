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
        String sql = "SELECT * FROM uhc_calendar WHERE unix_time > (UNIX_TIMESTAMP() - 1500) AND region = 'AU' ORDER BY unix_time ASC LIMIT 1";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                StringBuilder builder = new StringBuilder();

                builder.append(ChatColor.AQUA).append("Host" + ChatColor.GRAY + ": ")
                        .append(ChatColor.WHITE).append(resultSet.getString("host")).append("\n");

                builder.append(ChatColor.AQUA).append("Time (Sydney)" + ChatColor.GRAY + ": ")
                        .append(ChatColor.WHITE).append(getTimeUntil(resultSet.getLong("unix_time")).replaceAll("\n", "\n" + ChatColor.GRAY + ChatColor.ITALIC)).append("\n");

                builder.append(ChatColor.AQUA).append("Version" + ChatColor.GRAY + ": ")
                        .append(ChatColor.WHITE).append(resultSet.getString("version")).append(" ");

                builder.append(ChatColor.AQUA).append("Region" + ChatColor.GRAY + ": ").append(ChatColor.WHITE).append(resultSet.getString("region")).append("\n");

                builder.append(ChatColor.AQUA).append("Team Size" + ChatColor.GRAY + ": ")
                        .append(ChatColor.WHITE).append(getTeamSize(resultSet.getInt("team_size"))).append("\n");

                builder.append(ChatColor.AQUA).append("Scenarios" + ChatColor.GRAY + ": ")
                        .append(ChatColor.WHITE).append(String.join("\n", formatScenarios(resultSet.getString("scenarios"))));

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

        if (timeDifference < 0) {
            // Game has started
            long timeSinceStart = Math.abs(timeDifference);
            if (timeSinceStart < 3600) {
                // Less than an hour
                timeRemaining = timeSinceStart / 60 > 0 ? timeSinceStart / 60 + " minutes since opened" : "";
            } else {
                // Hour or more
                long hours = timeSinceStart / 3600;
                long minutes = (timeSinceStart % 3600) / 60;
                timeRemaining = (hours > 0 ? hours + " hour" + (hours > 1 ? "s" : "") : "")
                        + (hours > 0 && minutes > 0 ? " and " : "")
                        + (minutes > 0 ? minutes + " minute" + (minutes > 1 ? "s" : "") : "")
                        + " since start";
            }
        } else if (timeDifference < 3600) {
            // Less than an hour
            timeRemaining = timeDifference / 60 > 0 ? timeDifference / 60 + " minutes" : "";
        } else {
            // Hour or more
            long hours = timeDifference / 3600;
            long minutes = (timeDifference % 3600) / 60;
            timeRemaining = (hours > 0 ? hours + " hour" + (hours > 1 ? "s" : "") : "")
                    + (hours > 0 && minutes > 0 ? " and " : "")
                    + (minutes > 0 ? minutes + " minute" + (minutes > 1 ? "s" : "") : "");
        }

        return formattedTime + "\n(" + timeRemaining + ")";
    }

    private List<String> formatScenarios(String scenarios) {
        String[] scenarioArray = scenarios.split(","); // Assuming scenarios are comma-separated
        List<String> formattedScenarios = new ArrayList<>();

        int lineLength = 0;
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < scenarioArray.length; i++) {
            String scenario = scenarioArray[i];
            if (lineLength < 2) {
                if (lineLength > 0) {
                    line.append(", ");
                }
                line.append(scenario.trim());

                // If this is not the last scenario in the line, append a comma
                if (lineLength == 1 && i != scenarioArray.length - 1) {
                    line.append(",");
                }

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