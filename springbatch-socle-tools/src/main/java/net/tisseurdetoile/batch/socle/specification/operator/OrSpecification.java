package net.tisseurdetoile.batch.socle.specification.operator;

/**
 * Traitement des specification en ou.
 * @param <E> objet sujet de la specification.
 * @author tisseurDeToile
 */
public class OrSpecification<E> extends AbstractSpecification<E> {

    /**
     * Les specification a traiter en OU.
     */
    private final ISpecification<E>[] specifications;

    /**
     * specification a traiteren OU.
     * @param iSpecifications liste des specification.
     */
    @SafeVarargs
    public OrSpecification(final ISpecification<E>... iSpecifications) {
        specifications = iSpecifications;
    }

    @Override
    public final boolean isSatisfiedBy(final E candidate) {
        boolean result = false;
        for (ISpecification<E> iSpecification : specifications) {
            result |= iSpecification.isSatisfiedBy(candidate);
            if (result) {
                break;
            }
        }
        return result;
    }

}