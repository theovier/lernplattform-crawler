
public class Course {

    private static final String COURSEPATH = "https://campusapp01.hshl.de/course/view.php?id=";
    private String name, path;

    public Course(String path, String name) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
