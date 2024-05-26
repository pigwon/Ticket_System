package reservationsystemdb;

class Content {
    String type;
    String name;

    Content(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return type + ": " + name;
    }
}
