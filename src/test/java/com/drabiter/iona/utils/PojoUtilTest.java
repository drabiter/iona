package com.drabiter.iona.utils;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.junit.Test;

import static org.junit.Assert.*;

public class PojoUtilTest {

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
}
