package hu.neuron.imaginer.gallery.vo;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ImageVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String fileFormat;
	private Long size;

	public ImageVO() {
		super();
	}

	public ImageVO(String name, String fileFormat, Long size) {
		super();
		this.name = name;
		this.fileFormat = fileFormat;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getFormattedSize() {
		if (this.size <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
}
