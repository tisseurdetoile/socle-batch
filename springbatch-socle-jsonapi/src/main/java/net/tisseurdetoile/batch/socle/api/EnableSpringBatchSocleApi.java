package net.tisseurdetoile.batch.socle.api;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({SpringBatchSocleApiConfiguration.class})
public @interface EnableSpringBatchSocleApi {
}
