package timofey.xmlParser;

import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;
import timofey.config.SourceInit;
import timofey.entities.NewsArticle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class XMLParserByUrl extends DefaultHandler {

    private NewsArticle[] articles;
    private SourceInit rssResources;
    private File xmlDocument;
    private String httpOutput;
    public XMLParserByUrl(String url, SourceInit rssResources){
        this.rssResources = rssResources;
        String xml = getBodyFromRequest(url);
        this.articles = parseXML(xml);
    }

    //TODO
    public XMLParserByUrl(String[] urls){

    }

    protected abstract NewsArticle[] parseXML(String xml);
    private String getBodyFromRequest(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse httpResponse;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
//                    .version(HttpClient.Version.HTTP_2)
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

        String output =  httpResponse.body().toString();
        File file = convertStringToXMLDocument(output);
        this.httpOutput = output;
        this.xmlDocument  = file;
        return output;
    }
    private  File convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content

//        File file = new File(path);
        xmlDocument = null;
        try {
            xmlDocument = File.createTempFile("tempData", ".xml");

            FileWriter writer = new FileWriter(xmlDocument);
            writer.write(xmlString);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while converting XML string to file.");
            e.printStackTrace();
        }
        return xmlDocument;

    }

     public String getHttpOutput() {
        return httpOutput;
    }

    public File getXmlDocument() {
        return xmlDocument;
    }
    public boolean deleteTempFile(){
        return this.xmlDocument.delete();
    }


}
