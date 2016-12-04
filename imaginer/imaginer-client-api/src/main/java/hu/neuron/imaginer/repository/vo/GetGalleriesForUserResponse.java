package hu.neuron.imaginer.repository.vo;

import java.util.List;

import hu.neuron.imaginer.gallery.vo.GalleryVO;

public class GetGalleriesForUserResponse {

	private List<GalleryVO> galleries;

	public List<GalleryVO> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<GalleryVO> galleries) {
		this.galleries = galleries;
	}

}
