package com.drabiter.iona.utils;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.junit.Test;

import com.drabiter.iona.annotations.MentalModel;
import com.drabiter.iona.exception.IonaException;

import static org.assertj.core.api.Assertions.*;

import static org.junit.Assert.*;

public class ModelUtilTest {

    class PublicFoo {
        @Id
        public long myId;
    }

    class PrivateFoo {
        private long myId;

        @Id
        public long getMyId() {
            return myId;
        }

        public void setMyId(long myId) {
            this.myId = myId;
        }
    }

    @MentalModel(endpoint = "/endpoint")
    class CustomEndpoint {
    }

    class NoCustomEndpoint extends CustomEndpoint {
    }

    @MentalModel(endpoint = "/anotherendpoint")
    class AnotherCustomEndpoint extends CustomEndpoint {
    }

    @Test
    public void testGetIdPublicField() throws Exception {
        Field field = ModelUtil.findIdField(PublicFoo.class);

        assertNotNull(field);
        assertEquals("myId", field.getName());
    }

    @Test
    public void testGetIdPrivateField() throws Exception {
        Field field = ModelUtil.findIdField(PrivateFoo.class);

        assertNotNull(field);
        assertEquals("myId", field.getName());
    }

    @Test(expected = IonaException.class)
    public void testNotFoundIdField() throws Exception {
        ModelUtil.findIdField(CustomEndpoint.class);
    }

    @Test
    public void testGetClassCustomEndpoint() throws Exception {
        assertThat(ModelUtil.getEndpoint(CustomEndpoint.class)).isEqualTo("/endpoint");
    }

    @Test
    public void testGetParentClassCustomEndpoint() throws Exception {
        assertThat(ModelUtil.getEndpoint(NoCustomEndpoint.class)).isEqualTo("/endpoint");
    }

    @Test
    public void testGetChildCustomEndpoint() throws Exception {
        assertThat(ModelUtil.getEndpoint(AnotherCustomEndpoint.class)).isEqualTo("/endpoint");
    }
}
