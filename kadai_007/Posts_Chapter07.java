package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Posts_Chapter07 {
    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement statement = null;

        // postsリスト
        String[][] postsList = {
            { "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
            { "1002", "2023-02-08", "お疲れ様です！", "12" },
            { "1003", "2023-02-09", "今日も頑張ります！", "18" },
            { "1001", "2023-02-09", "無理は禁物ですよ！", "17" },
            { "1002", "2023-02-10", "明日から連休ですね！", "20" },
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
            		 "jdbc:mysql://localhost/challenge_java",
                     "root",
                     "naxoxo00"
            );

            System.out.println("データベース接続成功：" + con);

            // postsテーブルをクリア
            statement = con.prepareStatement("TRUNCATE TABLE posts;");
            statement.executeUpdate();

            // SQLクエリを準備
            String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
            statement = con.prepareStatement(sql);

            int totalInserted = 0; // 総追加レコード数

            // リストの1行目から順番に読み込む
            for (int i = 0; i < postsList.length; i++) {
                // SQLクエリの「?」部分をリストのデータに置き換え
                statement.setString(1, postsList[i][0]); // ユーザーID
                statement.setString(2, postsList[i][1]); // 投稿日時
                statement.setString(3, postsList[i][2]); // 投稿内容
                statement.setString(4, postsList[i][3]); // いいね数

                // SQLクエリを実行（DBMSに送信）
                totalInserted += statement.executeUpdate();
            }

            System.out.println(totalInserted + "件のレコードが追加されました");

            // 投稿データを検索する
            Statement selectStatement = con.createStatement();
            String selectSql = "SELECT * FROM posts WHERE user_id = 1002;";
            System.out.println("ユーザーIDが1002のレコードを検索しました");
            ResultSet result = selectStatement.executeQuery(selectSql);

            int rowNum = 0;
            while (result.next()) {
                rowNum++;
                String posted_at = result.getString("posted_at");
                String post_content = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(rowNum + "件目：投稿日時=" + posted_at + "／投稿内容=" + post_content + "／いいね数=" + likes);
            }

        } catch (SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            try {
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ignore) {
            }
        }
    }
}