import java.util.Date;
import java.util.HashMap;

public class TimeoutUpdateList<T> extends HashMap<T, Date>{
    public void push(T key) {
        if (put(key, new Date()) == null || update()) {
            print();
        }
    }
    public boolean update() {
        boolean isUpdated = false;
        for (T key : keySet()) {
            if ((new Date()).getTime() - get(key).getTime() > TimeConstants.LIFE_TIME) {
                remove(key);
                isUpdated = true;
            }
        }
        return isUpdated;
    }
    public void print () {
        for (T key : keySet()) {
            System.out.println(key);
        }
        System.out.println("//___________________________________");
    }
}