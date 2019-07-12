package ru.nik.library.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder.RepositoryMethodReference;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.nik.library.domain.User;
import ru.nik.library.repository.datajpa.UserRepository;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	private final Logger log = LoggerFactory.getLogger("Batch");

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

//	@Autowired
//	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private MongoTemplate mongoTemplate;

//	@Autowired
//	private Environment env;

	@Autowired
	public DataSource dataSource;


	@Autowired
	private UserRepository userRepository;


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






//	@Override
//	@Autowired
//	public void setDataSource(DataSource dataSource) {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
//		dataSource.setUrl(env.getProperty("spring.datasource.url"));
//		dataSource.setUsername(env.getProperty("spring.datasource.username"));
//		dataSource.setPassword(env.getProperty("spring.datasource.password"));
//
//		return dataSource;
//	}

//
//
//	@Bean()
//	public DataSource customDataSource() {
//		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("org.postgresql.Driver");
//		dataSource.setUrl("jdbc:postgresql://localhost/library");
//		dataSource.setUsername("postgres");
//		dataSource.setPassword("postgres");
//
//		return dataSource;
//	}

//	@Bean
//	public ItemReader<User> userItemReader() throws Exception {
//        /*return new MongoItemReaderBuilder<Person>()
//                .name("mongoPersonReader")
//                .template(mongoTemplate)
//                .targetType(Person.class)
//                .jsonQuery("{}")
//                .sorts(new HashMap<>())
//                .build();*/
////		return new FlatFileItemReaderBuilder<Person>()
////			.name("personItemReader")
////			.resource(new FileSystemResource("entries.csv"))
////			.delimited()
////			.names(new String[]{"name", "age"})
////			.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
////				setTargetType(Person.class);
////			}})
////			.build();
//
//		JpaPagingItemReader<User> databaseReader = new JpaPagingItemReader<>();
//		databaseReader.setEntityManagerFactory(entityManagerFactory);
//		JpaQueryProviderImpl<User> jpaQueryProvider = new JpaQueryProviderImpl<>();
//		jpaQueryProvider.setQuery("select u from User u");
//		databaseReader.setQueryProvider(jpaQueryProvider);
//		databaseReader.setPageSize(1000);
//		databaseReader.afterPropertiesSet();
//		return databaseReader;
//	}
//
//	@Bean
//	public JdbcCursorItemReader jdbcCursorItemReader() {
//		JdbcCursorItemReader personJdbcCursorItemReader = new JdbcCursorItemReader<>();
//		personJdbcCursorItemReader.setSql("select * from library_users");
//		personJdbcCursorItemReader.setDataSource(dataSource);
//		personJdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<>(User.class));
//		return personJdbcCursorItemReader;
//	}


//	@Bean
//	public RepositoryItemReader<User> reader() {
////		return new RepositoryItemReaderBuilder<User>().repository(userRepository)
////			.name("userItemReader")
////			.sorts(new HashMap<>())
////			.methodName("findAll")
////			.build();
//
//		RepositoryItemReader<User> reader = new RepositoryItemReader<>();
//		reader.setRepository(userRepository);
//		reader.setMethodName("findAll");
//		reader.setSort(new HashMap<>());
//		return reader;
//	}

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
