import Utils.*;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExtractURLs {
    private static WarningMessageLogger warning = new WarningMessageLogger();
    private static SuccessMessageLogger success = new SuccessMessageLogger();
    private static ErrorMessageLogger error = new ErrorMessageLogger();
    private static URL url;
    private static Scanner scan = new Scanner(System.in);
    private static String baseUrl;

    private static String[] getWebsiteNavigationLinks() throws IOException {
        String[] webLinks = {};
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            Set<String> webpageLinks = new HashSet<String>();

            String link;
            while((link = reader.readLine()) != null) {
                if (link.contains("href=") && link.contains("<a") && !(link.contains("http") || link.contains("https"))) {
                    String anchor = link.split("href")[1].split("=\"")[1].split("\"")[0] + "\n";
                    if (!(anchor.equals("./") || anchor.contains("#") || anchor.contains("mailto") || anchor.isEmpty())) {
                        webpageLinks.add(anchor);
                    }
                }
            }
            webLinks = webpageLinks.toArray(new String[webpageLinks.size()]);
            reader.close();
            return webLinks;
        }
        catch(Exception e){
            error.log(e.getMessage());
        }
        return webLinks;
    }

    private static Integer getFileSize(URL website) throws IOException{
        Integer count = 0;
        try{
            BufferedReader lineCounter = new BufferedReader(new InputStreamReader(website.openStream()));
            while( lineCounter.readLine() != null){
                count++;
            }
            lineCounter.close();
            return count;
        }
        catch(Exception e){
            error.log(e.getMessage());
        }
        return count;
    }

    private static String filterUnwantedKeywords(String sentence){
        sentence = sentence.replace("../","");
        sentence = sentence.replace("./","");
        sentence = sentence.replace("\n","");
        return sentence;
    }

    private static void createAFile(String filename) throws IOException {
        try{
            filename = filename.contains("?") ? filename.split("\\?")[0] : filename;
            File file = new File("Storage/"+baseUrl.split("//")[1]+filename);
            if(!file.exists()){
                if(file.getParentFile() != null){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
        }
        catch(Exception e){
            error.log(e.getMessage());
        }
    }

    private static void DownloadANavigationLink(String filename,Integer fileSize) throws IOException{
        try{
            BufferedReader webReader = new BufferedReader(new InputStreamReader(url.openStream()));
            filename = filename.contains("?") ? filename.split("\\?")[0] : filename;
            filename = filename.split("\\.")[0].length() == 0 ? "home.html" : filename.split("\\.")[0] + ".html";

            createAFile(filename);

            BufferedWriter writer = new BufferedWriter(new FileWriter("Storage/"+baseUrl.split("//")[1]+filename));
            String line;
            Integer currentLineCount = 0;
            Double percentage = 0.0;

            while((line = webReader.readLine()) != null){
                currentLineCount++;
                percentage = ((double)currentLineCount/(double)fileSize) * 100.0;
                System.out.print("Downloading "+Math.round(percentage)+"%\r");
                writer.write(line+"\n");
            }

            System.out.println();

            webReader.close();
            writer.close();

            success.log("Downloaded "+filename.replace("./","") +" successfully");
        }
        catch(Exception e){
            error.log(e.getMessage());
        }
    }

    private static void download() throws IOException {
        try{
            String webLinks[] = getWebsiteNavigationLinks();

            for(int i=0;i<webLinks.length;i++){
                webLinks[i] = filterUnwantedKeywords(webLinks[i]);

                url = new URL(baseUrl+"/"+webLinks[i]);

                warning.log("Downloading "+webLinks[i].replace("./",""));
                System.out.print("Downloading 0%\r");

                Integer fileSize = getFileSize(url);

                DownloadANavigationLink(webLinks[i],fileSize);
            }
            success.log("### Downloaded the whole website successfully ###");
        }
        catch(Exception e){
            error.log(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException{
        try{
            System.out.println("Enter the website you want to clone(format: https://website.com)");
            baseUrl = scan.nextLine();
            url = new URL(baseUrl);

            download();
        }
        catch(Exception e){
            error.log(e.getMessage());
        }
    }
}