package hu.neuron.imaginer.gallery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import hu.neuron.imaginer.gallery.vo.GalleryVO;
import hu.neuron.imaginer.gallery.vo.ImageVO;

@SessionScoped
@ManagedBean(name = "galleryManagedBean")
public class GalleryManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<GalleryVO> galleries;

	private GalleryVO selectedGallery;
	private GalleryVO newGallery;
	private ImageVO selectedImage;

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

		ArrayList<ImageVO> images = new ArrayList<>();
		images.add(new ImageVO("file1", 1234567l, "1920x1080", "jpg"));
		images.add(new ImageVO("file2", 1234567l, "1920x1080", "jpg"));
		images.add(new ImageVO("file3", 234567l, "1920x1080", "png"));
		images.add(new ImageVO("file4", 12l, "1920x1080", "jpg"));
		images.add(new ImageVO("file5", 12334567l, "1920x1080", "jpg"));
		images.add(new ImageVO("file6", 1234567l, "1920x1080", "tiff"));
		images.add(new ImageVO("file7", 1234567l, "1920x1080", "jpg"));
		galleries.get(0).setImages(images);
	}

	public void initNewGallery() {
		this.newGallery = new GalleryVO(null, new Date(), new Date(), 0);
	}

	public String createGallery() {
		galleries.add(this.newGallery);
		return "index";
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
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

	public GalleryVO getNewGallery() {
		return newGallery;
	}

	public void setNewGallery(GalleryVO newGallery) {
		this.newGallery = newGallery;
	}

	public ImageVO getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(ImageVO selectedImage) {
		this.selectedImage = selectedImage;
	}
}
