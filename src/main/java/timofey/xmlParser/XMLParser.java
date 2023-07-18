package timofey.xmlParser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class XMLParser extends DefaultHandler {

    private File xmlDocument;
    public XMLParser(String url, DefaultHandler defaultHandler) throws ParserConfigurationException, SAXException, IOException {
        this.xmlDocument =  getBodyFromRequest(url);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(this.getXmlDocument(), defaultHandler);
        String tempFileName = this.getXmlDocument().getName();
        boolean isDeleted = this.deleteTempFile();
        System.out.println(tempFileName + " id deleted: " + isDeleted);
    }

    private File getBodyFromRequest(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse httpResponse;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            httpResponse = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        File file = convertStringToXMLDocument(httpResponse);
        return file;
    }
    private File convertStringToXMLDocument(HttpResponse xmlResponse) {
        String xmlString = xmlResponse.body().toString();
        xmlDocument = null;
        try {
            xmlDocument = File.createTempFile("tempData", ".xml");
            OutputStream outputStream = new FileOutputStream(xmlDocument);
            outputStream.write(xmlString.getBytes());
        } catch (IOException e) {
            System.out.println("An error occurred while converting XML string to file.");
            e.printStackTrace();
        }
        return xmlDocument;

    }

    public File getXmlDocument() {
        return xmlDocument;
    }
    public boolean deleteTempFile(){
        return this.xmlDocument.delete();
    }


}
