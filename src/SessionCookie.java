import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vasudha on 11/1/2015.
 */
public class SessionCookie {
    public static int timeoutLength = 300;
    public static ArrayList<Long> cookieArray = new ArrayList<Long>(0);
    private long id;
    private long lastTimeStamp;


    public SessionCookie()
    {
        Random r = new Random();
        id = (long) r.nextDouble() * 9999;
        if(cookieArray.indexOf(id) == -1)
        {
            this.id = id;
            cookieArray.add(id);
        }
        lastTimeStamp = System.currentTimeMillis();
    }
    public SessionCookie(long id) {
        if(cookieArray.indexOf(id) == -1) {
            this.id = id;
            cookieArray.add(id);
        }
        lastTimeStamp = System.currentTimeMillis();

    }
    
    public boolean hasTimedOut() {
        if (System.currentTimeMillis() - lastTimeStamp > timeoutLength * 1000) {
            return true;
        } else {
            return false;
        }
    }
    public void updateTimeOfActivity()
    {
        lastTimeStamp = System.currentTimeMillis();
    }
    public long getID()
    {
        return id;
    }

}

