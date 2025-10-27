package Student;
import Student.StudentDataStore;
import javax.swing.*; import javax.swing.table.*; import java.awt.*;
public class TranscriptJPanel extends JPanel{
  public TranscriptJPanel(String sid){
    setLayout(new BorderLayout(8,8));
    JTable t=new JTable(new DefaultTableModel(new Object[]{"Term","Course","Grade","Credits"},0));
    add(new JScrollPane(t),BorderLayout.CENTER);
    StudentDataStore.Student s=StudentDataStore.get(sid);
    DefaultTableModel m=(DefaultTableModel)t.getModel();
    if(s.bal>0){
      m.setRowCount(0);
      JOptionPane.showMessageDialog(this,"Please clear tuition balance to view transcript.");
      return;
    }
    for(StudentDataStore.Enrollment e: s.e){
      m.addRow(new Object[]{e.term,e.c.title,e.grade,e.c.credits});
    }
  }
}
