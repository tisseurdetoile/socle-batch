package net.tisseurdetoile.batch.socle.tools.item;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;

import java.util.List;

/**
 * Logs each content item by using log4j
 * @param <T> Type de l'objet a traité
 */
@Log4j2
public class ConsoleItemWriter<T> implements ItemWriter<T> {

    public static final String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");

    private FormatterLineAggregator<T> lineAggregator;
    protected String lineSeparator = DEFAULT_LINE_SEPARATOR;

    public void setLineAggregator(FormatterLineAggregator<T> lineAggregator) {
        this.lineAggregator = lineAggregator;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public ConsoleItemWriter(FormatterLineAggregator<T> lineAggregator, String lineSeparator) {
        this.lineAggregator = lineAggregator;
        this.lineSeparator = lineSeparator;
    }

    public ConsoleItemWriter(FormatterLineAggregator<T> lineAggregator) {
        this.lineAggregator = lineAggregator;
    }

    public ConsoleItemWriter() {
    }

    @Override
    public void write(List<? extends T> items) {
        log.debug("{} writer start", this.getClass().getSimpleName());

        items.forEach(item -> log.info("{}{}",
                (this.lineAggregator != null)?
                this.lineAggregator.aggregate(item) : item.toString(),
                this.lineSeparator));

        log.debug("{} writer end", this.getClass().getSimpleName());
    }
}