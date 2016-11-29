package hu.neuron.imaginer.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

public class MessageBundle {
	
	public static String getBundle(String key) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ELContext elContext = facesContext.getELContext();
		final Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		ValueExpression exp = expressionFactory.createValueExpression(elContext, "#{msg['" + key + "']}", String.class);
		Object result = exp.getValue(elContext);

		return (String) result;
	}

}
