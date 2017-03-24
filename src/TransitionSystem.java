import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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
        System.out.println(transistionsSystem);

        transistionsSystem.add(new State(1, true, new String[]{"v"}, new int[]{2}));
        transistionsSystem.add(new State(2, false, new String[]{"v"}, new int[]{1, 4}));
        transistionsSystem.add(new State(3, false, new String[]{"c"}, new int[]{3}));
        transistionsSystem.add(new State(4, false, new String[]{"c"}, new int[]{4}));
        printPretty(transistionsSystem);

        ArrayList foo = ctlAP(new String[]{"c"});
        System.out.println(Arrays.deepToString(foo.toArray(new State[foo.size()])));
        printPretty(foo);


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


    public ArrayList ctlAG(ArrayList<State> arrayList) {


        return null;

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


    public ArrayList ctlAX(ArrayList<State> stateList){

        ArrayList<State> tempList = new ArrayList<>();
        for (int i = 0; i < transistionsSystem.size(); i++) {

            State temp = transistionsSystem.get(i);
            for (State s : stateList) {
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

}


