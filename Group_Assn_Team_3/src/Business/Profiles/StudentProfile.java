package Business.Profiles;

import Business.Person.Person;

/**
 *
 * @author amoghvenkat
 */
public class StudentProfile extends Profile {

    Person person;
//    Transcript transcript;
    //   EmploymentHistroy employmenthistory;

    public StudentProfile(Person p) {
        super(p);

//        transcript = new Transcript(this);
//        employmenthistory = new EmploymentHistroy();
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public boolean isMatch(String id) {
        return person.getPersonId().equals(id);
    }

}

