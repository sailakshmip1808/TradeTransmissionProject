/**
 * Class used to perform the tradeTransmissions to the Hashmap based on the given criteria
 */
package com.ttp.proj.transmission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.ttp.proj.bean.Trade;
import com.ttp.proj.exception.TradeException;
import com.ttp.proj.utility.TradeUtility;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sailakshmi Pakkala
 *
 */
@Slf4j
public class TradeTransmission {

	/**
	 * Method to perform the actual trade transmissions into the store
	 * @throws TradeException
	 * @throws ParseException
	 */
	public void performTradeTransmission() throws TradeException, ParseException {
		TradeUtility tUtility = new TradeUtility();
		Date currentDate = Calendar.getInstance().getTime();
		SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
		HashMap<String, Trade> tradeTransmissions = new HashMap<String, Trade>();

        //	Creating Trade T1
		Date maturityDate = sdFormat.parse("19/09/2022");
		Trade t1 = new Trade("T1", 1, "CP-1", "B1", maturityDate, currentDate, 'N');
		tUtility.createTrade(t1, tradeTransmissions);

		// Creating Trade T2
		maturityDate = sdFormat.parse("08/05/2023");
		Trade t2 = new Trade("T2", 2, "CP-2", "B1", maturityDate, currentDate, 'N');
		tUtility.createTrade(t2, tradeTransmissions);

		// Same version for T2 received. Hence the existing record will be overrided 
		maturityDate = sdFormat.parse("08/05/2024");
		Trade t22 = new Trade("T2", 2, "CP-2", "B1", maturityDate, currentDate, 'N');
		tradeTransmissions.replace("T2", t22);

		// Creating Trade T3
		maturityDate = sdFormat.parse("09/02/2023");
		Trade t3 = new Trade("T3", 3, "CP-3", "B2", maturityDate, currentDate, 'N');
		tUtility.createTrade(t3, tradeTransmissions);

		// Creating Trade T4
		maturityDate = sdFormat.parse("26/09/2022");
		Trade t4 = new Trade("T4", 5, "CP-3", "B2", maturityDate, currentDate, 'N');
		tUtility.createTrade(t4, tradeTransmissions);

		// Display all the trades
		log.info("\n\n");
		log.info("Displaying all the trades in the store");
		tUtility.displayTrade(tradeTransmissions);
        System.out.println();
        
		// Updating Trade T4
		maturityDate = sdFormat.parse("20/05/2014");
		Trade t41 = new Trade("T4", 5, "CP-3", "B2", maturityDate, currentDate, 'N');
        tradeTransmissions.replace("T4", t41);
        
        // Updating Trade T5
     	maturityDate = sdFormat.parse("20/05/2015");
     	Trade t5 = new Trade("T5", 4, "CP-1", "B5", maturityDate, currentDate, 'N');
     	tUtility.createTrade(t5, tradeTransmissions);
		
		// Checking for any Trade expired and setting the expiryFlag
		tUtility.checkExpiryandUpdate(tradeTransmissions);
		
		// Updating Trade T2
		maturityDate = sdFormat.parse("20/05/2021");
		Date createdDate = sdFormat.parse("14/03/2015"); 
		Trade t6 = new Trade("T2", 2, "CP-1", "B1", maturityDate, createdDate, 'N');
		tradeTransmissions.replace("T2", t6);
		
		System.out.println();
		log.info("Displaying all the trades in the store after modification");
		tUtility.displayTrade(tradeTransmissions);
		tradeTransmissions.clear();
	}

}
