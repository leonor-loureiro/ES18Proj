package pt.ulisboa.tecnico.softeng.broker.interfaces;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;

public class BankInterface {
	public static String processPayment(String IBAN, int amount) {
		return Bank.processPayment(IBAN, amount);
	}

	public static String cancelPayment(String paymentConfirmation) {
		return Bank.cancelPayment(paymentConfirmation);
	}

	public static BankOperationData getOperationData(String paymentConfirmation) {
		// TODO Auto-generated method stub
		return null;
	}
}
