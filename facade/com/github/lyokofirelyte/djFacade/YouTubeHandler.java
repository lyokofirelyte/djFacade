package com.github.lyokofirelyte.djFacade;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;

public class YouTubeHandler {
	
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String CREDENTIALS_DIRECTORY = "data/.oauth-credentials";
    private Credential credential = null;
    private YouTube youtube;
	
	public void authorize(List<String> scopes, String credentialDatastore) throws Exception {
		Reader clientSecretReader = new InputStreamReader(this.getClass().getResourceAsStream("/seekrits.json"));
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);
        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(CREDENTIALS_DIRECTORY));
        DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore).build();
        LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8085).build();
        credential = new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	}

	public List<SearchResult> getVideos(String query){

		try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("djFacade").build();
            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setQ(query);
            search.setType("video");
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(20L);
            SearchListResponse searchResponse = search.execute();
            return searchResponse.getItems();
        } catch (Exception e){
        	e.printStackTrace();
        }
		
		return new ArrayList<SearchResult>();
    }
	
	public String getEmbedCode(String id){
		return "<iframe width=\"560\" height=\"315\" src=\"https://youtube.com/embed/" + id + "\" frameborder=\"0\" allowfullscreen></iframe>";
	}
	
	public YouTube getYouTube(){
		return youtube;
	}
}