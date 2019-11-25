import java.util.Map;
import java.util.HashMap;

public class Student {
    String studentId;
    String name;
    String email;
    String status;
    String paymentAccount;
    Map<String, Map<Integer, Boolean>> lookup;

    public Student(String info) {
        String[] cnt = info.split(" ");
        this.studentId = cnt[0];
        this.name = cnt[1];
        this.email = cnt[2];
        this.status = cnt[3];
        this.paymentAccount = cnt[4];
        lookup = new HashMap<>();
    }


    public void mark(String courseId, int courseInfoId, boolean flag) throws Exception {
        if(not lookup.get(courseId).containsKey(courseInfoId) || not lookup.containsKey(courseId)) {
            throw new Exception();
        }
        lookup.get(courseId).put(courseInfoId, flag);
    }


    public void deleteCourse(String courseId) {
        lookup.remove(courseId);
    }
    @Override
    public String toString() {
        return "studentId : " + studentId + "  name : " + name + "   email : " + email + "   status: " + status + "   paymentAccount:" + paymentAccount;
    }
}