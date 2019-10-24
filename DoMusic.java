

import java.io.*;

import javax.sound.sampled.*;

public class DoMusic extends Thread
{
	public void run() {
		while(true){
		try {
		
		File sf = new File("mario.wav");
		AudioInputStream astr = AudioSystem.getAudioInputStream(sf);
		AudioFormat afmt = astr.getFormat();
		DataLine.Info inf = new DataLine.Info(SourceDataLine.class,afmt);
		SourceDataLine l = (SourceDataLine) AudioSystem.getLine(inf);
		l.open(afmt);
		l.start();
		byte[] buf = new byte[65536];
		
		for( int n=0; (n=astr.read(buf,0,buf.length))>0; )
		{
		l.write(buf,0,n);
		}
		l.drain();
		l.close();
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	}
}
