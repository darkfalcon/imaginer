package hu.neuron.imaginer.repository.vo;

import hu.neuron.imaginer.gallery.vo.ImageVO;

public class StoreImageRequest {
	
	private String username;
	private String galleryName;
	private ImageVO image;
	private byte[] imageContent;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getGalleryName() {
		return galleryName;
	}

	public void setGalleryName(String galleryName) {
		this.galleryName = galleryName;
	}

	public ImageVO getImage() {
		return image;
	}

	public void setImage(ImageVO image) {
		this.image = image;
	}

	public byte[] getImageContent() {
		return imageContent;
	}

	public void setImageContent(byte[] imageContent) {
		this.imageContent = imageContent;
	}
}
