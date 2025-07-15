import java.util.*;

public class Tag {
    public String name;
    public List<String[]> attributes = new ArrayList<>();

    public Tag(String name) {
        this.name = name;
    }
    public void addAttribute(String key, String value) {
        attributes.add(new String[]{key, value});
    }
    public String toString() {
        return "<" + name + ">";
    }
}
