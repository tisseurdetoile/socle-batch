package net.tisseurdetoile.batch.socle.specification.map;

import lombok.Getter;
import net.tisseurdetoile.batch.socle.specification.CompareMode;
import net.tisseurdetoile.batch.socle.specification.operator.AbstractSpecification;

import java.util.List;
import java.util.Map;

@Getter
public abstract class AbstractMapSpecification extends AbstractSpecification<Map<String, String>> {
    /**
     * paramètre a verifier.
     */
    private final String key;

    /**
     * valeur a comparer.
     */
    private String value;
    /**
     * valeurs a comparer.
     */
    private List<String> values;

    /**
     * mode de comparaison.
     */
    private final CompareMode compareMode;

    /**
     * Constructeur de la specifcation avec test de chaine.
     *
     * @param key nom du paramètre
     * @param val valeur du paramètre
     */
    public AbstractMapSpecification(final String key, final String val) {
        this.key = key;
        this.value = val;
        this.compareMode = CompareMode.STRING;
    }

    /**
     * Constructeur de la specifcation avec test de chaine.
     *
     * @param key nom du paramètre
     * @param vals valeur du paramètre
     */
    public AbstractMapSpecification(final String key, final List<String> vals) {

        this.values = vals;
        this.key = key;
        this.compareMode = CompareMode.LIST;

    }

    protected boolean isKeyIn (Map<String, String> candidate) {
        return candidate != null && (candidate.get(this.key) != null);
    }

}
