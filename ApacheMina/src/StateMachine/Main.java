package StateMachine;

import org.apache.mina.statemachine.StateMachine;
import org.apache.mina.statemachine.StateMachineFactory;
import org.apache.mina.statemachine.StateMachineProxyBuilder;
import org.apache.mina.statemachine.annotation.Transition;

public class Main {
	
	public static void main(String[] args) {
		
		TapeDeckHandler handler = new TapeDeckHandler();
		StateMachine sm = StateMachineFactory.getInstance(Transition.class).create(TapeDeckHandler.EMPTY, handler);
		TapeDeck deck = new StateMachineProxyBuilder().create(TapeDeck.class, sm);
		
		deck.load("The Knife - Silent Shout");
		deck.play();
		deck.pause();
		deck.play();
		deck.stop();
		deck.eject();
		deck.play();

	}
	
	

}
