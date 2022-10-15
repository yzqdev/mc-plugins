 package com.github.yukinoraru.ToggleInventory;

 import java.io.InputStream;
 import java.net.HttpURLConnection;
 import java.net.MalformedURLException;
 import java.net.URL;
 import javax.xml.parsers.DocumentBuilderFactory;
 import org.w3c.dom.Document;
 import org.w3c.dom.Node;
 import org.w3c.dom.NodeList;




 public class UpdateChecker
 {
   private ToggleInventory plugin;
   private URL filesFeed;
   private String version;
   private String link;

   public String getVersion() { return this.version; }



   public String getLink() { return this.link; }


   public UpdateChecker(ToggleInventory plugin, String url) {
     this.plugin = plugin;

     try {
       this.filesFeed = new URL(url);
     } catch (MalformedURLException e) {
       plugin.getLogger().warning("Update check failed.");
     }
   }


   public boolean updateNeeded() {
     try {
       HttpURLConnection urlConnection = (HttpURLConnection)this.filesFeed.openConnection();
       urlConnection.setConnectTimeout(5000);
       urlConnection.setReadTimeout(5000);

       InputStream input = urlConnection.getInputStream();
       Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

       Node latestFile = document.getElementsByTagName("item").item(0);
       NodeList children = latestFile.getChildNodes();

       this.version = children.item(1).getTextContent().replaceAll("[a-zA-Z -]", "").replaceAll("\\(.*\\)", "");
       this.link = children.item(3).getTextContent();




       String runningVersion = this.plugin.getDescription().getVersion().replaceAll("[-a-zA-Z ]", "");

       int cmp = runningVersion.compareTo(this.version);
       if (cmp < 0) {
         return true;
       }
       if (cmp > 0) {

         this.plugin.getLogger().info("This version is newer than dev.bukkit.org one.");
       } else {

         this.plugin.getLogger().info("This is the latest version :)");
       }

     } catch (Exception e) {
       this.plugin.getLogger().warning(String.format("Update check failed. (%s)", new Object[] { e.getMessage() }));
     }
     return false;
   }
 }


/* Location:              D:\Desktop\ToggleInventory 1.6.4.jar!\com\github\yukinoraru\ToggleInventory\UpdateChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.2
 */