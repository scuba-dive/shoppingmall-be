package io.groom.scubadive.shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ShoppingmallApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingmallApplication.class, args);
	}


//	@Bean
//	public CommandLineRunner initCategories(CategoryRepository categoryRepository) {
//		return args -> {
//			if (categoryRepository.count() == 0) {
//				categoryRepository.save(Category.createCategory("CHAIR", "의자"));
//				categoryRepository.save(Category.createCategory("TABLE", "책상"));
//			}
//		};
//	}
}
