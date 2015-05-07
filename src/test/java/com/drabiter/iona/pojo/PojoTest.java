package com.drabiter.iona.pojo;

import org.junit.Test;

import com.drabiter.iona._meta.Person;
import com.drabiter.iona._meta.PersonAssert;
import com.drabiter.iona.model.Pojo;

public class PojoTest {

    @Test
    public void testSetId() throws Exception {
        Person person = new Person();
        person.setId(33333L);
        String id = "99999";

        Pojo.register(Person.class);
        Pojo.setId(person, id);

        PersonAssert.assertThat(person).hasId(Long.parseLong(id));
    }

}
