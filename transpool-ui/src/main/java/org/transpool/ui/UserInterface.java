package org.transpool.ui;

import org.transpool.engine.Api;
import org.transpool.engine.ds.RequestTrip;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserInterface {
    private final int NUM_OF_OPTIONS = 7;
    private Api api;
    private Option[] options;
    private boolean endSystem;

    public UserInterface() {
        endSystem = false;
        api = new Api();
        options = new Option[NUM_OF_OPTIONS];
        options[0] = this::loadData;
        options[1] = this::makeRequest;
        options[2] = this::makeOffer;
        options[3] = this::viewOffers;
        options[4] = this::viewRequest;
        options[5] = this::findMatch;
        options[6] = () -> {
            endSystem = true;
            System.out.println("Goodbye!!");
        };
    }

    public void start() throws JAXBException, IOException {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        while (!endSystem) {
            System.out.println("Enter number of option:");
            System.out.println("1 - Load data from XML file");
            System.out.println("2 - Make a travel request");
            System.out.println("3 - Make a travel offer");
            System.out.println("4 - View the status of all travel offers");
            System.out.println("5 - View the status of all travel requests");
            System.out.println("6 - Find a match for a travel request");
            System.out.println("7 - EXIT");

            userInput = scanner.nextLine();
            if (userInput.length() != 1 || userInput.charAt(0) > '6' || userInput.charAt(0) < '1')
                System.out.println("ERROR: you must enter number between 1-6\nTry again:");
            else {
                int feather = userInput.charAt(0) - '1';
                options[feather].action();
            }
        }
    }

    private void loadData() throws JAXBException, IOException {
        Scanner scanner = new Scanner(System.in);
        String path;
        boolean valid = false;
        File file;
        while (!valid) {
            System.out.println("Enter the file path you would like to load:\n0 - Return to menu");
            path = scanner.nextLine();
            if (path.charAt(0) == '0')
                break;
            file = new File(path);
            if (!file.exists())
                System.out.println("ERROR: file is not exist\nTry again:");
            else if (!file.getName().endsWith("xml"))
                System.out.println("ERROR: not XML file\nTry again:");
            else if (!api.loadMap(file)) {
                System.out.println("ERROR: failed to load the file:");
                System.out.println(api.getErrorDes());
                System.out.println("Try again:");
            } else {
                System.out.println("File has been loaded successfully!");
                valid = true;
            }
        }
    }

    private void makeRequest() {
        List<String> stops = api.getStops();
        if (stops == null) {
            System.out.println("ERROR: you need to load map first");
            return;
        }
        boolean valid = false;
        String fullRequest;
        List<String> data;
        int minutes, hours;
        Scanner scanner = new Scanner(System.in);
        System.out.println("All existing stations:");
        api.getStops().forEach(x -> System.out.println(x));
        while (!valid) {
            System.out.println("Enter travel request by following these instructions:");
            System.out.println("[name],[from:name station],[to:name station],[time:hours 0-23],[time:minutes in multiple of 5],[checkout/arrival]");
            System.out.println("For example:shimon,nirit,tel-aviv,12,35,checkout");
            System.out.println("0 - Back to menu");
            fullRequest = scanner.nextLine();
            if (fullRequest.charAt(0) == '0')
                return;
            data = Arrays.stream(fullRequest.split(",")).collect(Collectors.toList());
            String errorDes = validRequestInput(data, stops);
            if (errorDes != null)
                System.out.println(errorDes);
            else {
                RequestTrip requestTrip = api.inRequest(data.get(0), data.get(1), data.get(2), Integer.parseInt(data.get(3)), Integer.parseInt(data.get(4)), data.get(5));
                System.out.println(requestTrip);
                System.out.println("Request travel have been added successfully");
                valid = true;
            }

        }

    }


    private String validRequestInput(List<String> strings, List<String> stops) {
        if (strings.size() != 6)
            return "ERROR: missing data";

        if (!stops.contains(strings.get(1)))
            return "ERROR: " + strings.get(1) + " invalid station";

        if (!stops.contains(strings.get(2)))
            return "ERROR: " + strings.get(2) + " invalid station";

        try {
            int hour = Integer.parseInt(strings.get(3));
            if (hour > 23 || hour < 0)
                return "ERROR: hours must be between 0-23";
        } catch (NumberFormatException n) {
            return "ERROR: hours must be a number";
        }

        try {
            int minutes = Integer.parseInt(strings.get(4));
            if (minutes % 5 != 0)
                return "ERROR: " + minutes + " minutes must be multiple of 5";
        } catch (NumberFormatException n) {
            return "ERROR: minutes must be a number";
        }

        String whichTime = strings.get(5);
        if (!whichTime.equals("checkout") && !whichTime.equals("arrival"))
            return "ERROR: " + whichTime + " is not equal to checkout/arrival";
        return null;
    }


    private void makeOffer() {

    }

    private void viewOffers() {

    }

    private void viewRequest() {

    }

    private void findMatch() {

    }

}


