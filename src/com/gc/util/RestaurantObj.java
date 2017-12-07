package com.gc.util;

public class RestaurantObj {

	private String zomatoID;
	private String restName;
	private String restLocation;
	private String restRating;
	
	public RestaurantObj() {

	}

	public RestaurantObj(String restName, String restLocation, String restRating, String zomatoID) {
		super();
		this.restName = restName;
		this.restLocation = restLocation;
		this.restRating = restRating;
		this.zomatoID = zomatoID;
	}
	public RestaurantObj(String zomatoID) {
		this.zomatoID = zomatoID;
		
	}
	

	public String getZomatoID() {
		return zomatoID;
	}

	public void setZomatoID(String zomatoID) {
		this.zomatoID = zomatoID;
	}

	public String getRestName() {
		return restName;
	}

	public void setRestName(String restName) {
		this.restName = restName;
	}

	public String getRestLocation() {
		return restLocation;
	}

	public void setRestLocation(String restLocation) {
		this.restLocation = restLocation;
	}

	public String getRestRating() {
		return restRating;
	}

	public void setRestRating(String restRating) {
		this.restRating = restRating;
	}
	
	/*public void getRestaurantInfo() {
		ZoomatoAPI myAPI = new ZoomatoAPI();//no location not needed for this search
		myAPI.searchByRestID(zomatoID);
	}*/
	
	
	@Override
	public String toString() {
		return "Restaurant [restName=" + restName + ", restLocation=" + restLocation + ", restRating=" + restRating	+ "]";
	}

}
