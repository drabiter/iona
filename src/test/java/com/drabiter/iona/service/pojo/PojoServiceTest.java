package com.drabiter.iona.service.pojo;

import org.junit.Test;

import com.drabiter.iona._meta.Person;
import com.drabiter.iona._meta.PersonAssert;
import com.drabiter.iona.service.pojo.PojoService;

import static org.assertj.core.api.Assertions.*;

public class PojoServiceTest {

    @Test
    public void testSetId() throws Exception {
        Person person = new Person();
        person.setId(System.currentTimeMillis());
        String id = "99999";

        PojoService.register(Person.class);
        PojoService.setId(person, id);

        PersonAssert.assertThat(person).hasId(Long.parseLong(id));
    }

    @Test
    public void testGetId() throws Exception {
        Long id = System.currentTimeMillis();

        Person person = new Person();
        person.setId(id);

        PojoService.register(Person.class);

        assertThat(PojoService.getId(person)).isEqualTo(id);
    }

}
