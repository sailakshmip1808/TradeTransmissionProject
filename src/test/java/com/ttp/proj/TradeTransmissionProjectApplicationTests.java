package com.ttp.proj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.ttp.proj.bean.Trade;
import com.ttp.proj.exception.TradeException;
import com.ttp.proj.utility.TradeUtility;

/**
 * @author Sailakshmi Pakkala
 *
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TradeTransmissionProjectApplicationTests {

	SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
	Date currentDate = Calendar.getInstance().getTime();
	TradeUtility tradeUtility = new TradeUtility();

	HashMap<String, Trade> tradeTransmissionsTestMap = new HashMap<String, Trade>();

	/**
	 * Test to check if a trade is being inserted or not
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 */
	@Test
	@Order(0)
	void testCreateTrade() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("19/09/2022");
		Trade t1 = new Trade("T1", 1, "CP-1", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t1, tradeTransmissionsTestMap);

		// Checking the list if the trade is inserted or not
		assertEquals(1, tradeTransmissionsTestMap.size());
	}

	/**
	 * Test to check if the version is low than the existing one, then the trade
	 * will be rejected and will throw TradeException
	 * 
	 * @throws TradeException
	 * @throws ParseException 
	 * 
	 * T1 3 CP-3 B1 20/09/2023 currentDate N 
	 * T1 1 CP-2 B1 20/09/2023 currentDate N
	 */
	@Test
	@Order(1)
	void testLowVersion() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("20/09/2023");

		Trade t1 = new Trade("T1", 3, "CP-3", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t1, tradeTransmissionsTestMap);

		Trade t2 = new Trade("T1", 1, "CP-2", "B1", maturityDate, currentDate, 'N');

		assertThrows(TradeException.class, () -> tradeUtility.createTrade(t2, tradeTransmissionsTestMap),
				"1 is less than 3");

	}

	/**
	 * Test to check if the version is same as the existing one, then the trade
	 * will be overrided with the new one
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T1 1 CP-2 B1 20/09/2023 currentDate N 
	 * T1 1 CP-1 B2 20/09/2023 currentDate N
	 */
	@Test
	@Order(2)
	void testSameVersion() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("20/09/2023");
		// Same Version as before and hence changing bookingId to B2
		Trade t1 = new Trade("T1", 1, "CP-1", "B2", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t1, tradeTransmissionsTestMap);
		assertEquals("B2", tradeTransmissionsTestMap.get("T1").getBookingId());
	}

	/**
	 * Test to check if the version is same than the existing one, then the trade
	 * will be overrided with the new one
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T2 2 CP-2 B1 20/09/2023 currentDate N 
	 * T2 3 CP-3 B1 20/09/2023 currentDate N
	 */
	@Test
	@Order(3)
	void testHighVersion() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("20/09/2023");
		Trade t2 = new Trade("T2", 2, "CP-2", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t2, tradeTransmissionsTestMap);

		// Changing Version as 3 and changing Counter-Party ID to CP-3
		Trade t3 = new Trade("T2", 3, "CP-3", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t3, tradeTransmissionsTestMap);
		assertEquals("CP-3", tradeTransmissionsTestMap.get("T2").getCounterPtyId());
	}

	/**
	 * Test to check if maturity Date is lower than currentdate then the trade will not be added
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T3 3 CP-3 B3 20/05/2021 currentDate N
	 */
	@Test
	@Order(4)
	void testLowerMaturityDate() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("20/05/2021");
		Trade t1 = new Trade("T3", 3, "CP-3", "B3", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t1, tradeTransmissionsTestMap);
		
		// As maturityDate is lower than currentDate, trade will no inserted and hence doing null check
		assertNull(tradeTransmissionsTestMap.get("T3"));
	}

	/**
	 * Test to check if maturity Date is greater than currentDate then the trade is added
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T5 5 CP-5 B3 20/09/2023 currentDate N
	 */
	@Test
	@Order(5)
	void testGreaterMaturityDate() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("20/09/2023");

		Trade t1 = new Trade("T5", 5, "CP-5", "B3", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t1, tradeTransmissionsTestMap);
		
		// As maturityDate is greater than currentDate, trade will be inserted
		assertEquals(t1, tradeTransmissionsTestMap.get("T5"));
	}

	/**
	 * Test to check if maturity Date is same as currentDate then the trade is inserted
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T4 4 CP-4 B2 currentDate currentDate N
	 */
	@Test
	@Order(6)
	void testSameMaturityDate() throws TradeException, ParseException {
		Trade t1 = new Trade("T4", 4, "CP-4", "B2", currentDate, currentDate, 'N');
		tradeUtility.createTrade(t1, tradeTransmissionsTestMap);
		
		// As maturityDate is same as currentDate, trade will be inserted and hence doing not null check
		assertNotNull(tradeTransmissionsTestMap.get("T4"));
		assertEquals(t1, tradeTransmissionsTestMap.get("T4"));
	}

	/**
	 * Test to check if maturityDate is expired it will update the Expired Flag
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T4 4 CP-4 B2 currentDate currentDate N
	 */
	@Test
	@Order(7)
	void testExpiryandUpdate() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("20/05/2020");
		Trade t16 = new Trade("T4", 4, "CP-4", "B2", maturityDate, currentDate, 'N');
		tradeTransmissionsTestMap.put("T4", t16);
		tradeUtility.checkExpiryandUpdate(tradeTransmissionsTestMap);
		assertEquals('Y', tradeTransmissionsTestMap.get("T4").getExpired());
	}

	/**
	 * Test to check if the expired Flag is getting updated
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T8 3 CP-3 B2 20/05/2023 currentDate N
	 * T8 3 CP-3 B1 20/05/2021 currentDate Y
	 */
	@Test
	@Order(8)
	void testExpiredFlag() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("20/09/2023");
		Trade t6 = new Trade("T6", 1, "CP-1", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t6, tradeTransmissionsTestMap);
		maturityDate = sdFormat.parse("20/09/2024");
		Trade t7 = new Trade("T7", 2, "CP-2", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t7, tradeTransmissionsTestMap);

		maturityDate = sdFormat.parse("20/05/2023");
		Trade t8 = new Trade("T8", 3, "CP-3", "B2", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t8, tradeTransmissionsTestMap);

		maturityDate = sdFormat.parse("20/05/2021");
		Trade t9 = new Trade("T8", 3, "CP-3", "B1", maturityDate, currentDate, 'N');
		tradeTransmissionsTestMap.replace("T8", t9);
		
		tradeUtility.checkExpiryandUpdate(tradeTransmissionsTestMap);
		assertEquals('Y', tradeTransmissionsTestMap.get("T8").getExpired());
	}

	/**
	 * Test to check if version is high but maturity date is low the trade will be rejected
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T7 2 CP-2 B1 20/09/2024 currentDate N
	 * T7 5 CP-3 B1 20/05/2021 currentDate N
	 */
	@Test
	@Order(9)
	void testHighVersionLowMaturityDate() throws TradeException, ParseException {

		Date maturityDate = sdFormat.parse("20/09/2024");
		Trade t1 = new Trade("T7", 2, "CP-2", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t1, tradeTransmissionsTestMap);
		
		maturityDate = sdFormat.parse("20/05/2021");
		Trade t2 = new Trade("T7", 5, "CP-3", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t2, tradeTransmissionsTestMap);
		assertEquals(2, tradeTransmissionsTestMap.get("T7").getVersion());
	}

	/**
	 * Test to check if version is same but maturity date is low, then the trade will not be updated
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T6 1 CP-1 B1 20/09/2023 currentDate N
	 * T6 1 CP-2 B1 20/05/2021 currentDate N
	 */
	@Test
	@Order(10)
	void testSameVersionLowMaturityDate() throws TradeException, ParseException {

		Date maturityDate1 = sdFormat.parse("20/09/2023"); 
		Trade t9 = new Trade("T6", 1, "CP-1", "B1", maturityDate1, currentDate, 'N'); 
		tradeUtility.createTrade(t9, tradeTransmissionsTestMap);
		 
		Date maturityDate = sdFormat.parse("20/05/2021");
		Trade t10 = new Trade("T6", 1, "CP-2", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t10, tradeTransmissionsTestMap);
		assertEquals(maturityDate1, tradeTransmissionsTestMap.get("T6").getMaturityDate());
	}

	/**
	 * Test to check if both version and also maturity date is low, then the trade will not be inserted
	 * 
	 * @throws TradeException
	 * @throws ParseException
	 * 
	 * T9 5 CP-3 B1 20/09/2023 currentDate N
	 * T9 1 CP-4 B1 20/05/2021 currentDate N
	 */
	@Test
	@Order(11)
	void testVersionAndMaturityLow() throws TradeException, ParseException {
		Date maturityDate = sdFormat.parse("20/09/2023");

		Trade t11 = new Trade("T9", 5, "CP-3", "B1", maturityDate, currentDate, 'N');
		tradeUtility.createTrade(t11, tradeTransmissionsTestMap);

		maturityDate = sdFormat.parse("20/05/2021");
		Trade t12 = new Trade("T9", 1, "CP-4", "B1", maturityDate, currentDate, 'N');
		assertThrows(Exception.class, () -> tradeUtility.createTrade(t12, tradeTransmissionsTestMap), "1 is less than 5");
	}

}
