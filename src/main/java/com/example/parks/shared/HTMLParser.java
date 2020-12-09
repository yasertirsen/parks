package com.example.parks.shared;

import com.example.parks.exceptions.ParsingException;
import com.example.parks.model.Address;
import com.example.parks.model.Facility;
import com.example.parks.model.Park;
import com.example.parks.repository.AddressRepository;
import com.example.parks.repository.FacilityRepository;
import com.example.parks.repository.ParkRepository;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class HTMLParser {

    private Park park;
    private Address address;

    private Logger logger = LoggerFactory.getLogger(HTMLParser.class);

    private final ParkRepository parkRepository;
    private final AddressRepository addressRepository;
    private final FacilityRepository facilityRepository;

    @Autowired
    public HTMLParser(ParkRepository parkRepository, AddressRepository addressRepository, FacilityRepository facilityRepository) {
        this.parkRepository = parkRepository;
        this.addressRepository = addressRepository;
        this.facilityRepository = facilityRepository;
    }

    public List<Park> parseDublinParks() throws ParsingException {
        List<Park> parks = new ArrayList<>();
        List<String> parksNames = new ArrayList<>();
        final String url = "https://www.dublincity.ie/residential/parks/dublin-city-parks/visit-park";
        final int OK = 200;
        String parksEndpoint = "?keys=&facilities=All&page=";
        String currentUrl;
        int page = 0;
        int status = OK;
        Connection.Response response = null;
        Document doc = null;

        while (status == OK && page < 4) {
            currentUrl = url + parksEndpoint + String.valueOf(page);  //add the page number to the url
            try {
                response = Jsoup.connect(currentUrl).userAgent("Mozilla/5.0").execute();
            } catch (IOException e1) {
                throw new ParsingException("Error has occurred while trying to connect to " + url);
            }
            status = response.statusCode();

            if (status == OK) {
                try {
                    doc = Jsoup.connect(currentUrl).get();

                    Elements allH2 = doc.getElementsByTag("h2");

                    for (Element element : allH2) {
                        if (element.attr("class").equalsIgnoreCase("search-result__title")) {
                            String name = element.text();
                            parksNames.add(name);
                        }
                    }

                } catch (IOException e) {
                    throw new ParsingException("Error has occurred while trying to connect to " + currentUrl);
                }
            }
            page++;
        }

        for (String park : parksNames) {
            String endpoint = park;
            endpoint = endpoint.replaceAll("[ /]", "-");
            endpoint = endpoint.replaceAll("[.']", "");
            parks.add(getDublinParkInfo(url + "/" + endpoint, park));
        }
        return parks;
    }

    private Park getDublinParkInfo(String url, String parkName) throws ParsingException {
        park = new Park();
        address = new Address();
        park.setName(parkName);
        List<Facility> facilities = new ArrayList<>();
        StringBuilder opTimes = new StringBuilder();

        try {
            logger.info(url);
            final Document document = Jsoup.connect(url).timeout(60000).get();

            Element phone = document.selectFirst("div.field--label-hidden.field--type-telephone.field--name-field-location-phone.field " +
                    "> div.field__items > div.field__item > a");

            Elements facilitiesDiv = document.select("div.text.location__facilities.full__facilities > ul > li");
            Elements addresses = document.select("div.field--label-hidden.field--type-address.field--name-field-location-address.field " +
                    "> div.field__items > div.field__item > p.address > span");

            Element table = null;
            Elements rows = null;
            try {
                table = document.select("table").get(1); //select the second table.
                rows = table.select("tr");

                for(int i = 0; i<rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    opTimes.append(cols.get(0).text()).append(" - ").append(cols.get(1).text()).append("\n");
                }
            } catch (IndexOutOfBoundsException ex) {
                table = document.select("table").get(0); //select the second table.
                rows = table.select("tr");

                for(int i = 0; i<rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    Elements headers = row.select("th");
                    opTimes.append(headers.get(0).text()).append(" - ").append(cols.get(0).text()).append("\n");
                }
            }

            park.setOpeningHours(opTimes.toString());

            if(phone != null)
                park.setPhone(phone.text());

            //Email protected
            park.setEmail("parks@dublincity.ie");

            for(Element result: facilitiesDiv) {
                String facility = result.select("li").text();
                facilities.add(new Facility(facility));
            }

            park.setFacilities(facilityRepository.saveAll(facilities));

            for(Element result: addresses) {
                if(result.select("span").attr("class").equalsIgnoreCase("address-line1"))
                    address.setAddress1(result.select("span").text());
                if(result.select("span").attr("class").equalsIgnoreCase("dependent-locality"))
                    address.setAddress2(result.select("span").text());
                if(result.select("span").attr("class").equalsIgnoreCase("locality")){
                    if(result.select("span").text().matches("[a-zA-Z.]+\\s\\d+"))
                        address.setPostcode(result.select("span").text());
                    else {
                        if (address.getAddress2() != null)
                            address.setPostcode(getPostCode(address.getAddress2()));
                        else
                            address.setPostcode("");
                    }
                }
            }

            park.setAddress(addressRepository.save(address));

        } catch (IndexOutOfBoundsException ex) {
            logger.warn("Could NOT parse opening hours from " + url);
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new ParsingException("Error parsing " + url);
        }
        try {
            parkRepository.save(park);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return park;
    }

    public String getPostCode(String address2) {
        final String url = "https://en.wikipedia.org/wiki/List_of_Dublin_postal_districts";
        try {
            final Document document = Jsoup.connect(url).get();

            Elements list = document.select("div.mw-parser-output > ul > li");

            for(Element result: list) {
                String name = result.select("li").text();
                if(name.contains(address2))
                    return name.split(" ")[0] + " " + name.split(" ")[1];
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        logger.warn("NO POST CODE FOUND FOR " + address2);
        return "Dublin";
    }

    @Transactional
    public List<Park> parseDunLaoParks() throws ParsingException {
        List<Park> parks = new ArrayList<>();
        List<String> parksNames = new ArrayList<>();
        final String url = "https://www.dlrcoco.ie/en/parks-outdoors/playgrounds/";

        try {
            final Document document = Jsoup.connect(url).get();

            Elements parksList = document.select("div.field-label-hidden.field-type-text-with-summary.field-name-body.field > div.field-items > div.even.field-item > ul > li");

            for(Element result: parksList) {
                String name = result.select("li").text().split(",")[0];
                parksNames.add(name);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        final String baseUrl = "https://www.dlrcoco.ie/en/";

        for(String park: parksNames) {
            String endpoint = park;
            endpoint = endpoint.replaceAll("the ", "");
            endpoint = endpoint.replaceAll("'", "");
            endpoint = endpoint.replaceAll(" ", "-");
            parks.add(getDunLaoInfo(park, baseUrl + endpoint, endpoint));
        }
        return parks;
    }

    private Park getDunLaoInfo(String parkName, String url, String endpoint) throws ParsingException {
        park = new Park();
        address = new Address();

        park.setName(parkName);
        try {

            Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0").execute();
            int status = response.statusCode();

            if (status == 200) {
                final Document document = Jsoup.connect(url).get();

                Elements addressEl = document.select(".even.field-item > p");

                for (Element result : addressEl) {
                    String name = result.select("p").text();
                    String[] addressLines = name.split(",", 5);
                    if (addressLines.length == 3) {
                        address.setAddress1(addressLines[0].replaceFirst("^\\s*", ""));
                        address.setAddress2(addressLines[1].replaceFirst("^\\s*", ""));
                        String postcode = addressLines[2].replaceFirst("^\\s*", "");
                        if(postcode.matches("[a-zA-Z.]+\\s\\d+"))
                            address.setPostcode(postcode);
                        else
                            address.setPostcode(getPostCode(address.getAddress2()));
                    }
                }
                park.setAddress(addressRepository.save(address));

                getDunLaoOpTimesAndFacilities(endpoint);
            }
        } catch (HttpStatusException ex) {
            logger.warn("Could NOT find webpage for park: " + parkName);
            parkRepository.save(park);
            return park;
        } catch (IOException e) {
            throw new ParsingException("Error parsing " + url);
        }
        parkRepository.save(park);
        return park;
    }

    private void getDunLaoOpTimesAndFacilities(String endpoint) {
        final String url = "https://www.dlrcoco.ie/en/parks/" + endpoint;
        StringBuilder opTimes = new StringBuilder();
        List<Facility> facilities = new ArrayList<>();

        try {
            final Document document = Jsoup.connect(url).get();
            Elements even = document.select("div.even.field-item > .view-mode-full.clearfix.field-collection-view > .clearfix.field-collection-item-field-opening-hours.entity-field-collection-item.entity > .content > .field-label-hidden.field-type-text.field-name-field-month.field > .field-items > .even.field-item");
            Elements odd = document.select("div.odd.field-item > .view-mode-full.clearfix.field-collection-view > .clearfix.field-collection-item-field-opening-hours.entity-field-collection-item.entity > .content > .field-label-hidden.field-type-text.field-name-field-month.field > .field-items > .even.field-item");
            Elements evenTimes = document.select("div.even.field-item > .view-mode-full.clearfix.field-collection-view > .clearfix.field-collection-item-field-opening-hours.entity-field-collection-item.entity > .content > .field-label-hidden.field-type-text.field-name-field-hours.field > .field-items > .even.field-item");
            Elements oddTimes = document.select("div.odd.field-item > .view-mode-full.clearfix.field-collection-view > .clearfix.field-collection-item-field-opening-hours.entity-field-collection-item.entity > .content > .field-label-hidden.field-type-text.field-name-field-hours.field > .field-items > .even.field-item");

            for(int i = 0; i < even.size(); i++) {
                opTimes.append(even.get(i).text()).append(" - ").append(evenTimes.get(i).text()).append("\n");
            }

            for(int i = 0; i < odd.size(); i++) {
                opTimes.append(odd.get(i).text()).append(" - ").append(oddTimes.get(i).text()).append("\n");
            }

            park.setOpeningHours(opTimes.toString());

            Elements facilitiesEl = document.select("div.views-field-field-facility-details.views-field > .field-content > p");

            String[] splitFacilities = null;

            if(facilitiesEl.text().contains(",")) {
                splitFacilities = facilitiesEl.get(0).text().split(",");
            }

            if(facilitiesEl.text().contains("-")) {
                splitFacilities = facilitiesEl.get(0).text().split("-");
            }

            if(splitFacilities != null) {
                for(String facility : splitFacilities) {
                    facilities.add(new Facility(facility.replaceFirst("^\\s*", "")));
                }
                park.setFacilities(facilityRepository.saveAll(facilities));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
