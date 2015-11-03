import java.util.ArrayList;

public class CircularBuffer {

    private int size;
    private ArrayList<String> messages;
    private static int counter;

    public CircularBuffer(int size) {
        this.size = size;
        messages = new ArrayList<String>();
        counter = 0;
    }

    public String[] getNewest(int numMessages) {
        if (numMessages < 0)
            return null;
        if (numMessages == 0) {
            String[]emptyArray = new String[numMessages];
            return emptyArray;
        }
        String[] messagesArray = messages.toArray(new String[min(messages.size(), numMessages)]);
        return messagesArray;
    }

    public void put(String message) {
        if (messages.size() < size) {
            messages.add(addStamp(message));
        } else {
            messages.remove(messages.size() - 1);
            messages.add(0, message);
        }
    }

    public String addStamp(String message) {
        String stamp = createStamp();
        return stamp + message;
    }

    public String createStamp() {
        String counterString = "";
        for (int i = 0; i < 4 - Integer.toString(counter).length(); i++) {
            counterString = "0" + counterString;
        }
        counter++;
        return counterString + counter + ") ";
    }

    public int min(int a, int b) {
        if (a < b) {
            return a;
        } else
            return b;
    }

}
