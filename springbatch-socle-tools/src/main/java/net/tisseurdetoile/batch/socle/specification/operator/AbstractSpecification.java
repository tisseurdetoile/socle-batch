package net.tisseurdetoile.batch.socle.specification.operator;

/**
 * Implementation abstraite de la specification.
 * @author tisseurdetoile
 * @param <E> Objet sujet de la speciciation.
 */
public abstract class AbstractSpecification<E> implements ISpecification<E>  {

    @Override
    public abstract boolean isSatisfiedBy(E candidate);

    @Override
    public final ISpecification<E> or(final ISpecification<E> otherSpecification) {
        return new OrSpecification<>(this, otherSpecification);
    }

    @Override
    public final ISpecification<E> and(final ISpecification<E> otherSpecification) {
        return new AndSpecification<>(this, otherSpecification);
    }

    @Override
    public final ISpecification<E> not() {
        return new NotSpecification<>(this);
    }
}
