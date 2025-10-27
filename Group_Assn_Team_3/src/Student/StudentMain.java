package Student;
import javax.swing.*;
import userinterface.student.StudentWorkAreaJPanel;
public class StudentMain {
  public static void main(String[] args){
    SwingUtilities.invokeLater(()->{
      JFrame f=new JFrame("University Student - Dashboard");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setSize(960,640);
      f.setLocationRelativeTo(null);
      f.setContentPane(new StudentWorkAreaJPanel("S001"));
      f.setVisible(true);
    });
  }
}
