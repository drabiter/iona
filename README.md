# iona
REST API quickly. No XML or any configuration. Annotate the model and one chain call.

## usage
```java
public static void main(String[] args) throws IonaException {
    Iona.init()
            .mysql("localhost", "my_table", "user", "passwd")
            .add(Person.class);
}
```
After prepared the model,
```java
import javax.persistence.*;

@Table(name = "persons") // custom table
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

    @Column(name = "full_name") // custom column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
Generated API (JSON) based on class name
```
GET     /person       # select all person records
GET     /person/:id   # select person :id
POST    /person       # insert new person record
PUT     /person/:id   # update person :id
DELETE  /person/:id   # delete person :id
```
More on [REST](https://github.com/drabiter/iona/wiki/REST-Specification).

## specification
Tested on MariaDB 10.0.17
