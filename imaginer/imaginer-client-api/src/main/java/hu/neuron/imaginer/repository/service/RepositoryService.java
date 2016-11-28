package hu.neuron.imaginer.repository.service;

import hu.neuron.imaginer.repository.vo.GetContentRequest;
import hu.neuron.imaginer.repository.vo.GetContentResponse;
import hu.neuron.imaginer.repository.vo.StoreContentRequest;
import hu.neuron.imaginer.repository.vo.StoreContentResponse;

public interface RepositoryService {
	
	public StoreContentResponse storeContent(StoreContentRequest request);
	
	public GetContentResponse getContent(GetContentRequest request);

}
