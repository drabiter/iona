package com.drabiter.iona._meta;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class PersonAssert extends AbstractAssert<PersonAssert, Person> {

    private PersonAssert(Person actual) {
        super(actual, PersonAssert.class);
    }

    public static PersonAssert assertThat(Person actual) {
        return new PersonAssert(actual);
    }

    public PersonAssert hasId() {
        Assertions.assertThat(actual.getId()).isNotNull();

        return this;
    }

    public PersonAssert hasId(long id) {
        Assertions.assertThat(actual.getId()).isEqualTo(id);

        return this;
    }

    public PersonAssert hasFirstName(String firstName) {
        Assertions.assertThat(actual.getFirstName()).isEqualTo(firstName);

        return this;
    }

    public PersonAssert hasLastName(String lastName) {
        Assertions.assertThat(actual.getLastName()).isEqualTo(lastName);

        return this;
    }

}
