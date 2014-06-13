package ifs;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class SoundEffectsProcessor {
	ShortBuffer sBuffer;//changing this changes the final output sound.
	FloatBuffer fBuffer;//used internally for effects processing.
	
	//this expects the byte buffer to be in the format of a 16bit sample stream, left channel first.
	public SoundEffectsProcessor(ByteBuffer inputTrack)
	{
		sBuffer = inputTrack.asShortBuffer();
		fBuffer = FloatBuffer.allocate(sBuffer.capacity());//allocateDirect is not needed since there's no i/o
		
		//convert the 16bit buffer to a 32bit floating point buffer.
		for(int i = 0; i < sBuffer.capacity(); i++)
		{
			short shrt = Short.reverseBytes(sBuffer.get(i));
			float divisor = shrt < 0 ? 32768f : 32767f;
			float sample = shrt/divisor;
			
			fBuffer.put(i,(float)sample);
		}
	}
	
	public void applyReverb(float delay,float stereoOffset, float decay)
	{
		int baseDelay = 		2*((int)(44100*delay)); //reverb takes 1/4 seconds to bounce back
		int leftRightOffset = 	2*((int)(44100*stereoOffset));// left and right delays differ by this much.
		//float decay = 0.5f;      //and bounces back with 0.5 times the volume.
		
		for(int i = 0; i < fBuffer.capacity(); i++)
		{
			int currentDelay = baseDelay + (i%2 == 0 ? leftRightOffset : -leftRightOffset);
			if(i < fBuffer.capacity()-currentDelay)
			{
				float sample = fBuffer.get(i);
				float sample2 = fBuffer.get(i + currentDelay);
				
				sample2 += sample * decay;
				sample2 = Utility.clamp(sample2,-1f,1f);
				fBuffer.put(i + currentDelay, sample2);
			}
		}
	}
	
	public void applyDistortion()
	{
		for(int i = 0; i < fBuffer.capacity(); i++)
		{
			float sample = fBuffer.get(i);
			boolean negative = sample < 0;
			sample = Math.abs(sample);
			sample = (float) Math.pow(sample,negative ? 0.5f: 1.2f);
			if(negative){sample = -sample;}
			fBuffer.put(i, sample);
		}
	}
	
	public void flush()
	{
		//convert the 32bit floating point buffer back to a 16bit buffer.
		for(int i = 0; i < sBuffer.capacity(); i++)
		{
			float sample = fBuffer.get(i);
			
			sample = Utility.clamp(sample,-1f,1f);//shitty limiter.
			
			float multiplier = sample < 0f ? 32768f : 32767f;
			sBuffer.put(i, Short.reverseBytes((short) (sample * multiplier)));
		}
		
	}
}
