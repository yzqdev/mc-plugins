 package com.github.yukinoraru.ToggleInventory;

 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashMap;
 import org.bukkit.potion.PotionEffect;
 import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;



 public class PotionUtils
 {
   private static final String defaultDelimiter = ",";

   static String serializePotion(Collection<PotionEffect> collection) throws IOException {
     String result = "";
     for (PotionEffect effect : collection) {
       HashMap<String, Object> tmp = new HashMap<String, Object>(effect.serialize());
       result = result + serialize(tmp) + ",";
     }
     return result;
   }



   static Collection<PotionEffect> deserializePotion(String effectsInString) throws IOException, ClassNotFoundException {
     ArrayList<PotionEffect> result = new ArrayList<PotionEffect>();
     if (effectsInString == null || effectsInString.length() == 0) {
       return result;
     }
     for (String effectInSring : effectsInString.split(",")) {
       if (effectInSring != null && effectInSring.length() != 0) {


         HashMap<String, Object> tmp = (HashMap<String, Object>)deserialize(effectInSring);
         PotionEffect potionEffect = new PotionEffect(tmp);
         result.add(potionEffect);
       }
     }  return result;
   }



   static Object deserialize(String s) throws IOException, ClassNotFoundException {
     byte[] data = Base64Coder.decode(s);
     ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));

     Object o = ois.readObject();
     ois.close();
     return o;
   }


   static String serialize(Serializable o) throws IOException {
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutputStream oos = new ObjectOutputStream(baos);
     oos.writeObject(o);
     oos.close();
     return new String(Base64Coder.encode(baos.toByteArray()));
   }
 }


/* Location:              D:\Desktop\ToggleInventory 1.6.4.jar!\com\github\yukinoraru\ToggleInventory\PotionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */