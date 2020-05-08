package net.tisseurdetoile.batch.socle.specification.operator;

/**
 * @param <E> Object sujet de la specification.
 * @author tisseurDeToile
 */
public class AndSpecification<E> extends AbstractSpecification<E> {

    /**
     * tableau des specification.
     */
    private final ISpecification<E>[] specifications;

    /**
     * Liens ET.
     * @param iSpecifications les autres specifications a traiter en ET
     */
    @SafeVarargs
    public AndSpecification(final ISpecification<E>...iSpecifications) {
        specifications = iSpecifications;
    }

    @Override
    public final boolean isSatisfiedBy(final E candidate) {
        boolean result = true;
        for (ISpecification<E> iSpecification : specifications) {
            result &= iSpecification.isSatisfiedBy(candidate);
            if (!result) {
                break;
            }
        }
        return result;
    }
}