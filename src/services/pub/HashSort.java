package services.pub;

import   java.util.ArrayList;   
import   java.util.Collections;   
import   java.util.Comparator;   
import   java.util.HashMap;   
import   java.util.Hashtable; 
import   java.util.Iterator;   
import   java.util.List;   
import   java.util.Set;   
import   java.util.TreeSet;   

public class HashSort {
	
    

    public   Set   sort(Hashtable ht)   {   
            List   list   =   new   ArrayList(ht.keySet());   
            Set           keySet     =   ht.keySet();   
            Collections.sort(list,   new   Comparator()   {   
                    public   int   compare(Object   a,   Object   b)   {   
                            return   a.toString().toLowerCase().compareTo(b.toString()   
                                    .toLowerCase());   
                    }   
            });   

            keySet   =   new   TreeSet(list);   
            return keySet;
    }   

   

}
