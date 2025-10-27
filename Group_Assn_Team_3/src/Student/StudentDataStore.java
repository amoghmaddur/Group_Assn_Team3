package Student;
import java.util.*;
public class StudentDataStore {
    public static class Course { public final String id,title,faculty; public final int credits;
        public Course(String i,String t,String f,int c){id=i;title=t;faculty=f;credits=c;} }
    public static class Enrollment { public final String term; public final Course c; public String grade;
        public Enrollment(String t,Course c,String g){term=t;this.c=c;this.grade=g;} }
    public static class Payment { public final Date d; public final double a; public final String n;
        public Payment(Date d,double a,String n){this.d=d;this.a=a;this.n=n;} }
    public static class Student { public final String id; public String name="Student"; public double bal=0;
        public final List<Enrollment> e=new ArrayList<>(); public final List<Payment> p=new ArrayList<>();
        public Student(String i){id=i;} }
    private static final Map<String,Student> db=new HashMap<>();
    public static Student get(String id){return db.computeIfAbsent(id,Student::new);}
    public static List<Course> catalog(){return Arrays.asList(
        new Course("INFO5100","App Eng & Dev","Prof. Smith",4),
        new Course("INFO6150","Web Tools","Prof. Lee",4),
        new Course("INFO6210","DBMS","Prof. Chen",4));}
    static { Student s=get("S001");
        Course c=catalog().get(0);
        s.e.add(new Enrollment("Fall 2025",c,"A"));
        s.bal=4000.0;
        s.p.add(new Payment(new Date(),4000.0,"Initial Tuition Bill"));
    }
}
