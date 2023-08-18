package Client;
// 客户端程序
import java.util.concurrent.TimeoutException;

public class Client {
    private String address;
    private int port;
    private int operationCount = 0;
    private ClientGUI ui;
    public static void main(String[] args) {
        try {
            // Check port format.
            if (Integer.parseInt(args[1]) <= 1024 || Integer.parseInt(args[1]) >= 49151) {
                System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
                System.exit(-1);
            }
            System.out.println("Dictionary Client");
            Client client = new Client(args[0], Integer.parseInt(args[1]));
            client.run();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Lack of Parameters:\nPlease run like \"java -java DictClient.java <server-adress> <server-port>");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Client(String address, int port) {
        this.address = address;
        this.port = port;
        this.operationCount = 0;
        ui = null;
    }

    public void run() {
        try {
            this.ui = new ClientGUI(this);
            ui.getFrame().setVisible(true);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please enter <server-address> <server-port>");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLog(int state, String word, String meaning) {
        System.out.println("--LOG: " + String.valueOf(operationCount) + " ------");
        System.out.println("  Request:");
        switch (state) {
            case StateLib.ADD:
                System.out.println("  Command: ADD");
                break;
            case StateLib.QUERY:
                System.out.println("  Command: QUERY");
                break;
            case StateLib.REMOVE:
                System.out.println("  Command: REMOVE");
                break;
            default:
                System.out.println("  Error: Unknown Command");
                break;
        }
        System.out.println("  Word: " + word);
        if (state == StateLib.ADD) System.out.println("  Meaning:\n\t" + meaning);
        operationCount++;
    }
    private void printResponse(int state, String meaning) {
        System.out.println("  Response:");
        switch (state) {
            case StateLib.SUCCESS:
                System.out.println("  State: SUCCESS");
                break;
            case StateLib.FAIL:
                System.out.println("  State: FAIL");
                break;
            default:
                System.out.println("  Error: Unknown State");
                break;
        }
        System.out.println("  Meaning:\n\t" + meaning);
    }

    public int add(String word, String meaning) {
        String[] resultArr = execute(StateLib.ADD, word, meaning);
        return Integer.parseInt(resultArr[0]);
    }

    public int remove(String word) {
        String[] resultArr = execute(StateLib.REMOVE, word, "");
        return Integer.parseInt(resultArr[0]);
    }

    public String[] query(String word) {
        return execute(StateLib.QUERY, word, "");
    }

    private String[] execute(int command, String word, String meaning) {
        int state = StateLib.FAIL;
        addLog(command, word, meaning);
        try {
            System.out.println("Trying to connect to server...");
            ExecuteThread eThread = new ExecuteThread(address, port, command, word, meaning);
            eThread.start();
            eThread.join(2000);
            if (eThread.isAlive()) {
                eThread.interrupt();
                throw new TimeoutException();
            }
            String[] eThreadResult = eThread.getResult();
            state = Integer.parseInt(eThreadResult[0]);
            meaning = eThreadResult[1];
            System.out.println("Connect Success!");
        } catch (TimeoutException e) {
            state = StateLib.TIMEOUT;
            meaning = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        printResponse(state, meaning);
        String[] resultArr = {String.valueOf(state), meaning};
        return resultArr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public void setOperationCount(int operationCount) {
        this.operationCount = operationCount;
    }

}

