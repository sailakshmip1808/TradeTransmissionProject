/**
 * Java bean class for Trade
 */
package com.ttp.proj.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sailakshmi Pakkala
 *
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Using Lombok. Hence getters and setters are auto generated by Lombok. 
public class Trade {

	private String tradeId;
	private int version;
	private String counterPtyId;
	private String bookingId;
	private Date maturityDate;
	private Date createdDate;
	private char expired;

	/**
	 * Overriding toString() using @Override
	 */
	@Override
	public String toString() {
		SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
		String tradeString = "   " + tradeId + "       " + version + "         " + counterPtyId + "         " + bookingId + "      "
				+ sdFormat.format(maturityDate) + "    " + sdFormat.format(createdDate) + "      " + expired;
		return tradeString;
	}

}
