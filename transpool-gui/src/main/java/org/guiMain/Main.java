package org.guiMain;

import org.transpool.engine.Engine;
import org.transpool.engine.ds.Match;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws JAXBException, IOException {
        //launch(args);
        Engine engine = new Engine();
        engine.loadMap(new File("/Users/shimon/Desktop/Transpool/transpool/transpool-engine/src/main/resources/ex1-small.xml"));
        engine.inRequest("shim","D","H",1,9,5,"checkout",true,10);
        List<Match> matches = engine.getMatches(engine.getRequestTrips().get(0),5);
        System.out.println(matches.size());
        for(Match match:matches){
            int count = 0;
            System.out.println(match.getRoutes().get(0).get(0) + "-" + match.getLastStop());
            for(List<String> route:match.getRoutes()) {
                System.out.println(route + " offerid:" + match.getOfferIDs().get(count) + "  time:" + match.getTimeForEachRoute().get(count).getCheckoutTime()
                + " - " + match.getTimeForEachRoute().get(count).getArrivalTime());
                count++;
            }
        }





    }
}
