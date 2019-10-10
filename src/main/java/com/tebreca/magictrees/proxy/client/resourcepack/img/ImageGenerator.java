package com.tebreca.magictrees.proxy.client.resourcepack.img;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageGenerator {

	public static Image overlay(Image baseImage, Image overlay, Color color) {
		int width = baseImage.getWidth(null);
		int height = baseImage.getHeight(null);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().drawImage(baseImage, 0, 0, null);
		BufferedImage overlayBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		overlayBuffer.getGraphics().setColor(color);
		overlayBuffer.getGraphics().drawImage(overlay, 0, 0, null);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = overlayBuffer.getRGB(x, y);
				Color baseColor = new Color(rgb);
				float rScale = baseColor.getRed() / 255f;
				float gScale = baseColor.getGreen() / 255f;
				float bScale = baseColor.getBlue() / 255f;
				int red = (int) (color.getRed() * rScale);
				int green = (int) (color.getGreen() * gScale);
				int blue = (int) (color.getBlue() * bScale);
				if (red == 0 && green == 0 && blue == 0)
					continue;
				Color newRgb = new Color(red, green, blue);
				image.setRGB(x, y, newRgb.getRGB());
			}
		}
		return image;
	}

}
