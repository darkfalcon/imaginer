package hu.neuron.imaginer.repository.vo;

import hu.neuron.imaginer.gallery.vo.GalleryVO;

public class GetGalleryResponse {
	
	private GalleryVO gallery;

	public GalleryVO getGallery() {
		return gallery;
	}

	public void setGallery(GalleryVO gallery) {
		this.gallery = gallery;
	}

}
