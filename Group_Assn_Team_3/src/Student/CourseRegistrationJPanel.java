package Student;
import javax.swing.*; import javax.swing.table.*; import java.awt.*; import java.util.*;
import userinterface.student.StudentDataStore;
public class CourseRegistrationJPanel extends JPanel{
  private JTable tbl; private final String sid;
  public CourseRegistrationJPanel(String sid){
    this.sid=sid; setLayout(new BorderLayout(8,8));
    tbl=new JTable(new DefaultTableModel(new Object[]{"ID","Title","Faculty","Credits"},0));
    add(new JScrollPane(tbl),BorderLayout.CENTER);
    JPanel btm=new JPanel(); JButton en=new JButton("Enroll"); JButton dr=new JButton("Drop"); btm.add(en); btm.add(dr); add(btm,BorderLayout.SOUTH);
    load();
    en.addActionListener(e->enroll());
    dr.addActionListener(e->drop());
  }
  private void load(){
    DefaultTableModel m=(DefaultTableModel)tbl.getModel(); m.setRowCount(0);
    for(StudentDataStore.Course c: StudentDataStore.catalog()) m.addRow(new Object[]{c.id,c.title,c.faculty,c.credits});
  }
  private void enroll(){
    int r=tbl.getSelectedRow(); if(r<0){JOptionPane.showMessageDialog(this,"Select a course"); return;}
    StudentDataStore.Course c=StudentDataStore.catalog().get(r);
    StudentDataStore.Student s=StudentDataStore.get(sid);
    for(StudentDataStore.Enrollment e: s.e){ if(e.c.id.equals(c.id)){ JOptionPane.showMessageDialog(this,"Already enrolled"); return; } }
    int termCredits=0; for(StudentDataStore.Enrollment e: s.e){ if("Fall 2025".equals(e.term)) termCredits+=e.c.credits; }
    if(termCredits + c.credits > 8){ JOptionPane.showMessageDialog(this,"Cannot exceed 8 credits in a term"); return; }
    s.e.add(new StudentDataStore.Enrollment("Fall 2025",c,"A"));
    s.bal += c.credits * 1000;
    JOptionPane.showMessageDialog(this,"Enrolled "+c.id+" — billed $"+(c.credits*1000));
  }
  private void drop(){
    int r=tbl.getSelectedRow(); if(r<0){JOptionPane.showMessageDialog(this,"Select a course"); return;}
    StudentDataStore.Course c=StudentDataStore.catalog().get(r);
    StudentDataStore.Student s=StudentDataStore.get(sid);
    boolean removed = s.e.removeIf(x->x.c.id.equals(c.id));
    if(removed){
      s.bal -= c.credits*1000;
      s.p.add(new StudentDataStore.Payment(new java.util.Date(), -c.credits*1000, "Refund "+c.id));
      JOptionPane.showMessageDialog(this,"Dropped "+c.id+" — refunded $"+(c.credits*1000));
    } else {
      JOptionPane.showMessageDialog(this,"Not enrolled in "+c.id);
    }
  }
}
