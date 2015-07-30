package org.mitre.sfrederiksen;

public class Entry {

	private Integer number = 0;
	private final Integer ADD = 1;
	
	public Entry(int i){
		this.number = i;
	}
	
	public Integer getNumber(){
		return this.number;
	}
	public Integer getAdd(){
		return this.ADD;
	}
}
