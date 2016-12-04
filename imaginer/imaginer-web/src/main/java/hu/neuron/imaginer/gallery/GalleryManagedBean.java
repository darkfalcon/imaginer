package hu.neuron.imaginer.gallery;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.gallery.vo.GalleryVO;
import hu.neuron.imaginer.gallery.vo.ImageVO;
import hu.neuron.imaginer.repository.service.RepositoryService;
import hu.neuron.imaginer.repository.vo.DeleteGalleryRequest;
import hu.neuron.imaginer.repository.vo.DeleteImageRequest;
import hu.neuron.imaginer.repository.vo.DeleteImageResponse;
import hu.neuron.imaginer.repository.vo.GetGalleriesForUserRequest;
import hu.neuron.imaginer.repository.vo.GetGalleriesForUserResponse;
import hu.neuron.imaginer.repository.vo.GetImageRequest;
import hu.neuron.imaginer.repository.vo.GetImagesOfGalleryRequest;
import hu.neuron.imaginer.repository.vo.GetImagesOfGalleryResponse;
import hu.neuron.imaginer.repository.vo.StoreGalleryRequest;
import hu.neuron.imaginer.repository.vo.StoreGalleryResponse;
import hu.neuron.imaginer.repository.vo.StoreImageRequest;
import hu.neuron.imaginer.repository.vo.StoreImageResponse;
import hu.neuron.imaginer.user.UserManagedBean;
import hu.neuron.imaginer.util.MessageBundle;

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

	/**
	 * A galéria oldalról az új galéria létrehozása gombra kattintva hívódik
	 * meg. Mindig új GalleryVO-t hoz létre.
	 * 
	 * @return outcome
	 */
	public String initGallery() {
		this.newGallery = new GalleryVO();
		return "create-gallery";
	}

	/**
	 * Ha valaki linkkel navigál a galéria létrehozás oldalra, ne fusson hibára
	 * a kód.
	 */
	public void initNewGallery() {
		if (this.newGallery == null) {
			this.newGallery = new GalleryVO();
		}
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
		try {
			UploadedFile file = event.getFile();
			ImageVO image = new ImageVO();
			image.setName(file.getFileName());
			image.setFileFormat(file.getContentType());

			StoreImageRequest request = new StoreImageRequest();
			request.setUsername(userManagedBean.getActualUser().getUsername());
			request.setGalleryName(this.selectedGallery.getName());
			request.setImage(image);
			request.setImageContent(file.getContents());
			StoreImageResponse storeImage = repositoryService.storeImage(request);
			this.selectedGallery.getImages().add(storeImage.getImage());
			FacesMessage message = new FacesMessage(MessageBundle.getBundle("imaginer.gallery.images.successful_upload"),
							event.getFile().getFileName());
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (ApplicationException e) {
			logger.error(e.getMessage(), e);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", MessageFormat.format(
					MessageBundle.getBundle("imaginer.error.service." + e.getErrorType().getAsString()),
					event.getFile().getFileName()));
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void handleIconUpload(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		this.newGallery.setIcon(file.getContents());
	}

	public String viewGallery() {
		this.selectedGallery.setImages(new ArrayList<>());
		this.selectedImage = null;
		try {
			GetImagesOfGalleryRequest request = new GetImagesOfGalleryRequest();
			request.setGalleryName(this.selectedGallery.getName());
			request.setUsername(this.userManagedBean.getActualUser().getUsername());
			GetImagesOfGalleryResponse response = this.repositoryService.getImagesOfGallery(request);
			this.selectedGallery.getImages().addAll(response.getImages());
			if (!this.selectedGallery.getImages().isEmpty()) {
				this.selectedImage = this.selectedGallery.getImages().get(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "view-gallery";
	}

	public void deleteImage(final ActionEvent event) {
		try {
			DeleteImageRequest request = new DeleteImageRequest();
			request.setUsername(this.userManagedBean.getActualUser().getUsername());
			request.setGalleryName(this.selectedGallery.getName());
			request.setImageName(this.selectedImage.getName());
			this.repositoryService.deleteImage(request);
			this.selectedGallery.getImages().remove(this.selectedImage);
			if (!this.selectedGallery.getImages().isEmpty()) {
				this.selectedImage = this.selectedGallery.getImages().get(0);
			} else {
				this.selectedImage = null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
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

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setUserManagedBean(UserManagedBean userManagedBean) {
		this.userManagedBean = userManagedBean;
	}
}
