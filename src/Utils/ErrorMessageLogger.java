package Utils;

public class ErrorMessageLogger {
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public void log(String message){
        System.out.println(RED+message+RESET);
    }
}
