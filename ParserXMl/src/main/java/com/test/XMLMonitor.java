package com.test;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class XMLMonitor {


    static String dirpath="dirPath";
    static String xsdFilePath="xsdFilePath";
    static String pathToSuccessXMLDir = "pathToSuccessXML";
    static String pathToFailedXMLDir = "pathToFailedXMLDir";
    static int threadPoolSize=20;
    static long timePeriod=10000;

    private static final Logger logger = Logger.getLogger(WorkInThread.class);

    public static void main(String[] args) throws IOException{

        Scanner in = new Scanner(System.in);

       /* System.out.println("Please paste a valid path to XML's file");
        xsdFilePath = in.nextLine();*/
        System.out.println("Please paste a valid directory with XML's file's");
        dirpath = in.nextLine();

        while (true) {
            try {
                ExecutorService service = Executors.newFixedThreadPool(threadPoolSize);
                File[] XMLfiles = new File(dirpath).listFiles();
                File fileXsd = new File(xsdFilePath);

                for (File XMLFile : XMLfiles) {
                    service.submit(new WorkInThread(XMLFile, fileXsd, pathToSuccessXMLDir, pathToFailedXMLDir));
                }
                service.shutdown();
                if(!service.awaitTermination(10, TimeUnit.SECONDS))
                    service.shutdownNow();
                Thread.sleep(timePeriod);

            }catch (InterruptedException e){
                logger.error("TimePeriod was interrupted"+e.getStackTrace());
            }
        }
    }

}
