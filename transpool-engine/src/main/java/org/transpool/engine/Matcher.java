package org.transpool.engine;

import org.transpool.engine.ds.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Matcher {


    private List<Match> optionalMatches;
    private MapDb map;
    private Map<Integer, TranspoolTrip> transPoolTrips;
    private final RequestTrip requestTrip;

    public Matcher(MapDb mapDb,Map<Integer, TranspoolTrip> transPoolTrips,RequestTrip requestTrip) {
        this.map = mapDb;
        optionalMatches = new ArrayList<>();
        this.transPoolTrips = transPoolTrips;
        this.requestTrip = requestTrip;

    }

    private void makeOptionalMatches() throws CloneNotSupportedException {
        String initStop = requestTrip.getFrom();
        String destStop = requestTrip.getTo();
        List<Integer> trips = map.getMap().get(initStop).getTrips();
        Queue<Match> queue = new LinkedList<>();
        for (int offerId : trips) {
            TranspoolTrip transpoolTrip = transPoolTrips.get(offerId);
            if (transpoolTrip.getStopsManager().get(initStop).getCapacity() > 0)
                queue.add(new Match(offerId, initStop, transpoolTrip.whenArrivedToStop(initStop, map)));
        }

        while (!queue.isEmpty()) {
            Match match = queue.remove();
            TranspoolTrip nextTrip = transPoolTrips.get(match.getNextOfferId());
            int index = nextTrip.getRoute().indexOf(match.getLastStop());
            if (nextTrip.getRoute().size() > (++index) && nextTrip.getStopsManager().get(match.getLastStop()).getCapacity() > 0) {
                String nextStop = nextTrip.getRoute().get(index);
                Time timeArrived = nextTrip.whenArrivedToStop(nextStop, map);
                Time timeCheckout = nextTrip.whenArrivedToStop(match.getLastStop(), map);
                if (nextStop.equals(destStop)) {
                     if(match.isPossibleToAddOffer(nextStop, -1, timeCheckout)) {
                         match = match.addOffer(nextStop, -1, timeArrived, timeCheckout);
                         if (!optionalMatches.contains(match))
                             optionalMatches.add(match);
                     }
                }
                else {
                    List<Integer> offersToAdd = map.getMap().get(nextStop).getTrips();
                    for (int offerId : offersToAdd) {
                        if (match.isPossibleToAddOffer(nextStop, offerId, timeCheckout))
                            queue.add(match.addOffer(nextStop, offerId, timeArrived, timeCheckout));
                    }
                }
            }
        }
    }
    private void updateCostAvgEachMatch(){
        optionalMatches.forEach(match -> {
            List<String> fullRoute = new ArrayList<>();
            List<List<String>> routes = match.getRoutes();
            List<Integer> offersId = match.getOfferIDs();
            int count = 0;
            for(List<String> route:routes) {
                fullRoute = Stream.concat(fullRoute.stream(), route.stream()).collect(Collectors.toList());
                fullRoute.remove(fullRoute.size() - 1);
                match.setCost(match.getCost() + TripDetails.cost(map,transPoolTrips.get(offersId.get(count)).getPpk(),route));
                count++;
            }
            fullRoute.add(match.getLastStop());
            match.setAvgFoul(TripDetails.avgFuelCon(map,fullRoute));
        });
    }

    public List<Match> getOptionalMatches(int limit) throws CloneNotSupportedException {
        makeOptionalMatches();
        for(Match match:optionalMatches)
            match.updateOffersNames(transPoolTrips);
        updateCostAvgEachMatch();
        if (!requestTrip.isComfortable())
            setSnobMatches(limit);
        boolean checkout = false;

        if (requestTrip.getRequestTime().getWhichTime().name().equals(RequestTime.WhichTime.checkout.name()))
            checkout = true;
        setMatchesByTime(checkout, requestTrip.getFlexHours(), limit, requestTrip.getRequestTime().getTime());
        return optionalMatches;
    }

    private void setMatchesByTime(boolean checkout,int flexHours,int limit,Time requestTime){
        optionalMatches = optionalMatches.stream().filter(match -> {
            if (checkout) {
                Time startTime = match.getStartTime();
                if (Math.abs(requestTime.getDay() - startTime.getDay()) > 1)
                    return false;
                if (startTime.getDay() < requestTime.getDay()) {
                    int hourCom = (startTime.getHours() + flexHours) % 24;
                    if (hourCom < requestTime.getHours())
                        return false;
                    else if (hourCom == requestTime.getHours())
                        if (startTime.getMinutes() < requestTime.getMinutes())
                            return false;
                }
                if (requestTime.getDay() < startTime.getDay()) {
                    int hourCom = (requestTime.getHours() + flexHours) % 24;
                    if (hourCom < startTime.getHours())
                        return false;
                    else if (hourCom == startTime.getHours())
                        if (requestTime.getMinutes() < startTime.getMinutes())
                            return false;
                }
                if (requestTime.getDay() == startTime.getDay()) {
                    int distance = Math.abs(startTime.getHours() - requestTime.getHours());
                    if (distance > flexHours)
                        return false;
                    if (distance == flexHours) {
                        if (startTime.getHours() < requestTime.getHours()) {
                            if (startTime.getMinutes() < requestTime.getMinutes())
                                return false;
                        }
                        if (requestTime.getHours() < startTime.getHours()) {
                            if (requestTime.getMinutes() < startTime.getMinutes())
                                return false;
                        }
                        if (requestTime.getHours() == startTime.getHours())
                            if (requestTime.getMinutes() != startTime.getMinutes())
                                return false;

                    }
                }
            } else {
                Time finishTime = match.getFinishTime();
                if (Math.abs(requestTime.getDay() - finishTime.getDay()) > 1)
                    return false;
                if (finishTime.getDay() < requestTime.getDay()) {
                    int hourCom = (finishTime.getHours() + flexHours) % 24;
                    if (hourCom < requestTime.getHours())
                        return false;
                    else if (hourCom == requestTime.getHours())
                        if (finishTime.getMinutes() < requestTime.getMinutes())
                            return false;
                }
                if (requestTime.getDay() < finishTime.getDay()) {
                    int hourCom = (requestTime.getHours() + flexHours) % 24;
                    if (hourCom < finishTime.getHours())
                        return false;
                    else if (hourCom == finishTime.getHours())
                        if (requestTime.getMinutes() < finishTime.getMinutes())
                            return false;
                }
                if (requestTime.getDay() == finishTime.getDay()) {
                    int distance = Math.abs(finishTime.getHours() - requestTime.getHours());
                    if (distance > flexHours)
                        return false;
                    if (distance == flexHours) {
                        if (finishTime.getHours() < requestTime.getHours()) {
                            if (finishTime.getMinutes() < requestTime.getMinutes())
                                return false;
                        }
                        if (requestTime.getHours() < finishTime.getHours()) {
                            if (requestTime.getMinutes() < finishTime.getMinutes())
                                return false;
                        }
                        if (requestTime.getHours() == finishTime.getHours())
                            if (requestTime.getMinutes() != finishTime.getMinutes())
                                return false;
                    }
                }
            }
            return true;
        }).sorted(mySort()).limit(limit).collect(Collectors.toList());

    }

    private Comparator<Match> mySort() {
        return (m1, m2) -> {
            if (m1.getOfferIDs().size() == 1)
                return -1;
            if (m2.getOfferIDs().size() == 1)
                return 1;
            if (TripDetails.howLong(map, TripDetails.appendRoutes(m1)) > TripDetails.howLong(map, TripDetails.appendRoutes(m2)))
                return -1;
            return 1;

        };
    }

    private void setSnobMatches(int limit){
            optionalMatches = optionalMatches.stream().filter(match -> {
                if(match.getOfferIDs().size() > 1)
                    return false;
                return true;
            }).limit(limit).collect(Collectors.toList());
    }


    public boolean setMatch(Match match) {
        if (!optionalMatches.contains(match))
            return false;
        int count = 0;
        List<List<String>> routes = match.getRoutes();
        for (List<String> route : routes)
            transPoolTrips.get(match.getOfferIDs().get(count)).
                    addRequestTrip(requestTrip.getId(), requestTrip.getName(), route.get(0), route.get(route.size() - 1));
        requestTrip.setMatch(match);
        return true;
    }

}
