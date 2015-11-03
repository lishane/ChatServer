/**
 * Created by Shane on 11/1/2015.
 */
public class TestClass {

    public static void main(String args[]) {
        CircularBuffer c = new CircularBuffer(5);
        System.out.println(c.addStamp("hello"));
        System.out.println(c.addStamp("hello"));
        System.out.println(c.addStamp("hello"));

        User[] u = new User[0];
        ChatServer cs = new ChatServer(u, 10);
        System.out.println(cs.parseRequest("This\tis\r\n"));
    }
}
