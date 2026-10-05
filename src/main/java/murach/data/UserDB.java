package murach.data;

import murach.business.User;
import java.util.ArrayList;
import java.util.List;

public class UserDB {

    // In-memory list to store users (simulating a database)
    private static final List<User> users = new ArrayList<>();

    public static long insert(User user) {
        users.add(user);
        // Return the index as a simulated primary key
        return users.size();
    }

    public static List<User> selectAll() {
        return new ArrayList<>(users);
    }

    public static int getCount() {
        return users.size();
    }
}
