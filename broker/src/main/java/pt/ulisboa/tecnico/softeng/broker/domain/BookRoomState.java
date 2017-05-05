package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface.RoomType;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class BookRoomState extends BookRoomState_Base {
	public static final int MAX_REMOTE_ERRORS = 10;

	@Override
	public State getValue() {
		return State.BOOK_ROOM;
	}

	@Override
	public void process() {
		try {
			getAdventure().setRoomConfirmation(HotelInterface.reserveRoom(RoomType.SINGLE, getAdventure().getBegin(),
					getAdventure().getEnd(), getAdventure().getID()));
		} catch (HotelException he) {
			getAdventure().setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				getAdventure().setState(State.UNDO);
			}
			return;
		}

		getAdventure().setState(State.CONFIRMED);
	}

}
