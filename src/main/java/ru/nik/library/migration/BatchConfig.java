package ru.nik.library.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.nik.library.domain.User;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	private final Logger log = LoggerFactory.getLogger("Batch");

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	public DataSource dataSource;

	@Bean
	public JdbcCursorItemReader<User> userReader() {
		JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql("select id, email, enabled, first_name, last_login, last_name, password from library_users");
		reader.setRowMapper(new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setEnabled(rs.getBoolean("enabled"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setPassword(rs.getString("password"));
				user.setLastLogin(rs.getDate("last_login"));
				return user;
			}
		});

		return reader;
	}



	@Bean
	public ItemProcessor processor() {
		return (ItemProcessor<User, User>) user -> {
			return user;
		};
	}

	@Bean
	public MongoItemWriter writer() {
		MongoItemWriter writer = new MongoItemWriter();

		return new MongoItemWriterBuilder<User>()
			.collection("library_users").template(mongoTemplate).build();
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
	public Step step1(MongoItemWriter writer, ItemReader reader, ItemProcessor itemProcessor) {
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
//                .taskExecutor(new SimpleAsyncTaskExecutor())
			.build();
	}


}
