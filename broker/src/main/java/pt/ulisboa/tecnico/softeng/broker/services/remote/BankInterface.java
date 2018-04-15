package pt.ulisboa.tecnico.softeng.broker.services.remote;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.BankOperationData;

public class BankInterface {
	public static String processPayment(String IBAN, double amount) {
		// return Bank.processPayment(IBAN, amount);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static String cancelPayment(String reference) {
		// return Bank.cancelPayment(reference);
		// TODO: implement in the final version as a rest invocation
		return null;
	}

	public static BankOperationData getOperationData(String reference) {
		// return Bank.getOperationData(reference);
		// TODO: implement in the final version as a rest invocation
		return null;
	}
}
