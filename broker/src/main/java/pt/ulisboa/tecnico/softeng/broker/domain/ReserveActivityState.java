package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.services.remote.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class ReserveActivityState extends ReserveActivityState_Base {
	public static final int MAX_REMOTE_ERRORS = 5;

	@Override
	public State getValue() {
		return State.RESERVE_ACTIVITY;
	}

	@Override
	public void process() {
		try {
			getAdventure().setActivityConfirmation(ActivityInterface.reserveActivity(getAdventure().getBegin(),
					getAdventure().getEnd(), getAdventure().getAge(), getAdventure().getID()));
		} catch (ActivityException ae) {
			getAdventure().setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				getAdventure().setState(State.UNDO);
			}
			return;
		}

		if (getAdventure().getBegin().equals(getAdventure().getEnd())) {
			getAdventure().setState(State.CONFIRMED);
		} else {
			getAdventure().setState(State.BOOK_ROOM);
		}
	}

}
