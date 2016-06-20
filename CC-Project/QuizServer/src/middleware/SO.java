/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import java.io.File;

/**
 *
 * @author carlosmorais
 */
public class SO {
        
    public static String getSeverDirSongs(){
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        
        if(os.contains("windows")){            
            return (System.getProperty("user.dir").replace("\\dist", "") + "\\songs\\");
        }
        else if(os.startsWith("mac os x")){
            return (System.getProperty("user.dir").replace("/dist", "") + "/songs/");
        }
        
        return null;
    }
    
    
    public static String getSeverDirImages(){
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        
        if(os.contains("windows")){
            return (System.getProperty("user.dir").replace("\\dist", "") + "\\images\\");
        }
        else if(os.startsWith("mac os x")){
            return (System.getProperty("user.dir").replace("/dist", "") + "/images/");
        }
        
        return null;
    }
    
    
    public static void creatDirToFiles(){
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        
        if(os.contains("windows")){
            new File( (System.getProperty("user.dir") + "\\songs\\") ).mkdirs();
            new File( (System.getProperty("user.dir") + "\\images\\") ).mkdirs();
        }
        else if(os.startsWith("mac os x")){
            new File( (System.getProperty("user.dir")+"/temp/songs/") ).mkdirs();
            new File( (System.getProperty("user.dir")+"/temp/images/") ).mkdirs();
        }
                       
    }
    
    
    public static String getUserDirSongs(){
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        
        if(os.contains("windows")){
            return (System.getProperty("user.dir") + "\\songs\\");
        }
        else if(os.startsWith("mac os x")){
            return (System.getProperty("user.dir") + "/temp/songs/");
        }
        
        return null;
    }
    
    
    public static String getUserDirImages(){
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        
        if(os.contains("windows")){
            return (System.getProperty("user.dir") + "\\images\\");
        }
        else if(os.startsWith("mac os x")){
            return (System.getProperty("user.dir") + "/temp/images/");
        }
        
        return null;
    }
       
    /*TEMPORÁRIO*/
    public static String getUserChallengesDir(){
        String os = System.getProperty("os.name");
        os = os.toLowerCase();
        
        if(os.contains("windows")){
            return (System.getProperty("user.dir") + "\\challenges\\");
        }
        else if(os.startsWith("mac os x")){
            return (System.getProperty("user.dir") + "/temp/challenges/");
        }
        
        return null;
    }
}
