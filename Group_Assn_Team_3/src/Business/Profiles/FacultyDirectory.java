package Business.Profiles;

import Business.Person.Person;
import java.util.ArrayList;

/**
 *
 * @author amoghvenkat
 */
public class FacultyDirectory {
    
    ArrayList<FacultyProfile> facultyList;
    
    public FacultyDirectory() {
        facultyList = new ArrayList<>();
    }
    
    public FacultyProfile newFacultyProfile(Person p) {
        FacultyProfile fp = new FacultyProfile(p);
        facultyList.add(fp);
        return fp;
    }
    
    public FacultyProfile findFaculty(String id) {
        for (FacultyProfile fp : facultyList) {
            if (fp.isMatch(id)) {
                return fp;
            }
        }
        return null;
    }
    
    public ArrayList<FacultyProfile> getFacultyList() {
        return facultyList;
    }
    
}
