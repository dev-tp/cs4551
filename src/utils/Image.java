package utils;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * A wrapper class of BufferedImage.<br>
 * Provide a couple of utility functions such as reading from and writing to PPM files.<br>
 * This image class is for a 24bit RGB image only.
 * @see BufferedImage
 */
public class Image {

    private BufferedImage bufferedImage;
    private String fileName;

    /**
     * Create an empty image given the width and height
     */
    public Image(int width, int height) {
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        fileName = "";
        System.out.println("Created an empty image with size " + width + "x" + height);
    }

    public Image(int width, int height, int[] background) {
        this(width, height);

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                setPixel(x, y, background);
            }
        }
    }

    public Image(Image image) {
        bufferedImage = image.bufferedImage;
        fileName = image.fileName;
    }

    /**
     * Create a buffer and read data from the image file
     */
    public Image(String fileName) throws Exception {
        this.fileName = fileName;
        readPPM(fileName);
        System.out.println("Created an image from " + fileName + " with size " + getWidth() + "x" + getHeight());
    }

    /**
     * Display the image buffer to the screen
     */
    public void display() {
        String title = fileName;
        JFrame frame = new JFrame(title);
        JLabel label = new JLabel(new ImageIcon(bufferedImage)); // Use a label to display the image
        frame.add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Display the unsigned RGB values for the pixel at the specified coordinate
     * @param x [0, Image Width]
     * @param y [0, Image Height]
     */
    public void displayPixelValue(int x, int y) {
        int pixel = bufferedImage.getRGB(x, y);

        byte b = (byte) pixel;
        byte g = (byte) (pixel >> 8);
        byte r = (byte) (pixel >> 16);

        String info = String.format("RGB Pixel value at (%d, %d): %d, %d, %d", x, y, 0xFF & r, 0xFF & g, 0xFF & b);
        System.out.println(info);
    }

    /**
     * @return The image height
     */
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    /**
     * Retrieve the RGB values at x and y and store them in a byte array
     * @param x [0, Image Width]
     * @param y [0, Image Height]
     * @param rgb Byte array where RGB values will be saved
     */
    public void getPixel(int x, int y, byte[] rgb) {
        int pixel = bufferedImage.getRGB(x, y);

        rgb[2] = (byte) pixel;
        rgb[1] = (byte) (pixel >> 8);
        rgb[0] = (byte) (pixel >> 16);
    }

    /**
     * Retrieve the RGB values at x and y and store them in an integer array
     * @param x [0, Image Width]
     * @param y [0, Image Height]
     * @param rgb Integer array where RGB values will be saved
     */
    public void getPixel(int x, int y, int[] rgb) {
        int pixel = bufferedImage.getRGB(x, y);

        byte b = (byte) pixel;
        byte g = (byte) (pixel >> 8);
        byte r = (byte) (pixel >> 16);

        // converts singed byte value (~128-127) to unsigned byte value (0~255)
        rgb[0] = 0xFF & r;
        rgb[1] = 0xFF & g;
        rgb[2] = 0xFF & b;
    }

    /**
     * @return The image size in bytes
     */
    public int getSize() {
        return getWidth() * getHeight() * 3; // pixel depth is 3
    }

    /**
     * @return The image width
     */
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    /**
     * Set a pixel's RGB value at specific coordinate using byte values
     * @param x [0, Image Width]
     * @param y [0, Image Height]
     * @param rgb An array consisting of three byte values ranging from -128 to 127
     */
    public void setPixel(int x, int y, byte[] rgb) {
        int pixel = 0xFF000000 | ((rgb[0] & 0xFF) << 16) | ((rgb[1] & 0xFF) << 8) | (rgb[2] & 0xFF);
        bufferedImage.setRGB(x, y, pixel);
    }

    /**
     * Set a pixel's RGB value at specific coordinate using integer values
     * @param x [0, Image Width]
     * @param y [0, Image Height]
     * @param irgb An array consisting of three integer values ranging from 0 to 128
     */
    public void setPixel(int x, int y, int[] irgb) {
        byte[] rgb = new byte[3];

        for (int i = 0; i < 3; i++) {
            rgb[i] = (byte) irgb[i];
        }

        setPixel(x, y, rgb);
    }

    /**
     * Read the data from the specified file
     * @param fileName Can be a path or file name
     */
    public void readPPM(String fileName) throws Exception {
        FileImageInputStream imageInputStream = new FileImageInputStream(new File(fileName));

        System.out.println("Reading " + fileName + "...");

        // read identifier
        if (!imageInputStream.readLine().equals("P6")) {
            System.err.println("This is NOT P6 PPM. Wrong Format.");
            System.exit(0);
        }

        imageInputStream.readLine(); // read comment line

        // read width & height
        String[] imageDimensions = imageInputStream.readLine().split(" ");
        int width = Integer.parseInt(imageDimensions[0]);
        int height = Integer.parseInt(imageDimensions[1]);

        // read maximum value
        int maximumValue = Integer.parseInt(imageInputStream.readLine());

        if (maximumValue != 255) {
            System.err.println("Max val is not 255");
            System.exit(0);
        }

        // read binary data byte by byte and save it into BufferedImage object
        byte[] rgb = new byte[3];
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                rgb[0] = imageInputStream.readByte();
                rgb[1] = imageInputStream.readByte();
                rgb[2] = imageInputStream.readByte();
                setPixel(x, y, rgb);
            }
        }

        imageInputStream.close();

        System.out.println("Read " + fileName + " Successfully.");
    }

    /**
     * Flush the image data into a PPM file
     * @param fileName Save the data to a file with the specified name
     */
    public void writeToPPM(String fileName) {
        FileOutputStream outputStream;
        PrintWriter outputWriter;

        try {
            outputStream = new FileOutputStream(fileName);
            outputWriter = new PrintWriter(outputStream);

            System.out.println("Writing the Image buffer into " + fileName + "...");

            // write header
            outputWriter.println("P6");
            outputWriter.println("# " + fileName);
            outputWriter.println(getWidth() + " " + getHeight());
            outputWriter.println(255);
            outputWriter.flush();

            // write data
            byte[] rgb = new byte[3];

            for (int y = 0; y < getHeight(); y++) {
                for (int x = 0; x < getWidth(); x++) {
                    getPixel(x, y, rgb);
                    outputStream.write(rgb[0]);
                    outputStream.write(rgb[1]);
                    outputStream.write(rgb[2]);
                }

                outputStream.flush();
            }

            outputWriter.close();
            outputStream.close();

            System.out.println("Wrote into " + fileName + " Successfully.");
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }
}
