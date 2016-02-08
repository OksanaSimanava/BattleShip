import java.sql.*;

public class Player {

    private int playerId;
    private int wonCount;
    private int loseCount;
    private int zeroCount;


    public Player(int playerID) {
        this.playerId = playerID;
        reloadStats();
    }

    public void reloadStats() {
            Connection cn;
            Statement st;
            String selectWin = "select gamesWon from users " +
                    "where user_id = " + this.playerId;
            String selectLost = "select gamesLose from users " +
                    "where user_id = " + this.playerId;
            String selectZero = "select gamesZero from users " +
                    "where user_id = " + this.playerId;

            try {
                cn = DriverManager.getConnection(
                        "jdbc:mysql://localhost/BD", "root", "root");

                st = cn.createStatement();
                ResultSet rs = st.executeQuery(selectWin);
                while (rs.next()) {
                    this.wonCount = rs.getInt("gamesWon");
                }

                rs = st.executeQuery(selectLost);
                while (rs.next()) {
                    this.loseCount = rs.getInt("gamesLose");
                }

                rs = st.executeQuery(selectZero);
                while (rs.next()) {
                    this.zeroCount = rs.getInt("gamesZero");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public int getWonCount() {
        return wonCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public int getZeroCount() {
        return zeroCount;
    }

    public void updateStatCount(int status) {
        Connection cn;
        Statement st;
        String updUser;
        if (status == 1) {
            this.wonCount++;
            updUser = "UPDATE users " +
                    " SET gamesWon = " + this.wonCount +
                    " WHERE user_id = " + this.playerId;
        } else if (status == -1) {
            this.loseCount++;
            updUser = "UPDATE users " +
                    " SET gamesLose = " + this.loseCount +
                    " WHERE user_id = " + this.playerId;
        } else {
            this.zeroCount++;
            updUser = "UPDATE users " +
                    " SET gamesZero = " + this.zeroCount +
                    " WHERE user_id = " + this.playerId;
        }
        try {
            cn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/BD", "root", "root");
            st = cn.createStatement();
            st.executeUpdate(updUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
