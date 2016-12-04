package hu.neuron.imaginer.gallery;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import hu.neuron.imaginer.repository.service.RepositoryService;
import hu.neuron.imaginer.repository.vo.GetImageRequest;

@ApplicationScoped
@ManagedBean(name = "imageManagedBean")
public class ImageManagedBean {
	
	@ManagedProperty("#{repositoryServiceImpl}")
	private RepositoryService repositoryService;

	public byte[] imageContent(String path) {
		GetImageRequest request = new GetImageRequest();
		request.setPath(path);
		return this.repositoryService.getImage(request).getImageContent();
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

}
