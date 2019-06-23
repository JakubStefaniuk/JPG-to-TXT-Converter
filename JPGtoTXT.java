package jpgtotxt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Jakub Stefaniuk
 * JPG converter to txt, size will remain unchanged, for viewing high resolution
 * JPG files, smaller font size is advised
 * 
 */
public class JPGtoTXT {
    //image two dimensional array consists of bytes with colors, received from 
    //reading the image, r - rows(file's height), c - columns(file's width),
    //converted - character - destination 2d array
    public static void convertToTxt(byte [][] image, int r, int c, char [][] converted){
        byte mxVal = image[0][0],minVal = image[0][0];
	byte [] colSeparator = new byte [5];
	//initializing highest and smallest color number
        for(int i = 0; i < r; i++){
            for(int j = 0; j < c; j++){
               if(image[i][j] > mxVal){mxVal = image[i][j];}
               if (image[i][j] < minVal){minVal = image[i][j];}
            }
        }
        //each colSeparator points to exact range to change into character 
        byte length = (byte)((mxVal - minVal) / 5);
	colSeparator[0] = (byte)(minVal + length);
	colSeparator[1] = (byte)(minVal+2*length+1);
	colSeparator[2] = (byte)(minVal+3*length+1);
	colSeparator[3] = (byte)(minVal+4*length+1);for(int j = 0; j < c; j++)
	{
		for(int i = 0; i < r; i++){
                    //the darkest
                    if(image[i][j] <= colSeparator[0]){converted[i][j] = '#';}
                    else if(image[i][j] <= colSeparator[1]){converted[i][j] = 'o';}  //  |
                    else if(image[i][j] <= colSeparator[2]){converted[i][j] = ':';}  //  |
                    else if (image[i][j] <= colSeparator[3]){converted[i][j] = '.';} //  v
                    //the brightest
                    else{converted[i][j] = ' ';}
		}
}
    }
    public static void main(String[] args) {
        BufferedImage image = null;
        try{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter JPG file name to convert: ");
        String name = scanner.nextLine();
        image = ImageIO.read(new File(name+".jpg"));
        }
        catch(IOException a){
            System.out.println("Could not find the image");
            System.out.println(a.getMessage());
            return;
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        //reading from JPG file to byte array
        BufferedImage outp = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[width * height];
        byte[][] array = new byte[height][width];
        PixelGrabber pg = new PixelGrabber(image, 0, 0, height, width, pixels, 0, width);
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                 Color c = new Color(image.getRGB(j, i));
                 byte val = (byte)((0.2126*c.getRed() + 0.7152*c.getGreen() + 0.0722*c.getBlue())/3);
                 array[i][j]=val;
            }
        }
        char [][] converted = new char[height][width];
        convertToTxt(array,height,width,converted);
        try{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter destination txt file name: ");
        String name = scanner.nextLine();
        File fOut = new File(name+".txt");
	FileOutputStream outP = new FileOutputStream(fOut);
	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outP));
        //saving to txt file
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                writer.write(converted[i][j]);
            }
            writer.newLine();
        }
        }
        catch(IOException a){
            System.out.println(a.getMessage());
        }
        System.out.println("successfully converted to txt");
    }
}
