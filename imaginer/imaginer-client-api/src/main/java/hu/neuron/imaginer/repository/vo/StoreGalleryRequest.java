package hu.neuron.imaginer.repository.vo;

import hu.neuron.imaginer.gallery.vo.GalleryVO;

public class StoreGalleryRequest {
	private String username;
	private GalleryVO gallery;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public GalleryVO getGallery() {
		return gallery;
	}

	public void setGallery(GalleryVO gallery) {
		this.gallery = gallery;
	}

	public String getGalleryName() {
		return this.gallery.getName();
	}


	public byte[] getIcon() {
		return this.gallery.getIcon();
	}
}
