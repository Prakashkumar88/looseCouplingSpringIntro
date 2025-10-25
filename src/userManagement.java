import java.util.*;

public class userManagement {
    public static void main(String[] args) {
//        Set<String> user1Role = new HashSet<>(Arrays.asList("Admin", "User"));
//        User user1 = new User("Alice", true, user1Role);
//        List<User> users = new ArrayList<>();
//        users.add(user1);

//        inShort we can do this in single line as
        List<User> users = new ArrayList<>();
        users.add(new User("Alice", true, new HashSet<>(Arrays.asList("Admin", "User"))));
        users.add(new User("Bob", true, new HashSet<>(Arrays.asList("User"))));
        users.add(new User("Charlie", false, new HashSet<>(Arrays.asList("User"))));


        //need to remove the inactive users
        Iterator<User> it = users.iterator();
        while(it.hasNext()){
            if(!it.next().isActive()) it.remove();
        }
        // or in single line => users.removeIf(user -> !user.isActive());

        //print all active users
        System.out.println("Active Users");
        for(User user: users){
            if(user.isActive()) System.out.println(user.getName());
        }

        //count users per role
        Map<String, Integer> noOfUser = new HashMap<>();
        for(User user: users){
            Set<String> roles = user.getRole();
            for(String role: roles){
                noOfUser.put(role, noOfUser.getOrDefault(role, 0) + 1);
            }
        }
        System.out.println("All Roles: ");
        for(Map.Entry<String, Integer> userEntry: noOfUser.entrySet()){
            System.out.println("Role " + userEntry.getKey() + " cnt: " + userEntry.getValue());
        }

    }
}