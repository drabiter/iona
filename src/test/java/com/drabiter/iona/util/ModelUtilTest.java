package com.drabiter.iona.util;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.junit.Test;

import com.drabiter.iona.annotation.MentalModel;
import com.drabiter.iona.exception.IonaException;
import com.drabiter.iona.util.ModelUtil;

import static org.assertj.core.api.Assertions.*;

import static org.junit.Assert.*;

public class ModelUtilTest {

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
        assertThat(ModelUtil.getEndpoint(AnotherCustomEndpoint.class)).isEqualTo("/anotherendpoint");
    }

    @Test
    public void testCastIdString() throws Exception {
        assertThat(ModelUtil.castId("", String.class)).isEqualTo("");
        assertThat(ModelUtil.castId(" ", String.class)).isEqualTo(" ");
        assertThat(ModelUtil.castId("abc", String.class)).isEqualTo("abc");
        assertThat(ModelUtil.castId(null, String.class)).isNull();
    }

    @Test
    public void testCastIdLong() throws Exception {
        assertThat(ModelUtil.castId("1", long.class)).isEqualTo(1L);

        try {
            ModelUtil.castId(null, long.class);
            fail();
        } catch (NumberFormatException e) {
        }

        try {
            ModelUtil.castId("", long.class);
            fail();
        } catch (NumberFormatException e) {
        }

        try {
            ModelUtil.castId(" ", long.class);
            fail();
        } catch (NumberFormatException e) {
        }
    }

    @Test(expected = IonaException.class)
    public void testCastIdUnsupportedType() throws Exception {
        ModelUtil.castId("2.9", Double.class);
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
