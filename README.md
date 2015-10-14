iona
=====

[![Build Status][travis-img]][travis-url]

[travis-img]: http://img.shields.io/travis/drabiter/iona.svg?style=flat-square
[travis-url]: https://travis-ci.org/drabiter/iona

REST API for your model quickly. No XML, JSON, or YAML, all programmatically.

## example

```java
public static void main(String[] args) throws IonaException {
    Iona.init("jdbc:mysql://localhost:3306/iona", "root", "")
        .port(8080)
        .add(Person.class)
        .start();
}
```
After prepared the model with [ORMLite](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Local-Annotations) or [javax.persistence](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Javax-Persistence-Annotations) annotations,
```java
import javax.persistence.*;

public class Person {

    @Id
    @GeneratedValue
    private long id;

    private String firstName;
    private String lastName;

    // getter..setter
}
```
Generated API JSON
```
GET     /person       # select all person records
GET     /person/:id   # select person :id
POST    /person       # insert new person record
PUT     /person/:id   # update person :id
DELETE  /person/:id   # delete person :id

{
    "id": 1,
    "first_name": "Foo",
    "last_name": "Bar"
}
```
More on [REST](https://github.com/drabiter/iona/wiki/REST-Specification).

## features

**Custom endpoint**
```
@MentalModel(endpoint = "people")
public class Person {..}
```
The class will be registered under `/people` endpoint instead `person`.

**custom table**
```
@Entity(name = "people_of_sea")
public class Person {..}
```
The class will be mapped to `people_of_sea` table instead `person`.

**custom column**
```
@Column(name = "full_name")
private String name;
```
The `name` field will be saved in `full_name` column instead `name`.

## specification

Tested on MariaDB 10.0.17, Java 1.8.0_25
