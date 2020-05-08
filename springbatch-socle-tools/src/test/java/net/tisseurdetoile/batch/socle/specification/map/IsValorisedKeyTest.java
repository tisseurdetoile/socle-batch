package net.tisseurdetoile.batch.socle.specification.map;

import net.tisseurdetoile.batch.socle.specification.operator.ISpecification;
import org.junit.Assert;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;

public class IsValorisedKeyTest {

    private final Map<String, String> testMap1 = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>("param1", "value1"),
            new AbstractMap.SimpleImmutableEntry<>("param2", "value2"));

    @Test
    public void testPassantSimple () {
        ISpecification<Map<String, String>> specification = new IsValorisedKey("param1", "");

        Assert.assertTrue(specification.isSatisfiedBy(testMap1));
    }

    @Test
    public void testPassantSimpleKo () {
        ISpecification<Map<String, String>> specification = new IsValorisedKey("param3", "");

        Assert.assertFalse(specification.isSatisfiedBy(testMap1));
    }
}