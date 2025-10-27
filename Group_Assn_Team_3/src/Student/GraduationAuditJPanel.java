package Student;
import javax.swing.*; import java.awt.*;
import userinterface.student.StudentDataStore;
public class GraduationAuditJPanel extends JPanel{
  public GraduationAuditJPanel(String sid){
    setLayout(new GridLayout(3,1,8,8));
    StudentDataStore.Student s=StudentDataStore.get(sid);
    int cr=s.e.stream().mapToInt(e->e.c.credits).sum();
    boolean core=s.e.stream().anyMatch(e->"INFO5100".equals(e.c.id));
    add(new JLabel("Credits Completed: "+cr+"/32"));
    add(new JLabel("Core INFO5100 Completed: "+(core?"Yes":"No")));
    add(new JLabel("Ready to Graduate: "+(cr>=32 && core ? "Yes":"No")));
  }
}
