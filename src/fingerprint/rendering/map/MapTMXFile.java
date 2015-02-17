package fingerprint.rendering.map;

import java.io.*;
import java.util.zip.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.codec.binary.Base64; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


public class MapTMXFile {
   String location = "";
   int[][] data;
   
   
   public MapTMXFile(int[][] data, int width, int height) {
      this.data = data;
      this.location = writeTXM(width, height);
      
   }
   
   public String writeTXM(int w, int h) {
      try {
         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
         
         String width = Integer.toString(w);
         String height = Integer.toString(h);
         String tileWidth = Integer.toString(TilemapRenderer.tileSize);
         String tileHeight = Integer.toString(TilemapRenderer.tileSize);
         
         
         // root elements
         Document doc = docBuilder.newDocument();
         Element mapElement = doc.createElement("map");
         doc.appendChild(mapElement);
         
         mapElement.setAttribute("version","1.0");
         mapElement.setAttribute("orientation", "orthogonal");
         mapElement.setAttribute("width", width);
         mapElement.setAttribute("height", height);
         mapElement.setAttribute("tilewidth", tileWidth);
         mapElement.setAttribute("tileheight", tileHeight);
         
            Element tileset = doc.createElement("tileset");
            tileset.setAttribute("firstgid", "1");
            tileset.setAttribute("name", "tileset");
            tileset.setAttribute("tilewidth", tileWidth);
            tileset.setAttribute("tileheight", tileHeight);
            
               Element image = doc.createElement("image");
               image.setAttribute("source", "tilemap.png");
               image.setAttribute("width", "1280");
               image.setAttribute("height", "2560");
               
            tileset.appendChild(image);
         
         mapElement.appendChild(tileset);
         
         Element layer = doc.createElement("layer");
         layer.setAttribute("name", "layer");
         layer.setAttribute("width", width);
         layer.setAttribute("height", height);
         
            Element data = doc.createElement("data");
            data.setAttribute("encoding", "base64");
            data.setAttribute("compression", "gzip");
               
               StringBuffer bytestring = new StringBuffer();
               for (int x = 0; x < w; x++) {                  
                  for (int y = 0; y < h; y++) {
                      bytestring.append(intTo4CharString(this.data[x][y])); //??
                      System.out.println(bytestring.length());
                  }
               }
               
               Text value = doc.createTextNode(compress(bytestring.toString())); 
               
               data.appendChild(value);
            
            layer.appendChild(data);
            
         mapElement.appendChild(layer);
         
    
         // write the content into xml file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         String location = "resources/" + "rendermap" + ".tmx";
         StreamResult result = new StreamResult(new File(location));
    
         // Output to console for testing
          //StreamResult result = new StreamResult(System.out);
    
         transformer.transform(source, result);
    
        } catch (ParserConfigurationException pce) {
         pce.printStackTrace();
        } catch (TransformerException tfe) {
         tfe.printStackTrace();
        }
      
      return location;
   }
   private static String compress(String str){
      
      byte byteAry[] = null;
      
      try{
         byteAry = str.getBytes("UTF-8");   
      }catch( UnsupportedEncodingException e){
         System.out.println("Unsupported character set");
      }
      //Change ASCII string character 0-9 to actual byte 0-9
      for(int i = 0; i < byteAry.length; i++) {
         if(byteAry[i] == 48)
            byteAry[i] = 0;
         if(byteAry[i] == 49)
            byteAry[i] = 1;
         if(byteAry[i] == 50)
            byteAry[i] = 2;
         if(byteAry[i] == 51)
            byteAry[i] = 3;
         if(byteAry[i] == 52)
            byteAry[i] = 4;
         if(byteAry[i] == 53)
            byteAry[i] = 5;
         if(byteAry[i] == 54)
            byteAry[i] = 6;
         if(byteAry[i] == 55)
            byteAry[i] = 7;
         if(byteAry[i] == 56)
            byteAry[i] = 8;
         if(byteAry[i] == 57)
            byteAry[i] = 9;
         
      }
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      
      try {
         OutputStream deflater = new GZIPOutputStream(buffer);
         deflater.write(byteAry);
         deflater.close();
      }catch (IOException e) {
         throw new IllegalStateException(e);
      }
      Base64 a = new Base64();
      
      String results = Base64.encodeBase64String(buffer.toByteArray());
      return results;
   }   
   private String intTo4CharString(int number){
       StringBuffer result = new StringBuffer();
       result.append(number);
       for(int p = result.length() ; p != 4 ; p++){
           result.append("0");
       }
       return result.toString();
   }
}