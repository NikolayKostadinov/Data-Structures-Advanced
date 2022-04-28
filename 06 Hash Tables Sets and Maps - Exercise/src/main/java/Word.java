import java.util.List;

public class Word {
    private List<String> parts;

    public void add(String part) {
        this.parts.add(part);
    }

    public void removeLastPart() {
        this.parts.remove(this.parts.size() - 1);
    }

    public String getString() {
        return String.join("", this.parts);
    }

    public List<String> getParts() {
        return parts;
    }
}
