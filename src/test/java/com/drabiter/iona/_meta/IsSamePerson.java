package com.drabiter.iona._meta;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import com.drabiter.iona.util.JsonUtil;

public class IsSamePerson<T> extends BaseMatcher<T> {

    private final Person expectedValue;

    public IsSamePerson(Person expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean matches(Object actualValue) {
        if (actualValue == null) {
            return expectedValue == null;
        }

        Person actualPerson = JsonUtil.get().fromJson((String) actualValue, Person.class);

        return StringUtils.equals(expectedValue.getFirstName(), actualPerson.getFirstName())
                && StringUtils.equals(expectedValue.getLastName(), actualPerson.getLastName());
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedValue);
    }

    @Factory
    public static Matcher<Person> isSamePerson(Person operand) {
        return new IsSamePerson<Person>(operand);
    }

}
