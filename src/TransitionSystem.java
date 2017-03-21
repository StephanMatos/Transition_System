import java.util.ArrayList;

/**
 * Created by Matos on 21-03-2017.
 */
public class TransitionSystem {

	public static void main(String[]args){

        new TransitionSystem();


	}

    public TransitionSystem(){

        ArrayList<State> TransistionsSystem = new ArrayList<>();

        TransistionsSystem.add(new State(1,true, new String[] {"v"}, new int[] {2}));
        TransistionsSystem.add(new State(2,false, new String[] {"v"}, new int[] {1,4}));
        TransistionsSystem.add(new State(3,false, new String[] {"c"}, new int[] {3}));
        TransistionsSystem.add(new State(4,false, new String[] {"c"}, new int[] {4}));


    }
    
    public void printPretty(){
    	
    }



}
