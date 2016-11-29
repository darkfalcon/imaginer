package hu.neuron.imaginer.repository.service;

import javax.jcr.Repository;
import javax.naming.Context;
import javax.naming.InitialContext;

import hu.neuron.imaginer.repository.vo.GetContentRequest;
import hu.neuron.imaginer.repository.vo.GetContentResponse;
import hu.neuron.imaginer.repository.vo.StoreContentRequest;
import hu.neuron.imaginer.repository.vo.StoreContentResponse;

public class RepositoryServiceImpl implements RepositoryService {

	public void init() {

	}

	public StoreContentResponse storeContent(StoreContentRequest request) {
		try {
			InitialContext context = new InitialContext();
			Context environment = (Context) context.lookup("java:comp/env");
			Repository repository = (Repository) environment.lookup("jcr/repository");
			System.out.println("repository");
		} catch (Exception e) {
			
		}
		return null;
	}

	public GetContentResponse getContent(GetContentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
