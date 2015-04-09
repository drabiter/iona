# iona
REST API quickly

## usage
```java
public static void main(String[] args) throws IonaException {
    Iona.init()
            .db("com.mysql.jdbc.jdbc2.optional.MysqlDataSource", "localhost", "my_table", "user", "passwd")
            .addModel(Person.class);
}
```
After prepared the model(s)
```java
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "persons")
public class Person {
    private long id;
    
    private String name;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "full_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
Generated API (JSON)
```
GET     /person       # select all person records
GET     /person/:id   # select person :id
POST    /person       # insert new person record
PUT     /person/:id   # update person :id
DELETE  /person/:id   # delete person :id
```
