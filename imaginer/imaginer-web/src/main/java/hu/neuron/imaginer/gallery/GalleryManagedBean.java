package hu.neuron.imaginer.gallery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import hu.neuron.imaginer.gallery.vo.GalleryVO;

@SessionScoped
@ManagedBean(name = "galleryManagedBean")
public class GalleryManagedBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<GalleryVO> galleries;
	
	private GalleryVO selectedGallery;
	
	@PostConstruct
	public void init() {
		galleries = new ArrayList<>();
		galleries.add(new GalleryVO("Nyaralás 2016", new Date(), new Date(), 10));
		galleries.add(new GalleryVO("Nyaralás 2015", new Date(), new Date(), 20));
		galleries.add(new GalleryVO("Nyaralás 2013", new Date(), new Date(), 130));
		galleries.add(new GalleryVO("Nyaralás 2011", new Date(), new Date(), 10));
		galleries.add(new GalleryVO("Nyaralás 2012", new Date(), new Date(), 140));
		galleries.add(new GalleryVO("Nyaralás 2010", new Date(), new Date(), 20));
		galleries.add(new GalleryVO("Nyaralás 2005", new Date(), new Date(), 40));
	}

	public List<GalleryVO> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<GalleryVO> galleries) {
		this.galleries = galleries;
	}

	public GalleryVO getSelectedGallery() {
		return selectedGallery;
	}

	public void setSelectedGallery(GalleryVO selectedGallery) {
		this.selectedGallery = selectedGallery;
	}

}
