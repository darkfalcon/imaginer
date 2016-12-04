package hu.neuron.imaginer.gallery;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import hu.neuron.imaginer.gallery.vo.GalleryVO;
import hu.neuron.imaginer.gallery.vo.ImageVO;
import hu.neuron.imaginer.user.UserManagedBean;

@RequestScoped
@ManagedBean(name = "imagePathManagedBean")
public class ImagePathManagedBean {

	@ManagedProperty("#{userManagedBean}")
	private UserManagedBean userManagedBean;

	@ManagedProperty("#{galleryManagedBean}")
	private GalleryManagedBean galleryManagedBean;

	public String galleryIconPath(final GalleryVO gallery) {
		return this.userManagedBean.getActualUser().getUsername() + "/" + gallery.getName();
	}

	public String imagePath(final ImageVO image) {
		return this.userManagedBean.getActualUser().getUsername() + "/"
				+ this.galleryManagedBean.getSelectedGallery().getName() + "/" + image.getName();
	}
	
	public void setUserManagedBean(UserManagedBean userManagedBean) {
		this.userManagedBean = userManagedBean;
	}

	public void setGalleryManagedBean(GalleryManagedBean galleryManagedBean) {
		this.galleryManagedBean = galleryManagedBean;
	}
}
