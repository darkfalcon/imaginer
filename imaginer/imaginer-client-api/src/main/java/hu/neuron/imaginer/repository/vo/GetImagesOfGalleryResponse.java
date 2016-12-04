package hu.neuron.imaginer.repository.vo;

import java.util.List;

import hu.neuron.imaginer.gallery.vo.ImageVO;

public class GetImagesOfGalleryResponse {

	private List<ImageVO> images;

	public List<ImageVO> getImages() {
		return images;
	}

	public void setImages(List<ImageVO> images) {
		this.images = images;
	}

}
