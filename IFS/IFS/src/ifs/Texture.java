package ifs;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

import static ifs.Utility.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Texture {
	int texid;
	
	public int iwidth;
	public int iheight;
	//public float width;
	//public float height;
	
	public Texture(InputStream in) throws IOException
	{
		texid = glGenTextures();
		
        PNGDecoder decoder = new PNGDecoder(in);
        
        iwidth = decoder.getWidth();
        iheight = decoder.getHeight();

        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();
        
        glBindTexture(GL_TEXTURE_2D, texid);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 4);//afaik this isn't really needed since the decoded format is RGBA which is 4 bytes
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, iwidth, iheight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        
        if(!(IsPowerOfTwo(iwidth)&& IsPowerOfTwo(iheight)))// this shouldn't need to be here, but apparently my gfx card and driver doesn't follow the spec
        {
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }
	}
	
	public void bind()
	{
        glBindTexture(GL_TEXTURE_2D, texid);
	}
	
	public void bind(int tiu)
	{
		glActiveTexture(GL_TEXTURE0+tiu); // specify tiu 1
		glBindTexture(GL_TEXTURE_2D, texid); // specify tbo_tex in tiu 1
		glActiveTexture(GL_TEXTURE0);
	}

}
