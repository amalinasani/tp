package seedu.duke.data.storage;

import com.alibaba.fastjson.JSON;
import seedu.duke.data.Task;
import seedu.duke.util.DummyModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Decoder {
    public static HashMap<String, String> loadModules(String dataFileName) throws FileNotFoundException {
        String jsonStr;
        jsonStr = loadJsonStringFromFile(dataFileName);
        List<DummyModule> moduleList = JSON.parseArray(jsonStr, DummyModule.class);// extractModules(jsonStr);
        HashMap<String, String> modulesMap = convertModulesToHashMap(moduleList);
        return modulesMap;
    }

    public static ArrayList<Task> loadTasks(String s) {
        return null;
    }

    private static HashMap<String, String> convertModulesToHashMap(List<DummyModule> moduleList) {
        HashMap<String, String> modulesMap = new HashMap<>();
        for (DummyModule module : moduleList) {
            modulesMap.put(module.getModuleCode(), module.getTitle());
        }
        return modulesMap;
    }

    private static String loadJsonStringFromFile(String dataFileName) throws FileNotFoundException {
        String encoding = "utf8";
        File file = new File(dataFileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (FileNotFoundException e) {
            //System.out.println("Retrieving the module list from nusmods...");
            return openModulesFile("https://api.nusmods.com/v2/2019-2020/moduleList.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    private static String openModulesFile(String filePath) {
        int httpResult; // the status from the server response
        String content = "";
        try {
            URL url = new URL(filePath); // create URL
            URLConnection urlConn = url.openConnection(); // try to connect and get the status code
            urlConn.connect();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpResult = httpConn.getResponseCode();
            if (httpResult != HttpURLConnection.HTTP_OK) {
                System.out.print("cannot connect!");
            } else {
                int fileSize = urlConn.getContentLength(); // get the length of the data
                InputStreamReader isReader = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer buffer = new StringBuffer();
                String line; // to save the content of every line
                line = reader.readLine(); // read the first line
                while (line != null) { // if line is empty, means finish reading
                    buffer.append(line); // append to the buffer
                    buffer.append(" "); // add new line
                    line = reader.readLine(); // read the next line
                }
                //System.out.print(buffer.toString());
                content = buffer.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
