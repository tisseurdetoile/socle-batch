package net.tisseurdetoile.batch.socle.specification.map;

import net.tisseurdetoile.batch.socle.specification.CompareMode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class IsKeyValueIn extends AbstractMapSpecification  {

    public IsKeyValueIn(String key, String val) {
        super(key, val);
    }

    public IsKeyValueIn(String key, List<String> vals) {
        super(key, vals);
    }

    @Override
    public boolean isSatisfiedBy(Map<String, String> candidate) {

        if (isKeyIn(candidate)) {
            if (this.getCompareMode() == CompareMode.LIST) {
                return compareListString(candidate);
            }

            return compareString(candidate);
        }
        return false;
    }

    /**
     * Comparaison de la valeur d'un champ par rapport à une Liste de chaine.
     *
     * @param candidate sujet de la comparaison
     * @return resultat de la comparaison
     */
    private boolean compareListString(final Map<String, String> candidate) {
        Set<String> searchedValues = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

            searchedValues.addAll(this.getValues());

            String readAttrValue = candidate.get(this.getKey());

            if (readAttrValue != null) {
                return searchedValues.contains(readAttrValue);
            } else {
                return false;
            }
    }

    /**
     * Comparaison de la valeur d'un champ par rapport à une chaine.
     *
     * @param candidate sujet de la comparaison
     * @return resultat de la comparaison
     */
    private boolean compareString(final Map<String, String> candidate) {

            String readAttrValue = candidate.get(this.getKey());
            if (readAttrValue != null) {
                return this.getValue().equalsIgnoreCase(candidate.get(this.getKey()));
            } else {
                return false;
            }
    }

}
