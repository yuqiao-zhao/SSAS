import java.util.*;
import java.sql.*;


public class ControlSystem {
    static Teacher teacher = null;
    static Map<String, Student> studentTable = new HashMap<>();


    static void signUp(String passward) {
        String teacherId = UUID.randomUUID().toString();
        rewriteDataBase(teacherId);
    }


    static void signOut() {
        teacher = null;
    }


    static void rewriteDataBase(String sql) {
        final String url = "jdbc:mysql://localhost:3306/database";
        final String user = "group4";
        final String password = "lambda";
        ResultSet res = null;
        try
        {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {

        }
    }



    static List<String> lookupDataBase(String sql) throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/database";
        final String user = "group4";
        final String password = "lambda";
        ResultSet res = null;
        try
        {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            res = preparedStatement.executeQuery();
        }
        catch (SQLException e) {

        }

        int len = res.getMetaData().getColumnCount();
        List<String> ans = new ArrayList<>();
        while(res.next()) {
            StringBuilder x = new StringBuilder();
            for(int i = 1; i < len-1; i++) {
                if (i != 1)
                    x.append(" ");
                x.append(res.getString(i));
            }
            ans.add(x.toString());
        }
        return ans;
    }

    static void createStudent() throws SQLException {
        List<String> ans = lookupDataBase("select * from Student");
        for(String student : ans) {
            String key = student.split(" ")[0];
            studentTable.put(key, new Student(student));
        }
    }


    private static void createTeacher(List<String> ans) throws SQLException {
        teacher = new Teacher(ans.get(0));
    }

    static void logIn(String teacherId, String password) throws Exception {
        if(teacher != null)
            throw new Exception();

        List<String> ans = lookupDataBase("select * from Teacher where teacher_id='" + teacherId + "' and password='" + password + "'");
        if(ans.size() != 1) {
            throw new Exception();
        }
        createTeacher(ans);
    }





}