package net.tisseurdetoile.batch.socle.specification.operator;

/**
 * Definition des specification et de leur liens.
 *
 * @param <E> Type objet sujet de la specification.
 * @author tisseurDeToile
 */
public interface ISpecification<E> {
    /**
     * evaluation de la specification.
     *
     * @param candidate sujet de la specification.
     * @return resultat de la specification.
     */
    boolean isSatisfiedBy(E candidate);

    /**
     * Lien de specification OR (OU).
     * @param specification specification à lier.
     * @return resultat de la specification
     */
    ISpecification<E> or(ISpecification<E> specification);

    /**
     * Lien de specification ET (ET).
     * @param specification specification à lier.
     * @return resultat de la specification
     */
    ISpecification<E> and(ISpecification<E> specification);

    /**
     * Lien de specification not.
     @return inverse le resultat de la specification
     */
    ISpecification<E> not();
}
