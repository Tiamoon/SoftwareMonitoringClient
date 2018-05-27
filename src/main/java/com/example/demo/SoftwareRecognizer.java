package com.example.demo;

import com.example.demo.model.InstalledSoftware;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by Kamil on 22.04.2018.
 */
public class SoftwareRecognizer {

    private static final String REGQUERY_UTIL = "reg query ";
    private static final String REGSTR_TOKEN = "REG_SZ";
    static String s = REGQUERY_UTIL + "HKEY_LOCAL_MACHINE\\Software"
            + "\\Microsoft\\Windows\\CurrentVersion\\Uninstall";

    static String s2 = REGQUERY_UTIL + "HKEY_LOCAL_MACHINE\\HARDWARE\\DESCRIPTION\\System\\BIOS";

    public static String getCurrentUserPersonalFolderPath() {
        try {
            Process process = Runtime.getRuntime().exec(s);
            StreamReader reader = new StreamReader(process.getInputStream());

            reader.start();
            process.waitFor();
            reader.join();

            String result = reader.getResult();
            int p = result.indexOf(REGSTR_TOKEN);

//            if (p == -1)
//                return null;

            return result.substring(p + REGSTR_TOKEN.length()).trim();
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }


    static class StreamReader extends Thread {
        private final InputStream is;
        private final StringWriter sw;

        StreamReader(InputStream is) {
            this.is = is;
            sw = new StringWriter();
        }

        @Override
        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String getResult() {
            return sw.toString();
        }
    }

    public static List<InstalledSoftware> getDisplayNameDword() {
        List<InstalledSoftware> installedSoft = new ArrayList<>();

        Set<String> set = new HashSet<>();
        String[] array = new String[500];
        array = getCurrentUserPersonalFolderPath().split("\n");

        for (String i : array) {
            installedSoft.add(getName((i.trim())));
        }

        return installedSoft;
    }

    private static InstalledSoftware getName(String s) {
        Process nameProcess = null;
        Process commentsProcess = null;
        Process locationProcess = null;
        Process publisherProcess = null;
        Process dateProcess = null;
        Process versionProcess = null;

        try {
            // Run reg query, then read output with StreamReader (internal class)
            nameProcess = Runtime.getRuntime().exec("reg query " +
                    '"' + s + "\" /v DisplayName");

            commentsProcess = Runtime.getRuntime().exec("reg query " +
                    '"' + s + "\" /v Comments");

            locationProcess = Runtime.getRuntime().exec("reg query " +
                    '"' + s + "\" /v InstallLocation");

            publisherProcess = Runtime.getRuntime().exec("reg query " +
                    '"' + s + "\" /v Publisher");

            dateProcess = Runtime.getRuntime().exec("reg query " +
                    '"' + s + "\" /v InstallDate");

            versionProcess = Runtime.getRuntime().exec("reg query " +
                    '"' + s + "\" /v DisplayVersion");

            StreamReader nameReader = new StreamReader(nameProcess.getInputStream());
            nameReader.start();
            nameProcess.waitFor();
            nameReader.join();

            StreamReader commentsReader = new StreamReader(commentsProcess.getInputStream());
            commentsReader.start();
            commentsProcess.waitFor();
            commentsReader.join();

            StreamReader locationReader = new StreamReader(locationProcess.getInputStream());
            locationReader.start();
            locationProcess.waitFor();
            locationReader.join();

            StreamReader publisherReader = new StreamReader(publisherProcess.getInputStream());
            publisherReader.start();
            publisherProcess.waitFor();
            publisherReader.join();

            StreamReader dateReader = new StreamReader(dateProcess.getInputStream());
            dateReader.start();
            dateProcess.waitFor();
            dateReader.join();

            StreamReader versionReader = new StreamReader(versionProcess.getInputStream());
            versionReader.start();
            versionProcess.waitFor();
            versionReader.join();

            // Parse out the value
            String[] name = nameReader.getResult().split(REGSTR_TOKEN);
            String[] comments = commentsReader.getResult().split(REGSTR_TOKEN);
            String[] location = locationReader.getResult().split(REGSTR_TOKEN);
            String[] publisher = publisherReader.getResult().split(REGSTR_TOKEN);
            String[] date = dateReader.getResult().split(REGSTR_TOKEN);
            String[] version = versionReader.getResult().split(REGSTR_TOKEN);

            if(name.length > 1)
            return new InstalledSoftware(name[name.length -1].trim(), version[version.length -1].trim(), location[location.length -1].trim(), publisher[publisher.length -1].trim(),
                    date[date.length -1].trim(), comments[comments.length -1].trim());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
