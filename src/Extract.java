import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.*;
class Extract {
    private static String filterUnwantedSymbols(String str){
        str = str.replace("../","");
        str = str.replace("./","");
        str = str.replace("\n","");
        return str;
    }

    public static void downloadURL(String str, int lineNbr) {
        List<String> list = new ArrayList<>();
        String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        while (m.find()) {
            list.add(str.substring(m.start(0), m.end(0)));
        }
        if (list.size() == 0) {
            System.out.println("No link found on line " + lineNbr);
        }
        int x = 0;
//        for (String foundUrl : list) {
//                if (x == 0)
//                    System.out.println("Found a link on line " + lineNbr + "..." + foundUrl + "\nStoring it in a file...");
//                else
//                    System.out.println(
//                            "Found another link on line " + lineNbr + "..." + foundUrl + "\nStoring it in a file...");
        System.out.println("Downloading...");
        File f1 = new File(
                "C:\\Users\\Benitha\\IdeaProjects\\class_Java\\Link_Folders\\Folder_of_line" + lineNbr);
        boolean createFolder = f1.mkdir();
        if (createFolder) {
            try {
                String filePath = "C:\\Users\\Benitha\\IdeaProjects\\class_Java\\Link_Folders\\Folder_of_line"
                        + lineNbr + "\\" + x + ".html";
                File newFile = new File(filePath);
                newFile.createNewFile();
                FileWriter myWriter = new FileWriter(filePath);
                try {
                    URL url = new URL("https://techcompanyweb.netlify.app");
                    URLConnection igiheConn = url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(igiheConn.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        myWriter.write(line);
                    }
                    System.out.println("Completed creating the file " + x + ".html");
                    myWriter.close();
                } catch (Exception e) {
                    e.getMessage();
                }
            } catch (Exception e) {
                System.out.println("File creation failed." + e.getMessage());
            }
        } else
            System.out.println("Folder creation failed.");
        x++;
//        }
    }
    private static Integer getlinesNumber(URL webLink) throws IOException {
        Integer count = 0;
        try{
            BufferedReader lineCounter = new BufferedReader(new InputStreamReader(webLink.openStream()));
            while( lineCounter.readLine() != null){
                count++;
            }
            lineCounter.close();
            return count;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return count;
    }
    public static void main(String args[]) {
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Website\'s Link: ");
            String urlInput = scan.nextLine();
            URL url = new URL(urlInput);
            URLConnection urlConn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line = null;
            int lineNbr = 1;
            while ((line = reader.readLine()) != null) {
                if (line.contains("href=") && line.contains("<a") && !(line.contains("http") || line.contains("https"))) {
                    String foundUrl=line.split("href")[1].split("=\"")[1].split("\"")[0] + "\n";;
                    if (!(foundUrl.equals("./") || foundUrl.contains("https://") || foundUrl.contains("http://") || foundUrl.contains("#") || foundUrl.contains("mailto") || foundUrl.isEmpty())) {
                        System.out.println("Scanning line " + lineNbr + " for links...");
                        foundUrl=filterUnwantedSymbols(foundUrl);
                        System.out.println("Downloading "+foundUrl);
                        System.out.println(foundUrl);
                        downloadURL(foundUrl, lineNbr);
                    }
//                    lineNbr++;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}