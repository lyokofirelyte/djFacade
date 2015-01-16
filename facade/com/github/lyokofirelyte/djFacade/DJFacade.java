package com.github.lyokofirelyte.djFacade;

import gnu.trove.map.hash.THashMap;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.lyokofirelyte.djFacade.Identifiers.AR;
import com.github.lyokofirelyte.djFacade.Identifiers.Resource;
import com.github.lyokofirelyte.djFacade.Listeners.ActionEventListener;
import com.github.lyokofirelyte.djFacade.Listeners.LineEventListener;
import com.github.lyokofirelyte.djFacade.Listeners.MouseEventListener;
import com.github.lyokofirelyte.djFacade.Listeners.WindowListener;
import com.github.lyokofirelyte.djFacade.Panels.Panel;

public class DJFacade {
	
	public Map<String, Object> registeredClasses = new THashMap<String, Object>();
	public Map<Resource, JSONMap> files = new THashMap<Resource, JSONMap>();
	public Map<String, String> styles = new THashMap<String, String>();
	public Map<String, Float> counters = new THashMap<String, Float>();
	public Map<String, Boolean> bools = new THashMap<String, Boolean>();
	public List<String> buttons = new ArrayList<String>();
	private MouseEventListener mouseListener;
	private YouTubeHandler handler = new YouTubeHandler();
	
	public DJFacade(){
		tryCatch(this, "start");
	}
	
	public static void main(String[] args){
		
		System.out.println("Booting up & adjusting system themes.");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){}
		
		new DJFacade();
	}
	
	public void start() throws Exception {
		
		try {
			handler.authorize(new ArrayList<String>(Arrays.asList("https://www.googleapis.com/auth/youtube.readonly")), "youtubetest");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		listeners();
		tryCatch(this, "settings");
		boolean cont = false;
		
		if (!files.get(Resource.SETTINGS).getBool("loggedIn")){
			defaultSetup();
			getPanel(Resource.PANEL_LOGIN).display();
		} else {
			Map<String, Object> sendMap = new THashMap<>();
			sendMap.put("username", files.get(Resource.SETTINGS).getStr("username"));
			sendMap.put("password", files.get(Resource.SETTINGS).getStr("password"));
			
			JSONObject obj = sendPost("/api/login", sendMap);
			
			if (obj.get("success").equals(true)){
				getPanel(Resource.MAIN).display();
				if (files.get(Resource.SETTINGS).containsKey("mainX")){
					getPanel(Resource.MAIN).getGui().setLocation(files.get(Resource.SETTINGS).getInt("mainX"), files.get(Resource.SETTINGS).getInt("mainY"));
				}
			} else {
				System.out.println("Files have been modified! Re-auth needed!");
				getPanel(Resource.PANEL_LOGIN).display();
			}
		}
		
		System.out.println("System completed startup.");
	}
	
	public void updateHeader(String name){
		getPanel(Resource.MAIN).getGui().getPanel("main").setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), loadStyle("setup.dj") + name + loadStyle("setup_end.dj")));
	}
	
	public void hideHeader(){
		updateHeader("");
	}
	
	public YouTubeHandler getYouTubeHandler(){
		return handler;
	}
	
	public void defaultSetup(){
		files.get(Resource.SETTINGS).set("transparency", 0.05f);
		files.get(Resource.SETTINGS).set("on_top", true);
		files.get(Resource.SETTINGS).set("color_tint", "#FFFFFF");
		files.get(Resource.SETTINGS).set("tool_tips", true);
		buttons = new ArrayList<String>(Arrays.asList("add", "remove", "refresh", "veto", "favorite", "video", "download", "list", "chat", "unlock", "settings"));
		files.get(Resource.SETTINGS).put("allowedButtons", buttons);
	}
	
	public void listeners(){
		
		List<Class<?>> allClasses = new ArrayList<Class<?>>();
		mouseListener = new MouseEventListener(this, "default");
        
        try {
        
	        List<String> classNames = new ArrayList<String>();
	        ZipInputStream zip = new ZipInputStream(new FileInputStream("data/djFacade.jar"));
	        boolean look = false;
	        
	        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()){
	        	
	        	if (entry.isDirectory()){
	        		look = entry.getName().contains("djFacade");
	        	}
	        	
	            if (entry.getName().endsWith(".class") && !entry.isDirectory() && look) {
	            	
	                StringBuilder className = new StringBuilder();
	                
	                for (String part : entry.getName().split("/")) {
	                	
	                    if (className.length() != 0){
	                        className.append(".");
	                    }
	                    
	                    className.append(part);
	                    
	                    if (part.endsWith(".class")){
	                        className.setLength(className.length()-".class".length());
	                    }
	                }
	                
	                classNames.add(className.toString());
	            }
	        }
	        
	        for (String clazz : classNames){
	        	allClasses.add(Class.forName(clazz));
	        }
	        
        } catch (Exception e){
        	e.printStackTrace();
        }
        
		for (Class<?> clazz : allClasses){
			
			Object obj = null;

			try {
				Constructor<?> con = clazz.getConstructors()[0];
				con.setAccessible(true);
				obj = con.newInstance(this);
			} catch (Exception e1){
				continue;
			}
			
			if (obj instanceof AR && !clazz.toString().contains("\\$") && !registeredClasses.containsKey(clazz.toString())){
				registeredClasses.put(clazz.toString(), obj);
			}
		}

		System.out.println(registeredClasses.size() + " classes auto-registered.");
	}

	public void settings() throws Exception {
		
		File file = new File("data/settings.json");
		
		if (!file.exists()){
			new File("data").mkdirs();
			file.createNewFile();
			JSONObject obj = new JSONObject();
			obj.put("loggedIn", "false");
			obj.put("created", System.currentTimeMillis());
			saveJSON("data/settings.json", obj);
		}
		
		JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader("data/settings.json"));
		JSONMap map = new JSONMap();
		
		for (Object o : obj.keySet()){
			map.put(o, obj.get(o));
		}
		
		files.put(Resource.SETTINGS, map);
		
		new File("data/styles").mkdirs();
		
		for (String fileName : new File("data/styles").list()){
			if (fileName.endsWith(".dj")){
				styles.put(fileName.replace(".dj", ""), loadStyle(fileName));
			}
		}
	}
	
	public void onlyShow(Panel... panels){
		for (Object obj : registeredClasses.values()){
			if (obj instanceof Panel){
				boolean cont = true;
				for (Panel show : panels){
					if (obj.equals(show)){
						cont = false;
						break;
					}
				}
				if (cont){
					try {
						((Panel) obj).destroy();
					} catch (Exception e){}
				}
			}
		}
	}
	
	public Panel getPanel(Resource name){
		return (Panel) registeredClasses.get(name.getType());
	}
	
	public String loadStyle(String name){
		
		try {
			return new String(Files.readAllBytes(Paths.get("data/styles/" + name)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "null";
	}
	
	public void saveAll() throws Exception {
		
		files.get(Resource.SETTINGS).put("mainX", getPanel(Resource.MAIN).getGui().getX());
		files.get(Resource.SETTINGS).put("mainY", getPanel(Resource.MAIN).getGui().getY());

		for (Resource file : files.keySet()){
			saveJSON(file.getType(), files.get(file));
		}
	}
	
	public WindowListener getListeners(){
		return (WindowListener) registeredClasses.get(WindowListener.class.toString());
	}
	
	public ActionEventListener getEventListener(){
		return (ActionEventListener) registeredClasses.get(ActionEventListener.class.toString());
	}
	
	public MouseEventListener getMouseListener(){
		return mouseListener;
	}
	
	public LineEventListener getLineListener(){
		return (LineEventListener) registeredClasses.get(LineEventListener.class.toString());
	}
	
	public ImageIcon getImage(String path, int width, int height){
		
		try {
		    BufferedImage img = ImageIO.read(getClass().getClassLoader().getResource(path));
		    Image dimg2 = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return new ImageIcon(dimg2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ImageIcon getImage(String path){
		try {
		    BufferedImage img = ImageIO.read(getClass().getClassLoader().getResource(path));
			return new ImageIcon(img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Object tryCatch(Object clazz, String method, Object... args){
		
		try {
			return clazz.getClass().getMethod(method).invoke(clazz, args);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void saveJSON(String name, JSONObject obj){
		
		try {
			FileWriter writer = new FileWriter(name);
			writer.write(obj.toJSONString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject sendPost(final String folder, final Map<String, Object> map){
		
		JSONObject result = null;
		
		try {
			
			JSONObject json = new JSONObject();
			
			for (String key : map.keySet()){
				json.put(key, map.get(key));
			}
			 
			String url = "http://worldsapart.no-ip.org:9090" + folder;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(json.toJSONString());
			wr.flush();
			wr.close();
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			in.close();
			
			result = (JSONObject) new JSONParser().parse(response.toString());
			
		} catch (Exception e){}
		
		return result;
	}
}