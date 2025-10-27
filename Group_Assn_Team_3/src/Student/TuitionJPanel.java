package Student;
import Student.StudentDataStore;
import javax.swing.*; import javax.swing.table.*; import java.awt.*; import java.util.*;
public class TuitionJPanel extends JPanel{
  private JLabel bal; private JTable t;
  public TuitionJPanel(String sid){
    setLayout(new BorderLayout(8,8));
    bal=new JLabel(); add(bal,BorderLayout.NORTH);
    t=new JTable(new DefaultTableModel(new Object[]{"Date","Amount","Note"},0));
    add(new JScrollPane(t),BorderLayout.CENTER);
    JButton pay=new JButton("Pay All"); add(pay,BorderLayout.SOUTH);
    pay.addActionListener(e->payAll(sid));
    refresh(sid);
  }
  private void refresh(String sid){
    StudentDataStore.Student s=StudentDataStore.get(sid);
    bal.setText("Balance: $"+String.format("%.2f", s.bal));
    DefaultTableModel m=(DefaultTableModel)t.getModel(); m.setRowCount(0);
    for(StudentDataStore.Payment p:s.p) m.addRow(new Object[]{p.d, String.format("%.2f", p.a), p.n});
  }
  private void payAll(String sid){
    StudentDataStore.Student s=StudentDataStore.get(sid);
    if(s.bal<=0){ JOptionPane.showMessageDialog(this,"No balance to pay."); return; }
    s.p.add(new StudentDataStore.Payment(new Date(), s.bal, "Tuition Payment"));
    s.bal = 0;
    refresh(sid);
    JOptionPane.showMessageDialog(this,"Payment successful.");
  }
}
