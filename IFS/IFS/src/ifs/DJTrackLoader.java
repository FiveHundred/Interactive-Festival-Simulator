package ifs;

import java.io.IOException;

public class DJTrackLoader implements Runnable {

	public DJ djClass;
	public int sountToLoad;
	public String track;
	
	@Override
	public void run() {
		try 
		{
			djClass.loadTrack(sountToLoad, track);
		}
		catch (Exception e)//if theres any other sort of exception
		{
			e.printStackTrace();
			return;//give up and the app will probably crash anyway (unless its an interrupt exception)
		}
	}
}
