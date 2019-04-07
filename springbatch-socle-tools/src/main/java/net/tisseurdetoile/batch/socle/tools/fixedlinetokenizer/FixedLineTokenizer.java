package net.tisseurdetoile.batch.socle.tools.fixedlinetokenizer;

import org.springframework.batch.item.file.transform.Range;

public interface FixedLineTokenizer {

    /**
     * retourne la liste des attributs a mapper
     * @return un tablean de la liste des attributs
     */
    public String[] getAttributes();

    /**
     * retourne les position des attributs dans la ligne lue.
     * @return la liste des Range correspondant.
     */
    public Range[] getRanges();
}
