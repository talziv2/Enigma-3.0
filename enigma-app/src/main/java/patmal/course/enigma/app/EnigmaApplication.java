package patmal.course.enigma.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = {"patmal.course.enigma"})  // Scan all our packages for @Service, @Controller, etc.
// TODO: Re-enable these when database is ready:
// @EntityScan(basePackages = {"patmal.course.enigma.dal.entity"})
// @EnableJpaRepositories(basePackages = {"patmal.course.enigma.dal.repository"})
public class EnigmaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnigmaApplication.class, args);
    }
}