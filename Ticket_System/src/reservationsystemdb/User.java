package reservationsystemdb;

import java.util.ArrayList;
import java.util.List;

class User {
    String id;
    String password;
    String name;
    List<Booking> cart;

    User(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.cart = new ArrayList<>();
    }
}
