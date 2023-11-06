import java.awt.*;
import java.awt.image.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageBoundaryExtraction {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BufferedImage originalImage = ImageIO.read(ImageBoundaryExtraction.class.getResource("aviao.png"));

                BufferedImage eightAdjacencyBoundary = extractBoundaryEightAdjacency(originalImage);
                BufferedImage mAdjacencyBoundary = extractBoundaryMAdjacency(originalImage);

                // Display the resulting images
                displayImages(originalImage, eightAdjacencyBoundary, mAdjacencyBoundary);

                 // Save the resulting images
                saveImage(eightAdjacencyBoundary, "eight_adjacency_boundary.png");
                saveImage(mAdjacencyBoundary, "m_adjacency_boundary.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Extract boundary using 8-adjacency
    private static BufferedImage extractBoundaryEightAdjacency(BufferedImage originalImage) {
        BufferedImage boundaryImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < originalImage.getHeight() - 1; y++) {
            for (int x = 1; x < originalImage.getWidth() - 1; x++) {
                int currentPixel = originalImage.getRGB(x, y);
                int leftPixel = originalImage.getRGB(x - 1, y);
                int rightPixel = originalImage.getRGB(x + 1, y);
                int topPixel = originalImage.getRGB(x, y - 1);
                int bottomPixel = originalImage.getRGB(x, y + 1);

                int average = (getRed(currentPixel) + getGreen(currentPixel) + getBlue(currentPixel)) / 3;

                if (Math.abs(getRed(currentPixel) - getRed(leftPixel)) > 128 ||
                    Math.abs(getGreen(currentPixel) - getGreen(rightPixel)) > 128 ||
                    Math.abs(getBlue(currentPixel) - getBlue(topPixel)) > 128 ||
                    Math.abs(getRed(currentPixel) - getRed(bottomPixel)) > 128) {
                    boundaryImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    boundaryImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        return boundaryImage;
    }

    // Extract boundary using m-adjacency
    private static BufferedImage extractBoundaryMAdjacency(BufferedImage originalImage) {
        BufferedImage boundaryImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < originalImage.getHeight() - 1; y++) {
            for (int x = 1; x < originalImage.getWidth() - 1; x++) {
                int currentPixel = originalImage.getRGB(x, y);
                int leftPixel = originalImage.getRGB(x - 1, y);
                int rightPixel = originalImage.getRGB(x + 1, y);
                int topPixel = originalImage.getRGB(x, y - 1);
                int bottomPixel = originalImage.getRGB(x, y + 1);

                int average = (getRed(currentPixel) + getGreen(currentPixel) + getBlue(currentPixel)) / 3;

                if ((Math.abs(getRed(currentPixel) - getRed(leftPixel)) + 
                    Math.abs(getGreen(currentPixel) - getGreen(rightPixel)) +
                    Math.abs(getBlue(currentPixel) - getBlue(topPixel)) +
                    Math.abs(getRed(currentPixel) - getRed(bottomPixel))) > 128) {
                    boundaryImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    boundaryImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        return boundaryImage;
    }

    // Helper method to extract red component from RGB value
    private static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    // Helper method to extract green component from RGB value
    private static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    // Helper method to extract blue component from RGB value
    private static int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    // Display images using Swing
    private static void displayImages(BufferedImage originalImage, BufferedImage eightAdjacencyBoundary, BufferedImage mAdjacencyBoundary) {
        JFrame frame = new JFrame("Boundary Extraction");
        frame.setLayout(new GridLayout(1, 3));

        frame.add(new JLabel(new ImageIcon(originalImage)));
        frame.add(new JLabel(new ImageIcon(eightAdjacencyBoundary)));
        frame.add(new JLabel(new ImageIcon(mAdjacencyBoundary)));

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

     // Helper method to save the image to disk
    private static void saveImage(BufferedImage image, String filename) {
        try {
            File output = new File(filename);
            ImageIO.write(image, "png", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
