package ru.nik.library.migration;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.domain.Genre;
import ru.nik.library.domain.Role;
import ru.nik.library.domain.User;

@EnableBatchProcessing
@Configuration
public class BatchConfig extends DefaultBatchConfigurer {

	private final Logger log = LoggerFactory.getLogger("Batch");

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private EntityManagerFactory entityManagerFactory;


	@Bean
	public JpaPagingItemReader<User> userReader() {
		JpaPagingItemReaderBuilder<User> readerBuilder = new JpaPagingItemReaderBuilder<>();
		readerBuilder.name("userReader");
		readerBuilder.entityManagerFactory(entityManagerFactory);
		readerBuilder.queryString("select u from User u");
		return readerBuilder.build();
	}

	@Bean
	public JpaPagingItemReader<Role> roleReader() {
		JpaPagingItemReaderBuilder<Role> readerBuilder = new JpaPagingItemReaderBuilder<>();
		readerBuilder.name("roleReader");
		readerBuilder.entityManagerFactory(entityManagerFactory);
		readerBuilder.queryString("select u from Role u");
		return readerBuilder.build();
	}

	@Bean
	public JpaPagingItemReader<Author> authorReader() {
		JpaPagingItemReaderBuilder<Author> readerBuilder = new JpaPagingItemReaderBuilder<>();
		readerBuilder.name("authorReader");
		readerBuilder.entityManagerFactory(entityManagerFactory);
		readerBuilder.queryString("select u from Author u");
		return readerBuilder.build();
	}

	@Bean
	public JpaPagingItemReader<Genre> genreReader() {
		JpaPagingItemReaderBuilder<Genre> readerBuilder = new JpaPagingItemReaderBuilder<>();
		readerBuilder.name("genreReader");
		readerBuilder.entityManagerFactory(entityManagerFactory);
		readerBuilder.queryString("select u from Genre u");
		return readerBuilder.build();
	}

	@Bean
	public JpaPagingItemReader<Book> bookReader() {
		JpaPagingItemReaderBuilder<Book> readerBuilder = new JpaPagingItemReaderBuilder<>();
		readerBuilder.name("bookReader");
		readerBuilder.entityManagerFactory(entityManagerFactory);
		readerBuilder.queryString("select u from Book u");
		return readerBuilder.build();
	}

	@Bean
	public JpaPagingItemReader<Comment> commentReader() {
		JpaPagingItemReaderBuilder<Comment> readerBuilder = new JpaPagingItemReaderBuilder<>();
		readerBuilder.name("commentReader");
		readerBuilder.entityManagerFactory(entityManagerFactory);
		readerBuilder.queryString("select u from Comment u");
		return readerBuilder.build();
	}


	@Bean
	public ItemProcessor userProcessor() {
		return (ItemProcessor<User, User>) user -> user;
	}

	@Bean
	public ItemProcessor roleProcessor() {
		return (ItemProcessor<Role, Role>) role -> role;
	}

	@Bean
	public ItemProcessor authorProcessor() {
		return (ItemProcessor<Author, Author>) author -> author;
	}

	@Bean
	public ItemProcessor genreProcessor() {
		return (ItemProcessor<Genre, Genre>) genre -> genre;
	}

	@Bean
	public ItemProcessor bookProcessor() {
		return (ItemProcessor<Book, Book>) book -> book;
	}

	@Bean
	public ItemProcessor commentProcessor() {
		return (ItemProcessor<Comment, Comment>) comment -> comment;
	}


	@Bean
	public MongoItemWriter userWriter() {
		return new MongoItemWriterBuilder<User>()
			.collection("library_users").template(mongoTemplate).build();
	}

	@Bean
	public MongoItemWriter roleWriter() {
		return new MongoItemWriterBuilder<User>()
			.collection("mongo_roles").template(mongoTemplate).build();
	}

	@Bean
	public MongoItemWriter authorWriter() {
		return new MongoItemWriterBuilder<User>()
			.collection("mongo_authors").template(mongoTemplate).build();
	}

	@Bean
	public MongoItemWriter genreWriter() {
		return new MongoItemWriterBuilder<User>()
			.collection("mongo_genres").template(mongoTemplate).build();
	}

	@Bean
	public MongoItemWriter bookWriter() {
		return new MongoItemWriterBuilder<User>()
			.collection("mongo_books").template(mongoTemplate).build();
	}

	@Bean
	public MongoItemWriter commentWriter() {
		return new MongoItemWriterBuilder<User>()
			.collection("mongo_comments").template(mongoTemplate).build();
	}


	@Bean
	public Job importUserJob(Step step1) {
		return jobBuilderFactory.get("importUserJob")
			.incrementer(new RunIdIncrementer())
			.flow(step1)
			.end()
			.listener(new JobExecutionListener() {
				@Override
				public void beforeJob(JobExecution jobExecution) {
					log.info("Начало job");
				}

				@Override
				public void afterJob(JobExecution jobExecution) {
					log.info("Конец job");
				}
			})
			.build();
	}

	@Bean
	public Job importRoleJob(Step step2) {
		return jobBuilderFactory.get("importRoleJob")
			.incrementer(new RunIdIncrementer())
			.flow(step2)
			.end()
			.listener(new JobExecutionListener() {
				@Override
				public void beforeJob(JobExecution jobExecution) {
					log.info("Начало job");
				}

				@Override
				public void afterJob(JobExecution jobExecution) {
					log.info("Конец job");
				}
			})
			.build();
	}

	@Bean
	public Job importAuthorJob(Step step3) {
		return jobBuilderFactory.get("importAuthorJob")
			.incrementer(new RunIdIncrementer())
			.flow(step3)
			.end()
			.listener(new JobExecutionListener() {
				@Override
				public void beforeJob(JobExecution jobExecution) {
					log.info("Начало job");
				}

				@Override
				public void afterJob(JobExecution jobExecution) {
					log.info("Конец job");
				}
			})
			.build();
	}

	@Bean
	public Job importGenreJob(Step step4) {
		return jobBuilderFactory.get("importGenreJob")
			.incrementer(new RunIdIncrementer())
			.flow(step4)
			.end()
			.listener(new JobExecutionListener() {
				@Override
				public void beforeJob(JobExecution jobExecution) {
					log.info("Начало job");
				}

				@Override
				public void afterJob(JobExecution jobExecution) {
					log.info("Конец job");
				}
			})
			.build();
	}

	@Bean
	public Job importBookJob(Step step5) {
		return jobBuilderFactory.get("importBookJob")
			.incrementer(new RunIdIncrementer())
			.flow(step5)
			.end()
			.listener(new JobExecutionListener() {
				@Override
				public void beforeJob(JobExecution jobExecution) {
					log.info("Начало job");
				}

				@Override
				public void afterJob(JobExecution jobExecution) {
					log.info("Конец job");
				}
			})
			.build();
	}

	@Bean
	public Job importCommentJob(Step step6) {
		return jobBuilderFactory.get("importCommentJob")
			.incrementer(new RunIdIncrementer())
			.flow(step6)
			.end()
			.listener(new JobExecutionListener() {
				@Override
				public void beforeJob(JobExecution jobExecution) {
					log.info("Начало job");
				}

				@Override
				public void afterJob(JobExecution jobExecution) {
					log.info("Конец job");
				}
			})
			.build();
	}


	@Bean
	public Step step1(@Qualifier("userWriter") MongoItemWriter writer,
		@Qualifier("userReader") ItemReader reader,
		@Qualifier("userProcessor") ItemProcessor itemProcessor) {
		return stepBuilderFactory.get("step1")
			.chunk(5)
			.reader(reader)
			.processor(itemProcessor)
			.writer(writer)
			.listener(new ItemReadListener() {
				public void beforeRead() {
					log.info("Начало чтения");
				}

				public void afterRead(Object o) {
					log.info("Конец чтения");
				}

				public void onReadError(Exception e) {
					log.info("Ошибка чтения");
				}
			})
			.listener(new ItemWriteListener() {
				public void beforeWrite(List list) {
					log.info("Начало записи");
				}

				public void afterWrite(List list) {
					log.info("Конец записи");
				}

				public void onWriteError(Exception e, List list) {
					log.info("Ошибка записи");
				}
			})
			.listener(new ItemProcessListener() {
				public void beforeProcess(Object o) {
					log.info("Начало обработки");
				}

				public void afterProcess(Object o, Object o2) {
					log.info("Конец обработки");
				}

				public void onProcessError(Object o, Exception e) {
					log.info("Ошбка обработки");
				}
			})
			.listener(new ChunkListener() {
				public void beforeChunk(ChunkContext chunkContext) {
					log.info("Начало пачки");
				}

				public void afterChunk(ChunkContext chunkContext) {
					log.info("Конец пачки");
				}

				public void afterChunkError(ChunkContext chunkContext) {
					log.info("Ошибка пачки");
				}
			})
			.build();
	}

	@Bean
	public Step step2(@Qualifier("roleWriter") MongoItemWriter writer,
		@Qualifier("roleReader") ItemReader reader,
		@Qualifier("roleProcessor") ItemProcessor itemProcessor) {
		return stepBuilderFactory.get("step2")
			.chunk(5)
			.reader(reader)
			.processor(itemProcessor)
			.writer(writer)
			.listener(new ItemReadListener() {
				public void beforeRead() {
					log.info("Начало чтения");
				}

				public void afterRead(Object o) {
					log.info("Конец чтения");
				}

				public void onReadError(Exception e) {
					log.info("Ошибка чтения");
				}
			})
			.listener(new ItemWriteListener() {
				public void beforeWrite(List list) {
					log.info("Начало записи");
				}

				public void afterWrite(List list) {
					log.info("Конец записи");
				}

				public void onWriteError(Exception e, List list) {
					log.info("Ошибка записи");
				}
			})
			.listener(new ItemProcessListener() {
				public void beforeProcess(Object o) {
					log.info("Начало обработки");
				}

				public void afterProcess(Object o, Object o2) {
					log.info("Конец обработки");
				}

				public void onProcessError(Object o, Exception e) {
					log.info("Ошбка обработки");
				}
			})
			.listener(new ChunkListener() {
				public void beforeChunk(ChunkContext chunkContext) {
					log.info("Начало пачки");
				}

				public void afterChunk(ChunkContext chunkContext) {
					log.info("Конец пачки");
				}

				public void afterChunkError(ChunkContext chunkContext) {
					log.info("Ошибка пачки");
				}
			})
			.build();
	}

	@Bean
	public Step step3(@Qualifier("authorWriter") MongoItemWriter writer,
		@Qualifier("authorReader") ItemReader reader,
		@Qualifier("authorProcessor") ItemProcessor itemProcessor) {
		return stepBuilderFactory.get("step3")
			.chunk(5)
			.reader(reader)
			.processor(itemProcessor)
			.writer(writer)
			.listener(new ItemReadListener() {
				public void beforeRead() {
					log.info("Начало чтения");
				}

				public void afterRead(Object o) {
					log.info("Конец чтения");
				}

				public void onReadError(Exception e) {
					log.info("Ошибка чтения");
				}
			})
			.listener(new ItemWriteListener() {
				public void beforeWrite(List list) {
					log.info("Начало записи");
				}

				public void afterWrite(List list) {
					log.info("Конец записи");
				}

				public void onWriteError(Exception e, List list) {
					log.info("Ошибка записи");
				}
			})
			.listener(new ItemProcessListener() {
				public void beforeProcess(Object o) {
					log.info("Начало обработки");
				}

				public void afterProcess(Object o, Object o2) {
					log.info("Конец обработки");
				}

				public void onProcessError(Object o, Exception e) {
					log.info("Ошбка обработки");
				}
			})
			.listener(new ChunkListener() {
				public void beforeChunk(ChunkContext chunkContext) {
					log.info("Начало пачки");
				}

				public void afterChunk(ChunkContext chunkContext) {
					log.info("Конец пачки");
				}

				public void afterChunkError(ChunkContext chunkContext) {
					log.info("Ошибка пачки");
				}
			})
			.build();
	}

	@Bean
	public Step step4(@Qualifier("genreWriter") MongoItemWriter writer,
		@Qualifier("genreReader") ItemReader reader,
		@Qualifier("genreProcessor") ItemProcessor itemProcessor) {
		return stepBuilderFactory.get("step4")
			.chunk(5)
			.reader(reader)
			.processor(itemProcessor)
			.writer(writer)
			.listener(new ItemReadListener() {
				public void beforeRead() {
					log.info("Начало чтения");
				}

				public void afterRead(Object o) {
					log.info("Конец чтения");
				}

				public void onReadError(Exception e) {
					log.info("Ошибка чтения");
				}
			})
			.listener(new ItemWriteListener() {
				public void beforeWrite(List list) {
					log.info("Начало записи");
				}

				public void afterWrite(List list) {
					log.info("Конец записи");
				}

				public void onWriteError(Exception e, List list) {
					log.info("Ошибка записи");
				}
			})
			.listener(new ItemProcessListener() {
				public void beforeProcess(Object o) {
					log.info("Начало обработки");
				}

				public void afterProcess(Object o, Object o2) {
					log.info("Конец обработки");
				}

				public void onProcessError(Object o, Exception e) {
					log.info("Ошбка обработки");
				}
			})
			.listener(new ChunkListener() {
				public void beforeChunk(ChunkContext chunkContext) {
					log.info("Начало пачки");
				}

				public void afterChunk(ChunkContext chunkContext) {
					log.info("Конец пачки");
				}

				public void afterChunkError(ChunkContext chunkContext) {
					log.info("Ошибка пачки");
				}
			})
			.build();
	}

	@Bean
	public Step step5(@Qualifier("bookWriter") MongoItemWriter writer,
		@Qualifier("bookReader") ItemReader reader,
		@Qualifier("bookProcessor") ItemProcessor itemProcessor) {
		return stepBuilderFactory.get("step5")
			.chunk(5)
			.reader(reader)
			.processor(itemProcessor)
			.writer(writer)
			.listener(new ItemReadListener() {
				public void beforeRead() {
					log.info("Начало чтения");
				}

				public void afterRead(Object o) {
					log.info("Конец чтения");
				}

				public void onReadError(Exception e) {
					log.info("Ошибка чтения");
				}
			})
			.listener(new ItemWriteListener() {
				public void beforeWrite(List list) {
					log.info("Начало записи");
				}

				public void afterWrite(List list) {
					log.info("Конец записи");
				}

				public void onWriteError(Exception e, List list) {
					log.info("Ошибка записи");
				}
			})
			.listener(new ItemProcessListener() {
				public void beforeProcess(Object o) {
					log.info("Начало обработки");
				}

				public void afterProcess(Object o, Object o2) {
					log.info("Конец обработки");
				}

				public void onProcessError(Object o, Exception e) {
					log.info("Ошбка обработки");
				}
			})
			.listener(new ChunkListener() {
				public void beforeChunk(ChunkContext chunkContext) {
					log.info("Начало пачки");
				}

				public void afterChunk(ChunkContext chunkContext) {
					log.info("Конец пачки");
				}

				public void afterChunkError(ChunkContext chunkContext) {
					log.info("Ошибка пачки");
				}
			})
			.build();
	}

	@Bean
	public Step step6(@Qualifier("commentWriter") MongoItemWriter writer,
		@Qualifier("commentReader") ItemReader reader,
		@Qualifier("commentProcessor") ItemProcessor itemProcessor) {
		return stepBuilderFactory.get("step6")
			.chunk(5)
			.reader(reader)
			.processor(itemProcessor)
			.writer(writer)
			.listener(new ItemReadListener() {
				public void beforeRead() {
					log.info("Начало чтения");
				}

				public void afterRead(Object o) {
					log.info("Конец чтения");
				}

				public void onReadError(Exception e) {
					log.info("Ошибка чтения");
				}
			})
			.listener(new ItemWriteListener() {
				public void beforeWrite(List list) {
					log.info("Начало записи");
				}

				public void afterWrite(List list) {
					log.info("Конец записи");
				}

				public void onWriteError(Exception e, List list) {
					log.info("Ошибка записи");
				}
			})
			.listener(new ItemProcessListener() {
				public void beforeProcess(Object o) {
					log.info("Начало обработки");
				}

				public void afterProcess(Object o, Object o2) {
					log.info("Конец обработки");
				}

				public void onProcessError(Object o, Exception e) {
					log.info("Ошбка обработки");
				}
			})
			.listener(new ChunkListener() {
				public void beforeChunk(ChunkContext chunkContext) {
					log.info("Начало пачки");
				}

				public void afterChunk(ChunkContext chunkContext) {
					log.info("Конец пачки");
				}

				public void afterChunkError(ChunkContext chunkContext) {
					log.info("Ошибка пачки");
				}
			})
			.build();
	}

}
