package net.tisseurdetoile.batch.socle.tools.item;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;

import java.util.List;

/**
 * Logs each content item by using toString. method.
 * @param <T>
 */
@Log4j2
public class ConsoleItemWriter<T> implements ItemWriter<T> {

    public static final String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");

    private FormatterLineAggregator lineAggregator;
    protected String lineSeparator = DEFAULT_LINE_SEPARATOR;
    private boolean sysOut = false;

    public void setLineAggregator(FormatterLineAggregator lineAggregator) {
        this.lineAggregator = lineAggregator;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public void setSysOut(boolean sysOut) {
        this.sysOut = sysOut;
    }

    public ConsoleItemWriter(FormatterLineAggregator lineAggregator, String lineSeparator) {
        this.lineAggregator = lineAggregator;
        this.lineSeparator = lineSeparator;
    }

    public ConsoleItemWriter(FormatterLineAggregator lineAggregator) {
        this.lineAggregator = lineAggregator;
    }

    public ConsoleItemWriter() {
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        log.debug(String.format("%s writer start", this.getClass().getSimpleName()));

        String output;

        for (T item : items) {
            if (this.lineAggregator != null) {
                output = (String.format("%s%s", this.lineAggregator.aggregate(item), this.lineSeparator));
            } else {
                output = (String.format("Item={%s}%s", item.toString(), this.lineSeparator));
            }

            if (this.sysOut) {
                System.out.print(output);
            }

            log.info(output);
        }
        log.debug(String.format("%s writer end", this.getClass().getSimpleName()));
    }
}