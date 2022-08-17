/**
 * Utility class to perform all the criteria checks on the data for trade transmission
 */
package com.ttp.proj.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.ttp.proj.bean.Trade;
import com.ttp.proj.exception.TradeException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sailakshmi Pakkala
 *
 */
@Slf4j
public class TradeUtility {
	/**
	 * Creating or adding Trade based upon the criteria
	 * 
	 * @param trade
	 * @throws TradeException
	 */
	public void createTrade(Trade trade, HashMap<String, Trade> tradeTransmissions) throws TradeException {
		if (tradeTransmissions.containsKey(trade.getTradeId())) {
			// Checking the first criteria to check the trade Version
			checkTradeVersion(trade, tradeTransmissions.get(trade.getTradeId()).getVersion());

			if (checkMaturityDate(trade.getMaturityDate(),
					tradeTransmissions.get(trade.getTradeId()).getMaturityDate())) {
				tradeTransmissions.replace(trade.getTradeId(), trade);
				log.info("Trade " + trade.getTradeId() + " is added to the Store");
			} else {
				log.warn("Unable to add " + trade.getTradeId()
						+ " in the store as maturity date is lower than current date");
			}
		} else {
			if (checkMaturityDate(trade.getMaturityDate(), trade.getCreatedDate())) {
				tradeTransmissions.put(trade.getTradeId(), trade);
				log.info("Trade " + trade.getTradeId() + " is added to the Store");
			} else {
				log.warn("Unable to add " + trade.getTradeId()
						+ " in the store as maturity date is lower than current date");
			}
		}
	}

	/**
	 * Displays all the trades
	 */
	public void displayTrade(HashMap<String, Trade> tradeTransmissions) {
		String tradeString = "Trade Id  Version Counter-PartyId Book-Id Maturity Date Created Date Expired";
		log.info(tradeString);
		for (String tradeId : tradeTransmissions.keySet()) {
			log.info(tradeTransmissions.get(tradeId).toString());
		}
	}

	/**
	 * Checking if the lower version is being received by the store, then it will
	 * reject the trade and will throw an exception. If the version is same, then it
	 * will override the existing record.
	 * 
	 * @param trade
	 * @param version
	 * @throws TradeException
	 */
	public void checkTradeVersion(Trade trade, int version) throws TradeException {
		if (trade.getVersion() < version) {
			log.error("Given version " + trade.getVersion() + " is less than " + version);
			throw new TradeException("Given version " + trade.getVersion() + " is less than " + version, null, false,
					false);
		}
	}

	/**
	 * Checking if maturityDate is less than currentDate
	 * 
	 * @param maturityDate
	 * @param currentDate
	 * @return
	 */
	public boolean checkMaturityDate(Date maturityDate, Date currentDate) {
		if (currentDate.after(maturityDate)) {
			log.error("Given maturityDate " + maturityDate + " is lower than " + currentDate);
			return false;
		}

		return true;
	}

	/**
	 * Update the expiry flag if the trade crosses the maturity date in a store
	 */
	public void checkExpiryandUpdate(HashMap<String, Trade> tradeTransmissions) {
		Date currentDate = new Date();
		List<String> expiredTradeList = new ArrayList<String>();
		for (String tradeKey : tradeTransmissions.keySet()) {
			if (currentDate.after(tradeTransmissions.get(tradeKey).getMaturityDate())) {
				Trade trade = tradeTransmissions.get(tradeKey);
				trade.setExpired('Y');
				tradeTransmissions.replace(tradeKey, trade);
				expiredTradeList.add(tradeKey);
			}
		}
		String expiredTradecsList = expiredTradeList.stream().collect(Collectors.joining(","));
		log.info("Expired Trades List is "+expiredTradecsList);
	}

}
