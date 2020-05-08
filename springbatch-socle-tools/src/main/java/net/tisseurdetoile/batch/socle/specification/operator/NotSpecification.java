package net.tisseurdetoile.batch.socle.specification.operator;

/**
 *
 * Traitement des specification NOT.
 * @param <E> Objet sujet de la specification
 * @author tisseurDeToile
 */
public class NotSpecification<E> extends AbstractSpecification<E> {

    /**
     * specifciation a traiter.
     */
    private final ISpecification<E> specification;

    /**
     * Liens NOT.
     * @param iSpecification specification en Not.
     */
    public NotSpecification(final ISpecification<E> iSpecification) {
        specification = iSpecification;
    }

    @Override
    public final boolean isSatisfiedBy(final E candidate) {
        return !specification.isSatisfiedBy(candidate);
    }

}