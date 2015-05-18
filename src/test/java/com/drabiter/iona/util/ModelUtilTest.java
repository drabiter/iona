package com.drabiter.iona.util;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.junit.Test;

import com.drabiter.iona.annotation.MentalModel;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.util.PojoUtil;

import static org.assertj.core.api.Assertions.*;

import static org.junit.Assert.*;

public class ModelUtilTest {

    @Test
    public void testGetIdPublicField() throws Exception {
        Field field = PojoUtil.findIdField(PublicFoo.class);

        assertNotNull(field);
        assertEquals("myId", field.getName());
    }

    @Test
    public void testGetIdPrivateField() throws Exception {
        Field field = PojoUtil.findIdField(PrivateFoo.class);

        assertNotNull(field);
        assertEquals("myId", field.getName());
    }

    @Test(expected = IonaException.class)
    public void testNotFoundIdField() throws Exception {
        PojoUtil.findIdField(CustomEndpoint.class);
    }

    @Test
    public void testGetClassCustomEndpoint() throws Exception {
        assertThat(PojoUtil.getEndpoint(CustomEndpoint.class)).isEqualTo("/endpoint");
    }

    @Test
    public void testGetParentClassCustomEndpoint() throws Exception {
        assertThat(PojoUtil.getEndpoint(NoCustomEndpoint.class)).isEqualTo("/endpoint");
    }

    @Test
    public void testGetChildCustomEndpoint() throws Exception {
        assertThat(PojoUtil.getEndpoint(AnotherCustomEndpoint.class)).isEqualTo("/anotherendpoint");
    }

    @Test
    public void testCastIdString() throws Exception {
        assertThat(PojoUtil.castId("", String.class)).isEqualTo("");
        assertThat(PojoUtil.castId(" ", String.class)).isEqualTo(" ");
        assertThat(PojoUtil.castId("abc", String.class)).isEqualTo("abc");
        assertThat(PojoUtil.castId(null, String.class)).isNull();
    }

    @Test
    public void testCastIdLong() throws Exception {
        assertThat(PojoUtil.castId("1", long.class)).isEqualTo(1L);

        try {
            PojoUtil.castId(null, long.class);
            fail();
        } catch (NumberFormatException e) {
        }

        try {
            PojoUtil.castId("", long.class);
            fail();
        } catch (NumberFormatException e) {
        }

        try {
            PojoUtil.castId(" ", long.class);
            fail();
        } catch (NumberFormatException e) {
        }
    }

    @Test(expected = IonaException.class)
    public void testCastIdUnsupportedType() throws Exception {
        PojoUtil.castId("2.9", Double.class);
    }

    class PublicFoo {
        @Id
        public long myId;
    }

    class PrivateFoo {
        @Id
        private long myId;

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
}
