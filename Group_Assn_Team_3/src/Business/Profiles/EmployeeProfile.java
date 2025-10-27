package Business.Profiles;

import Business.Person.Person;

/**
 *
 * @author amoghvenkat
 */
public class EmployeeProfile extends Profile {



    public EmployeeProfile(Person p) {

        super(p); 

    }
    @Override
    public String getRole(){
        return  "Admin";
    }

}