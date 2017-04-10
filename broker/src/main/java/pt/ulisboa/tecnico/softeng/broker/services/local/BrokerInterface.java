package pt.ulisboa.tecnico.softeng.broker.services.local;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.AdventureData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;

public class BrokerInterface {

	@Atomic(mode = TxMode.READ)
	public static List<BrokerData> getBrokers() {
		List<BrokerData> brokers = new ArrayList<>();
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			brokers.add(new BrokerData(broker));
		}
		return brokers;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBroker(BrokerData brokerData) {
		new Broker(brokerData.getCode(), brokerData.getName());
	}

	@Atomic(mode = TxMode.READ)
	public static BrokerData getBrokerByCode(String code) {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			if (broker.getCode().equals(code)) {
				return new BrokerData(broker);
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createAdventure(String brokerCode, AdventureData adventureData) {
		Broker broker = null;
		for (Broker brk : FenixFramework.getDomainRoot().getBrokerSet()) {
			if (brk.getCode().equals(brokerCode)) {
				broker = brk;
				break;
			}
		}
		new Adventure(broker,
				// hj
				adventureData.getBegin(),
				// khkl
				adventureData.getEnd(),
				// kjhl
				adventureData.getAge() != null ? adventureData.getAge() : 0,
				// hgjgj
				adventureData.getIban(),
				// ghjgkjh
				adventureData.getAmount() != null ? adventureData.getAmount() : 0);
	}

}
