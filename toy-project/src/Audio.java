import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    private Clip clip;
    private AudioInputStream audioInputStream;
    private boolean isLoop;

    public Audio(String pathName, boolean isLoop) {
    	this.isLoop = isLoop;
        try {
            clip = AudioSystem.getClip();
            audioInputStream = AudioSystem.getAudioInputStream(new File(pathName));
            clip.open(audioInputStream);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void start() {
    	 clip.setFramePosition(0);
         clip.start();
         if (isLoop) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
    	 if (clip != null) {
             clip.stop();
             clip.setFramePosition(0); 
         }
    }
    public void setVolume(float volume) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // volume ranges from 0.0 to 1.0
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}