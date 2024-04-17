package BE;

public class Customer {private int id;
    private String name;
    private String email;
    // Constructor to initialize a Customer object without an id (useful when id is auto-generated)
    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }
    // Constructor to initialize a Customer object with an id, name, and email
    public Customer(int id, String name, String email) {
        this(name, email);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name;
    }
}

