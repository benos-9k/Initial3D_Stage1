package nz.net.initial3d.renderer;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import nz.net.initial3d.DisplayWindow;

public class TestTex {

	public static void main(String[] args) throws Exception {

		Texture2DImpl tex = new Texture2DImpl(256, 256);

		BufferedImage img = ImageIO.read(new File("fractal.jpg"));

		tex.drawImage(img);

		tex.createMipMaps();

		BufferedImage img2 = tex.extractAll();

		DisplayWindow win = DisplayWindow.create(512, 512);

		win.setVisible(true);

		while (true) {
			win.display(img2);
			Thread.sleep(20);
		}

	}

}
