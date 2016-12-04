package hu.neuron.imaginer.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.jcr.Binary;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.springframework.stereotype.Service;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.exception.ErrorType;
import hu.neuron.imaginer.gallery.vo.GalleryVO;
import hu.neuron.imaginer.gallery.vo.ImageVO;
import hu.neuron.imaginer.repository.service.RepositoryService;
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
import hu.neuron.imaginer.util.JcrUtil;

@Service
public class RepositoryServiceImpl implements RepositoryService {

	private static final String PROPERTY_IMAGE_BINARY = "image_binary";
	private static final String PROPERTY_FILE_FORMAT = "file_format";
	private static final String PROPERTY_SIZE = "SIZE";

	private static final String PROPERTY_GALLERY_NAME = "gallery_name";
	private static final String PROPERTY_CREATION_DATE = "creation_date";
	private static final String PROPERTY_LAST_MODIFICATION_DATE = "last_modification_date";

	private static final String NODE_USERS = "users";

	@Override
	public StoreImageResponse storeImage(StoreImageRequest request) throws ApplicationException {
		StoreImageResponse response = new StoreImageResponse();
		Session session = null;
		try {
			Repository repository = JcrUtil.getRepository();
			session = JcrUtil.createSession(repository);
			Node user = getUserNode(session, request.getUsername());
			Node gallery = JcrUtils.getNodeIfExists(user, request.getGalleryName());
			if (gallery != null) {
				Node image = JcrUtils.getNodeIfExists(gallery, request.getImage().getName());
				if (image == null) {
					image = gallery.addNode(request.getImage().getName());
					InputStream stream = new ByteArrayInputStream(request.getImageContent());
					Binary imageBinary = session.getValueFactory().createBinary(stream);
					image.setProperty(PROPERTY_IMAGE_BINARY, imageBinary);
					image.setProperty(PROPERTY_FILE_FORMAT, request.getImage().getFileFormat());
					image.setProperty(PROPERTY_SIZE, request.getImageContent().length);

					gallery.setProperty(PROPERTY_LAST_MODIFICATION_DATE, Calendar.getInstance());

					session.save();
					response.setImage(request.getImage());
				} else {
					throw new ApplicationException(ErrorType.NO_SUCH_GALLERY, "No such gallery exists with name: "
							+ request.getGalleryName() + " for user: " + request.getUsername());
				}
			} else {
				throw new ApplicationException(ErrorType.IMAGE_ALREADY_EXISTS,
						"An image with this name: " + request.getImage().getName() + " already exists in this gallery: "
								+ request.getGalleryName() + " for user: " + request.getUsername());
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.FAILED_TO_STORE_IMAGE,
					"Failed to store image: " + request.getImage().getName() + " to gallery: "
							+ request.getGalleryName() + " for user: " + request.getUsername(),
					e);
		} finally {
			JcrUtil.closeSession(session);
		}
		return response;
	}

	@Override
	public GetImageResponse getImage(GetImageRequest request) throws ApplicationException {
		GetImageResponse response = new GetImageResponse();
		Session session = null;
		try {
			Repository repository = JcrUtil.getRepository();
			session = JcrUtil.createSession(repository);
			Node usersNode = getUsersNode(session);
			Node imageNode = JcrUtils.getNodeIfExists(usersNode, request.getPath());
			if (imageNode != null) {
				Binary binary = imageNode.getProperty(PROPERTY_IMAGE_BINARY).getBinary();
				response.setImageContent(IOUtils.toByteArray(binary.getStream()));
			} else {
				throw new ApplicationException(ErrorType.NO_SUCH_IMAGE,
						"No such image found on path: " + request.getPath());
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.FAILED_TO_GET_IMAGE,
					"Failed to get image on path: " + request.getPath(), e);
		} finally {
			JcrUtil.closeSession(session);
		}
		return response;
	}

	@Override
	public DeleteImageResponse deleteImage(DeleteImageRequest request) throws ApplicationException {
		DeleteImageResponse response = new DeleteImageResponse();
		Session session = null;
		try {
			Repository repository = JcrUtil.getRepository();
			session = JcrUtil.createSession(repository);
			Node user = getUserNode(session, request.getUsername());
			Node gallery = JcrUtils.getNodeIfExists(user, request.getGalleryName());
			if (gallery == null) {
				throw new ApplicationException(ErrorType.NO_SUCH_GALLERY,
						"No such gallery: " + request.getGalleryName());
			} else {
				Node image = JcrUtils.getNodeIfExists(gallery, request.getImageName());
				if (image != null) {
					image.remove();
					session.save();
				} else {
					throw new ApplicationException(ErrorType.NO_SUCH_IMAGE,
							"No such image: " + request.getImageName() + " exists in gallery: "
									+ request.getGalleryName() + " for user: " + request.getUsername());
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.FAILED_TO_GET_GALLERY,
					"Failed to get gallery: " + request.getGalleryName() + " for user: " + request.getUsername(), e);
		} finally {
			JcrUtil.closeSession(session);
		}
		return response;
	}

	@Override
	public StoreGalleryResponse storeGallery(final StoreGalleryRequest request) throws ApplicationException {
		StoreGalleryResponse response = new StoreGalleryResponse();
		Session session = null;
		try {
			Repository repository = JcrUtil.getRepository();
			session = JcrUtil.createSession(repository);
			Node user = getUserNode(session, request.getUsername());
			Node gallery = JcrUtils.getNodeIfExists(user, request.getGalleryName());
			if (gallery == null) {
				Calendar now = Calendar.getInstance();
				gallery = user.addNode(request.getGalleryName());
				gallery.setProperty(PROPERTY_GALLERY_NAME, request.getGalleryName());
				gallery.setProperty(PROPERTY_CREATION_DATE, now);
				gallery.setProperty(PROPERTY_LAST_MODIFICATION_DATE, now);
				InputStream stream = new ByteArrayInputStream(request.getGallery().getIcon());
				Binary imageBinary = session.getValueFactory().createBinary(stream);
				gallery.setProperty(PROPERTY_IMAGE_BINARY, imageBinary);

				GalleryVO galleryVO = new GalleryVO();
				galleryVO.setName(request.getGalleryName());
				galleryVO.setLastModificationDate(now.getTime());
				galleryVO.setCreationDate(now.getTime());
				galleryVO.setIcon(request.getIcon());
				galleryVO.setSize(0l);
				response.setGallery(galleryVO);
			} else {
				throw new ApplicationException(ErrorType.GALLERY_ALREADY_EXISTS, "Gallery already exists with name: "
						+ request.getGalleryName() + " for user: " + request.getUsername());
			}
			session.save();
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.FAILED_TO_STORE_GALLERY,
					"Failed to store gallery: " + request.getGalleryName() + " for user: " + request.getUsername(), e);
		} finally {
			JcrUtil.closeSession(session);
		}
		return response;
	}

	@Override
	public GetGalleryResponse getGallery(GetGalleryRequest request) throws ApplicationException {
		GetGalleryResponse response = new GetGalleryResponse();
		Session session = null;
		try {
			Repository repository = JcrUtil.getRepository();
			session = JcrUtil.createSession(repository);
			Node user = getUserNode(session, request.getUsername());
			Node gallery = JcrUtils.getNodeIfExists(user, request.getGalleryName());
			if (gallery == null) {
				throw new ApplicationException(ErrorType.NO_SUCH_GALLERY,
						"No such gallery: " + request.getGalleryName() + " exits for user: " + request.getUsername());
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.FAILED_TO_GET_GALLERY, "Failed to get gallery: "
					+ request.getGalleryName() + " for user: " + request.getUsername() + request.getGalleryName(), e);
		} finally {
			JcrUtil.closeSession(session);
		}
		return response;
	}

	@Override
	public DeleteGalleryResponse deleteGallery(DeleteGalleryRequest request) throws ApplicationException {
		DeleteGalleryResponse response = new DeleteGalleryResponse();
		Session session = null;
		try {
			Repository repository = JcrUtil.getRepository();
			session = JcrUtil.createSession(repository);
			Node user = getUserNode(session, request.getUsername());
			Node gallery = JcrUtils.getNodeIfExists(user, request.getGalleryName());
			if (gallery == null) {
				throw new ApplicationException(ErrorType.NO_SUCH_GALLERY,
						"No such gallery: " + request.getGalleryName() + " for user: " + request.getUsername());
			} else {
				gallery.remove();
				session.save();
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.FAILED_TO_GET_GALLERY, "Failed to get gallery: "
					+ request.getGalleryName() + " for user: " + request.getUsername() + request.getGalleryName(), e);
		} finally {
			JcrUtil.closeSession(session);
		}
		return response;
	}

	@Override
	public GetImagesOfGalleryResponse getImagesOfGallery(GetImagesOfGalleryRequest request)
			throws ApplicationException {
		GetImagesOfGalleryResponse response = new GetImagesOfGalleryResponse();
		Session session = null;
		try {
			Repository repository = JcrUtil.getRepository();
			session = JcrUtil.createSession(repository);
			Node user = getUserNode(session, request.getUsername());
			Node gallery = JcrUtils.getNodeIfExists(user, request.getGalleryName());
			if (gallery != null) {
				NodeIterator imageIterator = gallery.getNodes();
				if (imageIterator.hasNext()) {
					response.setImages(new ArrayList<>());
					while (imageIterator.hasNext()) {
						Node next = imageIterator.nextNode();
						ImageVO image = new ImageVO();
						image.setFileFormat(next.getProperty(PROPERTY_FILE_FORMAT).getString());
						image.setSize(next.getProperty(PROPERTY_SIZE).getLong());
						image.setName(next.getName());
						response.getImages().add(image);
					}
				} else {
					response.setImages(Collections.emptyList());
				}
			} else {
				throw new ApplicationException(ErrorType.NO_SUCH_GALLERY, "No such gallery exists with name: "
						+ request.getGalleryName() + " for user: " + request.getUsername());
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.FAILED_TO_GET_IMAGES, "Failed to get images of gallery: "
					+ request.getGalleryName() + " for user: " + request.getUsername(), e);
		} finally {
			JcrUtil.closeSession(session);
		}
		return response;
	}

	@Override
	public GetGalleriesForUserResponse getGalleriesForUser(GetGalleriesForUserRequest request)
			throws ApplicationException {
		GetGalleriesForUserResponse response = new GetGalleriesForUserResponse();
		Session session = null;
		try {
			Repository repository = JcrUtil.getRepository();
			session = JcrUtil.createSession(repository);
			Node user = getUserNode(session, request.getUsername());
			NodeIterator iterator = user.getNodes();
			if (iterator.hasNext()) {
				response.setGalleries(new ArrayList<>());
				while (iterator.hasNext()) {
					Node next = iterator.nextNode();
					GalleryVO gallery = new GalleryVO();
					gallery.setCreationDate(next.getProperty(PROPERTY_CREATION_DATE).getDate().getTime());
					gallery.setLastModificationDate(
							next.getProperty(PROPERTY_LAST_MODIFICATION_DATE).getDate().getTime());
					gallery.setName(next.getProperty(PROPERTY_GALLERY_NAME).getString());
					long size = 0l;
					NodeIterator imagesIterator = next.getNodes();
					while (imagesIterator.hasNext()) {
						size++;
						imagesIterator.next();
					}
					gallery.setSize(size);
					response.getGalleries().add(gallery);
				}
			} else {
				response.setGalleries(Collections.emptyList());
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.FAILED_TO_GET_GALLERIES,
					"Failed to get galleries for user: " + request.getUsername(), e);
		} finally {
			JcrUtil.closeSession(session);
		}
		return response;
	}

	private Node getUsersNode(final Session session) throws RepositoryException {
		Node rootNode = session.getRootNode();

		Node usersNode = JcrUtils.getNodeIfExists(rootNode, NODE_USERS);
		if (usersNode == null) {
			usersNode = rootNode.addNode(NODE_USERS);
			session.save();
		}
		return usersNode;
	}

	private Node getUserNode(final Session session, final String username) throws ItemExistsException,
			PathNotFoundException, VersionException, ConstraintViolationException, LockException, RepositoryException {
		Node usersNode = getUsersNode(session);

		Node userNode = JcrUtils.getNodeIfExists(usersNode, username);
		if (userNode == null) {
			userNode = usersNode.addNode(username);
			session.save();
		}
		return userNode;
	}
}
