package uk.co.nominet.stevemarks.postit.migration.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import uk.co.nominet.stevemarks.postit.domain.PostIt;
import uk.co.nominet.stevemarks.postit.migration.processors.JobCompletionNotificationListener;
import uk.co.nominet.stevemarks.postit.migration.processors.PostItNoteItemProcessor;
import uk.co.nominet.stevemarks.postit.migration.processors.PostItNoteItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<PostIt> reader() {
        return new FlatFileItemReaderBuilder<PostIt>().name("postitNoteItemReader")
                .resource(new ClassPathResource("admin-notes.csv")).delimited().names(new String[] { "email", "text" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<PostIt>() {
                    {
                        setTargetType(PostIt.class);
                    }
                }).build();
    }

    @Bean
    public PostItNoteItemProcessor processor() {
        return new PostItNoteItemProcessor();
    }

    /*
     * @Bean public JdbcBatchItemWriter<PostIt> writer(DataSource dataSource) {
     * return new JdbcBatchItemWriterBuilder<PostIt>()
     * .itemSqlParameterSourceProvider(new
     * BeanPropertyItemSqlParameterSourceProvider<>())
     * .sql("INSERT INTO postit (email, text) VALUES (:email, :text)").dataSource(
     * dataSource).build(); }
     */
    @Bean
    public PostItNoteItemWriter writer(DataSource dataSource) {
        return new PostItNoteItemWriter();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener).flow(step1)
                .end().build();
    }

    @Bean
    public Step step1(PostItNoteItemWriter writer) {
        return stepBuilderFactory.get("step1").<PostIt, PostIt>chunk(10).reader(reader()).processor(processor())
                .writer(writer).build();
    }
}
