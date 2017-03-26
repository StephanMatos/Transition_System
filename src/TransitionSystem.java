import java.util.ArrayList;
import java.util.Arrays;
public class TransitionSystem {

	public ArrayList<State> transistionsSystem;
	public ArrayList<State> initialStates;

	public static void main(String[] args) {
		new TransitionSystem();
	}

	public TransitionSystem() {

		transistionsSystem = new ArrayList<State>();
		initialStates = new ArrayList<State>();

		transistionsSystem.add(new State(1, false, new String[]{"c"}, new int[]{2}));
		transistionsSystem.add(new State(2, false, new String[]{"v"}, new int[]{1}));
		transistionsSystem.add(new State(3, false, new String[]{"c"}, new int[]{2, 4}));
		transistionsSystem.add(new State(4, false, new String[]{"c"}, new int[]{8}));
		transistionsSystem.add(new State(5, false, new String[]{"c"}, new int[]{3,7,8}));
		transistionsSystem.add(new State(6, true, new String[]{"v"}, new int[]{7}));
		transistionsSystem.add(new State(7, false, new String[]{"v"}, new int[]{6}));
		transistionsSystem.add(new State(8, false, new String[]{"c"}, new int[]{4}));
		

		System.out.println("States in the transistion system");
		printPretty(transistionsSystem);

		// Saving the initial states
		for(int i = 0; i < transistionsSystem.size(); i++){
			if(transistionsSystem.get(i).initial){
				initialStates.add(transistionsSystem.get(i));
			}
		}
		
		// Calculating EX with all the states, to control whether 
		// it's computed correct or not
		for(int i = 0; i < transistionsSystem.size(); i++){
			ArrayList<State> temp = new ArrayList<>();
			temp.add(transistionsSystem.get(i));
			System.out.println("\nPrinting results of EX for "+ (i+1));
			printPretty(ctlEX(temp));
		}

		// Calculating EF with all the states, to control whether 
		// it's computed correct or not
		for(int i = 0; i < transistionsSystem.size(); i++){
			ArrayList<State> temp = new ArrayList<>();
			temp.add(transistionsSystem.get(i));
			System.out.println("\nPrinting results of EF for "+ (i+1));
			printPretty(ctlEF(temp));
		}
		
		// Calculating AX with all the states, to control whether 
		// it's computed correct or not
		for(int i = 0; i < transistionsSystem.size(); i++){
			ArrayList<State> temp = new ArrayList<>();
			temp.add(transistionsSystem.get(i));
			System.out.println("\nPrinting results of AX for "+ (i+1));
			printPretty(ctlAX(temp));
		}
		
		// Calculating AG with all the states, to control whether 
		// it's computed correct or not
		for(int i = 0; i < transistionsSystem.size(); i++){
			ArrayList<State> temp = new ArrayList<>();
			temp.add(transistionsSystem.get(i));
			System.out.println("\nPrinting results of AG for "+ (i+1));
			printPretty(ctlAG(temp));
		}
	}

	// The wonderful pretty printer
	public void printPretty(ArrayList<State> arrayList) {
		if(arrayList.isEmpty()){ 
			System.out.println("|--- No Values ---|"); 
		} else {
			for (State s : arrayList) {
				if( s.initial) {
					System.out.println(s.number + " | " + s.initial + "  | " + Arrays.deepToString(s.strings) + " | " + Arrays.toString(s.integerArray));
				} else {
					System.out.println(s.number + " | " + s.initial + " | " + Arrays.deepToString(s.strings) + " | " + Arrays.toString(s.integerArray));
				}
				
			}
		}
	}
	
	// Returns a arrayList of all states in the TS with one of the input label
	public ArrayList<State> ctlAP(String[] AP) {

		ArrayList<State> temp = new ArrayList<State>();

		for (State s : transistionsSystem) {
			for (int i = 0; i < s.strings.length; i++) {
				for (int j = 0; j < AP.length; j++) {

					if (s.strings[i].equals(AP[j])) {
						temp.add(s);
						break;
					}
				}
			}
		}

		return temp;
	}

	/* Input is the states we wish to reach in one move.
	 * The method looks at all the states in the transition system,
	 * and if a state contains one of the input states as a neighbor,
	 * it is added to the output list.
	 */
	public ArrayList<State> ctlEX(ArrayList<State> arrayList) {

		ArrayList<State> tempList = new ArrayList<>();

		for (int i = 0; i < transistionsSystem.size(); i++) {
			State temp = transistionsSystem.get(i);
			for (State s : arrayList) {
				for (int j = 0; j < temp.integerArray.length; j++) {
					if (temp.integerArray[j] == s.number) {
						tempList.add(temp);
						break;
					}
				}
			}
		}
		return tempList;
	}

	/*
	 * Input is an arraylist of the states we wish to reach, 
	 * output is the states, which can reach these states.
	 * For each state in the input the whoCanRachMe method is called.
	 * whoCanReachMe is an DFS method. After each call, the list
	 * is cleared of duplicates.
	 */
	public ArrayList<State> ctlEF(ArrayList<State> stateArrayList){

		ArrayList<State> result = new ArrayList<>();

		for(State s : stateArrayList){
			ArrayList<State> temp = whoCanReachMe(s,new ArrayList<State>());
			result.removeAll(temp);
			result.addAll(temp);
		}
		return result;
	}

	/* Input is the states we wish to reach in one move.
	 * The method looks at all the states in the transition system,
	 * and if a state only contains input states as neighbors,
	 * it is added to the output list.
	 */
	public ArrayList<State> ctlAX(ArrayList<State> stateList){

		ArrayList<State> tempList = new ArrayList<>();

		for (int i = 0; i < transistionsSystem.size(); i++) {
			State temp = transistionsSystem.get(i);
			boolean[] trueForALL = new boolean[temp.integerArray.length];
			Arrays.fill(trueForALL, Boolean.FALSE);
			for (State s : stateList) {

				for (int j = 0; j < temp.integerArray.length; j++) {

					if (temp.integerArray[j] == s.number) {
						trueForALL[j] = true;
						break;
					}
				}
			}
			if(!containsFalse(trueForALL)){
				tempList.add(temp);
			}
		}
		return tempList;
	}

	public ArrayList<State> ctlAG(ArrayList<State> arrayList) {
		ArrayList<State> result = new ArrayList<>();

		for(State s : arrayList){
			ArrayList<State> tempList = whoCanIReach(s, new ArrayList<State>());
			for(State tempState : tempList){
				if(!s.strings[0].equals(tempState.strings[0])){
					tempList.clear();
					break;
				}
			}
			result.removeAll(tempList);
			result.addAll(tempList);
		}

		return result;

	}

	public ArrayList<State> whoCanIReach(State s, ArrayList<State> visited){
		ArrayList<State> reachable = new ArrayList<>();
		visited.add(s);
		reachable.add(s);
		for(int i = 0; i < s.integerArray.length; i++){
			State temp = transistionsSystem.get(s.integerArray[i]-1);
			if(!visited.contains(temp)){
				reachable.addAll(whoCanIReach(temp,visited));
			}
		}
		return reachable;
	}

	public ArrayList<State> notPhi(ArrayList<State> stateArrayList){

		ArrayList<State> temp = new ArrayList<>();
		temp.addAll(transistionsSystem);
		temp.removeAll(stateArrayList);

		return temp;
	}

	/*
	 * Takes a state, which we wish to reach, 
	 * and a list of already visited states as input.
	 * Returns a list of states, which can reach it.
	 * Is a recursive DFS-implementation.
	 */
	public ArrayList<State> whoCanReachMe(State state, ArrayList<State> visited){
		ArrayList<State> canReachMe = new ArrayList<>();
		visited.add(state);
		canReachMe.add(state);
		for(State s : ctlEX(canReachMe)){
			if(!visited.contains(s)){
				canReachMe.addAll(whoCanReachMe(s,visited));
			}
		}
		return canReachMe;
	}

	public boolean containsFalse(boolean[] booleanArray){

		for(int i = 0; i < booleanArray.length; i++){
			if(!booleanArray[i]){
				return true;
			}

		}
		return false;
	}

	public ArrayList<State> tt(){

		ArrayList <State> ts = new ArrayList<State>();
		ts = transistionsSystem;

		return ts;
	}

	public ArrayList<State> and(ArrayList<State> list1, ArrayList<State> list2){
		ArrayList<State> temp = new ArrayList<>();
		for(int i = 0; i < list1.size(); i++){
			if(list1.contains(list2.get(i))){
				temp.add(list2.get(i));
			}
		}
		return temp;
	}

	public boolean checkInitialStates(ArrayList<State> states){
		return states.containsAll(initialStates);
	}
}