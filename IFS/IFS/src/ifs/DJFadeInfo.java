package ifs;

public class DJFadeInfo {
	int startSample = 0;
	int length = 0;
	boolean active = false;
	boolean out = false; // if this is a fade out
	
	public DJFadeInfo()
	{
		this.active = false;
	}
	public DJFadeInfo(int startSample, int length, boolean active, boolean out)
	{
		this.startSample = startSample;
		this.length = length;
		this.active = active;
		this.out = out;
	}
}
