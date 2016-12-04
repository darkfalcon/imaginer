package hu.neuron.imaginer.repository.vo;

public class DeleteGalleryRequest {
	private String username;
	private String galleryName;

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

}
