package com.ttp.proj;

import java.text.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ttp.proj.exception.TradeException;
import com.ttp.proj.transmission.TradeTransmission;

/**
 * @author Sailakshmi Pakkala
 *
 */
@SpringBootApplication
public class TradeTransmissionProjectApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TradeTransmissionProjectApplication.class, args);
	}

	/**
	 * Method to invoke the business logic for TradeTransmission
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 */
	@Bean
	public void performTradeTransmission() throws TradeException, ParseException {
		new TradeTransmission().performTradeTransmission();
	}

}
