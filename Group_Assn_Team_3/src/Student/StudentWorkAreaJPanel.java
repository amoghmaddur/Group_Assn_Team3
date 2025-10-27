package Student;
import Student.ProfileJPanel;
import Student.GraduationAuditJPanel;
import Student.CourseRegistrationJPanel;
import javax.swing.*; import java.awt.*;
import userinterface.student.TranscriptJPanel;
import userinterface.student.TuitionJPanel;
public class StudentWorkAreaJPanel extends JPanel{
  public StudentWorkAreaJPanel(String sid){
    setLayout(new BorderLayout(12,12));
    JPanel menu=new JPanel(new GridLayout(5,1,8,8));
    JButton[] b={new JButton("Course Registration"),new JButton("Transcript"),new JButton("Graduation Audit"),new JButton("Tuition & Payments"),new JButton("Profile")};
    for(JButton x:b){x.setFocusPainted(false);menu.add(x);}
    add(menu,BorderLayout.WEST);
    JPanel cards=new JPanel(new CardLayout());
    cards.add(new CourseRegistrationJPanel(sid),"reg");
    cards.add(new TranscriptJPanel(sid),"tx");
    cards.add(new GraduationAuditJPanel(sid),"grad");
    cards.add(new TuitionJPanel(sid),"fee");
    cards.add(new ProfileJPanel(sid),"me");
    add(cards,BorderLayout.CENTER);
    String[] n={"reg","tx","grad","fee","me"};
    for(int i=0;i<b.length;i++){int j=i;b[i].addActionListener(e->((CardLayout)cards.getLayout()).show(cards,n[j]));}
  }
}
