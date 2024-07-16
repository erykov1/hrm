package erykmarnik.hrm;

import org.springframework.boot.SpringApplication;

public class TestHrmApplication {

	public static void main(String[] args) {
		SpringApplication.from(HrmApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
