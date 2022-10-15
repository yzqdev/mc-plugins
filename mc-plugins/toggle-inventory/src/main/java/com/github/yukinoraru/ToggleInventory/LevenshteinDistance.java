 package com.github.yukinoraru.ToggleInventory;




 public class LevenshteinDistance
 {
   private static int minimum(int a, int b, int c) { return Math.min(Math.min(a, b), c); }



   public static int computeLevenshteinDistance(CharSequence str1, CharSequence str2) {
     int[][] distance = new int[str1.length() + 1][str2.length() + 1];

     for (int i = 0; i <= str1.length(); i++) {
         distance[i][0] = i;
     }
     for (int j = 1; j <= str2.length(); j++) {
       distance[0][j] = j;
     }
     for (int i = 1; i <= str1.length(); i++) {
       for (int j = 1; j <= str2.length(); j++) {
           distance[i][j] = minimum(
               distance[i - 1][j] + 1,
               distance[i][j - 1] + 1,
               distance[i - 1][j - 1] + (
               (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 :
               1));
       }
     }
     return distance[str1.length()][str2.length()];
   }

   public static int find(String[] list, String str) {
     int minDist = 0;
     int target = 0;
     boolean isFirst = true;
     for (int i = 0; i < list.length; i++) {
       String l = list[i];
       int dist = computeLevenshteinDistance(l, str);
       if (l.startsWith(str)) {
         dist -= str.length() * 2;
       }
       if (isFirst) {
         target = i;
         minDist = dist;
         isFirst = false;
       } else if (dist < minDist) {
         minDist = dist;
         target = i;
       }
     }
     return target;
   }
 }


/* Location:              D:\Desktop\ToggleInventory 1.6.4.jar!\com\github\yukinoraru\ToggleInventory\LevenshteinDistance.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.2
 */