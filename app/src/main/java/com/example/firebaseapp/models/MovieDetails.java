package com.example.firebaseapp.models;

public class MovieDetails
{

	public String documentId;
	private String title;
	private String studio;
	private String image;
	private String criticsRating;
	private String shortDescription;

//	public MovieDetails(String doc_id, String title, String studio, String cricRate, String image, String short_desc)
//	{
//		this.documentId=doc_id;
//		this.title=title;
//		this.studio=studio;
//		this.criticsRating=cricRate;
//		this.shortDescription=short_desc;
//		this.image=image;
//	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCriticsRating() {
		return criticsRating;
	}

	public void setCriticsRating(String criticsRating) {
		this.criticsRating = criticsRating;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
}
