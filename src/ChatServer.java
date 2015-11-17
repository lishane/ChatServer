import sun.plugin2.message.Message;

import java.lang.reflect.Array;
import java.util.*;
/*
*
 * <b> CS 180 - Project 4 - Chat Server Skeleton </b>
 * <p>
 * <p>
 * This is the skeleton code for the ChatServer Class. This is a private chat
 * server for you and your friends to communicate.
 *
 * @author (Your Name) <(YourEmail@purdue.edu)>
 * @version (Today's Date)
 * @lab (Your Lab Section)*/


public class ChatServerTemp {

    ArrayList<User> users = new ArrayList<>();
    CircularBuffer cb;
    MessageFactory m = new MessageFactory();

    public ChatServerTemp(User[] users, int maxMessages) {
        this.users.add(new User("root", "cs180"));
        for (int i = 0; i < users.length; i++) {
            this.users.add(users[i]);
        }
        this.cb = new CircularBuffer(maxMessages);
    }

/*

*
     * This method begins server execution.
*/


    public void run() {
        boolean verbose = false;
        System.out.printf("The VERBOSE option is off.\n\n");
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.printf("Input Server Request: ");
            String command = in.nextLine();

            // this allows students to manually place "\r\n" at end of command
            // in prompt
            command = replaceEscapeChars(command);

            if (command.startsWith("kill"))
                break;

            if (command.startsWith("verbose")) {
                verbose = !verbose;
                System.out.printf("VERBOSE has been turned %s.\n\n", verbose ? "on" : "off");
                continue;
            }

            String response = null;
            try {
                response = parseRequest(command);
            } catch (Exception ex) {
                response = MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR,
                        String.format("An exception of %s occurred.", ex.getMessage()));
            }

            // change the formatting of the server response so it prints well on
            // the terminal (for testing purposes only)
            if (response.startsWith("SUCCESS\t"))
                response = response.replace("\t", "\n");

            // print the server response
            if (verbose)
                System.out.printf("response:\n");
            System.out.printf("\"%s\"\n\n", response);
        }

        in.close();
    }

/*
*
     * Replaces "poorly formatted" escape characters with their proper values.
     * For some terminals, when escaped characters are entered, the terminal
     * includes the "\" as a character instead of entering the escape character.
     * This function replaces the incorrectly inputed characters with their
     * proper escaped characters.
     *
     * @param str - the string to be edited
     * @return the properly escaped string

*/

    private static String replaceEscapeChars(String str) {
        str = str.replace("\\r", "\r");
        str = str.replace("\\n", "\n");
        str = str.replace("\\t", "\t");

        return str;
    }

/*
*
     * Determines which client command the request is using and calls the
     * function associated with that command.
     *
     * @param request - the full line of the client request (CRLF included)
     * @return the server response
*/


    public String parseRequest(String request) {
        if (request.equals(null) || request.length() == 0 || !request.endsWith("\r\n")) {
            return generateFailure(m.FORMAT_COMMAND_ERROR);
        }
        String[] req = request.split("\t");
        req[req.length - 1] = req[req.length - 1].substring(0, req[req.length - 1].length() - 2);
        return handleRequest(req);
    }

    public boolean verifyCookie(long cookie) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCookie().getID() == cookie)
                if (users.get(i).getCookie().hasTimedOut()) {
                    users.get(i).setCookie(null);
                    return false;
                } else
                    return true;
        }
        return false;
    }

    public String addUser(String[] args) {
        for (int i = 0; i < args[2].length(); i++) {
            if (Character.isLetterOrDigit(args[2].charAt(i)) == false) {
                return MessageFactory.makeErrorMessage(24);
            }

        }
        if (args[2].length() > 20 && args[2].length() < 1) {
            return MessageFactory.makeErrorMessage(24);
        }
        if (args[3].length() > 40 && args[3].length() < 1) {
            return MessageFactory.makeErrorMessage(24);
        }
        User u = new User(args[2], args[3]);
        u.getCookie().updateTimeOfActivity();
        users.add(u);
        return "SUCCESS\r\n";
    }

    public String userLogin(String[] args) {
        boolean exists = false;
        int index = -1;
        for (int i = 0; i < users.size(); i++) {
            String name = users.get(i).getName();
            if (args[0].equals(name)) {
                exists = true;
                index = i;
            }
        }
        // checks to see if 1) user isnt already authenticated 2) if the user is already created 3) password is correct
        if (users.get(index).getCookie() != null || exists == false || users.get(index).checkPassword(args[1]) == false) {
            return MessageFactory.makeErrorMessage(24);
        } else {
            SessionCookie s = new SessionCookie();
            users.get(index).setCookie(s);
            String paddedID = String.format("%04d", s.getID());
            return "SUCCESS\t" + paddedID + "\r\n";
        }
    }

    public String postMessage(String[] args, String name) {
        String trimmed = args[2].trim();
        if (trimmed.length() < 1) {
            return MessageFactory.makeErrorMessage(24);
        }
        String result = args[1] + ": " + args[2];
        cb.put(result);
        int index = -1;
        for (int i = 0; i < users.size(); i++) {
            String name1 = users.get(i).getName();
            if (name1.equals(name)) {
                index = i;
            }
        }
        users.get(index).getCookie().updateTimeOfActivity();
        return "SUCCESS\r\n";
    }

    public String getMessages(String[] args) {
        if (Integer.parseInt(args[2]) >= 1) {

        }

    }


    public boolean isLoggedIn(long cookie) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCookie().getID() == cookie) {
                return true;
            } else
                return false;
        }
        return false;
    }

    public String handleRequest(String[] req) {
        switch (req[0]) {
            case "ADD-USER": // cookie, username, pass
                if (req.length != 4)
                    return generateFailure(m.FORMAT_COMMAND_ERROR);
                if (!isLoggedIn(Long.parseLong(req[1])))
                    return generateFailure(m.LOGIN_ERROR);
                if (!verifyCookie(Long.parseLong(req[1])))
                    return generateFailure(m.COOKIE_TIMEOUT_ERROR);
                try {
                    return addUser(req[2], req[3]);
                } catch (Exception e) {
                    System.out.println(m.makeErrorMessage(m.));
                }
                break;
            case "USER-LOGIN": // username, password
                if (req.length != 3)
                    return generateFailure(m.FORMAT_COMMAND_ERROR);
                return userLogin(req[1], req[2]);
            break;
            case "POST-MESSAGE": // cookie, message
                if (req.length != 3)
                    return generateFailure(m.FORMAT_COMMAND_ERROR);
                if (!isLoggedIn(Long.parseLong(req[1])))
                    return generateFailure(m.LOGIN_ERROR);
                if (!verifyCookie(Long.parseLong(req[1])))
                    return generateFailure(m.COOKIE_TIMEOUT_ERROR);
                return postMessage(req[2]);
            break;
            case "GET-MESSAGES": //cookie numMessage
                if (req.length != 4)
                    return generateFailure(m.FORMAT_COMMAND_ERROR);
                if (!isLoggedIn(Long.parseLong(req[1])))
                    return generateFailure(m.LOGIN_ERROR);
                if (!verifyCookie(Long.parseLong(req[1])))
                    return generateFailure(m.COOKIE_TIMEOUT_ERROR);
                try {
                    return getMessages(Integer.parseInt(req[2]));
                } catch (Exception e) {
                    System.out.println(m.makeErrorMessage(m.));
                }
                break;
            default:
                return generateFailure(m.UNKNOWN_COMMAND_ERROR);
            break;

        }
        return request;

    public String generateFailure(int code) {
        return generateFailure(code, m.makeErrorMessage(code));
    }

    public String generateFailure(int code, String message) {
        String s = "FAILURE\t" + code + "\t" + message + "\r\n";
        return s;
    }
}
}

