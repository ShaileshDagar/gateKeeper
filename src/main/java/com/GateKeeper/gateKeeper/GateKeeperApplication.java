package com.GateKeeper.gateKeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GateKeeperApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateKeeperApplication.class, args);
	}

}