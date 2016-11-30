package hu.neuron.imaginer.gallery.vo;

import java.io.Serializable;
import java.util.Date;

public class GalleryVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private Date creationDate;
	private Date lastModificationDate;
	private Integer numberOfImages;

	public GalleryVO() {
		super();
	}

	public GalleryVO(String name, Date creationDate, Date lastModificationDate, Integer numberOfImages) {
		super();
		this.name = name;
		this.creationDate = creationDate;
		this.lastModificationDate = lastModificationDate;
		this.numberOfImages = numberOfImages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public Integer getNumberOfImages() {
		return numberOfImages;
	}

	public void setNumberOfImages(Integer numberOfImages) {
		this.numberOfImages = numberOfImages;
	}
}
