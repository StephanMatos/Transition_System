import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Matos on 21-03-2017.
 */
public class TransitionSystem {

    public ArrayList<State> transistionsSystem;

    public static void main(String[] args) {

        new TransitionSystem();


    }

    public TransitionSystem() {

        transistionsSystem = new ArrayList<State>();

        transistionsSystem.add(new State(1, true, new String[]{"v"}, new int[]{2}));
        transistionsSystem.add(new State(2, false, new String[]{"v"}, new int[]{1, 4}));
        transistionsSystem.add(new State(3, false, new String[]{"c"}, new int[]{4}));
        transistionsSystem.add(new State(4, false, new String[]{"c"}, new int[]{3}));
        printPretty(transistionsSystem);

        ArrayList foo = ctlAP(new String[]{"c"});
        System.out.println(Arrays.deepToString(foo.toArray(new State[foo.size()])));
        printPretty(foo);

        //System.out.println("\n Dette er EX");
        //printPretty(ctlEX(foo));

        System.out.println("\n Dette er AX");
        printPretty(ctlAX(foo));

        for(int i = 0; i < transistionsSystem.size(); i++){
            ArrayList<State> temp = new ArrayList<>();
            temp.add(transistionsSystem.get(i));
            System.out.println("Vi printer EF for "+ i+1);
            printPretty(ctlEF(temp));
        }
        System.out.println();
        for(int i = 0; i < transistionsSystem.size(); i++){
            ArrayList<State> temp = new ArrayList<>();
            temp.add(transistionsSystem.get(i));
            System.out.println("Vi printer AG for "+ (i+1));
            printPretty(ctlAG(temp));
        }



    }

    public void printPretty(ArrayList<State> arrayList) {

        for (State s : arrayList) {
            System.out.println(s.number + " | " + s.initial + " | " + Arrays.deepToString(s.strings) + " | " + Arrays.toString(s.integerArray));
        }
    }

    public ArrayList ctlAP(String[] AP) {

        ArrayList<State> temp = new ArrayList();

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

    public ArrayList ctlEX(ArrayList<State> arrayList) {

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

    public ArrayList ctlEF(ArrayList<State> stateArrayList){

        ArrayList<State> result = new ArrayList<>();

        for(State s : stateArrayList){
            ArrayList<State> temp = DFS(s,new ArrayList<State>());
            result.removeAll(temp);
            result.addAll(temp);
        }

    return result;
    }

    public ArrayList ctlAX(ArrayList<State> stateList){

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

    public ArrayList ctlAG(ArrayList<State> arrayList) {
        ArrayList<State> result = new ArrayList<>();
        ArrayList<State> temp = ctlEF(arrayList);
        for (int i = 0; i < transistionsSystem.size(); i++) {
            if(temp.contains(transistionsSystem.get(i))) {

            }
            else {
                result.add(transistionsSystem.get(i));
            }
        }

        return result;

    }

    public ArrayList notPhi(ArrayList<State> stateArrayList){

        ArrayList<State> temp = new ArrayList<>();
        temp.addAll(transistionsSystem);
        temp.removeAll(stateArrayList);

        return temp;
    }

    public ArrayList<State> DFS(State state, ArrayList<State> visited){
        ArrayList<State> reachable = new ArrayList<>();
        visited.add(state);
        reachable.add(state);

        for(int i = 0; i < state.integerArray.length; i++){
            int stateNo = state.integerArray[i];
            State temp = transistionsSystem.get(stateNo-1);
            if(!visited.contains(temp)){
                reachable.addAll(DFS(temp,visited));
            }

        }
        return reachable;
    }

    public boolean containsFalse(boolean[] booleanArray){

        for(int i = 0; i < booleanArray.length; i++){
            if(!booleanArray[i]){
                return true;
            }

        }
        return false;
    }




}


