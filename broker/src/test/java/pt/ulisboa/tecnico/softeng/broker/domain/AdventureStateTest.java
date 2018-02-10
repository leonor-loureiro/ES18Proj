package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class AdventureStateTest {

    AdventureState advState;

    @Before
    public void setUp() {
         advState = new AdventureState() {
            @Override
            public Adventure.State getState() {
                return null;
            }

            @Override
            public void process(Adventure adventure) {

            }
        };
    }

    @Test
    public void inc() {
        assertTrue(advState.getNumOfRemoteErrors() == 0);

        advState.incNumOfRemoteErrors();
        assertTrue(advState.getNumOfRemoteErrors() == 1);

        advState.incNumOfRemoteErrors();
        assertTrue(advState.getNumOfRemoteErrors() == 2);

        advState.resetNumOfRemoteErrors();
        assertTrue(advState.getNumOfRemoteErrors() == 0);
    }

}
