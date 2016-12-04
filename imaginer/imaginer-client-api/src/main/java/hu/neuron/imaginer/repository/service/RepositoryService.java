package hu.neuron.imaginer.repository.service;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.repository.vo.DeleteGalleryRequest;
import hu.neuron.imaginer.repository.vo.DeleteGalleryResponse;
import hu.neuron.imaginer.repository.vo.DeleteImageRequest;
import hu.neuron.imaginer.repository.vo.DeleteImageResponse;
import hu.neuron.imaginer.repository.vo.GetGalleriesForUserRequest;
import hu.neuron.imaginer.repository.vo.GetGalleriesForUserResponse;
import hu.neuron.imaginer.repository.vo.GetGalleryRequest;
import hu.neuron.imaginer.repository.vo.GetGalleryResponse;
import hu.neuron.imaginer.repository.vo.GetImageRequest;
import hu.neuron.imaginer.repository.vo.GetImageResponse;
import hu.neuron.imaginer.repository.vo.GetImagesOfGalleryRequest;
import hu.neuron.imaginer.repository.vo.GetImagesOfGalleryResponse;
import hu.neuron.imaginer.repository.vo.StoreGalleryRequest;
import hu.neuron.imaginer.repository.vo.StoreGalleryResponse;
import hu.neuron.imaginer.repository.vo.StoreImageRequest;
import hu.neuron.imaginer.repository.vo.StoreImageResponse;

public interface RepositoryService {

	public StoreImageResponse storeImage(final StoreImageRequest request) throws ApplicationException;

	public GetImageResponse getImage(final GetImageRequest request) throws ApplicationException;
	
	public DeleteImageResponse deleteImage(final DeleteImageRequest request) throws ApplicationException;

	public StoreGalleryResponse storeGallery(final StoreGalleryRequest request) throws ApplicationException;

	public GetGalleryResponse getGallery(final GetGalleryRequest request) throws ApplicationException;
	
	public DeleteGalleryResponse deleteGallery(final DeleteGalleryRequest request) throws ApplicationException;

	public GetImagesOfGalleryResponse getImagesOfGallery(final GetImagesOfGalleryRequest request)
			throws ApplicationException;

	public GetGalleriesForUserResponse getGalleriesForUser(final GetGalleriesForUserRequest request)
			throws ApplicationException;

}
