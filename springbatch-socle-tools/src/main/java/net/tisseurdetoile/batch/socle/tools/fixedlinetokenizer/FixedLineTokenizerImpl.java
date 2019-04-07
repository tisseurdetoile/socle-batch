package net.tisseurdetoile.batch.socle.tools.fixedlinetokenizer;

import org.springframework.batch.item.file.transform.Range;

public class FixedLineTokenizerImpl implements FixedLineTokenizer{
    private String[] attributes;
    private Range[] ranges;

    public FixedLineTokenizerImpl(String[] attributes, Range[] ranges) {
        this.attributes = attributes;
        this.ranges = ranges;
    }

    /**
     * retourne la liste des attributs a mapper
     * @return un tablean de la liste des attributs
     */
    public String[] getAttributes() {
        return attributes;
    }

    /**
     * retourne les position des attributs dans la ligne lue.
     * @return la liste des Range correspondant.
     */
    public Range[] getRanges(){
        return ranges;
    }

}
