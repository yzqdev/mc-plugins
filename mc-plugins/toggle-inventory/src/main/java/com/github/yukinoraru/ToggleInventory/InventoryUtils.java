 package com.github.yukinoraru.ToggleInventory;

 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.DataInput;
 import java.io.DataInputStream;
 import java.io.DataOutput;
 import java.io.DataOutputStream;
 import java.lang.reflect.Constructor;
 import java.lang.reflect.Field;
 import java.lang.reflect.Method;
 import java.math.BigInteger;
 import org.bukkit.Bukkit;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.InventoryHolder;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.PlayerInventory;




 public class InventoryUtils
 {
   private static String versionPrefix = "";

   private static Class<?> class_ItemStack;

   private static Class<?> class_NBTBase;

   private static Class<?> class_NBTTagCompound;

   private static Class<?> class_NBTTagList;
   private static Class<?> class_CraftInventoryCustom;
   private static Class<?> class_CraftItemStack;

   static  {
     try {
       String className = Bukkit.getServer().getClass().getName();
       String[] packages = className.split("\\.");
       if (packages.length == 5) {
         versionPrefix = packages[3] + ".";
       }

       class_ItemStack = fixBukkitClass("net.minecraft.server.ItemStack");
       class_NBTBase = fixBukkitClass("net.minecraft.server.NBTBase");
       class_NBTTagCompound = fixBukkitClass("net.minecraft.server.NBTTagCompound");
       class_NBTTagList = fixBukkitClass("net.minecraft.server.NBTTagList");
       class_CraftInventoryCustom = fixBukkitClass("org.bukkit.craftbukkit.inventory.CraftInventoryCustom");
       class_CraftItemStack = fixBukkitClass("org.bukkit.craftbukkit.inventory.CraftItemStack");
     }
     catch (Throwable ex) {
       ex.printStackTrace();
     }
   }

   private static Class<?> fixBukkitClass(String className) {
     className = className.replace("org.bukkit.craftbukkit.", "org.bukkit.craftbukkit." + versionPrefix);
     className = className.replace("net.minecraft.server.", "net.minecraft.server." + versionPrefix);
     try {
       return Class.forName(className);
     } catch (ClassNotFoundException e) {
       e.printStackTrace();
       return null;
     }
   }

   protected static Object getNMSCopy(ItemStack stack) {
     Object nms = null;
     try {
       Method copyMethod = class_CraftItemStack.getMethod("asNMSCopy", new Class[] { ItemStack.class });
       nms = copyMethod.invoke(null, new Object[] { stack });
     } catch (Throwable ex) {
       ex.printStackTrace();
     }
     return nms;
   }

   protected static Object getHandle(ItemStack stack) {
     Object handle = null;
     try {
       Field handleField = stack.getClass().getDeclaredField("handle");
       handleField.setAccessible(true);
       handle = handleField.get(stack);
     } catch (Throwable ex) {
       ex.printStackTrace();
     }
     return handle;
   }

   protected static Object getTag(Object mcItemStack) {
     Object tag = null;
     try {
       Field tagField = class_ItemStack.getField("tag");
       tag = tagField.get(mcItemStack);
     } catch (Throwable ex) {
       ex.printStackTrace();
     }
     return tag;
   }

   public static ItemStack getCopy(ItemStack stack) {
     if (stack == null) {
         return null;
     }

     try {
       Object craft = getNMSCopy(stack);
       Method mirrorMethod = class_CraftItemStack.getMethod("asCraftMirror", new Class[] { craft.getClass() });
       stack = (ItemStack)mirrorMethod.invoke(null, new Object[] { craft });
     } catch (Throwable ex) {
       ex.printStackTrace();
     }

     return stack;
   }

   public static String getMeta(ItemStack stack, String tag, String defaultValue) {
     String result = getMeta(stack, tag);
     return (result == null) ? defaultValue : result;
   }

   public static String getMeta(ItemStack stack, String tag) {
     if (stack == null) {
         return null;
     }
     String meta = null;
     try {
       Object craft = getHandle(stack);
       if (craft == null) {
           return null;
       }
       Object tagObject = getTag(craft);
       if (tagObject == null) {
           return null;
       }
       Method getStringMethod = class_NBTTagCompound.getMethod("getString", new Class[] { String.class });
       meta = (String)getStringMethod.invoke(tagObject, new Object[] { tag });
     } catch (Throwable ex) {
       ex.printStackTrace();
     }
     return meta;
   }

   public static void setMeta(ItemStack stack, String tag, String value) {
     if (stack == null) {
         return;
     }
       try {
       Object craft = getHandle(stack);
       Object tagObject = getTag(craft);
       Method setStringMethod = class_NBTTagCompound.getMethod("setString", new Class[] { String.class, String.class });
       setStringMethod.invoke(tagObject, new Object[] { tag, value });
     } catch (Throwable ex) {
       ex.printStackTrace();
     }
   }

   public static void addGlow(ItemStack stack) {
     if (stack == null) {
         return;
     }
     try {
       Object craft = getHandle(stack);
       Object tagObject = getTag(craft);
       Object enchList = class_NBTTagList.newInstance();
       Method setMethod = class_NBTTagCompound.getMethod("set", new Class[] { String.class, class_NBTBase });
       setMethod.invoke(tagObject, new Object[] { "ench", enchList });
     } catch (Throwable ex) {
       ex.printStackTrace();
     }
   }

   public static String inventoryToString(Inventory inventory) {
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     DataOutputStream dataOutput = new DataOutputStream(outputStream);
     try {
       Object itemList = class_NBTTagList.newInstance();
       for (int i = 0; i < inventory.getSize(); i++) {
         Object outputObject = class_NBTTagCompound.newInstance();
         Object craft = null;
         ItemStack is = inventory.getItem(i);
         if (is != null) {
           craft = getNMSCopy(is);
         } else {
           craft = null;
         }
         if (craft != null && class_ItemStack.isInstance(craft)) {
           Method saveMethod = class_ItemStack.getMethod("save", new Class[] { outputObject.getClass() });
           saveMethod.invoke(craft, new Object[] { outputObject });
         }
         Method addMethod = class_NBTTagList.getMethod("add", new Class[] { class_NBTBase });
         addMethod.invoke(itemList, new Object[] { outputObject });
       }



       Method saveMethod = class_NBTBase.getMethod("a", new Class[] { class_NBTBase, DataOutput.class });
       saveMethod.invoke(null, new Object[] { itemList, dataOutput });
     } catch (Throwable ex) {
       ex.printStackTrace();
     }

     return (new BigInteger(1, outputStream.toByteArray())).toString(32);
   }

   public static Inventory stringToInventory(String data) {
     Inventory inventory = null;

     try {
       ByteArrayInputStream inputStream = new ByteArrayInputStream((new BigInteger(data, 32)).toByteArray());


       Method loadMethod = class_NBTBase.getMethod("a", new Class[] { DataInput.class });
       Object itemList = loadMethod.invoke(null, new Object[] { new DataInputStream(inputStream) });

       Method sizeMethod = class_NBTTagList.getMethod("size", new Class[0]);
       Method getMethod = class_NBTTagList.getMethod("get", new Class[] { int.class });
       int listSize = ((Integer)sizeMethod.invoke(itemList, new Object[0])).intValue();

       Method isEmptyMethod = class_NBTTagCompound.getMethod("isEmpty", new Class[0]);
       Method setItemMethod = class_CraftInventoryCustom.getMethod("setItem", new Class[] { int.class, ItemStack.class });

       inventory = createInventory(null, listSize);

       for (int i = 0; i < listSize; i++) {
         Object inputObject = getMethod.invoke(itemList, new Object[] { Integer.valueOf(i) });
         if (!((Boolean)isEmptyMethod.invoke(inputObject, new Object[0])).booleanValue()) {
           Method createMethod = class_ItemStack.getMethod("createStack", new Class[] { inputObject.getClass() });
           Object newStack = createMethod.invoke(null, new Object[] { inputObject });
           Method bukkitCopyMethod = class_CraftItemStack.getMethod("asBukkitCopy", new Class[] { class_ItemStack });
           Object newCraftStack = bukkitCopyMethod.invoke(null, new Object[] { newStack });
           setItemMethod.invoke(inventory, new Object[] { Integer.valueOf(i), newCraftStack });
         }
       }
     } catch (Throwable ex) {
       ex.printStackTrace();
     }
     return inventory;
   }

   public static Inventory createInventory(InventoryHolder holder, int size) {
     Inventory inventory = null;
     try {
       Constructor<?> inventoryConstructor = class_CraftInventoryCustom.getConstructor(new Class[] { InventoryHolder.class, int.class });
       inventory = (Inventory)inventoryConstructor.newInstance(new Object[] { holder, Integer.valueOf(size) });
     } catch (Throwable ex) {
       ex.printStackTrace();
     }
     return inventory;
   }

   public static boolean inventorySetItem(Inventory inventory, int index, ItemStack item) {
     try {
       Method setItemMethod = class_CraftInventoryCustom.getMethod("setItem", new Class[] { int.class, ItemStack.class });
       setItemMethod.invoke(inventory, new Object[] { Integer.valueOf(index), item });
       return true;
     } catch (Throwable ex) {
       ex.printStackTrace();

       return false;
     }
   }



   public static Inventory getArmorInventory(PlayerInventory playerInventory) {
     ItemStack[] armor = playerInventory.getArmorContents();
     Inventory inventory = createInventory(null, armor.length);
     for (int i = 0; i < armor.length; i++) {
       inventory.setItem(i, armor[i]);
     }
     return inventory;
   }
 }


/* Location:              D:\Desktop\ToggleInventory 1.6.4.jar!\com\github\yukinoraru\ToggleInventory\InventoryUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.2
 */