package net.tisseurdetoile.batch.socle.specification.map;

import net.tisseurdetoile.batch.socle.specification.operator.ISpecification;
import org.junit.Assert;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

public class IsKeyValueInTest {

    private final Map<String, String> testMap1 = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>("param1", "value1"),
            new AbstractMap.SimpleImmutableEntry<>("param2", "value2"));

    @Test
    public final void testPassantSimple() {
        ISpecification<Map<String, String>> specification = new IsKeyValueIn("param1", "value1");

        Assert.assertTrue(specification.isSatisfiedBy(testMap1));
    }

    @Test
    public void testPassantSimpleKo() {
        ISpecification<Map<String, String>>  specification = new IsKeyValueIn("param3", "value1");

        Assert.assertFalse(specification.isSatisfiedBy(testMap1));
    }

    @Test
    public void testPassantSimpleList() {
        ISpecification<Map<String, String>>  specification = new IsKeyValueIn("param1", Arrays.asList("value10", "value2", "value1"));

        Assert.assertTrue(specification.isSatisfiedBy(testMap1));
    }

    @Test
    public void testPassantSimpleListKO() {
        ISpecification<Map<String, String>>  specification = new IsKeyValueIn("param1", Arrays.asList("value10", "value2", "val11"));

        Assert.assertFalse(specification.isSatisfiedBy(testMap1));
    }
}