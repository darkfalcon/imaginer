package hu.neuron.imaginer.gallery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.neuron.imaginer.gallery.vo.GalleryVO;
import hu.neuron.imaginer.gallery.vo.ImageVO;
import hu.neuron.imaginer.repository.service.RepositoryService;
import hu.neuron.imaginer.repository.vo.DeleteGalleryRequest;
import hu.neuron.imaginer.repository.vo.DeleteGalleryResponse;
import hu.neuron.imaginer.repository.vo.GetGalleriesForUserRequest;
import hu.neuron.imaginer.repository.vo.GetGalleriesForUserResponse;
import hu.neuron.imaginer.repository.vo.GetImageRequest;
import hu.neuron.imaginer.repository.vo.GetImagesOfGalleryRequest;
import hu.neuron.imaginer.repository.vo.GetImagesOfGalleryResponse;
import hu.neuron.imaginer.repository.vo.StoreGalleryRequest;
import hu.neuron.imaginer.repository.vo.StoreGalleryResponse;
import hu.neuron.imaginer.repository.vo.StoreImageRequest;
import hu.neuron.imaginer.user.UserManagedBean;

@SessionScoped
@ManagedBean(name = "galleryManagedBean")
public class GalleryManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GalleryManagedBean.class.getName());

	@ManagedProperty("#{repositoryServiceImpl}")
	private RepositoryService repositoryService;

	@ManagedProperty("#{userManagedBean}")
	private UserManagedBean userManagedBean;

	private List<GalleryVO> galleries;

	private GalleryVO selectedGallery;
	private GalleryVO newGallery;
	private UploadedFile galleryIcon;
	private ImageVO selectedImage;

	@PostConstruct
	public void init() {
		try {
			this.galleries = new ArrayList<>();
			GetGalleriesForUserRequest request = new GetGalleriesForUserRequest();
			request.setUsername(this.userManagedBean.getActualUser().getUsername());
			GetGalleriesForUserResponse response = this.repositoryService.getGalleriesForUser(request);
			this.galleries.addAll(response.getGalleries());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void initNewGallery() {
		this.newGallery = new GalleryVO();
	}

	public String createGallery() {
		try {
			StoreGalleryRequest request = new StoreGalleryRequest();
			request.setGallery(this.newGallery);
			request.setUsername(this.userManagedBean.getActualUser().getUsername());
			StoreGalleryResponse response = this.repositoryService.storeGallery(request);
			this.galleries.add(response.getGallery());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "index";
	}

	public void deleteGallery(final ActionEvent event) {
		try {
			DeleteGalleryRequest request = new DeleteGalleryRequest();
			request.setGalleryName(this.selectedGallery.getName());
			request.setUsername(this.userManagedBean.getActualUser().getUsername());
			this.repositoryService.deleteGallery(request);
			this.galleries.remove(this.selectedGallery);
			this.selectedGallery = null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void handleImageUpload(final FileUploadEvent event) {
		UploadedFile file = event.getFile();
		ImageVO image = new ImageVO();
		image.setName(file.getFileName());
		image.setFileFormat(file.getContentType());

		StoreImageRequest request = new StoreImageRequest();
		request.setUsername(userManagedBean.getActualUser().getUsername());
		request.setGalleryName(this.selectedGallery.getName());
		request.setImage(image);
		request.setImageContent(file.getContents());
		repositoryService.storeImage(request);
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void handleIconUpload(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		this.newGallery.setIcon(file.getContents());
	}

	public void loadImages() {
		if (this.selectedGallery == null) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
			} catch (IOException e) {
				logger.error("Failed to redirect index.xhtml!");
			}
		} else {
			this.selectedGallery.setImages(new ArrayList<>());
			try {
				GetImagesOfGalleryRequest request = new GetImagesOfGalleryRequest();
				request.setGalleryName(this.selectedGallery.getName());
				request.setUsername(this.userManagedBean.getActualUser().getUsername());
				GetImagesOfGalleryResponse response = this.repositoryService.getImagesOfGallery(request);
				this.selectedGallery.getImages().addAll(response.getImages());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public void upload(final ActionEvent event) {
		GetImageRequest request = new GetImageRequest();
		request.setPath(this.userManagedBean.getActualUser().getUsername() + "/" + this.selectedGallery.getName()
				+ "/unnamed.png");
		repositoryService.getImage(request);
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

	public UploadedFile getGalleryIcon() {
		return galleryIcon;
	}

	public void setGalleryIcon(UploadedFile galleryIcon) {
		this.galleryIcon = galleryIcon;
	}

	public ImageVO getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(ImageVO selectedImage) {
		this.selectedImage = selectedImage;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setUserManagedBean(UserManagedBean userManagedBean) {
		this.userManagedBean = userManagedBean;
	}
}
