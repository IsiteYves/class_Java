import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import Utils.*;

class ExtractUrls2 {
    private static String urlInput;
    private static SuccessMessageLogger success = new SuccessMessageLogger();
    private static ErrorMessageLogger error = new ErrorMessageLogger();

    private static String filterUnwantedSymbols(String str) {
        str = str.replace("../", "");
        str = str.replace("./", "");
        str = str.replace("\n", "");
        return str;
    }

    private static void downloadSubLink(String str, Integer linesNbr) {
        try {
            String filePath = "Downloads\\";
            String onlinePath = urlInput + "/" + str;
            String baseURL = urlInput.split("//")[1];
            if (str == "")
                filePath += baseURL + "\\" + baseURL + ".html";
            else
                filePath += baseURL + "\\" + str;
            String extension = filePath.substring(filePath.length() - 4, filePath.length());
            if (extension.equals(".php")) {
                filePath += ".html";
            }
            File newFile = new File(filePath);
            if (!newFile.exists()) {
                if (newFile.getParentFile() != null) {
                    newFile.getParentFile().mkdirs();
                }
                newFile.createNewFile();
            }
            FileWriter myWriter = new FileWriter(filePath);
            try {
                URL url = new URL(onlinePath);
                URLConnection urlConn = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String line = null;
                Integer currentLineCount = 0;
                Double percentage = 0.0;
                while ((line = reader.readLine()) != null) {
                    currentLineCount++;
                    percentage = ((double) currentLineCount / (double) linesNbr) * 100.0;
                    System.out.print("Downloading " + Math.round(percentage) + "%\r");
                    myWriter.write(line);
                }
                myWriter.close();
            } catch (Exception e) {
                error.log(e.getMessage());
            }
        } catch (Exception e) {
            error.log("File creation failed." + e.getMessage());
        }
    }

    public static void downloadURL(String str) throws IOException {
        int x = 0;
        URL newUrl = new URL(urlInput + "/" + str);
        int linesNbr = getlinesNumber(newUrl);
        downloadSubLink(str, linesNbr);
    }

    private static int getlinesNumber(URL webLink) {
        int count = 0;
        try {
            BufferedReader lineCounter = new BufferedReader(new InputStreamReader(webLink.openStream()));
            while (lineCounter.readLine() != null) {
                count++;
            }
            lineCounter.close();
            return count;
        } catch (Exception e) {
            error.log(e.getMessage());
        }
        return count;
    }

    public static void main(String args[]) {
        try {
            urlInput = "";
            while (true) {
                System.out.println("Enter Website\'s Link: ");
                Scanner scan = new Scanner(System.in);
                urlInput = scan.nextLine();
                if (urlInput != "" && !(urlInput.contains("https://") || urlInput.contains("http://")) ||
                        (urlInput.split("https://").length == 0 || urlInput.split("http://").length == 0))
                    System.out.println("Invalid link.Please try again.");
                else if (urlInput == "")
                    System.out.println("Please provide a value.");
                else
                    break;
            }
            URL url = new URL(urlInput);
            URLConnection urlConn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("href=") && line.contains("<a") && !(line.contains("http://") || line.contains("https://"))) {
                    String foundUrl = line.split("href")[1].split("=\"")[1].split("\"")[0] + "\n";
                    if (!(foundUrl.equals("./") || foundUrl.contains("#") || foundUrl.contains("mailto") || foundUrl.isEmpty())) {
                        foundUrl = filterUnwantedSymbols(foundUrl);
                        downloadURL(foundUrl);
                    }
                }
            }
            success.log("### Completed downloading ###");
        } catch (Exception e) {
            error.log(e.getMessage());
        }
    }
}