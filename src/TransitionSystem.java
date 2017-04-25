import java.util.ArrayList;
import java.util.Arrays;
public class TransitionSystem {

	public ArrayList<Node> transistionsSystem;
	public ArrayList<Node> initialStates;

	public static void main(String[] args) {
		new TransitionSystem();
	}

	public TransitionSystem() {

		transistionsSystem = new ArrayList<Node>();
		initialStates = new ArrayList<Node>();

		transistionsSystem.add(new Node(1, false, new String[]{"c"}, new int[]{2}));
		transistionsSystem.add(new Node(2, false, new String[]{"v"}, new int[]{1}));
		transistionsSystem.add(new Node(3, false, new String[]{"c"}, new int[]{2, 4}));
		transistionsSystem.add(new Node(4, false, new String[]{"c"}, new int[]{8}));
		transistionsSystem.add(new Node(5, false, new String[]{"c"}, new int[]{3,7,8}));
		transistionsSystem.add(new Node(6, true, new String[]{"v"}, new int[]{7}));
		transistionsSystem.add(new Node(7, false, new String[]{"v"}, new int[]{6}));
		transistionsSystem.add(new Node(8, false, new String[]{"c"}, new int[]{4}));


		System.out.println("States in the transistion system");
		printPretty(transistionsSystem);

		// Saving the initial states
		for(int i = 0; i < transistionsSystem.size(); i++){
			if(transistionsSystem.get(i).initial){
				initialStates.add(transistionsSystem.get(i));
			}
		}
		System.out.println("\n----- Initial tests of CTL implementation -----");
		// Calculating EX with all the states, to control whether 
		// it's computed correct or not
		for(int i = 0; i < transistionsSystem.size(); i++){
			ArrayList<Node> temp = new ArrayList<>();
			temp.add(transistionsSystem.get(i));
			System.out.println("\nPrinting results of EX for "+ (i+1));
			printPretty(ctlEX(temp));
		}

		// Calculating EF with all the states, to control whether 
		// it's computed correct or not
		for(int i = 0; i < transistionsSystem.size(); i++){
			ArrayList<Node> temp = new ArrayList<>();
			temp.add(transistionsSystem.get(i));
			System.out.println("\nPrinting results of EF for "+ (i+1));
			printPretty(ctlEF(temp));
		}

		// Calculating AX with all the states, to control whether 
		// it's computed correct or not
		for(int i = 0; i < transistionsSystem.size(); i++){
			ArrayList<Node> temp = new ArrayList<>();
			temp.add(transistionsSystem.get(i));
			System.out.println("\nPrinting results of AX for "+ (i+1));
			printPretty(ctlAX(temp));
		}

		// Calculating AG with all the states, to control whether 
		// it's computed correct or not
		for(int i = 0; i < transistionsSystem.size(); i++){
			ArrayList<Node> temp = new ArrayList<>();
			temp.add(transistionsSystem.get(i));
			System.out.println("\nPrinting results of AG for "+ (i+1));
			printPretty(ctlAG(temp));
		}
		System.out.println("\n------------ End of initial tests  ------------\n");
		modelChecking();
	}

	// The wonderful pretty printer
	public void printPretty(ArrayList<Node> arrayList) {
		if(arrayList.isEmpty()){ 
			System.out.println("|--- No Values ---|"); 
		} else {
			for (Node s : arrayList) {
				if( s.initial) {
					System.out.println(s.number + " | " + s.initial + "  | " + Arrays.deepToString(s.strings) + " | " + Arrays.toString(s.integerArray));
				} else {
					System.out.println(s.number + " | " + s.initial + " | " + Arrays.deepToString(s.strings) + " | " + Arrays.toString(s.integerArray));
				}

			}
		}
	}

	// Returns a arrayList of all states in the TS with one of the input label
	public ArrayList<Node> ctlAP(String[] AP) {

		ArrayList<Node> temp = new ArrayList<Node>();

		for (Node s : transistionsSystem) {
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
	public ArrayList<Node> ctlEX(ArrayList<Node> arrayList) {

		ArrayList<Node> tempList = new ArrayList<>();

		for (int i = 0; i < transistionsSystem.size(); i++) {
			Node temp = transistionsSystem.get(i);
			for (Node s : arrayList) {
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
	public ArrayList<Node> ctlEF(ArrayList<Node> stateArrayList){

		ArrayList<Node> result = new ArrayList<>();

		for(Node s : stateArrayList){
			ArrayList<Node> temp = whoCanReachMe(s,new ArrayList<Node>());
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
	public ArrayList<Node> ctlAX(ArrayList<Node> stateList){

		ArrayList<Node> tempList = new ArrayList<>();

		for (int i = 0; i < transistionsSystem.size(); i++) {
			Node temp = transistionsSystem.get(i);
			boolean[] trueForALL = new boolean[temp.integerArray.length];
			Arrays.fill(trueForALL, Boolean.FALSE);
			for (Node s : stateList) {

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

	/*
	 * Input is a list of the start states, which we wish to test,
	 * if they can reach a state, with an other label.
	 * Output is a list of the states, which cannot reach 
	 * a state, with another label.
	 * It calls the method whoCanIReach, to get all the reachable 
	 * states of the current state, and afterwards it checks whether 
	 * all the states have the same label, if not that list is 
	 * deleted, as it doen't fullfill the requirements,
	 * otherwise the result is saved and duplicants are removed.
	 */
	public ArrayList<Node> ctlAG(ArrayList<Node> arrayList) {
		ArrayList<Node> result = new ArrayList<>();

		for(Node s : arrayList){
			ArrayList<Node> tempList = whoCanIReach(s, new ArrayList<Node>());
			for(Node tempState : tempList){
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

	/*
	 * Takes a state, from which we want to know, 
	 * which states it can reach, 
	 * and a list of already visited states as input.
	 * Returns a list of states, which it can reach.
	 * Is a recursive DFS-implementation.
	 */
	public ArrayList<Node> whoCanIReach(Node s, ArrayList<Node> visited){
		ArrayList<Node> reachable = new ArrayList<>();
		visited.add(s);
		reachable.add(s);
		for(int i = 0; i < s.integerArray.length; i++){
			Node temp = transistionsSystem.get(s.integerArray[i]-1);
			if(!visited.contains(temp)){
				reachable.addAll(whoCanIReach(temp,visited));
			}
		}
		return reachable;
	}

	/*
	 * Input is a arraylist of states.
	 * Output is a arraylist of all the states in the transition system,
	 * not in the input list.
	 */
	public ArrayList<Node> notPhi(ArrayList<Node> stateArrayList){

		ArrayList<Node> temp = new ArrayList<>();
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
	public ArrayList<Node> whoCanReachMe(Node state, ArrayList<Node> visited){
		ArrayList<Node> canReachMe = new ArrayList<>();
		visited.add(state);
		canReachMe.add(state);
		for(Node s : ctlEX(canReachMe)){
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

	/*
	 * Returns all states in the transition system
	 */
	public ArrayList<Node> tt(){

		ArrayList <Node> ts = new ArrayList<Node>();
		ts = transistionsSystem;

		return ts;
	}

	/*
	 * Input is two lists of states.
	 * Output is a list containing all the states,
	 * which were present in both input lists.
	 */
	public ArrayList<Node> and(ArrayList<Node> list1, ArrayList<Node> list2){
		ArrayList<Node> temp = new ArrayList<>();
		for(int i = 0; i < list1.size(); i++){
			if(list1.contains(list2.get(i))){
				temp.add(list2.get(i));
			}
		}
		return temp;
	}

	/*
	 * Input is a list of states
	 * Output is a boolean.
	 * It checks if the input list contains all the initial states
	 * (which are stored in the list initialStates), if it does,
	 * true is returned, else false is returned.
	 */
	public boolean checkInitialStates(ArrayList<Node> states){
		return states.containsAll(initialStates);
	}

	/*
	 *  Doing some model checking runs stepwise.
	 *  Please keep in mind that we have a different transition system.
	 */
	public void modelChecking(){
		System.out.println("\nBeginning model checking tests");
		// Doing the example from the assignment example
		ArrayList<Node> testing = new ArrayList<Node>();
		// Testing AP
		String[] stringTest = {"c"};
		testing = ctlAP(stringTest);
		System.out.println("\nPrinting ctlAP with " + Arrays.toString(stringTest) + " as input");
		printPretty(testing);
		// Testing AG
		testing = ctlAG(testing);
		System.out.println("\nPrinting ctlAG with previous results as input");
		printPretty(testing);
		//Testing EX
		testing = ctlEX(testing);
		System.out.println("\nPrinting ctlEX with previous results as input");
		printPretty(testing);
		//Testing initial states
		System.out.println("\nPrinting checkInitialStates with previous results as input");
		System.out.println(checkInitialStates(testing));
		System.out.println("\n--------- End of this test ---------");
		System.out.println("\n\n------------- New test -------------");
		// Testing AP
		stringTest[0] = "v";
		testing = ctlAP(stringTest);
		System.out.println("\nPrinting ctlAP with " + Arrays.toString(stringTest) + " as input");
		printPretty(testing);
		// Testing EF
		testing = ctlEF(testing);
		System.out.println("\nPrinting ctlEF with previous results as input");
		printPretty(testing);
		// Testing AX
		testing = ctlAX(testing);
		System.out.println("\nPrinting ctlAX with previous results as input");
		printPretty(testing);
		// Testing AG
		testing = ctlAG(testing);
		System.out.println("\nPrinting ctlAG with previous results as input");
		printPretty(testing);
		//Testing initial states
		System.out.println("\nPrinting checkInitialStates with previous results as input");
		System.out.println(checkInitialStates(testing));
		System.out.println("\n--------- End of this test ---------");
	}
}