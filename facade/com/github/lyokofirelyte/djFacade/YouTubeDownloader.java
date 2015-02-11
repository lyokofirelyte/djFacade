package com.github.lyokofirelyte.djFacade;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoInfo;
import com.github.axet.vget.vhs.VimeoInfo;
import com.github.axet.vget.vhs.YoutubeInfo;
import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.DownloadInfo.Part;
import com.github.axet.wget.info.DownloadInfo.Part.States;

public class YouTubeDownloader {

    VideoInfo info;
    long last;
    DJFacade main;
    
    public YouTubeDownloader(DJFacade i){
    	main = i;
    }

    public void start(final String url, final File path) {
    	
    	new Thread(){
    		
    		public void run(){
    			try {
    	        	
    	            final AtomicBoolean stop = new AtomicBoolean(false);
    	            Runnable notify = new Runnable() {
    	            	
    	                @Override
    	                public void run(){
    	                	
    	                    VideoInfo i1 = info;
    	                    DownloadInfo i2 = i1.getInfo();

    	                    switch (i1.getState()){
    	                    
    	                    	case DONE:
    	                    		
    		                        if (i1 instanceof YoutubeInfo) {
    		                            YoutubeInfo i = (YoutubeInfo) i1;
    		                            System.out.println(i1.getState() + " " + i.getVideoQuality());
    		                        } else if (i1 instanceof VimeoInfo) {
    		                            VimeoInfo i = (VimeoInfo) i1;
    		                            System.out.println(i1.getState() + " " + i.getVideoQuality());
    		                        }
    		                        
    		                        main.updateHeader("Download complete!");
    		                        
    		                    break;
    		                    
    		                    case RETRYING:
    		                    	stop.set(true);
    		                    	main.updateHeader("FAILED TO DL SONG!");
    		                    break;
    		                    
    		                    case DOWNLOADING:
    		                    	
    		                        long now = System.currentTimeMillis();
    		                        
    		                        if (now - 1000 > last){
    		                        	
    		                            last = now;
    		                            String parts = "";
    		                            List<Part> pp = i2.getParts();
    		                            
    		                            if (pp != null){
    		                                for (Part p : pp) {
    		                                    if (p.getState().equals(States.DOWNLOADING)){
    		                                        parts += String.format("Part#%d(%.2f) ", p.getNumber(), p.getCount() / (float) p.getLength());
    		                                    }
    		                                }
    		                            }
    		                            
    		                            main.updateHeader("Download: " + String.format("%s %.2f %s", i1.getState(), i2.getCount() / (float) i2.getLength(), parts).replace("0.", "") + "%");
    		                        }
    		                        
    		                    break;
    	                    }
    	                }
    	            };

    	            URL web = new URL(url);
    	            VGetParser user = VGet.parser(web);
    	            info = user.info(web);

    	            VGet v = new VGet(info, path);
    	            v.extract(user, stop, notify);

    	            System.out.println("Title: " + info.getTitle());
    	            System.out.println("Download URL: " + info.getInfo().getSource());

    	            v.download(user, stop, notify);
    	            
    	        } catch (Exception e) {
    	           e.printStackTrace();
    	        }
    		}
    		
    	}.start();
    }
}