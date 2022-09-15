package io.dataspaceconnector.controller;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Log4j2
public class CheckRDF {
    static boolean checkRDF(String ap, String rdf) {

        log.info("Checking RDF " + rdf + " for AP " + ap); 

        String command =
        "curl -X POST http://localhost:8080/shacl/validation/report?shapesModel=" + ap + " -H \"Content-Type: application/ld+json\" -d '" + rdf + "'";
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        //processBuilder.directory(new File("/home/philipph/Documents/Arbeit/Fraunhofer/T66/piveau-metrics-validating-shacl"));

        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String line;
        String response = "";
        boolean conforms = false;
        BufferedReader input =// process.errorReader();
                    new BufferedReader(new 
                             InputStreamReader(process.getInputStream()));
        try {
            while ((line = input.readLine()) != null) { 
                //log.info(line); 
                if (line.contains("\"@value\": \"true\"")) {
                    conforms = true;
                }
                response += "\n";
                response += line;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (conforms) {
            log.info("OUTGOING MESSAGE CONFORMS TO APPLICATION PROFILE " + ap);
            log.info(response);
            return true;
        } else {
            log.error("OUTGOING MESSAGE VIOLATES APPLICATION PROFILE " + ap);
            log.error(response);
            return false;
        }
    }
}
