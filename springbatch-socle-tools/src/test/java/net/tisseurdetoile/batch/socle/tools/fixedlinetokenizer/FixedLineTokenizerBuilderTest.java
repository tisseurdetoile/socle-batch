package net.tisseurdetoile.batch.socle.tools.fixedlinetokenizer;

import net.tisseurdetoile.batch.socle.tools.fixedlinetokenizer.FixedLineTokenizer;
import net.tisseurdetoile.batch.socle.tools.fixedlinetokenizer.FixedLineTokenizerBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class FixedLineTokenizerBuilderTest {

    @Test
    public void test_passant_simple() {
        FixedLineTokenizer tokenIzer = FixedLineTokenizerBuilder.newBuilder().start("header",5)
                .next("ttra", 2)
                .next("cste", 3)
                .next("nexeeve", 4)
                .next("neve",7)
                .next("nsoc", 7).build();

        String strAttributesArrays = "[header, ttra, cste, nexeeve, neve, nsoc]";
        String strRangesArrays = "[1-5, 6-7, 8-10, 11-14, 15-21, 22-28]";

        Assert.assertEquals(strRangesArrays, Arrays.toString(tokenIzer.getRanges()));
        Assert.assertEquals(strAttributesArrays, Arrays.toString(tokenIzer.getAttributes()));
    }

    @Test
    public void test_passant_complexe() {
        FixedLineTokenizer tokenIzer = FixedLineTokenizerBuilder.newBuilder().start("header",5)
                .next("ttra", 2)
                .next("cste", 3)
                .next("lnompnm", 32, 89)
                .next("errorTech",3, 593)
                .next("errorFonc", 3).build();


        String strAttributesArrays = "[header, ttra, cste, lnompnm, errorTech, errorFonc]";
        String strRangesArrays = "[1-5, 6-7, 8-10, 89-120, 593-595, 596-598]";

        Assert.assertEquals(strRangesArrays, Arrays.toString(tokenIzer.getRanges()));
        Assert.assertEquals(strAttributesArrays, Arrays.toString(tokenIzer.getAttributes()));

    }


    @Test(expected = IllegalArgumentException.class)
    public void errorlengthTest() {
        FixedLineTokenizer error = FixedLineTokenizerBuilder.newBuilder().start("error", 0).build();
        FixedLineTokenizer error2 = FixedLineTokenizerBuilder.newBuilder().start("startok", 1).next("error", 0).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void errorskipTest() {
        FixedLineTokenizer error = FixedLineTokenizerBuilder.newBuilder().start("error", 1, -1).build();
        FixedLineTokenizer error2 = FixedLineTokenizerBuilder.newBuilder().start("startok", 1).next("error", 2, -1).build();
    }
}