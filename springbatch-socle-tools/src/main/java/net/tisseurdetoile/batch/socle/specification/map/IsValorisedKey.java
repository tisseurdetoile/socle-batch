package net.tisseurdetoile.batch.socle.specification.map;

import java.util.List;
import java.util.Map;

public class IsValorisedKey extends AbstractMapSpecification  {

    public IsValorisedKey(String key, String val) {
        super(key, val);
    }

    public IsValorisedKey(String key, List<String> vals) {
        super(key, vals);
    }

    @Override
    public boolean isSatisfiedBy(Map<String, String> candidate) {

        if ((candidate != null) && candidate.containsKey(this.getKey())) {
            return true;
        }
        return false;
    }
}
