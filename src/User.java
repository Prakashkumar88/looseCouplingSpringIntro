import java.util.Set;

public class User {
    private String name;
    private boolean isActive;
    private Set<String> role;

    public User(String name, boolean isActive, Set<String> role) {
        this.name = name;
        this.isActive = isActive;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}

