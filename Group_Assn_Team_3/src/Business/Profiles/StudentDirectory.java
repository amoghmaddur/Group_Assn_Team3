package Business.Profiles;

import Business.Person.Person;
import Business.Profiles.StudentProfile;

import java.util.ArrayList;

/**
 *
 * @author amoghvenkat
 */
public class StudentDirectory {


    ArrayList<StudentProfile> studentlist;

    public StudentDirectory() {

     studentlist = new ArrayList();

    }

    public StudentProfile newStudentProfile(Person p) {

        StudentProfile sp = new StudentProfile(p);
        studentlist.add(sp);
        return sp;
    }

    public StudentProfile findStudent(String id) {

        for (StudentProfile sp : studentlist) {

            if (sp.isMatch(id)) {
                return sp;
            }
        }
            return null; //not found after going through the whole list
         }
    
}
