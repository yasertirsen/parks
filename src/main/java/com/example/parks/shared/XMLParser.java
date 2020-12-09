package com.example.parks.shared;

import com.example.parks.model.Address;
import com.example.parks.model.Playground;
import com.example.parks.repository.AddressRepository;
import com.example.parks.repository.PlaygroundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class XMLParser {

    private final PlaygroundRepository playgroundRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public XMLParser(PlaygroundRepository playgroundRepository, AddressRepository addressRepository) {
        this.playgroundRepository = playgroundRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public List<Playground> parsePlaygrounds() {
        List<Playground> playgrounds = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("dataset.xml");

            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("Play_Areas");

            for(int i=0; i<nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    Address address = new Address(
                            element.getElementsByTagName("Address1").item(0).getTextContent(),
                            element.getElementsByTagName("Address2").item(0).getTextContent(),
                            element.getElementsByTagName("Address3").item(0).getTextContent()
                    );

                    playgrounds.add(
                            new Playground(
                                    element.getElementsByTagName("Name").item(0).getTextContent(),
                                    element.getElementsByTagName("Type").item(0).getTextContent(),
                                    element.getElementsByTagName("Opening_Hours").item(0).getTextContent(),
                                    addressRepository.save(address),
                                    element.getElementsByTagName("Email").item(0).getTextContent(),
                                    element.getElementsByTagName("Phone").item(0).getTextContent(),
                                    element.getElementsByTagName("Park_Ranger").item(0).getTextContent(),
                                    element.getElementsByTagName("Surface_Type").item(0).getTextContent(),
                                    element.getElementsByTagName("Toilets").item(0).getTextContent(),
                                    element.getElementsByTagName("Disabled_Toilets").item(0).getTextContent(),
                                    element.getElementsByTagName("Baby_Changing").item(0).getTextContent(),
                                    element.getElementsByTagName("Drinking_Water").item(0).getTextContent(),
                                    element.getElementsByTagName("Disabled_Parking").item(0).getTextContent(),
                                    element.getElementsByTagName("Seating").item(0).getTextContent()
                            )
                    );

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        playgroundRepository.saveAll(playgrounds);
        return playgrounds;

    }
}
