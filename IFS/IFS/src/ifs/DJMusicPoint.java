package ifs;

public class DJMusicPoint {
	public int sampleNo;
	public byte type;
	public float bpm;
	
	public DJFadeInfo fadeInfo;//if this is a fade point
	
	public boolean triggered = false;
	
	public DJMusicPoint()
	{
	}
	public DJMusicPoint(int sampleNo, byte type, float bpm)
	{
		this.sampleNo = sampleNo;
		this.type = type;
		this.bpm = bpm;
	}
}

/*music point format
 * 
 * first 4 bytes: int containging amount of elements in file
 * 
 * structure:
 * 
 * 4 byte int sample number
 * 1 byte type
 * data
 * 
 * 
 * types:
 * 
 * 0 turn hakk off, has no data
 * 1 turn hakk on, followed by one 4 byte float containing bpm
 * 2 quick transition fade in point, followed by float containing bpm and then int containing fade in length
 * 3 quick transition fade out point, followed by float containing bpm and then int containing fade out length
 * 4 quick transition fade in other track from quick transition fade in point, no data
 */