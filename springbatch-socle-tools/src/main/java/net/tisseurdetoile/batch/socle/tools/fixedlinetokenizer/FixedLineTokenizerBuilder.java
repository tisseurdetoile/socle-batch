package net.tisseurdetoile.batch.socle.tools.fixedlinetokenizer;

import org.springframework.batch.item.file.transform.Range;
import org.springframework.util.Assert;

/**
 * Builder pour faciliter la création de Tokenizer pour les fichier Cobol.
 * @version 1.0
 */
public class FixedLineTokenizerBuilder {

    public static FirstElementStep newBuilder() {
        return new StepBuilder();
    }

    /**
     * Premiere élément du builder
     * Element Suivant du Step : NextElementStep
     */
    public interface FirstElementStep {
        /**
         * La définition de premier attribut dans le flux de la ligne.
         * @param name nom du champ a mapper.
         * @param length taille du champ.
         * @return The first element
         */
        NextElementStep start(String name, int length);

        /**
         * La définition du premier attribut dans le flux de la ligne.
         * @param name nom du champ a mapper.
         * @param length taille du champ.
         * @param jumpTo postion ou sauter pour recommencer à lire le champ.
         * @return Next element
         */

        NextElementStep start(String name, int length, int jumpTo);
    }

    /**
     * Elément suivant du builder
     * Element Suivant du Step : NextElementStep
     * Element Terminant du Step : FixedLineTokenizerImpl
     */
    public interface NextElementStep {
        /**
         * La définition de l'attribut qui suit dans le flux de la ligne.
         * @param name nom du champ a mapper.
         * @param length taille du champ.
         * @return Next Element
         */
        NextElementStep next(String name, int length);

        /**
         * La définition de l'attribut qui suit dans le flux de la ligne.
         * @param name nom du champ a mapper.
         * @param length taille du champ.
         * @param jumpTo postion ou sauter pour recommencer à lire le champ.
         * @return Next Element
         */
        NextElementStep next(String name, int length, int jumpTo);

        /**
         * Retourne un FixedLineTockenizer
         * @return FixedLineTockenizer
         */
        FixedLineTokenizer build();
    }

    private static class StepBuilder implements FirstElementStep, NextElementStep {
        private Entry startEntry = null;
        private Entry currentEntry;
        private int depth = 0;


        @Override
        public NextElementStep start(String name, int length, int jumpTo) {
            Entry entry = new Entry(name, length, jumpTo);
            this.currentEntry = entry;
            this.startEntry = entry;
            this.depth = 1;

            return this;
        }

        @Override
        public NextElementStep start(String name, int length) {
            return start(name, length, 0);
        }


        @Override
        public NextElementStep next(String name, int length) {
            return next(name, length, 0);
        }

        @Override
        public NextElementStep next(String name, int length, int jumpTo) {
            Entry entry = new Entry(name, length, jumpTo);
            this.currentEntry.setChild(entry);
            this.currentEntry = entry;
            this.depth++;

            return this;
        }

        private String[] getAttributes(){
            int ni = 0;
            String[] array = new String[this.depth];

            if (this.startEntry != null) {
                Entry pointer = this.startEntry;

                while (pointer != null ) {
                    array[ni++] = pointer.name;
                    pointer = pointer.getChild();
                }
            }

            return array;
        }

        private Range[] getRanges() {
            int ni = 0;
            int readPos = 0;
            Range[] array = new Range[this.depth];

            if (this.startEntry != null) {
                Entry pointer = this.startEntry;

                while (pointer != null ) {
                    if (pointer.skipTo > 0) {
                        readPos = pointer.skipTo - 1;
                    }

                    int stop = readPos + pointer.length;
                    int start = readPos + 1;

                    array[ni++] = new Range(start, stop);
                    pointer = pointer.getChild();
                    readPos = stop;
                }
            }

            return array;
        }

        @Override
        public FixedLineTokenizer build() {
            return new FixedLineTokenizerImpl(getAttributes(), getRanges());
        }

        /**
         * Classe interne representant les données.
         * qui sont chainées
         */
        class Entry {
            /**
             * nom de l'attribut
             */
            private String name;

            /**
             * private longeur de la chaine à lire;
             */
            private int length;

            /**
             * private ou sauter pour recommencer à lire
             */
            private int skipTo;

            /**
             * element suivant à lire
             */
            private Entry child;

            public Entry(String name, int length, int skipTo) {
                Assert.isTrue(skipTo >= 0, "skipTo must be positive");
                Assert.isTrue(length > 0, "length must be greater than 0");
                Assert.notNull(name, "name can't be null");

                this.name = name;
                this.length = length;
                this.skipTo = skipTo;
            }

            public Entry(String name, int length) {
                this(name, length, 0);
            }

            public void setChild(Entry child) {
                this.child = child;
            }

            public Entry getChild() {
                return child;
            }
        }
    }
}
