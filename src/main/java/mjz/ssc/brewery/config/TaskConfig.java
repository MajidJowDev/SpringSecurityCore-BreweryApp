package mjz.ssc.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//Related to Spring Schedule task
@EnableScheduling
@EnableAsync
@Configuration
public class TaskConfig {

    @Bean
    TaskExecutor taskExecutor() {
        // Basic configuration of TaskExecutor and scheduling process
        // added to periodically check if there are any locked accounts so we can automatically unlock them
        return new SimpleAsyncTaskExecutor();
    }
}
