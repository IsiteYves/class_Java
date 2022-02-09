package Utils;

public class SuccessMessageLogger {
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    public void log(String message){
        System.out.println(GREEN+message+RESET);
    }
}
