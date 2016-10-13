package caroClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

class ImagePanel extends JPanel{
    Image image;
    public ImagePanel(Image image){
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0,0, this.getWidth(), this.getHeight(), this);
    }
}

public class PlayingUser extends Thread {
	private ImagePanel user;

	public PlayingUser(ImagePanel user) {
		this.user = user;
	}

	@Override
	public void run() {
		while (true) {
			user.setBorder(new LineBorder(Color.ORANGE));
			try {
				Thread.sleep(500);
				user.setBorder(new LineBorder(Color.GREEN));
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
		}
	}
}
