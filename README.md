iona
=====

[![Build Status][travis-img]][travis-url]

[travis-img]: http://img.shields.io/travis/drabiter/iona.svg?style=flat-square
[travis-url]: https://travis-ci.org/drabiter/iona

REST API quickly. No XML, YAML, or any configuration. Annotate the model, invoke, done.

## usage
Complete utilization,
```java
public static void main(String[] args) throws IonaException {
    Iona.init("jdbc:mysql://localhost:3306/iona", "root", "").port(8080).add(Person.class);
}
```
After prepared the model with [ORMLite](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Local-Annotations) or [javax.persistence](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Javax-Persistence-Annotations) annotations,
```java
import javax.persistence.*;

@Entity(name = "people_of_sea") // custom table
public class Person {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "full_name") // custom column
    private String name;

    // getter..setter
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
