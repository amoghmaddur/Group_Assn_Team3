package Business.Profiles;

import Business.Person.Person;

/**
 *
 * @author amoghvenkat
 */
public class FacultyProfile extends Profile {
    
    public FacultyProfile(Person p) {
        super(p);
    }
    
    @Override
    public String getRole() {
        return "Faculty";
    }
    
}
