package com.workshop.menusehatku;

public class NamaGejala {
	private int _id;
	private String gejala;
	private boolean selected;
	
	public String getGejala(){
		return gejala;
	}
	
	public void setGejala(String gejala){
		this.gejala = gejala;
	}
	
	public int getId(){
		return _id;
	}
	
	public void setId(int _id){
		this._id = _id;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


}