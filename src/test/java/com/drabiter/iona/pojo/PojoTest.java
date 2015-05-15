package com.drabiter.iona.pojo;

import org.junit.Test;

import com.drabiter.iona._meta.Person;
import com.drabiter.iona._meta.PersonAssert;
import com.drabiter.iona.model.Pojo;

import static org.assertj.core.api.Assertions.*;

public class PojoTest {

    @Test
    public void testSetId() throws Exception {
        Person person = new Person();
        person.setId(System.currentTimeMillis());
        String id = "99999";

        Pojo.register(Person.class);
        Pojo.setId(person, id);

        PersonAssert.assertThat(person).hasId(Long.parseLong(id));
    }

    @Test
    public void testGetId() throws Exception {
        Long id = System.currentTimeMillis();

        Person person = new Person();
        person.setId(id);

        Pojo.register(Person.class);

        assertThat(Pojo.getId(person)).isEqualTo(id);
    }

}
