package Student;
import javax.swing.*; import java.awt.*;
import userinterface.student.StudentDataStore;
import userinterface.student.StudentDataStore;
public class ProfileJPanel extends JPanel{
  public ProfileJPanel(String sid){
    setLayout(new GridLayout(4,2,8,8));
    StudentDataStore.Student s=StudentDataStore.get(sid);
    JTextField n=new JTextField(s.name);
    add(new JLabel("Name:")); add(n);
    add(new JLabel("Student ID:")); add(new JLabel(s.id));
    add(new JLabel("Balance:")); add(new JLabel("$"+String.format("%.2f", s.bal)));
    JButton save=new JButton("Save"); add(new JLabel()); add(save);
    save.addActionListener(e->{ s.name=n.getText(); JOptionPane.showMessageDialog(this,"Profile updated."); });
  }
}
