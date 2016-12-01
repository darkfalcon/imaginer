package hu.neuron.imaginer.gallery.vo;

import java.text.DecimalFormat;

public class ImageVO {

	private String name;
	private Long size;
	private String dimension;
	private String fileFormat;

	public ImageVO() {
		super();
	}

	public ImageVO(String name, Long size, String dimension, String fileFormat) {
		super();
		this.name = name;
		this.size = size;
		this.dimension = dimension;
		this.fileFormat = fileFormat;
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
		if(this.size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(this.size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(this.size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

}
