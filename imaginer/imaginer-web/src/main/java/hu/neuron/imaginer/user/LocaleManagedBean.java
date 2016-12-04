package hu.neuron.imaginer.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name = "localeManagedBean")
public class LocaleManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Locale locale;
	private List<Locale> locales;

	@PostConstruct
	public void init() {
		this.locales = new ArrayList<>();
		Locale huLocale = new Locale("hu");
		this.locale = huLocale;
		this.locales.add(huLocale);
		this.locales.add(Locale.ENGLISH);
	}

	public String selectLocale() {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(this.locale);
		return FacesContext.getCurrentInstance().getViewRoot().getViewId() + "?faces-redirect=true";
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<Locale> getLocales() {
		return locales;
	}

	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}

}
