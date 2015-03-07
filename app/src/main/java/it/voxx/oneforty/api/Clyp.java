package it.voxx.oneforty.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Clyp {

	private static final String CLYP_API_UPLOAD = "http://upload.clyp.it/upload";
	private static final String CLYP_API_METADATA = "http://api.clyp.it/";
	private static final String CLYP_FILES_PREFIX = "http://a.clyp.it/";
	private static final String CLYP_FILES_SUFFIX_MP3 = ".mp3";
	private File file;
	private String description;
	private Float latitude;
	private Float longitude;
	private String id;

	public Clyp(File file) {
		this.file = file;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String upload() throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(CLYP_API_UPLOAD);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("description", description));
		params.add(new BasicNameValuePair("longitude", longitude.toString()));
		params.add(new BasicNameValuePair("latitude", latitude.toString()));
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("audioFile", file);		
		httpPost.setEntity(builder.build());
		HttpResponse response = httpClient.execute(httpPost);
		String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
		System.out.println(responseBody);
		try {
			JSONObject jResponse = new JSONObject(responseBody);
			this.id = jResponse.getString("AudioFileId");
			return id;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getFileUrl() {
		if (id != null) {
			return CLYP_FILES_PREFIX + id + CLYP_FILES_SUFFIX_MP3;
		}
		return null;
	}
	
	public String getMetadataUrl() {
		if (id != null) {
			return CLYP_API_METADATA + id;
		}
		return null;
	}

}
