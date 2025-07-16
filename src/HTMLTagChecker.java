import java.io.*;
import java.util.*;
public class HTMLTagChecker {
    public static void main(String[] args) {
        System.out.println("checking Correct.html:");
        check("Correct.html");

        System.out.println("\nchecking Incorrect.html:");
        check("Incorrect.html");
    }

    private static void check(String filename) {
        Stack<Tag> stack = new Stack<>();
        List<String> tokens = readFile(filename);
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith("<!--") || token.startsWith("<!")) {
                continue; 
            } else if (token.startsWith("</")) {
                String closingTag = token.substring(2, token.length() - 1).trim();
                if (stack.isEmpty()) {
                    System.out.println("error: Found additional closing tag: <" + closingTag + ">");
                    return;
                }
                Tag openTag = stack.pop();
                if (!openTag.name.equals(closingTag)) {
                    System.out.println("errror: Found mismatched closing tag: </" + closingTag + ">");
                    System.out.println("It should be </" + openTag.name + ">");
                    return;
                }
            } else if (token.endsWith("/>")) {
                continue; // Self-closing tag
            } else if (token.startsWith("<")) {
                Tag t = parseTag(token);
                stack.push(t);
            }
        }

        if (!stack.isEmpty()) {
            System.out.println("error: Not found closing tag: <" + stack.peek().name + ">");
        } else {
            System.out.println("The HTML file nesting is correct");
        }
    }

    private static List<String> readFile(String filename) {
        List<String> tags = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int ch;
            boolean inTag = false;
            StringBuilder tag = new StringBuilder();

            while ((ch = reader.read()) != -1) {
                if (ch == '<') {
                    inTag = true;
                    tag.setLength(0);
                    tag.append((char) ch);
                } else if (ch == '>') {
                    if (inTag) {
                        tag.append((char) ch);
                        tags.add(tag.toString());
                        inTag = false;
                    }
                } else if (inTag) {
                    tag.append((char) ch);
                }
            }

        } catch (IOException e) {
            System.out.println("File not found: " + filename);
        }

        return tags;
    }

    private static Tag parseTag(String tagText) {
        tagText = tagText.substring(1, tagText.length() - 1).trim(); // to remove
        String[] parts = tagText.split("\\s+");
        Tag tag = new Tag(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            if (parts[i].contains("=")) {
                String[] keyVal = parts[i].split("=");
                String key = keyVal[0];
                String value = keyVal[1].replaceAll("\"", "");
                tag.addAttribute(key, value);
            }
        }

        return tag;
    }
}
