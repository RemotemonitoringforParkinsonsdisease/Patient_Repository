package POJOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {
    private static List<String> idList = new ArrayList<String>();
    private String id;
    private String email;
    private String fullName;

    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
        this.id = createId();
        idList.add(id);
    }

    private String createId(){
        String identifier = "P";
        final int idLength = 9;
        for (int i = 0; i < idLength; i++) {
            Random rand = new Random();
            identifier += rand.nextInt(10);
        }
        if(idList.contains(identifier)){
            return createId();
        }
        return identifier;
    }


}
